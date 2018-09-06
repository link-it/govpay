/**
 * 
 */
package it.govpay.rs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jackson.map.ObjectMapper;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.rest.ProcessingException;
import org.openspcoop2.utils.rest.ValidatorException;
import org.openspcoop2.utils.rest.entity.Cookie;
import org.openspcoop2.utils.rest.entity.TextHttpRequestEntity;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.core.dao.commons.exception.RedirectException;
import it.govpay.core.exceptions.BaseExceptionV1;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.RequestValidationException;
import it.govpay.core.exceptions.ResponseValidationException;
import it.govpay.core.rs.v1.beans.JSONSerializable;
import it.govpay.core.rs.v1.beans.base.FaultBean;
import it.govpay.core.rs.v1.beans.base.FaultBean.CategoriaEnum;
import it.govpay.core.rs.v1.costanti.Costanti;
import it.govpay.core.rs.v1.costanti.EsitoOperazione;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.log.MessageLoggingHandlerUtils;
import it.govpay.rs.v1.BaseRsServiceV1;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 22 feb 2018 $
 * 
 */
public abstract class BaseController {

	private static final String ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN = "Errore durante la serializzazione del FaultBean"; 
	public static final String LOG_MSG_ESECUZIONE_METODO_COMPLETATA = "Esecuzione {0} completata.";
	public static final String LOG_MSG_ESECUZIONE_METODO_IN_CORSO = "Esecuzione {0} in corso...";
	protected Logger log;
	protected String nomeServizio;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	private boolean validate;
	private String name;
	protected String transactionIdHeaderName = Costanti.HEADER_NAME_OUTPUT_TRANSACTION_ID;

	public BaseController(String nomeServizio, Logger log, String name) {
		this(nomeServizio, log, name, true);
	}

	public BaseController(String nomeServizio, Logger log, String name, boolean validate) {
		this.log = log;
		this.nomeServizio = nomeServizio;
		this.validate = validate;
		this.name = name;
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
	
	public void setupContext(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione) throws ServiceException {
		GpContext ctx = new GpContext(uriInfo,rsHttpHeaders, this.request, nomeOperazione, this.nomeServizio, GpContext.TIPO_SERVIZIO_GOVPAY_JSON, this.getVersione());
		MDC.put("op", ctx.getTransactionId());
		GpThreadLocal.set(ctx);
	}

	public void logResponse(UriInfo uriInfo, HttpHeaders rsHttpHeaders, String nomeOperazione, Object o, Integer responseCode) throws IOException, ResponseValidationException, ServiceException {
		if(o != null && o instanceof JSONSerializable) {
			this.logResponse(uriInfo, rsHttpHeaders, nomeOperazione, ((JSONSerializable) o).toJSON(null).getBytes(), responseCode);
		}
		else if(o != null && o instanceof String) {
			this.logResponse(uriInfo, rsHttpHeaders, nomeOperazione, ((String) o).getBytes(), responseCode);
		}
		else{
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(o);
			this.logResponse(uriInfo, rsHttpHeaders, nomeOperazione, json.getBytes(), responseCode);
		}
	}
	
	public void logRequest(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione, ByteArrayOutputStream baos) throws RequestValidationException {
		MessageLoggingHandlerUtils.logToSystemOut(uriInfo, rsHttpHeaders, this.request,baos,
				nomeOperazione, this.nomeServizio, GpContext.TIPO_SERVIZIO_GOVPAY_JSON, this.getVersione(), this.log, false);
		if(GovpayConfig.getInstance().isValidazioneAPIRestAbilitata() && this.validate) {
			this.validateRequest(uriInfo, rsHttpHeaders, baos.toByteArray());
		}
	}
	
	public void logRequest(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione, byte[] baos) throws RequestValidationException{
		MessageLoggingHandlerUtils.logToSystemOut(uriInfo, rsHttpHeaders, this.request,baos,
				nomeOperazione, this.nomeServizio, GpContext.TIPO_SERVIZIO_GOVPAY_JSON, this.getVersione(), this.log, false);
		if(GovpayConfig.getInstance().isValidazioneAPIRestAbilitata() && this.validate) {
			this.validateRequest(uriInfo, rsHttpHeaders, baos);
		}
	}

	private void validateRequest(UriInfo uriInfo, HttpHeaders rsHttpHeaders, byte[] baos) throws RequestValidationException {
		try {
			TextHttpRequestEntity httpEntity = new TextHttpRequestEntity();
			httpEntity.setMethod(HttpRequestMethod.valueOf(this.request.getMethod()));
			if(rsHttpHeaders.getCookies()!=null) {
				List<Cookie> cookies = new ArrayList<>();
				for(String cookieS: rsHttpHeaders.getCookies().keySet()) {
					javax.ws.rs.core.Cookie cookie = rsHttpHeaders.getCookies().get(cookieS);
					cookies.add(new Cookie(cookie.getName(), cookie.getValue()));
				}
				httpEntity.setCookies(cookies);
			}
			if(rsHttpHeaders.getRequestHeaders()!=null) {
				Properties properties = new Properties();
				for(String key: rsHttpHeaders.getRequestHeaders().keySet()) {
					List<String> header = rsHttpHeaders.getRequestHeaders().get(key);
					properties.put(key, header);
				}
				httpEntity.setParametersTrasporto(properties);
			}
			if(uriInfo.getQueryParameters()!=null) {
				Properties properties = new Properties();
				for(String key: uriInfo.getQueryParameters().keySet()) {
					List<String> header = uriInfo.getQueryParameters().get(key);
					properties.put(key, header);
				}
				httpEntity.setParametersQuery(properties);
			}
			httpEntity.setUrl(this.getServicePathWithoutParameters(uriInfo).toString());
			if(baos.length > 0) {
				httpEntity.setContent(new String(baos));
				httpEntity.setContentType(this.request.getContentType());
			}
			
			BaseRsServiceV1.validatorMap.get(this.name).validate(httpEntity);
		} catch (ProcessingException | ValidatorException | URISyntaxException e) {
			this.log.error("Errore di validazione di richiesta: " + e.getMessage(), e);
			throw new RequestValidationException(e.getMessage());
		}
	}

	public void logResponse(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione,byte[] bytes, Integer responseCode) throws ResponseValidationException {
		MessageLoggingHandlerUtils.logToSystemOut(uriInfo, rsHttpHeaders, this.request,bytes,
				nomeOperazione, this.nomeServizio, GpContext.TIPO_SERVIZIO_GOVPAY_JSON, this.getVersione(), this.log, true, responseCode);
		if(GovpayConfig.getInstance().isValidazioneAPIRestAbilitata() && this.validate) {
//			try {
//				TextHttpResponseEntity httpEntity = new TextHttpResponseEntity();
//				httpEntity.setMethod(HttpRequestMethod.valueOf(this.request.getMethod()));
//				httpEntity.setUrl(getServicePath(uriInfo).toString());
//				httpEntity.setContent(new String(bytes));
//				httpEntity.setContentType("application/json");
//				this.validator.validate(httpEntity);
//			} catch (ProcessingException | ValidatorException | URISyntaxException e) {
//				throw new NotAuthorizedException(e.getMessage());
//			}
		}
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
		if(transactionId != null)
			return responseBuilder.header(this.transactionIdHeaderName, transactionId);
		else 
			return responseBuilder;
	}
	
	protected Response handleException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, Exception e, String transactionId) {
		
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
		try {
			this.logResponse(uriInfo, httpHeaders, methodName, respKo, Status.INTERNAL_SERVER_ERROR.getStatusCode());
		}catch(Exception e1) {
			this.log.error("Errore durante il log della risposta", e1);
		}
		
		String respKoJson = this.getRespJson(respKo);
		 
		if(transactionId != null)
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKoJson).header(this.transactionIdHeaderName, transactionId).build();
		else 
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKoJson).build();
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

	protected String getRespJson(it.govpay.core.rs.v1.beans.pagamenti.FaultBean respKo) {
		String respKoJson = null;
		try {
			respKoJson =respKo.toJSON(null);
		} catch(ServiceException ex) {
			this.log.error(ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN, ex);
			respKoJson = ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN;
		}
		return respKoJson;
	}

	protected String getRespJson(it.govpay.core.rs.v1.beans.pendenze.FaultBean respKo) {
		String respKoJson = null;
		try {
			respKoJson =respKo.toJSON(null);
		} catch(ServiceException ex) {
			this.log.error(ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN, ex);
			respKoJson = ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN;
		}
		return respKoJson;
	}

	protected String getRespJson(it.govpay.core.rs.v1.beans.ragioneria.FaultBean respKo) {
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
		if(e instanceof NotAuthenticatedException || e instanceof NotAuthorizedException) {
			this.log.info("Accesso alla risorsa "+methodName+" non consentito: "+ e.getMessage() + ", " + e.getDetails());
		} else {
			this.log.info("Errore ("+e.getClass().getSimpleName()+") durante "+methodName+": "+ e.getMessage());
		}
		
		FaultBean respKo = new FaultBean();
		respKo.setCategoria(e.getCategoria());
		respKo.setCodice(e.getCode());
		respKo.setDescrizione(e.getMessage());
		respKo.setDettaglio(e.getDetails());
		
		try {
			this.logResponse(uriInfo, httpHeaders, methodName, respKo, e.getTransportErrorCode());
		}catch(Exception e1) {
			this.log.error("Errore durante il log della risposta  "+methodName+":", e1.getMessage(), e);
		}

		String respJson = this.getRespJson(respKo);
		if(transactionId != null)
				return Response.status(e.getTransportErrorCode()).entity(respJson).header(this.transactionIdHeaderName, transactionId).build();
			else
				return Response.status(e.getTransportErrorCode()).entity(respJson).build();
	}

	private Response handleGovpayException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, GovPayException e, String transactionId) {
		this.log.error("Errore ("+e.getClass().getSimpleName()+") durante "+methodName+": "+ e.getMessage());
		FaultBean respKo = new FaultBean();
		if(e.getFaultBean()!=null) {
			respKo.setCategoria(CategoriaEnum.PAGOPA);
			respKo.setCodice(e.getFaultBean().getFaultCode());
			respKo.setDescrizione(e.getFaultBean().getFaultString());
			respKo.setDettaglio(e.getFaultBean().getDescription());
		} else {
			respKo.setCategoria(CategoriaEnum.fromValue(e.getCategoria().name()));
			respKo.setCodice(e.getCodEsitoV3());
			respKo.setDescrizione(e.getDescrizioneEsito());
			respKo.setDettaglio(e.getMessageV3());
			
		}
		
		int statusCode = e.getStatusCode();
		
		try {
			this.logResponse(uriInfo, httpHeaders, methodName, respKo, statusCode);
		}catch(Exception e1) {
			this.log.error("Errore durante il log della risposta  "+methodName+":", e1.getMessage(), e);
		}
		
		String respJson = this.getRespJson(respKo);

		if(transactionId != null)
			return Response.status(statusCode).entity(respJson).header(this.transactionIdHeaderName, transactionId).build();
		else 
			return Response.status(statusCode).entity(respJson).build();
	}
	
	private Response handleValidationException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, ValidationException e, String transactionId) {
		this.log.warn("Richiesta rifiutata per errori di validazione: " + e);
		FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.RICHIESTA);
			respKo.setCodice("SINTASSI");
			respKo.setDescrizione("Richiesta non valida");
			respKo.setDettaglio(e.getMessage());
		
		int statusCode = 400;
		
		try {
			this.logResponse(uriInfo, httpHeaders, methodName, respKo, statusCode);
		}catch(Exception e1) {
			this.log.error("Errore durante il log della risposta  "+methodName+":", e1.getMessage(), e);
		}
		
		String respJson = this.getRespJson(respKo);

		if(transactionId != null)
			return Response.status(statusCode).entity(respJson).header(this.transactionIdHeaderName, transactionId).build();
		else 
			return Response.status(statusCode).entity(respJson).build();
	}

	private Response handleRedirectException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, RedirectException e, String transactionId) {
		this.log.error("Esecuzione del metodo ["+methodName+"] si e' conclusa con un errore: " + e.getMessage() + ", redirect verso la url: " + e.getLocation());
		if(transactionId != null)
			return Response.seeOther(e.getURILocation()).header(this.transactionIdHeaderName, transactionId).build();
		else
			return Response.seeOther(e.getURILocation()).build();
	}


}
