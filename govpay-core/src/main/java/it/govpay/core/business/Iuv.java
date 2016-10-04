/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Stazione;
import it.govpay.model.Iuv.TipoIUV;
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
	public GpGeneraIuvResponse generaIUV(Applicazione applicazione, GpGeneraIuv gpGeneraIuv) throws GovPayException {
		try {
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(this, gpGeneraIuv.getCodDominio());
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.DOM_000, gpGeneraIuv.getCodDominio());
			}
			
			if(dominio.isCustomIuv()) {
				throw new GovPayException(EsitoOperazione.DOM_002, gpGeneraIuv.getCodDominio());
			}
			
			Stazione stazione = AnagraficaManager.getStazione(this, dominio.getIdStazione());
			
			GpGeneraIuvResponse response = new GpGeneraIuvResponse();
			IuvBD iuvBD = new IuvBD(this);
			Exception e = null;
			for(GpGeneraIuv.IuvRichiesto iuvRichiesto : gpGeneraIuv.getIuvRichiesto()) {
				
				it.govpay.model.Iuv iuv = null;
				try {
					iuv = iuvBD.generaIuv(applicazione, dominio, iuvRichiesto.getCodVersamentoEnte(), it.govpay.model.Iuv.AUX_DIGIT, stazione.getApplicationCode(), it.govpay.model.Iuv.TipoIUV.NUMERICO);
				} catch (ServiceException se) {
					GpThreadLocal.get().log("iuv.generazioneIUVKo", applicazione.getCodApplicazione(), iuvRichiesto.getCodVersamentoEnte(), dominio.getCodDominio(), e.getMessage());
					e = se;
					continue;
				}
				GpThreadLocal.get().log("iuv.generazioneIUVOk", applicazione.getCodApplicazione(), iuvRichiesto.getCodVersamentoEnte(), dominio.getCodDominio(), iuv.getIuv());
				log.info("Generato IUV [CodDominio: " + dominio.getCodDominio() + "][CodIuv: " + iuv.getIuv() + "]");
				IuvGenerato iuvGenerato = IuvUtils.toIuvGenerato(applicazione, dominio, iuv, iuvRichiesto.getImportoTotale());
				response.getIuvGenerato().add(iuvGenerato);
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

	public GpCaricaIuvResponse caricaIUV(Applicazione applicazione, GpCaricaIuv gpCaricaIuv) throws GovPayException {
		try {
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(this, gpCaricaIuv.getCodDominio());
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.DOM_000, gpCaricaIuv.getCodDominio());
			}
			
			if(!dominio.isCustomIuv()) {
				throw new GovPayException(EsitoOperazione.DOM_003, gpCaricaIuv.getCodDominio());
			}
			
			GpCaricaIuvResponse response = new GpCaricaIuvResponse();
			Exception e = null;
			for(it.govpay.servizi.gpapp.GpCaricaIuv.IuvGenerato iuvProprietario : gpCaricaIuv.getIuvGenerato()) {
				it.govpay.model.Iuv iuv = null;
				try {
					iuv = caricaIUV(applicazione, dominio, iuvProprietario.getIuv(), TipoIUV.NUMERICO, iuvProprietario.getCodVersamentoEnte());
				} catch (ServiceException se) {
					e = se;
					continue;
				}
				
				log.info("Caricato IUV [CodDominio: " + dominio.getCodDominio() + "][CodIuv: " + iuv.getIuv() + "]");
				IuvGenerato iuvGenerato = IuvUtils.toIuvGenerato(applicazione, dominio, iuv, iuvProprietario.getImportoTotale());
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
	
	public it.govpay.model.Iuv caricaIUV(Applicazione applicazione, Dominio dominio,String iuvProposto,  TipoIUV tipo, String codVersamentoEnte) throws GovPayException, ServiceException{
		if(tipo.equals(TipoIUV.NUMERICO) && !IuvUtils.checkIuvNumerico(iuvProposto, it.govpay.model.Iuv.AUX_DIGIT, dominio.getStazione(this).getApplicationCode())) {
			throw new GovPayException(EsitoOperazione.VER_017, iuvProposto);
		}
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
