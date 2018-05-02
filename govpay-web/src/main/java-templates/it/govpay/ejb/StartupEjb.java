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

import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.business.Psp;
import it.govpay.core.cache.AclCache;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.slf4j.MDC;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.openspcoop2.utils.logger.LoggerFactory;
import org.openspcoop2.utils.logger.beans.proxy.Operation;
import org.openspcoop2.utils.logger.beans.proxy.Service;
import org.openspcoop2.utils.logger.config.DatabaseConfig;
import org.openspcoop2.utils.logger.config.DatabaseConfigDatasource;
import org.openspcoop2.utils.logger.config.DiagnosticConfig;
import org.openspcoop2.utils.logger.config.MultiLoggerConfig;
import org.openspcoop2.utils.logger.config.Log4jConfig;

@Startup
@Singleton
public class StartupEjb {

	private static Logger log = null;

	@PostConstruct
	public void init() {
		
		// Commit id
		String commit = "${git.commit.id}";
		if(commit.length() > 7) commit = commit.substring(0, 7);
		
		
		GovpayConfig gpConfig = null;
		try {
			gpConfig = GovpayConfig.newInstance(StartupEjb.class.getResourceAsStream(GovpayConfig.PROPERTIES_FILE));
			it.govpay.bd.GovpayConfig.newInstance4GovPay(StartupEjb.class.getResourceAsStream(GovpayConfig.PROPERTIES_FILE));
			it.govpay.bd.GovpayCustomConfig.newInstance(StartupEjb.class.getResourceAsStream(GovpayConfig.PROPERTIES_FILE));
		} catch (Exception e) {
			throw new RuntimeException("Inizializzazione di GovPay fallita: " + e, e);
		}

		// Gestione della configurazione di Log4J
		URI log4j2Config = gpConfig.getLog4j2Config();
		Log4jConfig log4jConfig = new Log4jConfig();
		if(log4j2Config != null) {
			log4jConfig.setLog4jConfigFile(new File(log4j2Config));
		} else {
			log4jConfig.setLog4jConfigURL(StartupEjb.class.getResource("/log4j2.xml"));
		}
		
		try {
			log = LoggerWrapperFactory.getLogger("boot");	
			log.info("Inizializzazione GovPay ${project.version} (build " + commit + ") in corso");
			
			if(log4j2Config != null) {
				log.info("Caricata configurazione logger: " + gpConfig.getLog4j2Config().getPath());
			} else {
				log.info("Configurazione logger da classpath.");
			}
			gpConfig.readProperties();
		} catch (Exception e) {
			throw new RuntimeException("Inizializzazione di GovPay fallita: " + e, e);
		}

		// Configurazione del logger Diagnostici/Tracce/Dump
		try {
			DiagnosticConfig diagnosticConfig = new DiagnosticConfig();
			InputStream is = StartupEjb.class.getResourceAsStream("/msgDiagnostici.properties");
			Properties props = new Properties();
			props.load(is);
			diagnosticConfig.setDiagnosticConfigProperties(props);
			diagnosticConfig.setThrowExceptionPlaceholderFailedResolution(false);

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
			LoggerFactory.initialize(GovpayConfig.getInstance().getmLogClass(), mConfig);

		} catch (Exception e) {
			log.error("Errore durante la configurazione dei diagnostici", e);
			throw new RuntimeException("Inizializzazione GovPay ${project.version} (build " + commit + ") fallita.", e);
		}

		GpContext ctx = null;

		try {
			ctx = new GpContext();
			MDC.put("cmd", "Inizializzazione");
			MDC.put("op", ctx.getTransactionId());
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
			throw new RuntimeException("Inizializzazione GovPay ${project.version} (build " + commit + ") fallita.", e);
		}

		try {
			AnagraficaManager.newInstance("it.govpay.cache.anagrafica.core");
			JaxbUtils.init();
			ThreadExecutorManager.setup();
			JmxOperazioni.register();
			AvvisoPagamentoProperties.newInstance(GovpayConfig.getInstance().getResourceDir());
			AclCache.newInstance(log);
		} catch (Exception e) {
			log.error("Inizializzazione fallita", e);
			shutdown();
			ctx.log();
			throw new RuntimeException("Inizializzazione GovPay ${project.version} (build " + commit + ") fallita.", e);
		}

		SetupThread st = new SetupThread(GpThreadLocal.get().getTransactionId());
		st.start();
		
		ctx.log();

		log.info("Inizializzazione GovPay ${project.version} (build " + commit + ") completata con successo.");
	}

	@PreDestroy
	public void shutdown() {
		MDC.put("cmd", "Shutdown");
		MDC.put("op", UUID.randomUUID().toString() );
		
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
