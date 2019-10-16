package it.govpay.rs.v1.authentication.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.openspcoop2.utils.service.authentication.entrypoint.jaxrs.AbstractBasicAuthenticationEntryPoint;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import it.govpay.rs.v1.exception.CodiceEccezione;

public class NotAuthorizedSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

	@Override
	public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
		HttpServletResponse response = event.getResponse();
		Response payload = CodiceEccezione.AUTENTICAZIONE.toFaultResponse("Sessione Scaduta");
		AbstractBasicAuthenticationEntryPoint.fillResponse(response, payload);
	}

}
