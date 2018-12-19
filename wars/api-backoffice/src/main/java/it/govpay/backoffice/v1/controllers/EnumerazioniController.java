package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.json.JSONUtils;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.databind.node.ArrayNode;

import it.govpay.backoffice.v1.beans.ServizioEnum;
import it.govpay.backoffice.v1.beans.VersioneApiEnum;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;



public class EnumerazioniController extends BaseController {

     public EnumerazioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response enumerazioniServiziACLGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders ) {
		String methodName = "enumerazioniServiziACLGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			List<String> results = new ArrayList<>();
			
			for(ServizioEnum serv: ServizioEnum.values()) {
				results.add(serv.toString());
			}

			this.logResponse(uriInfo, httpHeaders, methodName, this.toJsonArray(results), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(this.toJsonArray(results)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response enumerazioniVersioneConnettoreGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders ) {
		String methodName = "enumerazioniVersioneConnettoreGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			List<String> results = new ArrayList<>();
			
			for(VersioneApiEnum serv: VersioneApiEnum.values()) {
				results.add(serv.toString());
			}

			this.logResponse(uriInfo, httpHeaders, methodName, this.toJsonArray(results), 200);
			
			
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(this.toJsonArray(results)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



	/**
	 * @param results
	 * @return
	 * @throws UtilsException 
	 */
	private String toJsonArray(List<String> results) throws UtilsException {
		ArrayNode newArrayNode = JSONUtils.getInstance().newArrayNode();
		for(String str: results) {
			newArrayNode.add(str);
		}
		
		return JSONUtils.getInstance().toString(newArrayNode);
	}


}


