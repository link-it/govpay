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

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import it.govpay.backoffice.v1.controllers.PendenzeController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/pendenze")

public class Pendenze extends BaseRsServiceV1{


	private PendenzeController controller = null;

	public Pendenze() {
		super("pendenze");
		this.controller = new PendenzeController(this.nomeServizio,this.log);
	}

    @POST
    @Path("/tracciati")
    @Consumes({ "application/json", "multipart/form-data" })
    @Produces({ "application/json" })
    public Response addTracciatoPendenze(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is, @QueryParam("stampaAvvisi") Boolean stampaAvvisi){
        this.buildContext();
        return this.controller.addTracciatoPendenze(this.getUser(), uriInfo, httpHeaders, is, stampaAvvisi);
    }

    @GET
    @Path("/")
    @Produces({ "application/json" })
    public Response findPendenze(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("idDominio") String idDominio, @QueryParam("idA2A") String idA2A, @QueryParam("idDebitore") String idDebitore, @QueryParam("stato") String stato, @QueryParam("idPagamento") String idPagamento, @QueryParam("idPendenza") String idPendenza, @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("idTipoPendenza") List<String> idTipoPendenza, @QueryParam("direzione") String direzione, @QueryParam("divisione") String divisione, @QueryParam("iuv") String iuv, @QueryParam("mostraSpontaneiNonPagati") @DefaultValue(value="false") Boolean mostraSpontaneiNonPagati, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
        this.buildContext();
        return this.controller.findPendenze(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, idDominio, idA2A, idDebitore, stato, idPagamento, idPendenza, dataDa, dataA, idTipoPendenza, direzione, divisione, iuv, mostraSpontaneiNonPagati, metadatiPaginazione, maxRisultati);
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
        return this.controller.getPendenza(this.getUser(), uriInfo, httpHeaders,  idA2A,  idPendenza, true);
    }

    @GET
    @Path("/tracciati")
    @Produces({ "application/json" })
    public Response findTracciatiPendenze(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("idDominio") String idDominio, @QueryParam("statoTracciatoPendenza") String stato, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
        this.buildContext();
        return this.controller.findTracciatiPendenze(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, idDominio, stato, metadatiPaginazione, maxRisultati);
    }

    @PATCH
    @Path("/{idA2A}/{idPendenza}")
    @Consumes({ "application/json" })
    public Response updatePendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza, java.io.InputStream is){
        this.buildContext();
        return this.controller.updatePendenza(this.getUser(), uriInfo, httpHeaders,  idA2A,  idPendenza, is,true);
    }

    @POST
    @Path("/tracciati/{idDominio}")
    @Consumes({ "text/csv", "multipart/form-data" })
    @Produces({ "application/json" })
    public Response addTracciatoPendenze(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is, @PathParam("idDominio") String idDominio, @QueryParam("stampaAvvisi") Boolean stampaAvvisi){
        this.buildContext();
        return this.controller.addTracciatoPendenze(this.getUser(), uriInfo, httpHeaders, is,  idDominio, stampaAvvisi);
    }

    @POST
    @Path("/tracciati/{idDominio}/{idTipoPendenza}")
    @Consumes({ "text/csv", "multipart/form-data" })
    @Produces({ "application/json" })
    public Response addTracciatoPendenze(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is, @PathParam("idDominio") String idDominio, @PathParam("idTipoPendenza") String idTipoPendenza, @QueryParam("stampaAvvisi") Boolean stampaAvvisi){
        this.buildContext();
        return this.controller.addTracciatoPendenze(this.getUser(), uriInfo, httpHeaders, is,  idDominio, idTipoPendenza, stampaAvvisi);
    }

    @PUT
    @Path("/{idA2A}/{idPendenza}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "application/json" })
    public Response addPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, @PathParam("idPendenza") String idPendenza, java.io.InputStream is, @QueryParam("stampaAvviso") Boolean stampaAvviso){
        this.buildContext();
        return this.controller.addPendenza(this.getUser(), uriInfo, httpHeaders,  idA2A,  idPendenza, is, stampaAvviso);
    }

    @POST
    @Path("/")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "application/json" })
    public Response addPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is, @QueryParam("stampaAvviso") Boolean stampaAvviso){
        this.buildContext();
        return this.controller.addPendenza(this.getUser(), uriInfo, httpHeaders, is, stampaAvviso);
    }

    @GET
    @Path("/tracciati/{id}")
    @Produces({ "application/json" })
    public Response getTracciatoPendenze(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Integer id){
        this.buildContext();
        return this.controller.getTracciatoPendenze(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @GET
    @Path("/tracciati/{id}/richiesta")

    @Produces({ "application/json", "text/csv" })
    public Response getRichiestaTracciatoPendenze(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Integer id){
         this.buildContext();
        return this.controller.getRichiestaTracciatoPendenze(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @GET
    @Path("/tracciati/{id}/esito")
    @Produces({ "application/json", "text/csv" })
    public Response getEsitoTracciatoPendenze(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Integer id){
        this.buildContext();
        return this.controller.getEsitoTracciatoPendenze(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @GET
    @Path("/tracciati/{id}/stampe")
    @Produces({ "application/zip" })
    public Response getStampeTracciatoPendenze(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Integer id){
        this.buildContext();
        return this.controller.getStampeTracciatoPendenze(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @POST
    @Path("/{idDominio}/{idTipoPendenza}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "application/json" })
    public Response addPendenza(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idDominio") String idDominio, @PathParam("idTipoPendenza") String idTipoPendenza, java.io.InputStream is, @QueryParam("idUnitaOperativa") String idUnitaOperativa, @QueryParam("stampaAvviso") Boolean stampaAvviso){
        this.buildContext();
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.addPendenzaPOST(this.getUser(), uriInfo, httpHeaders,  idDominio,  idTipoPendenza, is, idUnitaOperativa, stampaAvviso);
    }

    @GET
    @Path("/tracciati/{id}/operazioni")
    @Produces({ "application/json" })
    public Response findOperazioniTracciatoPendenze(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Integer id, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
        this.buildContext();
        return this.controller.findOperazioniTracciatoPendenze(this.getUser(), uriInfo, httpHeaders,  id, pagina, risultatiPerPagina, metadatiPaginazione, maxRisultati);
    }

}


