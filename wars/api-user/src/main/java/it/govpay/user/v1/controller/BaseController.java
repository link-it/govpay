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
/**
 *
 */
package it.govpay.user.v1.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.Costanti;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.EventoContext.Esito;
import it.govpay.core.dao.commons.exception.RedirectException;
import it.govpay.core.exceptions.BaseExceptionV1;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.LogUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.user.v1.beans.FaultBean;
import it.govpay.user.v1.beans.FaultBean.CategoriaEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 22 feb 2018 $
 *
 */
public abstract class BaseController {

	private static final String SEMANTICA = "SEMANTICA";
	private static final String RICHIESTA_NON_VALIDA = "Richiesta non valida";
	private static final String SINTASSI = "SINTASSI";
	public static final String PARAMETRO_CONTENT_DISPOSITION = "Content-Disposition";
	public static final String PREFIX_CONTENT_DISPOSITION = "form-data; name=\"";
	public static final String SUFFIX_CONTENT_DISPOSITION = "\"";
	public static final String PREFIX_FILENAME = "filename=\"";
	public static final String SUFFIX_FILENAME = "\"";

	private static final String ERRORE_DURANTE_LA_SERIALIZZAZIONE_DEL_FAULT_BEAN = "Errore durante la serializzazione del FaultBean";
	public static final String LOG_MSG_ESECUZIONE_METODO_COMPLETATA = "Esecuzione {} completata.";
	public static final String LOG_MSG_ESECUZIONE_METODO_IN_CORSO = "Esecuzione {} in corso...";
	protected Logger log;
	protected String nomeServizio;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected String transactionIdHeaderName = Costanti.HEADER_NAME_OUTPUT_TRANSACTION_ID;
	protected IContext context;

	protected BaseController(String nomeServizio, Logger log) {
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

	protected Response handleException(String methodName, Exception e, String transactionId) {

		if(e instanceof UnprocessableEntityException unprocessableEntityException) {
			return this.handleUnprocessableEntityException(methodName, unprocessableEntityException, transactionId);
		}

		if(e instanceof BaseExceptionV1 baseExceptionV1) {
			return this.handleBaseException(methodName, baseExceptionV1, transactionId);
		}

		if(e instanceof RedirectException redirectException) {
			return this.handleRedirectException(methodName, redirectException, transactionId);
		}

		if(e instanceof GovPayException govPayException) {
			return this.handleGovpayException(methodName, govPayException, transactionId);
		}

		if(e instanceof ValidationException validationException) {
			return this.handleValidationException(validationException, transactionId);
		}

		if(e instanceof IOException ioException) {
			return this.handleIOException(ioException, transactionId);
		}

		this.logError("Errore interno durante "+methodName+": " + e.getMessage(), e);
		FaultBean respKo = new FaultBean();
		respKo.setCategoria(CategoriaEnum.INTERNO);
		respKo.setCodice(EsitoOperazione.INTERNAL.toString());
		respKo.setDescrizione("Errore interno");
		respKo.setDettaglio(e.getMessage());
		String respKoJson = this.getRespJson(respKo);

		ResponseBuilder responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity(respKoJson);
		this.handleEventoFail(responseBuilder, transactionId, respKo.getCodice(),  respKo.getDettaglio(), e);
		return handleResponseKo(responseBuilder, transactionId).build();
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

	private Response handleBaseException(String methodName, BaseExceptionV1 e, String transactionId) {
		FaultBean respKo = new FaultBean();
		respKo.setCategoria(FaultBean.CategoriaEnum.fromValue(e.getCategoria().name()));
		respKo.setCodice(e.getCode());
		respKo.setDescrizione(e.getMessage());
		respKo.setDettaglio(e.getDetails());

		String sottotipoEsito = respKo.getCodice();
		if(e instanceof NotAuthenticatedException || e instanceof NotAuthorizedException) {
			this.logInfo("Accesso alla risorsa "+methodName+" non consentito: "+ e.getMessage() + ", " + e.getDetails());
			sottotipoEsito = CategoriaEnum.AUTORIZZAZIONE.name();
		} else {
			this.logInfo(MessageFormat.format("Errore ({0}) durante {1}: {2}", e.getClass().getSimpleName(), methodName, e.getMessage()));
		}

		String respJson = this.getRespJson(respKo);
		ResponseBuilder responseBuilder = Response.status(e.getTransportErrorCode()).type(MediaType.APPLICATION_JSON).entity(respJson);
		if(e.getTransportErrorCode() > 499)
			this.handleEventoFail(responseBuilder, transactionId, sottotipoEsito, respKo.getDettaglio(), e);
		else
			this.handleEventoKo(responseBuilder, transactionId, sottotipoEsito, respKo.getDettaglio(), e);
		return handleResponseKo(responseBuilder, transactionId).build();
	}

	private Response handleGovpayException(String methodName, GovPayException e, String transactionId) {
		this.logError(MessageFormat.format("Errore ({0}) durante {1}: {2}", e.getClass().getSimpleName(), methodName, e.getMessage()), e);
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
		ResponseBuilder responseBuilder = Response.status(statusCode).type(MediaType.APPLICATION_JSON).entity(respJson);
		if(statusCode > 499)
			this.handleEventoFail(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio(), e);
		else
			this.handleEventoKo(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio(), e);

		return handleResponseKo(responseBuilder, transactionId).build();
	}

	private Response handleValidationException(ValidationException e, String transactionId) {
		this.logWarn("Richiesta rifiutata per errori di validazione: " + e);
		FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.RICHIESTA);
			respKo.setCodice(SINTASSI);
			respKo.setDescrizione(RICHIESTA_NON_VALIDA);
			respKo.setDettaglio(e.getMessage());

		int statusCode = 400;

		String respJson = this.getRespJson(respKo);
		ResponseBuilder responseBuilder = Response.status(statusCode).type(MediaType.APPLICATION_JSON).entity(respJson);
		this.handleEventoKo(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio(), e);
		return handleResponseKo(responseBuilder, transactionId).build();
	}

	private Response handleIOException(IOException e, String transactionId) {
		this.logWarn("Richiesta rifiutata per errori di validazione: " + e);
		FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.RICHIESTA);
			respKo.setCodice(SINTASSI);
			respKo.setDescrizione(RICHIESTA_NON_VALIDA);
			respKo.setDettaglio(e.getMessage());

		int statusCode = 400;
		String respJson = this.getRespJson(respKo);

		ResponseBuilder responseBuilder = Response.status(statusCode).type(MediaType.APPLICATION_JSON).entity(respJson);
		this.handleEventoKo(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio(), e);
		return handleResponseKo(responseBuilder, transactionId).build();
	}

	private Response handleRedirectException(String methodName, RedirectException e, String transactionId) {
		this.logError("Esecuzione del metodo ["+methodName+"] si e' conclusa con un errore: " + e.getMessage() + ", redirect verso la url: " + e.getLocation());
		ResponseBuilder responseBuilder = Response.seeOther(e.getURILocation());
		this.handleEventoOk(responseBuilder, transactionId);
		if(transactionId != null)
			return responseBuilder.header(this.transactionIdHeaderName, transactionId).build();
		else
			return responseBuilder.build();
	}

	private Response handleUnprocessableEntityException(String methodName, UnprocessableEntityException e, String transactionId) {
		this.logInfo("Errore ("+e.getClass().getSimpleName()+") durante "+methodName+": "+ e.getMessage());

		FaultBean respKo = new FaultBean();
		respKo.setCategoria(CategoriaEnum.RICHIESTA);
		respKo.setCodice(SEMANTICA);
		respKo.setDescrizione(RICHIESTA_NON_VALIDA);
		respKo.setDettaglio(e.getDetails());

		String respJson = this.getRespJson(respKo);
		ResponseBuilder responseBuilder = Response.status(e.getTransportErrorCode()).type(MediaType.APPLICATION_JSON).entity(respJson);
		if(e.getTransportErrorCode() > 499)
			this.handleEventoFail(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio(), e);
		else
			this.handleEventoKo(responseBuilder, transactionId, respKo.getCodice(), respKo.getDettaglio(), e);

		return handleResponseKo(responseBuilder, transactionId).build();
	}

	protected void logContext(IContext ctx) {
		if(ctx != null) {
			// donothing
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

	protected ResponseBuilder handleEventoKo(ResponseBuilder responseBuilder, String transactionId, String sottotipoEsito, String dettaglioEsito, Exception exception) {
		GpContext ctx = (GpContext) this.context.getApplicationContext();
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
		GpContext ctx = (GpContext) this.context.getApplicationContext();
		ctx.getEventoCtx().setEsito(Esito.FAIL);
		if(transactionId != null)
			ctx.getEventoCtx().setIdTransazione(transactionId);
		ctx.getEventoCtx().setDescrizioneEsito(dettaglioEsito);

		if(sottotipoEsito != null)
			ctx.getEventoCtx().setSottotipoEsito(sottotipoEsito);

		ctx.getEventoCtx().setException(exception);

		return responseBuilder;
	}

	protected Map<String, String> getHeaders(HttpServletRequest request) {
		Map<String, String> result = new HashMap<>();

		if (request == null) {
			return result;
		}

		Enumeration<String> headerNames = request.getHeaderNames();

		if (headerNames == null) {
			return result;
		}

		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			Enumeration<String> headerValues = request.getHeaders(headerName);

			if(headerValues != null) {
				List<String> values = new ArrayList<>();
				while (headerValues.hasMoreElements()) {
					String value = headerValues.nextElement();
					values.add(value);
				}
				result.put(headerName, String.join(",", values));
			}
		}
		return result;
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
