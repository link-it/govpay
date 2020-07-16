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

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.BatchBD;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.bd.configurazione.model.AppIOBatch;
import it.govpay.bd.configurazione.model.MailBatch;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.NotificaAppIo;
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.core.business.Rendicontazioni.DownloadRendicontazioniResponse;
import it.govpay.core.dao.pagamenti.dto.ElaboraTracciatoDTO;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.thread.InviaNotificaAppIoThread;
import it.govpay.core.utils.thread.InviaNotificaThread;
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

	private static boolean eseguiGestionePromemoria;
	private static boolean eseguiInvioPromemoria;
	private static boolean eseguiInvioNotifiche;
	private static boolean eseguiInvioNotificheAppIO;
	
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

	private static boolean eseguiGenerazioneAvvisi;

	public static synchronized void setEseguiGenerazioneAvvisi() {
		eseguiGenerazioneAvvisi = true;
	}

	public static synchronized void resetEseguiGenerazioneAvvisi() {
		eseguiGenerazioneAvvisi = false;
	}

	public static synchronized boolean getEseguiGenerazioneAvvisi() {
		return eseguiGenerazioneAvvisi;
	}

	private static boolean eseguiElaborazioneTracciati = true;

	public static synchronized void setEseguiElaborazioneTracciati() {
		eseguiElaborazioneTracciati = true;
	}

	public static synchronized void resetEseguiElaborazioneTracciati() {
		eseguiElaborazioneTracciati = false;
	}

	public static synchronized boolean getEseguiElaborazioneTracciati() {
		return eseguiElaborazioneTracciati;
	}

	public static String acquisizioneRendicontazioni(IContext ctx){
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(ctx.getTransactionId());
			if(BatchManager.startEsecuzione(bd, RND)) {
				DownloadRendicontazioniResponse downloadRendicontazioni = new Rendicontazioni(bd).downloadRendicontazioni(false);
				aggiornaSondaOK(RND, bd,ctx);
				return downloadRendicontazioni.getDescrizioneEsito();
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Acquisizione rendicontazioni fallita", e);
			aggiornaSondaKO(RND, e, bd,ctx);
			return "Acquisizione fallita#" + e;
		} finally {
			BatchManager.stopEsecuzione(bd, RND);
			if(bd != null) bd.closeConnection();
		}
	}

	public static String recuperoRptPendenti(IContext ctx){
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(ctx.getTransactionId());
			if(BatchManager.startEsecuzione(bd, PND)) {
				String verificaTransazioniPendenti = new Pagamento(bd).verificaTransazioniPendenti();
				aggiornaSondaOK(PND, bd,ctx);
				return verificaTransazioniPendenti;
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Acquisizione Rpt pendenti fallita", e);
			aggiornaSondaKO(PND, e, bd,ctx);
			return "Acquisizione fallita#" + e;
		} finally {
			BatchManager.stopEsecuzione(bd, PND);
			if(bd != null) bd.closeConnection();
		}
	}

	public static String spedizioneNotifiche(IContext ctx){
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(ctx.getTransactionId());

			if(BatchManager.startEsecuzione(bd, NTFY)) {
				log.trace("Spedizione notifiche non consegnate");

				ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
				ApplicazioneFilter applicazioniFilter = applicazioniBD.newFilter();

				List<String> applicazioni = applicazioniBD.findAllCodApplicazione(applicazioniFilter);
				it.govpay.core.business.Notifica notificheBD = new it.govpay.core.business.Notifica(bd);

				int threadNotificaPoolSize = GovpayConfig.getInstance().getDimensionePoolNotifica();

				for (String codApplicazione : applicazioni) {
					it.govpay.bd.model.Applicazione applicazione = null;
					try {
						applicazione = AnagraficaManager.getApplicazione(bd, codApplicazione);
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

						log.info("Trovate ["+notifiche.size()+"] notifiche da spedire per l'applicazione ["+codApplicazione+"]");

						if(notifiche.size() > 0) {
							for(Notifica notifica: notifiche) {
								InviaNotificaThread sender = new InviaNotificaThread(notifica, bd,ctx);
								ThreadExecutorManager.getClientPoolExecutorNotifica().execute(sender);
								threads.add(sender);
							}

							log.info("Processi di spedizione notifiche per l'applicazione ["+codApplicazione+"] avviati.");
							aggiornaSondaOK(NTFY, bd,ctx);
							
							// chiudo connessione in attesa che tutti abbiano finito
							bd.closeConnection();

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
							
							// Hanno finito tutti. riattivo la connessione
							bd.setupConnection(ctx.getTransactionId());
							BatchManager.aggiornaEsecuzione(bd, NTFY);
						}
					}   else {
						log.debug("Connettore non configurato per l'applicazione ["+codApplicazione+"], non ricerco notifiche da spedire.");
					}
				}
				aggiornaSondaOK(NTFY, bd,ctx);
				log.info("Spedizione notifiche completata.");
				return "Spedizione notifiche completata.";
			} else {
				log.info("Operazione in corso su altro nodo. Richiesta interrotta.");
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile avviare la spedizione delle notifiche", e);
			aggiornaSondaKO(NTFY, e, bd,ctx); 
			return "Non è stato possibile avviare la spedizione delle notifiche: " + e;
		} finally {
			BatchManager.stopEsecuzione(bd, NTFY);
			if(bd != null) bd.closeConnection();
		}
	}
	
	public static String spedizioneNotificheAppIO(IContext ctx){
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(ctx.getTransactionId());
			
			it.govpay.bd.model.Configurazione configurazione = AnagraficaManager.getConfigurazione(bd);
			
			if(!configurazione.getBatchSpedizioneAppIo().isAbilitato()) {
				return "Spedizione notifiche AppIO disabilitata.";
			}

			if(BatchManager.startEsecuzione(bd, NTFY_APP_IO)) {
				log.trace("Spedizione notifiche AppIO non consegnate");

				it.govpay.core.business.NotificaAppIo notificheBD = new it.govpay.core.business.NotificaAppIo(bd);

				int threadNotificaPoolSize = GovpayConfig.getInstance().getDimensionePoolNotificaAppIO();
				int offset = 0;
				int limit = (2 * threadNotificaPoolSize);
				List<InviaNotificaAppIoThread> threads = new ArrayList<>();
				List<NotificaAppIo> notifiche  = notificheBD.findNotificheDaSpedire(offset,limit);

				log.info("Trovate ["+notifiche.size()+"] notifiche AppIO da spedire");

				if(notifiche.size() > 0) {
					for(NotificaAppIo notifica: notifiche) {
						InviaNotificaAppIoThread sender = new InviaNotificaAppIoThread(notifica, bd,ctx);
						ThreadExecutorManager.getClientPoolExecutorNotificaAppIo().execute(sender);
						threads.add(sender);
					}

					log.info("Processi di spedizione notifiche AppIO avviati.");
					aggiornaSondaOK(NTFY_APP_IO, bd,ctx);
					
					// chiudo connessione in attesa che tutti abbiano finito
					bd.closeConnection();

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
					
					// Hanno finito tutti. riattivo la connessione
					bd.setupConnection(ctx.getTransactionId());
					BatchManager.aggiornaEsecuzione(bd, NTFY_APP_IO);
				}
				aggiornaSondaOK(NTFY, bd,ctx);
				log.info("Spedizione notifiche AppIO completata.");
				return "Spedizione notifiche AppIO completata.";
			} else {
				log.info("Operazione in corso su altro nodo. Richiesta interrotta.");
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile avviare la spedizione delle notifiche AppIO", e);
			aggiornaSondaKO(NTFY_APP_IO, e, bd,ctx); 
			return "Non è stato possibile avviare la spedizione delle notifiche AppIO: " + e;
		} finally {
			BatchManager.stopEsecuzione(bd, NTFY_APP_IO);
			if(bd != null) bd.closeConnection();
		}
	}

	public static String resetCacheAnagrafica(IContext ctx){
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(ctx.getTransactionId());
			BatchBD batchBD = new BatchBD(bd);
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
			if(bd != null) bd.closeConnection();
		}
	}

	public static String resetCacheAnagraficaCheck(IContext ctx){
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(ctx.getTransactionId());
			log.debug("Check reset della cache anagrafica locale in corso ...");	

			BatchBD batchBD = new BatchBD(bd);
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
			if(bd != null) bd.closeConnection();
		}
	}

	private static void aggiornaSondaOK(String nome, BasicBD bd, IContext ctx) {
		if(bd==null) return;
		boolean wasConnected = true;
		try {
			if(bd.isClosed()) {
				wasConnected = false;
				bd.setupConnection(ctx.getTransactionId());
			}
			bd.enableSelectForUpdate();
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
			if(bd != null)
				try {
					bd.disableSelectForUpdate();
				} catch (ServiceException e) {
					log.error("Errore " +e.getMessage() , e);
				}
			if(bd != null && !wasConnected) bd.closeConnection();
		}
	}

	private static void aggiornaSondaKO(String nome, Exception e, BasicBD bd, IContext ctx) {
		if(bd==null) return;
		boolean wasConnected = true;
		try {
			if(bd.isClosed()) {
				wasConnected = false;
				bd.setupConnection(ctx.getTransactionId());
			}
			bd.enableSelectForUpdate();
			Connection con = bd.getConnection();
			Sonda sonda = SondaFactory.get(nome, con, bd.getJdbcProperties().getDatabase());
			if(sonda == null) throw new SondaException("Sonda ["+nome+"] non trovata");
			//			Properties properties = new Properties();
			//			((SondaBatch)sonda).aggiornaStatoSonda(false, properties, new Date(), "Il batch e' stato interrotto con errore: " + e.getMessage(), con, bd.getJdbcProperties().getDatabase());
			((SondaBatch)sonda).aggiornaStatoSonda(false, new Date(), "Il batch e' stato interrotto con errore: " + e.getMessage(), con, bd.getJdbcProperties().getDatabase());
		} catch (Throwable t) {
			log.warn("Errore nell'aggiornamento della sonda KO: "+ t.getMessage());
		} finally {
			if(bd != null)
				try {
					bd.disableSelectForUpdate();
				} catch (ServiceException e1) {
					log.error("Errore " +e1.getMessage() , e1);
				}
			if(bd != null && !wasConnected) bd.closeConnection();
		}
	}

	public static String elaborazioneTracciatiPendenze(IContext ctx){
		BasicBD bd = null;
		boolean wasAutoCommit = false;
		try {
			bd = BasicBD.newInstance(ctx.getTransactionId());
			wasAutoCommit = bd.isAutoCommit();

			if(BatchManager.startEsecuzione(bd, BATCH_TRACCIATI)) {
				wasAutoCommit = bd.isAutoCommit();
				if(wasAutoCommit)
					bd.setAutoCommit(false);
				log.trace("Elaborazione tracciati");

				TracciatiBD tracciatiBD = new TracciatiBD(bd);
				TracciatoFilter filter = tracciatiBD.newFilter();
				filter.setTipo(Arrays.asList(TIPO_TRACCIATO.PENDENZA));
				filter.setStato(STATO_ELABORAZIONE.ELABORAZIONE);
				filter.setLimit(25);
				filter.setIncludiRawRichiesta(true);
				//				filter.setDataUltimoAggiornamentoMax(new Date());
				List<Tracciato> tracciati = tracciatiBD.findAll(filter);
				Tracciati tracciatiBusiness = new Tracciati(bd);

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

				aggiornaSondaOK(BATCH_TRACCIATI, bd, ctx);
				BatchManager.stopEsecuzione(bd, BATCH_TRACCIATI);
				log.info("Elaborazione tracciati terminata.");
				return "Elaborazione tracciati terminata.";
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			try {
				if(!bd.isAutoCommit()) bd.rollback();
				aggiornaSondaKO(BATCH_TRACCIATI, e, bd, ctx);
			} catch (Throwable e1) {
				log.error("Aggiornamento sonda fallito: " + e1.getMessage(),e1);
			}
			log.error("Non è stato possibile eseguire l'elaborazione dei tracciati", e);
			return "Non è stato possibile eseguire l'elaborazione dei tracciati: " + e;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}

	public static String spedizionePromemoria(IContext ctx){
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(ctx.getTransactionId());

			Configurazione configurazioneBD = new Configurazione(bd);
			it.govpay.bd.model.Configurazione configurazione = configurazioneBD.getConfigurazione();
			MailBatch batchSpedizioneEmail = configurazione.getBatchSpedizioneEmail();
			if(!batchSpedizioneEmail.isAbilitato()) {
				return "Spedizione promemoria disabilitata.";
			}

			if(BatchManager.startEsecuzione(bd, BATCH_SPEDIZIONE_PROMEMORIA)) {
				int limit = 100;
				log.trace("Spedizione primi ["+limit+"] promemoria non consegnati");
				Promemoria promemoriaBD = new Promemoria(bd); 
				List<it.govpay.bd.model.Promemoria> promemorias  = promemoriaBD.findPromemoriaDaSpedire(0, limit);

				if(promemorias.size() == 0) {
					aggiornaSondaOK(BATCH_SPEDIZIONE_PROMEMORIA, bd,ctx);
					BatchManager.stopEsecuzione(bd, BATCH_SPEDIZIONE_PROMEMORIA);
					log.debug("Nessun promemoria da inviare.");
					return "Nessun promemoria da inviare.";
				}

				log.info("Trovati ["+promemorias.size()+"] promemoria da spedire");

				for(it.govpay.bd.model.Promemoria promemoria: promemorias) {
					promemoriaBD.invioPromemoria(promemoria);
				}
				aggiornaSondaOK(BATCH_SPEDIZIONE_PROMEMORIA, bd,ctx);
				log.info("Spedizione promemoria completata.");
				return "Spedizione promemoria completata.";
			} else {
				log.info("Operazione in corso su altro nodo. Richiesta interrotta.");
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile avviare la spedizione dei promemoria", e);
			try {
				if(!bd.isAutoCommit()) bd.rollback();
				aggiornaSondaKO(BATCH_SPEDIZIONE_PROMEMORIA, e, bd,ctx); 
			} catch (Throwable e1) {
				log.error("Aggiornamento sonda fallito: " + e1.getMessage(),e1);
			}
			return "Non è stato possibile avviare la spedizione dei promemoria: " + e;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
	
	public static String gestionePromemoria(IContext ctx){
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(ctx.getTransactionId());

			Configurazione configurazioneBD = new Configurazione(bd);
			it.govpay.bd.model.Configurazione configurazione = configurazioneBD.getConfigurazione();
			MailBatch batchSpedizioneEmail = configurazione.getBatchSpedizioneEmail();
			AppIOBatch batchSpedizioneAppIO = configurazione.getBatchSpedizioneAppIo();
			
			if(!batchSpedizioneEmail.isAbilitato() && !batchSpedizioneAppIO.isAbilitato()) {
				return "Spedizione promemoria Email e AppIO disabilitata.";
			}

			if(BatchManager.startEsecuzione(bd, BATCH_GESTIONE_PROMEMORIA)) {
				int limit = 100;
				
				log.trace("Elaborazione primi ["+limit+"] versamenti con promemoria avviso non consegnati");
				VersamentiBD versamentiBD = new VersamentiBD(bd);
				it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
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
				
				aggiornaSondaOK(BATCH_GESTIONE_PROMEMORIA, bd,ctx);
				
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
				
				aggiornaSondaOK(BATCH_GESTIONE_PROMEMORIA, bd,ctx);
				
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
				
				aggiornaSondaOK(BATCH_GESTIONE_PROMEMORIA, bd,ctx);
				BatchManager.stopEsecuzione(bd, BATCH_GESTIONE_PROMEMORIA);
				
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
				if(!bd.isAutoCommit()) bd.rollback();
				aggiornaSondaKO(BATCH_GESTIONE_PROMEMORIA, e, bd,ctx); 
			} catch (Throwable e1) {
				log.error("Aggiornamento sonda fallito: " + e1.getMessage(),e1);
			}
			return "Non è stato possibile avviare la gestione dei promemoria: " + e;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
}
