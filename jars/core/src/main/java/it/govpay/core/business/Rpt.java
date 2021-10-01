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
import org.springframework.security.core.Authentication;

import gov.telematici.pagamenti.ws.rpt.FaultBean;
import gov.telematici.pagamenti.ws.rpt.NodoChiediStatoRPTRisposta;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.business.model.Risposta;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoNonValidoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.DateUtils;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.RptBuilder;
import it.govpay.core.utils.RptUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.UrlUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Anagrafica;
import it.govpay.model.Intermediario;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.Versamento.StatoVersamento;

public class Rpt {

	private static Logger log = LoggerWrapperFactory.getLogger(Rpt.class);

	public Rpt() {
	}

	public List<it.govpay.bd.model.Rpt> avviaTransazione(List<Versamento> versamenti, Authentication authentication, Canale canale, String ibanAddebito, Anagrafica versante, String autenticazione, String redirect, boolean aggiornaSeEsiste, PagamentoPortale pagamentoPortale) throws GovPayException, UtilsException {
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		try {
			appContext.getPagamentoCtx().setCarrello(true);
			String codCarrello = RptUtils.buildUUID35();
			if(pagamentoPortale != null) codCarrello = pagamentoPortale.getIdSessione();
			appContext.getPagamentoCtx().setCodCarrello(codCarrello);
			appContext.setCorrelationId(codCarrello);
			appContext.getRequest().addGenericProperty(new Property("codCarrello", appContext.getPagamentoCtx().getCodCarrello()));
			ctx.getApplicationLogger().log("pagamento.avviaTransazioneCarrelloWISP20");

			Stazione stazione = null;
			Giornale giornale = new it.govpay.core.business.Configurazione().getConfigurazione().getGiornale();

			for(Versamento versamentoModel : versamenti) {

				String codApplicazione = versamentoModel.getApplicazione(configWrapper).getCodApplicazione();
				String codVersamentoEnte = versamentoModel.getCodVersamentoEnte(); 
				ctx.getApplicationLogger().log("rpt.validazioneSemantica", codApplicazione, codVersamentoEnte);

				log.debug("Verifica autorizzazione pagamento del versamento [" + codVersamentoEnte + "] applicazione [" + codApplicazione + "]...");
				if(!versamentoModel.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
					log.debug("Non autorizzato pagamento del versamento [" + codVersamentoEnte + "] applicazione [" + codApplicazione + "]: pagamento in stato diverso da " + StatoVersamento.NON_ESEGUITO);
					throw new GovPayException(EsitoOperazione.PAG_006, codApplicazione, codVersamentoEnte, versamentoModel.getStatoVersamento().toString());
				} else {
					log.debug("Autorizzato pagamento del versamento [" + codVersamentoEnte + "] applicazione [" + codApplicazione + "]: pagamento in stato " + StatoVersamento.NON_ESEGUITO);
				}

				log.debug("Verifica scadenza del versamento [" + codVersamentoEnte + "] applicazione [" + codApplicazione + "]...");
				if(versamentoModel.getDataScadenza() != null && DateUtils.isDataDecorsa(versamentoModel.getDataScadenza())) {
					log.warn("Scadenza del versamento [" + codVersamentoEnte + "] applicazione [" + codApplicazione + "] decorsa.");
					throw new GovPayException(EsitoOperazione.PAG_007, codApplicazione, codVersamentoEnte, SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(versamentoModel.getDataScadenza()));
				} else { // versamento non scaduto, controllo data validita'
					log.debug("Verifica validita' del versamento [" + codVersamentoEnte + "] applicazione [" + codApplicazione + "]...");
					if(versamentoModel.getDataValidita() != null && DateUtils.isDataDecorsa(versamentoModel.getDataValidita())) {

						if(versamentoModel.getId() == null) {
							// Versamento fornito scaduto. Ritorno errore.
							throw new GovPayException(EsitoOperazione.PAG_012, codApplicazione, codVersamentoEnte, SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(versamentoModel.getDataValidita()));
						} else {
							// Versammento in archivio scaduto. Ne chiedo un aggiornamento.
							log.info("Validita del versamento [" + codVersamentoEnte + "] applicazione [" + codApplicazione + "] decorsa. Avvio richiesta di aggiornamento all'applicazione.");
							try {
								versamentoModel = VersamentoUtils.aggiornaVersamento(versamentoModel);
								log.info("Versamento [" + codVersamentoEnte + "] applicazione [" + codApplicazione + "] aggiornato tramite servizio di verifica.");
							} catch (VersamentoAnnullatoException e){
								log.warn("Aggiornamento del versamento [" + codVersamentoEnte + "] applicazione [" + codApplicazione + "] fallito: versamento annullato");
								throw new GovPayException(EsitoOperazione.VER_013, codApplicazione, codVersamentoEnte);
							} catch (VersamentoScadutoException e) {
								log.warn("Aggiornamento del versamento [" + codVersamentoEnte + "] applicazione [" + codApplicazione + "] fallito: versamento scaduto");
								throw new GovPayException(EsitoOperazione.VER_010, codApplicazione, codVersamentoEnte);
							} catch (VersamentoDuplicatoException e) {
								log.warn("Aggiornamento del versamento [" + codVersamentoEnte + "] applicazione [" + codApplicazione + "] fallito: versamento duplicato");
								throw new GovPayException(EsitoOperazione.VER_012, codApplicazione, codVersamentoEnte);
							} catch (VersamentoSconosciutoException e) {
								log.warn("Aggiornamento del versamento [" + codVersamentoEnte + "] applicazione [" + codApplicazione + "] fallito: versamento sconosciuto");
								throw new GovPayException(EsitoOperazione.VER_011, codApplicazione, codVersamentoEnte);
							} catch (ClientException e) {
								log.warn("Aggiornamento del versamento [" + codVersamentoEnte + "] applicazione [" + codApplicazione + "] fallito: errore di interazione con il servizio di verifica.");
								throw new GovPayException(EsitoOperazione.VER_014, codApplicazione, codVersamentoEnte, e.getMessage());
							} catch (VersamentoNonValidoException e) {
								log.warn("Aggiornamento del versamento [" + codVersamentoEnte + "] applicazione [" + codApplicazione + "] fallito: errore di validazione dei dati ricevuti dal servizio di verifica.");
								throw new GovPayException(EsitoOperazione.VER_014, codApplicazione, codVersamentoEnte, e.getMessage());
							}
						}
					} else { 
						// versamento valido passo
					}
				}

				log.debug("Scadenza del versamento [" + codVersamentoEnte + "] applicazione [" + codApplicazione + "] verificata.");

				if(stazione == null) {
					stazione = versamentoModel.getDominio(configWrapper).getStazione();
				} else {
					if(stazione.getId().compareTo(versamentoModel.getDominio(configWrapper).getStazione().getId()) != 0) {
						throw new GovPayException(EsitoOperazione.PAG_000);
					}
				}

				ctx.getApplicationLogger().log("rpt.validazioneSemanticaOk", codApplicazione, codVersamentoEnte);
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
						log.debug("IUV Proposto: " + versamento.getIuvProposto());
						TipoIUV tipoIuv = iuvBusiness.getTipoIUV(versamento.getIuvProposto());
						iuvBusiness.checkIUV(versamento.getDominio(configWrapper), versamento.getIuvProposto(), tipoIuv);
						iuv = versamento.getIuvProposto();
						ccp = IuvUtils.buildCCP();
						ctx.getApplicationLogger().log("iuv.assegnazioneIUVCustom", versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), versamento.getDominio(configWrapper).getCodDominio(), versamento.getIuvProposto(), ccp);
					} else {
						// Verifico se ha gia' uno IUV numerico assegnato. In tal caso lo riuso. 
						iuv = versamento.getIuvVersamento();
						if(iuv != null) {
							log.debug("Iuv gia' assegnato: " + iuv);
							ccp = IuvUtils.buildCCP();
							ctx.getApplicationLogger().log("iuv.assegnazioneIUVRiuso", versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), versamento.getDominio(configWrapper).getCodDominio(), iuv, ccp);
						} else {
							log.debug("Iuv non assegnato. Generazione...");
							// Non c'e' iuv assegnato. Glielo genero io.
							iuv = iuvBusiness.generaIUV(versamento.getApplicazione(configWrapper), versamento.getDominio(configWrapper), versamento.getCodVersamentoEnte(), it.govpay.model.Iuv.TipoIUV.ISO11694, rptBD);
							ccp = IuvUtils.buildCCP();
							ctx.getApplicationLogger().log("iuv.assegnazioneIUVGenerato", versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), versamento.getDominio(configWrapper).getCodDominio(), iuv, ccp);
						}
					}

					if(pagamentoPortale !=  null && pagamentoPortale.getTipo() == 1 && GovpayConfig.getInstance().isTimeoutPendentiModello1()) {
						log.debug("Blocco pagamento per il Mod1 attivo con soglia: [" + GovpayConfig.getInstance().getTimeoutPendentiModello1Mins() + " minuti]"); 
						log.debug("Controllo che non ci siano transazioni di pagamento in corso per il versamento [IdA2A:"+versamento.getApplicazione(configWrapper).getCodApplicazione()+", IdPendenza:"+versamento.getCodVersamentoEnte()+"].");

						// Controllo che non ci sia un pagamento in corso per i versamenti che sto provando ad eseguire
						RptFilter filter = rptBD.newFilter();
						filter.setStato(it.govpay.model.Rpt.stati_pendenti);
						filter.setIdVersamento(versamento.getId());
						List<it.govpay.bd.model.Rpt> rpt_pendenti = rptBD.findAll(filter);

						log.debug("Trovate ["+rpt_pendenti.size()+"] RPT pendenti per  il versamento [IdA2A:"+versamento.getApplicazione(configWrapper).getCodApplicazione()+", IdPendenza:"+versamento.getCodVersamentoEnte()+"].");

						// Per tutte quelle in corso controllo se hanno passato la soglia di timeout
						// Altrimenti lancio il fault
						Date dataSoglia = new Date(new Date().getTime() - GovpayConfig.getInstance().getTimeoutPendentiModello1Mins() * 60000);

						for(it.govpay.bd.model.Rpt rpt_pendente : rpt_pendenti) {
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
					ctx.getApplicationLogger().log("rpt.creazioneRpt", versamento.getDominio(configWrapper).getCodDominio(), iuv, ccp, rpt.getCodMsgRichiesta());
					log.info("Inserita Rpt per il versamento ("+versamento.getCodVersamentoEnte()+") dell'applicazione (" + versamento.getApplicazione(configWrapper).getCodApplicazione() + ") con dominio (" + rpt.getCodDominio() + ") iuv (" + rpt.getIuv() + ") ccp (" + rpt.getCcp() + ")");
				} 

				rptBD.commit();
			} catch (ServiceException | GovPayException | UtilsException e){
				if(rptBD != null) {
					rptBD.rollback();
				}
				throw e;
			} finally {
				if(rptBD != null) {
					rptBD.closeConnection();
				}
			}

			// Spedisco le RPT al Nodo
			// Se ho una GovPayException, non ho sicuramente spedito nulla.
			// Se ho una ClientException non so come sia andata la consegna.
			NodoClient clientInviaCarrelloRPT = null;
		    rptBD = null;
			try {
				Risposta risposta = null;
				String operationId = appContext.setupNodoClient(stazione.getCodStazione(), null, Azione.nodoInviaCarrelloRPT);
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codCarrello", appContext.getPagamentoCtx().getCodCarrello()));
				ctx.getApplicationLogger().log("rpt.invioCarrelloRpt");
				clientInviaCarrelloRPT = new it.govpay.core.utils.client.NodoClient(intermediario, operationId, giornale);
				risposta = RptUtils.inviaCarrelloRPT(clientInviaCarrelloRPT, intermediario, stazione, rpts, operationId);
				
				// ripristino la connessione
				rptBD = new RptBD(configWrapper);
				
				if(risposta.getEsito() == null || !risposta.getEsito().equals("OK")) {
					// RPT rifiutata dal Nodo
					// Aggiorno lo stato e ritorno l'errore
					try {

						for(FaultBean fb : risposta.getListaErroriRPT()) {
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
							rptBD.updateRpt(rpt.getId(), StatoRpt.RPT_RIFIUTATA_NODO, descrizione, null, null,EsitoPagamento.RIFIUTATO);
						}

					} catch (Exception e) {
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
					ctx.getApplicationLogger().log("rpt.invioKo", risposta.getLog());
					log.info("RPT rifiutata dal Nodo dei Pagamenti: " + risposta.getLog());
					if(clientInviaCarrelloRPT != null) {
						clientInviaCarrelloRPT.getEventoCtx().setSottotipoEsito(risposta.getFaultBean().getFaultCode());
						clientInviaCarrelloRPT.getEventoCtx().setEsito(Esito.KO);
						//						clientInviaCarrelloRPT.getEventoCtx().setDescrizioneEsito(risposta.toString());
					}
					throw new GovPayException(risposta.getFaultBean());
				} else {
					log.info("Rpt accettata dal Nodo dei Pagamenti");
					// RPT accettata dal Nodo
					// Aggiorno lo stato e ritorno
					if(risposta.getUrl() != null) {
						log.debug("Redirect URL: " + risposta.getUrl());
						ctx.getApplicationLogger().log("rpt.invioOk");
					} else {
						log.debug("Nessuna URL di redirect");
						ctx.getApplicationLogger().log("rpt.invioOkNoRedirect");
					}
					clientInviaCarrelloRPT.getEventoCtx().setEsito(Esito.OK);
					return this.updateStatoRpt(rpts, StatoRpt.RPT_ACCETTATA_NODO, risposta.getUrl(), pagamentoPortale, null);
				}
			} catch (ClientException e) {
				// ClientException: tento una chiedi stato RPT per recuperare lo stato effettivo.
				//   - RPT non esistente: rendo un errore NDP per RPT non inviata
				//   - RPT esistente: faccio come OK
				//   - Errore nella richiesta: rendo un errore NDP per stato sconosciuto
				if(clientInviaCarrelloRPT != null) {
					clientInviaCarrelloRPT.getEventoCtx().setSottotipoEsito(e.getResponseCode() + "");
					clientInviaCarrelloRPT.getEventoCtx().setEsito(Esito.FAIL);
					clientInviaCarrelloRPT.getEventoCtx().setDescrizioneEsito(e.getMessage());
					clientInviaCarrelloRPT.getEventoCtx().setException(e);
				}
				ctx.getApplicationLogger().log("rpt.invioFail", e.getMessage());
				log.warn("Errore nella spedizione dell'Rpt: " + e);
				NodoChiediStatoRPTRisposta risposta = null;
				log.info("Attivazione della procedura di recupero del processo di pagamento.");

				NodoClient chiediStatoRptClient = null;
				
				try {
					try {
						String operationId = appContext.setupNodoClient(stazione.getCodStazione(), rpts.get(0).getCodDominio(), Azione.nodoChiediStatoRPT);
						appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codCarrello", appContext.getPagamentoCtx().getCodCarrello()));

						chiediStatoRptClient = new it.govpay.core.utils.client.NodoClient(intermediario, operationId, giornale);
						// salvataggio id Rpt/ versamento/ pagamento
						chiediStatoRptClient.getEventoCtx().setCodDominio(rpts.get(0).getCodDominio());
						chiediStatoRptClient.getEventoCtx().setIuv(rpts.get(0).getIuv());
						chiediStatoRptClient.getEventoCtx().setCcp(rpts.get(0).getCcp());
						chiediStatoRptClient.getEventoCtx().setIdA2A(rpts.get(0).getVersamento().getApplicazione(configWrapper).getCodApplicazione());
						chiediStatoRptClient.getEventoCtx().setIdPendenza(rpts.get(0).getVersamento().getCodVersamentoEnte());
						if(rpts.get(0).getPagamentoPortale() != null)
							chiediStatoRptClient.getEventoCtx().setIdPagamento(rpts.get(0).getPagamentoPortale().getIdSessione());

						risposta = RptUtils.chiediStatoRPT(chiediStatoRptClient, intermediario, stazione, rpts.get(0), operationId);
						chiediStatoRptClient.getEventoCtx().setEsito(Esito.OK);
					} catch (ClientException ee) {
						if(chiediStatoRptClient != null) {
							chiediStatoRptClient.getEventoCtx().setEsito(Esito.FAIL);
							chiediStatoRptClient.getEventoCtx().setDescrizioneEsito(ee.getMessage());
							chiediStatoRptClient.getEventoCtx().setException(ee);
						}
						ctx.getApplicationLogger().log("rpt.invioRecoveryStatoRPTFail", ee.getMessage());
						log.warn("Errore nella richiesta di stato RPT: " + ee.getMessage() + ". Recupero stato fallito.");
						this.updateStatoRpt(rpts, StatoRpt.RPT_ERRORE_INVIO_A_NODO, "Impossibile comunicare con il Nodo dei Pagamenti SPC: " + e.getMessage(), pagamentoPortale, null);
						throw new GovPayException(EsitoOperazione.NDP_000, e, "Errore nella consegna della richiesta di pagamento al Nodo dei Pagamenti");
					} finally {
						
					}
					if(risposta.getEsito() == null) {
						if(chiediStatoRptClient != null) {
							chiediStatoRptClient.getEventoCtx().setSottotipoEsito(risposta.getFault().getFaultCode());
							chiediStatoRptClient.getEventoCtx().setEsito(Esito.FAIL);
							chiediStatoRptClient.getEventoCtx().setDescrizioneEsito(risposta.getFault().getFaultString());
						}
						// anche la chiedi stato e' fallita
						ctx.getApplicationLogger().log("rpt.invioRecoveryStatoRPTKo", risposta.getFault().getFaultCode(), risposta.getFault().getFaultString(), risposta.getFault().getDescription());
						log.warn("Recupero sessione fallito. Errore nella richiesta di stato RPT: " + risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString());
						throw new GovPayException(EsitoOperazione.NDP_000, e, "Errore nella consegna della richiesta di pagamento al Nodo dei Pagamenti");
					} else {
						StatoRpt statoRpt = StatoRpt.toEnum(risposta.getEsito().getStato());
						log.info("Acquisito stato RPT dal nodo: " + risposta.getEsito().getStato());


						if(statoRpt.equals(StatoRpt.RT_ACCETTATA_PA) || statoRpt.equals(StatoRpt.RT_RIFIUTATA_PA)) {
							ctx.getApplicationLogger().log("rpt.invioRecoveryStatoRPTcompletato");
							log.info("Processo di pagamento gia' completato.");
							if(chiediStatoRptClient != null) {
								chiediStatoRptClient.getEventoCtx().setSottotipoEsito("PAA_NODO_INDISPONIBILE"); 
								chiediStatoRptClient.getEventoCtx().setEsito(Esito.KO);
								chiediStatoRptClient.getEventoCtx().setDescrizioneEsito("Richiesta di pagamento gia' gestita dal Nodo dei Pagamenti");
							}
							// Ho gia' trattato anche la RT. Non faccio nulla.
							throw new GovPayException(EsitoOperazione.NDP_000, e, "Richiesta di pagamento gia' gestita dal Nodo dei Pagamenti");
						}


						// Ho lo stato aggiornato. Aggiorno il db
						if(risposta.getEsito().getUrl() != null) {
							log.info("Processo di pagamento recuperato. Url di redirect: " + risposta.getEsito().getUrl());
							appContext.getResponse().addGenericProperty(new Property("redirectUrl", risposta.getEsito().getUrl()));
							ctx.getApplicationLogger().log("rpt.invioRecoveryStatoRPTOk");
						} else {
							log.info("Processo di pagamento recuperato. Nessuna URL di redirect.");
							ctx.getApplicationLogger().log("rpt.invioRecoveryStatoRPTOk");
						}
						return this.updateStatoRpt(rpts, statoRpt, risposta.getEsito().getUrl(), pagamentoPortale, e);
					}
				} finally {
					if(chiediStatoRptClient != null && chiediStatoRptClient.getEventoCtx().isRegistraEvento()) {
						EventiBD eventiBD = new EventiBD(configWrapper);
						eventiBD.insertEvento(chiediStatoRptClient.getEventoCtx().toEventoDTO());
					}
				}
			}  finally {
								
				if(clientInviaCarrelloRPT != null && clientInviaCarrelloRPT.getEventoCtx().isRegistraEvento()) {
					EventiBD eventiBD = new EventiBD(configWrapper);
					for(it.govpay.bd.model.Rpt rpt : rpts) {
						// salvataggio id Rpt/ versamento/ pagamento
						clientInviaCarrelloRPT.getEventoCtx().setCodDominio(rpt.getCodDominio());
						clientInviaCarrelloRPT.getEventoCtx().setIuv(rpt.getIuv());
						clientInviaCarrelloRPT.getEventoCtx().setCcp(rpt.getCcp());
						clientInviaCarrelloRPT.getEventoCtx().setIdA2A(rpt.getVersamento().getApplicazione(configWrapper).getCodApplicazione());
						clientInviaCarrelloRPT.getEventoCtx().setIdPendenza(rpt.getVersamento().getCodVersamentoEnte());
						if(rpt.getPagamentoPortale() != null)
							clientInviaCarrelloRPT.getEventoCtx().setIdPagamento(rpt.getPagamentoPortale().getIdSessione());

						RptUtils.popolaEventoCooperazione(clientInviaCarrelloRPT, rpt, intermediario, stazione); 

						if(rpt.getFaultCode() != null)
							clientInviaCarrelloRPT.getEventoCtx().setSottotipoEsito(rpt.getFaultCode());
						if(!clientInviaCarrelloRPT.getEventoCtx().getEsito().equals(Esito.OK) && clientInviaCarrelloRPT.getEventoCtx().getDescrizioneEsito() == null) {
							clientInviaCarrelloRPT.getEventoCtx().setDescrizioneEsito(rpt.getDescrizioneStato());
						}

						eventiBD.insertEvento(clientInviaCarrelloRPT.getEventoCtx().toEventoDTO());
					}
				}
			}
		} catch (ServiceException e) {
			throw new GovPayException(e);
		} 
	}

	private List<it.govpay.bd.model.Rpt> updateStatoRpt(List<it.govpay.bd.model.Rpt> rpts, StatoRpt statoRpt, String url, PagamentoPortale pagamentoPortale, Exception e) throws ServiceException, GovPayException, UtilsException { 
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
					rptBD.closeConnection();
				}
			}
		}

		switch (statoRpt) {
		case RPT_RIFIUTATA_NODO:
		case RPT_ERRORE_INVIO_A_NODO:
		case RPT_ERRORE_INVIO_A_PSP:
		case RPT_RIFIUTATA_PSP:
			// Casi di rifiuto. Rendo l'errore
			if(e!= null)
				throw new GovPayException(EsitoOperazione.NDP_000, e);
			else 
				throw new GovPayException(EsitoOperazione.NDP_000);
		default:

			String codSessione = rpts.get(0).getCodSessione();

			if(codSessione != null) {
				appContext.getResponse().addGenericProperty(new Property("codPspSession", codSessione));
			}

			if(codSessione != null) {
				ctx.getApplicationLogger().log("pagamento.invioCarrelloRpt");
			} else {
				ctx.getApplicationLogger().log("pagamento.invioCarrelloRptNoRedirect");
			}

			log.info("RPT inviata correttamente al nodo");
			return rpts;
		}
		
	}
}
