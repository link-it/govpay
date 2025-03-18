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
package it.govpay.backoffice.v1.controllers;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.AclPost.ServizioEnum;
import it.govpay.backoffice.v1.beans.Connector.VersioneApiEnum;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;



public class EnumerazioniController extends BaseController {

     public EnumerazioniController(String nomeServizio,Logger log) {
    	 super(nomeServizio,log);
     }


    public Response findEnumerazioniServiziACL(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders ) {
		String methodName = "findEnumerazioniServiziACL";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
		try{
			List<String> results = new ArrayList<>();

			for(ServizioEnum serv: ServizioEnum.values()) {
				results.add(serv.toString());
			}

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(Status.OK).entity(this.toJsonArray(results)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }



    public Response findEnumerazioniVersioneConnettore(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders ) {
		String methodName = "findEnumerazioniVersioneConnettore";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
		try{
			List<String> results = new ArrayList<>();

			for(VersioneApiEnum serv: VersioneApiEnum.values()) {
				results.add(serv.toString());
			}
			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(Status.OK).entity(this.toJsonArray(results)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
    }



	/**
	 * @param results
	 * @return
	 * @throws UtilsException
	 */
	private String toJsonArray(List<String> results) throws UtilsException {
		try {
			return ConverterUtils.toJSON(results);
		} catch (IOException e) {
			throw new UtilsException(e);
		}
	}
}
