package it.govpay.rs.v1.controllers.base;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.http.HttpHeaders;
import org.slf4j.Logger;

import it.govpay.model.Ruolo;



public class EventiController extends it.govpay.rs.BaseController {

     public EventiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response eventiGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String idDominio, String iuv) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



}


