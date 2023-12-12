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
package it.govpay.backoffice.v1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.backoffice.v1.controllers.InfoController;
import it.govpay.core.utils.InitConstants;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/info")

public class Info extends BaseRsServiceV1{


	private InfoController controller = null;

	public Info() {
		super("info");
		this.controller = new InfoController(this.nomeServizio,this.log);
		String commit = (InitConstants.GOVPAY_BUILD_NUMBER.length() > 7) ? InitConstants.GOVPAY_BUILD_NUMBER.substring(0, 7) : InitConstants.GOVPAY_BUILD_NUMBER;
		this.controller.setGovpayBuildNumber(commit);
		this.controller.setGovpayVersione(InitConstants.GOVPAY_VERSION);
	}

    @GET
    @Path("/")
    @Produces({ "application/json" })
    public Response getInfo(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders){
        this.buildContext();
        return this.controller.getInfo(this.getUser(), uriInfo, httpHeaders);
    }

}


