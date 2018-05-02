/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.core.business;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.core.business.model.CaricaIuvDTO;
import it.govpay.core.business.model.CaricaIuvDTOResponse;
import it.govpay.core.business.model.GeneraIuvDTO;
import it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto;
import it.govpay.core.business.model.GeneraIuvDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.rs.v1.costanti.EsitoOperazione;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.bd.model.Applicazione;
import it.govpay.model.Iuv.TipoIUV;

public class Iuv extends BasicBD {
	
	private static Logger log = LoggerWrapperFactory.getLogger(Iuv.class);
	
	public Iuv(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Genera IUV per avvisi di pagamento.
	 * 
	 * @param applicazioneAutenticata
	 * @param gpGeneraIuv
	 * @return
	 * @throws ServiceException
	 * @throws GovPayException
	 */
	public GeneraIuvDTOResponse generaIUV(GeneraIuvDTO dto) throws GovPayException {
		try {
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(this, dto.getCodDominio());
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.DOM_000, dto.getCodDominio());
			}
			
			GeneraIuvDTOResponse response = new GeneraIuvDTOResponse();
			Exception e = null;
			
			IuvBD iuvBD = new IuvBD(this);
			for(IuvRichiesto iuvRichiesto : dto.getIuvRichiesto()) {
				
				it.govpay.model.Iuv iuv = null;
				try {
					try {
						iuv = iuvBD.getIuv(dto.getApplicazioneAutenticata().getId(), iuvRichiesto.getCodVersamentoEnte(), TipoIUV.NUMERICO);
						it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(dto.getApplicazioneAutenticata(), dominio, iuv, iuvRichiesto.getImportoTotale());
						response.getIuvGenerato().add(iuvGenerato);
					} catch (NotFoundException nfe) {
						iuv = this.generaIUV(dto.getApplicazioneAutenticata(), dominio, iuvRichiesto.getCodVersamentoEnte(), TipoIUV.NUMERICO);
						it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(dto.getApplicazioneAutenticata(), dominio, iuv, iuvRichiesto.getImportoTotale());
						response.getIuvGenerato().add(iuvGenerato);
					}
				} catch (ServiceException se) {
					e = se;
					continue;
				}
			}
			
			// Se non ho generato nessun IUV per colpa di errori interni, 
			// ritorno l'ultimo errore riscontato.
			if(response.getIuvGenerato().size() == 0)
				throw e;
			
			return response;
		} catch (Exception e) {
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			else 
				throw new GovPayException(e);
		}
	}
	
	public it.govpay.model.Iuv generaIUV(Applicazione applicazione, Dominio dominio, String codVersamentoEnte, TipoIUV type) throws GovPayException, ServiceException {
		
		// Build prefix
		String prefix = GovpayConfig.getInstance().getDefaultCustomIuvGenerator().buildPrefix(applicazione, dominio, GpThreadLocal.get().getPagamentoCtx().getAllIuvProps(applicazione));
		IuvBD iuvBD = new IuvBD(this);
		it.govpay.model.Iuv iuv = null;
		
		if(type.equals(TipoIUV.NUMERICO)) {
			// il prefisso deve essere numerico
			if(StringUtils.isNotEmpty(prefix)) { 
				try {
					Long.parseLong(prefix);
				} catch (NumberFormatException e) {
					GpThreadLocal.get().log("iuv.generazioneIUVPrefixFail", dominio.getCodDominio(), applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getIuvPrefix(), "Il prefisso generato non e' numerico", GpThreadLocal.get().getPagamentoCtx().getAllIuvPropsString(applicazione));
					throw new ServiceException("Il prefisso generato [" + prefix + "] non e' numerico.");
				}
			}
			iuv = iuvBD.generaIuv(applicazione, dominio, codVersamentoEnte, TipoIUV.NUMERICO, prefix);
		} else {
			// Il prefisso deve avere solo caratteri ammissibili
			if(prefix.matches("[a-zA-Z0-9]*"))
				iuv = iuvBD.generaIuv(applicazione, dominio, codVersamentoEnte, TipoIUV.ISO11694, prefix);
			else {
				GpThreadLocal.get().log("iuv.generazioneIUVPrefixFail", dominio.getCodDominio(), applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getIuvPrefix(), "Il prefisso generato non e' alfanumerico", GpThreadLocal.get().getPagamentoCtx().getAllIuvPropsString(applicazione));
				throw new ServiceException("Il prefisso generato [" + prefix + "] non e' alfanumerico.");
			}
		}
		
		// Verifico che lo iuv generato rispetti la regular expr definita nell'applicazione
		Pattern patternIuv = Pattern.compile(applicazione.getRegExp());
		Matcher matcher = patternIuv.matcher(iuv.getIuv()); 
		if(!matcher.matches())
			throw new ServiceException("Lo iuv generato [" + prefix + "] non rispetta il pattern previsto dall'applicazione ["+applicazione.getCodApplicazione()+"].");
		
		GpThreadLocal.get().log("iuv.generazioneIUVOk", applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getCodDominio(), iuv.getIuv());
		
		return iuv;
	}	
	

	public CaricaIuvDTOResponse caricaIUV(CaricaIuvDTO dto) throws GovPayException {
		try {
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(this, dto.getCodDominio());
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.DOM_000, dto.getCodDominio());
			}
			
//			if(!dominio.isCustomIuv()) {
//				throw new GovPayException("Il dominio [Dominio:" + dto.getCodDominio() + "] risulta configurato per la gestione centralizzata degli IUV. Non e' quindi possibile caricare IUV generati esternamente.", EsitoOperazione.DOM_003, dto.getCodDominio());
//			}
			
			CaricaIuvDTOResponse response = new CaricaIuvDTOResponse();
			Exception e = null;
			for(CaricaIuvDTO.Iuv iuvDaCaricare : dto.getIuvDaCaricare()) {
				it.govpay.model.Iuv iuv = null;
				try {
					checkIUV(dominio, iuvDaCaricare.getIuv(), TipoIUV.NUMERICO);
					iuv = caricaIUV(dto.getApplicazioneAutenticata(), dominio, iuvDaCaricare.getIuv(), TipoIUV.NUMERICO, iuvDaCaricare.getCodVersamentoEnte());
				} catch (ServiceException se) {
					e = se;
					continue;
				}
				
				log.info("Caricato IUV [CodDominio: " + dominio.getCodDominio() + "][CodIuv: " + iuv.getIuv() + "]");
				it.govpay.core.business.model.Iuv iuvCaricato = IuvUtils.toIuv(dto.getApplicazioneAutenticata(), dominio, iuv, iuvDaCaricare.getImportoTotale());
				response.getIuvCaricato().add(iuvCaricato);
			}
			
			// Se non ho generato nessun IUV per colpa di errori interni, 
			// ritorno l'ultimo errore riscontato.
			if(response.getIuvCaricato().size() == 0)
				throw e;
			
			return response;
		} catch (Exception e) {
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			else 
				throw new GovPayException(e);
		}
	}
	
	public TipoIUV getTipoIUV(String iuvProposto) {
		try {
			Long.parseLong(iuvProposto);
		} catch (NumberFormatException e) {
			return TipoIUV.ISO11694;
		}
		return TipoIUV.NUMERICO;
	}
	
	public void checkIUV(Dominio dominio, String iuvProposto, TipoIUV tipo) throws GovPayException, ServiceException {
		if(tipo.equals(TipoIUV.NUMERICO) && !IuvUtils.checkIuvNumerico(iuvProposto, dominio.getAuxDigit(), dominio.getStazione().getApplicationCode())) {
			throw new GovPayException(EsitoOperazione.VER_017, iuvProposto);
		}
	}
	
	public it.govpay.model.Iuv caricaIUV(Applicazione applicazione, Dominio dominio, String iuvProposto, TipoIUV tipo, String codVersamentoEnte) throws GovPayException, ServiceException{
		it.govpay.model.Iuv iuv = null;
		IuvBD iuvBD = new IuvBD(this);
		// Controllo se esiste gia'
		try {
			iuv = iuvBD.getIuv(dominio.getId(), iuvProposto);
			if(!iuv.getCodVersamentoEnte().equals(codVersamentoEnte)) {
				GpThreadLocal.get().log("iuv.caricamentoIUVKo", applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getCodDominio(), iuvProposto);
				throw new GovPayException(EsitoOperazione.VER_018, iuvProposto, iuv.getCodVersamentoEnte());
			}
		} catch (NotFoundException ne) {
			// Non esiste, lo carico 
			iuv = new it.govpay.model.Iuv();
			iuv.setIdDominio(dominio.getId());
			iuv.setPrg(0);
			iuv.setIuv(iuvProposto);
			iuv.setDataGenerazione(new Date());
			iuv.setIdApplicazione(applicazione.getId());
			iuv.setTipo(tipo);
			iuv.setCodVersamentoEnte(codVersamentoEnte);
			iuv.setApplicationCode(dominio.getStazione().getApplicationCode());
			iuvBD.insertIuv(iuv);
			GpThreadLocal.get().log("iuv.caricamentoIUVOk", applicazione.getCodApplicazione(), iuv.getCodVersamentoEnte(), dominio.getCodDominio(), iuv.getIuv());
		}
		return iuv;
	}

	
}
