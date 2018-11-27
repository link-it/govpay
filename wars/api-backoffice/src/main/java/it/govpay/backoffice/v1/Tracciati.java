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

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.controllers.TracciatiController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/tracciati")

public class Tracciati extends BaseRsServiceV1{


	private TracciatiController controller = null;

	public Tracciati() throws ServiceException {
		super("tracciati");
		this.controller = new TracciatiController(this.nomeServizio,this.log);
	}



    @GET
    @Path("/{id}/risposta")
    
    @Produces({ "application/octet-stream" })
    public Response tracciatiIdRispostaGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Long id){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.tracciatiIdRispostaGET(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response tracciatiGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.tracciatiGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina);
    }

    @GET
    @Path("/{id}/richiesta")
    
    @Produces({ "application/octet-stream" })
    public Response tracciatiIdRichiestaGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Long id){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.tracciatiIdRichiestaGET(this.getUser(), uriInfo, httpHeaders,  id);
    }

    @GET
    @Path("/{id}")
    
    @Produces({ "application/json" })
    public Response tracciatiIdGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") Long id){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.tracciatiIdGET(this.getUser(), uriInfo, httpHeaders,  id);
    }

}


