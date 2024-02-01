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

import it.govpay.backoffice.v1.controllers.IntermediariController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/intermediari")

public class Intermediari extends BaseRsServiceV1{


	private IntermediariController controller = null;

	public Intermediari() {
		super("intermediari");
		this.controller = new IntermediariController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idIntermediario}")

    @Produces({ "application/json" })
    public Response getIntermediario(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idIntermediario") String idIntermediario){
        this.buildContext();
        return this.controller.getIntermediario(this.getUser(), uriInfo, httpHeaders,  idIntermediario);
    }

    @PUT
    @Path("/{idIntermediario}/stazioni/{idStazione}")
    @Consumes({ "application/json" })

    public Response addStazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idIntermediario") String idIntermediario, @PathParam("idStazione") String idStazione, java.io.InputStream is){
        this.buildContext();
        return this.controller.addStazione(this.getUser(), uriInfo, httpHeaders,  idIntermediario,  idStazione, is);
    }

    @GET
    @Path("/")

    @Produces({ "application/json" })
    public Response findIntermediari(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
        this.buildContext();
        return this.controller.findIntermediari(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato, metadatiPaginazione, maxRisultati);
    }

    @PUT
    @Path("/{idIntermediario}")
    @Consumes({ "application/json" })

    public Response addIntermediario(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idIntermediario") String idIntermediario, java.io.InputStream is){
        this.buildContext();
        return this.controller.addIntermediario(this.getUser(), uriInfo, httpHeaders,  idIntermediario, is);
    }

    @GET
    @Path("/{idIntermediario}/stazioni")

    @Produces({ "application/json" })
    public Response findStazioni(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idIntermediario") String idIntermediario, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
        this.buildContext();
        return this.controller.findStazioni(this.getUser(), uriInfo, httpHeaders,  idIntermediario, pagina, risultatiPerPagina, ordinamento, campi, abilitato, metadatiPaginazione, maxRisultati);
    }

    @GET
    @Path("/{idIntermediario}/stazioni/{idStazione}")

    @Produces({ "application/json" })
    public Response getStazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idIntermediario") String idIntermediario, @PathParam("idStazione") String idStazione){
        this.buildContext();
        return this.controller.getStazione(this.getUser(), uriInfo, httpHeaders,  idIntermediario,  idStazione);
    }

}


