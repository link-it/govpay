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
package it.govpay.rs.v1.authentication.preauth.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.core.utils.GovpayConfig;

public class HeaderPreAuthFilter extends org.openspcoop2.utils.service.authentication.preauth.filter.HeaderPreAuthFilter {
	
	private static Logger log = LoggerWrapperFactory.getLogger(HeaderPreAuthFilter.class);
	
	private List<String> nomiHeaders = null;

	public HeaderPreAuthFilter() {
		super();
		
		this.nomiHeaders = GovpayConfig.getInstance().getAutenticazioneHeaderNomeHeaderPrincipal();
		if(this.nomiHeaders == null || this.nomiHeaders.isEmpty()) {
			log.warn("Attenzione non e' stato impostato un header dal quale leggere il principal!");
		}
	}

	@Override
	protected String getPrincipalHeaderName() {
		String headerAuth = (this.nomiHeaders != null && this.nomiHeaders.size() > 0 ) ? this.nomiHeaders.get(0) : null;  
		return headerAuth;
	}
	
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		
		Object tmp = null;
		for (String header : nomiHeaders) {
			String headerValue = request.getHeader(header);
			log.debug("Letto Header: ["+header+"] Valore: ["+headerValue+"]");
			
			if(headerValue != null) {
				tmp = headerValue;
				break;
			}
		}

		return tmp;
	}
}
