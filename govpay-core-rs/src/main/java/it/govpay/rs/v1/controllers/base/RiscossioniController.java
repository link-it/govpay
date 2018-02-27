package it.govpay.rs.v1.controllers.base;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.rs.v1.beans.base.StatoRiscossione;



public class RiscossioniController extends it.govpay.rs.BaseController {

     public RiscossioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response riscossioniIdDominioIuvIurIndiceGET(String principal, List<String> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String iur, String indice) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response riscossioniGET(String principal, List<String> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String idA2A, String idPendenza, StatoRiscossione stato, String dataRiscossioneDa, String dataRiscossioneA, String tipo) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }


}


