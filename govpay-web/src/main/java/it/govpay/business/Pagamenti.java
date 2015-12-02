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
package it.govpay.business;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloPagamentoRT;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediCopiaRT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediCopiaRTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediListaPendentiRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediListaPendentiRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediStatoRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediStatoRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaCarrelloRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaCarrelloRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.TipoElementoListaRPT;
import it.gov.digitpa.schemas._2011.ws.paa.TipoListaRPT;
import it.gov.digitpa.schemas._2011.ws.paa.TipoRPTPendente;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.EntiBD;
import it.govpay.bd.mail.MailBD;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.Esito;
import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.Iuv;
import it.govpay.bd.model.Portale;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.bd.model.Psp.ModelloPagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rpt.Autenticazione;
import it.govpay.bd.model.Rpt.FaultNodo;
import it.govpay.bd.model.Rpt.FirmaRichiesta;
import it.govpay.bd.model.Rpt.StatoRpt;
import it.govpay.bd.model.Rpt.TipoVersamento;
import it.govpay.bd.model.Rt;
import it.govpay.bd.model.Rt.EsitoPagamento;
import it.govpay.bd.model.Rt.FaultPa;
import it.govpay.bd.model.Rt.StatoRt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.EsitiBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RtBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.VersamentiBD.TipoIUV;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.exception.GovPayNdpException;
import it.govpay.plugin.NotImplementedException;
import it.govpay.plugin.PluginGovPay;
import it.govpay.rs.Pagamento;
import it.govpay.thread.InviaEsitoThread;
import it.govpay.thread.InviaRptThread;
import it.govpay.utils.EsitoFactory;
import it.govpay.utils.JaxbUtils;
import it.govpay.utils.PagamentiUtils;
import it.govpay.utils.PagamentiUtils.RptExtended;
import it.govpay.utils.UrlUtils;
import it.govpay.utils.ValidatoreRT;
import it.govpay.web.rs.gpv1.Converter;
import it.govpay.web.wsclient.BasicClient.TipoConnettore;
import it.govpay.web.wsclient.GpPerPa_v1;
import it.govpay.web.wsclient.NodoPerPa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import javax.activation.DataHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Pagamenti extends BasicBD {

	private BasicBD basicBD;

	public Pagamenti(BasicBD basicBD) {
		super(basicBD);
		this.basicBD = basicBD;
	}

	Logger log = LogManager.getLogger();

	public String generaIuv(long idApplicazione, String codDominio, TipoIUV type, int auxDigit) throws GovPayException {

		log.info("Generazione IUV [idApplicazione: " + idApplicazione + "][CodDominio: " + codDominio + "][IuvType: " + type + "]");
		try {
			Applicazione applicazione = AnagraficaManager.getApplicazione(basicBD, idApplicazione);
			Stazione stazione = AnagraficaManager.getStazione(basicBD, applicazione.getIdStazione());
			VersamentiBD versamentiBD = new VersamentiBD(this);
			setAutoCommit(false);
			Iuv iuv = versamentiBD.generaIuv(idApplicazione, auxDigit, stazione.getApplicationCode(), codDominio,  type);
			commit();
			log.info("Generato iuv " + iuv.getIuv());
			return iuv.getIuv();
		} catch (Exception e) {
			rollback();
			log.error("Riscontrato errore nella generazione dello IUV", e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} 
	}


	/**
	 * Aggiorna o inserisce un pagamento nel repository dei pagamenti in attesa.
	 * 
	 * Se il pagamento non esiste, lo inserisce in stato "DA_PAGARE"
	 * Se il pagamento esiste in stato "DA_PAGARE", lo aggiorna 
	 * Se il pagamento esiste in stato diverso da "DA_PAGARE", ritorna ERRORE_AUTORIZZAZIONE
	 * 
	 * @param ente
	 * @param applicazione
	 * @param versamento
	 * @throws GovPayException
	 */
	public void caricaPagamento(Versamento versamento) throws GovPayException {

		// Controllo che lo IUV indicato sia stato generato per applicazione richiedente
		Autorizzazione autorizzazione = new Autorizzazione(this);
		autorizzazione.authApplicazione(versamento.getIdApplicazione(), versamento.getCodDominio(), versamento.getIuv());

		for(SingoloVersamento sv : versamento.getSingoliVersamenti()) {
			autorizzazione.authApplicazione(versamento.getIdApplicazione(), sv.getIdTributo());
		}

		// Controllo se il versamento e' gia' nel db. Se non esiste lo inserisco come DA_PAGARE e esco
		VersamentiBD versamentiBD = new VersamentiBD(this);
		Versamento versamentoOld = null;

		try {
			setAutoCommit(false);
			try{
				versamentoOld = versamentiBD.getVersamento(versamento.getCodDominio(), versamento.getCodVersamentoEnte());
			} catch (NotFoundException nfe) {
				versamento.setStato(StatoVersamento.IN_ATTESA);
				versamentiBD.insertVersamento(versamento);
				log.info("Creato versamento in stato IN_ATTESA [idVersamento: " + versamento.getId() + "]");
				commit();
				return;
			}

			// Il Versamento esiste. 
			// Se lo stato e' DA_PAGARE, lo aggiorno
			if(versamentoOld.getStato().equals(StatoVersamento.IN_ATTESA)) {
				versamento.setId(versamentoOld.getId());
				versamentiBD.replaceVersamento(versamento);
				commit();
			} else {
				// Lo stato non consente un aggiornamento dei dati.
				throw new GovPayException(GovPayExceptionEnum.ERRORE_AUTORIZZAZIONE, "Non e' consentito aggiornare un pagamento in stato \"" + versamentoOld.getStato() + "\"");
			}
		} catch(Exception e) {
			rollback();
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}

	/**
	 * Annulla un pagamento nel repository dei pagamenti in attesa.
	 * 
	 * Se il pagamento non esiste, ritorna IUV_NON_TROVATO
	 * Se il pagamento esiste in stato "DA_PAGARE", lo annulla 
	 * Se il pagamento esiste in stato "ANNULLATO", non fa niente 
	 * Se il pagamento esiste in stato diverso da "DA_PAGARE" o "ANNULLATO", ritorna ERRORE_AUTORIZZAZIONE
	 * 
	 * @param applicazione
	 * @param iuv
	 * @throws GovPayException
	 */
	public void annullaPagamento(long idApplicazione, String codDominio, String codVersamentoEnte) throws GovPayException {

		VersamentiBD versamentiBD = new VersamentiBD(this);
		Versamento versamento = null;

		try {
			try{
				versamento = versamentiBD.getVersamento(idApplicazione, codVersamentoEnte);
				if(versamento.getStato().equals(StatoVersamento.IN_ATTESA)) {
					versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.ANNULLATO);
					return;
				} else if(versamento.getStato().equals(StatoVersamento.ANNULLATO)) {
					return;
				} else {
					throw new GovPayException(GovPayExceptionEnum.ERRORE_AUTORIZZAZIONE, "Non e' consentito annullare un pagamento in stato diverso da \"DA_PAGARE\"");
				}
			} catch (NotFoundException nfe) {
				throw new GovPayException(GovPayExceptionEnum.IUV_NON_TROVATO);
			}
		} catch(Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}

	public String eseguiPagamentoEnte(long idPortale, long idVersamento, long idCanale, String ibanAddebito, FirmaRichiesta firma, Autenticazione autenticazione, Anagrafica versante, String callbackUrl) throws GovPayException {
		return eseguiPagamentoEnte(idPortale, idVersamento, idCanale, ibanAddebito, null, firma, autenticazione, versante, callbackUrl);
	}

	public String eseguiPagamentoEnte(long idPortale, long idVersamento, long idCanale, String ibanAddebito, String ibanAccredito, FirmaRichiesta firma, Autenticazione autenticazione, Anagrafica versante, String callbackUrl) throws GovPayException {
		try {
			VersamentiBD versamentiBD = new VersamentiBD(this);
			Versamento versamento = versamentiBD.getVersamento(idVersamento);
			Portale portale = AnagraficaManager.getPortale(this, idPortale);
			Ente ente = AnagraficaManager.getEnte(this, versamento.getIdEnte());
			Stazione stazione = AnagraficaManager.getStazione(this, portale.getIdStazione());
			Intermediario intermediario = AnagraficaManager.getIntermediario(this, stazione.getIdIntermediario());
			Canale canale = AnagraficaManager.getCanale(this, idCanale);
			RptBD rptBD = new RptBD(this);
			setAutoCommit(false);
			RptExtended rptExtended = new PagamentiUtils().buildRpt(versamento, stazione.getId(), ente.getAnagraficaEnte(), portale, Rpt.CCP_NA, null, ibanAddebito, ibanAccredito, canale, firma, autenticazione, versante, callbackUrl, this);
			commit();
			closeConnection();

			Rpt rpt = rptExtended.getRpt();
			log.info("Spedizione RPT al Nodo [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "]");
			callbackUrl = callbackUrl.replaceAll(Pattern.quote("{idDominio}"), versamento.getCodDominio());
			NodoPerPa client = new NodoPerPa(intermediario);
			NodoInviaRPTRisposta risposta;
			try {
				NodoInviaRPT inviaRPT = new NodoInviaRPT();
				inviaRPT.setIdentificativoCanale(canale.getCodCanale());
				inviaRPT.setIdentificativoIntermediarioPSP(canale.getCodIntermediario());
				inviaRPT.setIdentificativoPSP(canale.getPsp().getCodPsp());
				inviaRPT.setPassword(stazione.getPassword());
				inviaRPT.setRpt(rptExtended.getRptXml());
				// Workaround bug del NodoSPC sulla valorizzazione del tipoFrima "Nessuna"
				if(!firma.equals(FirmaRichiesta.NESSUNA))
					inviaRPT.setTipoFirma(firma.getCodifica());
				else
					inviaRPT.setTipoFirma("");
				risposta = client.nodoInviaRPT(intermediario, stazione, canale, rpt, inviaRPT);
			} catch (GovPayException e) {
				setupConnection();
				// ERRORE DI RETE. Non so se la RPT e' stata effettivamente consegnata.
				log.error("Errore di rete nella spedizione della RPT: " + e);
				// Imposto lo stato in errore invio e lo lascio aggiornare al batch di controllo.
				log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in RPT_ERRORE_INVIO_A_NODO.");
				rpt.setStatoRpt(StatoRpt.RPT_ERRORE_INVIO_A_NODO);
				rpt.setFaultCode(null);
				rpt.setDescrizioneStato(e.getDescrizione());
				rptBD.updateStatoRpt(rpt.getId(), rpt.getStatoRpt(), rpt.getFaultCode(), rpt.getDescrizioneStato());
				aggiornaRptDaNpD(rpt);
				if(rpt.getPspRedirectURL() != null) {
					return rpt.getPspRedirectURL();
				} else {
					throw e;
				}
			}

			// Rinegozio la connessione;
			setupConnection();
			setAutoCommit(false);
			if(risposta.getFault() != null) {
				log.error("Ricevuto esito KO [FaultId: " + risposta.getFault().getId() + "] [FaultCode: " + risposta.getFault().getFaultCode() + "] [FaultString: " + risposta.getFault().getFaultString() + "] [FaultDescription: " +  risposta.getFault().getDescription()+ "].");
				Rpt.FaultNodo faultCode = Rpt.FaultNodo.valueOf(Rpt.FaultNodo.class, risposta.getFault().getFaultCode());
				if(risposta.getFault().getId().equals(Rpt.NdPFaultId)) {
					log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in RPT_RIFIUTATA_NODO.");
					rptBD.updateStatoRpt(rpt.getId(), StatoRpt.RPT_RIFIUTATA_NODO, faultCode, risposta.getFault().getFaultString() + ": " + risposta.getFault().getDescription());
					log.info("Aggiorno lo stato del versamento in StatoVersamento.IN_ATTESA.");
					versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.IN_ATTESA);
				} else {
					log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in RPT_RIFIUTATA_PSP.");
					rptBD.updateStatoRpt(rpt.getId(), StatoRpt.RPT_RIFIUTATA_PSP, faultCode, risposta.getFault().getFaultString() + ": " + risposta.getFault().getDescription());
					log.info("Aggiorno lo stato del versamento in StatoVersamento.FALLITO.");
					versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.FALLITO);
				}
				this.commit();
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, faultCode, risposta.getFault().getFaultString() + ": " + risposta.getFault().getDescription());
			} else {
				new it.govpay.business.Mail(this).generaNotificaRPT(ente, versamento, rpt);
				if(risposta.getRedirect() == 1) {
					log.info("Ricevuto esito " + risposta.getEsito() + " con URL di redirect: " + risposta.getUrl());
					log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in RPT_ACCETTATA_NODO.");
					rptBD.updateStatoRpt(rpt.getId(), StatoRpt.RPT_ACCETTATA_NODO, null, null);
					if(canale.getModelloPagamento().equals(ModelloPagamento.DIFFERITO)) {
						log.info("Aggiorno lo stato dei versamento in StatoVersamento.AUTORIZZATO_DIFFERITO.");
						versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.AUTORIZZATO_DIFFERITO);
					} else {
						log.info("Aggiorno lo stato dei versamento in StatoVersamento.AUTORIZZATO_IMMEDIATO.");
						versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.AUTORIZZATO_IMMEDIATO);
					}
					try {
						String codSessione = UrlUtils.getCodSessione(risposta.getUrl());
						callbackUrl = callbackUrl.replaceAll(Pattern.quote("{idSession}"), codSessione);
						rptBD.updateRpt(rpt.getId(), codSessione, risposta.getUrl());
					} catch (Exception e) {
						log.warn("Impossibile decodificare l'idSessione dalla URL [" + risposta.getUrl() + "]: " + e.getMessage());
						callbackUrl = callbackUrl.replaceAll(Pattern.quote("{idSession}"), "");
						rptBD.updateRpt(rpt.getId(), null, risposta.getUrl());
					}
					callbackUrl = callbackUrl.replaceAll(Pattern.quote("{esito}"), risposta.getEsito());
					this.commit();
					return risposta.getUrl();
				} else {
					log.info("Ricevuto esito " + risposta.getEsito() + " senza URL di redirect");
					log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in RPT_ACCETTATA_NODO.");
					rptBD.updateStatoRpt(rpt.getId(), StatoRpt.RPT_ACCETTATA_NODO, null, null);
					if(canale.getModelloPagamento().equals(ModelloPagamento.DIFFERITO)) {
						log.info("Aggiorno lo stato dei versamento in StatoVersamento.AUTORIZZATO_DIFFERITO.");
						versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.AUTORIZZATO_DIFFERITO);
					} else {
						log.info("Aggiorno lo stato dei versamento in StatoVersamento.AUTORIZZATO_IMMEDIATO.");
						versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.AUTORIZZATO_IMMEDIATO);
					}
					callbackUrl = callbackUrl.replaceAll(Pattern.quote("{idSession}"), "");
					callbackUrl = callbackUrl.replaceAll(Pattern.quote("{esito}"), "DIFFERITO");
					this.commit();
					return callbackUrl;
				}
			}
		} catch (Exception e) {
			if( e instanceof GovPayException)
				throw (GovPayException) e;
			log.error("Errore interno: " + e, e);
			this.rollback();
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}

	/**
	 * Esegue il pagamento di un carrello di RPT.
	 * 
	 * L'applicazione fornita in input viene utilizzata come stazione intermediario 
	 * e deve essere associata a tutti i tributi coinvolti nel pagamento.
	 * 
	 * 
	 * 
	 * @param applicazione
	 * @param idVersamenti
	 * @param ibanAddebito
	 * @param canale
	 * @param firma
	 * @param autenticazione
	 * @param versante
	 * @param callbackUrl
	 * @return
	 * @throws GovPayException
	 */
	public String eseguiPagamentoCarrelloEnte(long idPortale, List<Long> idVersamenti, long idCanale, String ibanAddebito, FirmaRichiesta firma, Autenticazione autenticazione, Anagrafica versante, String callbackUrl) throws GovPayException {
		log.debug("Esecuzione di pagamento di un carrello ad iniziativa ente [idCanale: " + idCanale +"]");

		try  {
			this.setAutoCommit(false);

			Portale portale = AnagraficaManager.getPortale(this, idPortale);

			Stazione stazione = AnagraficaManager.getStazione(this, portale.getIdStazione());

			Intermediario intermediario = AnagraficaManager.getIntermediario(this, stazione.getIdIntermediario());

			Canale canale = AnagraficaManager.getCanale(this, idCanale);

			String codCarrello = UUID.randomUUID().toString().replaceAll("-", "");

			List<RptExtended> carrelloRpt = new ArrayList<RptExtended>();
			List<Versamento> carrelloVersamenti = new ArrayList<Versamento>();
			List<Ente> carrelloEnte = new ArrayList<Ente>();
			List<Long> idsRpt = new ArrayList<Long>();
			log.info("Preparazione Carrello RPT [codCarrello: "+ codCarrello + "]");

			VersamentiBD versamentiBD = new VersamentiBD(this);
			PagamentiUtils pagamentiUtils = new PagamentiUtils();
			for(long idVersamento : idVersamenti) {
				Versamento versamento = versamentiBD.getVersamento(idVersamento);
				Ente ente = AnagraficaManager.getEnte(this, versamento.getIdEnte());
				RptExtended rptExtended = pagamentiUtils.buildRpt(versamento, stazione.getId(), ente.getAnagraficaEnte(), portale, Rpt.CCP_NA, codCarrello, ibanAddebito, canale, firma, autenticazione, versante, callbackUrl, this);
				idsRpt.add(rptExtended.getRpt().getId());
				carrelloRpt.add(rptExtended);
				carrelloEnte.add(ente);
				carrelloVersamenti.add(versamento);
			}

			RptBD rptBD = new RptBD(this);
			rptBD.insertCarrelloRpt(codCarrello, idsRpt);

			this.commit();
			closeConnection();

			log.info("Spedizione Carrello RPT al Nodo dei Pagamenti [codCarrello: "+ codCarrello + "]");

			TipoListaRPT listaRpt = new TipoListaRPT();

			for(RptExtended rptExtended : carrelloRpt) {
				TipoElementoListaRPT elementoListaRPT = new TipoElementoListaRPT();
				elementoListaRPT.setCodiceContestoPagamento(rptExtended.getRpt().getCcp());
				elementoListaRPT.setIdentificativoDominio(rptExtended.getRpt().getCodDominio());
				elementoListaRPT.setIdentificativoUnivocoVersamento(rptExtended.getRpt().getIuv());
				elementoListaRPT.setTipoFirma(firma.getCodifica());
				elementoListaRPT.setRpt(rptExtended.getRptXml());
				listaRpt.getElementoListaRPT().add(elementoListaRPT);
			}

			NodoInviaCarrelloRPT inviaCarrelloRPT = new NodoInviaCarrelloRPT();

			inviaCarrelloRPT.setIdentificativoCanale(canale.getCodCanale());
			inviaCarrelloRPT.setIdentificativoIntermediarioPSP(canale.getCodIntermediario());
			inviaCarrelloRPT.setIdentificativoPSP(canale.getPsp().getCodPsp());
			inviaCarrelloRPT.setPassword(stazione.getPassword());
			inviaCarrelloRPT.setListaRPT(listaRpt);

			NodoPerPa client = new NodoPerPa(intermediario);

			NodoInviaCarrelloRPTRisposta risposta;
			try {
				risposta = client.nodoInviaCarrelloRPT(intermediario, stazione, canale, inviaCarrelloRPT, codCarrello);
			} catch (GovPayException e) {
				setupConnection();
				setAutoCommit(false);

				rptBD = new RptBD(this);

				// ERRORE DI RETE. Non so se la RPT e' stata effettivamente consegnata.
				log.error("Errore di rete nella spedizione del carrello RPT [codCarrello: "+ codCarrello + "]: " + e);
				// Imposto lo stato in errore invio e lo lascio aggiornare al batch di controllo.

				String pspBackURL = null;
				for(RptExtended rptExtended : carrelloRpt) {
					Rpt rpt = rptExtended.getRpt();
					log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in RPT_ERRORE_INVIO_A_NODO.");
					rpt.setStatoRpt(StatoRpt.RPT_ERRORE_INVIO_A_NODO);
					rpt.setFaultCode(null);
					rpt.setDescrizioneStato(e.getMessage());
					rptBD.updateStatoRpt(rpt.getId(), rpt.getStatoRpt(), rpt.getFaultCode(), rpt.getDescrizioneStato());
					this.commit();
					aggiornaRptDaNpD(rpt);
					this.commit();
					if(rpt.getPspRedirectURL() != null) {
						pspBackURL = rpt.getPspRedirectURL();
					}
				}
				if(pspBackURL != null) {
					return pspBackURL;
				} else {
					throw e;
				}
			}

			setupConnection();
			setAutoCommit(false);

			versamentiBD = new VersamentiBD(this);
			rptBD = new RptBD(this);

			if(risposta.getFault() != null) {
				log.error("Ricevuto esito KO [FaultId: " + risposta.getFault().getId() + "] [FaultCode: " + risposta.getFault().getFaultCode() + "] [FaultString: " + risposta.getFault().getFaultString() + "] [FaultDescription: " +  risposta.getFault().getDescription()+ "].");
				Rpt.FaultNodo faultCode = Rpt.FaultNodo.valueOf(Rpt.FaultNodo.class, risposta.getFault().getFaultCode());
				if(risposta.getFault().getId().equals(Rpt.NdPFaultId)) {
					for(RptExtended rptExtended : carrelloRpt) {
						Rpt rpt = rptExtended.getRpt();
						rptBD.updateStatoRpt(rpt.getId(), StatoRpt.RPT_RIFIUTATA_NODO, faultCode, risposta.getFault().getFaultString() + ": " + risposta.getFault().getDescription());
						log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in RPT_RIFIUTATA_NODO.");
						versamentiBD.updateStatoVersamento(rpt.getIdVersamento(), StatoVersamento.IN_ATTESA);
						log.info("Aggiorno lo stato del versamento [idVersamento: " + rpt.getIdVersamento() + "] in StatoVersamento.IN_ATTESA.");
					}
				} else {
					for(RptExtended rptExtended : carrelloRpt) {
						Rpt rpt = rptExtended.getRpt();
						rptBD.updateStatoRpt(rpt.getId(), StatoRpt.RPT_RIFIUTATA_PSP, faultCode, risposta.getFault().getFaultString() + ": " + risposta.getFault().getDescription());
						log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in RPT_RIFIUTATA_PSP.");
						versamentiBD.updateStatoVersamento(rpt.getIdVersamento(), StatoVersamento.FALLITO);
						log.info("Aggiorno lo stato del versamento [idVersamento: " + rpt.getIdVersamento() + "] in " + StatoVersamento.FALLITO + ".");
					}
				}
				this.commit();
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, faultCode, risposta.getFault().getFaultString() + ": " + risposta.getFault().getDescription());			
			} else {

				it.govpay.business.Mail mailBD = new it.govpay.business.Mail(this);
				for(int i =0; i<carrelloRpt.size(); i++) {
					mailBD.generaNotificaRPT(carrelloEnte.get(i), carrelloVersamenti.get(i), carrelloRpt.get(i).getRpt());
				}


				if(risposta.getUrl() != null) {
					log.info("Ricevuto esito ok con URL di redirect: " + risposta.getUrl());
					for(RptExtended rptExtended : carrelloRpt) {
						Rpt rpt = rptExtended.getRpt();
						log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in RPT_ACCETTATA_NODO.");
						rptBD.updateStatoRpt(rpt.getId(), StatoRpt.RPT_ACCETTATA_NODO, null, null);
						if(canale.getModelloPagamento().equals(ModelloPagamento.DIFFERITO)) {
							log.info("Aggiorno lo stato del versamento [idVersamento: " + rpt.getIdVersamento() + "] in StatoVersamento.AUTORIZZATO_DIFFERITO.");
							versamentiBD.updateStatoVersamento(rpt.getIdVersamento(), StatoVersamento.AUTORIZZATO_DIFFERITO);
						} else {
							log.info("Aggiorno lo stato del versamento [idVersamento: " + rpt.getIdVersamento() + "] in StatoVersamento.AUTORIZZATO_IMMEDIATO.");
							versamentiBD.updateStatoVersamento(rpt.getIdVersamento(), StatoVersamento.AUTORIZZATO_IMMEDIATO);
						}
						try {
							String codSessione = UrlUtils.getCodSessione(risposta.getUrl());
							rptBD.updateRpt(rpt.getId(), codSessione, risposta.getUrl());
						} catch (Exception e) {
							log.warn("Impossibile decodificare l'idSessione dalla URL [" + risposta.getUrl() + "]: " + e.getMessage());
							rptBD.updateRpt(rpt.getId(), null, risposta.getUrl());
						}
					}
					this.commit();
					return risposta.getUrl();
				} else {
					log.info("Ricevuto esito ok senza URL di redirect");
					for(RptExtended rptExtended : carrelloRpt) {
						Rpt rpt = rptExtended.getRpt();
						log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in RPT_ACCETTATA_NODO.");
						rptBD.updateStatoRpt(rpt.getId(), StatoRpt.RPT_ACCETTATA_NODO, null, null);
						if(canale.getModelloPagamento().equals(ModelloPagamento.DIFFERITO)) {
							log.info("Aggiorno lo stato del versamento [idVersamento: " + rpt.getIdVersamento() + "] in StatoVersamento.AUTORIZZATO_DIFFERITO.");
							versamentiBD.updateStatoVersamento(rpt.getIdVersamento(), StatoVersamento.AUTORIZZATO_DIFFERITO);
						} else {
							log.info("Aggiorno lo stato del versamento [idVersamento: " + rpt.getIdVersamento() + "] in StatoVersamento.AUTORIZZATO_IMMEDIATO.");
							versamentiBD.updateStatoVersamento(rpt.getIdVersamento(), StatoVersamento.AUTORIZZATO_IMMEDIATO);
						}
					}
					callbackUrl = callbackUrl.replaceAll(Pattern.quote("{idSession}"), "");
					callbackUrl = callbackUrl.replaceAll(Pattern.quote("{esito}"), risposta.getEsitoComplessivoOperazione());
					this.commit();
					return callbackUrl;
				}
			}
		} catch (Exception e) {
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			log.error("Errore interno: " + e);
			this.rollback();
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}

	public void acquisisciRT(String codDominio, String iuv, String ccp, CtRicevutaTelematica ctRt, byte[] ctRtByte) throws GovPayException {
		try {
			log.info("Acquisizione della Ricevuta Telematica [CodMsgRicevuta: " + ctRt.getIdentificativoMessaggioRicevuta() + "]");
			RtBD rtBD = new RtBD(this);
			try {
				Rt rtLetta = rtBD.getRt(ctRt.getIdentificativoMessaggioRicevuta());
				// TODO E' possibile che una RT sia rispedita con lo stesso Identificativo ma diverso contenuto??
				// Duplicato.
				log.error("Ricevuta telematica duplicata [Id originale: " + rtLetta.getId() + "]. ");
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_RT_DUPLICATA, "Ricevuta telematica gia' acquisita con stato " + rtLetta.getStato());
			} catch (NotFoundException e) {
				//Ok... non e' un duplicato;
				//Procedo all'elaborazione.
			}

			log.debug("Recupero della Richiesta Pagamento Telematico [CodMessaggioRichiesta: " + ctRt.getRiferimentoMessaggioRichiesta() + "]");

			RptBD rptBD = new RptBD(this);
			Rpt rpt = null;
			try {
				rpt = rptBD.getRpt(ctRt.getRiferimentoMessaggioRichiesta());
				Canale canale = AnagraficaManager.getPsp(this, rpt.getIdPsp()).getCanale(rpt.getTipoVersamento(), null);
				ThreadContext.put(it.govpay.bd.model.Evento.CANALE, canale.getCodCanale());
				ThreadContext.put(it.govpay.bd.model.Evento.ID_PSP, canale.getPsp().getCodPsp());
				ThreadContext.put(it.govpay.bd.model.Evento.TIPO_VERSAMENTO, canale.getTipoVersamento().getCodifica());
			} catch (Exception e) {
				log.error("Impossibile individuare l'Rpt [CodMessaggioRichiesta: " + ctRt.getRiferimentoMessaggioRichiesta() + "]", e);
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_RPT_SCONOSCIUTA, "Impossibile individuare l'Rpt riferita dall'Rt [CodMessaggioRichiesta: " + ctRt.getRiferimentoMessaggioRichiesta() + "]", e);
			}

			// Verifico lo stato dell'RPT
			if(rpt.getStatoRpt().equals(StatoRpt.RT_ACCETTATA_PA)) {
				log.error("L'Rpt riferita risulta gia' in stato RT_ACCETTATA_PA. [CodMessaggioRichiesta: " + ctRt.getRiferimentoMessaggioRichiesta() + "]");
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_RT_DUPLICATA, "L'Rpt riferita risulta gia' in stato RT_ACCETTATA_PA");
			}
			
			if(rpt.getStatoRpt().equals(StatoRpt.RPT_ATTIVATA)) {
				log.error("L'Rpt riferita risulta in stato RPT_ATTIVATA. [CodMessaggioRichiesta: " + ctRt.getRiferimentoMessaggioRichiesta() + "]");
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_RPT_SCONOSCIUTA, "L'Rpt riferita risulta da inviare al Nodo");
			}

			Rt rt = new Rt();
			Anagrafica anagraficaAttestante = new Anagrafica();
			anagraficaAttestante.setCap(ctRt.getIstitutoAttestante().getCapAttestante());
			anagraficaAttestante.setCivico(ctRt.getIstitutoAttestante().getCivicoAttestante());
			anagraficaAttestante.setRagioneSociale(ctRt.getIstitutoAttestante().getDenominazioneAttestante());
			anagraficaAttestante.setCodUnivoco(ctRt.getIstitutoAttestante().getIdentificativoUnivocoAttestante().getCodiceIdentificativoUnivoco());
			anagraficaAttestante.setIndirizzo(ctRt.getIstitutoAttestante().getIndirizzoAttestante());
			anagraficaAttestante.setLocalita(ctRt.getIstitutoAttestante().getLocalitaAttestante());
			anagraficaAttestante.setNazione(ctRt.getIstitutoAttestante().getNazioneAttestante());
			anagraficaAttestante.setProvincia(ctRt.getIstitutoAttestante().getProvinciaAttestante());
			rt.setAnagraficaAttestante(anagraficaAttestante);
			rt.setCodMsgRicevuta(ctRt.getIdentificativoMessaggioRicevuta());
			rt.setDataOraMsgRicevuta(ctRt.getDataOraMessaggioRicevuta());
			rt.setEsitoPagamento(EsitoPagamento.toEnum(ctRt.getDatiPagamento().getCodiceEsitoPagamento()));
			rt.setImportoTotalePagato(ctRt.getDatiPagamento().getImportoTotalePagato());
			rt.setIdRpt(rpt.getId());
			log.debug("Recupero il tracciato dell'Rpt. [IdTracciato: " + rpt.getIdTracciatoXML() + "]");
			TracciatiBD tracciatiBD = new TracciatiBD(this);
			CtRichiestaPagamentoTelematico ctRpt;
			try {
				byte[] byteRpt = tracciatiBD.getTracciato(rpt.getIdTracciatoXML());
				ctRpt = JaxbUtils.toRPT(byteRpt);
			} catch (Exception e) {
				rt.setStato(StatoRt.RIFIUTATA);
				rt.setDescrizioneStato("Errore nel recupero dell'RPT originale: " + e.getMessage());
				setAutoCommit(false);
				rtBD.insertRt(rt, ctRtByte);
				rptBD.updateStatoRpt(rpt.getId(), StatoRpt.RT_RIFIUTATA_PA, null, "Errore nel recupero dell'RPT originale: " + e.getMessage());
				this.commit();
				log.error("Impossibile individuare il tracciato dell'Rpt. [IdTracciato: " + rpt.getIdTracciatoXML() + "]", e);
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_RPT_SCONOSCIUTA, "Impossibile recuperare il tracciato XML dell'RPT [CodDominio: " + codDominio + "][IUV: " + iuv + "][CCP: " + ccp + "]", e);
			}

			log.debug("Verifica della coerenza tra Rpt ed Rt.");
			try { 
				new ValidatoreRT().validaSemantica(ctRpt, ctRt);
			} catch (GovPayNdpException e) {
				rt.setStato(StatoRt.RIFIUTATA);
				rt.setDescrizioneStato(e.getDescrizione());
				setAutoCommit(false);
				rtBD.insertRt(rt, ctRtByte);
				rptBD.updateStatoRpt(rpt.getId(), StatoRpt.RT_RIFIUTATA_PA, null, e.getDescrizione());
				this.commit();
				log.error("L'Rt non risulta coerente con l'Rpt corrispondente: " + e.getDescrizione() + ". Rt rifiutata. Rpt aggiornata in stato RT_RIFIUTATA_PA.");
				throw e;
			}

			log.info("Acquisizione RT con esito " + rt.getEsitoPagamento() + " per un importo di " + rt.getImportoTotalePagato());
			rt.setDescrizioneStato("Acquisizione RT con esito " + rt.getEsitoPagamento() + " per un importo di " + rt.getImportoTotalePagato() + " eseguita con successo");

			// Aggiorno il versamento con i dati della ricevuta 
			VersamentiBD versamentiBD = new VersamentiBD(this);
			Versamento versamento = versamentiBD.getVersamento(ctRpt.getDominio().getIdentificativoDominio(), ctRpt.getDatiVersamento().getIdentificativoUnivocoVersamento());
			versamento.setStato(StatoVersamento.valueOf(rt.getEsitoPagamento().toString()));
			versamento.setDescrizioneStato(rt.getDescrizioneStato());
			versamento.setImportoPagato(rt.getImportoTotalePagato());
			for(int indice = 0; indice < ctRt.getDatiPagamento().getDatiSingoloPagamento().size(); indice++) {
				CtDatiSingoloPagamentoRT ctDatiSingoloPagamentoRT = ctRt.getDatiPagamento().getDatiSingoloPagamento().get(indice);
				SingoloVersamento singoloVersamento = versamento.getSingoloVersamento(indice);
				singoloVersamento.setDataEsitoSingoloPagamento(ctDatiSingoloPagamentoRT.getDataEsitoSingoloPagamento());
				singoloVersamento.setEsitoSingoloPagamento(ctDatiSingoloPagamentoRT.getEsitoSingoloPagamento());
				singoloVersamento.setIur(ctDatiSingoloPagamentoRT.getIdentificativoUnivocoRiscossione());
				singoloVersamento.setSingoloImportoPagato(ctDatiSingoloPagamentoRT.getSingoloImportoPagato());
				singoloVersamento.setDatiSpecificiRiscossione(ctDatiSingoloPagamentoRT.getDatiSpecificiRiscossione());
				if(ctDatiSingoloPagamentoRT.getSingoloImportoPagato().compareTo(BigDecimal.ZERO) == 0) {
					singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.DA_PAGARE);
				} else {
					singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.PAGATO);
				}
				log.debug("Aggiornamento SingoloVersamento [IUV: " + versamento.getIuv() + "][Indice: " + singoloVersamento.getIndice() + "] in stato [" + singoloVersamento.getStatoSingoloVersamento() + "]");
			}
			
			rt.setStato(StatoRt.ACCETTATA);
			
			Applicazione applicazione = AnagraficaManager.getApplicazione(this, versamento.getIdApplicazione());
			Ente ente = AnagraficaManager.getEnte(this, versamento.getIdEnte());
			MailBD mailBD = new MailBD(this);
			it.govpay.bd.model.Mail mail = new Mail(this).generaNotificaRT(ente, versamento, rpt, rt);
			
			Esito esito = EsitoFactory.newEsito(this, applicazione, versamento, rpt, rt, ctRtByte);
			EsitiBD esiti = new EsitiBD(this);
			
			setAutoCommit(false);
			versamentiBD.updateVersamento(versamento);
			rtBD.insertRt(rt, ctRtByte);
			rptBD.updateStatoRpt(rpt.getId(), StatoRpt.RT_ACCETTATA_PA, null, null);
			if(mail != null) mailBD.insertMail(mail);
			esiti.insertEsito(esito);
			this.commit();
			
			InviaEsitoThread inviaRptThread = null;
			if(applicazione.getConnettoreEsito() != null) {
				inviaRptThread = new InviaEsitoThread(applicazione, esito, ThreadContext.getImmutableContext());
				Executors.newSingleThreadExecutor().execute(inviaRptThread);
			}
		} catch (Exception e) {
			if(e instanceof GovPayNdpException)
				throw (GovPayNdpException) e;
			this.rollback();
			log.error("Errore interno", e);
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_INTERNO, FaultPa.PAA_ERRORE_INTERNO, "Errore durante l'acquisizione dell'RT", e);
		}
	}


	public boolean aggiornaRptDaNpD(Rpt rpt) throws GovPayException {
		try {
			log.info("Ricevuta richiesta di aggiornamento stato Rpt");
			setupConnection();
			RptBD rptBD = new RptBD(this);
			rpt = rptBD.getRpt(rpt.getId());
			closeConnection();
			// Se la distinta e' in uno stato non finale, tento un aggiornamento.
			switch (rpt.getStatoRpt()) {
			case RPT_RIFIUTATA_NODO:
			case RPT_RIFIUTATA_PSP:
			case RPT_ERRORE_INVIO_A_PSP:
			case RT_ACCETTATA_PA:
				log.info("Rpt in stato terminale [" + rpt.getStatoRpt()+ "]. Aggiornamento non necessario.");
				return false;
			default:
				log.info("Rpt in stato non terminale [" + rpt.getStatoRpt()+ "]. Eseguo un aggiornamento dello stato.");
				
				setupConnection();
				VersamentiBD versamentiBD = new VersamentiBD(this);
				Versamento versamento = versamentiBD.getVersamento(rpt.getIdVersamento());
				Stazione stazione = AnagraficaManager.getStazione(this, rpt.getIdStazione());
				Intermediario intermediario = AnagraficaManager.getIntermediario(this, stazione.getIdIntermediario());
				closeConnection();
				
				NodoPerPa client = new NodoPerPa(intermediario);
				NodoChiediStatoRPT richiesta = new NodoChiediStatoRPT();
				richiesta.setIdentificativoDominio(versamento.getCodDominio());
				richiesta.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
				richiesta.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
				richiesta.setPassword(stazione.getPassword());
				richiesta.setIdentificativoUnivocoVersamento(versamento.getIuv());
				richiesta.setCodiceContestoPagamento(rpt.getCcp());
				NodoChiediStatoRPTRisposta risposta = client.nodoChiediStatoRpt(richiesta, intermediario.getDenominazione());
				if(risposta.getFault() != null) {
					if(FaultNodo.valueOf(risposta.getFault().getFaultCode()).equals(FaultNodo.PPT_RPT_SCONOSCIUTA)) {
						log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in " + StatoRpt.RPT_INVIO_A_NODO_FALLITO + ".");
						rpt.setStatoRpt(StatoRpt.RPT_INVIO_A_NODO_FALLITO);
						rpt.setDescrizioneStato(null);
						rpt.setFaultCode(null);
						log.info("Aggiorno lo stato del versamentoP [idVersamento: " + versamento.getId() + "] in " + StatoVersamento.IN_ATTESA);
						versamento.setStato(StatoVersamento.IN_ATTESA);
						
						setupConnection();
						setAutoCommit(false);
						rptBD = new RptBD(this);
						rptBD.updateStatoRpt(rpt.getId(), StatoRpt.RPT_INVIO_A_NODO_FALLITO, null, null);
						versamentiBD = new VersamentiBD(this);
						versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.IN_ATTESA);
						this.commit();
						return true;
					}
					log.error("Riscontrato errore nella richiesta al nodo: [" + risposta.getFault().getFaultCode() + "] " + (risposta.getFault().getFaultString() != null ? risposta.getFault().getFaultString() : ""));
					throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP, risposta.getFault().getFaultCode() + ": " + risposta.getFault().getFaultString() != null ? risposta.getFault().getFaultString() : "");
				} else {
					StatoRpt nuovoStato = Rpt.StatoRpt.valueOf(risposta.getEsito().getStato());
					log.info("Acquisito stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "]: " + nuovoStato + ".");
					if(nuovoStato.equals(rpt.getStatoRpt())) {
						log.info("Stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] invariato.");
						return false;
					}
					switch (nuovoStato) {
					case RT_ACCETTATA_NODO:
					case RT_ESITO_SCONOSCIUTO_PA:
					case RT_RIFIUTATA_PA:
					case RT_ACCETTATA_PA:
						log.info("Richiesta dell'RT al Nodo dei Pagamenti [CodDominio: " + versamento.getCodDominio() + "][IUV: " + versamento.getIuv() + "].");

						NodoChiediCopiaRT nodoChiediCopiaRT = new NodoChiediCopiaRT();
						nodoChiediCopiaRT.setIdentificativoDominio(versamento.getCodDominio());
						nodoChiediCopiaRT.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
						nodoChiediCopiaRT.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
						nodoChiediCopiaRT.setPassword(stazione.getPassword());
						nodoChiediCopiaRT.setIdentificativoUnivocoVersamento(versamento.getIuv());
						nodoChiediCopiaRT.setCodiceContestoPagamento(rpt.getCcp());
						NodoChiediCopiaRTRisposta nodoChiediCopiaRTRisposta = client.nodoChiediCopiaRT(nodoChiediCopiaRT, intermediario.getDenominazione());

						setupConnection();
						rptBD = new RptBD(this);

						if(nodoChiediCopiaRTRisposta.getFault() != null) {
							log.info("Fault nell'acquisizione dell'RT: [" + risposta.getFault().getFaultCode() + "] " + risposta.getFault().getFaultString());
							log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in " + nuovoStato + ".");
							rptBD.updateStatoRpt(rpt.getId(), nuovoStato, null, null);
							rpt.setStatoRpt(nuovoStato);
							rpt.setDescrizioneStato(null);
							rpt.setFaultCode(null);
							return false;
						}

						FirmaRichiesta firma = null;
						try {
							firma = FirmaRichiesta.toEnum(nodoChiediCopiaRTRisposta.getTipoFirma());
						} catch (ServiceException e) {
							log.error("Ricevuto TipoFirma non gestito [" + nodoChiediCopiaRTRisposta.getTipoFirma() + "]");
							throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE, "Ricevuto TipoFirma non gestito [" + nodoChiediCopiaRTRisposta.getTipoFirma() + "]");
						}
						ValidatoreRT validatoreRT = new ValidatoreRT();
						log.info("Verifica della firma della Ricevuta Telematica");

						byte[] rtByte = null;
						try {
							ByteArrayOutputStream output = new ByteArrayOutputStream();
							DataHandler dh = nodoChiediCopiaRTRisposta.getRt();
							dh.writeTo(output);
							rtByte = output.toByteArray();
						} catch (IOException e) {
							log.error("Errore durante la lettura dell'RT: " + e);
							throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
						}

						byte[] ctRtByte = validatoreRT.validaFirma(firma, rtByte);
						log.info("Validazione della Ricevuta Telematica");
						CtRicevutaTelematica ctRt = null;
						try {
							ctRt = JaxbUtils.toRT(ctRtByte);
						} catch (Exception e) {
							log.error("Errore durante la validazione sintattica della Ricevuta Telematica.", e);
							throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, e.getMessage());
						}
						Pagamenti pagamenti = new Pagamenti(this);
						pagamenti.acquisisciRT(versamento.getCodDominio(), versamento.getIuv(), rpt.getCcp(), ctRt, rtByte);
						return true;
					case RPT_RIFIUTATA_NODO:
					case RPT_ERRORE_INVIO_A_PSP:
						log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in " + nuovoStato + ".");
						rpt.setStatoRpt(nuovoStato);
						rpt.setDescrizioneStato(null);
						rpt.setFaultCode(null);
						log.info("Aggiorno lo stato del versamento [idVersamento: " + versamento.getId() + "] in " + StatoVersamento.IN_ATTESA);
						versamento.setStato(StatoVersamento.IN_ATTESA);
						
						setupConnection();
						setAutoCommit(false);
						rptBD = new RptBD(this);
						rptBD.updateStatoRpt(rpt.getId(), nuovoStato, null, null);
						versamentiBD = new VersamentiBD(this);
						versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.IN_ATTESA);
						this.commit();
						return true;
					case RPT_RIFIUTATA_PSP:
						log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in " + nuovoStato + ".");
						rpt.setStatoRpt(nuovoStato);
						rpt.setDescrizioneStato(null);
						rpt.setFaultCode(null);
						log.info("Aggiorno lo stato del versamento [idVersamento: " + versamento.getId() + "] in " + StatoVersamento.FALLITO);
						versamento.setStato(StatoVersamento.FALLITO);
						
						setupConnection();
						setAutoCommit(false);
						versamentiBD = new VersamentiBD(this);
						versamentiBD.updateStatoVersamento(versamento.getId(), StatoVersamento.FALLITO);
						rptBD = new RptBD(this);
						rptBD.updateStatoRpt(rpt.getId(), nuovoStato, null, null);
						this.commit();
						return true;
					default:
						log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in " + nuovoStato + ".");
						rpt.setStatoRpt(nuovoStato);
						if(risposta.getEsito().getRedirect() == 1 && rpt.getCodSessione()==null)
							rpt.setPspRedirectURL(risposta.getEsito().getUrl());
						rpt.setDescrizioneStato(null);
						rpt.setFaultCode(null);
						setupConnection();
						rptBD = new RptBD(this);
						rptBD.updateStatoRpt(rpt.getId(), nuovoStato, null, null);
						return false;
					}
				}
			}
		} catch (Exception e) {
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			this.rollback();
			log.error("Errore interno", e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore durante l'acquisizione dello StatoRPT", e);
		}

	}

	public Versamento verifica(String codIntermediario, String codStazione, String codDominio, String iuv, String ccp) throws GovPayException {
		VersamentiBD versamentiBD = null;
		try {
			versamentiBD = new VersamentiBD(this);
			// Controllo se e' presente localmente
			Versamento versamento = versamentiBD.getVersamento(codDominio, iuv);
			// Controllo che non sia scaduto
			log.info("Versamento riferito dall'RPT presente in stato [StatoVersamento: " + versamento.getStato() + "]");
			switch (versamento.getStato()) {
			case IN_ATTESA:
				if(versamento.getDataScadenza().after(new Date())) {
					return versamento;
				} else {
					log.info("Versamento scaduto. Procedo a richiedere un aggiornamento o una conferma di scadenza");
					log.warn(">>> TODO <<<");
					// TODO Chiedere un aggiornamento!
					// Vado allo scaduto
				}
			case SCADUTO:
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_PAGAMENTO_SCADUTO, "Versamento scaduto il " + versamento.getDataScadenza());
			case ANNULLATO:
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_PAGAMENTO_ANNULLATO, "Versamento in stato " + versamento.getStato() + ".");
			case IN_CORSO:
			case AUTORIZZATO:
			case AUTORIZZATO_DIFFERITO:
			case AUTORIZZATO_IMMEDIATO:
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_PAGAMENTO_IN_CORSO, "Versamento in stato " + versamento.getStato() + ".");
			case DECORRENZA_TERMINI:
			case DECORRENZA_TERMINI_PARZIALE:
			case PAGAMENTO_ESEGUITO:
			case PAGAMENTO_NON_ESEGUITO:
			case PAGAMENTO_PARZIALMENTE_ESEGUITO:
			case FALLITO:
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_PAGAMENTO_DUPLICATO, "Versamento in stato " + versamento.getStato() + ".");
			}
		} catch (NotFoundException e) {
			// Non trovato. Chiedere all'applicazione
			long idApplicazione = 0;
			String codApplicazione = null;

			Dominio dominio;
			try {
				dominio = AnagraficaManager.getDominio(this, codDominio);
			} catch (Exception e4) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_ID_DOMINIO_ERRATO, "Dominio [CodDominio: " + codDominio + "] non presente in anagrafica domini.");
			}

			try {
				idApplicazione = versamentiBD.getIuv(codDominio, iuv).getIdApplicazione();
			} catch (NotFoundException ee) {
				log.debug("IUV non generato internamente. Verifico se disponibile un plugin per il discovery dell'applicazione.");

				String pluginClass = dominio.getPluginClass();
				if(pluginClass == null) {
					log.debug("Nessun plugin disponibile per il discovery dell'applicazione. Pagamento Sconosciuto.");
					throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, "Versamento non in Repository.");
				}

				PluginGovPay plugin;
				try {
					Object pluginInstance = Class.forName(pluginClass).newInstance();
					if(pluginInstance instanceof PluginGovPay) {
						plugin = (PluginGovPay) pluginInstance;
					} else {
						log.error("Il plugin fornito [" + pluginClass + "] non implementa l'interfaccia " + PluginGovPay.class.getCanonicalName());
						throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_ERRORE_INTERNO, "Errore di integrazione con il servizio applicativo.");
					}
				} catch (ClassNotFoundException cnf) {
					log.error("Il plugin fornito [" + pluginClass + "] non e' disponibile nel classpath.");
					throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_ERRORE_INTERNO, "Errore di integrazione con il servizio applicativo.");
				} catch (InstantiationException e1) {
					log.error("Errore nell'instanziazione del plugin fornito [" + pluginClass + "].", e);
					throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_ERRORE_INTERNO, "Errore di integrazione con il servizio applicativo.");
				} catch (IllegalAccessException e1) {
					log.error("Errore nell'accesso del plugin fornito [" + pluginClass + "].", e);
					throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_ERRORE_INTERNO, "Errore di integrazione con il servizio applicativo.");
				}
				log.debug("Instanziato plugin [" + pluginClass + "] per il discovery dell'applicazione.");
				try {
					codApplicazione = plugin.getCodApplicazione(codIntermediario, codStazione, codDominio, iuv, ccp);
				} catch (it.govpay.plugin.NotFoundException nfe) {
					log.error("Il plugin non e' stato in grado di individuare l'applicazione.");
					throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, "Il plugin non e' stato in grado di individuare l'applicazione.");
				} catch (NotImplementedException nie) {
					log.error("Il plugin non e' ha implementato il metodo getCodApplicazione.");
					throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, "Il plugin non ha implementato il metodo per l'individuazione dell'applicazione.");
				}
			} catch (ServiceException e1) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore nell'acquisizione dell'applicazione", e);
			} catch (MultipleResultException e1) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore nell'acquisizione dell'applicazione", e);
			}

			Applicazione applicazione = null;

			try {
				if(idApplicazione != 0) {
					log.debug("Applicazione [idApplicazione: " + idApplicazione + "] identificata per la verifica del pagamento.");
					applicazione = AnagraficaManager.getApplicazione(this, idApplicazione);
				} else {
					log.debug("Applicazione [codApplicazione: " + codApplicazione + "] identificata per la verifica del pagamento.");
					applicazione = AnagraficaManager.getApplicazione(this, codApplicazione);
				}
			} catch (Exception se) {
				log.error("Errore nell'acquisizione dell'anagrafica dell'applicazione", e);
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore nell'acquisizione dell'applicazione", e);
			}

			Versamento versamento = null;
			try {
				if(applicazione.getVersione().equals(Applicazione.Versione.GPv1)) {
					List<Ente> enti = new EntiBD(this).getEntiByDominio(dominio.getId());
					if(enti.size() != 1) {
						throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "L'uso dell'interfaccia GovPay v1 non consente la gestione di piu' enti nello stesso dominio [CodDominio: " + dominio.getCodDominio() + "].");
					}
					Ente ente = enti.get(0);
					closeConnection();
					GpPerPa_v1 client = new GpPerPa_v1(applicazione, TipoConnettore.VERIFICA);
					Pagamento pagamento = client.verifica(codDominio, iuv);
					setupConnection();
					versamento = Converter.toVersamento(dominio, ente.getId(), applicazione, pagamento, null, this);
				} else {
					//TODO Invoca con il client v2. Crea un versamento o lancia un'eccezione
					throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "NON IMPLEMENTATO CLIENT V2.", e);
				}
			} catch (Exception e3) {
				if(e3 instanceof GovPayException)
					throw (GovPayException) e3;
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore durante l'invocazione del servizio di verifica del pagamento dell'applicazione.", e);
			}
			caricaPagamento(versamento);
			return versamento;
		} catch (Exception e) {
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore durante l'acquisizione del versamento", e);
		}
		throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore durante l'acquisizione del versamento");
	}


	public Versamento attiva(
			String codIntermediario, 
			String codStazione,
			String codDominio, 
			String iuv, 
			String ccp,
			String identificativoPSP, 
			Anagrafica pagatore, 
			Anagrafica versante,
			String ibanAddebito, 
			BigDecimal importo) throws GovPayException {

		try {
			Versamento versamento = verifica(codIntermediario, codStazione, codDominio, iuv, ccp);

			if(versamento.getImportoTotale().compareTo(importo) != 0)
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_ATTIVA_RPT_IMPORTO_NON_VALIDO, "Importo dell'RPT da Attivare diverso dal Versamento dovuto.");

			Stazione stazione = AnagraficaManager.getStazione(this, codStazione);

			Ente ente = AnagraficaManager.getEnte(this, versamento.getIdEnte());

			Psp psp = AnagraficaManager.getPsp(this, identificativoPSP);
			Canale canale = psp.getCanale(TipoVersamento.ATTIVATO_PRESSO_PSP, Psp.ModelloPagamento.ATTIVATO_PRESSO_PSP);
			
			setAutoCommit(false);
			RptExtended rptExtended = new PagamentiUtils().buildRpt(versamento, stazione.getId(), ente.getAnagraficaEnte(), null, ccp, null, ibanAddebito, canale, FirmaRichiesta.NESSUNA, Autenticazione.N_A, versante, null, this);
			this.commit();

			Intermediario intermediario = AnagraficaManager.getIntermediario(this, stazione.getIdIntermediario());

			InviaRptThread inviaRptThread = new InviaRptThread(rptExtended.getRpt(), rptExtended.getRptXml(), intermediario, stazione, canale);
			Executors.newSingleThreadExecutor().execute(inviaRptThread);
			return versamento;
		} catch (Exception e) {
			rollback();
			if(e instanceof GovPayException)
				throw (GovPayException) e;
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore durante l'attivazione dell'RPT", e);
		}
	}


	public void verificaRptPedenti() throws GovPayException {
		try {
			DominiBD dominiBD = new DominiBD(this);
			List<Dominio> domini = dominiBD.getDomini();

			closeConnection();
			for(Dominio dominio : domini) {
				setupConnection();
				Stazione stazione = AnagraficaManager.getStazione(this, dominio.getIdStazione());
				Intermediario intermediario = AnagraficaManager.getIntermediario(this, stazione.getIdIntermediario());
				closeConnection();

				log.trace("Recupero i pendenti [CodStazione: " + stazione.getCodStazione() + "][CodDominio: " + dominio.getCodDominio() + "]");
				// Costruisco una mappa di tutti i pagamenti pendenti sul nodo
				// La chiave di lettura e' iuv@ccp
				Map<String, StatoRpt> statiRptPendenti = new HashMap<String, StatoRpt>();
				
				NodoPerPa client = new NodoPerPa(intermediario);
				// Le pendenze per specifica durano 60 giorni.
				// Richiedo la lista degli ultimi 60 gg con range settimanale, poi tutto l'antecedente ai 60 gg per sicurezza.

				Calendar da = Calendar.getInstance();
				Calendar a = Calendar.getInstance();
				da.add(Calendar.DATE, -7);
				for(int i = 0; i<10; i++) {
					NodoChiediListaPendentiRPT richiesta = new NodoChiediListaPendentiRPT();
					richiesta.setIdentificativoDominio(dominio.getCodDominio());
					richiesta.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
					richiesta.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
					richiesta.setPassword(stazione.getPassword());
					richiesta.setDimensioneLista(BigInteger.valueOf(500l));
					richiesta.setRangeA(a.getTime());
					if(i<9) {
						richiesta.setRangeDa(da.getTime());
						log.debug("Richiedo la lista delle RPT pendenti (da " + (i*7) + " a " + ((i+1)*7) + " giorni fa)");
					} else {
						log.debug("Richiedo la lista delle RPT pendenti (oltre " + (i*7) + " giorni fa)");
					}
					NodoChiediListaPendentiRPTRisposta risposta = null;
					try {
						risposta = client.nodoChiediListaPendentiRPT(richiesta, intermediario.getDenominazione());
					} catch (Exception e) {
						log.error("Errore durante la richiesta di lista pendenti", e);
						continue;
					} 

					if(risposta == null) {
						log.debug("Lista pendenti vuota.");
						continue;
					}
					if(risposta.getFault() != null) {
						log.error("Ricevuto errore durante la richiesta di lista pendenti: " + risposta.getFault().getFaultCode() + ": " + risposta.getFault().getFaultString());
						continue;
					}

					for(TipoRPTPendente rptPendente : risposta.getListaRPTPendenti().getRptPendente()) {
						String rptKey = rptPendente.getIdentificativoUnivocoVersamento() + "@" + rptPendente.getCodiceContestoPagamento();
						StatoRpt stato = StatoRpt.valueOf(rptPendente.getStato());
						statiRptPendenti.put(rptKey, stato);
					}
				}

				log.info("Identificate sul NodoSPC " + statiRptPendenti.size() + " RPT pendenti");

				// Ho acquisito tutti gli stati pendenti. 
				// Tutte quelle in stato terminale, 
				setupConnection();

				RptBD rptBD = new RptBD(this);
				List<Rpt> rpts = rptBD.getRptPendenti(dominio.getCodDominio());

				log.info("Identificate su GovPay " + rpts.size() + " RPT pendenti");
				// Scorro le distinte. Se lo stato non c'e' (quindi non e' pendente) la mando in aggiornamento.
				for(Rpt rpt : rpts) {
					StatoRpt stato = statiRptPendenti.get(rpt.getCodDominio() + "@" + rpt.getCcp());
					if(stato != null) {
						log.debug("Rpt confermata pendente dal nodo [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "]: stato " + stato);
						if(!rpt.getStatoRpt().equals(stato)) {
							rptBD.updateStatoRpt(rpt.getId(), stato, rpt.getFaultCode(), rpt.getDescrizioneStato());
						}
					} else {
						log.debug("Rpt non pendente sul nodo [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "]");
						aggiornaRptDaNpD(rpt);
					}
				}
				closeConnection();
			}
		} catch (Exception e) {
			log.error("Fallito aggiornamento pendenti", e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}
}
