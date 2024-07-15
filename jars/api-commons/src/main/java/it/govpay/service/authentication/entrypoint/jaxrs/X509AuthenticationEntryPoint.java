package it.govpay.service.authentication.entrypoint.jaxrs;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response;

import org.openspcoop2.utils.service.fault.jaxrs.FaultCode;
import org.springframework.security.core.AuthenticationException;

public class X509AuthenticationEntryPoint extends AbstractBasicAuthenticationEntryPoint {

	@Override
	protected void addCustomHeaders(HttpServletResponse httpResponse) {
	}
	
	@Override
	protected Response getPayload(AuthenticationException authException, HttpServletResponse httpResponse) {
		return FaultCode.AUTENTICAZIONE.toFaultResponse(authException);
	}

}
