package it.govpay.backoffice.v1;

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

import it.govpay.backoffice.v1.controllers.ApplicazioniController;
import it.govpay.core.beans.Costanti;
import it.govpay.rs.v1.BaseRsServiceV1;


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
    public Response findApplicazioni(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato, @QueryParam("idA2A") String idA2A, @QueryParam("principal") String principal, @QueryParam("metadatiPaginazione") @DefaultValue(value="true") Boolean metadatiPaginazione, @QueryParam("maxRisultati") @DefaultValue(value="true") Boolean maxRisultati){
        this.buildContext();
        return this.controller.findApplicazioni(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato, idA2A, principal, metadatiPaginazione, maxRisultati);
    }

    @GET
    @Path("/{idA2A}")
    @Produces({ "application/json" })
    public Response getApplicazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A){
        this.buildContext();
        return this.controller.getApplicazione(this.getUser(), uriInfo, httpHeaders,  idA2A);
    }
    
    @PUT
    @Path("/{idA2A}")
    @Consumes({ "application/json" })
    public Response addApplicazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("idA2A") String idA2A, java.io.InputStream is){
        this.buildContext();
        return this.controller.addApplicazione(this.getUser(), uriInfo, httpHeaders,  idA2A, is);
    }

    @PATCH
    @Path("/{idA2A}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    public Response updateApplicazione(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, java.io.InputStream is, @PathParam("idA2A") String idA2A){
        this.buildContext();
        return this.controller.updateApplicazione(this.getUser(), uriInfo, httpHeaders, is,  idA2A);
    }

}


