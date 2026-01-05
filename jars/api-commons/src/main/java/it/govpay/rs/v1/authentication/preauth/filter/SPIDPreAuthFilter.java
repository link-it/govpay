/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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

import jakarta.servlet.http.HttpServletRequest;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.core.utils.GovpayConfig;
import it.govpay.rs.v1.authentication.SPIDAuthenticationDetailsSource;

public class SPIDPreAuthFilter extends org.openspcoop2.utils.service.authentication.preauth.filter.HeaderPreAuthFilter {

	private static final String TINIT_PREFIX = SPIDAuthenticationDetailsSource.TINIT_PREFIX;
	private static Logger log = LoggerWrapperFactory.getLogger(SPIDPreAuthFilter.class);

	public SPIDPreAuthFilter() {
		super();
	}

	@Override
	protected String getPrincipalHeaderName() {
		return GovpayConfig.getInstance().getAutenticazioneSPIDNomeHeaderPrincipal();
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		Object tmp = super.getPreAuthenticatedPrincipal(request);

		// estrazione del CF dal valore letto dall'header che e' nel formato TINIT-<CF>
		log.debug("Lettura del principal SPID dall'Header [{}]...", getPrincipalHeaderName());

		if(tmp != null) {
			String tmpCf = (String) tmp;

			int indexOfTINIT = tmpCf.indexOf(TINIT_PREFIX);
			if(indexOfTINIT > -1) {
				String cf = tmpCf.substring(indexOfTINIT + TINIT_PREFIX.length());
				log.debug("Letto Principal: [{}]", cf);
				return cf;
			}
		}

		log.debug("Letto Principal: [{}]", tmp);
		return tmp;
	}
}
