package it.govpay.pagamento.api.listener;

import java.net.URI;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.openspcoop2.utils.logger.LoggerFactory;

import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.utils.tracciati.operazioni.OperazioneFactory;

public class InitListener implements ServletContextListener{

private static boolean initialized = false;
	
	public static boolean isInitialized() {
		return InitListener.initialized;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
//		try{
			
//			if(!isInitialized())
//				it.govpay.core.utils.GovpayConfig.newInstance();
//			
//			URI log4j2Config = null;
//			try {
//				log4j2Config = it.govpay.core.utils.GovpayConfig.getInstance().getLog4j2Config();
//				if(log4j2Config != null) {
//					LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
//					context.setConfigLocation(log4j2Config);
//				}
//			} catch (Exception e) {
//				LogManager.getLogger().warn("Errore durante la configurazione del Logger: " + e);
//			}
//			
//			GovpayConfig.newInstance4GovPay();
//			AnagraficaManager.newInstance(false);
//			ConnectionManager.initialize();
//			OperazioneFactory.init();
//		} catch(Exception e){
//			throw new RuntimeException("Inizializzazione di GovPay fallita: " + e, e);
//		}
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
