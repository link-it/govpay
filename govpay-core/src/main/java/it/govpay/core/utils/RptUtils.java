/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.slf4j.Logger;

import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivocoPersonaG;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersG;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediCopiaRT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediCopiaRTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediStatoRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediStatoRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaCarrelloRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRPT;
import it.gov.digitpa.schemas._2011.ws.paa.TipoElementoListaRPT;
import it.gov.digitpa.schemas._2011.ws.paa.TipoListaRPT;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.pagamento.RptBD;

import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.business.model.Risposta;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.core.utils.thread.InviaRptThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;

import it.govpay.model.Anagrafica;
import it.govpay.model.Evento;
import it.govpay.model.Evento.CategoriaEvento;
import it.govpay.model.Evento.TipoEvento;
import it.govpay.model.Intermediario;
import it.govpay.model.Rpt.StatoRpt;

import it.govpay.servizi.commons.EsitoOperazione;

public class RptUtils {

	private static Logger log = LoggerWrapperFactory.getLogger(RptUtils.class);

	public static String buildUUID35() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static CtEnteBeneficiario buildEnteBeneficiario(Dominio dominio, UnitaOperativa uo, BasicBD bd) throws ServiceException {

		CtEnteBeneficiario enteBeneficiario = new CtEnteBeneficiario();
		CtIdentificativoUnivocoPersonaG idUnivocoBeneficiario = new CtIdentificativoUnivocoPersonaG();
		idUnivocoBeneficiario.setCodiceIdentificativoUnivoco(dominio.getCodDominio());
		idUnivocoBeneficiario.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersG.G);
		enteBeneficiario.setIdentificativoUnivocoBeneficiario(idUnivocoBeneficiario);
		enteBeneficiario.setDenominazioneBeneficiario(dominio.getRagioneSociale());

		Anagrafica anagrafica = dominio.getAnagrafica();
		enteBeneficiario.setCapBeneficiario(getNotEmpty(anagrafica.getCap()));
		enteBeneficiario.setCivicoBeneficiario(getNotEmpty(anagrafica.getCivico()));
		enteBeneficiario.setIndirizzoBeneficiario(getNotEmpty(anagrafica.getIndirizzo()));
		enteBeneficiario.setLocalitaBeneficiario(getNotEmpty(anagrafica.getLocalita()));
		enteBeneficiario.setNazioneBeneficiario(getNotEmpty(anagrafica.getNazione()));
		enteBeneficiario.setProvinciaBeneficiario(getNotEmpty(anagrafica.getProvincia()));

		if(!uo.getCodUo().equals(Dominio.EC) && uo.getAnagrafica() != null) {
			if(uo.getAnagrafica().getCodUnivoco() != null && uo.getAnagrafica().getCodUnivoco().trim().length()>0)
				enteBeneficiario.setCodiceUnitOperBeneficiario(uo.getAnagrafica().getCodUnivoco());
			if(uo.getAnagrafica().getRagioneSociale() != null && uo.getAnagrafica().getRagioneSociale().trim().length()>0)
				enteBeneficiario.setDenomUnitOperBeneficiario(uo.getAnagrafica().getRagioneSociale());
			if(uo.getAnagrafica().getIndirizzo() != null && uo.getAnagrafica().getIndirizzo().trim().length()>0)
				enteBeneficiario.setIndirizzoBeneficiario(uo.getAnagrafica().getIndirizzo());
			if(uo.getAnagrafica().getCivico() != null && uo.getAnagrafica().getCivico().trim().length()>0)
				enteBeneficiario.setCivicoBeneficiario(uo.getAnagrafica().getCivico());
			if(uo.getAnagrafica().getCap() != null && uo.getAnagrafica().getCap().trim().length()>0)
				enteBeneficiario.setCapBeneficiario(uo.getAnagrafica().getCap());
			if(uo.getAnagrafica().getLocalita() != null && uo.getAnagrafica().getLocalita().trim().length()>0)
				enteBeneficiario.setLocalitaBeneficiario(uo.getAnagrafica().getLocalita());
			if(uo.getAnagrafica().getProvincia() != null && uo.getAnagrafica().getProvincia().trim().length()>0)
				enteBeneficiario.setProvinciaBeneficiario(uo.getAnagrafica().getProvincia());
			if(uo.getAnagrafica().getNazione() != null && uo.getAnagrafica().getNazione().trim().length()>0)
				enteBeneficiario.setNazioneBeneficiario(uo.getAnagrafica().getNazione());
		}
		return enteBeneficiario;
	}

	private static String getNotEmpty(String text) {
		if(text == null || text.trim().isEmpty())
			return null;
		else
			return text;
	}

	public static it.govpay.core.business.model.Risposta inviaRPT(Rpt rpt, BasicBD bd) throws GovPayException, ClientException, ServiceException {
		return inviaRPT(rpt, rpt.getIntermediario(bd), rpt.getStazione(bd), bd);
	}

	public static it.govpay.core.business.model.Risposta inviaRPT(Rpt rpt, Intermediario intermediario, Stazione stazione, BasicBD bd) throws GovPayException, ClientException, ServiceException {
		if(bd != null) bd.closeConnection();
		Evento evento = new Evento();
		it.govpay.core.business.model.Risposta risposta = null;
		try {
			NodoClient client = new it.govpay.core.utils.client.NodoClient(intermediario, bd);
			NodoInviaRPT inviaRPT = new NodoInviaRPT();
			inviaRPT.setIdentificativoCanale(rpt.getCodCanale());
			inviaRPT.setIdentificativoIntermediarioPSP(rpt.getCodIntermediarioPsp());
			inviaRPT.setIdentificativoPSP(rpt.getCodPsp());
			inviaRPT.setPassword(rpt.getStazione(bd).getPassword());
			inviaRPT.setRpt(rpt.getXmlRpt());
			
			// FIX Bug Nodo che richiede firma vuota in caso di NESSUNA
			inviaRPT.setTipoFirma("");
			risposta = new it.govpay.core.business.model.Risposta(client.nodoInviaRPT(rpt.getIntermediario(bd), rpt.getStazione(bd), rpt, inviaRPT)); 
			return risposta;
		} finally {
			// Se mi chiama InviaRptThread, BD e' null
			boolean newCon = bd == null;
			if(!newCon) 
				bd.setupConnection(GpThreadLocal.get().getTransactionId());
			else {
				bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			}
			
			try {
				GiornaleEventi giornale = new GiornaleEventi(bd);
				buildEvento(evento, rpt, risposta, TipoEvento.nodoInviaRPT, bd);
				giornale.registraEvento(evento);
			} finally {
				if(newCon) bd.closeConnection();
			}
		}
	}

	public static it.govpay.core.business.model.Risposta inviaCarrelloRPT(Intermediario intermediario, Stazione stazione, List<Rpt> rpts, BasicBD bd) throws GovPayException, ClientException, ServiceException {
		bd.closeConnection();
		Evento evento = new Evento();
		it.govpay.core.business.model.Risposta risposta = null;
		try {
			NodoClient client = new it.govpay.core.utils.client.NodoClient(intermediario, bd);
			NodoInviaCarrelloRPT inviaCarrelloRpt = new NodoInviaCarrelloRPT();
			inviaCarrelloRpt.setIdentificativoCanale(rpts.get(0).getCodCanale());
			inviaCarrelloRpt.setIdentificativoIntermediarioPSP(rpts.get(0).getCodIntermediarioPsp());
			inviaCarrelloRpt.setIdentificativoPSP(rpts.get(0).getCodPsp());
			inviaCarrelloRpt.setPassword(stazione.getPassword());
			TipoListaRPT listaRpt = new TipoListaRPT();
			for(Rpt rpt : rpts) {
				TipoElementoListaRPT elementoListaRpt = new TipoElementoListaRPT();
				elementoListaRpt.setCodiceContestoPagamento(rpt.getCcp());
				elementoListaRpt.setIdentificativoDominio(rpt.getCodDominio());
				elementoListaRpt.setIdentificativoUnivocoVersamento(rpt.getIuv());
				elementoListaRpt.setRpt(rpt.getXmlRpt());
				elementoListaRpt.setTipoFirma("");
				listaRpt.getElementoListaRPT().add(elementoListaRpt);
			}
			inviaCarrelloRpt.setListaRPT(listaRpt);
			risposta = new it.govpay.core.business.model.Risposta(client.nodoInviaCarrelloRPT(intermediario, stazione, inviaCarrelloRpt, rpts.get(0).getCodCarrello())); 
			return risposta;
		} finally {
			bd.setupConnection(GpThreadLocal.get().getTransactionId());

			GiornaleEventi giornale = new GiornaleEventi(bd);
			for(Rpt rpt : rpts) {
				buildEvento(evento, rpt, risposta, TipoEvento.nodoInviaCarrelloRPT, bd);
				giornale.registraEvento(evento);
			}
		}
	}
	
	private static void buildEvento(Evento evento, Rpt rpt, Risposta risposta, TipoEvento tipoEvento, BasicBD bd) throws ServiceException {
		evento.setAltriParametriRichiesta(null);
		evento.setAltriParametriRisposta(null);
		evento.setCategoriaEvento(CategoriaEvento.INTERFACCIA);
		evento.setCcp(rpt.getCcp());
		evento.setCodCanale(rpt.getCodCanale());
		evento.setCodDominio(rpt.getCodDominio());
		evento.setCodPsp(rpt.getCodPsp());
		evento.setCodStazione(rpt.getCodStazione());
		evento.setComponente(Evento.COMPONENTE);
		evento.setDataRisposta(new Date());
		evento.setErogatore(Evento.NDP);
		if(risposta != null)
			evento.setEsito(risposta.getEsito());
		else
			evento.setEsito("Errore di trasmissione al Nodo");
		evento.setFruitore(rpt.getIntermediario(bd).getDenominazione());
		evento.setIuv(rpt.getIuv());
		evento.setSottotipoEvento(null);
		evento.setTipoEvento(tipoEvento);
		evento.setTipoVersamento(rpt.getTipoVersamento());

	}

	public static void inviaRPTAsync(Rpt rpt, BasicBD bd) throws ServiceException {
		InviaRptThread t = new InviaRptThread(rpt, bd);
		ThreadExecutorManager.getClientPoolExecutor().execute(t);
	}

	public static NodoChiediStatoRPTRisposta chiediStatoRPT(Intermediario intermediario, Stazione stazione, Rpt rpt, BasicBD bd) throws GovPayException, ClientException {

		NodoClient client = new it.govpay.core.utils.client.NodoClient(intermediario, bd);

		NodoChiediStatoRPT nodoChiediStatoRPT = new NodoChiediStatoRPT();
		nodoChiediStatoRPT.setCodiceContestoPagamento(rpt.getCcp());
		nodoChiediStatoRPT.setIdentificativoDominio(rpt.getCodDominio());
		nodoChiediStatoRPT.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
		nodoChiediStatoRPT.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
		nodoChiediStatoRPT.setIdentificativoUnivocoVersamento(rpt.getIuv());
		nodoChiediStatoRPT.setPassword(stazione.getPassword());
		return client.nodoChiediStatoRpt(nodoChiediStatoRPT, rpt.getCodDominio());
	}


	public static boolean aggiornaRptDaNpD(NodoClient client, Rpt rpt, BasicBD bd) throws GovPayException, ServiceException, ClientException, NdpException {
		try {
			switch (rpt.getStato()) {
			case RPT_RIFIUTATA_NODO:
			case RPT_RIFIUTATA_PSP:
			case RPT_ERRORE_INVIO_A_PSP:
			case RT_ACCETTATA_PA:
				log.info("Rpt in stato terminale [" + rpt.getStato()+ "]. Aggiornamento non necessario.");
				return false;
				
			case RPT_ATTIVATA:
				log.info("Rpt in stato non terminale [" + rpt.getStato()+ "]. Eseguo una rispedizione della RPT.");
				inviaRPTAsync(rpt, bd);
				return false;
				
			default:
				log.info("Rpt in stato non terminale [" + rpt.getStato()+ "]. Eseguo un aggiornamento dello stato.");
	
				bd.closeConnection();
				String transactionId = null;
				NodoChiediStatoRPTRisposta risposta = null;
				try {
					transactionId = GpThreadLocal.get().openTransaction();
					GpThreadLocal.get().setupNodoClient(rpt.getStazione(bd).getCodStazione(), rpt.getCodDominio(), Azione.nodoChiediStatoRPT);
					NodoChiediStatoRPT richiesta = new NodoChiediStatoRPT();
					richiesta.setIdentificativoDominio(rpt.getCodDominio());
					richiesta.setIdentificativoIntermediarioPA(rpt.getStazione(bd).getIntermediario(bd).getCodIntermediario());
					richiesta.setIdentificativoStazioneIntermediarioPA(rpt.getStazione(bd).getCodStazione());
					richiesta.setPassword(rpt.getStazione(bd).getPassword());
					richiesta.setIdentificativoUnivocoVersamento(rpt.getIuv());
					richiesta.setCodiceContestoPagamento(rpt.getCcp());
	
					risposta = client.nodoChiediStatoRpt(richiesta, rpt.getStazione(bd).getIntermediario(bd).getDenominazione());
				} finally {
					bd.setupConnection(GpThreadLocal.get().getTransactionId());
					GpThreadLocal.get().closeTransaction(transactionId);
				}
				if(risposta.getFault() != null) {
					if(it.govpay.core.exceptions.NdpException.FaultNodo.valueOf(risposta.getFault().getFaultCode()).equals(it.govpay.core.exceptions.NdpException.FaultNodo.PPT_RPT_SCONOSCIUTA)) {
						log.info("RPT inesistente. Aggiorno lo stato in " + StatoRpt.RPT_ERRORE_INVIO_A_NODO + ".");
						rpt.setStato(StatoRpt.RPT_ERRORE_INVIO_A_NODO);
						rpt.setDescrizioneStato("Stato sul nodo: PPT_RPT_SCONOSCIUTA");
	
						RptBD rptBD = new RptBD(bd);
						rptBD.updateRpt(rpt.getId(), StatoRpt.RPT_ERRORE_INVIO_A_NODO, null, null, null);
						return true;
					}
					throw new GovPayException(risposta.getFault());
				} else {
					StatoRpt nuovoStato = Rpt.StatoRpt.toEnum(risposta.getEsito().getStato());
					log.info("Acquisito stato della RPT: " + nuovoStato + ".");
	
					RptBD rptBD = null;
					
					switch (nuovoStato) {
					case RT_ACCETTATA_NODO:
					case RT_ESITO_SCONOSCIUTO_PA:
					case RT_RIFIUTATA_PA:
					case RT_ACCETTATA_PA:
						log.info("Richiesta dell'RT al Nodo dei Pagamenti [CodDominio: " + rpt.getCodDominio() + "][IUV: " + rpt.getIuv() + "][CCP: " + rpt.getCcp() + "].");
	
						bd.closeConnection();
						transactionId = null;
						NodoChiediCopiaRTRisposta nodoChiediCopiaRTRisposta = null;
						try {
							transactionId = GpThreadLocal.get().openTransaction();
							GpThreadLocal.get().setupNodoClient(rpt.getStazione(bd).getCodStazione(), rpt.getCodDominio(), Azione.nodoChiediCopiaRT);
							NodoChiediCopiaRT nodoChiediCopiaRT = new NodoChiediCopiaRT();
							nodoChiediCopiaRT.setIdentificativoDominio(rpt.getCodDominio());
							nodoChiediCopiaRT.setIdentificativoIntermediarioPA(rpt.getIntermediario(bd).getCodIntermediario());
							nodoChiediCopiaRT.setIdentificativoStazioneIntermediarioPA(rpt.getStazione(bd).getCodStazione());
							nodoChiediCopiaRT.setPassword(rpt.getStazione(bd).getPassword());
							nodoChiediCopiaRT.setIdentificativoUnivocoVersamento(rpt.getIuv());
							nodoChiediCopiaRT.setCodiceContestoPagamento(rpt.getCcp());
							nodoChiediCopiaRTRisposta = client.nodoChiediCopiaRT(nodoChiediCopiaRT, rpt.getIntermediario(bd).getDenominazione());
						} finally {
							bd.setupConnection(GpThreadLocal.get().getTransactionId());
							GpThreadLocal.get().closeTransaction(transactionId);
						}
	
						rptBD = new RptBD(bd);
						
						byte[] rtByte = null;
						try {
							ByteArrayOutputStream output = new ByteArrayOutputStream();
							DataHandler dh = nodoChiediCopiaRTRisposta.getRt();
							dh.writeTo(output);
							rtByte = output.toByteArray();
						} catch (IOException e) {
							log.error("Errore durante la lettura dell'RT: " + e);
							rptBD.updateRpt(rpt.getId(), nuovoStato, null, null, null);
							rpt.setStato(nuovoStato);
							rpt.setDescrizioneStato(null);
							throw new GovPayException(EsitoOperazione.INTERNAL, e);
						}
	
						if(nodoChiediCopiaRTRisposta.getFault() != null) {
							log.info("Fault nell'acquisizione dell'RT: [" + nodoChiediCopiaRTRisposta.getFault().getFaultCode() + "] " + nodoChiediCopiaRTRisposta.getFault().getFaultString());
							log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in " + nuovoStato + ".");
							rptBD.updateRpt(rpt.getId(), nuovoStato, null, null, null);
							rpt.setStato(nuovoStato);
							rpt.setDescrizioneStato(null);
							return true;
						}
						
						GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("ccp", rpt.getCcp()));
						GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("codDominio", rpt.getCodDominio()));
						GpThreadLocal.get().getContext().getRequest().addGenericProperty(new Property("iuv", rpt.getIuv()));
						GpThreadLocal.get().log("pagamento.recuperoRt");
						rpt = RtUtils.acquisisciRT(rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp(), nodoChiediCopiaRTRisposta.getTipoFirma(), rtByte, bd);
						GpThreadLocal.get().getContext().getResponse().addGenericProperty(new Property("esitoPagamento", rpt.getEsitoPagamento().toString()));
						GpThreadLocal.get().log("pagamento.acquisizioneRtOk");
						return true;
					default:
						log.info("Aggiorno lo stato della RPT [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "] in " + nuovoStato + ".");
						rptBD = new RptBD(bd);
						rptBD.updateRpt(rpt.getId(), nuovoStato, null, null, null);
						rpt.setStato(nuovoStato);
						rpt.setDescrizioneStato(null);
						return false;
					}
				}
			}
		} catch(NotFoundException e) {
			throw new ServiceException(e);
		}
	}
}
