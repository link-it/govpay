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

import it.govpay.backoffice.v1.controllers.AvvisiController;
import it.govpay.rs.v1.BaseRsServiceV1;;


@Path("/avvisi")

public class Avvisi extends BaseRsServiceV1{


	private AvvisiController controller = null;

	public Avvisi() {
		super("avvisi");
		this.controller = new AvvisiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idDominio}/{numeroAvviso}")

    @Produces({ "application/json", "application/pdf" })
    public Response getAvviso(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("numeroAvviso") String numeroAvviso, @QueryParam("linguaSecondaria") String linguaSecondaria){
        this.buildContext();
        return this.controller.getAvviso(this.getUser(), uriInfo, httpHeaders,  idDominio,  numeroAvviso, linguaSecondaria);
    }

}


