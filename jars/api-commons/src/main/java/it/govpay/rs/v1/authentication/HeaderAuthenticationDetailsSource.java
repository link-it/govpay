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
	public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
		log.debug("Lettura Headers in corso...");
		GovpayWebAuthenticationDetails details = new GovpayWebAuthenticationDetails(log, context, GovpayConfig.getInstance().getAutenticazioneHeaderElencoHeadersRequest());
		log.debug("Lettura Headers completata.");
		return details;
	}
}