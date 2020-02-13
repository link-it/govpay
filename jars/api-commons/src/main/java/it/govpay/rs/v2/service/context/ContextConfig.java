package it.govpay.rs.v2.service.context;

import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;

/**
 * ContextConfig configurazione del context per le Api V1
 * 
 * @author pintori
 *
 */
public class ContextConfig extends org.openspcoop2.utils.service.context.ContextConfig {
	
	public static final Integer GOVPAY_VERSIONE_API = 2;
	public static final String GOVPAY_SERVICE_TYPE = GpContext.GovPay;

	public ContextConfig() {
		super();
		
		try {
			String clusterId = GovpayConfig.getInstance().getClusterId();
			if(clusterId != null)
				this.setClusterId(clusterId);
			else 
				this.setClusterId(GovpayConfig.getInstance().getAppName());
			
			this.setDump(GovpayConfig.getInstance().isScritturaDumpFileEnabled());
			this.setEmitTransaction(GovpayConfig.getInstance().isScritturaDiagnosticiFileEnabled());
			this.setServiceType(GOVPAY_SERVICE_TYPE);
			this.setServiceVersion(GOVPAY_VERSIONE_API);
		} catch(Exception e) {
			
		}
	}
}
