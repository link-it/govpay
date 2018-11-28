package it.govpay.core.springsec.authentication.entrypoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import it.govpay.core.beans.Costanti;

public abstract class AbstractBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
	
	private String realname = Costanti.GOVPAY;

    public String getRealname() {
            return this.realname;
    }
    public void setRealname(String realname) {
            this.realname = realname;
    }

	protected abstract void fillResponse(AuthenticationException authException, javax.servlet.http.HttpServletResponse httpResponse);

	protected abstract void addCustomHeaders(javax.servlet.http.HttpServletResponse httpResponse);
	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException, ServletException {
		this.addCustomHeaders(response);
		this.fillResponse(authException, response);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setRealmName(this.realname);
		super.afterPropertiesSet();
	}
}
