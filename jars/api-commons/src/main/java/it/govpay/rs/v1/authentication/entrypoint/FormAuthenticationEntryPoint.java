package it.govpay.rs.v1.authentication.entrypoint;

import java.io.IOException;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openspcoop2.utils.service.authentication.entrypoint.jaxrs.AbstractBasicAuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import it.govpay.rs.v1.exception.CodiceEccezione;

public class FormAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private TimeZone timeZone = TimeZone.getDefault();
    private String timeZoneId = null;
    public String getTimeZoneId() {
            return this.timeZoneId;
    }
    public void setTimeZoneId(String timeZoneId) {
            this.timeZoneId = timeZoneId;
            this.timeZone = TimeZone.getTimeZone(timeZoneId);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse httpResponse, AuthenticationException authException) throws IOException, ServletException {
    	AbstractBasicAuthenticationEntryPoint.fillResponse(httpResponse, CodiceEccezione.AUTENTICAZIONE.toFaultResponse(authException), this.timeZone);
    }
}
