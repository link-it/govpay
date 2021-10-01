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
package it.govpay.core.utils.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openspcoop2.utils.LoggerWrapperFactory;

import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GovpayConfig;

public class ThreadExecutorManager {

	private static ExecutorService executorNotifica;
	private static ExecutorService executorNotificaAppIo;
	private static ExecutorService executorRPT;
	private static ExecutorService executorCaricamentoTracciatiStampeAvvisi;
	private static ExecutorService executorCaricamentoTracciati;
	private static ExecutorService executorSpedizioneTracciatiNotificaPagamenti;
	private static boolean initialized = false;

	private static synchronized void init() throws GovPayException {
		if(!initialized) {
			int threadNotificaPoolSize = GovpayConfig.getInstance().getDimensionePoolNotifica();
			LoggerWrapperFactory.getLogger(ThreadExecutorManager.class).info("Predisposizione pool di spedizione messaggi notifica [NumThread: "+threadNotificaPoolSize+"]" );
			executorNotifica = Executors.newFixedThreadPool(threadNotificaPoolSize);
			
			int threadNotificaAppIoPoolSize = GovpayConfig.getInstance().getDimensionePoolNotificaAppIO();
			LoggerWrapperFactory.getLogger(ThreadExecutorManager.class).info("Predisposizione pool di spedizione messaggi notifica AppIO [NumThread: "+threadNotificaAppIoPoolSize+"]" );
			executorNotificaAppIo = Executors.newFixedThreadPool(threadNotificaAppIoPoolSize);

			int threadCaricamentoTracciatiStampeAvvisiPoolSize = GovpayConfig.getInstance().getDimensionePoolCaricamentoTracciatiStampaAvvisi();
			LoggerWrapperFactory.getLogger(ThreadExecutorManager.class).info("Predisposizione pool di caricamento tracciati: stampa avvisi [NumThread: "+threadCaricamentoTracciatiStampeAvvisiPoolSize+"]" );
			executorCaricamentoTracciatiStampeAvvisi = Executors.newFixedThreadPool(threadCaricamentoTracciatiStampeAvvisiPoolSize);

			int threadRPTPoolSize = GovpayConfig.getInstance().getDimensionePoolRPT();
			LoggerWrapperFactory.getLogger(ThreadExecutorManager.class).info("Predisposizione pool di spedizione rpt [NumThread: "+threadRPTPoolSize+"]" );
			executorRPT = Executors.newFixedThreadPool(threadRPTPoolSize);
			
			int threadCaricamentoTracciatiPoolSize = GovpayConfig.getInstance().getDimensionePoolCaricamentoTracciati();
			LoggerWrapperFactory.getLogger(ThreadExecutorManager.class).info("Predisposizione pool di caricamento tracciati [NumThread: "+threadCaricamentoTracciatiPoolSize+"]" );
			executorCaricamentoTracciati = Executors.newFixedThreadPool(threadCaricamentoTracciatiPoolSize);
			
			int threadSpedizioneTracciatiNotificaPagamentiPoolSize = GovpayConfig.getInstance().getDimensionePoolThreadSpedizioneTracciatiNotificaPagamenti();
			LoggerWrapperFactory.getLogger(ThreadExecutorManager.class).info("Predisposizione pool di spedizione tracciati notifica pagamenti [NumThread: "+threadSpedizioneTracciatiNotificaPagamentiPoolSize+"]" );
			executorSpedizioneTracciatiNotificaPagamenti = Executors.newFixedThreadPool(threadSpedizioneTracciatiNotificaPagamentiPoolSize);
		}
		initialized = true;
	}

	public static void setup() throws GovPayException {
		if(!initialized) {
			init();
		}
	}

	public static void shutdown() throws InterruptedException {
		executorNotifica.shutdown();
		while (!executorNotifica.isTerminated()) {
			Thread.sleep(500);
		}
		
		executorNotificaAppIo.shutdown();
		while (!executorNotificaAppIo.isTerminated()) {
			Thread.sleep(500);
		}

		executorCaricamentoTracciatiStampeAvvisi.shutdown();
		while (!executorCaricamentoTracciatiStampeAvvisi.isTerminated()) {
			Thread.sleep(500);
		}

		executorRPT.shutdown();
		while (!executorRPT.isTerminated()) {
			Thread.sleep(500);
		}
		
		executorCaricamentoTracciati.shutdown();
		while (!executorCaricamentoTracciati.isTerminated()) {
			Thread.sleep(500);
		}
		
		executorSpedizioneTracciatiNotificaPagamenti.shutdown();
		while (!executorSpedizioneTracciatiNotificaPagamenti.isTerminated()) {
			Thread.sleep(500);
		}
	}

	public static ExecutorService getClientPoolExecutorNotifica() {
		return executorNotifica;
	}
	
	public static ExecutorService getClientPoolExecutorNotificaAppIo() {
		return executorNotificaAppIo;
	}

	public static ExecutorService getClientPoolExecutorCaricamentoTracciatiStampeAvvisi() {
		return executorCaricamentoTracciatiStampeAvvisi;
	}

	public static ExecutorService getClientPoolExecutorRPT() {
		return executorRPT;
	}
	
	public static ExecutorService getClientPoolExecutorCaricamentoTracciati() {
		return executorCaricamentoTracciati;
	}
	
	public static ExecutorService getClientPoolExecutorSpedizioneTracciatiNotificaPagamenti() {
		return executorSpedizioneTracciatiNotificaPagamenti;
	}
}
