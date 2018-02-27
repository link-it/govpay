package it.govpay.rs.v1.controllers.base;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.model.IAutorizzato;



public class EntrateController extends it.govpay.rs.BaseController {

     public EntrateController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response entrateIdEntrataPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idEntrata, java.io.InputStream is) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response entrateIdEntrataGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idEntrata) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response entrateGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }


}


