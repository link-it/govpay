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
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import gov.telematici.pagamenti.ws.rpt.NodoChiediCopiaRT;
import gov.telematici.pagamenti.ws.rpt.NodoChiediCopiaRTRisposta;
import gov.telematici.pagamenti.ws.rpt.NodoChiediStatoRPT;
import gov.telematici.pagamenti.ws.rpt.NodoChiediStatoRPTRisposta;
import gov.telematici.pagamenti.ws.rpt.NodoInviaCarrelloRPT;
import gov.telematici.pagamenti.ws.rpt.TipoElementoListaRPT;
import gov.telematici.pagamenti.ws.rpt.TipoListaRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivocoPersonaG;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersG;
import it.govpay.bd.BasicBD;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.eventi.DatiPagoPA;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.core.utils.thread.InviaRptThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Anagrafica;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Intermediario;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.StatoRpt;

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

		if(!uo.getCodUo().equals(it.govpay.model.Dominio.EC) && uo.getAnagrafica() != null) {
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

	public static it.govpay.core.business.model.Risposta inviaCarrelloRPT(NodoClient client, Intermediario intermediario, Stazione stazione, List<Rpt> rpts, String operationId, BasicBD bd) throws GovPayException, ClientException, ServiceException, UtilsException {
		it.govpay.core.business.model.Risposta risposta = null;
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
			listaRpt.getElementoListaRPT().add(elementoListaRpt);
		}
		inviaCarrelloRpt.setListaRPT(listaRpt);
		risposta = new it.govpay.core.business.model.Risposta(client.nodoInviaCarrelloRPT(intermediario, stazione, inviaCarrelloRpt, rpts.get(0).getCodCarrello())); 
		return risposta;
	}

	public static void popolaEventoCooperazione(NodoClient client, Rpt rpt, Intermediario intermediario, Stazione stazione) throws ServiceException {
		DatiPagoPA datiPagoPA = new DatiPagoPA();
		datiPagoPA.setCodCanale(rpt.getCodCanale());
		datiPagoPA.setCodPsp(rpt.getCodPsp());
		datiPagoPA.setCodStazione(stazione.getCodStazione());
		datiPagoPA.setCodIntermediario(intermediario.getCodIntermediario());
		datiPagoPA.setErogatore(Evento.NDP);
		datiPagoPA.setFruitore(intermediario.getCodIntermediario());
		datiPagoPA.setTipoVersamento(rpt.getTipoVersamento());
		datiPagoPA.setModelloPagamento(rpt.getModelloPagamento());
		datiPagoPA.setCodIntermediarioPsp(rpt.getCodIntermediarioPsp());
		datiPagoPA.setCodDominio(rpt.getCodDominio());
		client.getEventoCtx().setDatiPagoPA(datiPagoPA);
	}

	public static void inviaRPTAsync(Rpt rpt, BasicBD bd, IContext ctx) throws ServiceException {
		InviaRptThread t = new InviaRptThread(rpt, bd, ctx);
		ThreadExecutorManager.getClientPoolExecutorRPT().execute(t);
	}

	public static NodoChiediStatoRPTRisposta chiediStatoRPT(NodoClient client, Intermediario intermediario, Stazione stazione, Rpt rpt, String operationId, BasicBD bd) throws GovPayException, ClientException, UtilsException, ServiceException {

		NodoChiediStatoRPT nodoChiediStatoRPT = new NodoChiediStatoRPT();
		nodoChiediStatoRPT.setCodiceContestoPagamento(rpt.getCcp());
		nodoChiediStatoRPT.setIdentificativoDominio(rpt.getCodDominio());
		nodoChiediStatoRPT.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
		nodoChiediStatoRPT.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
		nodoChiediStatoRPT.setIdentificativoUnivocoVersamento(rpt.getIuv());
		nodoChiediStatoRPT.setPassword(stazione.getPassword());
		return client.nodoChiediStatoRpt(nodoChiediStatoRPT, rpt.getCodDominio());
	}


	public static boolean aggiornaRptDaNpD(Intermediario intermediario, Rpt rpt, BasicBD bd) throws GovPayException, ServiceException, ClientException, NdpException, UtilsException {
		try {
			it.govpay.core.business.Notifica notificaBD = null;
			boolean insertNotificaOk = false;
			String msg = ".";
			StatoRpt stato_originale = rpt.getStato();
			IContext ctx = ContextThreadLocal.get();
			GpContext appContext = (GpContext) ctx.getApplicationContext();
			Giornale giornale = new it.govpay.core.business.Configurazione(bd).getConfigurazione().getGiornale();
			switch (stato_originale) {
			case RPT_RIFIUTATA_NODO:
			case RPT_RIFIUTATA_PSP:
			case RPT_ERRORE_INVIO_A_PSP:
				// inserisco una notifica di fallimento
				Notifica notifica = new Notifica(rpt, TipoNotifica.FALLIMENTO, bd);
				notificaBD = new it.govpay.core.business.Notifica(bd);
				insertNotificaOk = notificaBD.inserisciNotifica(notifica);
				msg = insertNotificaOk ? ", Schedulazione notifica di Fallimento del tentativo." : ".";
				log.info("Rpt [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] in stato terminale [" + rpt.getStato()+ "]. Aggiornamento non necessario"+msg);
				return false;
			case RT_ACCETTATA_PA:
				log.info("Rpt [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] in stato terminale [" + rpt.getStato()+ "]. Aggiornamento non necessario.");
				return false;

			case RPT_ATTIVATA:
				// Se modello 3, rispedisco

				if(rpt.getModelloPagamento().equals(ModelloPagamento.ATTIVATO_PRESSO_PSP)) {
					log.info("Rpt [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] iniziativa PSP in stato [" + rpt.getStato()+ "]. Eseguo una rispedizione della RPT.");
					inviaRPTAsync(rpt, bd,ctx);
					return false;
				} else {
					// Se modello 1, spedizione fallita
					StatoRpt nuovoStato = StatoRpt.RPT_ERRORE_INVIO_A_NODO;
					log.info("Rpt [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] iniziativa Ente in stato[" + rpt.getStato()+ "]. Aggiorno in [" + nuovoStato + "].");

					bd.enableSelectForUpdate();
					RptBD rptBD = new RptBD(bd);
					Rpt rpt_attuale = rptBD.getRpt(rpt.getId());
					if(!stato_originale.equals(rpt_attuale.getStato())) {
						// Lo stato e' cambiato. Rinuncio all'aggiornamento
						log.info("Lo stato della RPT [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] risulta cambiato su GovPay durante l'aggiornamento: " + rpt_attuale.getStato() + ". Operazione di recupero annullata.");
						bd.disableSelectForUpdate();
						return false;
					}
					log.info("Aggiorno lo stato della RPT [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] in " + nuovoStato + ".");
					rptBD.updateRpt(rpt.getId(), nuovoStato, "Stato aggiornato in fase di recupero pendenti.", null, null,null);
					rpt.setStato(nuovoStato);
					rpt.setDescrizioneStato("Stato aggiornato in fase di recupero pendenti.");
					bd.disableSelectForUpdate();
					return true;
				}

			default:
				log.info("Rpt [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] in stato non terminale [" + rpt.getStato()+ "]. Eseguo un aggiornamento dello stato con il Nodo dei Pagamenti.");

				bd.closeConnection();
				NodoChiediStatoRPTRisposta risposta = null;
				NodoClient chiediStatoRptClient = null;
				try {
					try {
						String operationId = appContext.setupNodoClient(rpt.getStazione(bd).getCodStazione(), rpt.getCodDominio(), Azione.nodoChiediStatoRPT);
						NodoChiediStatoRPT richiesta = new NodoChiediStatoRPT();
						richiesta.setIdentificativoDominio(rpt.getCodDominio());
						richiesta.setIdentificativoIntermediarioPA(rpt.getStazione(bd).getIntermediario(bd).getCodIntermediario());
						richiesta.setIdentificativoStazioneIntermediarioPA(rpt.getStazione(bd).getCodStazione());
						richiesta.setPassword(rpt.getStazione(bd).getPassword());
						richiesta.setIdentificativoUnivocoVersamento(rpt.getIuv());
						richiesta.setCodiceContestoPagamento(rpt.getCcp());
						chiediStatoRptClient = new NodoClient(intermediario, null, giornale, bd);

						bd.setupConnection(ctx.getTransactionId());
						// salvataggio id Rpt/ versamento/ pagamento
						chiediStatoRptClient.getEventoCtx().setCodDominio(rpt.getCodDominio());
						chiediStatoRptClient.getEventoCtx().setIuv(rpt.getIuv());
						chiediStatoRptClient.getEventoCtx().setCcp(rpt.getCcp());
						chiediStatoRptClient.getEventoCtx().setIdA2A(rpt.getVersamento(bd).getApplicazione(bd).getCodApplicazione());
						chiediStatoRptClient.getEventoCtx().setIdPendenza(rpt.getVersamento(bd).getCodVersamentoEnte());
						try {
							if(rpt.getPagamentoPortale(bd) != null)
								chiediStatoRptClient.getEventoCtx().setIdPagamento(rpt.getPagamentoPortale(bd).getIdSessione());
						} catch (NotFoundException e) {	}
						chiediStatoRptClient.setOperationId(operationId); 
						risposta = chiediStatoRptClient.nodoChiediStatoRpt(richiesta, rpt.getStazione(bd).getIntermediario(bd).getDenominazione());
						chiediStatoRptClient.getEventoCtx().setEsito(Esito.OK);
					} catch (GovPayException e) {
						if(chiediStatoRptClient != null) {
							chiediStatoRptClient.getEventoCtx().setSottotipoEsito(e.getCodEsito().toString());
							chiediStatoRptClient.getEventoCtx().setEsito(Esito.FAIL);
							chiediStatoRptClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
						}
						throw e;
					} catch (ClientException e) {
						if(chiediStatoRptClient != null) {
							chiediStatoRptClient.getEventoCtx().setSottotipoEsito(e.getResponseCode() + "");
							chiediStatoRptClient.getEventoCtx().setEsito(Esito.FAIL);
							chiediStatoRptClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
						}
						throw e;
					} catch (ServiceException | UtilsException e) {
						if(chiediStatoRptClient != null) {
							chiediStatoRptClient.getEventoCtx().setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
							chiediStatoRptClient.getEventoCtx().setEsito(Esito.FAIL);
							chiediStatoRptClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
						}
						throw e;
					} finally {
						if(bd.isClosed())
							bd.setupConnection(ctx.getTransactionId());
					}

					if(risposta.getFault() != null) {
						if(chiediStatoRptClient != null) {
							chiediStatoRptClient.getEventoCtx().setSottotipoEsito(risposta.getFault().getFaultCode());
							chiediStatoRptClient.getEventoCtx().setEsito(Esito.KO);
							chiediStatoRptClient.getEventoCtx().setDescrizioneEsito(risposta.getFault().getDescription());
						}
						if(risposta.getFault().getFaultCode().equals(it.govpay.core.exceptions.NdpException.FaultNodo.PPT_RPT_SCONOSCIUTA.name())) {
							log.info("Rpt inesistene sul Nodo dei Pagamenti [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "]: aggiorno lo stato in " + StatoRpt.RPT_ERRORE_INVIO_A_NODO + ".");
							rpt.setStato(StatoRpt.RPT_ERRORE_INVIO_A_NODO);
							rpt.setDescrizioneStato("Stato sul nodo: PPT_RPT_SCONOSCIUTA");


							// Controllo che lo stato sia ancora quello originale per il successivo aggiornamento
							bd.enableSelectForUpdate();
							RptBD rptBD = new RptBD(bd);
							Rpt rpt_attuale = rptBD.getRpt(rpt.getId());
							if(!stato_originale.equals(rpt_attuale.getStato())) {
								// Lo stato e' cambiato. Rinuncio all'aggiornamento
								bd.disableSelectForUpdate();
								return false;
							}
							rptBD.updateRpt(rpt.getId(), StatoRpt.RPT_ERRORE_INVIO_A_NODO, "Stato sul nodo: PPT_RPT_SCONOSCIUTA", null, null,null);
							bd.disableSelectForUpdate();
							return true;
						}
						throw new GovPayException(risposta.getFault());
					} else {
						StatoRpt nuovoStato = Rpt.StatoRpt.toEnum(risposta.getEsito().getStato());
						EsitoPagamento esitoPagamento = nuovoStato.equals(Rpt.StatoRpt.RPT_RIFIUTATA_NODO) ? EsitoPagamento.RIFIUTATO: null;

						log.info("Acquisito dal Nodo dei Pagamenti lo stato della RPT [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "]: " + nuovoStato);

						switch (nuovoStato) {
						case RT_ACCETTATA_NODO:
						case RT_ESITO_SCONOSCIUTO_PA:
						case RT_RIFIUTATA_PA:
						case RT_ACCETTATA_PA:
						case RT_ERRORE_INVIO_A_PA:
						case RPT_ANNULLATA:

							log.info("Richiesta dell'RT al Nodo dei Pagamenti [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "].");

							bd.closeConnection();
							NodoChiediCopiaRTRisposta nodoChiediCopiaRTRisposta = null;
							NodoClient chiediCopiaRTClient = null;
							String operationId = null;
							try { 
								try {
									operationId = appContext.setupNodoClient(rpt.getStazione(bd).getCodStazione(), rpt.getCodDominio(), Azione.nodoChiediCopiaRT);
									NodoChiediCopiaRT nodoChiediCopiaRT = new NodoChiediCopiaRT();
									nodoChiediCopiaRT.setIdentificativoDominio(rpt.getCodDominio());
									nodoChiediCopiaRT.setIdentificativoIntermediarioPA(rpt.getIntermediario(bd).getCodIntermediario());
									nodoChiediCopiaRT.setIdentificativoStazioneIntermediarioPA(rpt.getStazione(bd).getCodStazione());
									nodoChiediCopiaRT.setPassword(rpt.getStazione(bd).getPassword());
									nodoChiediCopiaRT.setIdentificativoUnivocoVersamento(rpt.getIuv());
									nodoChiediCopiaRT.setCodiceContestoPagamento(rpt.getCcp());
									chiediCopiaRTClient = new NodoClient(intermediario, null, giornale, bd);
									chiediCopiaRTClient.setOperationId(operationId);
									// salvataggio id Rpt/ versamento/ pagamento
									chiediCopiaRTClient.getEventoCtx().setCodDominio(rpt.getCodDominio());
									chiediCopiaRTClient.getEventoCtx().setIuv(rpt.getIuv());
									chiediCopiaRTClient.getEventoCtx().setCcp(rpt.getCcp());
									chiediCopiaRTClient.getEventoCtx().setIdA2A(rpt.getVersamento(bd).getApplicazione(bd).getCodApplicazione());
									chiediCopiaRTClient.getEventoCtx().setIdPendenza(rpt.getVersamento(bd).getCodVersamentoEnte());
									try {
										if(rpt.getPagamentoPortale(bd) != null)
											chiediCopiaRTClient.getEventoCtx().setIdPagamento(rpt.getPagamentoPortale(bd).getIdSessione());
									} catch (NotFoundException e) {	}
									nodoChiediCopiaRTRisposta = chiediCopiaRTClient.nodoChiediCopiaRT(nodoChiediCopiaRT, rpt.getIntermediario(bd).getDenominazione());
									chiediCopiaRTClient.getEventoCtx().setEsito(Esito.OK);
								}  catch (GovPayException e) {
									if(chiediCopiaRTClient != null) {
										chiediCopiaRTClient.getEventoCtx().setSottotipoEsito(e.getCodEsito().toString());
										chiediStatoRptClient.getEventoCtx().setEsito(Esito.FAIL);
										chiediCopiaRTClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
									}
									throw e;
								} catch (ClientException e) {
									if(chiediCopiaRTClient != null) {
										chiediCopiaRTClient.getEventoCtx().setSottotipoEsito(e.getResponseCode() + "");
										chiediCopiaRTClient.getEventoCtx().setEsito(Esito.FAIL);
										chiediCopiaRTClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
									}
									throw e;
								} catch (ServiceException | UtilsException e) {
									if(chiediCopiaRTClient != null) {
										chiediCopiaRTClient.getEventoCtx().setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
										chiediCopiaRTClient.getEventoCtx().setEsito(Esito.FAIL);
										chiediCopiaRTClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
									}
									throw e;
								} finally {
									bd.setupConnection(ctx.getTransactionId());
								}

								byte[] rtByte = null;
								try {
									ByteArrayOutputStream output = new ByteArrayOutputStream();
									DataHandler dh = nodoChiediCopiaRTRisposta.getRt();
									dh.writeTo(output);
									rtByte = output.toByteArray();
								} catch (IOException e) {
									if(chiediCopiaRTClient != null) {
										chiediCopiaRTClient.getEventoCtx().setSottotipoEsito(EsitoOperazione.INTERNAL.name());
										chiediCopiaRTClient.getEventoCtx().setEsito(Esito.FAIL);
										chiediCopiaRTClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
									}
									log.error("Errore durante la lettura dell'RT: " + e);
									throw new GovPayException(EsitoOperazione.INTERNAL, e);
								}

								if(nodoChiediCopiaRTRisposta.getFault() != null) {
									if(chiediCopiaRTClient != null) {
										chiediCopiaRTClient.getEventoCtx().setSottotipoEsito(nodoChiediCopiaRTRisposta.getFault().getFaultCode());
										chiediCopiaRTClient.getEventoCtx().setEsito(Esito.KO);
										chiediCopiaRTClient.getEventoCtx().setDescrizioneEsito(nodoChiediCopiaRTRisposta.getFault().getDescription());
									}
									log.info("Fault nell'acquisizione dell'RT: [" + nodoChiediCopiaRTRisposta.getFault().getFaultCode() + "] " + nodoChiediCopiaRTRisposta.getFault().getFaultString());
									return false;
								}

								if(operationId != null) {
									appContext.getServerByOperationId(operationId).addGenericProperty(new Property("ccp", rpt.getCcp()));
									appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", rpt.getCodDominio()));
									appContext.getServerByOperationId(operationId).addGenericProperty(new Property("iuv", rpt.getIuv()));
								} else {
									appContext.getTransaction().getLastServer().addGenericProperty(new Property("ccp", rpt.getCcp()));
									appContext.getTransaction().getLastServer().addGenericProperty(new Property("codDominio", rpt.getCodDominio()));
									appContext.getTransaction().getLastServer().addGenericProperty(new Property("iuv", rpt.getIuv()));
								}
								ctx.getApplicationLogger().log("pagamento.recuperoRt");
								rpt = RtUtils.acquisisciRT(rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp(), rtByte, true, bd);
								chiediCopiaRTClient.getEventoCtx().setDescrizioneEsito("Acquisita ricevuta di pagamento [IUV: " + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] emessa da " + rpt.getDenominazioneAttestante());
								appContext.getResponse().addGenericProperty(new Property("esitoPagamento", rpt.getEsitoPagamento().toString()));
								ctx.getApplicationLogger().log("pagamento.acquisizioneRtOk");
							}finally {
								if(chiediCopiaRTClient != null && chiediCopiaRTClient.getEventoCtx().isRegistraEvento()) {
									GiornaleEventi giornaleEventi = new GiornaleEventi(bd);
									giornaleEventi.registraEvento(chiediCopiaRTClient.getEventoCtx().toEventoDTO());
								}
							}

							return true;

						case RPT_RIFIUTATA_NODO:
						case RPT_RIFIUTATA_PSP:
						case RPT_ERRORE_INVIO_A_PSP:
							// Controllo che lo stato sia ancora quello originale per il successivo aggiornamento
							bd.enableSelectForUpdate();
							RptBD rptBD = new RptBD(bd);
							Rpt rpt_attuale_tmp = rptBD.getRpt(rpt.getId());
							if(!stato_originale.equals(rpt_attuale_tmp.getStato())) {
								// Lo stato e' cambiato. Rinuncio all'aggiornamento
								log.info("Lo stato della RPT [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] risulta cambiato su GovPay durante l'aggiornamento: " + rpt_attuale_tmp.getStato() + ". Operazione di recupero annullata.");
								bd.disableSelectForUpdate();
								return false;
							}

							boolean wasAutoCommit = bd.isAutoCommit();
							try {

								if(wasAutoCommit)
									bd.setAutoCommit(false);

								// inserisco una notifica di fallimento
								Notifica notificaFallimento = new Notifica(rpt, TipoNotifica.FALLIMENTO, bd);
								notificaBD = new it.govpay.core.business.Notifica(bd);
								insertNotificaOk = notificaBD.inserisciNotifica(notificaFallimento);

								msg = insertNotificaOk ? ", Schedulazione notifica di Fallimento del tentativo." : ".";
								log.info("Aggiorno lo stato della RPT [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] in " + nuovoStato + msg);
								rptBD.updateRpt(rpt.getId(), nuovoStato, "Stato acquisito da Nodo dei Pagamenti", null, null,esitoPagamento);
								rpt.setStato(nuovoStato);
								rpt.setDescrizioneStato("Stato acquisito da Nodo dei Pagamenti");
								bd.disableSelectForUpdate();

								// aggiornamento del pagamento portale
								Long idPagamentoPortale = rpt.getIdPagamentoPortale();
								if(idPagamentoPortale != null) {
									PagamentoPortaleUtils.aggiornaPagamentoPortale(idPagamentoPortale, bd); 
								}

								if(!bd.isAutoCommit())
									bd.commit();
							}catch(ServiceException e) {
								if(!bd.isAutoCommit())
									bd.rollback();
								throw e;
							} finally {
								if(wasAutoCommit)
									bd.setAutoCommit(true);
							}

							return true;
						default:

							// Controllo che lo stato sia ancora quello originale per il successivo aggiornamento
							bd.enableSelectForUpdate();
							rptBD = new RptBD(bd);
							Rpt rpt_attuale = rptBD.getRpt(rpt.getId());
							if(!stato_originale.equals(rpt_attuale.getStato())) {
								// Lo stato e' cambiato. Rinuncio all'aggiornamento
								log.info("Lo stato della RPT [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] risulta cambiato su GovPay durante l'aggiornamento: " + rpt_attuale.getStato() + ". Operazione di recupero annullata.");
								bd.disableSelectForUpdate();
								return false;
							}
							log.info("Aggiorno lo stato della RPT [Dominio:" + rpt.getCodDominio() + " IUV:" + rpt.getIuv() + " CCP:" + rpt.getCcp() + "] in " + nuovoStato + ".");
							rptBD.updateRpt(rpt.getId(), nuovoStato, "Stato acquisito da Nodo dei Pagamenti", null, null,esitoPagamento);
							rpt.setStato(nuovoStato);
							rpt.setDescrizioneStato("Stato acquisito da Nodo dei Pagamenti");
							bd.disableSelectForUpdate();
							return true;
						}
					}
				}finally {
					if(chiediStatoRptClient != null && chiediStatoRptClient.getEventoCtx().isRegistraEvento()) {
						GiornaleEventi giornaleEventi = new GiornaleEventi(bd);
						giornaleEventi.registraEvento(chiediStatoRptClient.getEventoCtx().toEventoDTO());
					}
				}
			}
		} catch(NotFoundException e) {
			throw new ServiceException(e);
		}
	}
}
