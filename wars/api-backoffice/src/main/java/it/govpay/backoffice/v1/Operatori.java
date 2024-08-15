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
package it.govpay.backoffice.v1;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import it.govpay.backoffice.v1.controllers.OperatoriController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/operatori")

public class Operatori extends BaseRsServiceV1{


	private OperatoriController controller = null;

	public Operatori() {
		super("operatori");
		this.controller = new OperatoriController(this.nomeServizio,this.log);
	}



    @PUT
    @Path("/{principal}")
    @Consumes({ "application/json" })
    public Response addOperatore(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("principal") String principal, java.io.InputStream is){
        this.buildContext();
        return this.controller.addOperatore(this.getUser(), uriInfo, httpHeaders,  principal, is);
    }

    @PATCH
    @Path("/{principal}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response updateOperatore(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is, @PathParam("principal") String principal){
        this.buildContext();
        return this.controller.updateOperatore(this.getUser(), uriInfo, httpHeaders, is,  principal);
    }

    @GET
    @Path("/{principal}")
    @Produces({ "application/json" })
    public Response getOperatore(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("principal") String principal){
        this.buildContext();
        return this.controller.getOperatore(this.getUser(), uriInfo, httpHeaders,  principal);
    }

    @GET
    @Path("/")
    @Produces({ "application/json" })
    public Response findOperatori(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
        this.buildContext();
        return this.controller.findOperatori(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato, metadatiPaginazione, maxRisultati);
    }

}


