package it.govpay.web;

import it.govpay.bd.ConnectionManager;
import it.govpay.bd.anagrafica.AnagraficaManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitListener implements ServletContextListener {

	private static boolean initialized = false;
	
	public static boolean isInitialized() {
		return InitListener.initialized;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try{
			AnagraficaManager.newInstance(false);
			ConnectionManager.initialize();
		} catch(Exception e){}
		initialized = true;
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
}
