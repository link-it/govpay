package it.govpay.rs.v1.controllers.base;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;



public class AvvisiController extends it.govpay.rs.BaseController {

     public AvvisiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response avvisiIdDominioIuvGET(String principal, List<String> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response avvisiIdDominioPOST(String principal, List<String> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, java.io.InputStream is) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }


}


