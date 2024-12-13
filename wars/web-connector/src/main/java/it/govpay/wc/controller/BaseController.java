/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
/**
 * 
 */
package it.govpay.wc.controller;

import java.net.URI;
import java.net.URISyntaxException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.core.beans.Costanti;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.dao.commons.exception.RedirectException;
import it.govpay.core.exceptions.BaseExceptionV1;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.LogUtils;
import it.govpay.wc.beans.FaultBean;
import it.govpay.wc.beans.FaultBean.CategoriaEnum;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 22 feb 2018 $
 * 
 */
public abstract class BaseController {

	public static final String PARAMETRO_CONTENT_DISPOSITION = "Content-Disposition";
	public static final String PREFIX_CONTENT_DISPOSITION = "form-data; name=\"";
	public static final String SUFFIX_CONTENT_DISPOSITION = "\"";
	public static final String PREFIX_FILENAME = "filename=\"";
	public static final String SUFFIX_FILENAME = "\"";

	private static final String ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN = "Errore durante la serializzazione del FaultBean"; 
	public static final String LOG_MSG_ESECUZIONE_METODO_COMPLETATA = "Esecuzione {0} completata.";
	public static final String LOG_MSG_ESECUZIONE_METODO_IN_CORSO = "Esecuzione {0} in corso...";
	protected Logger log;
	protected String nomeServizio;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected String transactionIdHeaderName = Costanti.HEADER_NAME_OUTPUT_TRANSACTION_ID;
	protected IContext context;
	
	public BaseController(String nomeServizio, Logger log){
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
		
		this.logError("Errore interno durante "+methodName+": " + e.getMessage(), e);
		FaultBean respKo = new FaultBean();
		respKo.setCategoria(CategoriaEnum.INTERNO);
		respKo.setCodice(EsitoOperazione.INTERNAL.toString());
		respKo.setDescrizione("Errore interno");
		respKo.setDettaglio(e.getMessage());
		
		String respKoJson = this.getRespJson(respKo);
		 
		return handleResponseKo(Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity(respKoJson), transactionId).build();
	}

	protected String getRespJson(FaultBean respKo) {
		String respKoJson = null;
		try {
			respKoJson =respKo.toJSON(null);
		} catch(IOException ex) {
			this.logError(ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN, ex);
			respKoJson = ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN;
		}
		return respKoJson;
	}

	private Response handleBaseException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, BaseExceptionV1 e, String transactionId) {
		if(e instanceof NotAuthenticatedException || e instanceof NotAuthorizedException) {
			this.logInfo("Accesso alla risorsa "+methodName+" non consentito: "+ e.getMessage() + ", " + e.getDetails());
		} else {
			this.logInfo("Errore ("+e.getClass().getSimpleName()+") durante "+methodName+": "+ e.getMessage());
		}
		
		FaultBean respKo = new FaultBean();
		respKo.setCategoria(CategoriaEnum.fromValue(e.getCategoria().name()));
		respKo.setCodice(e.getCode());
		respKo.setDescrizione(e.getMessage());
		respKo.setDettaglio(e.getDetails());

		String respJson = this.getRespJson(respKo);
		return handleResponseKo(Response.status(e.getTransportErrorCode()).type(MediaType.APPLICATION_JSON).entity(respJson), transactionId).build();
	}

	private Response handleGovpayException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, GovPayException e, String transactionId) {
		this.logError("Errore ("+e.getClass().getSimpleName()+") durante "+methodName+": "+ e.getMessage(), e);
		FaultBean respKo = new FaultBean();
		int statusCode = e.getStatusCode();
		if(e.getFaultBean()!=null) {
			respKo.setCategoria(CategoriaEnum.PAGOPA);
			respKo.setCodice(e.getFaultBean().getFaultCode());
			respKo.setDescrizione(e.getFaultBean().getFaultString());
			respKo.setDettaglio(e.getFaultBean().getDescription());
			statusCode = 502; // spostato dalla govpayException perche' ci sono dei casi di errore che non devono restituire 500
		} else {
			respKo.setCategoria(CategoriaEnum.fromValue(e.getCategoria().name()));
			respKo.setCodice(e.getCodEsitoV3());
			respKo.setDescrizione(e.getDescrizioneEsito());
			respKo.setDettaglio(e.getMessageV3());
		}
		
		String respJson = this.getRespJson(respKo);
		
		return handleResponseKo(Response.status(statusCode).type(MediaType.APPLICATION_JSON).entity(respJson), transactionId).build();
	}
	
	private Response handleValidationException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, ValidationException e, String transactionId) {
		this.logWarn("Richiesta rifiutata per errori di validazione: " + e);
		FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.RICHIESTA);
			respKo.setCodice("SINTASSI");
			respKo.setDescrizione("Richiesta non valida");
			respKo.setDettaglio(e.getMessage());
		
		int statusCode = 400;
		
		String respJson = this.getRespJson(respKo);
		
		return handleResponseKo(Response.status(statusCode).type(MediaType.APPLICATION_JSON).entity(respJson), transactionId).build();
	}

	private Response handleRedirectException(UriInfo uriInfo, HttpHeaders httpHeaders, String methodName, RedirectException e, String transactionId) {
		this.logError("Esecuzione del metodo ["+methodName+"] si e' conclusa con un errore: " + e.getMessage() + ", redirect verso la url: " + e.getLocation());
		if(transactionId != null)
			return Response.seeOther(e.getURILocation()).header(this.transactionIdHeaderName, transactionId).build();
		else
			return Response.seeOther(e.getURILocation()).build();
	}

	protected void logContext(IContext ctx) {
		if(ctx != null) {
			// donothing
		}
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
