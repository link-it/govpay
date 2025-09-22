/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.core.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.context.application.ApplicationContext;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import gov.telematici.pagamenti.ws.rpt.FaultBean;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.beans.EventoContext.Esito;
import it.govpay.core.business.model.Risposta;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.NotificaException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoNonValidoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.DateUtils;
import it.govpay.core.utils.EventoUtils;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.LogUtils;
import it.govpay.core.utils.RptBuilder;
import it.govpay.core.utils.RptUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.UrlUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.client.exception.ClientInitializeException;
import it.govpay.core.utils.logger.MessaggioDiagnosticoCostanti;
import it.govpay.core.utils.logger.MessaggioDiagnosticoUtils;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Anagrafica;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Intermediario;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.configurazione.Giornale;

public class Rpt {

	private static Logger log = LoggerWrapperFactory.getLogger(Rpt.class);

	public Rpt() {
		//nothing
	}

	public List<it.govpay.bd.model.Rpt> avviaTransazione(List<Versamento> versamenti, Canale canale, String ibanAddebito, 
			Anagrafica versante, String autenticazione, String redirect, boolean aggiornaSeEsiste, PagamentoPortale pagamentoPortale, String codiceConvenzione) throws GovPayException, UtilsException {
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		try {
			appContext.getPagamentoCtx().setCarrello(true);
			String codCarrello = RptUtils.buildUUID35();
			if(pagamentoPortale != null) codCarrello = pagamentoPortale.getIdSessione();
			appContext.getPagamentoCtx().setCodCarrello(codCarrello);
			appContext.setCorrelationId(codCarrello);
			appContext.getRequest().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_CARRELLO, appContext.getPagamentoCtx().getCodCarrello()));
			MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_PAGAMENTO_AVVIA_TRANSAZIONE_CARRELLO_WISP20);

			Stazione stazione = null;
			Giornale giornale = new it.govpay.core.business.Configurazione().getConfigurazione().getGiornale();

			for (int i = 0; i < versamenti.size() ; i++) {
				Versamento versamentoModel  = versamenti.get(i);

				String codApplicazione = versamentoModel.getApplicazione(configWrapper).getCodApplicazione();
				String codVersamentoEnte = versamentoModel.getCodVersamentoEnte(); 
				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RPT_VALIDAZIONE_SEMANTICA, codApplicazione, codVersamentoEnte);

				log.debug("Verifica autorizzazione pagamento del versamento [{}] applicazione [{}]...", codVersamentoEnte, codApplicazione);
				if(!versamentoModel.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
					log.debug("Non autorizzato pagamento del versamento [{}] applicazione [{}]: pagamento in stato diverso da {}", codVersamentoEnte, codApplicazione, StatoVersamento.NON_ESEGUITO);
					throw new GovPayException(EsitoOperazione.PAG_006, codApplicazione, codVersamentoEnte, versamentoModel.getStatoVersamento().toString());
				} else {
					log.debug("Autorizzato pagamento del versamento [{}] applicazione [{}]: pagamento in stato {}", codVersamentoEnte, codApplicazione, StatoVersamento.NON_ESEGUITO);
				}

				log.debug("Verifica scadenza del versamento [{}] applicazione [{}]...", codVersamentoEnte, codApplicazione);
				if(versamentoModel.getDataScadenza() != null && DateUtils.isDataDecorsa(versamentoModel.getDataScadenza(), DateUtils.CONTROLLO_SCADENZA)) {
					log.warn("Scadenza del versamento [{}] applicazione [{}] decorsa.", codVersamentoEnte, codApplicazione);
					throw new GovPayException(EsitoOperazione.PAG_007, codApplicazione, codVersamentoEnte, SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(versamentoModel.getDataScadenza()));
				} else { // versamento non scaduto, controllo data validita'
					log.debug("Verifica validità del versamento [{}] applicazione [{}]...", codVersamentoEnte, codApplicazione);
					if(versamentoModel.getDataValidita() != null && DateUtils.isDataDecorsa(versamentoModel.getDataValidita(), DateUtils.CONTROLLO_VALIDITA)) {

						if(versamentoModel.getId() == null) {
							// Versamento fornito scaduto. Ritorno errore.
							throw new GovPayException(EsitoOperazione.PAG_012, codApplicazione, codVersamentoEnte, SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(versamentoModel.getDataValidita()));
						} else {
							// Versammento in archivio scaduto. Ne chiedo un aggiornamento.
							log.info("Validità del versamento [{}] applicazione [{}] decorsa. Avvio richiesta di aggiornamento all'applicazione.", codVersamentoEnte, codApplicazione);
							try {
								versamentoModel = VersamentoUtils.aggiornaVersamento(versamentoModel, log);
								versamenti.set(i, versamentoModel); // aggiorno il versamento all'interno della lista, l'oggetto restituito in caso di verifica e' nuovo le modifiche a versamentoModel vengono perse 
								log.info("Versamento [{}] applicazione [{}] aggiornato tramite servizio di verifica.", codVersamentoEnte, codApplicazione);
							} catch (VersamentoAnnullatoException e){
								log.warn("Aggiornamento del versamento [{}] applicazione [{}] fallito: versamento annullato", codVersamentoEnte, codApplicazione);
								throw new GovPayException(EsitoOperazione.VER_013, codApplicazione, codVersamentoEnte);
							} catch (VersamentoScadutoException e) {
								log.warn("Aggiornamento del versamento [{}] applicazione [{}] fallito: versamento scaduto", codVersamentoEnte, codApplicazione);
								throw new GovPayException(EsitoOperazione.VER_010, codApplicazione, codVersamentoEnte);
							} catch (VersamentoDuplicatoException e) {
								log.warn("Aggiornamento del versamento [{}] applicazione [{}] fallito: versamento duplicato", codVersamentoEnte, codApplicazione);
								throw new GovPayException(EsitoOperazione.VER_012, codApplicazione, codVersamentoEnte);
							} catch (VersamentoSconosciutoException e) {
								log.warn("Aggiornamento del versamento [{}] applicazione [{}] fallito: versamento sconosciuto", codVersamentoEnte, codApplicazione);
								throw new GovPayException(EsitoOperazione.VER_011, codApplicazione, codVersamentoEnte);
							} catch (ClientException e) {
								log.warn("Aggiornamento del versamento [{}] applicazione [{}] fallito: errore di interazione con il servizio di verifica.", codVersamentoEnte, codApplicazione);
								throw new GovPayException(EsitoOperazione.VER_014, codApplicazione, codVersamentoEnte, e.getMessage());
							} catch (VersamentoNonValidoException e) {
								log.warn("Aggiornamento del versamento [{}] applicazione [{}] fallito: errore di validazione dei dati ricevuti dal servizio di verifica.", codVersamentoEnte, codApplicazione);
								throw new GovPayException(EsitoOperazione.VER_014, codApplicazione, codVersamentoEnte, e.getMessage());
							}
						}
					} else { 
						// versamento valido passo
					}
				}

				log.debug("Scadenza del versamento [{}] applicazione [{}] verificata.", codVersamentoEnte, codApplicazione);

				if(stazione == null) {
					stazione = versamentoModel.getDominio(configWrapper).getStazione();
				} else {
					if(stazione.getId().compareTo(versamentoModel.getDominio(configWrapper).getStazione().getId()) != 0) {
						throw new GovPayException(EsitoOperazione.PAG_000);
					}
				}

				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RPT_VALIDAZIONE_SEMANTICA_OK, codApplicazione, codVersamentoEnte);
			}

			Intermediario intermediario = null;
			try {
				intermediario = AnagraficaManager.getIntermediario(configWrapper, stazione.getIdIntermediario());
			} catch (NotFoundException e1) {
				throw new GovPayException(e1);
			}

			Iuv iuvBusiness = new Iuv();

			RptBD rptBD = null;
			it.govpay.core.business.Versamento versamentiBusiness = new it.govpay.core.business.Versamento();
			List<it.govpay.bd.model.Rpt> rpts = new ArrayList<>();

			try {
				rptBD = new RptBD(configWrapper);

				rptBD.setupConnection(configWrapper.getTransactionID());

				rptBD.setAutoCommit(false);

				rptBD.setAtomica(false);

				for(Versamento versamento : versamenti) {
					// Aggiorno tutti i versamenti che mi sono stati passati
					if(versamento.getId() == null) {
						versamentiBusiness.caricaVersamento(versamento, false, aggiornaSeEsiste, false, null, rptBD);
					}
					String iuv = null;
					String ccp = null;

					// Verifico se ha uno IUV suggerito ed in caso lo assegno
					if(versamento.getIuvProposto() != null) {
						log.debug("IUV Proposto: {}", versamento.getIuvProposto());
						TipoIUV tipoIuv = iuvBusiness.getTipoIUV(versamento.getIuvProposto());
						iuvBusiness.checkIUV(versamento.getDominio(configWrapper), versamento.getIuvProposto(), tipoIuv);
						iuv = versamento.getIuvProposto();
						ccp = IuvUtils.buildCCP();
						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_IUV_ASSEGNAZIONE_IUV_CUSTOM, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), versamento.getDominio(configWrapper).getCodDominio(), versamento.getIuvProposto(), ccp);
					} else {
						// Verifico se ha gia' uno IUV numerico assegnato. In tal caso lo riuso. 
						iuv = versamento.getIuvVersamento();
						if(iuv != null) {
							log.debug("Iuv gia' assegnato: {}", iuv);
							ccp = IuvUtils.buildCCP();
							MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_IUV_ASSEGNAZIONE_IUV_RIUSO, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), versamento.getDominio(configWrapper).getCodDominio(), iuv, ccp);
						} else {
							log.debug("Iuv non assegnato. Generazione...");
							
							// Non c'e' iuv assegnato. Glielo genero io sempre di tipo numerico.
							iuv = iuvBusiness.generaIUV(versamento.getApplicazione(configWrapper), versamento.getDominio(configWrapper), versamento.getCodVersamentoEnte(), it.govpay.model.Iuv.TipoIUV.NUMERICO, rptBD);
							ccp = IuvUtils.buildCCP();
							MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_IUV_ASSEGNAZIONE_IUV_GENERATO, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), versamento.getDominio(configWrapper).getCodDominio(), iuv, ccp);
						}
					}

					// controllo se la pendenza e' multibeneficiario, se lo e' allora non puo' essere pagata col modello3 SANP 2.3
					if(VersamentoUtils.isPendenzaMultibeneficiario(versamento, configWrapper)) {
						throw new GovPayException(EsitoOperazione.VER_038, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte());
					}

					// blocco dei pagamenti modello 3 per le RPT SANP 2.4.0
					// il nuovo pagamento modello 3 ha un timeout sempre impostato di max 30 minuti
					if(pagamentoPortale != null && pagamentoPortale.getTipo() == 1) {
						log.debug("Blocco pagamento per il Mod1 SANP 2.4 attivo con soglia: [{} minuti]", GovpayConfig.getInstance().getTimeoutPendentiModello3SANP24Mins());
						log.debug("Controllo che non ci siano transazioni di pagamento in corso per il versamento [IdA2A:{}, IdPendenza:{}].", versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte());

						// Controllo che non ci sia un pagamento in corso per i versamenti che sto provando ad eseguire
						RptFilter filter = rptBD.newFilter();
						filter.setStato(it.govpay.model.Rpt.getStatiPendenti());
						filter.setIdVersamento(versamento.getId());
						filter.setVersione(it.govpay.model.Rpt.VersioneRPT.SANP_240.toString());
						List<it.govpay.bd.model.Rpt> rptPendenti = rptBD.findAll(filter);

						log.debug("Trovate [{}] RPT pendenti per il versamento [IdA2A:{}, IdPendenza:{}].", rptPendenti.size(), versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte());

						// Per tutte quelle in corso controllo se hanno passato la soglia di timeout
						// Altrimenti lancio il fault
						Date dataSoglia = new Date(new Date().getTime() - GovpayConfig.getInstance().getTimeoutPendentiModello3SANP24Mins() * 60000);

						for(it.govpay.bd.model.Rpt rpt_pendente : rptPendenti) {
							Date dataMsgRichiesta = rpt_pendente.getDataMsgRichiesta();

							// se l'RPT e' bloccata allora controllo che il blocco sia indefinito oppure definito, altrimenti passo
							if(rpt_pendente.isBloccante() && dataSoglia.before(dataMsgRichiesta)) {
								throw new GovPayException(EsitoOperazione.PAG_014, rpt_pendente.getCodDominio(), rpt_pendente.getIuv(), rpt_pendente.getCcp());
							}
						}
					}

					// blocco dei pagamenti modello 1 per le RPT SANP 2.3.0
					if(pagamentoPortale !=  null && pagamentoPortale.getTipo() == 1 && GovpayConfig.getInstance().isTimeoutPendentiModello1()) {
						log.debug("Blocco pagamento per il Mod1 SANP 2.3 attivo con soglia: [{} minuti]", GovpayConfig.getInstance().getTimeoutPendentiModello1Mins());
						log.debug("Controllo che non ci siano transazioni di pagamento in corso per il versamento [IdA2A:{}, IdPendenza:{}].", versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte());

						// Controllo che non ci sia un pagamento in corso per i versamenti che sto provando ad eseguire
						RptFilter filter = rptBD.newFilter();
						filter.setStato(it.govpay.model.Rpt.getStatiPendenti());
						filter.setIdVersamento(versamento.getId());
						filter.setVersione(it.govpay.model.Rpt.VersioneRPT.SANP_230.toString());
						List<it.govpay.bd.model.Rpt> rptPendenti = rptBD.findAll(filter);

						log.debug("Trovate [{}] RPT pendenti per il versamento [IdA2A:{}, IdPendenza:{}].", rptPendenti.size(), versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte());

						// Per tutte quelle in corso controllo se hanno passato la soglia di timeout
						// Altrimenti lancio il fault
						Date dataSoglia = new Date(new Date().getTime() - GovpayConfig.getInstance().getTimeoutPendentiModello1Mins() * 60000);

						for(it.govpay.bd.model.Rpt rpt_pendente : rptPendenti) {
							Date dataMsgRichiesta = rpt_pendente.getDataMsgRichiesta();

							// se l'RPT e' bloccata allora controllo che il blocco sia indefinito oppure definito, altrimenti passo
							if(rpt_pendente.isBloccante() && (GovpayConfig.getInstance().getTimeoutPendentiModello1Mins() == 0 || dataSoglia.before(dataMsgRichiesta))) {
								throw new GovPayException(EsitoOperazione.PAG_014, rpt_pendente.getCodDominio(), rpt_pendente.getIuv(), rpt_pendente.getCcp());
							}
						}
					}

					if(appContext.getPagamentoCtx().getCodCarrello() != null) {
						appContext.setCorrelationId(appContext.getPagamentoCtx().getCodCarrello());
					} else {
						appContext.setCorrelationId(versamento.getDominio(configWrapper).getCodDominio() + iuv + ccp);
					}
					it.govpay.bd.model.Rpt rpt = new RptBuilder().buildRpt(appContext.getPagamentoCtx().getCodCarrello(), versamento, canale, iuv, ccp, versante, autenticazione, ibanAddebito, redirect);
					rpt.setCodSessionePortale(appContext.getPagamentoCtx().getCodSessionePortale());

					if(pagamentoPortale!= null) {
						rpt.setIdPagamentoPortale(pagamentoPortale.getId());
						rpt.setPagamentoPortale(pagamentoPortale);
					}

					rptBD.insertRpt(rpt);
					rpts.add(rpt);
					MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RPT_CREAZIONE_RPT, versamento.getDominio(configWrapper).getCodDominio(), iuv, ccp, rpt.getCodMsgRichiesta());
					log.info("Inserita RPT per il versamento ({}) dell'applicazione ({}) con dominio ({}) iuv ({}) ccp ({})", versamento.getCodVersamentoEnte(), versamento.getApplicazione(configWrapper).getCodApplicazione(), rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp());
				} 

				rptBD.commit();
			} catch (ServiceException | GovPayException e){
				if(rptBD != null) {
					rptBD.rollback();
				}
				throw e;
			} finally {
				if(rptBD != null) {
					// ripristino autocommit
					if(!rptBD.isAutoCommit() ) {
						rptBD.setAutoCommit(true);
					}
					
					rptBD.closeConnection();
				}
			}

			// Spedisco le RPT al Nodo
			// Se ho una GovPayException, non ho sicuramente spedito nulla.
			// Se ho una ClientException non so come sia andata la consegna.
			NodoClient clientInviaCarrelloRPT = null;
			rptBD = null;
			EventoContext inviaCarrelloRPTEventoCtx = new EventoContext(Componente.API_PAGOPA);
			try {
				Risposta risposta = null;
				String operationId = appContext.setupNodoClient(stazione.getCodStazione(), null, EventoContext.Azione.NODOINVIACARRELLORPT);
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_CARRELLO, appContext.getPagamentoCtx().getCodCarrello()));
				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RPT_INVIO_CARRELLO_RPT);
				clientInviaCarrelloRPT = new it.govpay.core.utils.client.NodoClient(intermediario, operationId, giornale, inviaCarrelloRPTEventoCtx);
				risposta = RptUtils.inviaCarrelloRPT(clientInviaCarrelloRPT, intermediario, stazione, rpts, codiceConvenzione);

				// ripristino la connessione
				rptBD = new RptBD(configWrapper);

				if(risposta.getEsito() == null || !risposta.getEsito().equals("OK")) {
					// RPT rifiutata dal Nodo
					// Aggiorno lo stato e ritorno l'errore
					try {

						for(FaultBean fb : risposta.getListaErroriRPT()) {
							if(fb==null) continue;
							it.govpay.bd.model.Rpt rpt = rpts.get(fb.getSerial() - 1);
							String descrizione = null; 
							String faultCode = null;
							if(fb != null) {
								faultCode = fb.getFaultCode();
								descrizione = "[" + fb.getFaultCode() + "] " + fb.getFaultString();
								descrizione = fb.getDescription() != null ? descrizione + ": " + fb.getDescription() : descrizione;
							}
							rpt.setStato(StatoRpt.RPT_RIFIUTATA_NODO);
							rpt.setDescrizioneStato(descrizione);
							rpt.setEsitoPagamento(EsitoPagamento.RIFIUTATO);
							rpt.setFaultCode(faultCode);
							log.info("Aggiorno stato = RPT_RIFIUTATA_NODO per l'RPT [{}/{}/{}]", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp());
							rptBD.updateRpt(rpt.getId(), StatoRpt.RPT_RIFIUTATA_NODO, descrizione, null, null,EsitoPagamento.RIFIUTATO);
						}

					} catch (Exception e) {
						log.warn("Si e' verificato un errore durante l'aggioramento della RPT: {} ,{}", e.getMessage(), e);
						// Se uno o piu' aggiornamenti vanno male, non importa. 
						// si risolvera' poi nella verifica pendenti
					} 
					// Potrebbero rimanere escluse delle RPT dall'aggiornamento:
					for(it.govpay.bd.model.Rpt rpt : rpts) {
						if(!rpt.getStato().equals(StatoRpt.RPT_RIFIUTATA_NODO)) {
							try {
								String descrizione = "Richiesta di pagamento rifiutata per errori rilevati in altre RPT del carrello";
								rpt.setDescrizioneStato(descrizione);
								rptBD.updateRpt(rpt.getId(), StatoRpt.RPT_RIFIUTATA_NODO, descrizione, null, null,EsitoPagamento.RIFIUTATO);
							} catch (NotFoundException e) {
								// Se uno o piu' aggiornamenti vanno male, non importa. 
								// si risolvera' poi nella verifica pendenti
							}
						}
					}
					MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RPT_INVIO_KO, risposta.getLog());
					log.info("RPT rifiutata dal Nodo dei Pagamenti: {}" , risposta.getLog());
					if(inviaCarrelloRPTEventoCtx != null) {
						inviaCarrelloRPTEventoCtx.setSottotipoEsito(risposta.getFaultBean().getFaultCode());
						inviaCarrelloRPTEventoCtx.setEsito(Esito.KO);
					}
					throw new GovPayException(Rpt.toFaultBean(risposta.getFaultBean()));
				} else {
					log.info("Rpt accettata dal Nodo dei Pagamenti");
					// RPT accettata dal Nodo
					// Aggiorno lo stato e ritorno
					if(risposta.getUrl() != null) {
						log.debug("Redirect URL: {}", risposta.getUrl());
						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RPT_INVIO_OK);
					} else {
						log.debug("Nessuna URL di redirect");
						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RPT_INVIO_OK_NO_REDIRECT);
					}
					inviaCarrelloRPTEventoCtx.setEsito(Esito.OK);
					return this.updateStatoRpt(rpts, StatoRpt.RPT_ACCETTATA_NODO, risposta.getUrl(), null);
				}
			} catch (ClientInitializeException | ClientException e) {
				// ClientException: tento una chiedi stato RPT per recuperare lo stato effettivo.
				//   - RPT non esistente: rendo un errore NDP per RPT non inviata
				//   - RPT esistente: faccio come OK
				//   - Errore nella richiesta: rendo un errore NDP per stato sconosciuto
				if(inviaCarrelloRPTEventoCtx != null) {
					if(e instanceof ClientException clientException) {
						log.warn("Errore nella spedizione del carrello RPT: " +e.getMessage(), e); 
						inviaCarrelloRPTEventoCtx.setSottotipoEsito(clientException.getResponseCode() + "");
					} else {
						log.error("Errore nella creazione del client per la spedizione del carrello RPT: " + e.getMessage(), e);
						inviaCarrelloRPTEventoCtx.setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
					}
					
					inviaCarrelloRPTEventoCtx.setEsito(Esito.FAIL);
					inviaCarrelloRPTEventoCtx.setDescrizioneEsito(e.getMessage());
					inviaCarrelloRPTEventoCtx.setException(e);
				}
				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_RPT_INVIO_FAIL, e.getMessage());
				
				try {
					// 2024-11-04 eliminato recupero della rpt, operazione non piu' supportata da PagoPA
					this.updateStatoRpt(rpts, StatoRpt.RPT_ERRORE_INVIO_A_NODO, "Impossibile comunicare con il Nodo dei Pagamenti SPC: " + e.getMessage(), e);
					throw new GovPayException(EsitoOperazione.NDP_000, e, "Errore nella consegna della richiesta di pagamento al Nodo dei Pagamenti");
				} catch (NotificaException e1) {
					throw new GovPayException(e);
				} finally {
					// donothing
				}
			} catch (NotificaException e) {
				throw new GovPayException(e);
			}  finally {

				if(inviaCarrelloRPTEventoCtx != null && inviaCarrelloRPTEventoCtx.isRegistraEvento()) {
					EventiBD eventiBD = new EventiBD(configWrapper);
					for(it.govpay.bd.model.Rpt rpt : rpts) {
						// salvataggio id Rpt/ versamento/ pagamento
						inviaCarrelloRPTEventoCtx.setCodDominio(rpt.getCodDominio());
						inviaCarrelloRPTEventoCtx.setIuv(rpt.getIuv());
						inviaCarrelloRPTEventoCtx.setCcp(rpt.getCcp());
						inviaCarrelloRPTEventoCtx.setIdA2A(rpt.getVersamento().getApplicazione(configWrapper).getCodApplicazione());
						inviaCarrelloRPTEventoCtx.setIdPendenza(rpt.getVersamento().getCodVersamentoEnte());
						if(rpt.getPagamentoPortale() != null)
							inviaCarrelloRPTEventoCtx.setIdPagamento(rpt.getPagamentoPortale().getIdSessione());

						GiornaleEventi.popolaEventoCooperazione(rpt, intermediario, stazione, inviaCarrelloRPTEventoCtx); 

						if(rpt.getFaultCode() != null)
							inviaCarrelloRPTEventoCtx.setSottotipoEsito(rpt.getFaultCode());
						if(!inviaCarrelloRPTEventoCtx.getEsito().equals(Esito.OK) && inviaCarrelloRPTEventoCtx.getDescrizioneEsito() == null) {
							inviaCarrelloRPTEventoCtx.setDescrizioneEsito(rpt.getDescrizioneStato());
						}

						eventiBD.insertEvento(EventoUtils.toEventoDTO(inviaCarrelloRPTEventoCtx,log));
					}
				}
			}
		} catch (ServiceException | IOException e) {
			throw new GovPayException(e);
		} 
	}

	private List<it.govpay.bd.model.Rpt> updateStatoRpt(List<it.govpay.bd.model.Rpt> rpts, StatoRpt statoRpt, String url, Exception e) throws ServiceException, GovPayException, NotificaException { 
		IContext ctx = ContextThreadLocal.get();
		ApplicationContext appContext = (ApplicationContext) ctx.getApplicationContext();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);

		String sessionId = null;
		it.govpay.core.business.Notifica notificaBD = new it.govpay.core.business.Notifica();
		try {
			if(url != null)
				sessionId = UrlUtils.getCodSessione(url);
		} catch (Exception ee) {
			log.warn("Impossibile acquisire l'idSessione dalla URL di redirect al PSP: " + url, ee);
		}

		RptBD rptBD = new RptBD(configWrapper);

		for(it.govpay.bd.model.Rpt rpt : rpts) {
			Notifica notifica = new Notifica(rpt, TipoNotifica.ATTIVAZIONE, configWrapper);
			rpt.setPspRedirectURL(url);
			rpt.setStato(statoRpt);
			rpt.setCodSessione(sessionId);
			try {
				rptBD.setupConnection(configWrapper.getTransactionID());
				rptBD.setAutoCommit(false);
				rptBD.setAtomica(false);
				rptBD.updateRpt(rpt.getId(), statoRpt, null, sessionId, url,null);
				boolean schedulaThreadInvio = notificaBD.inserisciNotifica(notifica, rptBD);
				rptBD.commit();
				if(schedulaThreadInvio)
					ThreadExecutorManager.getClientPoolExecutorNotifica().execute(new InviaNotificaThread(notifica, ctx));
			} catch (Exception ee) {
				// Se uno o piu' aggiornamenti vanno male, non importa. 
				// si risolvera' poi nella verifica pendenti 
				rptBD.rollback();
			} finally {
				if(rptBD != null) {
					// ripristino autocommit
					if(!rptBD.isAutoCommit() ) {
						rptBD.setAutoCommit(true);
					}
					
					rptBD.closeConnection();
				}
			}
		}

		switch (statoRpt) {
		case RPT_RIFIUTATA_NODO, RPT_ERRORE_INVIO_A_NODO, RPT_ERRORE_INVIO_A_PSP, RPT_RIFIUTATA_PSP:
			// Casi di rifiuto. Rendo l'errore
			if(e!= null)
				throw new GovPayException(EsitoOperazione.NDP_000, e);
			else 
				throw new GovPayException(EsitoOperazione.NDP_000);
		default:

			String codSessione = rpts.get(0).getCodSessione();

			if(codSessione != null) {
				appContext.getResponse().addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_PSP_SESSION, codSessione));
			}

			if(codSessione != null) {
				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_PAGAMENTO_INVIO_CARRELLO_RPT);
			} else {
				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_PAGAMENTO_INVIO_CARRELLO_RPT_NO_REDIRECT);
			}

			log.info("RPT inviata correttamente al nodo");
			return rpts;
		}

	}

	public void annullaRPTPendenti(String codDominio, String iuv, BDConfigWrapper configWrapper)  throws ServiceException {
		RptBD rptBD = null;
		try {
			rptBD = new RptBD(configWrapper);

			rptBD.setupConnection(configWrapper.getTransactionID());

			rptBD.setAtomica(false);

			rptBD.setAutoCommit(false);

			rptBD.enableSelectForUpdate();

			RptFilter filter = rptBD.newFilter();
			filter.setCodDominio(codDominio);
			filter.setIuv(iuv);
			filter.setStato(it.govpay.model.Rpt.getStatiPendenti());
			filter.setModelloPagamento(ModelloPagamento.ATTIVATO_PRESSO_PSP.getCodifica()+"");

			
			List<it.govpay.bd.model.Rpt> listaRptDaAnnullare = rptBD.findAll(filter );
			
			PagamentiPortaleBD ppbd = new PagamentiPortaleBD(rptBD);
			ppbd.setAtomica(false);

			for (it.govpay.bd.model.Rpt rpt : listaRptDaAnnullare) {
				rpt.setStato(StatoRpt.RPT_ANNULLATA);
				rpt.setDescrizioneStato("Ricevuta richiesta di un nuovo tentativo di pagamento");
				
				PagamentoPortale oldPagamentoPortale = null;
				if(rpt.getIdPagamentoPortale() != null) {
					try {
						oldPagamentoPortale = ppbd.getPagamento(rpt.getIdPagamentoPortale());
						oldPagamentoPortale.setStato(STATO.ANNULLATO);
						oldPagamentoPortale.setDescrizioneStato("Ricevuta richiesta di un nuovo tentativo di pagamento");
					}catch (NotFoundException e) {
						// donothing
					}
				}

				try {
					rptBD.updateRpt(rpt);
					if(oldPagamentoPortale != null) {
						ppbd.updatePagamento(oldPagamentoPortale, false, true); // aggiorna versamenti = false, autocommit gestito esternamente true
					}

					rptBD.commit();
					log.info("RPT [idDominio:{}][iuv:{}][ccp:{}] annullata con successo.", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp());
				} catch(ServiceException e) {
					rptBD.rollback();
					LogUtils.logError(log, "Errore durante l'annullamento della RPT [idDominio:"+rpt.getCodDominio()+"][iuv:"+rpt.getIuv()+"][ccp:"+rpt.getCcp()+"]: " +e .getMessage(), e);
					throw e;
				} 
			}

		}finally {
			if(rptBD != null) {
				rptBD.disableSelectForUpdate();
				
				// ripristino autocommit
				if(!rptBD.isAutoCommit() ) {
					rptBD.setAutoCommit(true);
				}
				
				rptBD.closeConnection();
			}
		}
	}
	
	public static it.govpay.core.exceptions.FaultBean toFaultBean(FaultBean faultBean) {
		if(faultBean == null) return null;
		
		it.govpay.core.exceptions.FaultBean toRet = new it.govpay.core.exceptions.FaultBean();
		
		toRet.setDescription(faultBean.getDescription());
		toRet.setFaultCode(faultBean.getFaultCode());
		toRet.setFaultString(faultBean.getFaultString());
		toRet.setId(faultBean.getId());
		toRet.setOriginalDescription(faultBean.getOriginalDescription());
		toRet.setOriginalFaultCode(faultBean.getOriginalFaultCode());
		toRet.setOriginalFaultString(faultBean.getOriginalFaultString());
		toRet.setSerial(faultBean.getSerial());
		return toRet;
	}
}
