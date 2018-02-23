package it.govpay.rs.v1.controllers.base;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.io.File;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import it.govpay.model.Ruolo;

import org.slf4j.Logger;

import it.govpay.rs.v1.beans.base.*;

import it.govpay.rs.v1.beans.base.Applicazione;
import it.govpay.rs.v1.beans.base.ApplicazionePost;



public class ApplicazioniController extends it.govpay.rs.BaseController {

     public ApplicazioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }


/*  
    public Response applicazioniGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response applicazioniIdA2AGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response applicazioniIdA2APUT(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, java.io.InputStream is) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


}


