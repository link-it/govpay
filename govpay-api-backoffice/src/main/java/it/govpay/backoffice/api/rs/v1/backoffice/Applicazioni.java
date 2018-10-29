package it.govpay.backoffice.api.rs.v1.backoffice;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.rs.v1.costanti.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.rs.v1.controllers.base.ApplicazioniController;


@Path("/applicazioni")

public class Applicazioni extends BaseRsServiceV1{


	private ApplicazioniController controller = null;

	public Applicazioni() throws ServiceException {
		super("applicazioni");
		this.controller = new ApplicazioniController(this.nomeServizio,this.log);
	}

	@GET
    @Path("/")
    @Produces({ "application/json" })
    public Response applicazioniGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.applicazioniGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }

    @GET
    @Path("/{idA2A}")
    @Produces({ "application/json" })
    public Response applicazioniIdA2AGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.applicazioniIdA2AGET(this.getUser(), uriInfo, httpHeaders,  idA2A);
    }
    
    @PUT
    @Path("/{idA2A}")
    @Consumes({ "application/json" })
    public Response applicazioniIdA2APUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.applicazioniIdA2APUT(this.getUser(), uriInfo, httpHeaders,  idA2A, is);
    }

    @PATCH
    @Path("/{idA2A}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response applicazioniIdA2APATCH(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is, @PathParam("idA2A") String idA2A){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.applicazioniIdA2APATCH(this.getUser(), uriInfo, httpHeaders, is,  idA2A);
    }

}


