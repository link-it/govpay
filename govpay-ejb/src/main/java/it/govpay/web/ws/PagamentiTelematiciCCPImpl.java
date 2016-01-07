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

import java.math.BigDecimal;

import gov.telematici.pagamenti.ws.ppthead.IntestazionePPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
import it.gov.digitpa.schemas._2011.ws.psp.EsitoAttivaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.FaultBean;
import it.gov.digitpa.schemas._2011.ws.psp.EsitoVerificaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.PaaAttivaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.PaaAttivaRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.psp.PaaTipoDatiPagamentoPA;
import it.gov.digitpa.schemas._2011.ws.psp.PaaVerificaRPT;
import it.gov.digitpa.schemas._2011.ws.psp.PaaVerificaRPTRisposta;
import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematiciccp.PagamentiTelematiciCCP;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.Rt.FaultPa;
import it.govpay.bd.model.Versamento;
import it.govpay.business.Pagamenti;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayNdpException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.servizi.pa.TipoVersamento;
import it.govpay.utils.NdpUtils;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

@WebService(serviceName = "PagamentiTelematiciCCPservice",
endpointInterface = "it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematiciccp.PagamentiTelematiciCCP",
targetNamespace = "http://NodoPagamentiSPC.spcoop.gov.it/servizi/PagamentiTelematiciCCP",
portName = "PPTPort")

@HandlerChain(file="../../../../handler-chains/ws-ndp-handler-chain.xml")
public class PagamentiTelematiciCCPImpl implements PagamentiTelematiciCCP {

	@Resource
	WebServiceContext wsCtxt;

	private static Logger log = LogManager.getLogger();

	@Override
	public PaaAttivaRPTRisposta paaAttivaRPT(PaaAttivaRPT bodyrichiesta, IntestazionePPT header) {
		String codIntermediario = header.getIdentificativoIntermediarioPA();
		String codStazione = header.getIdentificativoStazioneIntermediarioPA();
		String codDominio = header.getIdentificativoDominio();
		String iuv = header.getIdentificativoUnivocoVersamento();
		String ccp = header.getCodiceContestoPagamento();
		

		BasicBD bd = null;


		log.info("Ricevuta richiesta di attiva RPT [" + codIntermediario + "][" + codStazione + "][" + codDominio + "][" + iuv + "][" + ccp + "]");
		try {
			try {
				bd = BasicBD.newInstance();
				Intermediario interm = AnagraficaManager.getIntermediario(bd, header.getIdentificativoIntermediarioPA());
				
				NdpUtils.setThreadContextNdpParams(codDominio, iuv, ccp, bodyrichiesta.getIdentificativoPSP(), TipoVersamento.PO.name(), codStazione, null, null, "paaAttivaRPT", interm.getDenominazione());
			} catch (ServiceException e) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, FaultPa.PAA_ERRORE_INTERNO, "Impossibile stabilire una connessione con db", e);
			} catch (NotFoundException e) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, FaultPa.PAA_ERRORE_INTERNO, "Impossibile trovare l'intermediario ["+header.getIdentificativoIntermediarioPA()+"] sul db", e);
			} catch (MultipleResultException e) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, FaultPa.PAA_ERRORE_INTERNO, "Impossibile trovare l'intermediario ["+header.getIdentificativoIntermediarioPA()+"] sul db", e);
			}
			
			Pagamenti pagamenti = new Pagamenti(bd);
			
			Anagrafica pagatore = toOrm(bodyrichiesta.getDatiPagamentoPSP().getSoggettoPagatore());
			Anagrafica versante = toOrm(bodyrichiesta.getDatiPagamentoPSP().getSoggettoVersante());
			String ibanAddebito = bodyrichiesta.getDatiPagamentoPSP().getIbanAddebito();
			BigDecimal importo = bodyrichiesta.getDatiPagamentoPSP().getImportoSingoloVersamento();
			
			Versamento versamento = pagamenti.attiva(codIntermediario, codStazione, codDominio, iuv, ccp, bodyrichiesta.getIdentificativoPSP(), pagatore, versante, ibanAddebito, importo);
			PaaAttivaRPTRisposta risposta = new PaaAttivaRPTRisposta();
			EsitoAttivaRPT esitoAttiva = new EsitoAttivaRPT();
			PaaTipoDatiPagamentoPA datiPagamento = new PaaTipoDatiPagamentoPA();
			datiPagamento.setCausaleVersamento(versamento.getSingoloVersamento(0).getCausaleVersamento());
			datiPagamento.setImportoSingoloVersamento(versamento.getImportoTotale());
			esitoAttiva.setDatiPagamentoPA(datiPagamento);
			esitoAttiva.setEsito("OK");
			risposta.setPaaAttivaRPTRisposta(esitoAttiva);
			log.info("RPT attivata");
			ThreadContext.put(it.govpay.bd.model.Evento.ESITO, esitoAttiva.getEsito());
			return risposta;
		} catch (GovPayNdpException gpndp) {
			PaaAttivaRPTRisposta risposta = new PaaAttivaRPTRisposta();
			EsitoAttivaRPT esito = new EsitoAttivaRPT();
			esito.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(codDominio);
			fault.setFaultCode(gpndp.getFaultCode().toString());
			fault.setDescription(gpndp.getMessage());
			esito.setFault(fault);
			risposta.setPaaAttivaRPTRisposta(esito);
			log.info("Riscontrato esito KO nella attivazione del pagamento: [" + gpndp.getFaultCode().toString() + "] " + gpndp.getMessage());
			ThreadContext.put(it.govpay.bd.model.Evento.ESITO, esito.getEsito());
			return risposta;
		} catch (GovPayException e) {
			PaaAttivaRPTRisposta risposta = new PaaAttivaRPTRisposta();
			EsitoAttivaRPT esito = new EsitoAttivaRPT();
			esito.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(codDominio);
			fault.setFaultCode(FaultPa.PAA_ERRORE_INTERNO.toString());
			fault.setDescription(e.getDescrizione());
			esito.setFault(fault);
			risposta.setPaaAttivaRPTRisposta(esito);
			log.error("Riscontrato esito KO nella attivazione del pagamento: [" + FaultPa.PAA_ERRORE_INTERNO.toString() + "] " + e.getDescrizione(), e);
			ThreadContext.put(it.govpay.bd.model.Evento.ESITO, esito.getEsito());
			return risposta;
		} finally {
			if(bd!= null) bd.closeConnection();
		}
	}

	

	@Override
	public PaaVerificaRPTRisposta paaVerificaRPT(PaaVerificaRPT bodyrichiesta, IntestazionePPT header) {
		String codIntermediario = header.getIdentificativoIntermediarioPA();
		String codStazione = header.getIdentificativoStazioneIntermediarioPA();
		String codDominio = header.getIdentificativoDominio();
		String iuv = header.getIdentificativoUnivocoVersamento();
		String ccp = header.getCodiceContestoPagamento();
		log.info("Ricevuta richiesta di verifica RPT [" + codIntermediario + "][" + codStazione + "][" + codDominio + "][" + iuv + "][" + ccp + "]");
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.newInstance();
				Intermediario interm = AnagraficaManager.getIntermediario(bd, header.getIdentificativoIntermediarioPA());
				NdpUtils.setThreadContextNdpParams(codDominio, iuv, ccp, bodyrichiesta.getIdentificativoPSP(), TipoVersamento.PO.name(), codStazione, null, null, "paaVerificaRPT", interm.getDenominazione());
			} catch (ServiceException e) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, FaultPa.PAA_ERRORE_INTERNO, "Impossibile stabilire una connessione con db", e);
			} catch (NotFoundException e) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, FaultPa.PAA_ERRORE_INTERNO, "Impossibile trovare l'intermediario ["+header.getIdentificativoIntermediarioPA()+"] sul db", e);
			} catch (MultipleResultException e) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, FaultPa.PAA_ERRORE_INTERNO, "Impossibile trovare l'intermediario ["+header.getIdentificativoIntermediarioPA()+"] sul db", e);
			}
			Pagamenti pagamenti = new Pagamenti(bd);
			Versamento versamento = pagamenti.verifica(codIntermediario, codStazione, codDominio, iuv, ccp);
			PaaVerificaRPTRisposta risposta = new PaaVerificaRPTRisposta();
			EsitoVerificaRPT esitoVerifica = new EsitoVerificaRPT();
			PaaTipoDatiPagamentoPA datiPagamento = new PaaTipoDatiPagamentoPA();
			datiPagamento.setCausaleVersamento(versamento.getSingoloVersamento(0).getCausaleVersamento());
			datiPagamento.setImportoSingoloVersamento(versamento.getImportoTotale());
			esitoVerifica.setDatiPagamentoPA(datiPagamento);
			esitoVerifica.setEsito("OK");
			risposta.setPaaVerificaRPTRisposta(esitoVerifica);
			log.info("RPT verificata");
			ThreadContext.put(it.govpay.bd.model.Evento.ESITO, esitoVerifica.getEsito());
			return risposta;
		} catch (GovPayNdpException gpndp) {
			PaaVerificaRPTRisposta risposta = new PaaVerificaRPTRisposta();
			EsitoVerificaRPT esito = new EsitoVerificaRPT();
			esito.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(codDominio);
			fault.setFaultCode(gpndp.getFaultCode().toString());
			fault.setDescription(gpndp.getMessage());
			esito.setFault(fault);
			risposta.setPaaVerificaRPTRisposta(esito);
			log.info("Riscontrato esito KO nella verifica del pagamento: [" + gpndp.getFaultCode().toString() + "] " + gpndp.getMessage());
			ThreadContext.put(it.govpay.bd.model.Evento.ESITO, esito.getEsito());
			return risposta;
		} catch (GovPayException e) {
			PaaVerificaRPTRisposta risposta = new PaaVerificaRPTRisposta();
			EsitoVerificaRPT esito = new EsitoVerificaRPT();
			esito.setEsito("KO");
			FaultBean fault = new FaultBean();
			fault.setId(codDominio);
			fault.setFaultCode(FaultPa.PAA_ERRORE_INTERNO.toString());
			fault.setDescription(e.getDescrizione());
			esito.setFault(fault);
			risposta.setPaaVerificaRPTRisposta(esito);
			log.error("Riscontrato esito KO nella verifica del pagamento: [" + FaultPa.PAA_ERRORE_INTERNO.toString() + "] " + e.getDescrizione(), e);
			ThreadContext.put(it.govpay.bd.model.Evento.ESITO, esito.getEsito());
			return risposta;
		} finally {
			if(bd!= null) bd.closeConnection();
		}
	}
	
	
	private Anagrafica toOrm(CtSoggettoPagatore soggettoPagatore) {
		Anagrafica anagrafica = new Anagrafica();
		anagrafica.setCap(soggettoPagatore.getCapPagatore());
		anagrafica.setCivico(soggettoPagatore.getCivicoPagatore());
		anagrafica.setCodUnivoco(soggettoPagatore.getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco());
		anagrafica.setEmail(soggettoPagatore.getEMailPagatore());
		anagrafica.setIndirizzo(soggettoPagatore.getIndirizzoPagatore());
		anagrafica.setLocalita(soggettoPagatore.getLocalitaPagatore());
		anagrafica.setNazione(soggettoPagatore.getNazionePagatore());
		anagrafica.setProvincia(soggettoPagatore.getProvinciaPagatore());
		anagrafica.setRagioneSociale(soggettoPagatore.getAnagraficaPagatore());
		return anagrafica;
	}
	
	private Anagrafica toOrm(CtSoggettoVersante soggettoVersante) {
		if(soggettoVersante == null) return null;
		Anagrafica anagrafica = new Anagrafica();
		anagrafica.setCap(soggettoVersante.getCapVersante());
		anagrafica.setCivico(soggettoVersante.getCivicoVersante());
		anagrafica.setCodUnivoco(soggettoVersante.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco());
		anagrafica.setEmail(soggettoVersante.getEMailVersante());
		anagrafica.setIndirizzo(soggettoVersante.getIndirizzoVersante());
		anagrafica.setLocalita(soggettoVersante.getLocalitaVersante());
		anagrafica.setNazione(soggettoVersante.getNazioneVersante());
		anagrafica.setProvincia(soggettoVersante.getProvinciaVersante());
		anagrafica.setRagioneSociale(soggettoVersante.getAnagraficaVersante());
		return anagrafica;
	}
}
