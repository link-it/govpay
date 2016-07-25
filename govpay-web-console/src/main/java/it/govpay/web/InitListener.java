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
package it.govpay.web;

import it.govpay.bd.ConnectionManager;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.web.utils.ConsoleProperties;

import java.net.URI;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

public class InitListener implements ServletContextListener {

	private static boolean initialized = false;
	
	public static boolean isInitialized() {
		return InitListener.initialized;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try{
			
			URI log4j2Config = null;
			try {
				log4j2Config = ConsoleProperties.getInstance().getLog4j2Config();
				if(log4j2Config != null) {
					LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
					context.setConfigLocation(log4j2Config);
				}
			} catch (Exception e) {
				LogManager.getLogger().warn("Errore durante la configurazione del Logger: " + e);
			}
			
			
			AnagraficaManager.newInstance(false);
			ConnectionManager.initialize();
		} catch(Exception e){}
		initialized = true;
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			ConnectionManager.shutdown();
		} catch (Exception e) {
			LogManager.getLogger().warn("Errore nella de-registrazione JMX: " + e);
		}
	}
}
