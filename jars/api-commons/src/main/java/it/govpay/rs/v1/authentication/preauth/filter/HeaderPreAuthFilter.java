package it.govpay.rs.v1.authentication.preauth.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.core.utils.GovpayConfig;

public class HeaderPreAuthFilter extends org.openspcoop2.utils.service.authentication.preauth.filter.HeaderPreAuthFilter {
	
	private static Logger log = LoggerWrapperFactory.getLogger(HeaderPreAuthFilter.class);
	
	private List<String> nomiHeaders = null;

	public HeaderPreAuthFilter() {
		super();
		
		this.nomiHeaders = GovpayConfig.getInstance().getAutenticazioneHeaderNomeHeaderPrincipal();
	}

	@Override
	protected String getPrincipalHeaderName() {
		String headerAuth = (this.nomiHeaders != null && this.nomiHeaders.size() > 0 ) ? this.nomiHeaders.get(0) : null;  
		return headerAuth;
	}
	
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		
		Object tmp = null;
		for (String header : nomiHeaders) {
			String headerValue = request.getHeader(header);
			log.debug("Letto Header [:"+header+"] Valore: ["+headerValue+"]");
			
			if(headerValue != null) {
				tmp = headerValue;
				break;
			}
		}

		return tmp;
	}
}
