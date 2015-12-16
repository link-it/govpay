package it.govpay.thread;

import it.govpay.exception.GovPayException;
import it.govpay.utils.GovPayConfiguration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;

public class ThreadExecutorManager {
	
	private static ExecutorService executor;
	
	public static void setup() throws GovPayException {
		int threadPoolSize = GovPayConfiguration.newInstance().getThreadPoolSize();
		LogManager.getLogger().info("Predisposizione pool di spedizione esiti [NumThread: "+threadPoolSize+"]" );
		executor = Executors.newFixedThreadPool(threadPoolSize);
	}
	
	public static void shutdown() throws InterruptedException {
		executor.shutdown();
		while (!executor.isTerminated()) {
			Thread.sleep(500);
		}
	}
	
	public static ExecutorService getEsitiPoolExecutor() {
		return executor;
	}

}
