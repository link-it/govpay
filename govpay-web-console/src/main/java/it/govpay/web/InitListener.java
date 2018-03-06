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
package it.govpay.web;

import java.net.URI;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.utils.tracciati.operazioni.OperazioneFactory;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;
import it.govpay.stampe.pdf.rt.utils.RicevutaPagamentoProperties;
import it.govpay.web.utils.ConsoleProperties;

public class InitListener implements ServletContextListener {

	private static boolean initialized = false;
	private static Logger log = null;

	public static boolean isInitialized() {
		return InitListener.initialized;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try{

			URI log4j2Config =  ConsoleProperties.getInstance().getLog4j2Config();
			if(log4j2Config != null) {
				LoggerWrapperFactory.setLogConfiguration(log4j2Config);
			} else {
				LoggerWrapperFactory.setLogConfiguration("/log4j2.xml");
			}

			log = LoggerWrapperFactory.getLogger("boot");	
			log.info("Inizializzazione GovPay Console in corso");

			if(log4j2Config != null) {
				log.info("Caricata configurazione logger: " + log4j2Config.getPath());
			} else {
				log.info("Configurazione logger da classpath.");
			}

			GovpayConfig.newInstance4GovPayConsole(InitListener.class.getResourceAsStream("/govpayConsole.properties"));
			RicevutaPagamentoProperties.newInstance(ConsoleProperties.getInstance().getResourceDir());
			AvvisoPagamentoProperties.newInstance(ConsoleProperties.getInstance().getResourceDir());
			AnagraficaManager.newInstance();
			ConnectionManager.initialize();
			OperazioneFactory.init();
		} catch(Exception e){
			throw new RuntimeException("Inizializzazione di GovPay Console fallita: " + e, e);
		}
		log.info("Inizializzazione GovPay Console completata con successo.");
		initialized = true;
		
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			ConnectionManager.shutdown();
		} catch (Exception e) {
			LoggerWrapperFactory.getLogger(InitListener.class).warn("Errore nella de-registrazione JMX: " + e);
		}
	}
}
