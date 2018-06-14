package it.govpay.backoffice.api.rs.v1.backoffice;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
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
import it.govpay.rs.v1.controllers.base.OperatoriController;


@Path("/operatori")

public class Operatori extends BaseRsServiceV1{


	private OperatoriController controller = null;

	public Operatori() throws ServiceException {
		super("operatori");
		this.controller = new OperatoriController(this.nomeServizio,this.log);
	}



    @PUT
    @Path("/{principal}")
    @Consumes({ "application/json" })
    
    public Response operatoriPrincipalPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("principal") String principal, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.operatoriPrincipalPUT(this.getUser(), uriInfo, httpHeaders,  principal, is);
    }

    @DELETE
    @Path("/{principal}")
    
    
    public Response operatoriPrincipalDELETE(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("principal") String principal){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.operatoriPrincipalDELETE(this.getUser(), uriInfo, httpHeaders,  principal);
    }

    @GET
    @Path("/{principal}")
    
    @Produces({ "application/json" })
    public Response operatoriPrincipalGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("principal") String principal){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.operatoriPrincipalGET(this.getUser(), uriInfo, httpHeaders,  principal);
    }

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response operatoriGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam(value=Costanti.PARAMETRO_PAGINA) @DefaultValue(value="1") Integer pagina, @QueryParam(value=Costanti.PARAMETRO_RISULTATI_PER_PAGINA) @DefaultValue(value="25") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.operatoriGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }

}


