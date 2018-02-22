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

import it.govpay.rs.v1.beans.base.Intermediario;
import it.govpay.rs.v1.beans.base.IntermediarioPost;
import it.govpay.rs.v1.beans.base.Stazione;
import it.govpay.rs.v1.beans.base.StazionePost;



public class IntermediariController extends it.govpay.rs.BaseController {


     public IntermediariController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }


/*  
    public Response intermediariGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina , Integer risultatiPerPagina , String ordinamento , String campi , Boolean abilitato ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response intermediariIdIntermediarioGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response intermediariIdIntermediarioPUT(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario , IntermediarioPost intermediariopost ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response intermediariIdIntermediarioStazioniGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina , Integer risultatiPerPagina , String ordinamento , String campi , Boolean abilitato ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response intermediariIdIntermediarioStazioniIdStazioneGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario , String idStazione ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response intermediariIdIntermediarioStazioniIdStazionePUT(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario , String idStazione , StazionePost stazionepost ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


}


