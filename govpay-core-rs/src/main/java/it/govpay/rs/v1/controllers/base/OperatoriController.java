package it.govpay.rs.v1.controllers.base;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.model.IAutorizzato;



public class OperatoriController extends it.govpay.rs.BaseController {

     public OperatoriController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response operatoriPrincipalPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String principal, java.io.InputStream is) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response operatoriPrincipalDELETE(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String principal) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response operatoriPrincipalGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String principal) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response operatoriGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }


}


