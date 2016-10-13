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
package it.govpay.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.utils.logger.beans.proxy.Operation;
import org.openspcoop2.utils.logger.beans.proxy.Service;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.core.business.EstrattoConto;
import it.govpay.core.business.Pagamento;
import it.govpay.core.business.Psp;
import it.govpay.core.business.Rendicontazioni;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;

@Singleton
public class Operazioni{

	private static Logger log = LogManager.getLogger();

	@Resource
	TimerService timerservice;

	@Schedule(hour="4,8,16,20", persistent=false)
	@AccessTimeout(value=10, unit=TimeUnit.MINUTES)
	public static String acquisizioneRendicontazioni(){
		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			ThreadContext.put("cmd", "AcquisizioneRendicontazioni");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName("Batch");
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_BATCH);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("AcquisizioneRendicontazioni");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			String response = new Rendicontazioni(bd).downloadRendicontazioni();
			return response;
		} catch (Exception e) {
			log.error("Acquisizione rendicontazioni fallita", e);
			return "Acquisizione fallita#" + e;
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}

	@Schedule(hour="0,12", persistent=false)
	@AccessTimeout(value=10, unit=TimeUnit.MINUTES)
	public static String aggiornamentoRegistroPsp(){
		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			ThreadContext.put("cmd", "AggiornamentoRegistroPsp");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName("Batch");
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_BATCH);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("AggiornamentoRegistroPsp");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			return new Psp(bd).aggiornaRegistro();
		} catch (Exception e) {
			log.error("Aggiornamento della lista dei PSP fallito", e);
			return "Acquisizione fallita#" + e;
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}

	@Schedule(hour="2,6,10,14,18,22", persistent=false)
	@AccessTimeout(value=10, unit=TimeUnit.MINUTES)
	public static String recuperoRptPendenti(){
		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			ThreadContext.put("cmd", "RecuperoRptPendenti");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName("Batch");
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_BATCH);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("RecuperoRptPendenti");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			return new Pagamento(bd).verificaTransazioniPendenti();
		} catch (Exception e) {
			log.error("Acquisizione Rpt pendenti fallita", e);
			return "Acquisizione fallita#" + e;
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}

	@Schedule(hour="*", minute="*/30", persistent=false)
	@AccessTimeout(value=20, unit=TimeUnit.MINUTES)
	public static boolean spedizioneNotifiche(){
		BasicBD bd = null;
		List<InviaNotificaThread> threads = new ArrayList<InviaNotificaThread>();
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			ThreadContext.put("cmd", "SpedizioneNotifiche");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName("Batch");
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_BATCH);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("SpedizioneNotifiche");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			log.trace("Spedizione notifiche non consegnate");

			NotificheBD notificheBD = new NotificheBD(bd);

			List<Notifica> notifiche  = notificheBD.findNotificheDaSpedire();

			if(notifiche.size() == 0) return true;

			log.info("Trovate ["+notifiche.size()+"] notifiche da spedire");

			for(Notifica notifica: notifiche) {
				InviaNotificaThread sender = new InviaNotificaThread(notifica, bd);
				ThreadExecutorManager.getClientPoolExecutor().execute(sender);
			}

			log.info("Processi di spedizione avviati.");
		} catch (Exception e) {
			log.error("Non è stato possibile avviare la spedizione delle notifiche", e);
			return false;
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
				log.info("Processo di spedizione completati.");
				return true;
			}
		}
	}

	public static boolean resetCacheAnagrafica(){
		ThreadContext.put("cmd", "ResetCacheAnagrafica");
		ThreadContext.put("op", UUID.randomUUID().toString() );
		try {
			AnagraficaManager.cleanCache();
			return true;
		} catch (Exception e) {
			log.error("Reset cache anagrafica fallita", e);
			return false;
		} 
	}
	
	@Schedule(hour="0,12", persistent=false)
	@AccessTimeout(value=5, unit=TimeUnit.MINUTES)
	public void generaEstrattoConto(Timer timer) {
		estrattoConto();
	}

	public static String estrattoConto(){
		if(!GovpayConfig.getInstance().isBatchEstrattoConto())
			return "Servizio estratto conto non configurato";
		
		BasicBD bd = null;
		GpContext ctx = null;
		try {
			ctx = new GpContext();
			ThreadContext.put("cmd", "EstrattoConto");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName("Batch");
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_BATCH);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("EstrattoConto");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			return new EstrattoConto(bd).estrattoConto();
		} catch (Exception e) {
			log.error("Estratto Conto fallito", e);
			return "Estratto Conto#" + e.getMessage();
		} finally {
			if(bd != null) bd.closeConnection();
			if(ctx != null) ctx.log();
		}
	}
}