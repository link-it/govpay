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

import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.business.Psp;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.thread.ThreadExecutorManager;

import java.io.File;
import java.net.URI;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;
import org.openspcoop2.utils.logger.LoggerFactory;
import org.openspcoop2.utils.logger.beans.proxy.Operation;
import org.openspcoop2.utils.logger.beans.proxy.Service;
import org.openspcoop2.utils.logger.config.DatabaseConfig;
import org.openspcoop2.utils.logger.config.DatabaseConfigDatasource;
import org.openspcoop2.utils.logger.config.DiagnosticConfig;
import org.openspcoop2.utils.logger.config.Log4jConfig;
import org.openspcoop2.utils.logger.config.MultiLoggerConfig;
import org.openspcoop2.utils.logger.log4j.Log4jType;

@Startup
@Singleton
public class StartupEjb {

	private static Logger log = LogManager.getLogger("boot");	
	private static org.apache.log4j.Logger logv1 = org.apache.log4j.LogManager.getLogger(StartupEjb.class);	

	@PostConstruct
	public void init() {
		GovpayConfig gpConfig = null;
		try {
			gpConfig = GovpayConfig.newInstance();
			it.govpay.bd.GovpayConfig.newInstance("/govpay.properties");
			it.govpay.bd.GovpayCustomConfig.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Inizializzazione di GovPay fallita: " + e, e);
		}

		// Gestione della configurazione di Log4J
		URI log4j2Config = gpConfig.getLog4j2Config();
		if(log4j2Config != null) {
			LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
			context.setConfigLocation(log4j2Config);
			log = LogManager.getLogger("boot");	
			
			log.info("Inizializzazione GovPay v2.3-SNAPSHOT ($Format:%h$) in corso.");
			log.info("Caricata configurazione logger: " + gpConfig.getLog4j2Config().getPath());
		} else {
			log.info("Inizializzazione GovPay v2.3-SNAPSHOT ($Format:%h$) in corso.");
			log.info("Configurazione logger da classpath.");
		}
		
		try {
			gpConfig.readProperties();
		} catch (Exception e) {
			throw new RuntimeException("Inizializzazione di GovPay fallita: " + e, e);
		}

		// Configurazione del logger Diagnostici/Tracce/Dump
		try {
			DiagnosticConfig diagnosticConfig = new DiagnosticConfig();
			diagnosticConfig.setDiagnosticPropertiesResourceURI("/msgDiagnostici.properties");
			diagnosticConfig.setThrowExceptionPlaceholderFailedResolution(false);

			Log4jConfig log4jConfig = new Log4jConfig();
			log4jConfig.setLog4jType(Log4jType.LOG4Jv2);
			if(log4j2Config != null) {
				log4jConfig.setLog4jPropertiesResource(new File(log4j2Config));
			} else {
				log4jConfig.setLog4jPropertiesResourceURI("/log4j2.xml");
			}

			MultiLoggerConfig mConfig = new MultiLoggerConfig();
			mConfig.setDiagnosticConfig(diagnosticConfig);
			mConfig.setDiagnosticSeverityFilter(GovpayConfig.getInstance().getmLogLevel());
			mConfig.setLog4jLoggerEnabled(GovpayConfig.getInstance().ismLogOnLog4j());
			mConfig.setLog4jConfig(log4jConfig);	
			mConfig.setDbLoggerEnabled(GovpayConfig.getInstance().ismLogOnDB());

			if(GovpayConfig.getInstance().ismLogOnDB()) {
				DatabaseConfig dbConfig = new DatabaseConfig();
				DatabaseConfigDatasource dbDSConfig = new DatabaseConfigDatasource();
				dbDSConfig.setJndiName(GovpayConfig.getInstance().getmLogDS());
				dbConfig.setConfigDatasource(dbDSConfig);
				dbConfig.setDatabaseType(GovpayConfig.getInstance().getmLogDBType());
				dbConfig.setLogSql(GovpayConfig.getInstance().ismLogSql());
				mConfig.setDatabaseConfig(dbConfig);
			}
			LoggerFactory.initialize(GovpayConfig.getInstance().getmLogClass(), logv1, mConfig);

		} catch (Exception e) {
			log.error("Errore durante la configurazione dei diagnostici", e);
			throw new RuntimeException("Inizializzazione GovPay fallita.", e);
		}

		GpContext ctx = null;

		try {
			ctx = new GpContext();
			ThreadContext.put("cmd", "Inizializzazione");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName("Inizializzazione");
			service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_OPT);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("Init");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
		} catch (Exception e) {
			log.error("Errore durante predisposizione del contesto: " + e);
			if(ctx != null) ctx.log();
			throw new RuntimeException("Inizializzazione GovPay fallita.", e);
		}

		try {
			AnagraficaManager.newInstance();
			JaxbUtils.init();
			ConnectionManager.initialize();
			ThreadExecutorManager.setup();
			JmxOperazioni.register();
		} catch (Exception e) {
			log.error("Inizializzazione fallita", e);
			shutdown();
			ctx.log();
			throw new RuntimeException("Inizializzazione GovPay fallita.", e);
		}

		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			new Psp(bd).aggiornaRegistro();
		} catch (Exception e) {
			log.error("Aggiornamento della lista dei PSP fallito",e);
		} finally {
			if(bd != null) bd.closeConnection();
		}

		ctx.log();

		log.info("Inizializzazione GovPay v2.3-SNAPSHOT completata con successo.");
	}

	@PreDestroy
	public void shutdown() {
		ThreadContext.put("cmd", "Shutdown");
		ThreadContext.put("op", UUID.randomUUID().toString() );
		
		log.info("Shutdown GovPay in corso...");
		
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
		
		log.info("Shutdown di GovPay completato.");
	}
}
