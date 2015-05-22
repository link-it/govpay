/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.ndp.web.ws;

import java.util.List;
import java.util.Map;

import gov.telematici.pagamenti.ws.ppthead.IntestazionePPT;
import it.gov.digitpa.schemas._2011.ws.psp.EsitoAttivaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.EsitoVerificaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.FaultBean;
import it.gov.digitpa.schemas._2011.ws.psp.PaaAttivaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.PaaAttivaRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.psp.PaaTipoDatiPagamentoPA;
import it.gov.digitpa.schemas._2011.ws.psp.PaaVerificaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.PaaVerificaRPTRisposta;
import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematiciccp.PagamentiTelematiciCCP;
import it.govpay.ejb.controller.AnagraficaEJB;
import it.govpay.ejb.controller.DistintaEJB;
import it.govpay.ejb.exception.GovPayEnteException;
import it.govpay.ejb.exception.GovPayException;
import it.govpay.ndp.ejb.AnagraficaDominioEJB;
import it.govpay.ndp.controller.GdeController;
import it.govpay.ndp.controller.RptController;
import it.govpay.ndp.model.DominioEnteModel;
import it.govpay.ndp.model.EventiInterfacciaModel;
import it.govpay.ndp.pojo.NdpFaultCode;
import it.govpay.ndp.util.GdeUtils;
import it.govpay.ndp.util.NdpUtils;
import it.govpay.ndp.util.exception.GovPayNdpException;
import it.govpay.rs.Pagamento;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

@WebService(serviceName = "PagamentiTelematiciCCPservice",
endpointInterface = "it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematiciccp.PagamentiTelematiciCCP",
targetNamespace = "http://NodoPagamentiSPC.spcoop.gov.it/servizi/PagamentiTelematiciCCP",
portName = "PPTPort")

public class PagamentiTelematiciCCPImpl implements PagamentiTelematiciCCP {
	
	@Inject 
	DistintaEJB distintaEjb;
	
	@Inject 
	AnagraficaDominioEJB anagraficaDominioEjb;
	
	@Inject 
	AnagraficaEJB anagraficaEjb;
	
	@Inject
	RptController rptCtrl;
	
	@Inject
	GdeController gdeCtrl;
	
	@Resource
	WebServiceContext wsCtxt;
	
	Logger log = LogManager.getLogger(PagamentiTelematiciCCPImpl.class);

	@Override
	public PaaAttivaRPTRisposta paaAttivaRPT(PaaAttivaRPT bodyrichiesta, IntestazionePPT header) {
		ThreadContext.put("proc", "AttivaRPT");
		ThreadContext.put("dom", header.getIdentificativoDominio());
		ThreadContext.put("iuv", header.getIdentificativoUnivocoVersamento());
		ThreadContext.put("ccp", header.getCodiceContestoPagamento());
		
		log.info("Ricevuta richiesta di Attiva RPT");
		
		PaaAttivaRPTRisposta risposta = new PaaAttivaRPTRisposta();
		EsitoAttivaRPT esitoAttiva = new EsitoAttivaRPT();
		PaaTipoDatiPagamentoPA datiPagamento = new PaaTipoDatiPagamentoPA();
		FaultBean fault = new FaultBean();
		fault.setId(header.getIdentificativoDominio());
		@SuppressWarnings("unchecked")
		Map<String,List<String>> httpHeaders = (Map<String,List<String>>) wsCtxt.getMessageContext().get(MessageContext.HTTP_REQUEST_HEADERS);
		EventiInterfacciaModel eventi = new EventiInterfacciaModel();
		eventi.setInfospcoop(GdeUtils.creaInfoSPCoop(httpHeaders));
		String nomeSoggettoEnte = "--Informazione SPCoop mancante--";
		if(eventi.getInfospcoop() != null) {
			nomeSoggettoEnte = eventi.getInfospcoop().getSoggettoErogatore();
		}
		
		eventi.getEventi().add(GdeUtils.creaEventoRichiesta(nomeSoggettoEnte, header, GdeUtils.Azione.paaAttivaRPT));

		try {
			
			DominioEnteModel dominioEnte = anagraficaDominioEjb.getDominioEnte(header.getIdentificativoDominio(), header.getIdentificativoIntermediarioPA(), header.getIdentificativoStazioneIntermediarioPA());
			if(dominioEnte == null) {
				log.error("Impossibile l'Ente associato all'idDominio " + header.getIdentificativoDominio());
				fault.setFaultCode(NdpFaultCode.PAA_ID_DOMINIO_ERRATO.name());
				esitoAttiva.setFault(fault);
				esitoAttiva.setEsito("KO");
				risposta.setPaaAttivaRPTRisposta(esitoAttiva);
				return risposta;
			}
			
			Pagamento p = rptCtrl.attivaRPT(dominioEnte, header.getIdentificativoUnivocoVersamento(), header.getCodiceContestoPagamento(), bodyrichiesta);
			datiPagamento.setCausaleVersamento(NdpUtils.buildCausaleSingoloVersamento(p.getDatiVersamento().getIuv(), p.getDatiVersamento().getImportoTotaleDaVersare()));
			datiPagamento.setImportoSingoloVersamento(p.getDatiVersamento().getImportoTotaleDaVersare());
			esitoAttiva.setDatiPagamentoPA(datiPagamento);
			esitoAttiva.setEsito("OK");
			risposta.setPaaAttivaRPTRisposta(esitoAttiva);
			eventi.buildResponses();
			eventi.setEsito(esitoAttiva.getEsito());
			log.info("RPT attivata");
		} catch (GovPayEnteException e) {
			switch (e.getFaultCode()) {
			case PAGAMENTO_ANNULLATO:
				fault.setFaultCode(NdpFaultCode.PAA_PAGAMENTO_ANNULLATO.name());
				break;
			case PAGAMENTO_DUPLICATO:
				fault.setFaultCode(NdpFaultCode.PAA_PAGAMENTO_DUPLICATO.name());
				break;
			case PAGAMENTO_SCADUTO:
				fault.setFaultCode(NdpFaultCode.PAA_PAGAMENTO_SCADUTO.name());
				break;
			case PAGAMENTO_SCONOSCIUTO:
				fault.setFaultCode(NdpFaultCode.PAA_PAGAMENTO_SCONOSCIUTO.name());
				break;
			case RS_ERRORE_HTTP:
			case RS_ERRORE_RETE:
			case RS_RISPOSTA_SCONOSCIUTA:
				fault.setFaultCode(NdpFaultCode.PAA_ERRORE_INTERNO.name());
				break;
			case PAGAMENTO_IN_CORSO:
				fault.setFaultCode(NdpFaultCode.PAA_PAGAMENTO_IN_CORSO.name());
				break;
			}
			
			esitoAttiva.setFault(fault);
			esitoAttiva.setEsito("KO");
			risposta.setPaaAttivaRPTRisposta(esitoAttiva);
			eventi.buildResponses();
			eventi.setEsito(esitoAttiva.getEsito() + ": " + esitoAttiva.getFault().getFaultCode());
			log.error("Errore dell'Ente durante la attivazione del pagamento. " + e.getFaultCode() + ": " + e.getDescrizione());
		} catch (GovPayNdpException e) {
			fault.setFaultCode(e.getFaultCode().name());
			esitoAttiva.setFault(fault);
			esitoAttiva.setEsito("KO");
			risposta.setPaaAttivaRPTRisposta(esitoAttiva);
			eventi.buildResponses();
			eventi.setEsito(esitoAttiva.getEsito() + ": " + esitoAttiva.getFault().getFaultCode());
			log.error("Errore del Nodo durante la attivazione del pagamento. " + esitoAttiva.getEsito() + ": " + esitoAttiva.getFault().getFaultCode() + " - " + e.getDescrizione());
		} catch (GovPayException e) {
			fault.setFaultCode(NdpFaultCode.PAA_ERRORE_INTERNO.name());
			esitoAttiva.setFault(fault);
			esitoAttiva.setEsito("KO");
			risposta.setPaaAttivaRPTRisposta(esitoAttiva);
			eventi.buildResponses();
			eventi.setEsito(esitoAttiva.getEsito() + ": " + esitoAttiva.getFault().getFaultCode());
			log.error("Errore durante la attivazione del pagamento. " + e.getTipoException() + ": " + e.getDescrizione());
		}  finally {
			log.info("Registrazione degli eventi nel Giornale.");
			try {
				gdeCtrl.registraEventi(eventi);
			} catch (Exception e) {
				log.error("Registrazione degli eventi fallita.", e);
			}
		}
		return risposta;
	}

	@Override
	public PaaVerificaRPTRisposta paaVerificaRPT(PaaVerificaRPT bodyrichiesta, IntestazionePPT header) {
		ThreadContext.put("proc", "VerificaRPT");
		ThreadContext.put("dom", header.getIdentificativoDominio());
		ThreadContext.put("iuv", header.getIdentificativoUnivocoVersamento());
		ThreadContext.put("ccp", header.getCodiceContestoPagamento());
		
		log.info("Ricevuta richiesta di verifica RPT");
		
		PaaVerificaRPTRisposta risposta = new PaaVerificaRPTRisposta();
		EsitoVerificaRPT esitoVerifica = new EsitoVerificaRPT();
		PaaTipoDatiPagamentoPA datiPagamento = new PaaTipoDatiPagamentoPA();
		FaultBean fault = new FaultBean();
		fault.setId(header.getIdentificativoDominio());
		
		@SuppressWarnings("unchecked")
		Map<String,List<String>> httpHeaders = (Map<String,List<String>>) wsCtxt.getMessageContext().get(MessageContext.HTTP_REQUEST_HEADERS);
		EventiInterfacciaModel eventi = new EventiInterfacciaModel();
		eventi.setInfospcoop(GdeUtils.creaInfoSPCoop(httpHeaders));
		String nomeSoggettoEnte = "--Informazione SPCoop mancante--";
		if(eventi.getInfospcoop() != null) {
			nomeSoggettoEnte = eventi.getInfospcoop().getSoggettoErogatore();
		}
		
		eventi.getEventi().add(GdeUtils.creaEventoRichiesta(nomeSoggettoEnte, header, GdeUtils.Azione.paaVerificaRPT));

		try {
			DominioEnteModel dominioEnte = anagraficaDominioEjb.getDominioEnte(header.getIdentificativoDominio(), header.getIdentificativoIntermediarioPA(), header.getIdentificativoStazioneIntermediarioPA());
			
			if(dominioEnte == null) {
				log.error("Impossibile l'Ente associato all'idDominio " + header.getIdentificativoDominio());
				fault.setFaultCode(NdpFaultCode.PAA_ID_DOMINIO_ERRATO.name());
				esitoVerifica.setFault(fault);
				esitoVerifica.setEsito("KO");
				risposta.setPaaVerificaRPTRisposta(esitoVerifica);
				return risposta;
			}
			
			Pagamento p = rptCtrl.verificaRPT(dominioEnte, header.getIdentificativoUnivocoVersamento(), header.getCodiceContestoPagamento());
			datiPagamento.setCausaleVersamento(NdpUtils.buildCausaleSingoloVersamento(p.getDatiVersamento().getIuv(), p.getDatiVersamento().getImportoTotaleDaVersare()));
			datiPagamento.setImportoSingoloVersamento(p.getDatiVersamento().getImportoTotaleDaVersare());
			esitoVerifica.setDatiPagamentoPA(datiPagamento);
			esitoVerifica.setEsito("OK");
			risposta.setPaaVerificaRPTRisposta(esitoVerifica);
			eventi.buildResponses();
			eventi.setEsito(esitoVerifica.getEsito());
			log.info("RPT verificata");
		} catch (GovPayEnteException e) {
			switch (e.getFaultCode()) {
			case PAGAMENTO_ANNULLATO:
				fault.setFaultCode(NdpFaultCode.PAA_PAGAMENTO_ANNULLATO.name());
				break;
			case PAGAMENTO_DUPLICATO:
				fault.setFaultCode(NdpFaultCode.PAA_PAGAMENTO_DUPLICATO.name());
				break;
			case PAGAMENTO_SCADUTO:
				fault.setFaultCode(NdpFaultCode.PAA_PAGAMENTO_SCADUTO.name());
				break;
			case PAGAMENTO_SCONOSCIUTO:
				fault.setFaultCode(NdpFaultCode.PAA_PAGAMENTO_SCONOSCIUTO.name());
				break;
			case RS_ERRORE_HTTP:
			case RS_ERRORE_RETE:
			case RS_RISPOSTA_SCONOSCIUTA:
				fault.setFaultCode(NdpFaultCode.PAA_ERRORE_INTERNO.name());
				break;
			case PAGAMENTO_IN_CORSO:
				fault.setFaultCode(NdpFaultCode.PAA_PAGAMENTO_IN_CORSO.name());
				break;
			}
			esitoVerifica.setFault(fault);
			esitoVerifica.setEsito("KO");
			risposta.setPaaVerificaRPTRisposta(esitoVerifica);
			eventi.buildResponses();
			eventi.setEsito(esitoVerifica.getEsito() + ": " + esitoVerifica.getFault().getFaultCode());
			log.error("Errore dell'Ente durante la verifica del pagamento: " + e.getFaultCode().name() + " " + (e.getCause() != null ? e.getCause() : ""));
		} catch (GovPayNdpException e) {
			fault.setFaultCode(e.getFaultCode().name());
			esitoVerifica.setFault(fault);
			esitoVerifica.setEsito("KO");
			risposta.setPaaVerificaRPTRisposta(esitoVerifica);
			eventi.buildResponses();
			eventi.setEsito(esitoVerifica.getEsito() + ": " + esitoVerifica.getFault().getFaultCode());
			log.error("Errore del Nodo durante la verifica del pagamento. " + esitoVerifica.getEsito() + ": " + esitoVerifica.getFault().getFaultCode());
		} catch (GovPayException e) {
			fault.setFaultCode(NdpFaultCode.PAA_ERRORE_INTERNO.name());
			esitoVerifica.setFault(fault);
			esitoVerifica.setEsito("KO");
			risposta.setPaaVerificaRPTRisposta(esitoVerifica);
			eventi.buildResponses();
			eventi.setEsito(esitoVerifica.getEsito() + ": " + esitoVerifica.getFault().getFaultCode());
			log.error("Errore durante la verifica del pagamento: " + e.getTipoException().name() + " " + e.getDescrizione());
		} catch (Exception e) {
			fault.setFaultCode(NdpFaultCode.PAA_ERRORE_INTERNO.name());
			esitoVerifica.setFault(fault);
			esitoVerifica.setEsito("KO");
			risposta.setPaaVerificaRPTRisposta(esitoVerifica);
			eventi.buildResponses();
			eventi.setEsito(esitoVerifica.getEsito() + ": " + esitoVerifica.getFault().getFaultCode());
			log.error("Errore non gestito durante la verifica del pagamento.", e);
		}  finally {
			log.info("Registrazione degli eventi nel Giornale.");
			try {
				gdeCtrl.registraEventi(eventi);
			} catch (Exception e) {
				log.error("Registrazione degli eventi fallita.", e);
			}
		}
		return risposta;
	}
}
