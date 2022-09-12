package it.govpay.pagopa.v2.authentication.entrypoint;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component("basicAuthenticationEntryPoint")
public class BasicAuthenticationEntryPoint extends AbstractAuthenticationEntryPoint {

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
}
