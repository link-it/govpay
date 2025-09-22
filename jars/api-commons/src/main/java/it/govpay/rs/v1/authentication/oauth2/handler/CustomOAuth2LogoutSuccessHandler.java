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
package it.govpay.rs.v1.authentication.oauth2.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import it.govpay.core.beans.Costanti;
import it.govpay.core.utils.LogUtils;
import it.govpay.service.authentication.entrypoint.jaxrs.AbstractBasicAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response;

/**
 * Classe che gestisce il logout in un contesto OAuth2/OIDC generando il redirect per effettuare il logout sul server di autorizzazione
 * 
 *  @author pintori@link.it
 */
public class CustomOAuth2LogoutSuccessHandler implements LogoutSuccessHandler {

	private final Logger log = LoggerWrapperFactory.getLogger(CustomOAuth2LogoutSuccessHandler.class);

	private final String logoutUri;
	private final String postLogoutRedirectUri;

	public CustomOAuth2LogoutSuccessHandler(String logoutUri, String postLogoutRedirectUri) {
		this.logoutUri = logoutUri;
		this.postLogoutRedirectUri = postLogoutRedirectUri;
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		if (StringUtils.isNotEmpty(this.logoutUri) && StringUtils.isNotEmpty(this.postLogoutRedirectUri)) {
			String idToken = request.getParameter(Costanti.PARAM_ID_TOKEN_HINT);

			StringBuilder sb = new StringBuilder(this.logoutUri);
			sb.append("?post_logout_redirect_uri=" + URLEncoder.encode(this.postLogoutRedirectUri, StandardCharsets.UTF_8));

			if (StringUtils.isNotEmpty(idToken)) {
				sb.append("&id_token_hint=" + URLEncoder.encode(idToken, StandardCharsets.UTF_8));
			}

			String logoutUriWithParams = sb.toString();
			LogUtils.logTrace(log, "Logout RedirectUrl Oauth: {}", logoutUriWithParams);

			Map<String, String> responseMap = new HashMap<>();
			responseMap.put("logoutUrl", logoutUriWithParams); 

			Response responseEntity = Response.ok(responseMap).build();
			AbstractBasicAuthenticationEntryPoint.fillResponse(response, responseEntity, null);
		}

		response.setStatus(HttpServletResponse.SC_OK);
	}
}
