package it.govpay.rs.v1.controllers.base;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;



public class ProfiloController extends it.govpay.rs.BaseController {

     public ProfiloController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response profiloGET(String principal, List<String> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina) {
    	return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }


}


