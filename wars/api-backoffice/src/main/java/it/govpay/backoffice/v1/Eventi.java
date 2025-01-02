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

import it.govpay.backoffice.v1.controllers.EventiController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/eventi")

public class Eventi extends BaseRsServiceV1{


	private EventiController controller = null;

	public Eventi() {
		super("eventi");
		this.controller = new EventiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{id}")

    @Produces({ "application/json" })
    public Response getEvento(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String id){
        this.buildContext();
        return this.controller.getEvento(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @GET
    @Path("/")

    @Produces({ "application/json" })
    public Response findEventi(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina,
    		@QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("idDominio") String idDominio, @QueryParam("iuv") String iuv, @QueryParam("ccp") String ccp,
    		@QueryParam("idA2A") String idA2A, @QueryParam("idPendenza") String idPendenza, @QueryParam("idPagamento") String idPagamento, @QueryParam("esito") String esito, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA,
		@QueryParam("categoriaEvento") String categoriaEvento, @QueryParam("tipoEvento") String tipoEvento, @QueryParam("sottotipoEvento") String sottotipoEvento, @QueryParam("componente") String componente, @QueryParam("ruolo") String ruolo, @QueryParam("messaggi") Boolean messaggi, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati, @QueryParam("severitaDa") String severitaDa, @QueryParam("severitaA") String severitaA){
        this.buildContext();
        return this.controller.findEventi(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, idDominio, iuv, ccp, idA2A, idPendenza, idPagamento, esito, dataDa, dataA, categoriaEvento, tipoEvento, sottotipoEvento, componente, ruolo, messaggi, metadatiPaginazione, maxRisultati, severitaDa, severitaA);
    }

}


