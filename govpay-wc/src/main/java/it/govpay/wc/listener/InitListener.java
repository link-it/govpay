package it.govpay.wc.listener;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.LoggerFactory;
import org.openspcoop2.utils.logger.beans.proxy.Operation;
import org.openspcoop2.utils.logger.beans.proxy.Service;
import org.openspcoop2.utils.logger.config.DatabaseConfig;
import org.openspcoop2.utils.logger.config.DatabaseConfigDatasource;
import org.openspcoop2.utils.logger.config.DiagnosticConfig;
import org.openspcoop2.utils.logger.config.Log4jConfig;
import org.openspcoop2.utils.logger.config.MultiLoggerConfig;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.cache.RuoliCache;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;

public class InitListener implements ServletContextListener{

	private static Logger log = LoggerWrapperFactory.getLogger("boot");	
	private static boolean initialized = false;

	public static boolean isInitialized() {
		return InitListener.initialized;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		GpContext ctx = null;
		// Commit id
		String commit = "9e3d8bab6d416d659be45a4f32a7697da47fa54d";
		if(commit.length() > 7) commit = commit.substring(0, 7);
		try{
			GovpayConfig gpConfig = null;
			try {
				gpConfig = GovpayConfig.newInstance();
				it.govpay.bd.GovpayConfig.newInstance4GovPay();
				it.govpay.bd.GovpayCustomConfig.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Inizializzazione di GovPay fallita: " + e, e);
			}
			
			
			URI log4j2Config = null;
			try {
				log4j2Config = gpConfig.getLog4j2Config();
				if(log4j2Config != null) {
					LoggerWrapperFactory.setLogConfiguration(log4j2Config);
					log = LoggerWrapperFactory.getLogger("boot");	

					log.info("Inizializzazione GovPay 2.4.7 (build " + commit + ") in corso");
					log.info("Caricata configurazione logger: " + log4j2Config.getPath());
				} else {
					LoggerWrapperFactory.setLogConfiguration("/log4j2.xml");
					log = LoggerWrapperFactory.getLogger("boot");	

					log.info("Inizializzazione GovPay 2.4.7 (build " + commit + ") in corso.");
					log.info("Configurazione logger da classpath.");
				}
			} catch (Exception e) {
				LoggerWrapperFactory.getLogger(InitListener.class).warn("Errore durante la configurazione del Logger: " + e);
			}
			
			try {
				gpConfig.readProperties();
			} catch (Exception e) {
				throw new RuntimeException("Inizializzazione di GovPay fallita: " + e, e);
			}

			// Configurazione del logger Diagnostici/Tracce/Dump
			try {
				DiagnosticConfig diagnosticConfig = new DiagnosticConfig();
				Properties props = new Properties();
				InputStream is = InitListener.class.getResourceAsStream("/msgDiagnostici.properties");
				props.load(is);
				diagnosticConfig.setDiagnosticConfigProperties(props);
				diagnosticConfig.setThrowExceptionPlaceholderFailedResolution(false);

				Log4jConfig log4jConfig = new Log4jConfig();
				if(log4j2Config != null) {
					log4jConfig.setLog4jConfigFile(new File(log4j2Config));
				} else {
					log4jConfig.setLog4jConfigURL(InitListener.class.getResource("/log4j2.xml"));
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
				LoggerFactory.initialize(GovpayConfig.getInstance().getmLogClass(), mConfig);

			} catch (Exception e) {
				log.error("Errore durante la configurazione dei diagnostici", e);
				throw new RuntimeException("Inizializzazione GovPay 2.4.7 (build " + commit + ") fallita.", e);
			}



			try {
				ctx = new GpContext();
				MDC.put("cmd", "Inizializzazione");
				MDC.put("op", ctx.getTransactionId());
				Service service = new Service();
				service.setName("Inizializzazione");
				service.setType(GpContext.TIPO_SERVIZIO_GOVPAY_JSON);
				ctx.getTransaction().setService(service);
				Operation opt = new Operation();
				opt.setName("Init");
				ctx.getTransaction().setOperation(opt);
				GpThreadLocal.set(ctx);
			} catch (Exception e) {
				log.error("Errore durante predisposizione del contesto: " + e);
				if(ctx != null) ctx.log();
				throw new RuntimeException("Inizializzazione GovPay 2.4.7 (build " + commit + ") fallita.", e);
			}
			
			//		per ora vengono inizializzate nel govpay web
			
			AnagraficaManager.newInstance(false);
			RuoliCache.newInstance(log);
			//			ConnectionManager.initialize();
			//			OperazioneFactory.init();
		} catch(Exception e){
			throw new RuntimeException("Inizializzazione di GovPay-API-Pagamento fallita: " + e, e);
		}

		ctx.log();

		log.info("Inizializzazione GovPay 2.4.7 (build " + commit + ") completata con successo.");
		initialized = true;
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//		try {
		//			ConnectionManager.shutdown();
		//		} catch (Exception e) {
		//			LogManager.getLogger().warn("Errore nella de-registrazione JMX: " + e);
		//		}
	}
}
