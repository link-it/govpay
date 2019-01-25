package it.govpay.rs.v1.authentication.preauth.filter;

import it.govpay.core.utils.GovpayConfig;

public class HeaderPreAuthFilter extends org.openspcoop2.utils.service.authentication.preauth.filter.HeaderPreAuthFilter {

	public HeaderPreAuthFilter() {
		super();
	}

	@Override
	protected String getPrincipalHeaderName() {
		String headerAuth = GovpayConfig.getInstance().getHeaderAuth();
		return headerAuth;
	}
}
