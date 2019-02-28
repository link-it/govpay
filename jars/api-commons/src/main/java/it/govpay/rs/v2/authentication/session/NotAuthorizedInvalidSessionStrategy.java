package it.govpay.rs.v2.authentication.session;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.openspcoop2.utils.service.authentication.entrypoint.jaxrs.AbstractBasicAuthenticationEntryPoint;
import org.openspcoop2.utils.service.fault.jaxrs.FaultCode;
import org.springframework.security.web.session.InvalidSessionStrategy;

public class NotAuthorizedInvalidSessionStrategy implements InvalidSessionStrategy {
	
	private boolean createNewSession = true;

	public NotAuthorizedInvalidSessionStrategy() {
	}

	public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (createNewSession) {
			request.getSession();
		}
		Response payload = FaultCode.AUTORIZZAZIONE.toFaultResponse("Sessione Scaduta");
		AbstractBasicAuthenticationEntryPoint.fillResponse(response, payload);
	}

	/**
	 * Determines whether a new session should be created before redirecting (to avoid
	 * possible looping issues where the same session ID is sent with the redirected
	 * request). Alternatively, ensure that the configured URL does not pass through the
	 * {@code SessionManagementFilter}.
	 *
	 * @param createNewSession defaults to {@code true}.
	 */
	public void setCreateNewSession(boolean createNewSession) {
		this.createNewSession = createNewSession;
	}
}
