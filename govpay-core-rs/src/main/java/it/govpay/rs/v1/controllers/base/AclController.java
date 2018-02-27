package it.govpay.rs.v1.controllers.base;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.model.IAutorizzato;



public class AclController extends it.govpay.rs.BaseController {

     public AclController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response aclGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, Boolean abilitato, Boolean ruolo, Boolean principal, Boolean servizio) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response aclIdGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String id) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response aclPOST(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String id, java.io.InputStream is) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response aclIdDELETE(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String id) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }


}


