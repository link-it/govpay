package it.govpay.rs.v1.authentication.preauth.filter;

import javax.servlet.http.HttpServletRequest;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.core.utils.GovpayConfig;

public class HeaderPreAuthFilter extends org.openspcoop2.utils.service.authentication.preauth.filter.HeaderPreAuthFilter {
	
	private static Logger log = LoggerWrapperFactory.getLogger(HeaderPreAuthFilter.class);

	public HeaderPreAuthFilter() {
		super();
	}

	@Override
	protected String getPrincipalHeaderName() {
		String headerAuth = GovpayConfig.getInstance().getAutenticazioneHeaderNomeHeaderPrincipal();
		return headerAuth;
	}
	
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		Object tmp = super.getPreAuthenticatedPrincipal(request);
		log.debug("Letto Principal: ["+tmp+"]");
		return tmp;
	}
}
