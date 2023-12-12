/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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



import java.text.MessageFormat;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.InfoGovPay;
import it.govpay.core.utils.GovpayConfig;



public class InfoController extends BaseController {

	private String govpayBuildNumber = null;
	private String govpayVersione = null;

	public InfoController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}

	public Response getInfo(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders ) {
		String methodName = "getInfo";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));
		try{
			InfoGovPay info = new InfoGovPay();
			info.setAmbiente(GovpayConfig.getInstance().getAmbienteDeploy());
			info.setAppName(GovpayConfig.getInstance().getAppName());
			info.setBuild(this.govpayBuildNumber);
			info.setVersione(this.govpayVersione);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
			return this.handleResponseOk(Response.status(Status.OK).entity(info.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}

	public String getGovpayBuildNumber() {
		return govpayBuildNumber;
	}

	public void setGovpayBuildNumber(String govpayBuildNumber) {
		this.govpayBuildNumber = govpayBuildNumber;
	}

	public String getGovpayVersione() {
		return govpayVersione;
	}

	public void setGovpayVersione(String govpayVersione) {
		this.govpayVersione = govpayVersione;
	}

}


