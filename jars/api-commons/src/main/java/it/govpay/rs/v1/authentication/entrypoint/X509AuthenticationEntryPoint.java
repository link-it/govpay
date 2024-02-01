/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.springframework.security.core.AuthenticationException;

import it.govpay.rs.v1.exception.CodiceEccezione;

public class X509AuthenticationEntryPoint extends org.openspcoop2.utils.service.authentication.entrypoint.jaxrs.X509AuthenticationEntryPoint {

	@Override
	protected Response getPayload(AuthenticationException authException, HttpServletResponse httpResponse) {
		return CodiceEccezione.AUTENTICAZIONE.toFaultResponse(authException);
	}

	@Override
	protected void addCustomHeaders(HttpServletResponse httpResponse) {
		// do nothing
	}

}
