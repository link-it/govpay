package it.govpay.user.v1.controller;

import java.text.MessageFormat;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

public class LogoutController extends BaseController {

	public LogoutController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}

	public Response logout(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders ) {
		String methodName = "login";  
		String transactionId = this.context.getTransactionId();
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
			this.log(this.context);
		}
	}

}


