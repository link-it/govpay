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
package it.govpay.ejb.ndp.controller;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediCopiaRT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediCopiaRTRisposta;
import it.govpay.ejb.core.controller.DistintaEJB;
import it.govpay.ejb.core.controller.PendenzaEJB;
import it.govpay.ejb.core.controller.ScadenzarioEJB;
import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.core.model.DistintaModel;
import it.govpay.ejb.core.model.EsitoPagamentoDistinta;
import it.govpay.ejb.core.model.PendenzaModel;
import it.govpay.ejb.core.model.ScadenzarioModel;
import it.govpay.ejb.ndp.ejb.AnagraficaDominioEJB;
import it.govpay.ejb.ndp.ejb.DocumentiEJB;
import it.govpay.ejb.ndp.model.DominioEnteModel;
import it.govpay.ejb.ndp.model.impl.RPTModel;
import it.govpay.ejb.ndp.model.impl.RTModel;
import it.govpay.ejb.ndp.pojo.NdpFaultCode;
import it.govpay.ejb.ndp.util.NdpUtils;
import it.govpay.ejb.ndp.util.ValidatoreNdP;
import it.govpay.ejb.ndp.util.exception.GovPayNdpException;
import it.govpay.ejb.ndp.util.wsclient.NodoPerPa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.xml.ws.Holder;

import org.apache.logging.log4j.Logger;

@Stateless
public class RtController {

	@Inject
	DocumentiEJB documentiEjb;
	
	@Inject
	ScadenzarioEJB scadenzarioEjb;
	
	@Inject
	DistintaEJB distintaEjb;
	
	@Inject
	PendenzaEJB pendenzaEjb;
	
	@Inject
	AnagraficaDominioEJB anagraficaDominioEjb;
	
    @Inject  
    private transient Logger log;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EsitoPagamentoDistinta acquisisciRT(byte[] rtByte, String tipoFirma, String idIntermediario, String idStazioneIntermediario, String idDominio, String iuv, String ccp) throws GovPayException {
		log.debug("Archiviazione della Ricevuta Telematica: \n" + new String(rtByte));
		
		RTModel documento = new RTModel(idDominio, iuv, ccp, rtByte);
		documentiEjb.archiviaDocumento(documento);
		
		DominioEnteModel dominioEnte = anagraficaDominioEjb.getDominioEnte(idDominio, idIntermediario, idStazioneIntermediario);
		if(dominioEnte == null) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_ID_DOMINIO_ERRATO, null);
		}
		
		// Valido i dati di intestazione
		ValidatoreNdP validatoreRT = new ValidatoreNdP();
		log.debug("Validazione dei dati di intestazione.");
		validatoreRT.validaDatiDominioEnte(idDominio, idIntermediario, idStazioneIntermediario, dominioEnte);
		
		// Recupero l'RPT associata all'RT
		
		log.debug("Recupero della Richiesta Pagamento Telematico.");
		CtRichiestaPagamentoTelematico rpt;
		try {
			RPTModel rptDoc = documentiEjb.recuperaRPT(idDominio, iuv, ccp);
			if(rptDoc == null) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_RPT_SCONOSCIUTA, "La Richiesta Pagamento Telematico riferita non e' presente nella base dati.");
			}
			rpt = NdpUtils.toRPT(rptDoc.getBytes());
		} catch (Exception e) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, NdpFaultCode.PAA_ERRORE_INTERNO, "Impossibile recuperare il documento Richiesta Pagamento Telematico.", e);
		}
		
		
		log.debug("Recupero i dati della distinta e delle pendenze associate.");
		DistintaModel distinta = null;
		try {
			distinta = distintaEjb.getDistinta(dominioEnte.getEnteCreditore().getIdFiscale(), iuv, ccp);
		} catch (Exception e) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, NdpFaultCode.PAA_ERRORE_INTERNO, "Impossibile recuperare la distinta associata al pagamento.");
		}
		
		if(distinta == null) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_RPT_SCONOSCIUTA, "La distinta riferita non e' presente nella base dati.");
		}
		
		List<PendenzaModel> pendenze = null;
		try {
			pendenze = pendenzaEjb.getPendenze(distinta.extractIdCondizioni());
		} catch (Exception e) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, NdpFaultCode.PAA_ERRORE_INTERNO, "Impossibile recuperare le pendenze associate alla distinta [listaCondizioni:" + distinta.extractIdCondizioni().toString() + "].");		}
		
		if(pendenze == null) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, NdpFaultCode.PAA_RPT_SCONOSCIUTA, null);
		}
		
		// Verifica della firma
		byte[] rtByteVerificati = validatoreRT.validaFirma(distinta.getFirma(), rtByte, tipoFirma);
		
		// Validazione della ricevuta
		log.debug("Validazione sintattica della ricevuta.");
		CtRicevutaTelematica rt = validatoreRT.validaXSD_RT(rtByteVerificati);
		
		// Validazione Semantica della ricevuta
		log.debug("Validazione semantica della ricevuta.");
		EsitoPagamentoDistinta esitoDistinta = validatoreRT.validaSemantica(rpt, rt, pendenze);
		
		log.info("Acquisizione RT avente esito " + esitoDistinta.getDescrizioneStatoPSP() + " per un importo di " + esitoDistinta.getImportoTotalePagato());
		
		// Chiedo l'aggiornamento con i dati della ricevuta
		log.debug("Aggiorno la distina con i dati della ricevuta");
		distintaEjb.updateDistinta(esitoDistinta);
		
		scadenzarioEjb.notificaVerificaPagamento(dominioEnte.getEnteCreditore().getIdEnteCreditore(), dominioEnte.getStazione().getScadenzario(dominioEnte.getEnteCreditore().getIdEnteCreditore()), distinta);
		
		return esitoDistinta;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EsitoPagamentoDistinta chiediCopiaRT(String idEnteCreditore, ScadenzarioModel scadenzario, String iuv, String codTransazionePsp) throws GovPayException {
		log.info("Richiedo copia della RT al Nodo");
		DominioEnteModel dominioEnte = anagraficaDominioEjb.getDominioEnte(idEnteCreditore, scadenzario.getIdStazione());
		if(dominioEnte == null) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_CONFIGURAZIONE, "Dominio NdP dell'ente non censito sul sistema.");
		}
		NodoPerPa client = new NodoPerPa(dominioEnte.getIntermediario(), log);
		Holder<Map<String,List<String>>> responseHeaders = new Holder<Map<String,List<String>>>();
		NodoChiediCopiaRT nodoChiediCopiaRT = new NodoChiediCopiaRT();
		nodoChiediCopiaRT.setCodiceContestoPagamento(codTransazionePsp);
		nodoChiediCopiaRT.setIdentificativoDominio(dominioEnte.getIdDominio());
		nodoChiediCopiaRT.setIdentificativoIntermediarioPA(dominioEnte.getIntermediario().getIdIntermediarioPA());
		nodoChiediCopiaRT.setIdentificativoStazioneIntermediarioPA(dominioEnte.getStazione().getIdStazioneIntermediarioPA());
		nodoChiediCopiaRT.setIdentificativoUnivocoVersamento(iuv);
		nodoChiediCopiaRT.setPassword(dominioEnte.getStazione().getPassword());
		NodoChiediCopiaRTRisposta risposta;
		try {
			risposta = client.nodoChiediCopiaRT(responseHeaders, nodoChiediCopiaRT);
		} catch (GovPayException e) {
			log.error("Richiesta copia RT fallita: " + e);
			throw e;
		}
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		try {
			DataHandler dh = risposta.getRt();
			dh.writeTo(output);
		} catch (IOException e) {
			log.error("Errore durante la lettura dell'RT: " + e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
		
		return acquisisciRT(output.toByteArray(), 
				risposta.getTipoFirma(), 
				dominioEnte.getIntermediario().getIdIntermediarioPA(), 
				dominioEnte.getStazione().getIdStazioneIntermediarioPA(), 
				dominioEnte.getIdDominio(),
				iuv,
				codTransazionePsp);
	}
}
