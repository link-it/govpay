package it.govpay.backoffice.v1.controllers;

import java.text.MessageFormat;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.exceptions.NotAuthorizedException;



public class TracciatiController extends BaseController {

     public TracciatiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response findTracciati(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina) {
		String methodName = "findTracciati";  
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



    public Response getTracciato(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Long id) {
		String methodName = "getTracciato";  
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



    public Response getMessaggioRichiestaTracciato(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Long id) {
		String methodName = "getMessaggioRichiestaTracciato";  
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



    public Response getMessaggioRispostaTracciato(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Long id) {
		String methodName = "getMessaggioRispostaTracciato";  
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


}


