package it.govpay.rs.v1.controllers.base;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.utils.GovpayConfig;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseController;



public class RuoliController extends BaseController {

     public RuoliController(String nomeServizio,Logger log) {
 		super(nomeServizio,log, GovpayConfig.GOVPAY_PAGAMENTI_OPEN_API_FILE_NAME);
     }

    public Response ruoliGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response ruoliIdRuoloGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idRuolo) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response ruoliIdRuoloPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idRuolo, java.io.InputStream is) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }


}


