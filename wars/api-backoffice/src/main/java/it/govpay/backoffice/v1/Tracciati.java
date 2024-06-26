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

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import it.govpay.backoffice.v1.controllers.TracciatiController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/tracciati")

public class Tracciati extends BaseRsServiceV1{


	private TracciatiController controller = null;

	public Tracciati() {
		super("tracciati");
		this.controller = new TracciatiController(this.nomeServizio,this.log);
	}

    @GET
    @Path("/{id}/risposta")

    @Produces({ "application/octet-stream" })
    public Response getMessaggioRispostaTracciato(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Long id){
        this.buildContext();
        return this.controller.getMessaggioRispostaTracciato(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @GET
    @Path("/")

    @Produces({ "application/json" })
    public Response findTracciati(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina){
        this.buildContext();
        return this.controller.findTracciati(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina);
    }

    @GET
    @Path("/{id}/richiesta")

    @Produces({ "application/octet-stream" })
    public Response getMessaggioRichiestaTracciato(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Long id){
        this.buildContext();
        return this.controller.getMessaggioRichiestaTracciato(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @GET
    @Path("/{id}")

    @Produces({ "application/json" })
    public Response getTracciato(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Long id){
        this.buildContext();
        return this.controller.getTracciato(this.getUser(), uriInfo, httpHeaders,  id);
    }

}


