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


import gov.telematici.pagamenti.ws.ppthead.IntestazioneCarrelloPPT;
import gov.telematici.pagamenti.ws.ppthead.IntestazionePPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
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
import it.gov.digitpa.schemas._2011.ws.psp.PaaAttivaRPT;
import it.govpay.ejb.core.controller.AnagraficaEJB;
import it.govpay.ejb.core.controller.DistintaEJB;
import it.govpay.ejb.core.controller.PendenzaEJB;
import it.govpay.ejb.core.controller.ScadenzarioEJB;
import it.govpay.ejb.core.exception.GovPayEnteException;
import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.exception.GovPayEnteException.EnteFaultCode;
import it.govpay.ejb.core.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.core.model.DistintaModel;
import it.govpay.ejb.core.model.EnteCreditoreModel;
import it.govpay.ejb.core.model.GatewayPagamentoModel;
import it.govpay.ejb.core.model.PendenzaModel;
import it.govpay.ejb.core.model.ScadenzarioModel;
import it.govpay.ejb.core.model.TributoModel;
import it.govpay.ejb.core.model.DistintaModel.EnumStatoDistinta;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumModalitaPagamento;
import it.govpay.ejb.core.model.TributoModel.EnumStatoTributo;
import it.govpay.ejb.core.utils.DataTypeUtils;
import it.govpay.ejb.ndp.ejb.AnagraficaDominioEJB;
import it.govpay.ejb.ndp.ejb.DocumentiEJB;
import it.govpay.ejb.ndp.model.DominioEnteModel;
import it.govpay.ejb.ndp.model.EventiInterfacciaModel;
import it.govpay.ejb.ndp.model.IntermediarioModel;
import it.govpay.ejb.ndp.model.StazioneModel;
import it.govpay.ejb.ndp.model.impl.RPTModel;
import it.govpay.ejb.ndp.pojo.NdpFaultCode;
import it.govpay.ejb.ndp.pojo.StatoRPT;
import it.govpay.ejb.ndp.util.GdeUtils;
import it.govpay.ejb.ndp.util.NdpUtils;
import it.govpay.ejb.ndp.util.GdeUtils.Azione;
import it.govpay.ejb.ndp.util.exception.GovPayNdpException;
import it.govpay.ejb.ndp.util.wsclient.NodoPerPa;
import it.govpay.rs.AutenticazioneSoggetto;
import it.govpay.rs.Pagamento;
import it.govpay.rs.RichiestaPagamento;
import it.govpay.rs.Soggetto;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;


@Stateless
public class RptController {

	@Inject
	GdeController gdeCtrl;
	
	@Inject
	RtController rtCtrl;

	@Inject
	AnagraficaEJB anagraficaEjb;

	@Inject
	DocumentiEJB documentiEjb;

	@Inject
	DistintaEJB distintaEjb;
	
	@Inject
	PendenzaEJB pendenzaEjb;
	
	@Inject
	ScadenzarioEJB scadenzarioEjb;

	@Inject
	AnagraficaDominioEJB anagraficaDominioEjb;

    @Inject  
    private transient Logger log;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public URL inviaRPT(Map<DistintaModel, List<PendenzaModel>> distintePendenzeMap) throws GovPayException {
		List<DatiGestioneRPT> listaDatiGestioneRPT = new ArrayList<DatiGestioneRPT>();
		GatewayPagamentoModel gw = null;

		log.debug("Validazione vincoli Nodo dei Pagamenti");

		for(DistintaModel distinta : distintePendenzeMap.keySet()) {
			DatiGestioneRPT datiGestioneRPT = new DatiGestioneRPT();
			datiGestioneRPT.distinta = distinta;

			// Tutte le distinte devono avere lo stesso psp
			if(gw != null && gw.getIdGateway() != distinta.getIdGatewayPagamento()) {
				log.error("La distinta [" + distinta.getIdDistinta() + "] presenta un PSP [" + gw.getIdGateway() + "] diverso dagli altri [" + distinta.getIdGatewayPagamento() + "]");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, "Il canale Nodo dei Pagamenti non consente Carrello di Pagamenti multi PSP.");
			}

			if(gw == null) {				
				gw = anagraficaEjb.getValidGateway(distinta.getIdGatewayPagamento());
				log.debug("PSP di destinazione " + gw.getBundleKey() + "[Tipologia: " + gw.getStrumentoPagamento() + "][Modalita: " + gw.getModalitaPagamento() + "]");
			}

			// Controllo ci siano al massimo 5 pendenze per distinta 
			// con ciascuna 1 condizione e tutte con lo stesso beneficiario
			List<PendenzaModel> pendenze = distintePendenzeMap.get(distinta);
			if(pendenze.size() > 5) {
				log.error("La distinta [" + distinta.getIdDistinta() + "] presenta piu' di 5 pendenze associate.");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, "Il canale Nodo dei Pagamenti non consente Pagamenti con piu' di cinque Versamenti.");
			}

			String idEnteCreditore = null;
			String codTributo = null;
			
			for(PendenzaModel pendenza : pendenze) {
				if(pendenza.getCondizioniPagamento().size() != 1)  {
					log.error("La distinta [" + distinta.getIdDistinta() + "] presenta una pendenza [" + pendenza.getIdPendenza() + "] con piu di una Condizione.");
					throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, "Il canale Nodo dei Pagamenti non consente Pagamenti multi Condizione.");
				}

				if(idEnteCreditore!=null && !pendenza.getIdEnteCreditore().equals(idEnteCreditore)) {
					log.error("La distinta [" + distinta.getIdDistinta() + "] presenta una pendenza [" + pendenza.getIdPendenza() + "] con beneficiario [" + pendenza.getIdEnteCreditore() + "] diverso dalle altre.");
					throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, "Il canale Nodo dei Pagamenti non consente Pagamenti multi Beneficiario.");
				}
				idEnteCreditore = pendenza.getIdEnteCreditore();
				codTributo = pendenza.getCodiceTributo();
			}
			
			TributoModel tributo = anagraficaEjb.getTributoById(idEnteCreditore, codTributo);
			if(tributo == null) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_CONFIGURAZIONE, "Non e' stata trovata la configurazione di tributo [IdEnteCreditore: " + idEnteCreditore + "][TipoTributo: " + codTributo + "]");
			} else if(EnumStatoTributo.D.equals(tributo.getStato())) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_CONFIGURAZIONE, "Il tributo [IdEnteCreditore: " + idEnteCreditore + "][TipoTributo: " + codTributo + "] e' DISATTIVO");
			}
			
			ScadenzarioModel scadenzario = anagraficaEjb.getScadenzario(tributo);
			if(scadenzario == null) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_CONFIGURAZIONE, "Non e' stata trovata la configurazione dello scadenzario [IdEnteCreditore: " + idEnteCreditore + "][TipoTributo: " + codTributo + "]");
			}
			
			DominioEnteModel dominioEnte = anagraficaDominioEjb.getDominioEnte(idEnteCreditore, scadenzario.getIdStazione());
			if(dominioEnte == null) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_CONFIGURAZIONE, "Non e' stata trovata la configurazione di dominio [IdEnteCreditore: " + idEnteCreditore + "][TipoTributo: " + codTributo + "]");
			}

			log.info("Validazione eseguita con successo. Procedo alla creazione delle RPT.");
			datiGestioneRPT.rpt=NdpUtils.buildRPT(dominioEnte, gw, distinta, pendenze);
			datiGestioneRPT.dominioEnte = dominioEnte;
			listaDatiGestioneRPT.add(datiGestioneRPT);
		}

		if(listaDatiGestioneRPT.size() == 1) {
			DatiGestioneRPT datiGestioneRPT = listaDatiGestioneRPT.get(0);
			return spedizioneRPT(datiGestioneRPT, gw);
		} else {
			return spedizioneCarrelloRPT(listaDatiGestioneRPT, gw);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private URL spedizioneCarrelloRPT(List<DatiGestioneRPT> listaDatiGestioneRPT, GatewayPagamentoModel gw) throws GovPayException {
		EventiInterfacciaModel eventi = new EventiInterfacciaModel();

		TipoListaRPT listaRpt = new TipoListaRPT();
		String idCarrello = null;

		//Preparazione della messaggio NodoInviaCarrelloRPT

		StazioneModel stazione = null;
		IntermediarioModel intermediario = null;

		for(DatiGestioneRPT datiGestioneRPT : listaDatiGestioneRPT) {
			CtRichiestaPagamentoTelematico rpt = datiGestioneRPT.rpt;
			DistintaModel distinta = datiGestioneRPT.distinta;
			idCarrello = distinta.getIdGruppo();

			if(stazione == null) {
				stazione = datiGestioneRPT.dominioEnte.getStazione();
				intermediario  = datiGestioneRPT.dominioEnte.getIntermediario();
			} else if(!intermediario.equals(datiGestioneRPT.dominioEnte.getIntermediario())) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP, "Non e' possibile inviare pagamenti per creditori con Intermediari diversi.");
			}

			byte[] rptByte;

			try {
				rptByte = NdpUtils.toByte(rpt);
				log.debug("Contenuto della RPT: " + new String(rptByte));
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Impossibile serializzare la richiesta di pagamento.", e);
			}

			TipoElementoListaRPT elementoListaRPT = new TipoElementoListaRPT();
			elementoListaRPT.setCodiceContestoPagamento(distinta.getCodTransazionePsp());
			elementoListaRPT.setIdentificativoDominio(datiGestioneRPT.dominioEnte.getIdDominio());
			elementoListaRPT.setIdentificativoUnivocoVersamento(rpt.getDatiVersamento().getIdentificativoUnivocoVersamento());
			
			switch (distinta.getFirma()) {
			case CA_DES:
				elementoListaRPT.setTipoFirma("1");
			case XA_DES:
				elementoListaRPT.setTipoFirma("3");
			case AVANZATA:
				elementoListaRPT.setTipoFirma("4");
			default :
				elementoListaRPT.setTipoFirma("0");
				break;
			}
			
			elementoListaRPT.setRpt(rptByte);
			
			listaRpt.getElementoListaRPT().add(elementoListaRPT);
			eventi.getEventi().add(GdeUtils.creaEventoRichiesta(datiGestioneRPT.dominioEnte, 
					rpt.getDatiVersamento().getIdentificativoUnivocoVersamento(), 
					distinta.getCodTransazionePsp(), 
					rpt.getDatiVersamento().getTipoVersamento().name(), 
					gw.getSystemId(),
					gw.getApplicationId().substring(0, gw.getApplicationId().lastIndexOf("-")), 
					Azione.nodoInviaCarrelloRPT));
		}

		IntestazioneCarrelloPPT intestazione = new IntestazioneCarrelloPPT();
		intestazione.setIdentificativoIntermediarioPA(stazione.getIdIntermediarioPA());
		intestazione.setIdentificativoStazioneIntermediarioPA(stazione.getIdStazioneIntermediarioPA());
		intestazione.setIdentificativoCarrello(idCarrello);

		NodoInviaCarrelloRPT inviaCarrelloRPT = new NodoInviaCarrelloRPT();

		// TODO L'application Id e' una bundle. Finche' non viene rivista, devo estrarre il dato.
		inviaCarrelloRPT.setIdentificativoCanale(gw.getApplicationId().substring(0, gw.getApplicationId().lastIndexOf("-")));
		inviaCarrelloRPT.setIdentificativoIntermediarioPSP(gw.getSubSystemId());
		inviaCarrelloRPT.setIdentificativoPSP(gw.getSystemId());
		inviaCarrelloRPT.setPassword(stazione.getPassword());
		inviaCarrelloRPT.setListaRPT(listaRpt);

		// Registrazione delle RTP

		for(TipoElementoListaRPT elemento : listaRpt.getElementoListaRPT()) {
			log.debug("Archiviazione della RPT [" + elemento.getIdentificativoDominio() + "][" + elemento.getIdentificativoUnivocoVersamento() + "][" + elemento.getCodiceContestoPagamento() + "]");
			try {
				RPTModel documento = new RPTModel(elemento.getIdentificativoDominio(), elemento.getIdentificativoUnivocoVersamento(), elemento.getCodiceContestoPagamento(), elemento.getRpt());
				documentiEjb.archiviaDocumento(documento);
			} catch (Exception e) {
				log.error("Errore durante l'archiviazione della RPT [" + elemento.getIdentificativoDominio() + "][" + elemento.getIdentificativoUnivocoVersamento() + "][" + elemento.getCodiceContestoPagamento() + "]", e);
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore durante l'archiviazione della Richiesta Pagamento Telematico", e);
			}
		} 

		log.debug("Spedizione del Carrello RPT al Nodo dei Pagamenti in corso [IdGruppo: " + idCarrello +"].");
		
		NodoPerPa client = new NodoPerPa(intermediario, log);
		Holder<Map<String,List<String>>> responseHeaders = new Holder<Map<String,List<String>>>();
		
		NodoInviaCarrelloRPTRisposta risposta;
		try {
			risposta = client.nodoInviaCarrelloRPT(responseHeaders, inviaCarrelloRPT, intestazione);
		} catch (Exception e) {
			log.error("Spedizione fallita: " + e.getMessage(), e);
			log.debug("Registrazione degli eventi relativi alla spedizione.");
			eventi.setEsito("Spedizione fallita");
			gdeCtrl.registraEventi(eventi);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, e);
		}

		if(eventi.getInfospcoop() != null) {
			log.debug("Spedizione della RPT al Nodo dei Pagamenti completata con successo [IdEgov: " + eventi.getInfospcoop().getIdEgov() + "].");
		} else {
			log.debug("Spedizione della RPT al Nodo dei Pagamenti completata con successo.");
		}

		log.debug("Registrazione degli eventi relativi alla spedizione.");
		try {
			eventi.setInfospcoop(GdeUtils.creaInfoSPCoop(responseHeaders.value));
			eventi.buildResponses();
			eventi.setEsito(risposta.getEsitoComplessivoOperazione());
			gdeCtrl.registraEventi(eventi);
		} catch (Exception e) {
			log.error("Errore durante la registrazione degli eventi", e);
		}

		if(risposta.getFault() != null) {
			log.error("Ricevuto esito di errore: " + risposta.getFault().getFaultCode() + " - " + risposta.getFault().getFaultString() + ". " + risposta.getFault().getDescription());
			NdpFaultCode faultCode = NdpFaultCode.valueOf(risposta.getFault().getFaultCode());
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, faultCode, risposta.getFault().getFaultString() + ". " + risposta.getFault().getDescription());
		} else {
			log.info("Ricevuto esito ok con URL di redirect: " + risposta.getUrl());
			try {
				return new URL(risposta.getUrl());
			} catch (MalformedURLException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "La Url del PSP non e' valida.", e);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private URL spedizioneRPT(DatiGestioneRPT datiGestioneRPT, GatewayPagamentoModel gw) throws GovPayException {
		EventiInterfacciaModel eventi = new EventiInterfacciaModel();
		CtRichiestaPagamentoTelematico rpt = datiGestioneRPT.rpt;
		DistintaModel distinta = datiGestioneRPT.distinta;
		DominioEnteModel dominioEnte = datiGestioneRPT.dominioEnte;

		IntestazionePPT intestazione = new IntestazionePPT();
		intestazione.setCodiceContestoPagamento(rpt.getDatiVersamento().getCodiceContestoPagamento());
		intestazione.setIdentificativoDominio(dominioEnte.getIdDominio());
		intestazione.setIdentificativoIntermediarioPA(dominioEnte.getIntermediario().getIdIntermediarioPA());
		intestazione.setIdentificativoStazioneIntermediarioPA(dominioEnte.getStazione().getIdStazioneIntermediarioPA());
		intestazione.setIdentificativoUnivocoVersamento(rpt.getDatiVersamento().getIdentificativoUnivocoVersamento());

		byte[] rptByte;

		try {
			rptByte = NdpUtils.toByte(rpt);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Impossibile serializzare la richiesta di pagamento.", e);
		}

		NodoInviaRPT inviaRPT = new NodoInviaRPT();
		// TODO L'application Id e' una bundle. Finche' non viene rivista, devo estrarre il dato.
		inviaRPT.setIdentificativoCanale(gw.getApplicationId().substring(0, gw.getApplicationId().lastIndexOf("-")));
		inviaRPT.setIdentificativoIntermediarioPSP(gw.getSubSystemId());
		inviaRPT.setIdentificativoPSP(gw.getSystemId());
		inviaRPT.setPassword(dominioEnte.getStazione().getPassword());
		inviaRPT.setRpt(rptByte);
		switch (distinta.getFirma()) {
		case CA_DES:
			inviaRPT.setTipoFirma("1");
			break;
		case XA_DES:
			inviaRPT.setTipoFirma("3");
			break;
		case AVANZATA:
			inviaRPT.setTipoFirma("4");
			break;
		default :
			inviaRPT.setTipoFirma("");
			break;
		}

		eventi.getEventi().add(GdeUtils.creaEventoRichiesta(datiGestioneRPT.dominioEnte, 
				rpt.getDatiVersamento().getIdentificativoUnivocoVersamento(), 
				distinta.getCodTransazionePsp(), 
				rpt.getDatiVersamento().getTipoVersamento().name(), 
				gw.getSystemId(),
				gw.getApplicationId().substring(0, gw.getApplicationId().lastIndexOf("-")), 
				Azione.nodoInviaRPT));

		// Registrazione delle RTP

		log.debug("Archiviazione della RPT [" + intestazione.getIdentificativoDominio() + "][" + intestazione.getIdentificativoUnivocoVersamento() + "][" + intestazione.getCodiceContestoPagamento() + "]");
		try {
			RPTModel documento = new RPTModel(intestazione.getIdentificativoDominio(), intestazione.getIdentificativoUnivocoVersamento(), intestazione.getCodiceContestoPagamento(), rptByte);
			documentiEjb.archiviaDocumento(documento);
		} catch (Exception e) {
			log.error("Errore durante l'archiviazione della RPT [" + intestazione.getIdentificativoDominio() + "][" + intestazione.getIdentificativoUnivocoVersamento() + "][" + intestazione.getCodiceContestoPagamento() + "]", e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore durante l'archiviazione della Richiesta Pagamento Telematico", e);
		}

		log.debug("Spedizione dell'RPT al Nodo dei Pagamenti in corso.");
		NodoPerPa client = new NodoPerPa(dominioEnte.getIntermediario(), log);
		Holder<Map<String,List<String>>> responseHeaders = new Holder<Map<String,List<String>>>();
		NodoInviaRPTRisposta risposta;
		try {
			risposta = client.nodoInviaRPT(responseHeaders, inviaRPT, intestazione);
		} catch (GovPayException e) {
			log.error("Spedizione fallita: " + e.getMessage());
			log.debug("Registrazione degli eventi relativi alla spedizione.");
			eventi.setEsito("Spedizione fallita");
			try {
				gdeCtrl.registraEventi(eventi);
			} catch (GovPayException ev) {
				log.error("Errore durante la registrazione degli eventi nel giornale.", e);
			}
			throw e;
		}

		if(eventi.getInfospcoop() != null) {
			log.debug("Spedizione della RPT al Nodo dei Pagamenti completata con successo [IdEgov: " + eventi.getInfospcoop().getIdEgov() + "].");
		} else {
			log.debug("Spedizione della RPT al Nodo dei Pagamenti completata con successo.");
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

		String redirectUrlString = null;
		
		if(risposta.getFault() != null) {
			NdpFaultCode faultCode = NdpFaultCode.valueOf(risposta.getFault().getFaultCode());
			
			// Distinguo il caso in cui il Nodo mi risponde che la RPT e' gia' stata consegnata
			if(faultCode.equals(NdpFaultCode.PPT_RPT_DUPLICATA)) {
				log.warn("Ricevuto esito erorre: " + NdpFaultCode.PPT_RPT_DUPLICATA);
				//cerco di recuperare la URL di pagamento
				try {
					DatiStatoRPT datiStato = chiediStatoRPT(dominioEnte.getEnteCreditore().getIdEnteCreditore(), dominioEnte.getStazione().getScadenzario(dominioEnte.getEnteCreditore().getIdEnteCreditore()), intestazione.getIdentificativoUnivocoVersamento(), intestazione.getCodiceContestoPagamento());
					redirectUrlString = datiStato.paymentUrl != null ? datiStato.paymentUrl : null;
				} catch (Exception e) {
					// Non sono riuscito a recuperare la redirect URL.... 
					redirectUrlString = null;
				}
			} else {
				log.error("Ricevuto esito di errore: " + risposta.getFault().getFaultCode() + " - " + risposta.getFault().getFaultString() + ". " + risposta.getFault().getDescription());
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, faultCode, risposta.getFault().getFaultString() + ". " + risposta.getFault().getDescription());
			}
		} else {
			if(risposta.getRedirect() == 1) {
				log.info("Ricevuto esito ok con URL di redirect: " + risposta.getUrl());
				redirectUrlString = risposta.getUrl();
			} else {
				log.info("Ricevuto esito ok senza URL di redirect");
			}
		}
		
		if(redirectUrlString != null) {
			URL redirect;
			try {
				redirect = new URL(redirectUrlString);
			} catch (MalformedURLException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP, "La URL per il FESP del PSP non e' valida: " + e);
			}
			return redirect;
		} else {
			return null;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DatiStatoRPT chiediStatoRPT(String idEnteCreditore, ScadenzarioModel scadenzario, String iuv, String ccp) throws GovPayException {
		DominioEnteModel dominioEnte = anagraficaDominioEjb.getDominioEnte(idEnteCreditore, scadenzario.getIdStazione());
		DatiStatoRPT datiStatoRPT = new DatiStatoRPT();
		NodoPerPa client = new NodoPerPa(dominioEnte.getIntermediario(), log);
		NodoChiediStatoRPT richiesta = new NodoChiediStatoRPT();
		richiesta.setIdentificativoDominio(dominioEnte.getIdDominio());
		richiesta.setIdentificativoIntermediarioPA(dominioEnte.getIntermediario().getIdIntermediarioPA());
		richiesta.setIdentificativoStazioneIntermediarioPA(dominioEnte.getStazione().getIdStazioneIntermediarioPA());
		richiesta.setPassword(dominioEnte.getStazione().getPassword());
		richiesta.setIdentificativoUnivocoVersamento(iuv);
		richiesta.setCodiceContestoPagamento(ccp);
		NodoChiediStatoRPTRisposta risposta = client.nodoChiediStatoRpt(richiesta);
		if(risposta.getFault() != null) {
			if(StatoRPT.PPT_RPT_SCONOSCIUTA.name().equals(risposta.getFault().getFaultCode())) {
				datiStatoRPT.statoRPT = StatoRPT.PPT_RPT_SCONOSCIUTA;
			} else {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP, risposta.getFault().getFaultCode() + ": " + risposta.getFault().getFaultString());
			}
		} else {
			datiStatoRPT.statoRPT = StatoRPT.valueOf(risposta.getEsito().getStato());
			datiStatoRPT.paymentUrl = risposta.getEsito().getUrl();
		}
		return datiStatoRPT;
	}
	
	public class DatiStatoRPT {
		public StatoRPT statoRPT;
		public String paymentUrl;
	}
	
	public class DatiGestioneRPT {
		public CtRichiestaPagamentoTelematico rpt;
		public DistintaModel distinta;
		public DominioEnteModel dominioEnte;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void verificaPendenti(String idEnteCreditore, ScadenzarioModel scadenzario) throws GovPayException {
		DominioEnteModel dominioEnte = anagraficaDominioEjb.getDominioEnte(idEnteCreditore, scadenzario.getIdStazione());
		
		// Costruisco una mappa di tutti i pagamenti pendenti sul nodo
		// La chiave di lettura e' iuv@ccp
		Map<String, StatoRPT> statiRptPendenti = new HashMap<String, StatoRPT>();
		NodoPerPa client = new NodoPerPa(dominioEnte.getIntermediario(), log);
		
		// Le pendenze per specifica durano 60 giorni.
		// Richiedo la lista degli ultimi 60 gg con range settimanale, poi tutto l'antecedente ai 60 gg per sicurezza.
		XMLGregorianCalendar data = DataTypeUtils.getXMLGregorianCalendarAdesso();
		int step = 60;
		for(int i = 0; i<60; ) {
			NodoChiediListaPendentiRPT richiesta = new NodoChiediListaPendentiRPT();
			richiesta.setIdentificativoDominio(dominioEnte.getIdDominio());
			richiesta.setIdentificativoIntermediarioPA(dominioEnte.getIntermediario().getIdIntermediarioPA());
			richiesta.setIdentificativoStazioneIntermediarioPA(dominioEnte.getStazione().getIdStazioneIntermediarioPA());
			richiesta.setPassword(dominioEnte.getStazione().getPassword());
			richiesta.setDimensioneLista(BigInteger.valueOf(100));
			if(i != 0)
				richiesta.setRangeA(DataTypeUtils.getGiorniPrima(data, i));
			if(i+step < 60) {
				richiesta.setRangeDa(DataTypeUtils.getGiorniPrima(data, i+step));
				log.debug("Richiedo la lista delle RPT pendenti (da " + i + " a " + (i+step) + " giorni fa)");
			} else {
				log.debug("Richiedo la lista delle RPT pendenti (oltre 60 giorni fa)");
			}
			NodoChiediListaPendentiRPTRisposta risposta = null;
			try {
				risposta = client.nodoChiediListaPendentiRPT(richiesta);
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
			
			if(risposta.getListaRPTPendenti().getRptPendente().size() == 100){
				log.info("Troppe richieste prendenti. Riduco la ricerca.");
				step = step/2;
				continue;
			}
			
			log.info("Individuate " + risposta.getListaRPTPendenti().getRptPendente().size() + " RPT pendenti (da " + i + " a " + (i+step) + " giorni fa)");
			
			for(TipoRPTPendente rptPendente : risposta.getListaRPTPendenti().getRptPendente()) {
				String rptKey = rptPendente.getIdentificativoUnivocoVersamento() + "@" + rptPendente.getCodiceContestoPagamento();
				StatoRPT stato = StatoRPT.valueOf(rptPendente.getStato());
				statiRptPendenti.put(rptKey, stato);
			}
			i = i+step;
		}
		
		// Ho acquisito tutti gli stati pendenti. 
		// Tutte quelle in stato terminale, 
		
		List<DistintaModel> distintePendenti = distintaEjb.getDistintePendenti(dominioEnte.getEnteCreditore().getIdFiscale());
		
		// Scorro le distinte. Se lo stato non c'e' (quindi non e' pendente) la mando in aggiornamento.
		
		for(DistintaModel distinta : distintePendenti) {
			StatoRPT stato = statiRptPendenti.get(distinta.getIuv() + "@" + distinta.getCodTransazionePsp());
			if(stato != null) {
				log.debug("Pagamento " + distinta.getIuv() + "@" + distinta.getCodTransazionePsp() + " pendente in stato " + stato);
			} else {
				log.info("Pagamento " + distinta.getIuv() + "@" + distinta.getCodTransazionePsp() + " non risulta pendente. Richiedo aggiornamento.");
				
				DatiStatoRPT datiStatoRPT = chiediStatoRPT(idEnteCreditore, scadenzario, distinta.getIuv(), distinta.getCodTransazionePsp());
				log.info("Stato presso il Nodo di Pagamento: " + datiStatoRPT.statoRPT);
				switch (datiStatoRPT.statoRPT) {
				case RPT_RIFIUTATA_NODO:
				case RPT_ERRORE_INVIO_A_PSP:
				case RPT_RIFIUTATA_PSP:
				case PPT_RPT_SCONOSCIUTA:
					log.info("Aggiornamento dello stato della distinta " + EnumStatoDistinta.IN_ERRORE);
					distintaEjb.updateStatoDistinta(distinta.getIdDistinta(), EnumStatoDistinta.IN_ERRORE, "Aggiornamento a fronte dello stato presso il NdP " + datiStatoRPT.statoRPT);
					break;
				case RT_ACCETTATA_PA:
					// Non sarei dovuto arrivarci. Mi sono perso l'RT?
					// Chiedo una copia, aggiorno lo stato della distinta e ritorno l'esito aggiornato.
					log.info("Procedo al recupero della RT e aggiornamento della distinta");
					rtCtrl.chiediCopiaRT(idEnteCreditore, scadenzario, distinta.getIuv(), distinta.getCodTransazionePsp());
				case RPT_ACCETTATA_PSP:
				case RPT_DECORSI_TERMINI:
				case RT_ESITO_SCONOSCIUTO_PA:
				case RPT_RICEVUTA_NODO:
				case RPT_ACCETTATA_NODO:
				case RPT_INVIATA_A_PSP:
				case RT_ACCETTATA_NODO:
				case RT_RICEVUTA_NODO:
				case RT_RIFIUTATA_NODO:
				case RT_RIFIUTATA_PA:
					// Stato non terminale. 
					// Lascio lo stato IN_CORSO o ESEGUITO_SBF. Arrivera' la RT.
					log.info("Stato del pagamento non terminale. Niente da aggiornare.");
					break;
				}
			}
		}
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pagamento verificaRPT(DominioEnteModel ente, String iuv, String ccp) throws GovPayException {
		DistintaModel distinta = null;
		try {
			distinta = distintaEjb.getDistinta(ente.getEnteCreditore().getIdFiscale(), iuv);
		} catch(Exception e) {
			log.error("Impossibile recuperare la distinta", e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}

		if(distinta != null) {
			log.info("Pagamento presente sul sistema in stato " + distinta.getStato());
			switch (distinta.getStato()) {
			case ANNULLATO:
				throw new GovPayEnteException(EnteFaultCode.PAGAMENTO_ANNULLATO, "Pagamento annullato");
			case ANNULLATO_OPE:
				throw new GovPayEnteException(EnteFaultCode.PAGAMENTO_ANNULLATO, "Pagamento annullato da operatore");
			case ESEGUITO:
			case PARZIALMENTE_ESEGUITO:
			case STORNATO:
				throw new GovPayEnteException(EnteFaultCode.PAGAMENTO_DUPLICATO, "Pagamento gia' effettuato con esito "+ distinta.getStato());
			case ESEGUITO_SBF:
			case IN_CORSO:
				throw new GovPayEnteException(EnteFaultCode.PAGAMENTO_IN_CORSO, "Pagamento in corso");
			case NON_ESEGUITO:
			case IN_ERRORE:
				// Tutto ok
				log.info("Pagamento replicabile. Ricerco nello Scadenzario");
			}
		} else {
			log.info("Pagamento non ancora gestito. Ricerco nello Scadenzario " + ente.getStazione().getScadenzario(ente.getEnteCreditore().getIdEnteCreditore()).getIdSystem());
			
		}
		
		// Ricerco nello Scadenzario dell'ente.
		return scadenzarioEjb.recuperaPagamento(ente.getEnteCreditore(), ente.getStazione().getScadenzario(ente.getEnteCreditore().getIdEnteCreditore()), iuv);
	}
	
	
	public Pagamento attivaRPT(DominioEnteModel dominioEnte, String identificativoUnivocoVersamento, String codiceContestoPagamento, PaaAttivaRPT datiAttivazione) throws GovPayException {
		// Cerco il PSP richiedente
		List<GatewayPagamentoModel> gws = anagraficaEjb.getListaGatewayPagamento(EnumModalitaPagamento.ATTIVATO_PRESSO_PSP);
		Long idGateway = null;
		for(GatewayPagamentoModel gw : gws) {
			if(gw.getSystemId().equals(datiAttivazione.getIdentificativoPSP())) {
				log.info("Individuato Gateway richiedente [id: " + gw.getIdGateway() + "]: " + gw.getSystemId());
				idGateway = gw.getIdGateway();
				break;
			}
		}
		if(idGateway == null) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, NdpFaultCode.PAA_ERRORE_INTERNO, "Psp " + datiAttivazione.getIdentificativoPSP() + " non censito sul sistema per pagamenti attivati presso PSP");
		}
		
		log.info("Recupero e verifico i dati del pagamento.");
		// Cerco il pagamento richiesto
		Pagamento p = verificaRPT(dominioEnte, identificativoUnivocoVersamento, codiceContestoPagamento);
		// Verifico i dati 
		if(datiAttivazione.getDatiPagamentoPSP().getImportoSingoloVersamento().compareTo(p.getDatiVersamento().getImportoTotaleDaVersare()) != 0) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, NdpFaultCode.PAA_SEMANTICA, "L'importo della richiesta di attivazione non corrisponde con l'importo dovuto.");
		}
		if(datiAttivazione.getDatiPagamentoPSP().getSoggettoPagatore() != null) {
			//Se mi e' stato mandato, controllo l'identificativo del pagatore
			if(!datiAttivazione.getDatiPagamentoPSP().getSoggettoPagatore().getIdentificativoUnivocoPagatore().getCodiceIdentificativoUnivoco().equalsIgnoreCase(p.getSoggettoPagatore().getIdentificativo())) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, NdpFaultCode.PAA_SEMANTICA, "Il soggetto pagatore non corrisponde con quello indicato.");
			}
		}
		// Cerco l'indirizzo email
		String email = null;
		if(datiAttivazione.getDatiPagamentoPSP().getSoggettoPagatore() != null &&  datiAttivazione.getDatiPagamentoPSP().getSoggettoPagatore().getEMailPagatore() != null) {
			email = datiAttivazione.getDatiPagamentoPSP().getSoggettoPagatore().getEMailPagatore();
		}
		if(p.getSoggettoPagatore() != null && p.getSoggettoPagatore().getEmail() != null) {
			email = p.getSoggettoPagatore().getEmail();
		}
		if(datiAttivazione.getDatiPagamentoPSP().getSoggettoVersante() != null && datiAttivazione.getDatiPagamentoPSP().getSoggettoVersante().getEMailVersante() != null) {
			email = datiAttivazione.getDatiPagamentoPSP().getSoggettoVersante().getEMailVersante();
		}
		
		// Costruisco la richiesta
		RichiestaPagamento richiesta = new RichiestaPagamento();
		richiesta.setAutenticazioneSoggetto(AutenticazioneSoggetto.N_A);
		richiesta.setEmail(email);
		richiesta.setIdentificativoPsp(idGateway);
		richiesta.setRedirectUrl(null);
		CtSoggettoVersante versante = datiAttivazione.getDatiPagamentoPSP().getSoggettoVersante();
		if(versante != null) {
			Soggetto s = new Soggetto();
			s.setAnagrafica(versante.getAnagraficaVersante());
			s.setCap(versante.getCapVersante());
			s.setCivico(versante.getCivicoVersante());
			s.setEmail(versante.getEMailVersante());
			s.setIdentificativo(versante.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco());
			s.setIndirizzo(versante.getIndirizzoVersante());
			s.setLocalita(versante.getLocalitaVersante());
			s.setNazione(versante.getNazioneVersante());
			s.setProvincia(versante.getProvinciaVersante());
			richiesta.setSoggettoVersante(s);
		}
		if(datiAttivazione.getDatiPagamentoPSP().getIbanAddebito() != null) {
			p.getDatiVersamento().setIbanAddebito(datiAttivazione.getDatiPagamentoPSP().getIbanAddebito());
		}
		richiesta.getPagamentis().add(p);
		distintaEjb.creaDistinta(richiesta, codiceContestoPagamento);
		return p;
	}

	public void spedizioneAttivati(EnteCreditoreModel enteCreditore) throws GovPayException {
		spedizioneAttivati(enteCreditore, null);
	}
	
	public void spedizioneAttivati(EnteCreditoreModel enteCreditore, String iuv) throws GovPayException {
		List<DistintaModel> distinte = distintaEjb.getDistinteDaSpedire(enteCreditore.getIdFiscale());
		
		for(DistintaModel distinta : distinte) {
			if(iuv != null && !distinta.getIuv().equals(iuv)) continue;
			
			ScadenzarioModel scadenzario = anagraficaEjb.getScadenzario(distinta);
			DominioEnteModel dominioEnte = anagraficaDominioEjb.getDominioEnte(enteCreditore.getIdEnteCreditore(), scadenzario.getIdStazione());
		
			ThreadContext.put("dom", dominioEnte.getIdDominio());
			ThreadContext.put("iuv", distinta.getIuv());
			ThreadContext.put("ccp", distinta.getCodTransazionePsp());
			try { 
				log.info("Spedizione del pagamento attivato.");
				
				GatewayPagamentoModel gw = anagraficaEjb.getValidGateway(distinta.getIdGatewayPagamento());
				
				DatiGestioneRPT datiGestioneRPT = new DatiGestioneRPT();
				datiGestioneRPT.distinta = distinta;
				datiGestioneRPT.dominioEnte = dominioEnte;
				List<PendenzaModel> pendenze = pendenzaEjb.getPendenze(distinta.getIdDistinta());
				datiGestioneRPT.rpt = NdpUtils.buildRPT(dominioEnte, gw, distinta, pendenze);
				spedizioneRPT(datiGestioneRPT, gw);
				distintaEjb.updateStatoDistinta(distinta.getIdDistinta(), EnumStatoDistinta.ESEGUITO_SBF, "Spedizione della RPT al Nodo dei Pagamenti");
			} catch (GovPayException e) {
				log.error("Errore durante la spedizione del pagamento attivato: " + e.getTipoException().name() + " " + e.getDescrizione());
			} catch (Exception e) {
				log.error("Errore durante la spedizione del pagamento attivato", e);
			}
		}
	}
}
