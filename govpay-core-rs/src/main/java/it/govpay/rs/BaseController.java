/**
 * 
 */
package it.govpay.rs;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jackson.map.ObjectMapper;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.json.JsonValidatorAPI.ApiName;
import org.openspcoop2.utils.openapi.validator.OpenapiApiValidatorConfig;
import org.openspcoop2.utils.openapi.validator.Test;
import org.openspcoop2.utils.openapi.validator.Validator;
import org.openspcoop2.utils.rest.ApiFactory;
import org.openspcoop2.utils.rest.ApiFormats;
import org.openspcoop2.utils.rest.ApiReaderConfig;
import org.openspcoop2.utils.rest.IApiReader;
import org.openspcoop2.utils.rest.ProcessingException;
import org.openspcoop2.utils.rest.ValidatorException;
import org.openspcoop2.utils.rest.api.Api;
import org.openspcoop2.utils.rest.entity.Cookie;
import org.openspcoop2.utils.rest.entity.TextHttpRequestEntity;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.core.dao.commons.exception.RedirectException;
import it.govpay.core.exceptions.BaseExceptionV1;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.rs.v1.beans.JSONSerializable;
import it.govpay.core.rs.v1.beans.base.FaultBean;
import it.govpay.core.rs.v1.beans.base.FaultBean.CategoriaEnum;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.log.MessageLoggingHandlerUtils;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 22 feb 2018 $
 * 
 */
public abstract class BaseController {

	protected Logger log;
	protected String nomeServizio;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	private Validator validator;
	private boolean validate;

	public BaseController(String nomeServizio, Logger log) {
		this(nomeServizio, log, true);
	}

	public BaseController(String nomeServizio, Logger log, boolean validate) {
		this.log = log;
		this.nomeServizio = nomeServizio;
		this.validate = validate;
		
		if(GovpayConfig.getInstance().isValidazioneAPIRestAbilitata()) {
			try {
				IApiReader apiReader = ApiFactory.newApiReader(ApiFormats.OPEN_API_3);

				File file = new File(GovpayConfig.getInstance().getResourceDir(), GovpayConfig.GOVPAY_OPEN_API_FILE);
				apiReader.init(log, file, new ApiReaderConfig());
				Api api = apiReader.read();
				
				this.validator = (Validator) ApiFactory.newApiValidator(ApiFormats.OPEN_API_3);
				OpenapiApiValidatorConfig config = new OpenapiApiValidatorConfig();
				config.setJsonValidatorAPI(ApiName.FGE);
				validator.init(LoggerWrapperFactory.getLogger(Test.class), api, config);
			} catch(Throwable e) {
				this.log.error("Errore durante l'init del modulo di validazione: " + e.getMessage(), e);
			}
		}

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
	
	public void logResponse(UriInfo uriInfo, HttpHeaders rsHttpHeaders, String nomeOperazione, Object o) throws IOException, NotAuthorizedException {
		this.logResponse(uriInfo, rsHttpHeaders, nomeOperazione, o, null);
	}
	

	public void logResponse(UriInfo uriInfo, HttpHeaders rsHttpHeaders, String nomeOperazione, Object o, Integer responseCode) throws IOException, NotAuthorizedException {
		if(o != null && o instanceof JSONSerializable) {
			this.logResponse(uriInfo, rsHttpHeaders, nomeOperazione, ((JSONSerializable) o).toJSON(null).getBytes(), responseCode);
		}
		else {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(o);
			this.logResponse(uriInfo, rsHttpHeaders, nomeOperazione, json.getBytes(), responseCode);
		}
	}
	
	public void logRequest(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione, ByteArrayOutputStream baos) throws NotAuthorizedException {
		MessageLoggingHandlerUtils.logToSystemOut(uriInfo, rsHttpHeaders, this.request,baos,
				nomeOperazione, this.nomeServizio, GpContext.TIPO_SERVIZIO_GOVPAY_JSON, this.getVersione(), this.log, false);
		if(GovpayConfig.getInstance().isValidazioneAPIRestAbilitata() && this.validate) {
			validateRequest(uriInfo, rsHttpHeaders, baos.toByteArray());
		}
	}
	
	public void logRequest(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione, byte[] baos) throws NotAuthorizedException{
		MessageLoggingHandlerUtils.logToSystemOut(uriInfo, rsHttpHeaders, this.request,baos,
				nomeOperazione, this.nomeServizio, GpContext.TIPO_SERVIZIO_GOVPAY_JSON, this.getVersione(), this.log, false);
		if(GovpayConfig.getInstance().isValidazioneAPIRestAbilitata() && this.validate) {
			validateRequest(uriInfo, rsHttpHeaders, baos);
		}
	}

	private void validateRequest(UriInfo uriInfo, HttpHeaders rsHttpHeaders, byte[] baos) throws NotAuthorizedException {
		try {
			TextHttpRequestEntity httpEntity = new TextHttpRequestEntity();
			httpEntity.setMethod(HttpRequestMethod.valueOf(this.request.getMethod()));
			if(rsHttpHeaders.getCookies()!=null) {
				List<Cookie> cookies = new ArrayList<Cookie>();
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
			httpEntity.setUrl(getServicePathWithoutParameters(uriInfo).toString());
			httpEntity.setContent(new String(baos));
			httpEntity.setContentType(this.request.getContentType());
			this.validator.validate(httpEntity);
		} catch (ProcessingException | ValidatorException | URISyntaxException e) {
			this.log.error("Errore di validazione di richiesta: " + e.getMessage(), e);
			throw new NotAuthorizedException(e.getMessage());
		}
	}

	public void logResponse(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione, ByteArrayOutputStream baos) throws NotAuthorizedException {
		MessageLoggingHandlerUtils.logToSystemOut(uriInfo, rsHttpHeaders, this.request,baos,
				nomeOperazione, this.nomeServizio, GpContext.TIPO_SERVIZIO_GOVPAY_JSON, this.getVersione(), this.log, true);
		if(GovpayConfig.getInstance().isValidazioneAPIRestAbilitata() && this.validate) {
//			try {
//				TextHttpResponseEntity httpEntity = new TextHttpResponseEntity();
//				httpEntity.setMethod(HttpRequestMethod.valueOf(this.request.getMethod()));
//				httpEntity.setUrl(getServicePath(uriInfo).toString());
//				httpEntity.setContent(new String(baos.toByteArray()));
//				httpEntity.setContentType("application/json");
//				this.validator.validate(httpEntity);
//			} catch (ProcessingException | ValidatorException | URISyntaxException e) {
//				throw new NotAuthorizedException(e.getMessage());
//			}
		}
	}
	
	public void logResponse(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione,byte[] bytes) throws NotAuthorizedException {
		MessageLoggingHandlerUtils.logToSystemOut(uriInfo, rsHttpHeaders, this.request,bytes,
				nomeOperazione, this.nomeServizio, GpContext.TIPO_SERVIZIO_GOVPAY_JSON, this.getVersione(), this.log, true);
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
	
	public void logResponse(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione,byte[] bytes, Integer responseCode) throws NotAuthorizedException {
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

		URI servicePathwithParameters = getServicePath(uriInfo);
		String baseUri = servicePathwithParameters.toString();

		int indexOf = baseUri.indexOf("?");
		if(indexOf> 0) {
			return new URI(baseUri.substring(0, indexOf));
		} else {
			return new URI(baseUri);
		}
	}
	
	protected Response handleException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, Exception e) {
		
		if(e instanceof BaseExceptionV1) {
			return handleBaseException(uriInfo, httpHeaders, methodName, (BaseExceptionV1)e);
		}
		
		if(e instanceof RedirectException) {
			return handleRedirectException(uriInfo, httpHeaders, methodName, (RedirectException)e);
		}
		
		if(e instanceof GovPayException) {
			return handleGovpayException(uriInfo, httpHeaders, methodName, (GovPayException)e);
		}
		
		log.error("Errore interno durante "+methodName+": " + e.getMessage(), e);
		FaultBean respKo = new FaultBean();
		respKo.setCategoria(CategoriaEnum.INTERNO);
		respKo.setCodice("");
		respKo.setDescrizione(e.getMessage());
		try {
			this.logResponse(uriInfo, httpHeaders, methodName, respKo.toJSON(null), Status.INTERNAL_SERVER_ERROR.getStatusCode());
		}catch(Exception e1) {
			log.error("Errore durante il log della risposta", e1);
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo.toJSON(null)).build();
	}

	private Response handleBaseException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, BaseExceptionV1 e) {
		log.error("Errore ("+e.getClass().getSimpleName()+") durante "+methodName+": "+ e.getMessage(), e);
		FaultBean respKo = new FaultBean();
		respKo.setCategoria(e.getCategoria());
		respKo.setCodice(e.getCode());
		respKo.setDescrizione(e.getMessage());
		respKo.setDettaglio(e.getDetails());
		try {
			this.logResponse(uriInfo, httpHeaders, methodName, respKo.toJSON(null), e.getTransportErrorCode());
		}catch(Exception e1) {
			log.error("Errore durante il log della risposta  "+methodName+":", e1.getMessage(), e);
		}
		return Response.status(e.getTransportErrorCode()).entity(respKo.toJSON(null)).build();
	}

	private Response handleGovpayException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, GovPayException e) {
		log.error("Errore ("+e.getClass().getSimpleName()+") durante "+methodName+": "+ e.getMessage(), e);
		FaultBean respKo = new FaultBean();
		respKo.setCategoria(e.getFaultBean()!=null ? CategoriaEnum.PAGOPA:CategoriaEnum.OPERAZIONE);
		respKo.setCodice(e.getCodEsito().toString());
		respKo.setDescrizione(e.getMessage());
		respKo.setDettaglio(e.getDescrizioneEsito());
		try {
			this.logResponse(uriInfo, httpHeaders, methodName, respKo.toJSON(null), Status.INTERNAL_SERVER_ERROR.getStatusCode());
		}catch(Exception e1) {
			log.error("Errore durante il log della risposta  "+methodName+":", e1.getMessage(), e);
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity(respKo.toJSON(null)).build();
	}

	private Response handleRedirectException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, RedirectException e) {
		log.error("Esecuzione del metodo ["+methodName+"] si e' conclusa con un errore: " + e.getMessage() + ", redirect verso la url: " + e.getLocation(), e);
		return Response.seeOther(e.getURILocation()).build();
	}


}
