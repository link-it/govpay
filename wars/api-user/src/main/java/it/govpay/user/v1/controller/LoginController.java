package it.govpay.user.v1.controller;

import java.net.URI;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.commons.exception.NonTrovataException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.rs.v1.authentication.preauth.filter.SessionPrincipalExtractorPreAuthFilter;

public class LoginController extends BaseController {

     public LoginController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }

    public Response login(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders, String urlID) {
    	String methodName = "login";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			if(urlID == null) {
				throw new NonTrovataException("URL-ID non specificato.");
			} else {
				Properties props = GovpayConfig.getInstance().getApiUserLoginRedirectURLs();
				
				String redirectURL = props.getProperty(urlID);
				
				if(StringUtils.isBlank(redirectURL))
					throw new NonTrovataException("URL-ID non registrato nel sistema.");
				
				if(this.request.getSession() != null) {
					HttpSession session = this.request.getSession();
					Authentication authentication = this.context.getAuthentication();
					session.setAttribute(SessionPrincipalExtractorPreAuthFilter.SESSION_PRINCIPAL_ATTRIBUTE_NAME, authentication != null ? authentication.getName() : null);
					session.setAttribute(SessionPrincipalExtractorPreAuthFilter.SESSION_PRINCIPAL_OBJECT_ATTRIBUTE_NAME, authentication != null ? authentication.getPrincipal() : null);
				}
				
				// forward dei parametri in query string
				MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
				if(queryParameters != null && !queryParameters.isEmpty()) {
					
					int indexOf = redirectURL.indexOf("?");
					
					StringBuilder sb = new StringBuilder();
					for (String paramName : queryParameters.keySet()) {
						List<String> values = queryParameters.get(paramName);
						
						if(values != null) {
							for (String val : values) {
								if(sb.length() == 0) {
									sb.append("?");
								} else {
									sb.append("&");
								}
								sb.append(paramName).append("=").append(val);
							}
						}
					}
					
					if(indexOf > -1) {
						sb.replace(0, 1, "&");
					}
					
					redirectURL += sb.toString();
				}
				
				
				this.log.info("Esecuzione " + methodName + " completata con redirect verso la URL ["+ redirectURL +"].");	
				return this.handleResponseOk(Response.seeOther(new URI(redirectURL)),transactionId).build();
			}
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }


}


