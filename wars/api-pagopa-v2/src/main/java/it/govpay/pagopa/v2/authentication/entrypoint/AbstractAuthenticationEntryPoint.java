package it.govpay.pagopa.v2.authentication.entrypoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerExceptionResolver;

public abstract class AbstractAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {
	
	public final static String GOVPAY_REALM_NAME = "GovPay";
	
	private String realmName = GOVPAY_REALM_NAME;

	@Override
	public void afterPropertiesSet() {
		Assert.hasText(this.realmName, "realmName must be specified");
	}
	
	public String getRealmName() {
		return this.realmName;
	}

	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}
	
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
	
	protected abstract void addCustomHeaders(javax.servlet.http.HttpServletResponse httpResponse);
	
	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException)  {
		this.addCustomHeaders(response);
		this.resolver.resolveException(request, response, null, authException);
	}
}
