/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
