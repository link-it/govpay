package it.govpay.rs.v1.authentication;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import it.govpay.core.autorizzazione.beans.GovpayWebAuthenticationDetails;
import it.govpay.core.utils.GovpayConfig;
 
public class HeaderAuthenticationDetailsSource implements
		AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

	// ~ Methods
	// ========================================================================================================

	/**
	 * @param context the {@code HttpServletRequest} object.
	 * @return the {@code WebAuthenticationDetails} containing information about the
	 * current request
	 */
	public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
		return new GovpayWebAuthenticationDetails(context, GovpayConfig.getInstance().getAutenticazioneHeaderElencoHeadersRequest());
	}
}