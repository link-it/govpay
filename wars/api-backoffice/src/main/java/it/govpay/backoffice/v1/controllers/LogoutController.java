package it.govpay.backoffice.v1.controllers;


import java.text.MessageFormat;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;




public class LogoutController extends BaseController {

	public LogoutController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}



	public Response logout(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders ) {
		String methodName = "logout";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			if(this.request.getSession() != null) {
				HttpSession session = this.request.getSession();
				session.invalidate();
			}

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.ok(),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}
}


