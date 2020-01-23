package it.govpay.user.listener;

import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.core.utils.EventiUtils;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.InitConstants;
import it.govpay.core.utils.StartupUtils;

public class InitListener implements ServletContextListener{

	private static Logger log = null;
	private static boolean initialized = false;
	private String warName = "GovPay-API-User";
	private String tipoServizioGovpay = GpContext.TIPO_SERVIZIO_GOVPAY_JSON;
	private String dominioAnagraficaManager = "it.govpay.cache.anagrafica.user";

	public static boolean isInitialized() {
		return InitListener.initialized;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// Commit id
		String commit = (InitConstants.GOVPAY_BUILD_NUMBER.length() > 7) ? InitConstants.GOVPAY_BUILD_NUMBER.substring(0, 7) : InitConstants.GOVPAY_BUILD_NUMBER;
		
		InputStream govpayPropertiesIS = InitListener.class.getResourceAsStream(GovpayConfig.PROPERTIES_FILE);
		URL log4j2URL = InitListener.class.getResource(GovpayConfig.LOG4J2_XML_FILE);
		InputStream msgDiagnosticiIS = InitListener.class.getResourceAsStream(GovpayConfig.MSG_DIAGNOSTICI_PROPERTIES_FILE);
		InputStream mappingTipiEventoPropertiesIS = InitListener.class.getResourceAsStream(EventiUtils.MAPPING_TIPI_EVENTO_PROPERTIES_FILE);
		IContext ctx = StartupUtils.startup(log, warName, InitConstants.GOVPAY_VERSION, commit, govpayPropertiesIS, log4j2URL, msgDiagnosticiIS, mappingTipiEventoPropertiesIS, tipoServizioGovpay);
		
		try {
			log = LoggerWrapperFactory.getLogger("boot");	
			StartupUtils.startupServices(log, warName, InitConstants.GOVPAY_VERSION, commit, ctx, dominioAnagraficaManager, GovpayConfig.getInstance());
		} catch (RuntimeException e) {
			log.error("Inizializzazione fallita", e);
			try {
				ctx.getApplicationLogger().log();
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: "+e1.getMessage(), e1);
			}
			throw e;
		} catch (Exception e) {
			log.error("Inizializzazione fallita", e);
			try {
				ctx.getApplicationLogger().log();
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: "+e1.getMessage(), e1);
			}
			throw new RuntimeException("Inizializzazione "+StartupUtils.getGovpayVersion(warName, InitConstants.GOVPAY_VERSION, commit)+" fallita.", e);
		} 

		try {
			ctx.getApplicationLogger().log();
		} catch (UtilsException e) {
			log.error("Errore durante il log dell'operazione: "+e.getMessage(), e);
		}

		log.info("Inizializzazione "+StartupUtils.getGovpayVersion(warName, InitConstants.GOVPAY_VERSION, commit)+" completata con successo."); 
		initialized = true;
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// Commit id
		String commit = (InitConstants.GOVPAY_BUILD_NUMBER.length() > 7) ? InitConstants.GOVPAY_BUILD_NUMBER.substring(0, 7) : InitConstants.GOVPAY_BUILD_NUMBER;
		
		MDC.put(MD5Constants.OPERATION_ID, "Shutdown");
		MDC.put(MD5Constants.TRANSACTION_ID, UUID.randomUUID().toString() );
		
		log.info("Shutdown "+StartupUtils.getGovpayVersion(warName, InitConstants.GOVPAY_VERSION, commit)+" in corso...");
		
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
		
		log.info("Shutdown "+StartupUtils.getGovpayVersion(warName, InitConstants.GOVPAY_VERSION, commit)+" completato.");
	}
}
