/**
 * 
 */
package it.govpay.user.v1.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.Costanti;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.dao.commons.exception.RedirectException;
import it.govpay.core.exceptions.BaseExceptionV1;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.user.v1.beans.FaultBean;
import it.govpay.user.v1.beans.FaultBean.CategoriaEnum;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 22 feb 2018 $
 * 
 */
public abstract class BaseController {

	public final static String PARAMETRO_CONTENT_DISPOSITION = "Content-Disposition";
	public final static String PREFIX_CONTENT_DISPOSITION = "form-data; name=\"";
	public final static String SUFFIX_CONTENT_DISPOSITION = "\"";
	public final static String PREFIX_FILENAME = "filename=\"";
	public final static String SUFFIX_FILENAME = "\"";

	private static final String ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN = "Errore durante la serializzazione del FaultBean"; 
	public static final String LOG_MSG_ESECUZIONE_METODO_COMPLETATA = "Esecuzione {0} completata.";
	public static final String LOG_MSG_ESECUZIONE_METODO_IN_CORSO = "Esecuzione {0} in corso...";
	protected Logger log;
	protected String nomeServizio;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected String transactionIdHeaderName = Costanti.HEADER_NAME_OUTPUT_TRANSACTION_ID;
	protected IContext context;
	
	public BaseController(String nomeServizio, Logger log) {
		this.log = log;
		this.nomeServizio = nomeServizio;
	}
	
	public void setContext(IContext context) {
		this.context = context;
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
		return 1;
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
		String respKoJson = this.getRespJson(respKo);
		 
		ResponseBuilder responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity(respKoJson); 
		this.handleEventoFail(responseBuilder, transactionId, respKo.getCodice(),  respKo.getDettaglio());
		return handleResponseKo(responseBuilder, transactionId).build();
	}

	protected String getRespJson(FaultBean respKo) {
		String respKoJson = null;
		try {
			respKoJson =respKo.toJSON(null);
		} catch(ServiceException ex) {
			this.log.error(ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN, ex);
			respKoJson = ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN;
		}
		return respKoJson;
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

		String respJson = this.getRespJson(respKo);
		ResponseBuilder responseBuilder = Response.status(e.getTransportErrorCode()).type(MediaType.APPLICATION_JSON).entity(respJson);
		if(e.getTransportErrorCode() > 499)
			this.handleEventoFail(responseBuilder, transactionId, sottotipoEsito, respKo.getDettaglio());
		else 
			this.handleEventoKo(responseBuilder, transactionId, sottotipoEsito, respKo.getDettaglio());
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
		
		String respJson = this.getRespJson(respKo);
		ResponseBuilder responseBuilder = Response.status(statusCode).type(MediaType.APPLICATION_JSON).entity(respJson);
		if(statusCode > 499)
			this.handleEventoFail(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio());
		else 
			this.handleEventoKo(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio());
		
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
		
		String respJson = this.getRespJson(respKo);
		ResponseBuilder responseBuilder = Response.status(statusCode).type(MediaType.APPLICATION_JSON).entity(respJson);
		this.handleEventoKo(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio());
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
	
	private Response handleUnprocessableEntityException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, UnprocessableEntityException e, String transactionId) {
		this.log.info("Errore ("+e.getClass().getSimpleName()+") durante "+methodName+": "+ e.getMessage());
		
		FaultBean respKo = new FaultBean();
		respKo.setCategoria(CategoriaEnum.RICHIESTA);
		respKo.setCodice("SEMANTICA");
		respKo.setDescrizione("Richiesta non valida");
		respKo.setDettaglio(e.getDetails());

		String respJson = this.getRespJson(respKo);
		ResponseBuilder responseBuilder = Response.status(e.getTransportErrorCode()).type(MediaType.APPLICATION_JSON).entity(respJson);
		if(e.getTransportErrorCode() > 499)
			this.handleEventoFail(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio());
		else 
			this.handleEventoKo(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio());
		
		return handleResponseKo(responseBuilder, transactionId).build();
	}

	protected void log(IContext ctx) {
		if(ctx != null) {
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
		GpContext ctx = (GpContext) this.context.getApplicationContext();
		ctx.getEventoCtx().setEsito(Esito.OK);
		if(transactionId != null)
			ctx.getEventoCtx().setIdTransazione(transactionId);
		
		return responseBuilder;
	}
	
	protected ResponseBuilder handleEventoKo(ResponseBuilder responseBuilder, String transactionId, String sottotipoEsito, String dettaglioEsito) {
		GpContext ctx = (GpContext) this.context.getApplicationContext();
		ctx.getEventoCtx().setEsito(Esito.KO);
		if(transactionId != null)
			ctx.getEventoCtx().setIdTransazione(transactionId);
		ctx.getEventoCtx().setDescrizioneEsito(dettaglioEsito);
		
		if(sottotipoEsito != null)
			ctx.getEventoCtx().setSottotipoEsito(sottotipoEsito);
		
		return responseBuilder;
	}
	
	protected ResponseBuilder handleEventoFail(ResponseBuilder responseBuilder, String transactionId, String sottotipoEsito, String dettaglioEsito) {
		GpContext ctx = (GpContext) this.context.getApplicationContext();
		ctx.getEventoCtx().setEsito(Esito.FAIL);
		if(transactionId != null)
			ctx.getEventoCtx().setIdTransazione(transactionId);
		ctx.getEventoCtx().setDescrizioneEsito(dettaglioEsito);
		
		if(sottotipoEsito != null)
			ctx.getEventoCtx().setSottotipoEsito(sottotipoEsito);
		
		return responseBuilder;
	}
	
	protected Map<String, String> getHeaders(HttpServletRequest request) {
		Enumeration<String> headerNames = request.getHeaderNames();

		Map<String, String> result = new HashMap<>();
		if (request == null || headerNames == null) {
			return result;
		}

		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			Enumeration<String> headerValues = request.getHeaders(headerName);

			if(headerValues != null) {
				List<String> values = new ArrayList<>();
				while (headerValues.hasMoreElements()) {
					String value = (String) headerValues.nextElement();
					values.add(value);
				}
				result.put(headerName, String.join(",", values));
			}
		}
		return result;
	}
}
