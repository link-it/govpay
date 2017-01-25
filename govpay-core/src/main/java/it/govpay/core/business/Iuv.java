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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.util.CustomIuv;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.model.Versionabile.Versione;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.commons.IuvGenerato;
import it.govpay.servizi.gpapp.GpCaricaIuv;
import it.govpay.servizi.gpapp.GpCaricaIuvResponse;
import it.govpay.servizi.gpapp.GpGeneraIuv;
import it.govpay.servizi.gpapp.GpGeneraIuvResponse;

public class Iuv extends BasicBD {
	
	private static Logger log = LogManager.getLogger();
	
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
	public GpGeneraIuvResponse generaIUV(Applicazione applicazione, GpGeneraIuv gpGeneraIuv, Versione versione) throws GovPayException {
		try {
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(this, gpGeneraIuv.getCodDominio());
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.DOM_000, gpGeneraIuv.getCodDominio());
			}
			
			GpGeneraIuvResponse response = new GpGeneraIuvResponse();
			Exception e = null;
			for(GpGeneraIuv.IuvRichiesto iuvRichiesto : gpGeneraIuv.getIuvRichiesto()) {
				
				it.govpay.model.Iuv iuv = null;
				try {
					iuv = generaIUV(applicazione, dominio, iuvRichiesto.getCodVersamentoEnte());
					IuvGenerato iuvGenerato = IuvUtils.toIuvGenerato(applicazione, dominio, iuv, iuvRichiesto.getImportoTotale(), versione);
					response.getIuvGenerato().add(iuvGenerato);
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
	
	public it.govpay.model.Iuv generaIUV(Applicazione applicazione, Dominio dominio, String codVersamentoEnte) throws GovPayException, ServiceException {
		try {
			// Controllo se e' stata impostata la generazione degli IUV distribuita.
			if(dominio.isCustomIuv()) {
				try {
					if(GovpayConfig.getInstance().getDefaultCustomIuvGenerator().getClass().getMethod("buildIuvNumerico", it.govpay.model.Applicazione.class, it.govpay.model.Dominio.class, long.class).getDeclaringClass().getName().equals(CustomIuv.class.getName()))
						throw new GovPayException("Il dominio [Dominio:" + dominio.getCodDominio() + "] risulta configurato per una generazione decentralizzata degli IUV e non e' stato fornito un plugin per la generazione custom. Non e' quindi possibile avviare una transazione di pagamento se non viene fornito lo IUV da utilizzare.", EsitoOperazione.DOM_002, dominio.getCodDominio());
				} catch (NoSuchMethodException e) {
					throw new GovPayException("Il dominio [Dominio:" + dominio.getCodDominio() + "] risulta configurato per una generazione decentralizzata degli IUV e non e' stato fornito un plugin per la generazione custom. Non e' quindi possibile avviare una transazione di pagamento se non viene fornito lo IUV da utilizzare.", EsitoOperazione.DOM_002, dominio.getCodDominio());
				}
			}
			
			it.govpay.model.Iuv iuv = generaIuv(applicazione, dominio, codVersamentoEnte, it.govpay.model.Iuv.TipoIUV.NUMERICO);
			
			log.debug("Generato IUV [CodDominio: " + dominio.getCodDominio() + "][CodIuv: " + iuv.getIuv() + "]");
			return iuv;
		} catch (GovPayException e) {
			GpThreadLocal.get().log("iuv.generazioneIUVKo", applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getCodDominio(), e.getMessage());
			throw e;
		}
	}
	
	public it.govpay.model.Iuv generaIuv(Applicazione applicazione, Dominio dominio, String codVersamentoEnte, TipoIUV type) throws ServiceException {
		
		// Build prefix
		String prefix = "";
		try {
			prefix = GovpayConfig.getInstance().getDefaultCustomIuvGenerator().buildPrefix(applicazione, dominio, GpThreadLocal.get().getPagamentoCtx().getAllIuvProps(applicazione));
		} catch (ServiceException e) {
			if(dominio.isIuvPrefixStrict()) {
				GpThreadLocal.get().log("iuv.generazioneIUVPrefixFail", dominio.getCodDominio(), applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getIuvPrefix(), e.getMessage(), GpThreadLocal.get().getPagamentoCtx().getAllIuvPropsString(applicazione));
				throw e;
			} else {
				GpThreadLocal.get().log("iuv.generazioneIUVPrefixWarn", dominio.getCodDominio(), applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getIuvPrefix(), e.getMessage(), GpThreadLocal.get().getPagamentoCtx().getAllIuvPropsString(applicazione));
			}
		}
		
		IuvBD iuvBD = new IuvBD(this);
		it.govpay.model.Iuv iuv = iuvBD.generaIuv(applicazione, dominio, codVersamentoEnte, type, prefix);
		
		GpThreadLocal.get().log("iuv.generazioneIUVOk", applicazione.getCodApplicazione(), codVersamentoEnte, dominio.getCodDominio(), iuv.getIuv());
		
		return iuv;
	}	
	

	public GpCaricaIuvResponse caricaIUV(Applicazione applicazione, GpCaricaIuv gpCaricaIuv, Versione versione) throws GovPayException {
		try {
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(this, gpCaricaIuv.getCodDominio());
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.DOM_000, gpCaricaIuv.getCodDominio());
			}
			
			if(!dominio.isCustomIuv()) {
				throw new GovPayException("Il dominio [Dominio:" + gpCaricaIuv.getCodDominio() + "] risulta configurato per la gestione centralizzata degli IUV. Non e' quindi possibile caricare IUV generati esternamente.", EsitoOperazione.DOM_003, gpCaricaIuv.getCodDominio());
			}
			
			GpCaricaIuvResponse response = new GpCaricaIuvResponse();
			Exception e = null;
			for(it.govpay.servizi.gpapp.GpCaricaIuv.IuvGenerato iuvProprietario : gpCaricaIuv.getIuvGenerato()) {
				it.govpay.model.Iuv iuv = null;
				try {
					checkIUV(dominio, iuvProprietario.getIuv(), TipoIUV.NUMERICO);
					iuv = caricaIUV(applicazione, dominio, iuvProprietario.getIuv(), TipoIUV.NUMERICO, iuvProprietario.getCodVersamentoEnte());
				} catch (ServiceException se) {
					e = se;
					continue;
				}
				
				log.info("Caricato IUV [CodDominio: " + dominio.getCodDominio() + "][CodIuv: " + iuv.getIuv() + "]");
				IuvGenerato iuvGenerato = IuvUtils.toIuvGenerato(applicazione, dominio, iuv, iuvProprietario.getImportoTotale(), versione);
				response.getIuvCaricato().add(iuvGenerato);
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
		if(tipo.equals(TipoIUV.NUMERICO) && !IuvUtils.checkIuvNumerico(iuvProposto, dominio.getAuxDigit(), dominio.getStazione(this).getApplicationCode())) {
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
			iuv.setApplicationCode(dominio.getStazione(this).getApplicationCode());
			iuvBD.insertIuv(iuv);
			GpThreadLocal.get().log("iuv.caricamentoIUVOk", applicazione.getCodApplicazione(), iuv.getCodVersamentoEnte(), dominio.getCodDominio(), iuv.getIuv());
		}
		return iuv;
	}

	
}
