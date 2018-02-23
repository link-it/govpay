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

import it.govpay.rs.v1.beans.base.Riscossione;
import it.govpay.rs.v1.beans.base.StatoRiscossione;



public class RiscossioniController extends it.govpay.rs.BaseController {
	
	public RiscossioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}


/*  
    public Response riscossioniGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina , Integer risultatiPerPagina , String ordinamento , String campi , String idDominio , String idA2A , String idPendenza , StatoRiscossione stato ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response riscossioniIdDominioIuvIurIndiceGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio , String iuv , String iur , String indice ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


}


