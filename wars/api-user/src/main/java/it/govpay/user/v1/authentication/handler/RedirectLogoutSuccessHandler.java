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
package it.govpay.user.v1.authentication.handler;

import java.io.IOException;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

import it.govpay.core.utils.GovpayConfig;
import it.govpay.user.v1.authentication.matcher.LogoutRequestMatcher;
import it.govpay.user.v1.authentication.matcher.LogoutRequestMatcher.Matcher;
import it.govpay.user.v1.authentication.matcher.LogoutRequestMatcher.SpringAntMatcher;
import it.govpay.user.v1.authentication.matcher.LogoutRequestMatcher.SubpathMatcher;

/**
 * Implementa il gestore del logout effettuato con successo fornendo la funzionalita' di redirect alla url indicata dall'eventuale URL-ID passato come parametro
 * Se non viene passato il parametro URL-ID allora la procedura si conclude con un 200OK
 *
 * @author pintori
 *
 */
public class RedirectLogoutSuccessHandler implements LogoutSuccessHandler{

	private static Logger log = LoggerWrapperFactory.getLogger(RedirectLogoutSuccessHandler.class);

	private final String pattern;
	private final boolean caseSensitive;
	private final Matcher matcher;

	public RedirectLogoutSuccessHandler(String pattern) {
		this(pattern, true);
	}

	public RedirectLogoutSuccessHandler(String pattern, boolean caseSensitive) {
		Assert.hasText(pattern, "Pattern cannot be null or empty");
		this.caseSensitive = caseSensitive;
		if (pattern.equals(LogoutRequestMatcher.MATCH_ALL) || pattern.equals("**")) {
			pattern = LogoutRequestMatcher.MATCH_ALL;
			this.matcher = null;
		}
		else {
			// If the pattern ends with {@code /**} and has no other wildcards or path
			// variables, then optimize to a sub-path match
			if (pattern.endsWith(LogoutRequestMatcher.MATCH_ALL)
					&& (pattern.indexOf('?') == -1 && pattern.indexOf('{') == -1
							&& pattern.indexOf('}') == -1)
					&& pattern.indexOf("*") == pattern.length() - 2) {
				this.matcher = new SubpathMatcher(
						pattern.substring(0, pattern.length() - 3), this.caseSensitive);
			}
			else {
				this.matcher = new SpringAntMatcher(pattern, this.caseSensitive);
			}
		}

		this.pattern = pattern;
	}

	public boolean doMatches(HttpServletRequest request) {
		String url = LogoutRequestMatcher.getRequestPath(request);
		
		if (this.pattern.equals(LogoutRequestMatcher.MATCH_ALL)) {
			log.debug("Request '{}' matched by universal pattern '/**'", url);
			return true;
		}

		log.debug("Checking match of request : '{}'; against '{}'", url, this.pattern);

		return this.matcher.matches(url);
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		log.debug("Esecuzione del logout in corso...");
		String url = LogoutRequestMatcher.getRequestPath(request);
		log.debug("Url invocata [{}]",url);
		String urlID = doMatches(request) ? decodificaUrlID(url) : null;
		log.debug("urlID [{}]", urlID);

		if(StringUtils.isBlank(urlID)) {
			response.setStatus(HttpServletResponse.SC_OK);
			log.debug("Esecuzione del logout completata con status 200 OK.");
		} else {

			Properties props = GovpayConfig.getInstance().getApiUserLogoutRedirectURLs();

			String redirectURL = props.getProperty(urlID);

			if(StringUtils.isBlank(redirectURL)) {
				response.setStatus(HttpServletResponse.SC_OK);
				log.debug("Esecuzione del logout completata con status 200 OK.");
			} else {
				response.setStatus(HttpServletResponse.SC_SEE_OTHER);
				response.setHeader("Location", redirectURL);
				log.debug("Esecuzione del logout completata con status 303 SeeOther, Location [{}].", redirectURL);
			}
		}
	}

	private String decodificaUrlID(String url) {
		String urlID = null;
		String [] tokensPath = org.springframework.util.StringUtils.tokenizeToStringArray(url, AntPathMatcher.DEFAULT_PATH_SEPARATOR, false, true);
		String [] tokensPattern = org.springframework.util.StringUtils.tokenizeToStringArray(this.pattern, AntPathMatcher.DEFAULT_PATH_SEPARATOR, false, true);
		// il pattern e il path invocato matchano grazie al confronto fatto con il metodo domatch, mi preoccupo di estrarre solo la parte finale
		String tokenTrovato = null;
		for (int i = 0; i < tokensPattern.length; i++) {
			if(this.pattern.endsWith(LogoutRequestMatcher.MATCH_ALL)) {
				// skip di tutti i token fino al doppio **
				if(!tokensPattern[i].equals("**")) {
					continue;
				}

				if(i < tokensPath.length) {
					tokenTrovato = tokensPath[i];
				}
			}
		}
		if(tokenTrovato != null) {
			urlID = url.substring(url.indexOf(tokenTrovato));
		}
		return urlID;
	}
}
