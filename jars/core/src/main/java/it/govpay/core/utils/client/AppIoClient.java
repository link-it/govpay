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
package it.govpay.core.utils.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.core.GenericType;

import org.apache.commons.lang3.ArrayUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.dump.DumpRequest;
import org.openspcoop2.utils.service.context.dump.DumpResponse;
import org.openspcoop2.utils.service.context.server.ServerInfoContextManuallyAdd;
import org.openspcoop2.utils.service.context.server.ServerInfoRequest;
import org.openspcoop2.utils.service.context.server.ServerInfoResponse;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;
import org.slf4j.Logger;
import org.springframework.http.MediaType;

import it.govpay.core.beans.EventoContext;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.appio.client.AppIoAPIClient;
import it.govpay.core.utils.appio.impl.ApiException;
import it.govpay.core.utils.appio.impl.Pair;
import it.govpay.core.utils.appio.impl.auth.ApiKeyAuth;
import it.govpay.core.utils.appio.model.LimitedProfile;
import it.govpay.core.utils.appio.model.MessageCreated;
import it.govpay.core.utils.appio.model.NewMessage;
import it.govpay.core.utils.client.beans.TipoDestinatario;
import it.govpay.core.utils.client.exception.ClientInitializeException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.configurazione.AppIOBatch;
import it.govpay.model.configurazione.Giornale;

public class AppIoClient extends BasicClientCORE {

	private static final String HEADER_LOCATION = "Location";
	private static final String PATH_PROFILES_FISCAL_CODE = "/profiles/{fiscal_code}";
	private static final String PATH_MESSAGES = "/messages";
	private static final String STATUS_LINE = "Status-line";
	private static final String REQUEST_PATH = "RequestPath";
	private static final String HTTP_METHOD = "HTTP-Method";
	private static final String SUBSCRIPTION_KEY = "SubscriptionKey";
	
	private static Logger log = LoggerWrapperFactory.getLogger(AppIoClient.class);
	private AppIoAPIClient apiClient = null;

	public AppIoClient(String operazioneSwaggerAppIO, AppIOBatch appIo, String operationID, Giornale giornale, EventoContext eventoCtx) throws ClientInitializeException { 
		super(operazioneSwaggerAppIO, TipoDestinatario.APP_IO, appIo, eventoCtx); 

		this.apiClient = new AppIoAPIClient();
		this.apiClient.setBasePath(this.url.toExternalForm());
		// Imposto timeout specifici per AppIO
		if(this.connectionTimeout != null) {
			this.apiClient.setConnectTimeout(this.connectionTimeout);
		}
		if(this.readTimeout != null) {
			this.apiClient.setReadTimeout(this.readTimeout);
		}

		this.operationID = operationID;
		this.setGiornale(giornale);
	}

	public LimitedProfile getProfile(String fiscalCode, String appIOAPIKey, String swaggerOperationId) throws ApiException {
		ApiKeyAuth subscriptionKey = (ApiKeyAuth) this.apiClient.getAuthentication(SUBSCRIPTION_KEY);
		subscriptionKey.setApiKey(appIOAPIKey);

		// Salvataggio Tipo Evento
		this.getEventoCtx().setTipoEvento(swaggerOperationId);
		HttpMethod httpMethodEnum = fromHttpMethod(HttpRequestMethod.GET);

		int responseCode = 0;
		DumpRequest dumpRequest = new DumpRequest();
		DumpResponse dumpResponse = new DumpResponse();
		ServerInfoResponse serverInfoResponse = new ServerInfoResponse();
		Map<String, List<String>> headerFields = null;
		byte[] msg = null;

		try {
			Object localVarPostBody = null;

			IContext ctx = ContextThreadLocal.get();

			ServerInfoRequest serverInfoRequest = new ServerInfoRequest();

			// create path and map variables
			String localVarPath = fiscalCode != null ? PATH_PROFILES_FISCAL_CODE.replaceAll("\\{" + "fiscal_code" + "\\}", apiClient.escapeString(fiscalCode)) : PATH_PROFILES_FISCAL_CODE;

			// Url Completa che viene invocata
			String urlString = this.url.toExternalForm();
			if(urlString.endsWith("/")) urlString = urlString.substring(0, urlString.length() - 1);
			urlString = urlString.concat(localVarPath);

			this.getEventoCtx().setUrl(urlString);

			this.serverInfoContext = new ServerInfoContextManuallyAdd(this.getServerConfig(ctx));
			serverInfoRequest.setAddress(urlString);
			serverInfoRequest.setHttpRequestMethod(HttpRequestMethod.GET);

			// verify the required parameter 'fiscalCode' is set
			if (fiscalCode == null) {
				throw new ApiException(400, "Missing the required parameter 'fiscalCode' when calling getProfile");
			}

			// query params
			List<Pair> localVarQueryParams = new ArrayList<>();
			Map<String, String> localVarHeaderParams = new HashMap<>();
			Map<String, Object> localVarFormParams = new HashMap<>();

			final String[] localVarAccepts = { MediaType.APPLICATION_JSON_VALUE };
			final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

			final String[] localVarContentTypes = { };
			final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

			// Salvataggio content type ed header con l'apikey
			dumpRequest.getHeaders().put(subscriptionKey.getParamName(), subscriptionKey.getApiKey());
			dumpRequest.setPayload("".getBytes());

			dumpRequest.getHeaders().put(HTTP_METHOD, httpMethodEnum.name());
			dumpRequest.getHeaders().put(REQUEST_PATH, urlString);

			this.serverInfoContext.processBeforeSend(serverInfoRequest, dumpRequest);

			dumpResponse.getHeaders().put(HTTP_METHOD, httpMethodEnum.name());
			dumpResponse.getHeaders().put(REQUEST_PATH, urlString);

			String[] localVarAuthNames = new String[] { SUBSCRIPTION_KEY };

			GenericType<LimitedProfile> localVarReturnType = new GenericType<LimitedProfile>() {};
			LimitedProfile limitedProfile = apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);

			Map<String, List<String>> responseHeaders = apiClient.getResponseHeaders();
			responseCode = apiClient.getStatusCode();

			for(String key : responseHeaders.keySet()) {
				if(responseHeaders.get(key) != null) {
					if(responseHeaders.get(key).size() == 1) {
						dumpResponse.getHeaders().put(key, responseHeaders.get(key).get(0));
					}else {
						dumpResponse.getHeaders().put(key, ArrayUtils.toString(responseHeaders.get(key)));
					}
				}
			}
			dumpResponse.getHeaders().put(STATUS_LINE, ""+responseCode);

			try {
				String msgRes = ConverterUtils.toJSON(limitedProfile);
				msg = msgRes != null ? msgRes.getBytes() : new byte[]{};
			} catch (IOException e) {
				log.warn("Errore durante la serializzazione del messaggio di risposta per il giornale eventi: " + e.getMessage(), e);
			}

			return limitedProfile;
		} catch (ApiException e) {
			responseCode = e.getCode();
			Map<String, List<String>> responseHeaders = e.getResponseHeaders();
			for(String key : responseHeaders.keySet()) {
				if(responseHeaders.get(key) != null) {
					if(responseHeaders.get(key).size() == 1) {
						dumpResponse.getHeaders().put(key, responseHeaders.get(key).get(0));
					}else {
						dumpResponse.getHeaders().put(key, ArrayUtils.toString(responseHeaders.get(key)));
					}
				}
			}
			dumpResponse.getHeaders().put(STATUS_LINE, ""+responseCode);
			String messaggio = e.getResponseBody();
			if(messaggio == null) {
				messaggio = e.getMessage();
			}
			
			msg = messaggio != null ? messaggio.getBytes() : new byte[]{};
			throw e;
		} finally {
			serverInfoResponse.setResponseCode(responseCode);
			this.serverInfoContext.processAfterSend(serverInfoResponse, dumpResponse);
			if(msg != null && msg.length > 0) dumpResponse.setPayload(msg);
			if(log.isTraceEnabled() && headerFields != null) {
				StringBuilder sb = new StringBuilder();
				for(String key : headerFields.keySet()) { 
					sb.append("\n\t" + key + ": " + headerFields.get(key));
				}
				sb.append("\n" + new String(msg));
				log.trace(sb.toString());
			}
			this.popolaContextEvento(httpMethodEnum, responseCode, dumpRequest, dumpResponse);
		}
	}

	public MessageCreated postMessage(NewMessage messageWithCF, String appIOAPIKey, String swaggerOperationId) throws ApiException {
		ApiKeyAuth subscriptionKey = (ApiKeyAuth) apiClient.getAuthentication(SUBSCRIPTION_KEY);
		subscriptionKey.setApiKey(appIOAPIKey);

		// Salvataggio Tipo Evento
		this.getEventoCtx().setTipoEvento(swaggerOperationId);
		HttpMethod httpMethodEnum = fromHttpMethod(HttpRequestMethod.POST);

		int responseCode = 0;
		DumpRequest dumpRequest = new DumpRequest();
		DumpResponse dumpResponse = new DumpResponse();
		ServerInfoResponse serverInfoResponse = new ServerInfoResponse();
		Map<String, List<String>> headerFields = null;
		byte[] msg = null;
		try {
			// create path and map variables
			String localVarPath = PATH_MESSAGES;

			IContext ctx = ContextThreadLocal.get();

			ServerInfoRequest serverInfoRequest = new ServerInfoRequest();

			// Url Completa che viene invocata
			String urlString = this.url.toExternalForm();
			if(urlString.endsWith("/")) urlString = urlString.substring(0, urlString.length() - 1);
			urlString = urlString.concat(localVarPath);

			this.getEventoCtx().setUrl(urlString);

			this.serverInfoContext = new ServerInfoContextManuallyAdd(this.getServerConfig(ctx));
			serverInfoRequest.setAddress(urlString);
			serverInfoRequest.setHttpRequestMethod(HttpRequestMethod.POST);

			// query params
			List<Pair> localVarQueryParams = new ArrayList<>();
			Map<String, String> localVarHeaderParams = new HashMap<>();
			Map<String, Object> localVarFormParams = new HashMap<>();

			final String[] localVarAccepts = { MediaType.APPLICATION_JSON_VALUE };
			final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

			final String[] localVarContentTypes = { MediaType.APPLICATION_JSON_VALUE };
			final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

			String jsonBody = null;
			try {
				jsonBody = ConverterUtils.toJSON(messageWithCF);
			} catch (IOException e) {
				log.warn("Errore durante la serializzazione del messaggio di richiesta per il giornale eventi: " + e.getMessage(), e);
			} 

			// Salvataggio content type ed header con l'apikey
			dumpRequest.setContentType(localVarContentTypes[0]);
			dumpRequest.getHeaders().put(subscriptionKey.getParamName(), subscriptionKey.getApiKey());


			dumpRequest.setPayload(jsonBody != null ? jsonBody.getBytes() : "".getBytes());

			dumpRequest.getHeaders().put(HTTP_METHOD, httpMethodEnum.name());
			dumpRequest.getHeaders().put(REQUEST_PATH, urlString);

			this.serverInfoContext.processBeforeSend(serverInfoRequest, dumpRequest);

			dumpResponse.getHeaders().put(HTTP_METHOD, httpMethodEnum.name());
			dumpResponse.getHeaders().put(REQUEST_PATH, urlString);

			String[] localVarAuthNames = new String[] { SUBSCRIPTION_KEY };

			GenericType<MessageCreated> localVarReturnType = new GenericType<MessageCreated>() {};
			MessageCreated createdMessage = apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, messageWithCF, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);

			Map<String, List<String>> responseHeaders = apiClient.getResponseHeaders();
			responseCode = apiClient.getStatusCode();

			for(String key : responseHeaders.keySet()) {
				if(responseHeaders.get(key) != null) {
					if(responseHeaders.get(key).size() == 1) {
						dumpResponse.getHeaders().put(key, responseHeaders.get(key).get(0));
					}else {
						dumpResponse.getHeaders().put(key, ArrayUtils.toString(responseHeaders.get(key)));
					}
				}
			}
			dumpResponse.getHeaders().put(STATUS_LINE, ""+responseCode);

			try {
				String msgRes = ConverterUtils.toJSON(createdMessage);
				msg = msgRes != null ? msgRes.getBytes() : new byte[]{};
			} catch (IOException e) {
				log.warn("Errore durante la serializzazione del messaggio di risposta per il giornale eventi: " + e.getMessage(), e);
			}

			return createdMessage;
		} catch (ApiException e) {
			responseCode = e.getCode();
			Map<String, List<String>> responseHeaders = e.getResponseHeaders();
			for(String key : responseHeaders.keySet()) {
				if(responseHeaders.get(key) != null) {
					if(responseHeaders.get(key).size() == 1) {
						dumpResponse.getHeaders().put(key, responseHeaders.get(key).get(0));
					}else {
						dumpResponse.getHeaders().put(key, ArrayUtils.toString(responseHeaders.get(key)));
					}
				}
			}
			dumpResponse.getHeaders().put(STATUS_LINE, ""+responseCode);
			String messaggio = e.getResponseBody();
			if(messaggio == null) {
				messaggio = e.getMessage();
			}
			
			msg = messaggio != null ? messaggio.getBytes() : new byte[]{};
			throw e;
		} finally {
			serverInfoResponse.setResponseCode(responseCode);
			this.serverInfoContext.processAfterSend(serverInfoResponse, dumpResponse);
			if(msg != null && msg.length > 0) dumpResponse.setPayload(msg);
			if(log.isTraceEnabled() && headerFields != null) {
				StringBuilder sb = new StringBuilder();
				for(String key : headerFields.keySet()) { 
					sb.append("\n\t" + key + ": " + headerFields.get(key));
				}
				sb.append("\n" + new String(msg));
				log.trace(sb.toString());
			}
			this.popolaContextEvento(httpMethodEnum, responseCode, dumpRequest, dumpResponse);
		}
	}

	public String getMessageLocation() {
		return this.apiClient.getResponseHeaders().containsKey(HEADER_LOCATION) ? this.apiClient.getResponseHeaders().get(HEADER_LOCATION).get(0) : "";
	}

	@Override
	public String getOperationId() {
		return this.operationID;
	}
}
