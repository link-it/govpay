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


import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import it.govpay.backoffice.v1.controllers.TipiPendenzaController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/tipiPendenza")

public class TipiPendenza extends BaseRsServiceV1{


	private TipiPendenzaController controller = null;

	public TipiPendenza() {
		super("tipiPendenza");
		this.controller = new TipiPendenzaController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{idTipoPendenza}")

    @Produces({ "application/json" })
    public Response getTipoPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idTipoPendenza") String idTipoPendenza){
        this.buildContext();
        return this.controller.getTipoPendenza(this.getUser(), idTipoPendenza);
    }

    @GET
    @Path("/")

    @Produces({ "application/json" })
    public Response findTipiPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("tipo") String tipo, @QueryParam("associati") Boolean associati, @QueryParam("form") Boolean form, @QueryParam("idTipoPendenza") String idTipoPendenza, @QueryParam("descrizione") String descrizione, @QueryParam("trasformazione") Boolean trasformazione, @QueryParam("nonAssociati") String nonAssociati, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
        this.buildContext();
        return this.controller.findTipiPendenza(this.getUser(), uriInfo, pagina, risultatiPerPagina, ordinamento, campi, abilitato, associati, form, idTipoPendenza, descrizione, trasformazione, nonAssociati, metadatiPaginazione, maxRisultati);
    }

    @PUT
    @Path("/{idTipoPendenza}")
    @Consumes({ "application/json" })

    public Response addTipoPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idTipoPendenza") String idTipoPendenza, java.io.InputStream is){
        this.buildContext();
        return this.controller.addTipoPendenza(this.getUser(), idTipoPendenza, is);
    }

}


