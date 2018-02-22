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


import it.govpay.rs.v1.beans.base.*;

import it.govpay.rs.v1.beans.base.Dominio;
import it.govpay.rs.v1.beans.base.DominioPost;
import it.govpay.rs.v1.beans.base.Entrata;
import it.govpay.rs.v1.beans.base.EntrataPost;
import it.govpay.rs.v1.beans.base.IbanAccredito;
import it.govpay.rs.v1.beans.base.IbanAccreditoPost;
import it.govpay.rs.v1.beans.base.UnitaOperativa;
import it.govpay.rs.v1.beans.base.UnitaOperativaPost;



public class DominiController extends it.govpay.rs.BaseController {
	
	public DominiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}


/*  
    public Response dominiGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina , Integer risultatiPerPagina , String ordinamento , String campi , Boolean abilitato , String idStazione ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response dominiGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina , Integer risultatiPerPagina , String ordinamento , String campi , Boolean abilitato , String idStazione ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response dominiIdDominioEntrateGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio , Integer pagina , Integer risultatiPerPagina , String ordinamento , String campi , Boolean abilitato ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response dominiIdDominioEntrateIdEntrataGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio , String idEntrata ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response dominiIdDominioEntrateIdEntrataPUT(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio , String idEntrata , EntrataPost entratapost ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response dominiIdDominioGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response dominiIdDominioGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response dominiIdDominioIbanAccreditoGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio , Integer pagina , Integer risultatiPerPagina , String ordinamento , String campi , Boolean abilitato ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response dominiIdDominioIbanAccreditoIbanAccreditoGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio , String ibanAccredito ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response dominiIdDominioIbanAccreditoIbanAccreditoPUT(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio , String ibanAccredito , IbanAccreditoPost ibanaccreditopost ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response dominiIdDominioPUT(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio , DominioPost dominiopost ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response dominiIdDominioUnitaOperativeGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio , Integer pagina , Integer risultatiPerPagina , String ordinamento , String campi , Boolean abilitato ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response dominiIdDominioUnitaOperativeGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio , Integer pagina , Integer risultatiPerPagina , String ordinamento , String campi , Boolean abilitato ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response dominiIdDominioUnitaOperativeIdUnitaOperativaGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio , String idUnitaOperativa ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response dominiIdDominioUnitaOperativeIdUnitaOperativaGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio , String idUnitaOperativa ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


/*  
    public Response dominiIdDominioUnitaOperativeIdUnitaOperativaPUT(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio , String idUnitaOperativa , UnitaOperativaPost unitaoperativapost ) {
        return new Response().status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


}


