package it.govpay.rs.v1.authentication.handler;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.springframework.security.core.AuthenticationException;

import it.govpay.rs.v1.exception.CodiceEccezione;

public class DefaultAuthenticationFailureHandler extends org.openspcoop2.utils.jaxrs.impl.authentication.handler.DefaultAuthenticationFailureHandler {

	
	@Override
	public Response getPayload(AuthenticationException authException, HttpServletResponse httpResponse) {
		return CodiceEccezione.AUTORIZZAZIONE.toFaultResponse(authException);
	}
}
