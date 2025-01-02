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
package it.govpay.rs.v1.authentication;

import javax.servlet.http.HttpServletRequest;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import it.govpay.core.autorizzazione.beans.GovpayWebAuthenticationDetails;
import it.govpay.core.utils.GovpayConfig;

public class HeaderAuthenticationDetailsSource implements
		AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

	private static Logger log = LoggerWrapperFactory.getLogger(HeaderAuthenticationDetailsSource.class);

	// ~ Methods
	// ========================================================================================================

	/**
	 * @param context the {@code HttpServletRequest} object.
	 * @return the {@code WebAuthenticationDetails} containing information about the
	 * current request
	 */
	@Override
	public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
		log.debug("Lettura Headers in corso...");
		GovpayWebAuthenticationDetails details = new GovpayWebAuthenticationDetails(log, context, GovpayConfig.getInstance().getAutenticazioneHeaderElencoHeadersRequest());
		log.debug("Lettura Headers completata.");
		return details;
	}
}