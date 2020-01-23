package it.govpay.user.v1.authentication.handler;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.beans.Costanti;
import it.govpay.core.utils.EventoContext.Componente;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.service.context.GpContextFactory;
import it.govpay.rs.v1.authentication.preauth.filter.SessionPrincipalExtractorPreAuthFilter;
import it.govpay.rs.v1.exception.CodiceEccezione;

public class RedirectAuthenticationSuccessHandler extends org.openspcoop2.utils.service.authentication.handler.jaxrs.DefaultAuthenticationSuccessHandler{
	
	private static Logger log = LoggerWrapperFactory.getLogger(RedirectAuthenticationSuccessHandler.class);
	
	public static final String REDIRECT_URL_PARAMETER_NAME = "redirectURL";
	
	private String apiName;
	private String authType;

	@Override
	public Response getPayload(HttpServletRequest request, HttpServletResponse res, Authentication authentication) {
		IContext ctx = null;
		try{
			if(authentication == null || ! authentication.isAuthenticated()) {
				log.warn("Richiesta non autorizzata.");
				return CodiceEccezione.AUTENTICAZIONE.toFaultResponse();
			}
			
			ctx = ContextThreadLocal.get();
			
			if(ctx == null) {
				GpContextFactory factory  = new GpContextFactory();
				String user = authentication != null ? authentication.getName() : null;
				ctx = factory.newContext(request.getRequestURI(), "login", "login", request.getMethod(), 1, user, Componente.API_USER);
				ContextThreadLocal.set(ctx);
			}
			
			GpContext gpContext = (GpContext) ctx.getApplicationContext();
			
			gpContext.getEventoCtx().setEsito(Esito.OK);
			gpContext.getEventoCtx().setIdTransazione(ctx.getTransactionId());
			
			String redirectURL =  request.getParameter(RedirectAuthenticationSuccessHandler.REDIRECT_URL_PARAMETER_NAME);
			
			if(request.getSession() != null) {
				HttpSession session = request.getSession();
				session.setAttribute(SessionPrincipalExtractorPreAuthFilter.SESSION_PRINCIPAL_ATTRIBUTE_NAME, authentication != null ? authentication.getName() : null);
				session.setAttribute(SessionPrincipalExtractorPreAuthFilter.SESSION_PRINCIPAL_OBJECT_ATTRIBUTE_NAME, authentication != null ? authentication.getPrincipal() : null);
			}
			
			if(redirectURL == null) {
				this.debug(ctx.getTransactionId(), "Utente autorizzato ma URL di Redirect non indicata, restituisco 200 OK."); 
//				log.warn("Utente autorizzato ma URL di Redirect non indicata, restituisco 200 OK.");
				return Response.status(Status.OK).header(Costanti.HEADER_NAME_OUTPUT_TRANSACTION_ID, ctx.getTransactionId()).build();
			} else {
				this.debug(ctx.getTransactionId(), "Utente autorizzato redirect verso la URL ["+ redirectURL +"].");	
//				log.info("Utente autorizzato redirect verso la URL ["+ redirectURL +"].");	
				return Response.seeOther(new URI(redirectURL)).header(Costanti.HEADER_NAME_OUTPUT_TRANSACTION_ID, ctx.getTransactionId()).build();
			}
		}catch (Exception e) {
			return CodiceEccezione.AUTENTICAZIONE.toFaultResponse(e);
		} finally {
			if(ctx != null)
				try {
					ctx.getApplicationLogger().log();
				} catch (UtilsException e) {
					log.error("Errore durante il log dell'operazione: "+e.getMessage(), e);
				}
		}
	}
	
	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}
	
	public void debug(String transactionId, String msg) {
		StringBuilder sb = new StringBuilder();
		
		// API Name / Auth Type
		if(this.apiName != null) {
			sb.append("API: ").append(this.apiName);
		}
		if(this.authType != null) {
			if(sb.length() > 0)
				sb.append(" | ");
			
			sb.append("AUTH: ").append(this.authType);
		}
		
		// Id transazione accesso DB
		if(transactionId != null) {
			if(sb.length() > 0)
				sb.append(" | ");
			
			sb.append("Id Transazione Autenticazione: ").append(transactionId);
		}
	
		// messaggio
		if(sb.length() > 0)
			sb.append(" | ");
		
		sb.append(msg);
		
		log.debug(sb.toString());
	}
}
