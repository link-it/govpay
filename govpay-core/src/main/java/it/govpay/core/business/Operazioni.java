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
package it.govpay.core.business;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.core.business.IConservazione.EsitoConservazione;
import it.govpay.core.business.model.ElaboraTracciatoDTO;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.model.Rpt.StatoConservazione;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.Tracciato.StatoTracciatoType;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.proxy.Operation;
import org.openspcoop2.utils.logger.beans.proxy.Service;
import org.openspcoop2.utils.sonde.Sonda;
import org.openspcoop2.utils.sonde.SondaException;
import org.openspcoop2.utils.sonde.SondaFactory;
import org.openspcoop2.utils.sonde.impl.SondaBatch;

public class Operazioni{

	private static Logger log = LogManager.getLogger();
	public static final String rnd = "update-rnd";
	public static final String psp = "update-psp";
	public static final String pnd = "update-pnd";
	public static final String ntfy = "update-ntfy";
	public static final String check_ntfy = "check-ntfy";
	public static final String batch_tracciati = "caricamento-tracciati";
	public static final String check_tracciati = "check-tracciati";
	public static final String conto = "update-conto";
	public static final String conservazione_req = "cons-req";
	public static final String conservazione_esito = "cons-esito";

	
	private static boolean eseguiElaborazioneTracciati;
	
	public static synchronized void setEseguiElaborazioneTracciati() {
		eseguiElaborazioneTracciati = true;
	}
	
	public static synchronized boolean getAndResetEseguiElaborazioneTracciati() {
		boolean value = eseguiElaborazioneTracciati;
		if(eseguiElaborazioneTracciati) {
			eseguiElaborazioneTracciati = false;
		}
		return value;
	}

	public static String acquisizioneRendicontazioni(String serviceName){

		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			ThreadContext.put("cmd", "AcquisizioneRendicontazioni");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName(serviceName);
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("AcquisizioneRendicontazioni");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			if(BatchManager.startEsecuzione(bd, rnd)) {
				String response = new Rendicontazioni(bd).downloadRendicontazioni(false);
				aggiornaSondaOK(rnd, bd);
				return response;
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Acquisizione rendicontazioni fallita", e);
			aggiornaSondaKO(rnd, e, bd);
			return "Acquisizione fallita#" + e;
		} finally {
			BatchManager.stopEsecuzione(bd, rnd);
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}

	public static String aggiornamentoRegistroPsp(String serviceName){
		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			ThreadContext.put("cmd", "AggiornamentoRegistroPsp");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName(serviceName);
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("AggiornamentoRegistroPsp");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			if(BatchManager.startEsecuzione(bd, psp)) {
				String response = new Psp(bd).aggiornaRegistro();
				AnagraficaManager.cleanPspCache();
				aggiornaSondaOK(psp, bd);
				return response;
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Aggiornamento della lista dei PSP fallito", e);
			aggiornaSondaKO(psp, e, bd);
			return "Acquisizione fallita#" + e;
		} finally {
			BatchManager.stopEsecuzione(bd, psp);
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}

	public static String recuperoRptPendenti(String serviceName){
		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			ThreadContext.put("cmd", "RecuperoRptPendenti");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName(serviceName);
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("RecuperoRptPendenti");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			if(BatchManager.startEsecuzione(bd, pnd)) {
				String verificaTransazioniPendenti = new Pagamento(bd).verificaTransazioniPendenti();
				aggiornaSondaOK(pnd, bd);
				return verificaTransazioniPendenti;
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Acquisizione Rpt pendenti fallita", e);
			aggiornaSondaKO(pnd, e, bd);
			return "Acquisizione fallita#" + e;
		} finally {
			BatchManager.stopEsecuzione(bd, pnd);
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}

	public static String spedizioneNotifiche(String serviceName){
		BasicBD bd = null;
		List<InviaNotificaThread> threads = new ArrayList<InviaNotificaThread>();
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			ThreadContext.put("cmd", "SpedizioneNotifiche");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName(serviceName);
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("SpedizioneNotifiche");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			if(BatchManager.startEsecuzione(bd, ntfy)) {
				log.trace("Spedizione notifiche non consegnate");
				NotificheBD notificheBD = new NotificheBD(bd);
				List<Notifica> notifiche  = notificheBD.findNotificheDaSpedire();
				if(notifiche.size() == 0) {
					BatchManager.stopEsecuzione(bd, ntfy);
					return "Nessuna notifica da inviare.";
				}

				log.info("Trovate ["+notifiche.size()+"] notifiche da spedire");
				for(Notifica notifica: notifiche) {
					InviaNotificaThread sender = new InviaNotificaThread(notifica, bd);
					ThreadExecutorManager.getClientPoolExecutor().execute(sender);
				}
				log.info("Processi di spedizione avviati.");
				aggiornaSondaOK(ntfy, bd);
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile avviare la spedizione delle notifiche", e);
			aggiornaSondaKO(ntfy, e, bd); 
			return "Non è stato possibile avviare la spedizione delle notifiche: " + e;
		} finally {
			if(bd != null) bd.closeConnection();
		}

		// Aspetto che abbiano finito tutti
		while(true){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {

			}
			boolean completed = true;
			for(InviaNotificaThread sender : threads) {
				if(!sender.isCompleted()) 
					completed = false;
			}

			if(completed) {
				try {
					bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
					BatchManager.stopEsecuzione(bd, ntfy);
				} catch (ServiceException e) {
				} finally {
					if(bd != null) bd.closeConnection();
				}

				return "Spedizione notifiche completata.";
			}
		}
	}
	
	
	
	public static String richiestaConservazioneRt(String serviceName){
		BasicBD bd = null;
		GpContext ctx = null;
		long conservate = 0;
		try {
			
			// Verifico se e' stato configurato il plugin di conservazione
			IConservazione conservazionePlugin = GovpayConfig.getInstance().getConservazionPlugin();
			if(conservazionePlugin == null) {
				return "Plugin di conservazione non configurato.";
			}
			
			ctx = new GpContext();
			ThreadContext.put("cmd", "RichiestaConservazioneRT");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName(serviceName);
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("RichiestaConservazioneRT");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			
			if(BatchManager.startEsecuzione(bd, conservazione_req)) {
				log.trace("Gestione RT non conservate");
				
				RptBD rptBD = new RptBD(bd);
				RptFilter filter = rptBD.newFilter();
				filter.setStato(StatoRpt.RT_ACCETTATA_PA);
				filter.setConservato(false);
				
				List<Rpt> rpts  = rptBD.findAll(filter);

				log.info("Trovate ["+rpts.size()+"] RT da conservare");
				for(Rpt rpt: rpts) {
					conservazionePlugin.sendConservazione(rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp(), rpt.getCodMsgRicevuta(), rpt.getXmlRt());
					rpt.setStatoConservazione(StatoConservazione.RICHIESTA);
					rpt.setDataConservazione(new Date());
					rptBD.updateRpt(rpt.getId(), rpt);
					conservate++;
				}
				log.info("Rt inviate alla conservazione.");
				aggiornaSondaOK(conservazione_req, bd);
				return "Inviate a conservazione " + conservate + " RT.";
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile completare la conservazione delle RT", e);
			aggiornaSondaKO(conservazione_req, e, bd); 
			return "Inviate a conservazione " + conservate + " RT. Processo interrotto: " + e;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
	
	public static String esitoConservazioneRt(String serviceName){
		BasicBD bd = null;
		GpContext ctx = null;
		long esitati = 0;
		try {
			
			// Verifico se e' stato configurato il plugin di conservazione
			IConservazione conservazionePlugin = GovpayConfig.getInstance().getConservazionPlugin();
			if(conservazionePlugin == null) {
				return "Plugin di conservazione non configurato.";
			}
			
			ctx = new GpContext();
			ThreadContext.put("cmd", "EsitoConservazioneRT");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName(serviceName);
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("EsitoConservazioneRT");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			
			if(BatchManager.startEsecuzione(bd, conservazione_esito)) {
				log.trace("Gestione esito RT");
				
				RptBD rptBD = new RptBD(bd);
				
				List<EsitoConservazione> esiti  = conservazionePlugin.getEsitiConservazione();

				log.info("Trovate ["+esiti.size()+"] esiti conservazione da acquisire");
				
				for(EsitoConservazione esito: esiti) {
					Rpt rpt = rptBD.getRpt(esito.getCodDominio(), esito.getIuv(), esito.getCcp());
					rpt.setStatoConservazione(esito.getStatoConservazione());
					rpt.setDataConservazione(new Date());
					rpt.setDescrizioneStatoConservazione(esito.getDescrizioneStato());
					rptBD.updateRpt(rpt.getId(), rpt);
					try {
						conservazionePlugin.notificaAcquisizioneEsito(esito);
					} catch (Exception e) {
						log.warn("Notifica esito conservazione RT fallita", e);
					}
					esitati++;
				}
				log.info("Rt inviate alla conservazione.");
				aggiornaSondaOK(conservazione_req, bd);
				return "Acquisiti " + esitati + " esiti di conservazione RT.";
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile completare l'acquisizione degli esiti della conservazione RT", e);
			aggiornaSondaKO(conservazione_req, e, bd); 
			return "Acquisiti " + esitati + " esiti di conservazione RT. Processo interrotto: " + e;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}

	public static String elaborazioneTracciati(String serviceName){
		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			ThreadContext.put("cmd", "ElaborazioneTracciati");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName(serviceName);
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("CaricamentoTracciato");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			bd.setAutoCommit(false);
			if(BatchManager.startEsecuzione(bd, batch_tracciati)) {
				log.trace("Elaborazione tracciati");
				TracciatiBD tracciatiBD = new TracciatiBD(bd);
				TracciatoFilter filter = tracciatiBD.newFilter();
				filter.addStatoTracciato(StatoTracciatoType.NUOVO);
				filter.addStatoTracciato(StatoTracciatoType.IN_CARICAMENTO);
				filter.setDataUltimoAggiornamentoMax(new Date());

				List<Tracciato> tracciati  = tracciatiBD.findAll(filter);
				if(tracciati.size() == 0) {
					BatchManager.stopEsecuzione(bd, batch_tracciati);
					return "Nessun tracciato da elaborare.";
				}

				log.info("Trovati ["+tracciati.size()+"] tracciati da caricare");
				
				Tracciati tracciatiBusiness = new Tracciati(bd);
				for(Tracciato tracciato: tracciati) {
					log.info("Avvio elaborazione tracciato "  + tracciato.getId());
					ElaboraTracciatoDTO elaboraTracciatoDTO = new ElaboraTracciatoDTO();
					elaboraTracciatoDTO.setTracciato(tracciato);
					tracciatiBusiness.elaboraTracciato(elaboraTracciatoDTO);
					log.info("Elaborazione tracciato "  + tracciato.getId() + " completata");
				}
				aggiornaSondaOK(batch_tracciati, bd);
				BatchManager.stopEsecuzione(bd, batch_tracciati);
				log.info("Elaborazione tracciati terminata.");
				return "Elaborazione tracciati terminata.";
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Non è stato possibile eseguire l'elaborazione dei tracciati", e);
			aggiornaSondaKO(batch_tracciati, e, bd); 
			return "Non è stato possibile eseguire l'elaborazione dei tracciati: " + e;
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}

	public static String resetCacheAnagrafica(){
		ThreadContext.put("cmd", "ResetCacheAnagrafica");
		ThreadContext.put("op", UUID.randomUUID().toString() );
		try {
			AnagraficaManager.cleanCache();
			return "Reset cache completata con successo.";
		} catch (Exception e) {
			log.error("Reset cache anagrafica fallita", e);
			return "Reset cache completata fallita: " + e;
		} 
	}

	public static String estrattoConto(String serviceName){
		if(!GovpayConfig.getInstance().isBatchEstrattoConto())
			return "Servizio estratto conto non configurato";

		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			ThreadContext.put("cmd", "EstrattoConto");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName(serviceName);
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("EstrattoConto");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			if(BatchManager.startEsecuzione(bd, conto)) {
				String creaEstrattiContoSuFileSystem = new EstrattoConto(bd).creaEstrattiContoSuFileSystem();
				aggiornaSondaOK(conto, bd);
				return creaEstrattiContoSuFileSystem;
			} else {
				return "Operazione in corso su altro nodo. Richiesta interrotta.";
			}
		} catch (Exception e) {
			log.error("Estratto Conto fallito", e);
			aggiornaSondaKO(conto, e, bd);
			return "Estratto Conto#" + e.getMessage();
		} finally {
			BatchManager.stopEsecuzione(bd, conto);
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}

	private static void aggiornaSondaOK(String nome, BasicBD bd) {
		if(bd==null) return;
		boolean wasConnected = true;
		try {
			if(bd.isClosed()) {
				wasConnected = false;
				bd.setupConnection(GpThreadLocal.get().getTransactionId());
			}
			Connection con = bd.getConnection();

			Sonda sonda = SondaFactory.get(nome, con, bd.getJdbcProperties().getDatabase());
			if(sonda == null) throw new SondaException("Sonda ["+nome+"] non trovata");
			((SondaBatch)sonda).aggiornaStatoSonda(true, new Date(), "Ok", con, bd.getJdbcProperties().getDatabase());
		} catch (Throwable t) {
			log.warn("Errore nell'aggiornamento della sonda OK", t);
		}
		finally {
			if(bd != null && !wasConnected) bd.closeConnection();
		}
	}

	private static void aggiornaSondaKO(String nome, Exception e, BasicBD bd) {
		if(bd==null) return;
		boolean wasConnected = true;
		try {
			if(bd.isClosed()) {
				wasConnected = false;
				bd.setupConnection(GpThreadLocal.get().getTransactionId());
			}
			Connection con = bd.getConnection();
			Sonda sonda = SondaFactory.get(nome, con, bd.getJdbcProperties().getDatabase());
			if(sonda == null) throw new SondaException("Sonda ["+nome+"] non trovata");
			((SondaBatch)sonda).aggiornaStatoSonda(false, new Date(), "Il batch e' stato interrotto con errore: " + e.getMessage(), con, bd.getJdbcProperties().getDatabase());
		} catch (Throwable t) {
			log.warn("Errore nell'aggiornamento della sonda KO", t);
		} finally {
			if(bd != null && !wasConnected) bd.closeConnection();
		}
	}
	
	
	
	
}