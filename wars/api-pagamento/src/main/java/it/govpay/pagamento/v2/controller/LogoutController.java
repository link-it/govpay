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
package it.govpay.pagamento.v2.controller;


import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;




public class LogoutController extends BaseController {

     public LogoutController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



     public Response logout(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders ) {
 		String methodName = "logout";
 		String transactionId = ContextThreadLocal.get().getTransactionId();
 		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
 		try{
 			if(this.request.getSession() != null) {
 				HttpSession session = this.request.getSession();
 				session.invalidate();
 			}

 			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
 			return this.handleResponseOk(Response.ok(),transactionId).build();
 		}catch (Exception e) {
 			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
 		} finally {
 			this.logContext(ContextThreadLocal.get());
 		}
 	}


}
