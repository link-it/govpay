package it.govpay.ragioneria.v1.controller;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.dao.anagrafica.dto.LeggiProfiloDTOResponse;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.ragioneria.v1.beans.Profilo;
import it.govpay.ragioneria.v1.beans.converter.ProfiloConverter;

public class ProfiloController extends BaseController {

     public ProfiloController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



     public Response profiloGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders) {
     	String methodName = "profiloGET";  
 		GpContext ctx = null;
 		String transactionId = null;
 		ByteArrayOutputStream baos= null;
 		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
 		try{
 			baos = new ByteArrayOutputStream();
 			this.logRequest(uriInfo, httpHeaders, methodName, baos);
 			
 			ctx =  GpThreadLocal.get();
 			transactionId = ctx.getTransactionId();
 			
 			// Parametri - > DTO Input

 			// INIT DAO
 			
 			UtentiDAO utentiDAO = new UtentiDAO();
 			
 			// CHIAMATA AL DAO
 			
 			LeggiProfiloDTOResponse leggiProfilo = utentiDAO.getProfilo(user);
 			
 			// CONVERT TO JSON DELLA RISPOSTA

 			Profilo profilo = ProfiloConverter.getProfilo(leggiProfilo);

 			this.logResponse(uriInfo, httpHeaders, methodName, profilo.toJSON(null), 200);
 			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
 			return this.handleResponseOk(Response.status(Status.OK).entity(profilo.toJSON(null)),transactionId).build();
 			
 		}catch (Exception e) {
 			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
 		} finally {
 			if(ctx != null) ctx.log();
 		}
     }

}


