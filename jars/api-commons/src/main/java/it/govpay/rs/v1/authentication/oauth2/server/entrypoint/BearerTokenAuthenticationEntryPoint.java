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
package it.govpay.rs.v1.authentication.oauth2.server.entrypoint;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.Utilities;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import it.govpay.rs.v1.exception.CodiceEccezione;
import it.govpay.service.authentication.entrypoint.jaxrs.AbstractBasicAuthenticationEntryPoint;


/**
 * Un {@link AuthenticationEntryPoint} estende l'implementazione {@link org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint}
 * per includere una risposta nel formato GovPay nei casi non gestiti dalla procedura originale.
 *
 * @author Giuliano Pintori
 */
public class BearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private TimeZone timeZone = TimeZone.getDefault();
    private String timeZoneId = null;
    public String getTimeZoneId() {
            return this.timeZoneId;
    }
    public void setTimeZoneId(String timeZoneId) {
            this.timeZoneId = timeZoneId;
            this.timeZone = TimeZone.getTimeZone(timeZoneId);
    }

	private String realmName;

	/**
	 * Collect error details from the provided parameters and format according to RFC
	 * 6750, specifically {@code error}, {@code error_description}, {@code error_uri}, and
	 * {@code scope}.
	 * @param request that resulted in an <code>AuthenticationException</code>
	 * @param response so that the user agent can begin authentication
	 * @param authException that caused the invocation
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) {
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		Map<String, String> parameters = new LinkedHashMap<>();
		if (this.realmName != null) {
			parameters.put("realm", this.realmName);
		}
		if (authException instanceof OAuth2AuthenticationException) {
			OAuth2Error error = ((OAuth2AuthenticationException) authException).getError();
			parameters.put("error", error.getErrorCode());
			if (StringUtils.hasText(error.getDescription())) {
				parameters.put("error_description", error.getDescription());
			}
			if (StringUtils.hasText(error.getUri())) {
				parameters.put("error_uri", error.getUri());
			}
			if (error instanceof BearerTokenError) {
				BearerTokenError bearerTokenError = (BearerTokenError) error;
				if (StringUtils.hasText(bearerTokenError.getScope())) {
					parameters.put("scope", bearerTokenError.getScope());
				}
				status = ((BearerTokenError) error).getHttpStatus();
			}

			String wwwAuthenticate = computeWWWAuthenticateHeaderValue(parameters);
			response.addHeader(HttpHeaders.WWW_AUTHENTICATE, wwwAuthenticate);
			response.setStatus(status.value());
			return;
		}

		// altre eccezioni
		if(Utilities.existsInnerException(authException, ServiceException.class)) {
			AbstractBasicAuthenticationEntryPoint.fillResponse(response, CodiceEccezione.ERRORE_INTERNO.toFaultResponse(authException), this.timeZone);
			return;
		}

		AbstractBasicAuthenticationEntryPoint.fillResponse(response, CodiceEccezione.AUTENTICAZIONE.toFaultResponse(authException), this.timeZone);
	}

	/**
	 * Set the default realm name to use in the bearer token error response
	 * @param realmName
	 */
	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}

	private static String computeWWWAuthenticateHeaderValue(Map<String, String> parameters) {
		StringBuilder wwwAuthenticate = new StringBuilder();
		wwwAuthenticate.append("Bearer");
		if (!parameters.isEmpty()) {
			wwwAuthenticate.append(" ");
			int i = 0;
			for (Map.Entry<String, String> entry : parameters.entrySet()) {
				wwwAuthenticate.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
				if (i != parameters.size() - 1) {
					wwwAuthenticate.append(", ");
				}
				i++;
			}
		}
		return wwwAuthenticate.toString();
	}


}
