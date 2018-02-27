package it.govpay.pagamento.api.rs.v1.pagamenti;

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

import it.govpay.rs.v1.controllers.base.OperatoriController;

import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/operatori")

public class Operatori extends BaseRsServiceV1{


	private OperatoriController controller = null;

	public Operatori() {
		super("operatori");
		this.controller = new OperatoriController(this.nomeServizio,this.log);
	}



/*
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response operatoriGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("ordinamento") String ordinamento, @QueryParam("campi") String campi, @QueryParam("abilitato") Boolean abilitato){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.operatoriGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, ordinamento, campi, abilitato);
    }
*/

/*
    @DELETE
    @Path("/{principal}")
    
    
    public Response operatoriPrincipalDELETE(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("principal") String principal){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.operatoriPrincipalDELETE(this.getUser(), uriInfo, httpHeaders,  principal);
    }
*/

/*
    @GET
    @Path("/{principal}")
    
    @Produces({ "application/json" })
    public Response operatoriPrincipalGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("principal") String principal){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.operatoriPrincipalGET(this.getUser(), uriInfo, httpHeaders,  principal);
    }
*/

/*
    @PUT
    @Path("/{principal}")
    @Consumes({ "application/json" })
    
    public Response operatoriPrincipalPUT(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("principal") String principal, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.operatoriPrincipalPUT(this.getUser(), uriInfo, httpHeaders,  principal, is);
    }
*/

}


