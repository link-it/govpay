package it.govpay.rs;

import java.io.IOException;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.transport.http.AbstractCORSFilter;
import org.openspcoop2.utils.transport.http.CORSFilterConfiguration;
import org.slf4j.Logger;

import it.govpay.core.utils.GovpayConfig;

/***
 * OriginFilter filtro per la gestione del CORS.
 * 
 * configurazione definita nel file di properties: govpay.properties
 * le properties sono definite con il prefisso "it.govpay.configurazioneFiltroCors.",
 * vengono raggruppate per essere passate per la configurazione di CORSFilterConfiguration. 
 * 
 * cors.allowCredentials=true/false
 * 
 * cors.allowRequestHeaders=true/false
 * cors.allowHeaders=HDR1,...,HDRN
 * 
 * cors.allowRequestMethod=true/false
 * cors.allowMethods=METHOD1,...,METHODN
 * 
 * cors.allowRequestOrigin=true/false
 * cors.allowAllOrigin=true/false
 * cors.allowOrigins=http://origin1, ... ,http://originN
 * 
 * cors.exposeHeaders=HDR1,...,HDRN
 * 
 * cors.maxAge.cacheDisable=true/false
 * cors.maxAge.seconds=
 * 
 * @author pintori
 *
 */
public class OriginFilter extends AbstractCORSFilter {

	private static CORSFilterConfiguration CORS_FILTER_CONFIGURATION;
	private static synchronized void initCORSFilterConfiguration() throws IOException {
		if(CORS_FILTER_CONFIGURATION==null) {
			try {
				CORS_FILTER_CONFIGURATION = new CORSFilterConfiguration();
				CORS_FILTER_CONFIGURATION.init(GovpayConfig.getInstance().getCORSProperties());
			}catch(Exception e) {
				throw new IOException(e.getMessage(),e);
			}
		}
	}
	private static CORSFilterConfiguration getCORSFilterConfiguration() throws IOException {
		if(CORS_FILTER_CONFIGURATION==null) {
			initCORSFilterConfiguration();
		}
		return CORS_FILTER_CONFIGURATION;
	}

	@Override
	protected CORSFilterConfiguration getConfig() throws IOException {
		return getCORSFilterConfiguration();
	}

	@Override
	protected Logger getLog() {
		return LoggerWrapperFactory.getLogger(OriginFilter.class); 
	}
}
