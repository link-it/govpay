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

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.util.CustomIuv;
import it.govpay.core.business.model.CaricaIuvDTO;
import it.govpay.core.business.model.CaricaIuvDTOResponse;
import it.govpay.core.business.model.GeneraIuvDTO;
import it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto;
import it.govpay.core.business.model.GeneraIuvDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.bd.model.Applicazione;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.servizi.commons.EsitoOperazione;

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
		
		if(dominio.isCustomIuv()) {
			try {
				if(GovpayConfig.getInstance().getDefaultCustomIuvGenerator().getClass().getMethod("buildPrefix", it.govpay.bd.model.Applicazione.class, it.govpay.model.Dominio.class, java.util.Map.class).getDeclaringClass().getName().equals(CustomIuv.class.getName()))
					throw new GovPayException("Il dominio [Dominio:" + dominio.getCodDominio() + "] risulta configurato per una generazione decentralizzata degli IUV e non e' stato fornito un plugin per la generazione custom. Non e' quindi possibile avviare una transazione di pagamento se non viene fornito lo IUV da utilizzare.", EsitoOperazione.DOM_002, dominio.getCodDominio());
			} catch (NoSuchMethodException e) {
				throw new GovPayException("Il dominio [Dominio:" + dominio.getCodDominio() + "] risulta configurato per una generazione decentralizzata degli IUV e non e' stato fornito un plugin per la generazione custom. Non e' quindi possibile avviare una transazione di pagamento se non viene fornito lo IUV da utilizzare.", EsitoOperazione.DOM_002, dominio.getCodDominio());
			}
		}
		
		// Build prefix
		String prefix = GovpayConfig.getInstance().getDefaultCustomIuvGenerator().buildPrefix(applicazione, dominio, GpThreadLocal.get().getPagamentoCtx().getAllIuvProps(applicazione));
		IuvBD iuvBD = new IuvBD(this);
		it.govpay.model.Iuv iuv = null;
		boolean isNumericOnly = GovpayConfig.getInstance().getDefaultCustomIuvGenerator().isNumericOnly(applicazione, dominio, GpThreadLocal.get().getPagamentoCtx().getAllIuvProps(applicazione));;
		
		if(isNumericOnly || type.equals(TipoIUV.NUMERICO)) {
			// il prefisso deve essere numerico
			try {
				Long.parseLong(prefix);
			} catch (NumberFormatException e) {
				if(dominio.isIuvPrefixStrict()) {
					GpThreadLocal.get().log("iuv.generazioneIUVPrefixFail", dominio.getCodDominio(), applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getIuvPrefix(), "Il prefisso generato non e' numerico", GpThreadLocal.get().getPagamentoCtx().getAllIuvPropsString(applicazione));
					throw new ServiceException("Il prefisso generato [" + prefix + "] non e' numerico.");
				} else {
					GpThreadLocal.get().log("iuv.generazioneIUVPrefixWarn", dominio.getCodDominio(), applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getIuvPrefix(), "Il prefisso generato non e' numerico. Prefisso non utilizzato.", GpThreadLocal.get().getPagamentoCtx().getAllIuvPropsString(applicazione));
					prefix = "";
				}
			}
			iuv = iuvBD.generaIuv(applicazione, dominio, codVersamentoEnte, TipoIUV.NUMERICO, prefix);
		} else {
			// Il prefisso deve avere solo caratteri ammissibili
			if(prefix.matches("[a-zA-Z0-9]*"))
				iuv = iuvBD.generaIuv(applicazione, dominio, codVersamentoEnte, TipoIUV.ISO11694, prefix);
			else {
				if(dominio.isIuvPrefixStrict()) {
					GpThreadLocal.get().log("iuv.generazioneIUVPrefixFail", dominio.getCodDominio(), applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getIuvPrefix(), "Il prefisso generato non e' alfanumerico", GpThreadLocal.get().getPagamentoCtx().getAllIuvPropsString(applicazione));
					throw new ServiceException("Il prefisso generato [" + prefix + "] non e' alfanumerico.");
				} else {
					GpThreadLocal.get().log("iuv.generazioneIUVPrefixWarn", dominio.getCodDominio(), applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getIuvPrefix(), "Il prefisso generato non e' alfanumerico. Prefisso non utilizzato.", GpThreadLocal.get().getPagamentoCtx().getAllIuvPropsString(applicazione));
					iuv = iuvBD.generaIuv(applicazione, dominio, codVersamentoEnte, TipoIUV.ISO11694, "");
				}
			}
		}
		
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
			
			if(!dominio.isCustomIuv()) {
				throw new GovPayException("Il dominio [Dominio:" + dto.getCodDominio() + "] risulta configurato per la gestione centralizzata degli IUV. Non e' quindi possibile caricare IUV generati esternamente.", EsitoOperazione.DOM_003, dto.getCodDominio());
			}
			
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
