/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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
import it.govpay.core.beans.EventoContext.Categoria;
import it.govpay.core.beans.commons.Dominio;
import it.govpay.core.beans.commons.Dominio.Uo;
import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.LogUtils;


public abstract class BaseRsService {

	public static final String ERRORE_INTERNO = "Errore Interno";

	@Context protected HttpServletRequest request;
	@Context protected HttpServletResponse response;
	@Context protected UriInfo uriInfo;

	protected String nomeServizio;
	protected Logger log;

	protected String codOperazione;

	protected BaseRsService() {
		this.log = LoggerWrapperFactory.getLogger(BaseRsService.class);
	}

	protected BaseRsService(String nomeServizio) {
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
		return Response.status(Response.Status.UNAUTHORIZED)
				.header("Access-Control-Allow-Origin", "*")
				.build();
	}

	protected Response getForbiddenResponse(){
		return Response.status(Response.Status.FORBIDDEN)
				.header("Access-Control-Allow-Origin", "*")
				.build();
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
		if(context instanceof org.openspcoop2.utils.service.context.Context) {
			((org.openspcoop2.utils.service.context.Context)context).update(this.request, this.response, this.uriInfo, 2, this.log);
			((org.openspcoop2.utils.service.context.Context)context).setRestPath(this.getPathFromRestMethod(context.getMethodName()));

			GpContext ctx = (GpContext) ((org.openspcoop2.utils.service.context.Context)context).getApplicationContext();
			ctx.getEventoCtx().setCategoriaEvento(Categoria.INTERFACCIA);
			ctx.getEventoCtx().setMethod(this.request.getMethod());
			ctx.getEventoCtx().setTipoEvento(context.getMethodName());
			ctx.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(context.getAuthentication()));
			GovpayLdapUserDetails authenticationDetails = AutorizzazioneUtils.getAuthenticationDetails(context.getAuthentication());
			String baseUri = request.getRequestURI();
			String requestUri = uriInfo.getRequestUri().toString();
			int idxOfBaseUri = requestUri.indexOf(baseUri);

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

				sb.append("Profilo: \n");
				sb.append("\t[\n\t").append("TipoUtenza: [").append(authenticationDetails.getTipoUtenza()).append("], Abilitato: [").append(utenza.isAbilitato()).append("]");
				sb.append("\n");
				sb.append("\t").append("Id Transazione Autenticazione: [").append(authenticationDetails.getIdTransazioneAutenticazione()).append("]");
				sb.append("\n");
				sb.append("\t").append("ACL:").append("\n").append("\t\t[\n");

				List<Acl> aclsProfilo = utenza.getAclsProfilo();

				this.printAcl(sb, aclsProfilo);
				sb.append("\t\t]\n");
				sb.append("\t");
				this.printAutorizzazioneDominiUo(sb, utenza);
				sb.append("\n").append("\t");
				this.printAutorizzazioneTipiVersamento(sb, utenza);
				sb.append("\n");
				sb.append("\t]\n");
			}
			sb.append("Query Params: [").append(this.uriInfo.getQueryParameters()).append("]");
			sb.append("\n");
			sb.append("Path Params: [").append(this.uriInfo.getPathParameters()).append("]");
			this.logDebug(sb.toString());
		}
		return context;
	}

	private void printAcl(StringBuilder sb, List<Acl> aclsProfilo) {
		for (Acl acl : aclsProfilo) {
			sb.append("\t").append("\t");
			sb.append("Ruolo[").append(acl.getRuolo()).append("], IdUtenza: [").append(acl.getIdUtenza())
				.append("], Servizio: [").append(acl.getServizio()).append("], Diritti: [").append(acl.getListaDirittiString()).append("]");
			sb.append("\n");
		}
	}

	private void printAutorizzazioneTipiVersamento(StringBuilder sb, Utenza utenza) {
		if(utenza.isAutorizzazioneTipiVersamentoStar())
			sb.append("TipiPendenza: [Tutti]");
		else
			sb.append("TipiPendenza: [").append(utenza.getIdTipoVersamento()).append("]");
	}

	private void printAutorizzazioneDominiUo(StringBuilder sb, Utenza utenza) {
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
							dominio.getUo().stream().map(Uo::getCodUo).collect(Collectors.toList())
							) : "Tutte")).append("]");
				}
				sb.append("\t");
				sb.append("]");
			} else {
				sb.append("Domini: [").append(utenza.getIdDominio()).append("]");
			}
		}
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
			String baseUri = request.getRequestURI();
			String requestUri = uriInfo.getRequestUri().toString();
			int idxOfBaseUri = requestUri.indexOf(baseUri);

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

				printAcl(sb, aclsProfilo);
				sb.append("\t\t]\n");
				sb.append("\t");
				printAutorizzazioneDominiUo(sb, utenza);
				sb.append("\n").append("\t");
				printAutorizzazioneTipiVersamento(sb, utenza);
				sb.append("\n");
				sb.append("\t]\n");
			}
			sb.append("Query Params: [").append(this.uriInfo.getQueryParameters()).append("]");
			sb.append("\n");
			sb.append("Path Params: [").append(this.uriInfo.getPathParameters()).append("]");
			this.logDebug(sb.toString());
		}
	}

	private String getPathFromRestMethod(String methodName) {

        try {
        	Class<?> c = this.getClass();

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
        	for (Method method2 : methods) {
        		if (method2 != null && method2.getName().equals(methodName) && method2.isAnnotationPresent(Path.class)) {
        			method = method2;
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

	protected void logDebugException(String msg, Exception e) {
		LogUtils.logDebugException(this.log, msg, e);
	}

	protected void logDebug(String msg, Object ... params) {
		LogUtils.logDebug(this.log, msg, params);
	}

	protected void logInfoException(String msg, Exception e) {
		LogUtils.logInfoException(this.log, msg, e);
	}

	protected void logInfo(String msg, Object ... params) {
		LogUtils.logInfo(this.log, msg, params);
	}

	protected void logWarnException(String msg, Exception e) {
		LogUtils.logWarnException(this.log, msg, e);
	}

	protected void logWarn(String msg, Object ... params) {
		LogUtils.logWarn(this.log, msg, params);
	}

	protected void logError(String msg) {
		LogUtils.logError(this.log, msg);
	}

	protected void logError(String msg, Exception e) {
		LogUtils.logError(this.log, msg, e);
	}

	protected void logTrace(String msg, Object ... params) {
		LogUtils.logTrace(this.log, msg, params);
	}
}

