/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.user.v1.controller;

import java.net.URI;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import it.govpay.core.utils.GovpayConfig;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

public class LogoutController extends BaseController {

	public LogoutController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}


	public Response logout(UriInfo uriInfo, String urlID) {
		String methodName = "logout";
		String transactionId = this.context.getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
		try{
			if(this.request.getSession() != null) {
				HttpSession session = this.request.getSession();
				session.invalidate();
			}

			if(urlID == null) {
				this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
				return this.handleResponseOk(Response.ok(),transactionId).build();
			} else {
				Properties props = GovpayConfig.getInstance().getApiUserLogoutRedirectURLs();

				String redirectURL = props.getProperty(urlID);

				if(StringUtils.isBlank(redirectURL)) {
					this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
					return this.handleResponseOk(Response.ok(),transactionId).build();
				} else {
					MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters(false);
					UriBuilder target = UriBuilder.fromUri(new URI(redirectURL));

					for (Entry<String, List<String>> next : queryParameters.entrySet()) {
						this.log.debug("Aggiungo queryParam {}: {}",next.getKey(), next.getValue());
						target = target.queryParam(next.getKey(), next.getValue().get(0));
					}
					redirectURL = target.build().toString();
					this.log.info("Esecuzione {} completata con redirect verso la URL [{}].", methodName, redirectURL);
					return this.handleResponseOk(Response.seeOther(new URI(redirectURL)),transactionId).build();
				}
			}
		}catch (Exception e) {
			return this.handleException(methodName, e, transactionId);
		} finally {
			this.logContext(this.context);
		}
	}

}
