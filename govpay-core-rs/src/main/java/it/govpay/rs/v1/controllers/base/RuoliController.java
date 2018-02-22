package it.govpay.rs.v1.controllers.base;

import org.slf4j.Logger;
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

import it.govpay.rs.v1.beans.base.RuoloPost;



public class RuoliController extends it.govpay.rs.BaseController {
	
	public RuoliController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}


/*  
    public Response ruoliGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina , Integer risultatiPerPagina , Boolean abilitato ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response ruoliIdRuoloGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idRuolo ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response ruoliIdRuoloPUT(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idRuolo , RuoloPost ruolopost ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


}


