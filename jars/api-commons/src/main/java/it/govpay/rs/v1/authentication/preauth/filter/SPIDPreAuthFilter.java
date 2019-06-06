package it.govpay.rs.v1.authentication.preauth.filter;

import javax.servlet.http.HttpServletRequest;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.core.utils.GovpayConfig;
import it.govpay.rs.v1.authentication.SPIDAuthenticationDetailsSource;

public class SPIDPreAuthFilter extends org.openspcoop2.utils.service.authentication.preauth.filter.HeaderPreAuthFilter {
	
	private static final String TINIT_PREFIX = SPIDAuthenticationDetailsSource.TINIT_PREFIX;
	private static Logger log = LoggerWrapperFactory.getLogger(SPIDPreAuthFilter.class);

	public SPIDPreAuthFilter() {
		super();
	}

	@Override
	protected String getPrincipalHeaderName() {
		String headerAuth = GovpayConfig.getInstance().getAutenticazioneSPIDNomeHeaderPrincipal();
		return headerAuth;
	}
	
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		Object tmp = super.getPreAuthenticatedPrincipal(request);
		
		// estrazione del CF dal valore letto dall'header che e' nel formato TINIT-<CF>
		
		if(tmp != null) {
			String tmpCf = (String) tmp;
			
			int indexOfTINIT = tmpCf.indexOf(TINIT_PREFIX);
			if(indexOfTINIT > -1) {
				String cf = tmpCf.substring(indexOfTINIT + TINIT_PREFIX.length());
				log.debug("Letto Principal: ["+cf+"]");
				return cf;
			}
		}
		
		log.debug("Letto Principal: ["+tmp+"]");
		return tmp;
	}
}
