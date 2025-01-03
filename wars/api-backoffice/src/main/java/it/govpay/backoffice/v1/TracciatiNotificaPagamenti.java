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
package it.govpay.backoffice.v1;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.backoffice.v1.controllers.TracciatiNotificaPagamentiController;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/tracciatiNotificaPagamenti")

public class TracciatiNotificaPagamenti extends BaseRsServiceV1{


	private TracciatiNotificaPagamentiController controller = null;

	public TracciatiNotificaPagamenti() {
		super("tracciatiNotificaPagamenti");
		this.controller = new TracciatiNotificaPagamentiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{id}")

    @Produces({ "application/zip" })
    public Response getTracciatoNotificaPagamenti(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Long id, @QueryParam("secID") String secID){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.getTracciatoNotificaPagamenti(this.getUser(), uriInfo, httpHeaders,  id, secID);
    }

}


