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

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.backoffice.v1.controllers.EntrateController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/entrate")

public class Entrate extends BaseRsServiceV1{


	private EntrateController controller = null;

	public Entrate() {
		super("entrate");
		this.controller = new EntrateController(this.nomeServizio,this.log);
	}



    @PUT
    @Path("/{idEntrata}")
    @Consumes({ "application/json" })

    public Response addEntrata(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idEntrata") String idEntrata, java.io.InputStream is){
        this.buildContext();
        return this.controller.addEntrata(this.getUser(), uriInfo, httpHeaders,  idEntrata, is);
    }

    @GET
    @Path("/{idEntrata}")

    @Produces({ "application/json" })
    public Response getEntrata(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idEntrata") String idEntrata){
        this.buildContext();
        return this.controller.getEntrata(this.getUser(), uriInfo, httpHeaders,  idEntrata);
    }

    @GET
    @Path("/")

    @Produces({ "application/json" })
    public Response findEntrate(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
        this.buildContext();
        return this.controller.findEntrate(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, metadatiPaginazione, maxRisultati);
    }

}


