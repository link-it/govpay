package it.govpay.rs.v1.authentication.oauth2.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import it.govpay.core.autorizzazione.beans.GovPayLdapJwt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
		if (StringUtils.isNotEmpty(logoutUri) && StringUtils.isNotEmpty(postLogoutRedirectUri)) {
			String idToken = null;

			StringBuilder sb = new StringBuilder(this.logoutUri);
			if (authentication != null) {
				GovPayLdapJwt principal = (GovPayLdapJwt) authentication.getPrincipal();
				log.debug("principal: {}", principal.getUtenza().getPrincipal());
				if (principal instanceof OidcUser oidcUser) {
					idToken = oidcUser.getIdToken().getTokenValue();
				}
				if (idToken != null) {
					sb.append("?id_token_hint=" + URLEncoder.encode(idToken, StandardCharsets.UTF_8))
					.append("&post_logout_redirect_uri=" + URLEncoder.encode(postLogoutRedirectUri, StandardCharsets.UTF_8));
				}
				log.trace("Logout RedirectUrl Oauth: {}", logoutUri);
			} else {
				log.trace("No authentication information available.");
			}

			response.sendRedirect(sb.toString());

		}

		response.setStatus(HttpServletResponse.SC_OK);
	}
}
