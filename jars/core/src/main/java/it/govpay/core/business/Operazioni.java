/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2018 Link.it srl (http://www.link.it).
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

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.sonde.Sonda;
import org.openspcoop2.utils.sonde.SondaException;
import org.openspcoop2.utils.sonde.SondaFactory;
import org.openspcoop2.utils.sonde.impl.SondaBatch;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.BatchBD;
import it.govpay.bd.configurazione.model.AppIOBatch;
import it.govpay.bd.configurazione.model.MailBatch;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.NotificaAppIo;
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.model.TracciatoNotificaPagamenti;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.core.business.Rendicontazioni.DownloadRendicontazioniResponse;
import it.govpay.core.dao.pagamenti.dto.ElaboraTracciatoDTO;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.thread.InviaNotificaAppIoThread;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.SpedizioneTracciatoNotificaPagamentiThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Batch;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;

public class Operazioni{

	private static Logger log = LoggerWrapperFactory.getLogger(Operazioni.class);
	public static final String RND = "update-rnd";
	public static final String PSP = "update-psp";
	public static final String PND = "update-pnd";
	public static final String NTFY = "update-ntfy";
	public static final String NTFY_APP_IO = "update-ntfy-appio";
	public static final String CHECK_NTFY = "check-ntfy";
	public static final String CHECK_NTFY_APP_IO = "check-ntfy-appio";
	public static final String BATCH_TRACCIATI = "caricamento-tracciati";
	public static final String CHECK_TRACCIATI = "check-tracciati";
	public static final String BATCH_GENERAZIONE_AVVISI = "generazione-avvisi";
	public static final String CHECK_PROMEMORIA = "check-promemoria";
	public static final String BATCH_SPEDIZIONE_PROMEMORIA = "spedizione-promemoria";
	public static final String CHECK_RESET_CACHE = "check-reset-cache";
	public static final String BATCH_RESET_CACHE = "reset-cache";
	public static final String CACHE_ANAGRAFICA_GOVPAY = "cache-anagrafica";
	public static final String BATCH_GESTIONE_PROMEMORIA = "gestione-promemoria";
	public static final String CHECK_GESTIONE_PROMEMORIA = "check-gestione-promemoria";
	
	public static final String BATCH_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI = "elaborazione-tracciati-notifica-pagamenti";
	public static final String CHECK_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI = "check-elab-tracciati-notifica-pagamenti";
	
	public static final String BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI = "spedizione-tracciati-notifica-pagamenti";
	public static final String CHECK_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI = "check-spedizione-tracciati-notifica-pagamenti";

	private static boolean eseguiGestionePromemoria;
	private static boolean eseguiInvioPromemoria;
	private static boolean eseguiInvioNotifiche;
	private static boolean eseguiInvioNotificheAppIO;
	private static boolean eseguiGenerazioneAvvisi;
	private static boolean eseguiElaborazioneTracciati = true;
	
	private static boolean eseguiElaborazioneTracciatiNotificaPagamenti;
	private static boolean eseguiInvioTracciatiNotificaPagamenti;

	public static synchronized void setEseguiGestionePromemoria() {
		eseguiGestionePromemoria = true;
	}

	public static synchronized void resetEseguiGestionePromemoria() {
		eseguiGestionePromemoria = false;
	}

	public static synchronized boolean getEseguiGestionePromemoria() {
		return eseguiGestionePromemoria;
	}

	public static synchronized void setEseguiInvioPromemoria() {
		eseguiInvioPromemoria = true;
	}

	public static synchronized void resetEseguiInvioPromemoria() {
		eseguiInvioPromemoria = false;
	}

	public static synchronized boolean getEseguiInvioPromemoria() {
		return eseguiInvioPromemoria;
	}

	public static synchronized void setEseguiInvioNotifiche() {
		eseguiInvioNotifiche = true;
	}

	public static synchronized void resetEseguiInvioNotifiche() {
		eseguiInvioNotifiche = false;
	}

	public static synchronized boolean getEseguiInvioNotifiche() {
		return eseguiInvioNotifiche;
	}

	public static synchronized void setEseguiInvioNotificheAppIO() {
		eseguiInvioNotificheAppIO = true;
	}

	public static synchronized void resetEseguiInvioNotificheAppIO() {
		eseguiInvioNotificheAppIO = false;
	}

	public static synchronized boolean getEseguiInvioNotificheAppIO() {
		return eseguiInvioNotificheAppIO;
	}

	public static synchronized void setEseguiGenerazioneAvvisi() {
		eseguiGenerazioneAvvisi = true;
	}

	public static synchronized void resetEseguiGenerazioneAvvisi() {
		eseguiGenerazioneAvvisi = false;
	}

	public static synchronized boolean getEseguiGenerazioneAvvisi() {
		return eseguiGenerazioneAvvisi;
	}

	public static synchronized void setEseguiElaborazioneTracciati() {
		eseguiElaborazioneTracciati = true;
	}

	public static synchronized void resetEseguiElaborazioneTracciati() {
		eseguiElaborazioneTracciati = false;
	}

	public static synchronized boolean getEseguiElaborazioneTracciati() {
		return eseguiElaborazioneTracciati;
	}
	
	public static synchronized void setEseguiElaborazioneTracciatiNotificaPagamenti() {
		eseguiElaborazioneTracciatiNotificaPagamenti = true;
	}

	public static synchronized void resetEseguiElaborazioneTracciatiNotificaPagamenti() {
		eseguiElaborazioneTracciatiNotificaPagamenti = false;
	}

	public static synchronized boolean getEseguiElaborazioneTracciatiNotificaPagamenti() {
		return eseguiElaborazioneTracciatiNotificaPagamenti;
	}
	
	public static synchronized void setEseguiInvioTracciatiNotificaPagamenti() {
		eseguiInvioTracciatiNotificaPagamenti = true;
	}

	public static synchronized void resetEseguiInvioTracciatiNotificaPagamenti() {
		eseguiInvioTracciatiNotificaPagamenti = false;
	}

	public static synchronized boolean getEseguiInvioTracciatiNotificaPagamenti() {
		return eseguiInvioTracciatiNotificaPagamenti;
	}

	public static String acquisizioneRendicontazioni(IContext ctx){
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		try {
			if(BatchManager.startEsecuzione(configWrapper, RND)) {
				DownloadRendicontazioniResponse downloadRendicontazioni = new Rendicontazioni().downloadRendicontazioni(ctx, false);
				aggiornaSondaOK(configWrapper, RND);
				return downloadRendicontazioni.getDescrizioneEsito();
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Acquisizione rendicontazioni fallita", e);
			aggiornaSondaKO(configWrapper, RND, e);
			return "Acquisizione fallita#" + e;
		} finally {
			BatchManager.stopEsecuzione(configWrapper, RND);
		}
	}

	public static String recuperoRptPendenti(IContext ctx){
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		try {
			if(BatchManager.startEsecuzione(configWrapper, PND)) {
				String verificaTransazioniPendenti = new Pagamento().verificaTransazioniPendenti();
				aggiornaSondaOK(configWrapper, PND);
				return verificaTransazioniPendenti;
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Acquisizione Rpt pendenti fallita", e);
			aggiornaSondaKO(configWrapper, PND, e);
			return "Acquisizione fallita#" + e;
		} finally {
			BatchManager.stopEsecuzione(configWrapper, PND);
		}
	}

	public static String spedizioneNotifiche(IContext ctx){
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		try {
			if(BatchManager.startEsecuzione(configWrapper, NTFY)) {
				log.debug("Spedizione notifiche non consegnate");

				List<String> applicazioni = AnagraficaManager.getListaCodApplicazioni(configWrapper);
				
				it.govpay.core.business.Notifica notificheBD = new it.govpay.core.business.Notifica();

				int threadNotificaPoolSize = GovpayConfig.getInstance().getDimensionePoolNotifica();

				for (String codApplicazione : applicazioni) {
					it.govpay.bd.model.Applicazione applicazione = null;
					try {
						applicazione = AnagraficaManager.getApplicazione(configWrapper, codApplicazione);
					}catch(NotFoundException e) {
						log.debug("Applicazione ["+codApplicazione+"] non trovata, passo alla prossima applicazione.");
						continue;
					}

					// effettuo la spedizione solo per le applicazioni che hanno il connettore configurato.
					if(applicazione.getConnettoreIntegrazione() != null) {
						int offset = 0;
						int limit = (2 * threadNotificaPoolSize);
						List<InviaNotificaThread> threads = new ArrayList<>();
						List<Notifica> notifiche  = notificheBD.findNotificheDaSpedire(offset,limit,codApplicazione);

						log.debug("Trovate ["+notifiche.size()+"] notifiche da spedire per l'applicazione ["+codApplicazione+"]");

						if(notifiche.size() > 0) {
							for(Notifica notifica: notifiche) {
								InviaNotificaThread sender = new InviaNotificaThread(notifica, ctx);
								ThreadExecutorManager.getClientPoolExecutorNotifica().execute(sender);
								threads.add(sender);
							}

							log.info("Processi di spedizione notifiche per l'applicazione ["+codApplicazione+"] avviati.");
							aggiornaSondaOK(configWrapper, NTFY);

							// Aspetto che abbiano finito tutti
							int numeroErrori = 0;
							while(true){
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {

								}
								boolean completed = true;
								for(InviaNotificaThread sender : threads) {
									if(!sender.isCompleted()) 
										completed = false;
								}

								if(completed) { 
									for(InviaNotificaThread sender : threads) {
										if(sender.isErrore()) 
											numeroErrori ++;
									}
									int numOk = threads.size() - numeroErrori;
									log.debug("Completata Esecuzione dei ["+threads.size()+"] Threads, OK ["+numOk+"], Errore ["+numeroErrori+"]");
									break; // esco
								}
							}

							// Hanno finito tutti, aggiorno stato esecuzione
							BatchManager.aggiornaEsecuzione(configWrapper, NTFY);
							log.info("Spedizione notifiche completata.");
						}
					}   else {
						log.debug("Connettore non configurato per l'applicazione ["+codApplicazione+"], non ricerco notifiche da spedire.");
					}
				}
				aggiornaSondaOK(configWrapper, NTFY);
				return "Spedizione notifiche completata.";
			} else {
				log.debug("Operazione in corso su altro nodo. Richiesta interrotta.");
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile avviare la spedizione delle notifiche", e);
			aggiornaSondaKO(configWrapper, NTFY, e); 
			return "Non è stato possibile avviare la spedizione delle notifiche: " + e;
		} finally {
			BatchManager.stopEsecuzione(configWrapper, NTFY);
		}
	}

	public static String spedizioneNotificheAppIO(IContext ctx){
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		
		try {
			it.govpay.bd.model.Configurazione configurazione = new it.govpay.core.business.Configurazione().getConfigurazione();
			if(!configurazione.getBatchSpedizioneAppIo().isAbilitato()) {
				return "Spedizione notifiche AppIO disabilitata.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile avviare la spedizione delle notifiche AppIO", e);
			return "Non è stato possibile avviare la spedizione delle notifiche AppIO: " + e;
		}
		
		try {
			if(BatchManager.startEsecuzione(configWrapper, NTFY_APP_IO)) {
				log.debug("Spedizione notifiche AppIO non consegnate");

				it.govpay.core.business.NotificaAppIo notificheBD = new it.govpay.core.business.NotificaAppIo();

				int threadNotificaPoolSize = GovpayConfig.getInstance().getDimensionePoolNotificaAppIO();
				int offset = 0;
				int limit = (2 * threadNotificaPoolSize);
				List<InviaNotificaAppIoThread> threads = new ArrayList<>();
				List<NotificaAppIo> notifiche  = notificheBD.findNotificheDaSpedire(offset,limit);

				log.debug("Trovate ["+notifiche.size()+"] notifiche AppIO da spedire");

				if(notifiche.size() > 0) {
					for(NotificaAppIo notifica: notifiche) {
						InviaNotificaAppIoThread sender = new InviaNotificaAppIoThread(notifica, ctx);
						ThreadExecutorManager.getClientPoolExecutorNotificaAppIo().execute(sender);
						threads.add(sender);
					}

					log.debug("Processi di spedizione notifiche AppIO avviati.");
					aggiornaSondaOK(configWrapper, NTFY_APP_IO);

					// Aspetto che abbiano finito tutti
					int numeroErrori = 0;
					while(true){
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {

						}
						boolean completed = true;
						for(InviaNotificaAppIoThread sender : threads) {
							if(!sender.isCompleted()) 
								completed = false;
						}

						if(completed) { 
							for(InviaNotificaAppIoThread sender : threads) {
								if(sender.isErrore()) 
									numeroErrori ++;
							}
							int numOk = threads.size() - numeroErrori;
							log.debug("Completata Esecuzione dei ["+threads.size()+"] Threads, OK ["+numOk+"], Errore ["+numeroErrori+"]");
							break; // esco
						}
					}
					log.info("Spedizione notifiche AppIO completata.");
					//Hanno finito tutti, aggiorno stato esecuzione
					BatchManager.aggiornaEsecuzione(configWrapper, NTFY_APP_IO);
				}
				aggiornaSondaOK(configWrapper, NTFY_APP_IO);
				return "Spedizione notifiche AppIO completata.";
			} else {
				log.debug("Operazione in corso su altro nodo. Richiesta interrotta.");
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile avviare la spedizione delle notifiche AppIO", e);
			aggiornaSondaKO(configWrapper, NTFY_APP_IO, e); 
			return "Non è stato possibile avviare la spedizione delle notifiche AppIO: " + e;
		} finally {
			BatchManager.stopEsecuzione(configWrapper, NTFY_APP_IO);
		}
	}

	public static String resetCacheAnagrafica(IContext ctx){
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		BatchBD batchBD = null;
		try {
			batchBD = new BatchBD(configWrapper);
			Batch batch = batchBD.get(CACHE_ANAGRAFICA_GOVPAY);
			batch.setAggiornamento(new Date());
			batchBD.update(batch);
			AnagraficaManager.cleanCache();
			log.info("Aggiornamento della data di reset della cache anagrafica del sistema completato con successo.");	
			return "Aggiornamento della data di reset della cache anagrafica del sistema completato con successo.";
		} catch (Exception e) {
			log.error("Aggiornamento della data di reset cache anagrafica del sistema fallita", e);
			return "Aggiornamento della data di reset cache del sistema fallita: " + e;
		} finally {
			if(batchBD != null) batchBD.closeConnection();
		}
	}

	public static String resetCacheAnagraficaCheck(IContext ctx){
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		BatchBD batchBD = null;
		try {
			log.debug("Check reset della cache anagrafica locale in corso ...");	

			batchBD = new BatchBD(configWrapper);
			Batch batch = batchBD.get(CACHE_ANAGRAFICA_GOVPAY);
			Date aggiornamento = batch.getAggiornamento();

			Date dataResetAttuale = AnagraficaManager.getDataReset();
			if(aggiornamento != null && dataResetAttuale.getTime() < aggiornamento.getTime()) {
				String clusterId = GovpayConfig.getInstance().getClusterId();
				if(StringUtils.isEmpty(clusterId))
					clusterId = "1";

				log.info("Nodo ["+clusterId+"]: Reset della cache anagrafica locale in corso...");	
				AnagraficaManager.cleanCache();
				log.info("Nodo ["+clusterId+"]: Reset della cache anagrafica locale completato.");
			}

			log.debug("Check reset della cache anagrafica locale completato con successo.");	
			return "Check reset della cache anagrafica locale completato con successo.";
		} catch (Exception e) {
			log.error("Check reset della cache anagrafica locale fallito", e);
			return "Check reset della cache anagrafica locale fallito: " + e;
		} finally {
			if(batchBD != null) batchBD.closeConnection();
		}
	}

	private static void aggiornaSondaOK(BDConfigWrapper configWrapper, String nome) {
		BasicBD bd = null;

		try {
			// costruttore
			bd = BasicBD.newInstance(configWrapper.getTransactionID());

			// apro la connessione
			bd.setupConnection(configWrapper.getTransactionID());

			// setto enableselectforupdate
			bd.enableSelectForUpdate();

			// prendo la connessione
			Connection con = bd.getConnection();

			Sonda sonda = SondaFactory.get(nome, con, bd.getJdbcProperties().getDatabase());
			if(sonda == null) throw new SondaException("Sonda ["+nome+"] non trovata");
			//			Properties properties = new Properties();
			//			((SondaBatch)sonda).aggiornaStatoSonda(true, properties, new Date(), "Ok", con, bd.getJdbcProperties().getDatabase());
			((SondaBatch)sonda).aggiornaStatoSonda(true,  new Date(), "Ok", con, bd.getJdbcProperties().getDatabase());
		} catch (Throwable t) {
			log.warn("Errore nell'aggiornamento della sonda OK: "+ t.getMessage());
		}
		finally {
			if(bd != null) {
				try {
					bd.disableSelectForUpdate();
				} catch (ServiceException e) {
					log.error("Errore " +e.getMessage() , e);
				}

				bd.closeConnection();
			}
		}
	}

	private static void aggiornaSondaKO(BDConfigWrapper configWrapper, String nome, Exception e) {
		BasicBD bd = null;

		try {
			// costruttore
			bd = BasicBD.newInstance(configWrapper.getTransactionID());

			// apro la connessione
			bd.setupConnection(configWrapper.getTransactionID());

			// setto enableselectforupdate
			bd.enableSelectForUpdate();

			// prendo la connessione
			Connection con = bd.getConnection();
			
			Sonda sonda = SondaFactory.get(nome, con, bd.getJdbcProperties().getDatabase());
			if(sonda == null) throw new SondaException("Sonda ["+nome+"] non trovata");
			//			Properties properties = new Properties();
			//			((SondaBatch)sonda).aggiornaStatoSonda(false, properties, new Date(), "Il batch e' stato interrotto con errore: " + e.getMessage(), con, bd.getJdbcProperties().getDatabase());
			((SondaBatch)sonda).aggiornaStatoSonda(false, new Date(), "Il batch e' stato interrotto con errore: " + e.getMessage(), con, bd.getJdbcProperties().getDatabase());
		} catch (Throwable t) {
			log.warn("Errore nell'aggiornamento della sonda KO: "+ t.getMessage());
		} finally {
			if(bd != null) {
				try {
					bd.disableSelectForUpdate();
				} catch (ServiceException e1) {
					log.error("Errore " +e1.getMessage() , e1);
				}

				bd.closeConnection();
			}
		}
	}

	public static String elaborazioneTracciatiPendenze(IContext ctx){
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		try {

			if(BatchManager.startEsecuzione(configWrapper, BATCH_TRACCIATI)) {
				log.trace("Elaborazione tracciati");

				TracciatiBD tracciatiBD = new TracciatiBD(configWrapper);
				TracciatoFilter filter = tracciatiBD.newFilter();
				filter.setTipo(Arrays.asList(TIPO_TRACCIATO.PENDENZA));
				filter.setStato(STATO_ELABORAZIONE.ELABORAZIONE);
				filter.setLimit(25);
				filter.setIncludiRawRichiesta(true);
				//				filter.setDataUltimoAggiornamentoMax(new Date());
				List<Tracciato> tracciati = tracciatiBD.findAll(filter);
				Tracciati tracciatiBusiness = new Tracciati();

				while(!tracciati.isEmpty()) {
					log.info("Trovati ["+tracciati.size()+"] tracciati da elaborare...");

					for(Tracciato tracciato: tracciati) {
						log.info("Avvio elaborazione tracciato "  + tracciato.getId());
						ElaboraTracciatoDTO elaboraTracciatoDTO = new ElaboraTracciatoDTO();
						elaboraTracciatoDTO.setTracciato(tracciato);
						tracciatiBusiness.elaboraTracciatoPendenze(elaboraTracciatoDTO, ctx);
						log.info("Elaborazione tracciato "  + tracciato.getId() + " completata");
					}

					//					filter.setDataUltimoAggiornamentoMax(new Date());
					tracciati = tracciatiBD.findAll(filter);
				}

				aggiornaSondaOK(configWrapper, BATCH_TRACCIATI);
				BatchManager.stopEsecuzione(configWrapper, BATCH_TRACCIATI);
				log.info("Elaborazione tracciati terminata.");
				return "Elaborazione tracciati terminata.";
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			try {
				aggiornaSondaKO(configWrapper, BATCH_TRACCIATI, e);
			} catch (Throwable e1) {
				log.error("Aggiornamento sonda fallito: " + e1.getMessage(),e1);
			}
			log.error("Non è stato possibile eseguire l'elaborazione dei tracciati", e);
			return "Non è stato possibile eseguire l'elaborazione dei tracciati: " + e;
		} finally {
		}
	}

	public static String spedizionePromemoria(IContext ctx){
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		try {
			it.govpay.bd.model.Configurazione configurazione = new it.govpay.core.business.Configurazione().getConfigurazione();

			MailBatch batchSpedizioneEmail = configurazione.getBatchSpedizioneEmail();
			if(!batchSpedizioneEmail.isAbilitato()) {
				return "Spedizione promemoria disabilitata.";
			}

			if(BatchManager.startEsecuzione(configWrapper, BATCH_SPEDIZIONE_PROMEMORIA)) {
				int limit = 100;
				log.trace("Spedizione primi ["+limit+"] promemoria non consegnati");
				Promemoria promemoriaBD = new Promemoria(); 
				List<it.govpay.bd.model.Promemoria> promemorias = promemoriaBD.findPromemoriaDaSpedire(0, limit);

				if(promemorias.size() == 0) {
					aggiornaSondaOK(configWrapper, BATCH_SPEDIZIONE_PROMEMORIA);
					BatchManager.stopEsecuzione(configWrapper, BATCH_SPEDIZIONE_PROMEMORIA);
					log.debug("Nessun promemoria da inviare.");
					return "Nessun promemoria da inviare.";
				}

				log.info("Trovati ["+promemorias.size()+"] promemoria da spedire");

				for(it.govpay.bd.model.Promemoria promemoria: promemorias) {
					promemoriaBD.invioPromemoria(promemoria);
				}
				aggiornaSondaOK(configWrapper, BATCH_SPEDIZIONE_PROMEMORIA);
				log.info("Spedizione promemoria completata.");
				return "Spedizione promemoria completata.";
			} else {
				log.info("Operazione in corso su altro nodo. Richiesta interrotta.");
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile avviare la spedizione dei promemoria", e);
			try {
				aggiornaSondaKO(configWrapper, BATCH_SPEDIZIONE_PROMEMORIA, e); 
			} catch (Throwable e1) {
				log.error("Aggiornamento sonda fallito: " + e1.getMessage(),e1);
			}
			return "Non è stato possibile avviare la spedizione dei promemoria: " + e;
		} finally {
		}
	}

	public static String gestionePromemoria(IContext ctx){
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		try {
			it.govpay.bd.model.Configurazione configurazione = new it.govpay.core.business.Configurazione().getConfigurazione();

			MailBatch batchSpedizioneEmail = configurazione.getBatchSpedizioneEmail();
			AppIOBatch batchSpedizioneAppIO = configurazione.getBatchSpedizioneAppIo();

			if(!batchSpedizioneEmail.isAbilitato() && !batchSpedizioneAppIO.isAbilitato()) {
				return "Spedizione promemoria Email e AppIO disabilitata.";
			}

			if(BatchManager.startEsecuzione(configWrapper, BATCH_GESTIONE_PROMEMORIA)) {
				int limit = 100;

				log.trace("Elaborazione primi ["+limit+"] versamenti con promemoria avviso non consegnati");
				VersamentiBD versamentiBD = new VersamentiBD(configWrapper);
				it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento();
				List<Versamento> listaPromemoriaAvviso = versamentiBD.findVersamentiConAvvisoDiPagamentoDaSpedire(0, limit);

				if(listaPromemoriaAvviso.size() == 0) {
					log.debug("Nessun promemoria avviso da inviare, controllo presenza promemoria scadenza.");
				} else {
					// elaborazione avvisi...
					log.info("Trovati ["+listaPromemoriaAvviso.size()+"] promemoria avviso da spedire");
					for (Versamento versamento : listaPromemoriaAvviso) {
						versamentoBusiness.inserisciPromemoriaAvviso(versamento);				
					}
				}

				aggiornaSondaOK(configWrapper, BATCH_GESTIONE_PROMEMORIA);

				log.trace("Elaborazione primi ["+limit+"] versamenti con promemoria scadenza via mail non consegnati");
				List<Versamento> listaPromemoriaScadenzaMail = versamentiBD.findVersamentiConAvvisoDiScadenzaDaSpedireViaMail(0, limit);

				if(listaPromemoriaScadenzaMail.size() == 0) {
					log.debug("Nessun promemoria scadenza da inviare via mail.");
				} else { 
					log.info("Trovati ["+listaPromemoriaScadenzaMail.size()+"] promemoria scadenza da spedire via mail");
					for (Versamento versamento : listaPromemoriaScadenzaMail) {
						versamentoBusiness.inserisciPromemoriaScadenzaMail(versamento);
					}
				}

				aggiornaSondaOK(configWrapper, BATCH_GESTIONE_PROMEMORIA);

				log.trace("Elaborazione primi ["+limit+"] versamenti con promemoria scadenza via appIO non consegnati");
				List<Versamento> listaPromemoriaScadenzaAppIO = versamentiBD.findVersamentiConAvvisoDiScadenzaDaSpedireViaAppIO(0, limit);

				if(listaPromemoriaScadenzaAppIO.size() == 0) {
					log.debug("Nessun promemoria scadenza da inviare via appIO.");
				} else {
					log.info("Trovati ["+listaPromemoriaScadenzaAppIO.size()+"] promemoria scadenza da spedire");
					for (Versamento versamento : listaPromemoriaScadenzaAppIO) {
						versamentoBusiness.inserisciPromemoriaScadenzaAppIO(versamento);
					}
				}

				aggiornaSondaOK(configWrapper, BATCH_GESTIONE_PROMEMORIA);
				BatchManager.stopEsecuzione(configWrapper, BATCH_GESTIONE_PROMEMORIA);

				Operazioni.setEseguiInvioPromemoria();
				Operazioni.setEseguiInvioNotificheAppIO();

				log.info("Gestione promemoria completata.");
				return "Gestione promemoria completata.";
			} else {
				log.info("Operazione in corso su altro nodo. Richiesta interrotta.");
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile avviare la gestione dei promemoria", e);
			try {
				aggiornaSondaKO(configWrapper, BATCH_GESTIONE_PROMEMORIA, e); 
			} catch (Throwable e1) {
				log.error("Aggiornamento sonda fallito: " + e1.getMessage(),e1);
			}
			return "Non è stato possibile avviare la gestione dei promemoria: " + e;
		} finally {
		}
	}
	
	public static String elaborazioneTracciatiNotificaPagamenti(IContext ctx){
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		try {
			if(BatchManager.startEsecuzione(configWrapper, BATCH_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI)) {
				// ricerca domini con connettore mypivot abilitato
				List<String> domini = AnagraficaManager.getListaCodDomini(configWrapper);
				
				for (String codDominio : domini) {
					it.govpay.bd.model.Dominio dominio = null;
					try {
						dominio = AnagraficaManager.getDominio(configWrapper, codDominio);
					}catch(NotFoundException e) {
						log.debug("Dominio ["+dominio+"] non trovato, passo alla prossimo.");
						continue;
					}

					if(dominio.getConnettoreMyPivot() != null && dominio.getConnettoreMyPivot().isAbilitato()) {
						log.debug("Elaborazione Tracciato MyPivot per il Dominio ["+codDominio+"]...");
						TracciatiNotificaPagamenti tracciatiMyPivot = new TracciatiNotificaPagamenti(it.govpay.model.TracciatoNotificaPagamenti.TIPO_TRACCIATO.MYPIVOT);
						tracciatiMyPivot.elaboraTracciatoNotificaPagamenti(dominio, dominio.getConnettoreMyPivot(), ctx);
						log.debug("Elaborazione Tracciato MyPivot per il Dominio ["+codDominio+"] completata.");
					} else {
						log.trace("Connettore MyPivot non configurato per il Dominio ["+codDominio+"], non ricerco tracciati da elaborare.");
					}
					
					if(dominio.getConnettoreSecim() != null && dominio.getConnettoreSecim().isAbilitato()) {
						log.debug("Elaborazione Tracciato Secim per il Dominio ["+codDominio+"]...");
						TracciatiNotificaPagamenti tracciatiSecim = new TracciatiNotificaPagamenti(it.govpay.model.TracciatoNotificaPagamenti.TIPO_TRACCIATO.SECIM);
						tracciatiSecim.elaboraTracciatoNotificaPagamenti(dominio, dominio.getConnettoreSecim(), ctx);
						log.debug("Elaborazione Tracciato Secim per il Dominio ["+codDominio+"] completata.");
					} else {
						log.trace("Connettore Secim non configurato per il Dominio ["+codDominio+"], non ricerco tracciati da elaborare.");
					}
				}
				
				aggiornaSondaOK(configWrapper, BATCH_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI);
				BatchManager.stopEsecuzione(configWrapper, BATCH_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI);
			
				log.info("Elaborazione tracciati notifica pagamenti terminata.");
				return "Elaborazione tracciati notifica pagamenti terminata.";
			} else {
				log.info("Operazione in corso su altro nodo. Richiesta interrotta.");
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile avviare l'elaborazione dei tracciati notifica pagamenti", e);
			try {
				aggiornaSondaKO(configWrapper, BATCH_ELABORAZIONE_TRACCIATI_NOTIFICA_PAGAMENTI, e); 
			} catch (Throwable e1) {
				log.error("Aggiornamento sonda fallito: " + e1.getMessage(),e1);
			}
			return "Non è stato possibile avviare l'elaborazione dei tracciati notifica pagamenti: " + e;
		} finally {
		}
	}
	
	public static String spedizioneTracciatiNotificaPagamenti(IContext ctx){
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		try {
			if(BatchManager.startEsecuzione(configWrapper, BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI)) {
				// ricerca domini con connettore mypivot abilitato
				List<String> domini = AnagraficaManager.getListaCodDomini(configWrapper);
				
				int threadNotificaPoolSize = GovpayConfig.getInstance().getDimensionePoolThreadSpedizioneTracciatiNotificaPagamenti();
				for (String codDominio : domini) {
					it.govpay.bd.model.Dominio dominio = null;
					try {
						dominio = AnagraficaManager.getDominio(configWrapper, codDominio);
					}catch(NotFoundException e) {
						log.debug("Dominio ["+dominio+"] non trovato, passo alla prossimo.");
						continue;
					}

					if(dominio.getConnettoreMyPivot() != null && dominio.getConnettoreMyPivot().isAbilitato()) {
						log.debug("Scheduling spedizione Tracciati MyPivot per il Dominio ["+codDominio+"]...");
						TracciatiNotificaPagamenti tracciatiMyPivot = new TracciatiNotificaPagamenti(it.govpay.model.TracciatoNotificaPagamenti.TIPO_TRACCIATO.MYPIVOT);
						
						
						int offset = 0;
						int limit = (2 * threadNotificaPoolSize);
						List<SpedizioneTracciatoNotificaPagamentiThread> threads = new ArrayList<>();
						List<TracciatoNotificaPagamenti> tracciatiInStatoNonTerminalePerDominio = tracciatiMyPivot.findTracciatiInStatoNonTerminalePerDominio(codDominio, offset, limit, ctx);
						
						log.debug("Trovati ["+tracciatiInStatoNonTerminalePerDominio.size()+"] Tracciati MyPivot da spedire per il Dominio ["+codDominio+"]...");

						if(tracciatiInStatoNonTerminalePerDominio.size() > 0) {
							for(TracciatoNotificaPagamenti tracciatoMyPivot: tracciatiInStatoNonTerminalePerDominio) {
								SpedizioneTracciatoNotificaPagamentiThread sender = new SpedizioneTracciatoNotificaPagamentiThread(tracciatoMyPivot, dominio.getConnettoreMyPivot(), ctx);
								ThreadExecutorManager.getClientPoolExecutorSpedizioneTracciatiNotificaPagamenti().execute(sender);
								threads.add(sender);
							}

							log.debug("Processi di spedizione Tracciati MyPivot avviati.");
							aggiornaSondaOK(configWrapper, BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI);

							// Aspetto che abbiano finito tutti
							int numeroErrori = 0;
							while(true){
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {

								}
								boolean completed = true;
								for(SpedizioneTracciatoNotificaPagamentiThread sender : threads) {
									if(!sender.isCompleted()) 
										completed = false;
								}

								if(completed) { 
									for(SpedizioneTracciatoNotificaPagamentiThread sender : threads) {
										if(sender.isErrore()) 
											numeroErrori ++;
									}
									int numOk = threads.size() - numeroErrori;
									log.debug("Completata Esecuzione dei ["+threads.size()+"] Threads, OK ["+numOk+"], Errore ["+numeroErrori+"]");
									break; // esco
								}
							}
							
							log.info("Spedizione Tracciati MyPivot per il Dominio ["+codDominio+"] completata.");
							//Hanno finito tutti, aggiorno stato esecuzione
							BatchManager.aggiornaEsecuzione(configWrapper, BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI);
						}
					} else {
						log.trace("Connettore MyPivot non configurato per il Dominio ["+codDominio+"], non ricerco tracciati da spedire.");
					}
					
					if(dominio.getConnettoreSecim() != null && dominio.getConnettoreSecim().isAbilitato()) {
						log.debug("Scheduling spedizione Tracciati Secim per il Dominio ["+codDominio+"]...");
						TracciatiNotificaPagamenti tracciatiSecim = new TracciatiNotificaPagamenti(it.govpay.model.TracciatoNotificaPagamenti.TIPO_TRACCIATO.SECIM);
						
						int offset = 0;
						int limit = (2 * threadNotificaPoolSize);
						List<SpedizioneTracciatoNotificaPagamentiThread> threads = new ArrayList<>();
						List<TracciatoNotificaPagamenti> tracciatiInStatoNonTerminalePerDominio = tracciatiSecim.findTracciatiInStatoNonTerminalePerDominio(codDominio, offset, limit, ctx);
						
						log.debug("Trovati ["+tracciatiInStatoNonTerminalePerDominio.size()+"] Tracciati Secim da spedire per il Dominio ["+codDominio+"]...");

						if(tracciatiInStatoNonTerminalePerDominio.size() > 0) {
							for(TracciatoNotificaPagamenti tracciatoMyPivot: tracciatiInStatoNonTerminalePerDominio) {
								SpedizioneTracciatoNotificaPagamentiThread sender = new SpedizioneTracciatoNotificaPagamentiThread(tracciatoMyPivot, dominio.getConnettoreSecim(), ctx);
								ThreadExecutorManager.getClientPoolExecutorSpedizioneTracciatiNotificaPagamenti().execute(sender);
								threads.add(sender);
							}

							log.debug("Processi di spedizione Tracciati Secim avviati.");
							aggiornaSondaOK(configWrapper, BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI);

							// Aspetto che abbiano finito tutti
							int numeroErrori = 0;
							while(true){
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {

								}
								boolean completed = true;
								for(SpedizioneTracciatoNotificaPagamentiThread sender : threads) {
									if(!sender.isCompleted()) 
										completed = false;
								}

								if(completed) { 
									for(SpedizioneTracciatoNotificaPagamentiThread sender : threads) {
										if(sender.isErrore()) 
											numeroErrori ++;
									}
									int numOk = threads.size() - numeroErrori;
									log.debug("Completata Esecuzione dei ["+threads.size()+"] Threads, OK ["+numOk+"], Errore ["+numeroErrori+"]");
									break; // esco
								}
							}
							
							log.info("Spedizione Tracciati Secim per il Dominio ["+codDominio+"] completata.");
							//Hanno finito tutti, aggiorno stato esecuzione
							BatchManager.aggiornaEsecuzione(configWrapper, BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI);
						}
					} else {
						log.trace("Connettore Secim non configurato per il Dominio ["+codDominio+"], non ricerco tracciati da spedire.");
					}
				}
				
				aggiornaSondaOK(configWrapper, BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI);
				BatchManager.stopEsecuzione(configWrapper, BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI);
			
				log.info("Spedizione tracciati MyPivot terminata.");
				return "Spedizione tracciati MyPivot terminata.";
			} else {
				log.info("Operazione in corso su altro nodo. Richiesta interrotta.");
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile avviare la spedizione dei tracciati notifica pagamenti", e);
			try {
				aggiornaSondaKO(configWrapper, BATCH_SPEDIZIONE_TRACCIATI_NOTIFICA_PAGAMENTI, e); 
			} catch (Throwable e1) {
				log.error("Aggiornamento sonda fallito: " + e1.getMessage(),e1);
			}
			return "Non è stato possibile avviare la spedizione dei tracciati notifica pagamenti: " + e;
		} finally {
		}
	}
}
