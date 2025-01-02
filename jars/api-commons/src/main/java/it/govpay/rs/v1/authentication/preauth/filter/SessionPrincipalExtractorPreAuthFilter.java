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
package it.govpay.rs.v1.authentication.preauth.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import it.govpay.core.autorizzazione.AuthorizationManager;


public class SessionPrincipalExtractorPreAuthFilter extends
AbstractPreAuthenticatedProcessingFilter {
	
	public static final String SESSION_PRINCIPAL_ATTRIBUTE_NAME = AuthorizationManager.SESSION_PRINCIPAL_ATTRIBUTE_NAME;
	public static final String SESSION_PRINCIPAL_OBJECT_ATTRIBUTE_NAME = AuthorizationManager.SESSION_PRINCIPAL_OBJECT_ATTRIBUTE_NAME;

	/**
	 * Public constructor which overrides the default AuthenticationDetails class to be
	 * used.
	 */
	public SessionPrincipalExtractorPreAuthFilter() {
		super();
	}

	// setAuthenticationDetailsSource(new WebSpherePreAuthenticatedWebAuthenticationDetailsSource());
	/**
	 * Return the WebSphere user name.
	 */
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpRequest) {
		if(httpRequest.getSession(false) != null) {
			HttpSession session = httpRequest.getSession(false);
			
			Object principal = session.getAttribute(SessionPrincipalExtractorPreAuthFilter.SESSION_PRINCIPAL_ATTRIBUTE_NAME);
			if (logger.isDebugEnabled()) {
				logger.debug("PreAuthenticated Govpay session principal: " + principal);
			}
			return principal;
		}

		return null;
	}

	/**
	 * For J2EE container-based authentication there is no generic way to retrieve the
	 * credentials, as such this method returns a fixed dummy value.
	 */
	protected Object getPreAuthenticatedCredentials(HttpServletRequest httpRequest) {
		return "N/A";
	}

}
