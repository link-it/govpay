package it.govpay.backoffice.api.rs.v1.backoffice;

import it.govpay.rs.v1.beans.base.Intermediario;
import it.govpay.rs.v1.beans.base.IntermediarioPost;
import it.govpay.rs.v1.beans.base.Stazione;
import it.govpay.rs.v1.beans.base.StazionePost;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import it.govpay.rs.v1.controllers.base.IntermediariController;

import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/intermediari")

public class Intermediari extends BaseRsServiceV1{


	private IntermediariController controller = null;

	public Intermediari() {
		super("intermediari");
		this.controller = new IntermediariController(this.nomeServizio,this.log);
	}



/*
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response intermediariGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.intermediariGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }
*/

/*
    @GET
    @Path("/{idIntermediario}")
    
    @Produces({ "application/json" })
    public Response intermediariIdIntermediarioGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idIntermediario") String idIntermediario){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.intermediariIdIntermediarioGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idIntermediario);
    }
*/

/*
    @PUT
    @Path("/{idIntermediario}")
    @Consumes({ "application/json" })
    
    public Response intermediariIdIntermediarioPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idIntermediario") String idIntermediario, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.intermediariIdIntermediarioPUT(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idIntermediario, is);
    }
*/

/*
    @GET
    @Path("/{idIntermediario}/stazioni")
    
    @Produces({ "application/json" })
    public Response intermediariIdIntermediarioStazioniGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.intermediariIdIntermediarioStazioniGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }
*/

/*
    @GET
    @Path("/{idIntermediario}/stazioni/{idStazione}")
    
    @Produces({ "application/json" })
    public Response intermediariIdIntermediarioStazioniIdStazioneGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idIntermediario") String idIntermediario, @PathParam("idStazione") String idStazione){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.intermediariIdIntermediarioStazioniIdStazioneGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idIntermediario,  idStazione);
    }
*/

/*
    @PUT
    @Path("/{idIntermediario}/stazioni/{idStazione}")
    @Consumes({ "application/json" })
    
    public Response intermediariIdIntermediarioStazioniIdStazionePUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idIntermediario") String idIntermediario, @PathParam("idStazione") String idStazione, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.intermediariIdIntermediarioStazioniIdStazionePUT(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idIntermediario,  idStazione, is);
    }
*/

}
