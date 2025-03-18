/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.rs;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

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

	private static CORSFilterConfiguration corsFilterConfiguration;
	private static synchronized void initCORSFilterConfiguration() throws IOException {
		if(corsFilterConfiguration==null) {
			try {
				corsFilterConfiguration = new CORSFilterConfiguration();
				corsFilterConfiguration.init(GovpayConfig.getInstance().getCORSProperties());
			}catch(Exception e) {
				throw new IOException(e.getMessage(),e);
			}
		}
	}
	private static CORSFilterConfiguration getCORSFilterConfiguration() throws IOException {
		if(corsFilterConfiguration==null) {
			initCORSFilterConfiguration();
		}
		return corsFilterConfiguration;
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
