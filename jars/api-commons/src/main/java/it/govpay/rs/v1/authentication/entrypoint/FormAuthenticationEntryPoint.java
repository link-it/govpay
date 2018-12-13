package it.govpay.rs.v1.authentication.entrypoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openspcoop2.utils.jaxrs.impl.authentication.entrypoint.AbstractBasicAuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import it.govpay.rs.v1.exception.CodiceEccezione;

public class FormAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse httpResponse, AuthenticationException authException) throws IOException, ServletException {
    	AbstractBasicAuthenticationEntryPoint.fillResponse(httpResponse, CodiceEccezione.AUTORIZZAZIONE.toFaultResponse(authException));
    }
}
