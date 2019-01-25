package it.govpay.rs.v1.authentication.entrypoint;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.springframework.security.core.AuthenticationException;

import it.govpay.rs.v1.exception.CodiceEccezione;

public class Http403ForbiddenEntryPoint extends org.openspcoop2.utils.service.authentication.entrypoint.jaxrs.Http403ForbiddenEntryPoint { 

	@Override
	protected Response getPayload(AuthenticationException authException, HttpServletResponse httpResponse) {
		return CodiceEccezione.AUTORIZZAZIONE.toFaultResponse(authException);
	}

	@Override
	protected void addCustomHeaders(HttpServletResponse httpResponse) {
		// Non voglio che appaia la finestra di Basic
		//httpResponse.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
	}

}
