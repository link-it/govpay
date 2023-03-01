package it.govpay.rs.v1.authentication.entrypoint;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.springframework.security.core.AuthenticationException;

import it.govpay.rs.v1.exception.CodiceEccezione;

public class X509AuthenticationEntryPoint extends org.openspcoop2.utils.service.authentication.entrypoint.jaxrs.X509AuthenticationEntryPoint {

	@Override
	protected Response getPayload(AuthenticationException authException, HttpServletResponse httpResponse) {
		return CodiceEccezione.AUTENTICAZIONE.toFaultResponse(authException);
	}

	@Override
	protected void addCustomHeaders(HttpServletResponse httpResponse) {
		// do nothing
	}

}
