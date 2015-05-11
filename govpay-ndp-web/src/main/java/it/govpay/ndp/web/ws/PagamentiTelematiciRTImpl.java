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
import it.gov.digitpa.schemas._2011.ws.nodo.EsitoPaaInviaRT;
import it.gov.digitpa.schemas._2011.ws.nodo.PaaInviaRT;
import it.gov.digitpa.schemas._2011.ws.nodo.PaaInviaRTRisposta;
import it.gov.digitpa.schemas._2011.ws.nodo.TipoInviaEsitoStornoRisposta;
import it.gov.digitpa.schemas._2011.ws.nodo.FaultBean;
import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirt.PagamentiTelematiciRT;
import it.govpay.ndp.controller.GdeController;
import it.govpay.ndp.controller.RrController;
import it.govpay.ndp.controller.RtController;
import it.govpay.ndp.model.EventiInterfacciaModel;
import it.govpay.ndp.pojo.NdpFaultCode;
import it.govpay.ndp.util.GdeUtils;
import it.govpay.ndp.util.exception.GovPayNdpException;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

@WebService(serviceName = "PagamentiTelematiciRTservice",
endpointInterface = "it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirt.PagamentiTelematiciRT",
targetNamespace = "http://NodoPagamentiSPC.spcoop.gov.it/servizi/PagamentiTelematiciRT",
portName = "PPTPort")

public class PagamentiTelematiciRTImpl implements PagamentiTelematiciRT {

	@Inject
	RtController rtCtrl;
	
	@Inject
	RrController rrCtrl;

	@Inject
	GdeController gdeCtrl;

	@Resource
	WebServiceContext wsCtxt;

	Logger log = LogManager.getLogger(PagamentiTelematiciRTImpl.class);

	@Override
	public TipoInviaEsitoStornoRisposta paaInviaEsitoStorno(
			String identificativoIntermediarioPA,
			String identificativoStazioneIntermediarioPA,
			String identificativoDominio,
			String identificativoUnivocoVersamento,
			String codiceContestoPagamento, byte[] er) {
		// TODO Gestire l'esito dello storno
		ThreadContext.put("proc", "InviaEsitoStorno");
		ThreadContext.put("dom", identificativoDominio);
		ThreadContext.put("iuv", identificativoUnivocoVersamento);
		ThreadContext.put("ccp", codiceContestoPagamento);
		log.info("Ricevuta esito richiesta di Storno");
		
		FaultBean fault = new FaultBean();
		fault.setId(identificativoDominio);
		
		@SuppressWarnings("unchecked")
		Map<String,List<String>> httpHeaders = (Map<String,List<String>>) wsCtxt.getMessageContext().get(MessageContext.HTTP_REQUEST_HEADERS);
		EventiInterfacciaModel eventi = new EventiInterfacciaModel();
		eventi.setInfospcoop(GdeUtils.creaInfoSPCoop(httpHeaders));
		String nomeSoggettoEnte = "--Informazione SPCoop mancante--";
		if(eventi.getInfospcoop() != null) {
			nomeSoggettoEnte = eventi.getInfospcoop().getSoggettoErogatore();
		}
		
		IntestazionePPT header = new IntestazionePPT();
		header.setIdentificativoDominio(identificativoDominio);
		header.setIdentificativoUnivocoVersamento(identificativoUnivocoVersamento);
		header.setCodiceContestoPagamento(codiceContestoPagamento);
		header.setIdentificativoIntermediarioPA(identificativoIntermediarioPA);
		header.setIdentificativoStazioneIntermediarioPA(identificativoStazioneIntermediarioPA);
		
		eventi.getEventi().add(GdeUtils.creaEventoRichiesta(nomeSoggettoEnte, header, GdeUtils.Azione.paaInviaEsitoStorno));

		TipoInviaEsitoStornoRisposta risposta = new TipoInviaEsitoStornoRisposta();
		try {
			rrCtrl.acquisisciER(identificativoIntermediarioPA,
					identificativoStazioneIntermediarioPA,
					identificativoDominio,
					identificativoUnivocoVersamento,
					codiceContestoPagamento, 
					er);
			
			log.info("Esito storno acquisito con successo.");
			risposta.setEsito("OK");
			eventi.buildResponses();
			eventi.setEsito(risposta.getEsito());
		} catch (GovPayNdpException e) {
			log.error("Errore durante l'acquisizione della ER: " + e.getFaultCode() + " - " + e.getDescrizione());
			fault.setFaultCode(e.getFaultCode().name());
			if(!e.getFaultCode().equals(NdpFaultCode.PAA_ERRORE_INTERNO)) {
				fault.setFaultString(e.getDescrizione());
			} else {
				fault.setFaultString("Errore interno");
				log.error("Errore interno", e);
			}
			risposta.setFault(fault);
			risposta.setEsito("KO");
			eventi.buildResponses();
			eventi.setEsito(risposta.getEsito() + ": " + risposta.getFault().getFaultCode());
		} catch (Exception e) {
			log.error("Errore imprevisto durante l'acquisizione della ER", e);
			risposta.setEsito("KO");
			fault.setFaultCode(NdpFaultCode.PAA_ERRORE_INTERNO.name());
			fault.setFaultString("Errore interno");
			risposta.setFault(fault);
			eventi.buildResponses();
			eventi.setEsito(risposta.getEsito() + ": " + risposta.getFault().getFaultCode());
		} finally {
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
	public PaaInviaRTRisposta paaInviaRT(PaaInviaRT bodyrichiesta, IntestazionePPT header) {
		ThreadContext.put("proc", "InviaRT");
		ThreadContext.put("dom", header.getIdentificativoDominio());
		ThreadContext.put("iuv", header.getIdentificativoUnivocoVersamento());
		ThreadContext.put("ccp", header.getCodiceContestoPagamento());
		log.info("Ricevuta richiesta di Ricevuta Telematica");

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
		eventi.getEventi().add(GdeUtils.creaEventoRichiesta(nomeSoggettoEnte, header, GdeUtils.Azione.paaInviaRT));

		PaaInviaRTRisposta risposta = new PaaInviaRTRisposta();
		try {
			rtCtrl.acquisisciRT(bodyrichiesta.getRt(), 
					bodyrichiesta.getTipoFirma(), 
					header.getIdentificativoIntermediarioPA(), 
					header.getIdentificativoStazioneIntermediarioPA(), 
					header.getIdentificativoDominio(), 
					header.getIdentificativoUnivocoVersamento(),
					header.getCodiceContestoPagamento());
			
			log.info("Ricevuta Telematica acquisita con successo.");
			EsitoPaaInviaRT esito = new EsitoPaaInviaRT();
			esito.setEsito("OK");
			risposta.setPaaInviaRTRisposta(esito);
			eventi.buildResponses();
			eventi.setEsito(esito.getEsito());
		} catch (GovPayNdpException e) {
			log.error("Errore durante l'acquisizione della RT: " + e.getFaultCode() + " - " + e.getDescrizione());
			EsitoPaaInviaRT esito = new EsitoPaaInviaRT();
			esito.setEsito("KO");
			fault.setFaultCode(e.getFaultCode().name());
			if(!e.getFaultCode().equals(NdpFaultCode.PAA_ERRORE_INTERNO)) {
				fault.setFaultString(e.getDescrizione());
			} else {
				fault.setFaultString("Errore interno");
				log.error("Errore interno", e);
			}
			esito.setFault(fault);
			risposta.setPaaInviaRTRisposta(esito);
			eventi.buildResponses();
			eventi.setEsito(esito.getEsito() + ": " + esito.getFault().getFaultCode());
		} catch (Exception e) {
			log.error("Errore imprevisto durante l'acquisizione della RT", e);
			EsitoPaaInviaRT esito = new EsitoPaaInviaRT();
			esito.setEsito("KO");
			fault.setFaultCode(NdpFaultCode.PAA_ERRORE_INTERNO.name());
			fault.setFaultString("Errore interno");
			esito.setFault(fault);
			risposta.setPaaInviaRTRisposta(esito);
			eventi.buildResponses();
			eventi.setEsito(esito.getEsito() + ": " + esito.getFault().getFaultCode());
		} finally {
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
