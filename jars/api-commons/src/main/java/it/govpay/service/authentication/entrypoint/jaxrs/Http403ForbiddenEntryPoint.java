package it.govpay.service.authentication.entrypoint.jaxrs;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response;

import org.openspcoop2.utils.service.fault.jaxrs.FaultCode;
import org.springframework.security.core.AuthenticationException;

public class Http403ForbiddenEntryPoint extends AbstractBasicAuthenticationEntryPoint {

	@Override
	protected void addCustomHeaders(HttpServletResponse httpResponse) {
		// Non voglio che appaia la finestra di Basic
		//httpResponse.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
	}
	
	@Override
	protected Response getPayload(AuthenticationException authException, HttpServletResponse httpResponse) {
		return FaultCode.AUTORIZZAZIONE.toFaultResponse(authException);
	}

}
