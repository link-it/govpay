package it.govpay.pagopa.v2.authentication.entrypoint;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component("x509AuthenticationEntryPoint")
public class X509AuthenticationEntryPoint extends AbstractAuthenticationEntryPoint {

	@Override
	protected void addCustomHeaders(HttpServletResponse httpResponse) {
	}

}
