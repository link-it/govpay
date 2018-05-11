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
package it.govpay.ejb;

import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.business.Psp;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.StartupUtils;
import it.govpay.core.utils.thread.ThreadExecutorManager;

@Startup
@Singleton
public class StartupEjb {

	private static Logger log = null;
	private String warName = "GovPay";
	private String govpayVersion = "${project.version}";
	private String buildVersion = "${git.commit.id}";
	private String tipoServizioGovpay = GpContext.TIPO_SERVIZIO_GOVPAY_OPT;
	private String dominioAnagraficaManager = "it.govpay.cache.anagrafica.core";
	
	@PostConstruct
	public void init() {
		
		// Commit id
		String commit = (buildVersion.length() > 7) ? buildVersion.substring(0, 7) : buildVersion;
		
		InputStream govpayPropertiesIS = StartupEjb.class.getResourceAsStream(GovpayConfig.PROPERTIES_FILE);
		URL log4j2URL = StartupEjb.class.getResource(GovpayConfig.LOG4J2_XML_FILE);
		InputStream msgDiagnosticiIS = StartupEjb.class.getResourceAsStream(GovpayConfig.MSG_DIAGNOSTICI_PROPERTIES_FILE);
		GpContext ctx = StartupUtils.startup(log, warName, govpayVersion, commit, govpayPropertiesIS, log4j2URL, msgDiagnosticiIS, tipoServizioGovpay);
		
		try {
			log = LoggerWrapperFactory.getLogger("boot");	
			InputStream govpaySchemaIS = StartupEjb.class.getResourceAsStream(GovpayConfig.GOVPAY_OPEN_API_FILE);
			
			StartupUtils.startupServices(log, warName, govpayVersion, commit, ctx, dominioAnagraficaManager, GovpayConfig.getInstance(), govpaySchemaIS);
			JmxOperazioni.register();
		} catch (RuntimeException e) {
			log.error("Inizializzazione fallita", e);
			shutdown();
			ctx.log();
			throw e;
		} catch (Exception e) {
			log.error("Inizializzazione fallita", e);
			shutdown();
			ctx.log();
			throw new RuntimeException("Inizializzazione "+StartupUtils.getGovpayVersion(warName, govpayVersion, commit)+" fallita.", e);
		} 

		SetupThread st = new SetupThread(GpThreadLocal.get().getTransactionId());
		st.start();
		
		ctx.log();

		log.info("Inizializzazione "+StartupUtils.getGovpayVersion(warName, govpayVersion, commit)+" completata con successo."); 
	}

	@PreDestroy
	public void shutdown() {
		// Commit id
		String commit = (buildVersion.length() > 7) ? buildVersion.substring(0, 7) : buildVersion;
		MDC.put("cmd", "Shutdown");
		MDC.put("op", UUID.randomUUID().toString() );
		
		log.info("Shutdown "+StartupUtils.getGovpayVersion(warName, govpayVersion, commit)+" in corso...");
		
		log.info("De-registrazione delle cache ...");
		AnagraficaManager.unregister();
		log.info("De-registrazione delle cache completato");
		
		
		log.info("Shutdown pool thread notifiche ...");
		try {
			ThreadExecutorManager.shutdown();
			log.info("Shutdown pool thread notifiche completato.");
		} catch (Exception e) {
			log.warn("Shutdown pool thread notifiche fallito:" + e);
		}
		
		
		log.info("De-registrazione risorse JMX ...");
		try {
			JmxOperazioni.unregister();
			log.info("De-registrazione risorse JMX completato.");
		} catch (Exception e) {
			log.warn("Errore nella de-registrazione JMX: " + e);
		}
		
		log.info("Shutdown del Connection Manager ...");
		try {
			ConnectionManager.shutdown();
			log.info("Shutdown del Connection Manager completato.");
		} catch (Exception e) {
			log.warn("Errore nello shutdown del Connection Manager: " + e);
		}
		
		log.info("Shutdown di "+StartupUtils.getGovpayVersion(warName, govpayVersion, commit)+" completato.");
	}



	public class SetupThread extends Thread {
		
		private String transactionId = null;
		
		public SetupThread(String transactionId) {
			this.transactionId = transactionId;
		}
		
		@Override
		public void run() {
			BasicBD bd = null;
			GpContext ctx = null;
			try {
				log.debug("Aggiornamento della lista dei PSP in corso...");
				ctx = new GpContext(this.transactionId);
				GpThreadLocal.set(ctx);
				bd = BasicBD.newInstance(this.transactionId);
				String esito = new Psp(bd).aggiornaRegistro();
				if(esito.contains("Acquisizione fallita"))
					log.warn("Aggiornamento della lista dei PSP fallito");
				else 
					log.info("Aggiornamento della lista dei PSP completato");
				
			} catch (Exception e) {
				log.error("Aggiornamento della lista dei PSP fallito",e);
			} finally {
				if(bd != null) bd.closeConnection();
				if(ctx != null) ctx.log();
			}
		}

	}
}
