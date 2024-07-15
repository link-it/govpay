package it.govpay.service.authentication.entrypoint.jaxrs;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response;

import org.openspcoop2.utils.service.fault.jaxrs.FaultCode;
import org.springframework.security.core.AuthenticationException;

public class BasicAuthenticationEntryPoint extends AbstractBasicAuthenticationEntryPoint {

	private boolean wwwAuthenticate = true;
	
	public boolean isWwwAuthenticate() {
		return this.wwwAuthenticate;
	}
	public void setWwwAuthenticate(boolean wwwAuthenticate) {
		this.wwwAuthenticate = wwwAuthenticate;
	}

	@Override
	protected void addCustomHeaders(HttpServletResponse httpResponse) {
		if(this.wwwAuthenticate) {
			httpResponse.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
		}
	}
	
	@Override
	protected Response getPayload(AuthenticationException authException, HttpServletResponse httpResponse) {
		return FaultCode.AUTENTICAZIONE.toFaultResponse(authException);
	}

}
