package it.govpay.ragioneria.v3.api.impl;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.json.ValidationException;
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
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.Costanti;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.dao.commons.Dominio;
import it.govpay.core.dao.commons.exception.RedirectException;
import it.govpay.core.exceptions.BaseExceptionV1;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IncassiException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.EventoContext.Categoria;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.GpContext;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.ragioneria.v3.beans.FaultBean;
import it.govpay.ragioneria.v3.beans.FaultBean.CategoriaEnum;

public class BaseApiServiceImpl {

	public final static String PARAMETRO_CONTENT_DISPOSITION = "Content-Disposition";
	public final static String PREFIX_CONTENT_DISPOSITION = "form-data; name=\"";
	public final static String SUFFIX_CONTENT_DISPOSITION = "\"";
	public final static String PREFIX_FILENAME = "filename=\"";
	public final static String SUFFIX_FILENAME = "\"";

	private static final String ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN = "Errore durante la serializzazione del FaultBean"; 
	public static final String LOG_MSG_ESECUZIONE_METODO_COMPLETATA = "Esecuzione {0} completata.";
	public static final String LOG_MSG_ESECUZIONE_METODO_IN_CORSO = "Esecuzione {0} in corso...";

	protected String transactionIdHeaderName = Costanti.HEADER_NAME_OUTPUT_TRANSACTION_ID;
	
	public static final String ERRORE_INTERNO = "Errore Interno";

	@Context protected HttpServletRequest request;
	@Context protected HttpServletResponse response;
	@Context protected UriInfo uriInfo;
	@Context protected HttpHeaders httpHeaders;

	protected String nomeServizio;
	protected Logger log;

	protected String codOperazione;
	
	public BaseApiServiceImpl(String nomeServizio, Class<?> clazz) {
		this.log = LoggerWrapperFactory.getLogger(clazz);
		this.nomeServizio = nomeServizio;
	}
	
	public void setRequestResponse(HttpServletRequest request,HttpServletResponse response) {
		this.response = response;
		this.request = request;
	}
	
	public HttpServletRequest getRequest() {
		return this.request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return this.response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public int getVersione() {
		return 3;
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
	
	public URI getServicePath(UriInfo uriInfo) throws URISyntaxException {
		String baseUri = uriInfo.getBaseUri().toString();
		String requestUri = uriInfo.getRequestUri().toString();
		int idxOfBaseUri = requestUri.indexOf(baseUri);
		
		String servicePathwithParameters = requestUri.substring((idxOfBaseUri + baseUri.length()) - 1);
		
		return new URI(servicePathwithParameters);
	}
	
	public URI getServicePathWithoutParameters(UriInfo uriInfo) throws URISyntaxException {

		URI servicePathwithParameters = this.getServicePath(uriInfo);
		String baseUri = servicePathwithParameters.toString();

		int indexOf = baseUri.indexOf("?");
		if(indexOf> 0) {
			return new URI(baseUri.substring(0, indexOf));
		} else {
			return new URI(baseUri);
		}
	}
	
	protected ResponseBuilder handleResponseOk(ResponseBuilder responseBuilder, String transactionId) {
		this.handleEventoOk(responseBuilder, transactionId);
		if(transactionId != null)
			return responseBuilder.header(this.transactionIdHeaderName, transactionId);
		else 
			return responseBuilder;
	}
	
	protected ResponseBuilder handleResponseKo(ResponseBuilder responseBuilder, String transactionId) {
		if(transactionId != null)
			return responseBuilder.header(this.transactionIdHeaderName, transactionId);
		else 
			return responseBuilder;
	}
	
	protected Response handleException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, Exception e, String transactionId) {
		
		if(e instanceof UnprocessableEntityException) {
			return this.handleUnprocessableEntityException(uriInfo, httpHeaders, methodName, (UnprocessableEntityException)e,transactionId);
		}
		
		if(e instanceof IncassiException) {
			return this.handleIncassiException(uriInfo, httpHeaders, methodName, (IncassiException)e,transactionId);
		}
		
		if(e instanceof BaseExceptionV1) {
			return this.handleBaseException(uriInfo, httpHeaders, methodName, (BaseExceptionV1)e,transactionId);
		}
		
		if(e instanceof RedirectException) {
			return this.handleRedirectException(uriInfo, httpHeaders, methodName, (RedirectException)e,transactionId);
		}
		
		if(e instanceof GovPayException) {
			return this.handleGovpayException(uriInfo, httpHeaders, methodName, (GovPayException)e,transactionId);
		}
		
		if(e instanceof ValidationException) {
			return this.handleValidationException(uriInfo, httpHeaders, methodName, (ValidationException)e,transactionId);
		}
		
		this.log.error("Errore interno durante "+methodName+": " + e.getMessage(), e);
		FaultBean respKo = new FaultBean();
		respKo.setCategoria(CategoriaEnum.INTERNO);
		respKo.setCodice(EsitoOperazione.INTERNAL.toString());
		respKo.setDescrizione("Errore interno");
		respKo.setDettaglio(e.getMessage());
		
		ResponseBuilder responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity(respKo); 
		this.handleEventoFail(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio(), e);
		return handleResponseKo(responseBuilder, transactionId).build();
	}

	private Response handleBaseException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, BaseExceptionV1 e, String transactionId) {
		FaultBean respKo = new FaultBean();
		respKo.setCategoria(FaultBean.CategoriaEnum.fromValue(e.getCategoria().name()));
		respKo.setCodice(e.getCode());
		respKo.setDescrizione(e.getMessage());
		respKo.setDettaglio(e.getDetails());
		
		String sottotipoEsito = respKo.getCodice();
		if(e instanceof NotAuthenticatedException || e instanceof NotAuthorizedException) {
			this.log.info("Accesso alla risorsa "+methodName+" non consentito: "+ e.getMessage() + ", " + e.getDetails());
			sottotipoEsito = CategoriaEnum.AUTORIZZAZIONE.name();
		} else {
			this.log.info("Errore ("+e.getClass().getSimpleName()+") durante "+methodName+": "+ e.getMessage());
		}

//		String respJson = this.getRespJson(respKo);
		ResponseBuilder responseBuilder = Response.status(e.getTransportErrorCode()).type(MediaType.APPLICATION_JSON).entity(respKo);
		if(e.getTransportErrorCode() > 499)
			this.handleEventoFail(responseBuilder, transactionId, sottotipoEsito, respKo.getDettaglio(), e);
		else 
			this.handleEventoKo(responseBuilder, transactionId, sottotipoEsito, respKo.getDettaglio(), e);
		return handleResponseKo(responseBuilder, transactionId).build(); 
	}

	private Response handleGovpayException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, GovPayException e, String transactionId) {
		this.log.error("Errore ("+e.getClass().getSimpleName()+") durante "+methodName+": "+ e.getMessage(), e);
		FaultBean respKo = new FaultBean();
		int statusCode = e.getStatusCode();
		if(e.getFaultBean()!=null) {
			respKo.setCategoria(CategoriaEnum.PAGOPA);
			respKo.setCodice(e.getFaultBean().getFaultCode());
			respKo.setDescrizione(e.getFaultBean().getFaultString());
			respKo.setDettaglio(e.getFaultBean().getDescription());
			statusCode = 502; // spostato dalla govpayException perche' ci sono dei casi di errore che non devono restituire 500;
		} else {
			respKo.setCategoria(CategoriaEnum.fromValue(e.getCategoria().name()));
			respKo.setCodice(e.getCodEsitoV3());
			respKo.setDescrizione(e.getDescrizioneEsito());
			respKo.setDettaglio(e.getMessageV3());
		}
		
		ResponseBuilder responseBuilder = Response.status(statusCode).type(MediaType.APPLICATION_JSON).entity(respKo);
		if(statusCode > 499)
			this.handleEventoFail(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio(), e);
		else 
			this.handleEventoKo(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio(), e);
		
		return handleResponseKo(responseBuilder, transactionId).build();
	}
	
	private Response handleUnprocessableEntityException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, UnprocessableEntityException e, String transactionId) {
		this.log.info("Errore ("+e.getClass().getSimpleName()+") durante "+methodName+": "+ e.getMessage());
		
		FaultBean respKo = new FaultBean();
		respKo.setCategoria(CategoriaEnum.RICHIESTA);
		respKo.setCodice("SEMANTICA");
		respKo.setDescrizione("Richiesta non valida");
		respKo.setDettaglio(e.getDetails());

		ResponseBuilder responseBuilder = Response.status(e.getTransportErrorCode()).type(MediaType.APPLICATION_JSON).entity(respKo);
		if(e.getTransportErrorCode() > 499)
			this.handleEventoFail(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio(), e);
		else 
			this.handleEventoKo(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio(), e);
		
		return handleResponseKo(responseBuilder, transactionId).build();
	}
	
	private Response handleValidationException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, ValidationException e, String transactionId) {
		this.log.warn("Richiesta rifiutata per errori di validazione: " + e);
		FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.RICHIESTA);
			respKo.setCodice("SINTASSI");
			respKo.setDescrizione("Richiesta non valida");
			respKo.setDettaglio(e.getMessage());
		
		int statusCode = 400;
		
		ResponseBuilder responseBuilder = Response.status(statusCode).type(MediaType.APPLICATION_JSON).entity(respKo);
		this.handleEventoKo(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio(), e);
		return handleResponseKo(responseBuilder, transactionId).build();
	}
	
	private Response handleIncassiException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, IncassiException e, String transactionId) {
		this.log.info("Errore ("+e.getClass().getSimpleName()+") durante "+methodName+": "+ e.getMessage() + ": " + e.getDetails());
		
		FaultBean respKo = new FaultBean();
		respKo.setCategoria(CategoriaEnum.RICHIESTA);
		respKo.setCodice(e.getCode());
		respKo.setDescrizione(e.getMessage());
		respKo.setDettaglio(e.getDetails());

		ResponseBuilder responseBuilder = Response.status(e.getTransportErrorCode()).type(MediaType.APPLICATION_JSON).entity(respKo);
		if(e.getTransportErrorCode() > 499)
			this.handleEventoFail(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio(), e);
		else 
			this.handleEventoKo(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio(), e);
		return handleResponseKo(responseBuilder, transactionId).build();
	}

	private Response handleRedirectException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, RedirectException e, String transactionId) {
		this.log.error("Esecuzione del metodo ["+methodName+"] si e' conclusa con un errore: " + e.getMessage() + ", redirect verso la url: " + e.getLocation());
		ResponseBuilder responseBuilder = Response.seeOther(e.getURILocation());
		this.handleEventoOk(responseBuilder, transactionId);
		if(transactionId != null)
			return responseBuilder.header(this.transactionIdHeaderName, transactionId).build();
		else
			return responseBuilder.build();
	}

	protected void log(IContext ctx) {
		if(ctx != null) {
//			if(this.response != null) {
//				String transactionId = ctx.getTransactionId();
//				this.response.setHeader(this.transactionIdHeaderName, transactionId);
//			}
			
//			try {
//				ctx.getApplicationLogger().log();
//			} catch (UtilsException e) {
//				 this.log.error("Errore durante la chiusura dell'operazione: "+e.getMessage(),e);
//			}
		}
	}
	
	protected void isAuthorized(Authentication authentication, List<TIPO_UTENZA> tipoUtenza, List<Servizio> servizi, List<Diritti> listaDiritti) throws NotAuthorizedException {
		if(!AuthorizationManager.isAuthorized(authentication, tipoUtenza, servizi, listaDiritti)) {
			throw AuthorizationManager.toNotAuthorizedException(authentication);
		}
	}
	
	protected ResponseBuilder handleEventoOk(ResponseBuilder responseBuilder, String transactionId) {
		GpContext ctx = (GpContext) ContextThreadLocal.get().getApplicationContext();
		ctx.getEventoCtx().setEsito(Esito.OK);
		if(transactionId != null)
			ctx.getEventoCtx().setIdTransazione(transactionId);
		
		return responseBuilder;
	}
	
	protected ResponseBuilder handleEventoKo(ResponseBuilder responseBuilder, String transactionId, String sottotipoEsito, String dettaglioEsito, Exception exception) {
		GpContext ctx = (GpContext) ContextThreadLocal.get().getApplicationContext();
		ctx.getEventoCtx().setEsito(Esito.KO);
		if(transactionId != null)
			ctx.getEventoCtx().setIdTransazione(transactionId);
		ctx.getEventoCtx().setDescrizioneEsito(dettaglioEsito);
		
		if(sottotipoEsito != null)
			ctx.getEventoCtx().setSottotipoEsito(sottotipoEsito);
		
		ctx.getEventoCtx().setException(exception);
		
		return responseBuilder;
	}
	
	protected ResponseBuilder handleEventoFail(ResponseBuilder responseBuilder, String transactionId, String sottotipoEsito, String dettaglioEsito, Exception exception) {
		GpContext ctx = (GpContext) ContextThreadLocal.get().getApplicationContext();
		ctx.getEventoCtx().setEsito(Esito.FAIL);
		if(transactionId != null)
			ctx.getEventoCtx().setIdTransazione(transactionId);
		ctx.getEventoCtx().setDescrizioneEsito(dettaglioEsito);
		
		if(sottotipoEsito != null)
			ctx.getEventoCtx().setSottotipoEsito(sottotipoEsito);
		
		ctx.getEventoCtx().setException(exception);
		
		return responseBuilder;
	}
}
