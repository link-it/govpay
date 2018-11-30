package it.govpay.rs.v1.authentication.preauth.filter;

import javax.servlet.http.HttpServletRequest;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import it.govpay.core.utils.GovpayConfig;

public class HeaderPreAuthFilter extends RequestHeaderAuthenticationFilter {

	private static Logger log = LoggerWrapperFactory.getLogger(HeaderPreAuthFilter.class);
	
	public HeaderPreAuthFilter() {
		super();
		log.info("Init in corso...");
		String headerAuth = GovpayConfig.getInstance().getHeaderAuth();
		log.info("Header utilizzato per la ricerca del principal ["+headerAuth+"].");
		this.setPrincipalRequestHeader(headerAuth);
		log.info("Init completata.");
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		return super.getPreAuthenticatedPrincipal(request);
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return super.getPreAuthenticatedCredentials(request);
	}

}
