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
package it.govpay.core.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.xml.sax.SAXException;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiEsitoRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingolaRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloEsitoRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtEsitoRevoca;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaRevoca;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRichiestaStorno;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Evento.CategoriaEvento;
import it.govpay.bd.model.Evento.TipoEvento;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Notifica.TipoNotifica;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Rr.StatoRr;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.business.model.Risposta;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;

public class RrUtils extends NdpValidationUtils {

	private static Logger log = LogManager.getLogger();

	public static String buildUUID35() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static Rr buildRr(Rpt rpt, List<Pagamento> pagamenti, BasicBD bd) throws ServiceException {

		CtRicevutaTelematica ctRt = null;
		
		try {
			ctRt = JaxbUtils.toRT(rpt.getXmlRt());
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		CtRichiestaRevoca ctRr = buildCtRr(ctRt, pagamenti, bd);
		
		byte[] xmlRr;
		try {
			xmlRr = JaxbUtils.toByte(ctRr);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		Rr rr = new Rr();
		rr.setCcp(rpt.getCcp());
		rr.setCodDominio(rpt.getCodDominio());
		rr.setCodMsgEsito(null);
		rr.setCodMsgRevoca(ctRr.getIdentificativoMessaggioRevoca());
		rr.setDataMsgRevoca(ctRr.getDataOraMessaggioRevoca());
		rr.setDescrizioneStato(null);
		rr.setIdRpt(rpt.getId());
		rr.setImportoTotaleRevocato(null);
		rr.setImportoTotaleRichiesto(ctRr.getDatiRevoca().getImportoTotaleRevocato());
		rr.setIuv(rpt.getIuv());
		rr.setPagamenti(pagamenti);
		rr.setRpt(rpt);
		rr.setStato(StatoRr.RR_ATTIVATA);
		rr.setXmlEr(null);
		rr.setXmlRr(xmlRr);
		return rr;
	}

	private static CtRichiestaRevoca buildCtRr(CtRicevutaTelematica ctRt, List<Pagamento> pagamenti, BasicBD bd) throws ServiceException {
		CtRichiestaRevoca ctRr = new CtRichiestaRevoca();
		ctRr.setVersioneOggetto(ctRt.getVersioneOggetto());
		ctRr.setDominio(ctRt.getDominio());
		ctRr.setIdentificativoMessaggioRevoca(buildUUID35());
		ctRr.setDataOraMessaggioRevoca(new Date());
		ctRr.setIstitutoAttestante(ctRt.getIstitutoAttestante());
		ctRr.setSoggettoVersante(ctRt.getSoggettoVersante());
		ctRr.setSoggettoPagatore(ctRt.getSoggettoPagatore());
		ctRr.setDatiRevoca(buildDatiRevoca(ctRt, pagamenti, bd));
		return ctRr;
	}

	private static CtDatiRevoca buildDatiRevoca(CtRicevutaTelematica ctRt, List<Pagamento> pagamenti, BasicBD bd) throws ServiceException {
		CtDatiRevoca datiRevoca = new CtDatiRevoca();
		datiRevoca.setIdentificativoUnivocoVersamento(ctRt.getDatiPagamento().getIdentificativoUnivocoVersamento());
		datiRevoca.setCodiceContestoPagamento(ctRt.getDatiPagamento().getCodiceContestoPagamento());

		BigDecimal importoTotaleRevocato = BigDecimal.ZERO;
		for(Pagamento pagamento : pagamenti) {
			CtDatiSingolaRevoca singolaRevoca = new CtDatiSingolaRevoca();
			singolaRevoca.setCausaleRevoca(pagamento.getCausaleRevoca());
			singolaRevoca.setDatiAggiuntiviRevoca(pagamento.getDatiRevoca());
			singolaRevoca.setIdentificativoUnivocoRiscossione(pagamento.getIur());
			singolaRevoca.setSingoloImportoRevocato(pagamento.getImportoPagato());
			importoTotaleRevocato = importoTotaleRevocato.add(pagamento.getImportoPagato());
			datiRevoca.getDatiSingolaRevoca().add(singolaRevoca);
		}
		
		datiRevoca.setImportoTotaleRevocato(importoTotaleRevocato);
		return datiRevoca;
	}
	
	public static it.govpay.core.business.model.Risposta inviaRr(Rr rr, Rpt rpt, BasicBD bd) throws GovPayException, ClientException, ServiceException {
		if(bd != null) bd.closeConnection();
		Evento evento = new Evento();
		it.govpay.core.business.model.Risposta risposta = null;
		try {
			NodoClient client = new it.govpay.core.utils.client.NodoClient(rpt.getIntermediario(bd));
			NodoInviaRichiestaStorno inviaRR = new NodoInviaRichiestaStorno();
			inviaRR.setCodiceContestoPagamento(rr.getCcp());
			inviaRR.setIdentificativoDominio(rr.getCodDominio());
			inviaRR.setIdentificativoIntermediarioPA(rpt.getIntermediario(bd).getCodIntermediario());
			inviaRR.setIdentificativoStazioneIntermediarioPA(rpt.getStazione(bd).getCodStazione());
			inviaRR.setIdentificativoUnivocoVersamento(rr.getIuv());
			inviaRR.setPassword(rpt.getStazione(bd).getPassword());
			inviaRR.setRr(rr.getXmlRr());
			risposta = new it.govpay.core.business.model.Risposta(client.nodoInviaRichiestaStorno(inviaRR)); 
			return risposta;
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			// Se mi chiama InviaRptThread, BD e' null
			if(bd != null) 
				bd.setupConnection();
			else
				bd = BasicBD.newInstance();

			GiornaleEventi giornale = new GiornaleEventi(bd);
			buildEvento(evento, rpt, risposta, TipoEvento.nodoInviaRichiestaStorno, bd);
			giornale.registraEvento(evento);
		}
	}
	
	private static void buildEvento(Evento evento, Rpt rpt, Risposta risposta, TipoEvento tipoEvento, BasicBD bd) throws ServiceException {
		evento.setAltriParametriRichiesta(null);
		evento.setAltriParametriRisposta(null);
		evento.setCategoriaEvento(CategoriaEvento.INTERFACCIA);
		evento.setCcp(rpt.getCcp());
		evento.setCodCanale(rpt.getCanale(bd).getCodCanale());
		evento.setCodDominio(rpt.getCodDominio());
		evento.setCodPsp(rpt.getPsp(bd).getCodPsp());
		evento.setCodStazione(rpt.getCodStazione());
		evento.setComponente(Evento.COMPONENTE);
		evento.setDataRisposta(new Date());
		evento.setErogatore(Evento.NDP);
		if(risposta != null)
			evento.setEsito(risposta.getEsito());
		else
			evento.setEsito("Errore di trasmissione al Nodo");
		try {
			evento.setFruitore(rpt.getIntermediario(bd).getDenominazione());
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
		evento.setIuv(rpt.getIuv());
		evento.setSottotipoEvento(null);
		evento.setTipoEvento(tipoEvento);
		evento.setTipoVersamento(rpt.getCanale(bd).getTipoVersamento());
	}

	public static Rr acquisisciEr(String identificativoDominio, String identificativoUnivocoVersamento, String codiceContestoPagamento, byte[] er, BasicBD bd) throws NdpException, ServiceException {
		bd.setAutoCommit(false);
		bd.enableSelectForUpdate();
		
		
		CtEsitoRevoca ctEr = null;
		// Validazione Sintattica
		try {
			ctEr = JaxbUtils.toER(er);
		} catch (Exception e) {
			log.error("Errore durante la validazione sintattica della Ricevuta Telematica.", e);
			throw new NdpException(FaultPa.PAA_SINTASSI_XSD, e.getCause().getMessage());
		}
		
		RrBD rrBD = new RrBD(bd);
		Rr rr = null;
		try {
			rr = rrBD.getRr(ctEr.getRiferimentoMessaggioRevoca());
		} catch (NotFoundException e) {
			throw new NdpException(FaultPa.PAA_RPT_SCONOSCIUTA, identificativoDominio, null);
		}
		
		if(rr.getStato().equals(StatoRr.ER_ACCETTATA_PA)) {
			throw new NdpException(FaultPa.PAA_ER_DUPLICATA, identificativoDominio);
		}
		
		try {
			// Validazione Semantica
			try {
				RrUtils.validaSemantica(rr, JaxbUtils.toRR(rr.getXmlRr()), ctEr, bd);
			} catch (JAXBException e) {
				throw new ServiceException(e);
			} catch (SAXException e) {
				throw new ServiceException(e);
			}
		} catch (NdpException e) {
			rr.setStato(StatoRr.ER_RIFIUTATA_PA);
			rr.setXmlEr(er);
			rrBD.updateRr(rr.getId(), rr);
			throw e;
		}
		
		log.info("Acquisizione ER per un importo di " + ctEr.getDatiRevoca().getImportoTotaleRevocato());
		
		// Rileggo per avere la lettura dello stato rpt in transazione
		rr.setCodMsgEsito(ctEr.getIdentificativoMessaggioEsito());
		rr.setDataMsgEsito(ctEr.getDataOraMessaggioEsito());
		rr.setImportoTotaleRevocato(ctEr.getDatiRevoca().getImportoTotaleRevocato());
		rr.setStato(StatoRr.ER_ACCETTATA_PA);
		rr.setXmlEr(er);
		
		// Aggiorno l'RR con i dati dell'ER
		rrBD.updateRr(rr.getId(), rr);
		
		List<Pagamento> pagamenti = rr.getPagamenti(bd);
		
		PagamentiBD pagamentiBD = new PagamentiBD(bd);
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		
		for(Pagamento pagamento : pagamenti) {
			pagamentiBD.updatePagamento(pagamento);
			
			SingoloVersamento sv = pagamento.getSingoloVersamento(bd);
			versamentiBD.updateStatoSingoloVersamento(sv.getIdVersamento(), StatoSingoloVersamento.ANOMALO);
			versamentiBD.updateStatoVersamento(sv.getIdVersamento(), StatoVersamento.ANOMALO, "Pagamenti stornati");
		}
		
		
		
		Notifica notifica = new Notifica(rr, TipoNotifica.RICEVUTA, bd);
		NotificheBD notificheBD = new NotificheBD(bd);
		notificheBD.insertNotifica(notifica);
		
		bd.commit();
		bd.disableSelectForUpdate();
		
		ThreadExecutorManager.getClientPoolExecutor().execute(new InviaNotificaThread(notifica, bd));
		
		log.info("ER acquisita con successo.");
		
		return rr;
	}

	private static String validaSemantica(Rr rr, CtRichiestaRevoca ctRr, CtEsitoRevoca ctEr, BasicBD bd) throws NdpException, ServiceException {
		String errore = null;
		if((errore = validaSemantica(ctRr.getIstitutoAttestante(),ctEr.getIstitutoAttestante())) != null) 
			throw new NdpException(FaultPa.PAA_SEMANTICA, errore);
		if((errore = validaSemantica(ctRr.getSoggettoPagatore(),ctEr.getSoggettoPagatore())) != null) 
			throw new NdpException(FaultPa.PAA_SEMANTICA, errore);
		if((errore = validaSemantica(ctRr.getSoggettoVersante(),ctEr.getSoggettoVersante())) != null) 
			throw new NdpException(FaultPa.PAA_SEMANTICA, errore);
		if((errore = validaSemantica(ctRr.getDominio(),ctEr.getDominio())) != null) 
			throw new NdpException(FaultPa.PAA_SEMANTICA, errore);
		
		CtDatiRevoca datiRr = ctRr.getDatiRevoca();
		CtDatiEsitoRevoca datiEr = ctEr.getDatiRevoca();
		if(!equals(datiRr.getCodiceContestoPagamento(), datiEr.getCodiceContestoPagamento())) throw new NdpException(FaultPa.PAA_SEMANTICA, "CodiceContestoPagamento non corrisponde");
		if(!equals(datiRr.getIdentificativoUnivocoVersamento(), datiEr.getIdentificativoUnivocoVersamento())) throw new NdpException(FaultPa.PAA_SEMANTICA, "IdentificativoUnivocoVersamento non corrisponde");
		
		for(CtDatiSingoloEsitoRevoca singolaRevoca : datiEr.getDatiSingolaRevoca()) {
			Pagamento pagamento = null;
			try {
				pagamento = rr.getPagamento(singolaRevoca.getIdentificativoUnivocoRiscossione(), bd);
			} catch (NotFoundException e) {
				throw new NdpException(FaultPa.PAA_SEMANTICA, "Storno non richiesto");
			}
			
			pagamento.setDatiEsitoRevoca(singolaRevoca.getDatiAggiuntiviEsito());
			pagamento.setEsitoRevoca(singolaRevoca.getCausaleEsito());
			pagamento.setImportoRevocato(singolaRevoca.getSingoloImportoRevocato());
		}
		
		return null;
	}


}
