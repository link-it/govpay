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
package it.govpay.core.utils.thread;

import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GovpayConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;

public class ThreadExecutorManager {
	
	private static ExecutorService executor;
	
	public static void setup() throws GovPayException {
		int threadPoolSize = GovpayConfig.getInstance().getDimensionePool();
		LogManager.getLogger().info("Predisposizione pool di spedizione messaggi [NumThread: "+threadPoolSize+"]" );
		executor = Executors.newFixedThreadPool(threadPoolSize);
	}
	
	public static void shutdown() throws InterruptedException {
		executor.shutdown();
		while (!executor.isTerminated()) {
			Thread.sleep(500);
		}
	}
	
	public static ExecutorService getClientPoolExecutor() {
		return executor;
	}

}
