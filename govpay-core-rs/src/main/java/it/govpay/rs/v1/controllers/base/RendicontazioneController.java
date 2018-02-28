package it.govpay.rs.v1.controllers.base;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.model.IAutorizzato;




public class RendicontazioneController extends it.govpay.rs.BaseController {

     public RendicontazioneController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response rendicontazioneIdFlussoGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idFlusso) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response rendicontazioneGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String idDominio) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }


}


