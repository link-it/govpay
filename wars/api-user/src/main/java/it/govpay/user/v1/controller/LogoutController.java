package it.govpay.user.v1.controller;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.utils.GovpayConfig;

public class LogoutController extends BaseController {

	public LogoutController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}


	public Response logout(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders, String urlID) {
		String methodName = "logout";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			if(this.request.getSession() != null) {
				HttpSession session = this.request.getSession();
				session.invalidate();
			}
			
			if(urlID == null) {
				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
				return this.handleResponseOk(Response.ok(),transactionId).build();
			} else {
				Properties props = GovpayConfig.getInstance().getApiUserLogoutRedirectURLs();
				
				String redirectURL = props.getProperty(urlID);
				
				if(StringUtils.isBlank(redirectURL)) {
					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
					return this.handleResponseOk(Response.ok(),transactionId).build();
				} else {
					MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters(false);
					UriBuilder target = UriBuilder.fromUri(new URI(redirectURL));
					
					Iterator<Entry<String, List<String>>> iterator = queryParameters.entrySet().iterator();
					while(iterator.hasNext()) {
						Entry<String, List<String>> next = iterator.next();
						this.log.debug("Aggiungo queryParam " + next.getKey() + ": " + next.getValue());
						target = target.queryParam(next.getKey(), next.getValue().get(0));
					}
					redirectURL = target.build().toString();
					this.log.info("Esecuzione " + methodName + " completata con redirect verso la URL ["+ redirectURL +"].");	
					return this.handleResponseOk(Response.seeOther(new URI(redirectURL)),transactionId).build();
				}
			}
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

}


