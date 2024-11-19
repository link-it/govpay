/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.rs.v3.service.context;

import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;

/**
 * ContextConfig configurazione del context per le Api V2
 * 
 * @author pintori
 *
 */
public class ContextConfigV3 extends org.openspcoop2.utils.service.context.ContextConfig {

	public static final Integer GOVPAY_VERSIONE_API = 3;
	public static final String GOVPAY_SERVICE_TYPE = GpContext.GOVPAY;
	
	public ContextConfigV3() {
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
			//donothing
		}
	}
}
