package it.govpay.backoffice.v1.controllers;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication; 



public class NotificheController extends BaseController {

     public NotificheController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response findNotifiche(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String dataDa, String dataA, String stato, String tipo) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }


}


