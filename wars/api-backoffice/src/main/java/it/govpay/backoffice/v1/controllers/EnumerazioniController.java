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
			List<String> results = new ArrayList<>();
			
			for(Componente componente: EventoContext.Componente.values()) {
				switch(componente) {
				case API_BACKOFFICE:
				case API_ENTE:
				case API_PAGAMENTO:
				case API_PAGOPA:
				case API_PENDENZE:
				case API_RAGIONERIA:
					results.add(componente.toString());
					break;
				case API_USER:
				case API_WC: // eventi non presenti per questo componente
					break;
				default:
					break;
				}
			}

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(this.toJsonArray(results)),transactionId).build();
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
			
			ListaMappingTipiEventoDTO listaMappingTipiEventoDTO = new ListaMappingTipiEventoDTO(user);
			EnumerazioniDAO enumerazioniDAO = new EnumerazioniDAO(false);
			ListaMappingTipiEventoDTOResponse listaMappingTipiEventoDTOResponse = enumerazioniDAO.listaMappingTipiEvento(listaMappingTipiEventoDTO);
			List<Entry<String,String>> results = listaMappingTipiEventoDTOResponse.getResults();
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(results),transactionId).build();
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


