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
package it.govpay.ndp.controller;

import it.gov.digitpa.schemas._2011.pagamenti.CtEsitoRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaRevoca;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRichiestaStorno;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRichiestaStornoRisposta;
import it.govpay.ejb.controller.AnagraficaEJB;
import it.govpay.ejb.controller.DistintaEJB;
import it.govpay.ejb.controller.PendenzaEJB;
import it.govpay.ejb.controller.RevocaDistintaEJB;
import it.govpay.ejb.controller.ScadenzarioEJB;
import it.govpay.ejb.exception.GovPayException;
import it.govpay.ejb.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.model.DistintaModel;
import it.govpay.ejb.model.EnteCreditoreModel;
import it.govpay.ejb.model.EsitoRevocaDistinta;
import it.govpay.ejb.model.GatewayPagamentoModel;
import it.govpay.ejb.model.PendenzaModel;
import it.govpay.ejb.model.RevocaDistintaModel;
import it.govpay.ejb.model.RevocaDistintaModel.EnumStatoRevoca;
import it.govpay.ejb.model.ScadenzarioModel;
import it.govpay.ejb.utils.IdUtils;
import it.govpay.ndp.ejb.AnagraficaDominioEJB;
import it.govpay.ndp.ejb.DocumentiEJB;
import it.govpay.ndp.model.DominioEnteModel;
import it.govpay.ndp.model.EventiInterfacciaModel;
import it.govpay.ndp.model.impl.ERModel;
import it.govpay.ndp.model.impl.RRModel;
import it.govpay.ndp.model.impl.RTModel;
import it.govpay.ndp.pojo.NdpFaultCode;
import it.govpay.ndp.util.GdeUtils;
import it.govpay.ndp.util.NdpUtils;
import it.govpay.ndp.util.ValidatoreNdP;
import it.govpay.ndp.util.GdeUtils.Azione;
import it.govpay.ndp.util.exception.GovPayNdpException;
import it.govpay.ndp.util.wsclient.NodoPerPa;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.xml.ws.Holder;

import org.apache.logging.log4j.Logger;

@Stateless
public class RrController {

	@Inject
	DocumentiEJB documentiEjb;
	
	@Inject
	RevocaDistintaEJB revocaDistintaEjb;
	
	@Inject
	DistintaEJB distintaEjb;
	
	@Inject
	AnagraficaEJB anagraficaEjb;
	
	@Inject
	AnagraficaDominioEJB anagraficaDominioEjb;
	
	@Inject
	GdeController gdeCtrl;
	
	@Inject
	PendenzaEJB pendenzaEjb;
	
	@Inject
	ScadenzarioEJB scadenzarioEjb;
	
    @Inject  
    private transient Logger log;
	
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void richiediStorno(EnteCreditoreModel enteCreditore, DistintaModel distinta, String causaleStorno) throws GovPayException {
		log.debug("Verifico che il PSP supporti lo storno");
		GatewayPagamentoModel gw = anagraficaEjb.getValidGateway(distinta.getIdGatewayPagamento());
		if(gw == null)
			throw new GovPayException(GovPayExceptionEnum.STORNO_NON_CONSENTITO, "Il PSP non e' disponibile.");
		if(!gw.isStornoGestito()) 
			throw new GovPayException(GovPayExceptionEnum.STORNO_NON_CONSENTITO, "Il pagamento risulta essere ancora in corso.");
		
		log.debug("Identificazione del Dominio");
		ScadenzarioModel scadenzario = anagraficaEjb.getScadenzario(distinta);
		DominioEnteModel dominioEnte = anagraficaDominioEjb.getDominioEnte(enteCreditore.getIdEnteCreditore(), scadenzario.getIdStazione());
		if(dominioEnte == null) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_CONFIGURAZIONE, "Dominio NdP dell'ente non censito sul sistema.");
		}
		
		log.info("Creazione della RevocaDistinta in stato IN_CORSO");
		String idTransazioneRevoca = IdUtils.generaCodTransazione();
		RevocaDistintaModel revocaDistinta = revocaDistintaEjb.creaRevocaDistinta(distinta, idTransazioneRevoca, causaleStorno);

		
		log.info("Recupero la Ricevuta Telematica dal repository documenti");
		
		String idDominio = dominioEnte.getIdDominio();
		String iuv = distinta.getIuv();
		String ccp = distinta.getCodTransazionePsp();
		
		CtRicevutaTelematica rt;
		try {
			RTModel rptDoc = documentiEjb.recuperaRT(idDominio, iuv, ccp);
			if(rptDoc == null) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_RPT_SCONOSCIUTA, "La Ricevuta Telematica riferita non e' presente nella base dati.");
			}
			rt = NdpUtils.toRT(rptDoc.getBytes());
		} catch (Exception e) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, NdpFaultCode.PAA_ERRORE_INTERNO, "Impossibile recuperare il documento Ricevuta Telematica.", e);
		}
		
		CtRichiestaRevoca rr = NdpUtils.buildRR(rt, idTransazioneRevoca, causaleStorno);

		byte[] rrByte;

		try {
			rrByte = NdpUtils.toByte(rr);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Impossibile serializzare la richiesta di pagamento.", e);
		}
		
		log.debug("Archiviazione della Richiesta di Revoca");
		
		RRModel documento = new RRModel(idDominio, iuv, ccp, rrByte);
		documentiEjb.archiviaDocumento(documento);
		
		
		Holder<Map<String,List<String>>> responseHeaders = new Holder<Map<String,List<String>>>();
		NodoInviaRichiestaStorno richiesta = new NodoInviaRichiestaStorno();
		richiesta.setCodiceContestoPagamento(distinta.getCodTransazionePsp());
		richiesta.setIdentificativoDominio(dominioEnte.getIdDominio());
		richiesta.setIdentificativoIntermediarioPA(dominioEnte.getIntermediario().getIdIntermediarioPA());
		richiesta.setIdentificativoStazioneIntermediarioPA(dominioEnte.getStazione().getIdStazioneIntermediarioPA());
		richiesta.setIdentificativoUnivocoVersamento(distinta.getIuv());
		richiesta.setPassword(dominioEnte.getStazione().getPassword());
		richiesta.setRr(rrByte);
		
		EventiInterfacciaModel eventi = new EventiInterfacciaModel();
		eventi.getEventi().add(GdeUtils.creaEventoRichiesta(dominioEnte, 
				iuv, 
				distinta.getCodTransazionePsp(),
				null,
				gw.getSystemId(),
				gw.getApplicationId().substring(0, gw.getApplicationId().lastIndexOf("-")), 
				Azione.nodoInviaRichiestaStorno));
		
		NodoInviaRichiestaStornoRisposta risposta;
		try {
			NodoPerPa client = new NodoPerPa(dominioEnte.getIntermediario(), log);
			risposta = client.nodoInviaRichiestaStorno(responseHeaders, richiesta);
		} catch (GovPayException e) {
			log.error("Spedizione Richiesta Storno fallita: " + e.getMessage());
			log.debug("Registrazione degli eventi relativi alla spedizione.");
			eventi.setEsito("Spedizione fallita");
			try {
				gdeCtrl.registraEventi(eventi);
			} catch (GovPayException ev) {
				log.error("Errore durante la registrazione degli eventi nel giornale.", e);
			}
			revocaDistintaEjb.updateStatoRevocaDistinta(revocaDistinta.getIdRevocaDistinta(), EnumStatoRevoca.IN_ERRORE, null, "Errore di rete durante la trasmissione dello storno.");
			throw e;
		}
		
		if(eventi.getInfospcoop() != null) {
			log.debug("Spedizione della RR al Nodo dei Pagamenti completata con successo [IdEgov: " + eventi.getInfospcoop().getIdEgov() + "].");
		} else {
			log.debug("Spedizione della RR al Nodo dei Pagamenti completata con successo.");
		}

		log.debug("Registrazione degli eventi relativi alla spedizione.");
		try {
			eventi.setInfospcoop(GdeUtils.creaInfoSPCoop(responseHeaders.value));
			eventi.buildResponses();
			eventi.setEsito(risposta.getEsito());
			gdeCtrl.registraEventi(eventi);
		} catch (Exception e) {
			log.error("Errore durante la registrazione degli eventi", e);
		}

		if(risposta.getFault() != null) {
			NdpFaultCode faultCode = NdpFaultCode.valueOf(risposta.getFault().getFaultCode());
			String descrizioneFault = risposta.getFault().getFaultCode() + " - " + risposta.getFault().getFaultString() + ". " + (risposta.getFault().getDescription() != null ?risposta.getFault().getDescription() : ""); 
			log.error("Ricevuto esito di errore: " + descrizioneFault);
			revocaDistintaEjb.updateStatoRevocaDistinta(revocaDistinta.getIdRevocaDistinta(), EnumStatoRevoca.IN_ERRORE, null, "Il Nodo dei Pagamenti ha rifiutato la richiesta di storno: " + descrizioneFault);
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, faultCode, descrizioneFault);
		} 
			
		log.info("Ricevuto esito " + risposta.getEsito() + ".");
		
		revocaDistinta = revocaDistintaEjb.updateStatoRevocaDistinta(revocaDistinta.getIdRevocaDistinta(), EnumStatoRevoca.ESEGUITO_SBF, null, null);
	}

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public EsitoRevocaDistinta acquisisciER(String idIntermediario,
			String idStazioneIntermediario,
			String idDominio,
			String iuv,
			String ccp, byte[] erByte) throws GovPayException{
		
    	log.debug("Archiviazione della Esito Revoca");
    	
    	ERModel documento = new ERModel(idDominio, iuv, ccp, erByte);
		documentiEjb.archiviaDocumento(documento);
		
		DominioEnteModel dominioEnte = anagraficaDominioEjb.getDominioEnte(idDominio, idIntermediario, idStazioneIntermediario);
		if(dominioEnte == null) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_ID_DOMINIO_ERRATO, null);
		}
		
		// Valido i dati di intestazione
		ValidatoreNdP validatore = new ValidatoreNdP();
		log.debug("Validazione dei dati di intestazione.");
		validatore.validaDatiDominioEnte(idDominio, idIntermediario, idStazioneIntermediario, dominioEnte);
		
		log.debug("Recupero i dati della distinta e delle pendenze associate.");
		DistintaModel distinta = null;
		try {
			distinta = distintaEjb.getDistinta(dominioEnte.getEnteCreditore().getIdFiscale(), iuv);
		} catch (Exception e) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, NdpFaultCode.PAA_ERRORE_INTERNO, "Impossibile recuperare la distinta associata al pagamento.");
		}
		
		if(distinta == null) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_RPT_SCONOSCIUTA, "La distinta riferita non e' presente nella base dati.");
		}
		
		log.debug("Recupero la Revoca Distinta.");
		RevocaDistintaModel revocaDistinta = revocaDistintaEjb.getRevocaDistinta(distinta);
		
		if(revocaDistinta == null) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SEMANTICA, "La Richiesta Revoca riferita non e' presente nella base dati.");
		}
		
		switch (revocaDistinta.getStatoRevoca()) {
		case ESEGUITO_SBF:
		case IN_CORSO:
			// Ok... si va avanti con l'acquisizione
			break;
		case ESEGUITO:
		case NON_ESEGUITO:
		case PARZIALMENTE_ESEGUITO:
		case IN_ERRORE:
			// Gia acquisito... non posso acquisirlo ancora
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, NdpFaultCode.PAA_ER_DUPLICATA, "Esito gia' recepito come " + revocaDistinta.getStatoRevoca());
		}
		
		
		// Recupero l'RR associata all'ER
		log.debug("Recupero della Richiesta Revoca.");
		CtRichiestaRevoca rr;
		try {
			RRModel rrDoc = documentiEjb.recuperaRR(idDominio, iuv, ccp);
			if(rrDoc == null) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_SEMANTICA, "La Richiesta Revoca riferita non e' presente nella base dati.");
			}
			rr = NdpUtils.toRR(rrDoc.getBytes());
		} catch (Exception e) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, NdpFaultCode.PAA_ERRORE_INTERNO, "Impossibile recuperare il documento Richiesta Revoca.", e);
		}
		
		
		
		List<PendenzaModel> pendenze = null;
		try {
			pendenze = pendenzaEjb.getPendenze(distinta.extractIdCondizioni());
		} catch (Exception e) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, NdpFaultCode.PAA_ERRORE_INTERNO, "Impossibile recuperare le pendenze associate alla distinta [listaCondizioni:" + distinta.extractIdCondizioni().toString() + "].");		}
		
		if(pendenze == null) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_RPT_SCONOSCIUTA, null);
		}
		
		
		// Validazione dell'esito
		log.debug("Validazione sintattica dell'esito revoca.");
		CtEsitoRevoca er = validatore.validaXSD_ER(erByte);
		
		// Validazione Semantica della ricevuta
		log.debug("Validazione semantica dell'esito di revoca.");
		EsitoRevocaDistinta esitoRevocaDistinta = validatore.validaSemantica(rr, er, pendenze);
		
		distintaEjb.updateDistinta(distinta.getIdDistinta(), esitoRevocaDistinta);
		revocaDistintaEjb.updateStatoRevocaDistinta(revocaDistinta.getIdRevocaDistinta(), esitoRevocaDistinta.getStato(), esitoRevocaDistinta.getImportoTotaleRevocato(), null);
		scadenzarioEjb.notificaEsitoRevoca(dominioEnte.getEnteCreditore().getIdEnteCreditore(), dominioEnte.getStazione().getScadenzario(dominioEnte.getEnteCreditore().getIdEnteCreditore()), distinta);
		
		return esitoRevocaDistinta;
	}
}
