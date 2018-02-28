package it.govpay.ragioneria.api.listener;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.UUID;

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

import it.govpay.bd.ConnectionManager;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.cache.AclCache;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.stampe.pdf.rt.utils.RicevutaPagamentoProperties;

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
		String commit = "${git.commit.id}";
		if(commit.length() > 7) commit = commit.substring(0, 7);
		try{
			GovpayConfig gpConfig = null;
			try {
				gpConfig = GovpayConfig.newInstance(InitListener.class.getResourceAsStream(GovpayConfig.PROPERTIES_FILE));
				it.govpay.bd.GovpayConfig.newInstance4GovPay(InitListener.class.getResourceAsStream(GovpayConfig.PROPERTIES_FILE));
				it.govpay.bd.GovpayCustomConfig.newInstance(InitListener.class.getResourceAsStream(GovpayConfig.PROPERTIES_FILE));
			} catch (Exception e) {
				throw new RuntimeException("Inizializzazione di Govpay-API-Ragioneria fallita: " + e, e);
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
				InputStream is = InitListener.class.getResourceAsStream("/msgDiagnostici.properties");
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
				throw new RuntimeException("Inizializzazione Govpay-API-Ragioneria ${project.version} (build " + commit + ") fallita.", e);
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
				throw new RuntimeException("Inizializzazione Govpay-API-Ragioneria ${project.version} (build " + commit + ") fallita.", e);
			}
			
			RicevutaPagamentoProperties.newInstance(gpConfig.getResourceDir());
			AnagraficaManager.newInstance("it.govpay.cache.anagrafica.ragioneria");
			ConnectionManager.initialize();
			AclCache.newInstance(log);
			//			OperazioneFactory.init();
		} catch(Exception e){
			throw new RuntimeException("Inizializzazione di GovPay-API-Ragioneria fallita: " + e, e);
		}

		ctx.log();

		log.info("Inizializzazione Govpay-API-Ragioneria ${project.version} (build " + commit + ") completata con successo.");
		initialized = true;
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		MDC.put("cmd", "Shutdown");
		MDC.put("op", UUID.randomUUID().toString() );
		
		log.info("Shutdown Govpay-API-Ragioneria in corso...");
		
//		log.info("De-registrazione delle cache ...");
//		AnagraficaManager.unregister();
//		log.info("De-registrazione delle cache completato");
//		
//		log.info("Shutdown del Connection Manager ...");
//		try {
//			ConnectionManager.shutdown();
//			log.info("Shutdown del Connection Manager completato.");
//		} catch (Exception e) {
//			log.warn("Errore nello shutdown del Connection Manager: " + e);
//		}
		
		log.info("Shutdown di Govpay-API-Ragioneria completato.");
	}
}
