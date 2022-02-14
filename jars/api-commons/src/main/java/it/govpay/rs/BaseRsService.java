/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.rs;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Path;
//import javax.ws.rs.OPTIONS;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import it.govpay.bd.model.Acl;
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.UtenzaApplicazione;
import it.govpay.bd.model.UtenzaOperatore;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.dao.commons.Dominio;
import it.govpay.core.utils.EventoContext.Categoria;
import it.govpay.core.utils.GpContext;


public abstract class BaseRsService {
	
	public static final String ERRORE_INTERNO = "Errore Interno";

	@Context protected HttpServletRequest request;
	@Context protected HttpServletResponse response;
	@Context protected UriInfo uriInfo;

	protected String nomeServizio;
	protected Logger log;

	protected String codOperazione;

	public BaseRsService() throws ServiceException{
		this.log = LoggerWrapperFactory.getLogger(BaseRsService.class);
	}

	public BaseRsService(String nomeServizio) throws ServiceException{
		this();
		this.nomeServizio = nomeServizio;

	}

	public void setHttpServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	protected Authentication getUser() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	protected Response getUnauthorizedResponse(){
		Response res =	Response.status(Response.Status.UNAUTHORIZED)
				.header("Access-Control-Allow-Origin", "*")
				.build();

		return res;
	}

	protected Response getForbiddenResponse(){
		Response res =	Response.status(Response.Status.FORBIDDEN)
				.header("Access-Control-Allow-Origin", "*")
				.build();

		return res;
	}

	public void invalidateSession(Logger log){
		if(log!= null)
			log.info("Invalidate Session in corso...");

		HttpSession session = this.request.getSession(false);
		if(session != null){
			session.invalidate();
		}

		if(log!= null)
			log.info("Invalidate Session completata.");
	}

	public static boolean isEmpty(List<?> lista){
		if(lista == null)
			return true;

		return lista.isEmpty();
	}

	public abstract int getVersione();
	
	protected IContext getContext() {
		IContext context = ContextThreadLocal.get();
		//System.out.println("SYNC:   " + Thread.currentThread().getId() + " " + context.getTransactionId() + " " + context.toString() );
		if(context instanceof org.openspcoop2.utils.service.context.Context) {
			((org.openspcoop2.utils.service.context.Context)context).update(this.request, this.response, this.uriInfo, 2, this.log);
			((org.openspcoop2.utils.service.context.Context)context).setRestPath(this.getPathFromRestMethod(context.getMethodName()));
			
			GpContext ctx = (GpContext) ((org.openspcoop2.utils.service.context.Context)context).getApplicationContext();
			ctx.getEventoCtx().setCategoriaEvento(Categoria.INTERFACCIA);
			ctx.getEventoCtx().setMethod(this.request.getMethod());
			ctx.getEventoCtx().setTipoEvento(context.getMethodName());
			ctx.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(context.getAuthentication()));
			GovpayLdapUserDetails authenticationDetails = AutorizzazioneUtils.getAuthenticationDetails(context.getAuthentication());
			if(authenticationDetails != null) {
				Utenza utenza = authenticationDetails.getUtenza();
				switch(utenza.getTipoUtenza()) {
				case CITTADINO:
				case ANONIMO:
					ctx.getEventoCtx().setUtente(authenticationDetails.getIdentificativo());
					break;
				case APPLICAZIONE:
					ctx.getEventoCtx().setUtente(((UtenzaApplicazione)utenza).getCodApplicazione());
					break;
				case OPERATORE:
					ctx.getEventoCtx().setUtente(((UtenzaOperatore)utenza).getNome());
					break;
				}
			}
			String baseUri = request.getRequestURI(); // uriInfo.getBaseUri().toString();
			String requestUri = uriInfo.getRequestUri().toString();
			int idxOfBaseUri = requestUri.indexOf(baseUri);
			
//			String servicePathwithParameters = requestUri.substring((idxOfBaseUri + baseUri.length()) - 1);
			String servicePathwithParameters = requestUri.substring(idxOfBaseUri);
			ctx.getEventoCtx().setUrl(servicePathwithParameters);
			
			StringBuilder sb = new StringBuilder();
			sb.append("Ricevuta una richiesta:");
			sb.append("\n");
			sb.append("API: [").append(ctx.getEventoCtx().getComponente()).append("]").append(" HTTPMethod: [").append(this.request.getMethod()).append("], Risorsa: [").append(context.getRestPath()).append("]");
			sb.append("\n");
			sb.append("URL: [").append(servicePathwithParameters).append("]");
			sb.append("\n");
			sb.append("Principal: [").append(AutorizzazioneUtils.getPrincipal(context.getAuthentication())).append("], Utente: [").append(ctx.getEventoCtx().getUtente()).append("]");
			sb.append("\n");
			if(authenticationDetails != null) {
				Utenza utenza = authenticationDetails.getUtenza();
				sb.append("Profilo: \n");
				sb.append("\t[\n\t").append("TipoUtenza: [").append(authenticationDetails.getTipoUtenza()).append("], Abilitato: [").append(utenza.isAbilitato()).append("]");
				sb.append("\n");
				sb.append("\t").append("Id Transazione Autenticazione: [").append(authenticationDetails.getIdTransazioneAutenticazione()).append("]");
				sb.append("\n");
				sb.append("\t").append("ACL:").append("\n").append("\t\t[\n");
				
				List<Acl> aclsProfilo = utenza.getAclsProfilo();
				
				for (Acl acl : aclsProfilo) {
					sb.append("\t").append("\t");
					sb.append("Ruolo[").append(acl.getRuolo()).append("], IdUtenza: [").append(acl.getIdUtenza())
						.append("], Servizio: [").append(acl.getServizio()).append("], Diritti: [").append(acl.getListaDirittiString()).append("]");
					sb.append("\n");
				}
				sb.append("\t\t]\n");
				sb.append("\t");
				if(utenza.isAutorizzazioneDominiStar())
					sb.append("Domini: [Tutti]");
				else {
					List<IdUnitaOperativa> dominiUo = utenza.getDominiUo();
					if(dominiUo != null) {
						List<Dominio> domini = UtentiDAO.convertIdUnitaOperativeToDomini(dominiUo); 
						sb.append("Domini: [");
						for (Dominio dominio : domini) {
							sb.append("\t\t");
							
							sb.append(dominio.getCodDominio()).append(", UO: [").append((dominio.getUo() != null ? (
									dominio.getUo().stream().map(d -> d.getCodUo()).collect(Collectors.toList())
									) : "Tutte")).append("]");
						}
						sb.append("\t");
						sb.append("]");
					} else {
						sb.append("Domini: [").append(utenza.getIdDominio()).append("]");
					}
				}
				sb.append("\n").append("\t");
				if(utenza.isAutorizzazioneTipiVersamentoStar())
					sb.append("TipiPendenza: [Tutti]");
				else 
					sb.append("TipiPendenza: [").append(utenza.getIdTipoVersamento()).append("]");
				sb.append("\n");
				sb.append("\t]\n");
			}
			sb.append("Query Params: [").append(this.uriInfo.getQueryParameters()).append("]");
			sb.append("\n");
			sb.append("Path Params: [").append(this.uriInfo.getPathParameters()).append("]");
			this.log.debug(sb.toString());
		}
		return context;
	}
	
	protected void buildContext() {
		IContext context = ContextThreadLocal.get(); 
		if(context instanceof org.openspcoop2.utils.service.context.Context) {
			((org.openspcoop2.utils.service.context.Context)context).update(this.request, this.response, this.uriInfo, 2, this.log);
			((org.openspcoop2.utils.service.context.Context)context).setRestPath(this.getPathFromRestMethod(context.getMethodName()));
			
			GpContext ctx = (GpContext) ((org.openspcoop2.utils.service.context.Context)context).getApplicationContext();
			ctx.getEventoCtx().setCategoriaEvento(Categoria.INTERFACCIA);
			ctx.getEventoCtx().setMethod(this.request.getMethod());
			ctx.getEventoCtx().setTipoEvento(context.getMethodName());
			ctx.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(context.getAuthentication()));
			GovpayLdapUserDetails authenticationDetails = AutorizzazioneUtils.getAuthenticationDetails(context.getAuthentication());
			if(authenticationDetails != null) {
				Utenza utenza = authenticationDetails.getUtenza();
				switch(utenza.getTipoUtenza()) {
				case CITTADINO:
				case ANONIMO:
					ctx.getEventoCtx().setUtente(authenticationDetails.getIdentificativo());
					break;
				case APPLICAZIONE:
					ctx.getEventoCtx().setUtente(((UtenzaApplicazione)utenza).getCodApplicazione());
					break;
				case OPERATORE:
					ctx.getEventoCtx().setUtente(((UtenzaOperatore)utenza).getNome());
					break;
				}
			}
			String baseUri = request.getRequestURI(); // uriInfo.getBaseUri().toString();
			String requestUri = uriInfo.getRequestUri().toString();
			int idxOfBaseUri = requestUri.indexOf(baseUri);
			
//			String servicePathwithParameters = requestUri.substring((idxOfBaseUri + baseUri.length()) - 1);
			String servicePathwithParameters = requestUri.substring(idxOfBaseUri);
			ctx.getEventoCtx().setUrl(servicePathwithParameters);
			
			String idSessione = null;
			if(this.request.getSession() != null) {
				idSessione = this.request.getSession().getId();
			}
			
			StringBuilder sb = new StringBuilder();
			sb.append("Ricevuta una richiesta:");
			sb.append("\n");
			sb.append("API: [").append(ctx.getEventoCtx().getComponente()).append("]").append(" HTTPMethod: [").append(this.request.getMethod()).append("], Risorsa: [").append(context.getRestPath()).append("]");
			sb.append("\n");
			sb.append("URL: [").append(servicePathwithParameters).append("]");
			sb.append("\n");
			sb.append("Principal: [").append(AutorizzazioneUtils.getPrincipal(context.getAuthentication())).append("], Utente: [").append(ctx.getEventoCtx().getUtente()).append("]");
			sb.append("\n");
			if(authenticationDetails != null) {
				Utenza utenza = authenticationDetails.getUtenza();
				sb.append("Profilo: \n");
				sb.append("\t[\n\t").append("TipoUtenza: [").append(authenticationDetails.getTipoUtenza()).append("], Abilitato: [").append(utenza.isAbilitato()).append("]");
				if(idSessione != null) {
					sb.append("\n");
					sb.append("\t").append("Id Sessione: [").append(idSessione).append("]");
				}
				sb.append("\n");
				sb.append("\t").append("Id Transazione Autenticazione: [").append(authenticationDetails.getIdTransazioneAutenticazione()).append("]");
				sb.append("\n");
				sb.append("\t").append("ACL:").append("\n").append("\t\t[\n");
				
				List<Acl> aclsProfilo = utenza.getAclsProfilo();
				
				for (Acl acl : aclsProfilo) {
					sb.append("\t").append("\t");
					sb.append("Ruolo[").append(acl.getRuolo()).append("], IdUtenza: [").append(acl.getIdUtenza())
						.append("], Servizio: [").append(acl.getServizio()).append("], Diritti: [").append(acl.getListaDirittiString()).append("]");
					sb.append("\n");
				}
				sb.append("\t\t]\n");
				sb.append("\t");
				if(utenza.isAutorizzazioneDominiStar())
					sb.append("Domini: [Tutti]");
				else {
					List<IdUnitaOperativa> dominiUo = utenza.getDominiUo();
					if(dominiUo != null) {
						List<Dominio> domini = UtentiDAO.convertIdUnitaOperativeToDomini(dominiUo); 
						sb.append("Domini: [");
						for (Dominio dominio : domini) {
							sb.append("\t\t");
							
							sb.append(dominio.getCodDominio()).append(", UO: [").append((dominio.getUo() != null ? (
									dominio.getUo().stream().map(d -> d.getCodUo()).collect(Collectors.toList())
									) : "Tutte")).append("]");
						}
						sb.append("\t");
						sb.append("]");
					} else {
						sb.append("Domini: [").append(utenza.getIdDominio()).append("]");
					}
				}
				sb.append("\n").append("\t");
				if(utenza.isAutorizzazioneTipiVersamentoStar())
					sb.append("TipiPendenza: [Tutti]");
				else 
					sb.append("TipiPendenza: [").append(utenza.getIdTipoVersamento()).append("]");
				sb.append("\n");
				sb.append("\t]\n");
			}
			sb.append("Query Params: [").append(this.uriInfo.getQueryParameters()).append("]");
			sb.append("\n");
			sb.append("Path Params: [").append(this.uriInfo.getPathParameters()).append("]");
			this.log.debug(sb.toString());
		}
	}
	
	private String getPathFromRestMethod(String methodName) {

        try {
        	Class<?> c = this.getClass();
        	// la versione V1 non implementa interfacce
//        	Class<?> [] interfaces = c.getInterfaces();
//        	if(interfaces==null || interfaces.length<=0) {
//        		return null;
//        	}
//        	Class<?> cInterface = null;
//        	for (int i = 0; i < interfaces.length; i++) {
//        		if (interfaces[i] != null && interfaces[i].isAnnotationPresent(Path.class)) {
//        			cInterface = interfaces[i];
//        			break;
//        		}
//			}
//        	if(cInterface==null) {
//        		return null;
//        	}
//        	Method [] methods = cInterface.getMethods();
        	
        	String rsBasePathValue = "";
        	Path rsBasePath = c.getAnnotation(Path.class);
        	if(rsBasePath !=null) {
        		rsBasePathValue = rsBasePath.value();
        	}
        	
        	Method [] methods = c.getMethods();
        	if(methods==null || methods.length<=0) {
        		return null;
        	}
        	Method method = null;
        	for (int i = 0; i < methods.length; i++) {
        		if (methods[i] != null && methods[i].getName().equals(methodName) && methods[i].isAnnotationPresent(Path.class)) {
        			method = methods[i];
        			break;
        		}
			}
        	if(method==null) {
        		return null;
        	}
        	Path path = method.getAnnotation(Path.class);
        	if(path==null) {
        		return null;
        	}
        	return rsBasePathValue + path.value();
        } catch (Exception e) {
            this.log.error(e.getMessage(),e);
        }

        return null;
    }
}

