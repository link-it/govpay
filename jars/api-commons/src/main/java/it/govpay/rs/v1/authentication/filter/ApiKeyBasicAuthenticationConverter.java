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
package it.govpay.rs.v1.authentication.filter;

import jakarta.servlet.http.HttpServletRequest;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.Assert;

import it.govpay.core.utils.GovpayConfig;

/**
 * Based on {@link BasicAuthenticationConverter}
 *
 * @author Giuliano Pintori
 */
public class ApiKeyBasicAuthenticationConverter implements AuthenticationConverter {

	private static Logger log = LoggerWrapperFactory.getLogger(ApiKeyBasicAuthenticationConverter.class);

	private String apiIdHeader = null;
	private String apiKeyHeader = null;

	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

	public ApiKeyBasicAuthenticationConverter() {
		this(new WebAuthenticationDetailsSource());
	}

	public ApiKeyBasicAuthenticationConverter(
			AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
		this.authenticationDetailsSource = authenticationDetailsSource;

		this.apiIdHeader = GovpayConfig.getInstance().getAutenticazioneApiKeyNomeHeaderApiId();
		this.apiKeyHeader = GovpayConfig.getInstance().getAutenticazioneApiKeyNomeHeaderApiKey();

		if(this.apiIdHeader == null || this.apiKeyHeader == null) {
			log.warn("Attenzione non sono stati impostati gli header dal quale leggere le informazioni per l'autenticazione API-Key");
		}
	}

	public AuthenticationDetailsSource<HttpServletRequest, ?> getAuthenticationDetailsSource() {
		return this.authenticationDetailsSource;
	}

	public void setAuthenticationDetailsSource(
			AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
		Assert.notNull(authenticationDetailsSource, "AuthenticationDetailsSource required");
		this.authenticationDetailsSource = authenticationDetailsSource;
	}

	@Override
	public UsernamePasswordAuthenticationToken convert(HttpServletRequest request) {

		// estrazione dell'API-Id
		log.debug("Lettura del API-Id dall'Header [{}]...", this.apiIdHeader);
		String apiId = request.getHeader(this.apiIdHeader);
		log.debug("Letto API-Id: [{}]", apiId);

		// estrazione dell'API-Key
		log.debug("Lettura del API-Key dall'Header [{}]...", this.apiKeyHeader);
		String apiKey = request.getHeader(this.apiKeyHeader);
		log.debug("Letto API-Key: [{}]", apiKey);

		if (apiId == null || apiKey == null) {
			return null;
		}

		UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken.unauthenticated(apiId, apiKey);
		result.setDetails(this.authenticationDetailsSource.buildDetails(request));
		return result;
	}

}
