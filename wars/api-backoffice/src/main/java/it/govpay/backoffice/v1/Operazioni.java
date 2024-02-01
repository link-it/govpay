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

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.backoffice.v1.controllers.OperazioniController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/operazioni")

public class Operazioni extends BaseRsServiceV1{


	private OperazioniController controller = null;

	public Operazioni() {
		super("operazioni");
		this.controller = new OperazioniController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/")

    @Produces({ "application/json" })
    public Response findOperazioni(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina){
        this.buildContext();
        return this.controller.findOperazioni(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, null, null);
    }

    @GET
    @Path("/stato/{id}")

    @Produces({ "application/json" })
    public Response getStatoOperazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String id){
        this.buildContext();
        return this.controller.getStatoOperazione(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @GET
    @Path("/{idOperazione}")

    @Produces({ "application/json" })
    public Response getOperazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idOperazione") String idOperazione){
        this.buildContext();
        return this.controller.getOperazione(this.getUser(), uriInfo, httpHeaders,  idOperazione);
    }

}


