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


import it.govpay.backoffice.v1.controllers.RicevuteController;
import it.govpay.rs.v1.BaseRsServiceV1;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;


@Path("/ricevute")

public class Ricevute extends BaseRsServiceV1 {


	private RicevuteController controller = null;

	public Ricevute() {
		super("ricevute");
		this.controller = new RicevuteController(this.nomeServizio,this.log);
	}



    @POST
    @Path("/")
    @Consumes({ "text/xml", "multipart/form-data" })
    @Produces({ "application/json" })
    public Response addRicevuta(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is){
    	this.buildContext();
        return this.controller.addRicevuta(this.getUser(), httpHeaders, is);
    }

}


