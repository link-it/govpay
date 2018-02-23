package it.govpay.backoffice.api.rs.v1.backoffice;

import it.govpay.rs.v1.beans.base.Ruolo;
import it.govpay.rs.v1.beans.base.RuoloPost;


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

import it.govpay.rs.v1.controllers.base.RuoliController;

import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/ruoli")

public class Ruoli extends BaseRsServiceV1{


	private RuoliController controller = null;

	public Ruoli() {
		super("ruoli");
		this.controller = new RuoliController(this.nomeServizio,this.log);
	}



/*
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response ruoliGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.ruoliGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders, pagina, risultatiPerPagina, abilitato);
    }
*/

/*
    @GET
    @Path("/{idRuolo}")
    
    @Produces({ "application/json" })
    public Response ruoliIdRuoloGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idRuolo") String idRuolo){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.ruoliIdRuoloGET(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idRuolo);
    }
*/

/*
    @PUT
    @Path("/{idRuolo}")
    @Consumes({ "application/json" })
    
    public Response ruoliIdRuoloPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idRuolo") String idRuolo, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
	return this.controller.ruoliIdRuoloPUT(this.getPrincipal(), this.getListaRuoli(), uriInfo, httpHeaders,  idRuolo, is);
    }
*/

}
