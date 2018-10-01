package it.govpay.backoffice.api.listener;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.StartupUtils;

public class InitListener implements ServletContextListener{

	private static Logger log = null;
	private static boolean initialized = false;
	private String warName = "GovPay-API-Backoffice";
	private String govpayVersion = "${project.version}";
	private String buildVersion = "${git.commit.id}";
	private String tipoServizioGovpay = GpContext.TIPO_SERVIZIO_GOVPAY_JSON;
	private String dominioAnagraficaManager = "it.govpay.cache.anagrafica.backoffice";

	public static boolean isInitialized() {
		return InitListener.initialized;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// Commit id
		String commit = (buildVersion.length() > 7) ? buildVersion.substring(0, 7) : buildVersion;
		
		InputStream govpayPropertiesIS = InitListener.class.getResourceAsStream(GovpayConfig.PROPERTIES_FILE);
		URL log4j2URL = InitListener.class.getResource(GovpayConfig.LOG4J2_XML_FILE);
		InputStream msgDiagnosticiIS = InitListener.class.getResourceAsStream(GovpayConfig.MSG_DIAGNOSTICI_PROPERTIES_FILE);
		GpContext ctx = StartupUtils.startup(log, warName, govpayVersion, commit, govpayPropertiesIS, log4j2URL, msgDiagnosticiIS, tipoServizioGovpay);
		try {
			log = LoggerWrapperFactory.getLogger("boot");	
			StartupUtils.startupServices(log, warName, govpayVersion, commit, ctx, dominioAnagraficaManager, GovpayConfig.getInstance());
			
			Map<String, String> map = new HashMap<String, String>();
			map.put(GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE);
			map.put(GovpayConfig.GOVPAY_PAGAMENTI_OPEN_API_FILE_NAME, GovpayConfig.GOVPAY_PAGAMENTI_OPEN_API_FILE);
			
			for(String k : map.keySet()) {
				StartupUtils.initValidator(log, warName, govpayVersion, commit, ctx, k, InitListener.class.getResourceAsStream(map.get(k)));				
			}
		} catch (RuntimeException e) {
			log.error("Inizializzazione fallita", e);
			ctx.log();
			throw e;
		} catch (Exception e) {
			log.error("Inizializzazione fallita", e);
			ctx.log();
			throw new RuntimeException("Inizializzazione "+StartupUtils.getGovpayVersion(warName, govpayVersion, commit)+" fallita.", e);
		} 

		ctx.log();

		log.info("Inizializzazione "+StartupUtils.getGovpayVersion(warName, govpayVersion, commit)+" completata con successo."); 
		initialized = true;
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// Commit id
		String commit = (buildVersion.length() > 7) ? buildVersion.substring(0, 7) : buildVersion;
		MDC.put("cmd", "Shutdown");
		MDC.put("op", UUID.randomUUID().toString() );
		
		log.info("Shutdown "+StartupUtils.getGovpayVersion(warName, govpayVersion, commit)+" in corso...");
		
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
		
		log.info("Shutdown "+StartupUtils.getGovpayVersion(warName, govpayVersion, commit)+" completato.");
	}
}
