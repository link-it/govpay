package it.govpay.pagopa.v2.authentication.entrypoint;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component("http403ForbiddenEntryPoint")
public class Http403ForbiddenEntryPoint extends AbstractAuthenticationEntryPoint { 

	@Override
	protected void addCustomHeaders(HttpServletResponse httpResponse) {
		// Non voglio che appaia la finestra di Basic
		//httpResponse.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
	}

}
