package it.govpay.backoffice.v1.controllers;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.json.JSONUtils;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.databind.node.ArrayNode;

import it.govpay.backoffice.v1.beans.AclPost.ServizioEnum;
import it.govpay.backoffice.v1.beans.Connector.VersioneApiEnum;
import it.govpay.core.dao.anagrafica.EnumerazioniDAO;
import it.govpay.core.dao.anagrafica.dto.ListaMappingTipiEventoDTO;
import it.govpay.core.dao.anagrafica.dto.ListaMappingTipiEventoDTOResponse;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.EventoContext;
import it.govpay.core.utils.EventoContext.Componente;



public class EnumerazioniController extends BaseController {

     public EnumerazioniController(String nomeServizio,Logger log) {
    	 super(nomeServizio,log);
     }



    public Response findEnumerazioniComponentiEvento(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders ) {
    	String methodName = "findEnumerazioniComponentiEvento";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			throw new NotAuthorizedException("Operazione non piu' disponibile");
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }



    public Response findEnumerazioniLabelTipiEvento(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders ) {
    	String methodName = "findEnumerazioniLabelTipiEvento";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			throw new NotAuthorizedException("Operazione non piu' disponibile");
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }



    public Response findEnumerazioniServiziACL(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders ) {
		String methodName = "findEnumerazioniServiziACL";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			List<String> results = new ArrayList<>();
			
			for(ServizioEnum serv: ServizioEnum.values()) {
				results.add(serv.toString());
			}

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(this.toJsonArray(results)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }



    public Response findEnumerazioniVersioneConnettore(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders ) {
		String methodName = "findEnumerazioniVersioneConnettore";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			List<String> results = new ArrayList<>();
			
			for(VersioneApiEnum serv: VersioneApiEnum.values()) {
				results.add(serv.toString());
			}
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(this.toJsonArray(results)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
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


