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

import it.govpay.rs.v1.controllers.base.AclController;

import it.govpay.rs.v1.BaseRsServiceV1;


@Path("/acl")

public class Acl extends BaseRsServiceV1{


	private AclController controller = null;

	public Acl() {
		super("acl");
		this.controller = new AclController(this.nomeServizio,this.log);
	}



/*
    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    public Response aclGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("pagina") Integer pagina, @QueryParam("risultatiPerPagina") Integer risultatiPerPagina, @QueryParam("abilitato") Boolean abilitato, @QueryParam("ruolo") Boolean ruolo, @QueryParam("principal") Boolean principal, @QueryParam("servizio") Boolean servizio){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.aclGET(this.getUser(), uriInfo, httpHeaders, pagina, risultatiPerPagina, abilitato, ruolo, principal, servizio);
    }
*/

/*
    @DELETE
    @Path("/{id}")
    
    @Produces({ "application/json" })
    public Response aclIdDELETE(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String id){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.aclIdDELETE(this.getUser(), uriInfo, httpHeaders,  id);
    }
*/

/*
    @GET
    @Path("/{id}")
    
    @Produces({ "application/json" })
    public Response aclIdGET(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String id){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.aclIdGET(this.getUser(), uriInfo, httpHeaders,  id);
    }
*/

/*
    @POST
    @Path("/")
    @Consumes({ "application/json" })
    
    public Response aclPOST(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String id, java.io.InputStream is){
        this.controller.setRequestResponse(this.request, this.response);
        return this.controller.aclPOST(this.getUser(), uriInfo, httpHeaders,  id, is);
    }
*/

}


