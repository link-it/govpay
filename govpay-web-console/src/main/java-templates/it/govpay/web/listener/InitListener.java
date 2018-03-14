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
package it.govpay.web.listener;

import java.io.File;
import java.net.URI;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.config.Log4jConfig;
import org.slf4j.Logger;

import it.govpay.core.utils.GovpayConfig;

public class InitListener implements ServletContextListener {

	private static Logger log = LoggerWrapperFactory.getLogger("boot");	
	private static boolean initialized = false;

	public static boolean isInitialized() {
		return InitListener.initialized;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String commit = "${git.commit.id}";
		if(commit.length() > 7) commit = commit.substring(0, 7);
		try{
			GovpayConfig gpConfig = null;
			try {
				gpConfig = GovpayConfig.newInstance(InitListener.class.getResourceAsStream(GovpayConfig.PROPERTIES_FILE));
				it.govpay.bd.GovpayConfig.newInstance4GovPay(InitListener.class.getResourceAsStream(GovpayConfig.PROPERTIES_FILE));
				it.govpay.bd.GovpayCustomConfig.newInstance(InitListener.class.getResourceAsStream(GovpayConfig.PROPERTIES_FILE));
			} catch (Exception e) {
				throw new RuntimeException("Inizializzazione di GovPay-Console ${project.version} (build " + commit + ") fallita: " + e, e);
			}
		
			// Gestione della configurazione di Log4J
			URI log4j2Config = gpConfig.getLog4j2Config();
			Log4jConfig log4jConfig = new Log4jConfig();
			if(log4j2Config != null) {
				log4jConfig.setLog4jConfigFile(new File(log4j2Config));
			} else {
				log4jConfig.setLog4jConfigURL(InitListener.class.getResource("/log4j2.xml"));
			}
			
			try {
				log = LoggerWrapperFactory.getLogger("boot");	
				log.info("Inizializzazione GovPay-Console ${project.version} (build " + commit + ") in corso");
				
				if(log4j2Config != null) {
					log.info("Caricata configurazione logger: " + gpConfig.getLog4j2Config().getPath());
				} else {
					log.info("Configurazione logger da classpath.");
				}
				gpConfig.readProperties();
			} catch (Exception e) {
				throw new RuntimeException("Inizializzazione di GovPay-Console ${project.version} (build " + commit + ") fallita: " + e, e);
			}
		
//			RicevutaPagamentoProperties.newInstance(ConsoleProperties.getInstance().getResourceDir());
//			AvvisoPagamentoProperties.newInstance(ConsoleProperties.getInstance().getResourceDir());
//			AnagraficaManager.newInstance();
//			ConnectionManager.initialize();
//			OperazioneFactory.init();
		} catch(Exception e){
			throw new RuntimeException("Inizializzazione di GovPay-Console ${project.version} (build " + commit + ") fallita: " + e, e);
		}
		log.info("Inizializzazione GovPay-Console ${project.version} (build " + commit + ") completata con successo.");
		initialized = true;
		
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		log.info("Shutdown Govpay-Console in corso...");
//		try {
//			ConnectionManager.shutdown();
//		} catch (Exception e) {
//			LoggerWrapperFactory.getLogger(InitListener.class).warn("Errore nella de-registrazione JMX: " + e);
//		}
		log.info("Shutdown di Govpay-Console completato.");
	}
}
