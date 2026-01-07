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
package it.govpay.pagamento.v2;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import it.govpay.core.beans.Costanti;
import it.govpay.pagamento.v2.controller.PendenzeController;
import it.govpay.rs.v2.BaseRsServiceV2;


@Path("/pendenze")

public class Pendenze extends BaseRsServiceV2{


	private PendenzeController controller = null;

	public Pendenze() {
		super("pendenze");
		this.controller = new PendenzeController(this.nomeServizio,this.log);
	}



    @POST
    @Path("/{idDominio}/{idTipoPendenza}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "application/json" })
    public Response addPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idTipoPendenza") String idTipoPendenza, java.io.InputStream is, @QueryParam("idA2A") String idA2A, @QueryParam("idPendenza") String idPendenza){
        this.buildContext();
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.addPendenza(this.getUser(), uriInfo, httpHeaders,  idDominio,  idTipoPendenza, is, idA2A, idPendenza);
    }

    @GET
    @Path("/")

    @Produces({ "application/json" })
    public Response findPendenze(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi,  @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("idDominio") String idDominio, @QueryParam("idA2A") String idA2A, @QueryParam("idDebitore") String idDebitore, @QueryParam("stato") String stato, @QueryParam("idPagamento") String idPagamento, @QueryParam("direzione") String direzione, @QueryParam("divisione") String divisione, @QueryParam("mostraSpontaneiNonPagati") @DefaultValue(value="false") Boolean mostraSpontaneiNonPagati, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
        this.buildContext();
        return this.controller.pendenzeGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, dataDa, dataA, idDominio, idA2A, idDebitore, stato, idPagamento, direzione, divisione, mostraSpontaneiNonPagati, metadatiPaginazione, maxRisultati);
    }

    @GET
    @Path("/byAvviso/{idDominio}/{numeroAvviso}")

    @Produces({ "application/json" })
    public Response getPendenzaByAvviso(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("numeroAvviso") String numeroAvviso){
        this.buildContext();
        return this.controller.getPendenzaByAvviso(this.getUser(), uriInfo, httpHeaders,  idDominio,  numeroAvviso);
    }

    @GET
    @Path("/{idA2A}/{idPendenza}")

    @Produces({ "application/json" })
    public Response getPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza){
        this.buildContext();
        return this.controller.pendenzeIdA2AIdPendenzaGET(this.getUser(), uriInfo, httpHeaders,  idA2A,  idPendenza);
    }

}


