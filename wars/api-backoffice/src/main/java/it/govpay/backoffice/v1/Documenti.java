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
package it.govpay.backoffice.v1;


import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import it.govpay.backoffice.v1.controllers.DocumentiController;
import it.govpay.rs.v1.BaseRsServiceV1;



@Path("/documenti")

public class Documenti extends BaseRsServiceV1{


	private DocumentiController controller = null;

	public Documenti() {
		super("documenti");
		this.controller = new DocumentiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idA2A}/{idDominio}/{numeroDocumento}/avvisi")

    @Produces({ "application/pdf" })
    public Response getAvvisiDocumento(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idDominio") String idDominio, @PathParam("numeroDocumento") String numeroDocumento, @QueryParam("linguaSecondaria") String linguaSecondaria, @QueryParam("numeriAvviso") List<String> numeriAvviso){
    	this.buildContext();
        return this.controller.getAvvisiDocumento(this.getUser(), httpHeaders,  idA2A,  idDominio,  numeroDocumento, linguaSecondaria, numeriAvviso);
    }

}


