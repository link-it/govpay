package it.govpay.rs.v1.authentication.entrypoint;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.Utilities;
import org.springframework.security.core.AuthenticationException;

import it.govpay.rs.v1.exception.CodiceEccezione;

public class BasicAuthenticationEntryPoint extends org.openspcoop2.utils.service.authentication.entrypoint.jaxrs.BasicAuthenticationEntryPoint {

	@Override
	protected Response getPayload(AuthenticationException authException, HttpServletResponse httpResponse) {
		if(Utilities.existsInnerException(authException, ServiceException.class)) {
			return CodiceEccezione.ERRORE_INTERNO.toFaultResponse(authException);
		}
		
		return CodiceEccezione.AUTENTICAZIONE.toFaultResponse(authException);
	}
}
