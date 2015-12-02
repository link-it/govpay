/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.web.ws;


import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.PspBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.Iuv;
import it.govpay.bd.model.Portale;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rt;
import it.govpay.bd.model.Psp.ModelloPagamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.bd.model.Rpt.FirmaRichiesta;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RtBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.VersamentiBD.TipoIUV;
import it.govpay.business.Autorizzazione;
import it.govpay.business.Pagamenti;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.servizi.pa.CodErrore;
import it.govpay.servizi.pa.CodEsito;
import it.govpay.servizi.pa.EsitoOperazione;
import it.govpay.servizi.pa.GpChiediStatoPagamento;
import it.govpay.servizi.pa.GpChiediStatoPagamentoResponse;
import it.govpay.servizi.pa.GpGeneraRpt;
import it.govpay.servizi.pa.GpGeneraRptResponse;
import it.govpay.servizi.pa.GpInviaRpt;
import it.govpay.servizi.pa.GpInviaRptResponse;
import it.govpay.servizi.pa.GpInviaRr;
import it.govpay.servizi.pa.IdPagamento;
import it.govpay.servizi.pa.PagamentiTelematiciGPPrt;
import it.govpay.servizi.pa.Pagamento;
import it.govpay.web.ws.utils.PagamentiTelematiciGPUtil;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.commons.lang.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

@WebService(serviceName = "PagamentiTelematiciGPPrtservice",
endpointInterface = "it.govpay.servizi.pa.PagamentiTelematiciGPPrt",
targetNamespace = "http://www.govpay.it/servizi/PagamentiTelematiciGP",
portName = "GPPort")

@HandlerChain(file="../../../../handler-chains/handler-chain.xml")
public class PagamentiTelematiciGPPrtImpl implements PagamentiTelematiciGPPrt {
	
	@Resource
	WebServiceContext wsCtxt;
	
	Logger log = LogManager.getLogger();
	
	@Override
	public GpInviaRptResponse gpInviaRpt(GpInviaRpt bodyrichiesta) {
		log.info("Ricevuta richiesta di InviaRpt");
		
		GpInviaRptResponse esitoOperazione = new GpInviaRptResponse();
		esitoOperazione.setCodPortale(bodyrichiesta.getCodPortale());
		esitoOperazione.setCodOperazione(ThreadContext.get("op"));
		esitoOperazione.getIdPagamento().addAll(bodyrichiesta.getIdPagamento());
		
		BasicBD bd = null;
		try {
			
			try {
				bd = BasicBD.getInstance();
			} catch (ServiceException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			
			Portale portale = new Autorizzazione(bd).authPortale(wsCtxt.getUserPrincipal(), bodyrichiesta.getCodPortale());
			
			Canale canale = null;
			// Se indicato, recupero il PSP scelto per il pagamento
			if(bodyrichiesta.getCanale() != null) {
				Psp psp = null;
				try {
					psp = AnagraficaManager.getPsp(bd, bodyrichiesta.getCanale().getCodPsp());
				} catch (NotFoundException nfe) {
					throw new GovPayException(GovPayExceptionEnum.PSP_NON_TROVATO);
				} 
				
				// Cerco il canale indicato
				for(Canale c : psp.getCanali()) {
					if(c.getTipoVersamento().equals(PagamentiTelematiciGPUtil.toOrm(bodyrichiesta.getCanale().getTipoVersamento()))) {
						canale = c;
						break;
					}
				}
				if(canale == null) {
					throw new GovPayException(GovPayExceptionEnum.PSP_NON_TROVATO);
				}
			} else {
				// TODO Redirezione al WebPay per la scelta del Psp.
				throw new NotImplementedException();
			}
			
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			List<Long> versamenti = new ArrayList<Long>();
			
			for(IdPagamento idPagamento : bodyrichiesta.getIdPagamento() ) {
				Ente ente = null;
				try {
					ente = AnagraficaManager.getEnte(bd, idPagamento.getCodEnte());
				} catch (NotFoundException e){
					log.error("Ente [codEnte: " + idPagamento.getCodEnte() + "] non censito in Anagrafica Enti.");
					throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
				} 
				
				try { 
					versamenti.add(versamentiBD.getVersamento(ente.getId(), idPagamento.getIuv()).getId());
				} catch (NotFoundException nfe) {
					throw new GovPayException(GovPayExceptionEnum.IUV_NON_TROVATO);
				}
			}
			
			//Effettuo il pagamento
			Pagamenti pagamenti = new Pagamenti(bd);
			String url = null;
			if(versamenti.size() == 1)
				url = pagamenti.eseguiPagamentoEnte(portale.getId(), versamenti.get(0), canale.getId(), bodyrichiesta.getIbanAddebito(), FirmaRichiesta.NESSUNA, PagamentiTelematiciGPUtil.toOrm(bodyrichiesta.getAutenticazione()), PagamentiTelematiciGPUtil.toOrm(bodyrichiesta.getVersante()), bodyrichiesta.getCallbackUrl());
			else
				url = pagamenti.eseguiPagamentoCarrelloEnte(portale.getId(), versamenti, canale.getId(), bodyrichiesta.getIbanAddebito(), FirmaRichiesta.NESSUNA, PagamentiTelematiciGPUtil.toOrm(bodyrichiesta.getAutenticazione()), PagamentiTelematiciGPUtil.toOrm(bodyrichiesta.getVersante()), bodyrichiesta.getCallbackUrl());
			esitoOperazione.setUrl(url);
			esitoOperazione.setCodEsito(CodEsito.OK);
			return esitoOperazione;
		} catch (GovPayException e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(PagamentiTelematiciGPUtil.toDescrizioneEsito(e.getTipoException()));
			if(e.getTipoException().equals(GovPayExceptionEnum.ERRORE_INTERNO))
				log.error("Errore durante il pagamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			else
				log.error("Errore durante il pagamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]");
			return esitoOperazione;
		} catch (Exception e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(CodErrore.ERRORE_INTERNO);
			log.error("Errore durante il pagamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			return esitoOperazione;
		} finally {
			bd.closeConnection();
		}
	}
	
	@Override
	public EsitoOperazione gpInviaRr(GpInviaRr bodyrichiesta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GpChiediStatoPagamentoResponse gpChiediStatoPagamento(GpChiediStatoPagamento bodyrichiesta) {
		log.info("Ricevuta richiesta di gpChiediStatoPagamento.");
		
		GpChiediStatoPagamentoResponse esitoOperazione = new GpChiediStatoPagamentoResponse();
		esitoOperazione.setCodPortale(bodyrichiesta.getCodPortale());
		esitoOperazione.setCodOperazione(ThreadContext.get("op"));
		
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}

			Portale portale = new Autorizzazione(bd).authPortale(wsCtxt.getUserPrincipal(), bodyrichiesta.getCodPortale());
			log.info("Identificazione Portale avvenuta con successo [CodPortale: " + portale.getCodPortale() + "]");
			
			Ente ente = null;
			try {
				ente = AnagraficaManager.getEnte(bd, bodyrichiesta.getIdPagamento().getCodEnte());
			} catch (NotFoundException e){
				log.error("Ente [codEnte: " + bodyrichiesta.getIdPagamento().getCodEnte() + "] non censito in Anagrafica Enti.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 
			
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, ente.getIdDominio());
			} catch (NotFoundException e){
				log.error("Dominio [idDominio: " + ente.getIdDominio() + "] associato all'Ente [codEnte: " + ente.getCodEnte() + " non censito in Anagrafica Domini.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			} 

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			Versamento versamento = null;
			try {
				versamento = versamentiBD.getVersamento(dominio.getCodDominio(), bodyrichiesta.getIdPagamento().getIuv());
			} catch (Exception e){
				throw new GovPayException(GovPayExceptionEnum.IUV_NON_TROVATO);
			} 
			
			Applicazione applicazione = null;
			try {
				applicazione = AnagraficaManager.getApplicazione(bd, versamento.getIdApplicazione());
			} catch (NotFoundException e){
				log.error("Applicazione [codApplicazione: " + bodyrichiesta.getCodApplicazione() + "] non censito in Anagrafica Applicazioni.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
			}
			
			RptBD rptBD = new RptBD(bd);
			Rpt rpt = null;
			try {
				rpt = rptBD.getLastRpt(versamento.getId());
				//Verifico lo stato RPT
				if(new Pagamenti(bd).aggiornaRptDaNpD(rpt)) {
					versamento = versamentiBD.getVersamento(versamento.getId());
				} else {
					bd.setupConnection(); //TODO corretto reinizializzarla qui?
				}
			} catch (Exception e){
				return PagamentiTelematiciGPUtil.toGpChiediStatoPagamentoResponse(bd, esitoOperazione, applicazione, ente, dominio, versamento, null, null);
			} 
			
			RtBD rtBD = new RtBD(bd);
			Rt rt = null;
			byte[] rtByte = null;
			try {
				rt = rtBD.getLastRt(rpt.getId());
				TracciatiBD tracciatiBD = new TracciatiBD(bd);
				rtByte = tracciatiBD.getTracciato(rt.getIdTracciatoXML());
			} catch (Exception e){
				
			} 
			log.info("Verifica effettuata. Pagamento in stato: " + versamento.getStato());
			return PagamentiTelematiciGPUtil.toGpChiediStatoPagamentoResponse(bd, esitoOperazione, applicazione, ente, dominio,versamento, rpt, rtByte);
		} catch (GovPayException e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(PagamentiTelematiciGPUtil.toDescrizioneEsito(e.getTipoException()));
			if(e.getTipoException().equals(GovPayExceptionEnum.ERRORE_INTERNO))
				log.error("Errore durante il pagamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			else
				log.error("Errore durante il pagamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]");
			return esitoOperazione;
		} catch (Exception e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(CodErrore.ERRORE_INTERNO);
			log.error("Errore durante il pagamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			return esitoOperazione;
		} finally {
			bd.closeConnection();
		}
	}

	@Override
	public GpGeneraRptResponse gpGeneraRpt(GpGeneraRpt bodyrichiesta) {
		log.info("Ricevuta richiesta di Esecuzione Pagamento.");

		GpGeneraRptResponse esitoOperazione = new GpGeneraRptResponse();
		esitoOperazione.setCodPortale(bodyrichiesta.getCodPortale());
		esitoOperazione.setCodOperazione(ThreadContext.get("op"));
		
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}

			Portale portale = new Autorizzazione(bd).authPortale(wsCtxt.getUserPrincipal(), bodyrichiesta.getCodPortale());

			log.info("Identificazione Portale avvenuta con successo [CodPortale: " + portale.getCodPortale() + "]");
			
			Psp psp = new PspBD(bd).getPsp(bodyrichiesta.getCanale().getCodPsp());
			
			ModelloPagamento modello = bodyrichiesta.getPagamento().size() == 1 ? null : ModelloPagamento.IMMEDIATO_MULTIBENEFICIARIO;
			
			Canale canale = psp.getCanale(PagamentiTelematiciGPUtil.toTipoVersamento(bodyrichiesta.getCanale().getTipoVersamento()), modello);
			
			if(canale == null) throw new GovPayException(GovPayExceptionEnum.PSP_NON_TROVATO);
			
			List<Long> versamenti = new ArrayList<Long>();
			
			for(Pagamento pagamento : bodyrichiesta.getPagamento()) {
				
				Applicazione applicazione = null;
				try {
					applicazione = AnagraficaManager.getApplicazione(bd, pagamento.getCodApplicazione());
				} catch (NotFoundException e){
					log.error("Applicazione [codApplicazione: " + pagamento.getCodApplicazione() + "] non censito in Anagrafica Applicazioni.");
					throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
				}
				
				Ente ente = null;
				try {
					ente = AnagraficaManager.getEnte(bd, pagamento.getCodEnte());
				} catch (NotFoundException e){
					log.error("Ente [codEnte: " + pagamento.getCodEnte() + "] non censito in Anagrafica Enti.");
					throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
				} 
				
				Dominio dominio = null;
				try {
					dominio = AnagraficaManager.getDominio(bd, ente.getIdDominio());
				} catch (NotFoundException e){
					log.error("Dominio [idDominio: " + ente.getIdDominio() + "] associato all'Ente [codEnte: " + ente.getCodEnte() + " non censito in Anagrafica Domini.");
					throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO);
				} 
				
				Pagamenti pagamenti = new Pagamenti(bd);
			
				String iuv = pagamento.getIuv();
				if(iuv == null) {
					switch (pagamento.getTipoIuv()) {
					case IUV_INIZIATIVA_ENTE:
						iuv = pagamenti.generaIuv(applicazione.getId(), dominio.getCodDominio(), TipoIUV.ISO11694, Iuv.AUX_DIGIT);
						break;
		
					case IUV_INIZIATIVA_PSP:
						iuv = pagamenti.generaIuv(applicazione.getId(), dominio.getCodDominio(), TipoIUV.NUMERICO, Iuv.AUX_DIGIT);
						break;
					}
					log.info("Assegnato al Pagamento in Attesa lo IUV [" + iuv + "]");
				} 
				
				IdPagamento idPagamento = new IdPagamento();
				idPagamento.setCodEnte(pagamento.getCodEnte());
				idPagamento.setIuv(iuv);
				esitoOperazione.getIdPagamento().add(idPagamento);
				
				Versamento versamento = PagamentiTelematiciGPUtil.toVersamento(pagamento, ente, dominio, applicazione, iuv, bd);
				pagamenti.caricaPagamento(versamento);
				versamenti.add(versamento.getId());
			}
			
			log.info("Esecuzione del pagamento in corso...");
			//Effettuo il pagamento
			Pagamenti pagamenti = new Pagamenti(bd);
			String url = null;
			if(versamenti.size() == 1)
				url = pagamenti.eseguiPagamentoEnte(portale.getId(), versamenti.get(0), canale.getId(), bodyrichiesta.getIbanAddebito(), FirmaRichiesta.NESSUNA, PagamentiTelematiciGPUtil.toOrm(bodyrichiesta.getAutenticazione()), PagamentiTelematiciGPUtil.toOrm(bodyrichiesta.getVersante()), bodyrichiesta.getCallbackUrl());
			else
				url = pagamenti.eseguiPagamentoCarrelloEnte(portale.getId(), versamenti, canale.getId(), bodyrichiesta.getIbanAddebito(), FirmaRichiesta.NESSUNA, PagamentiTelematiciGPUtil.toOrm(bodyrichiesta.getAutenticazione()), PagamentiTelematiciGPUtil.toOrm(bodyrichiesta.getVersante()), bodyrichiesta.getCallbackUrl());
			esitoOperazione.setUrl(url);
			esitoOperazione.setCodEsito(CodEsito.OK);
			return esitoOperazione;
		} catch (GovPayException e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(PagamentiTelematiciGPUtil.toDescrizioneEsito(e.getTipoException()));
			if(e.getTipoException().equals(GovPayExceptionEnum.ERRORE_INTERNO))
				log.error("Errore durante il pagamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			else
				log.error("Errore durante il pagamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]");
			return esitoOperazione;
		} catch (Exception e) {
			esitoOperazione.setCodEsito(CodEsito.KO);
			esitoOperazione.setCodErrore(CodErrore.ERRORE_INTERNO);
			log.error("Errore durante il pagamento. Ritorno esito [" + esitoOperazione.getCodErrore() + "]", e);
			return esitoOperazione;
		} finally {
			bd.closeConnection();
		}
	}
}
