package it.govpay.core.utils.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericType;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.beans.HttpMethodEnum;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.dump.DumpRequest;
import org.openspcoop2.utils.service.context.dump.DumpResponse;
import org.openspcoop2.utils.service.context.server.ServerInfoContextManuallyAdd;
import org.openspcoop2.utils.service.context.server.ServerInfoRequest;
import org.openspcoop2.utils.service.context.server.ServerInfoResponse;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;
import org.slf4j.Logger;

import it.govpay.bd.configurazione.model.AppIO;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.core.utils.EventoContext.Componente;
import it.govpay.core.utils.appio.client.AppIoAPIClient;
import it.govpay.core.utils.appio.impl.ApiException;
import it.govpay.core.utils.appio.impl.Pair;
import it.govpay.core.utils.appio.impl.auth.ApiKeyAuth;
import it.govpay.core.utils.appio.model.LimitedProfile;
import it.govpay.core.utils.appio.model.MessageCreated;
import it.govpay.core.utils.appio.model.NewMessage;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumAuthType;

public class AppIoClient extends BasicClient {

	private static Logger log = LoggerWrapperFactory.getLogger(AppIoClient.class);
	private AppIoAPIClient apiClient = null;

	public AppIoClient(String operazioneSwaggerAppIO, AppIO appIo, String operationID, Giornale giornale) throws ClientException { 
		super(operazioneSwaggerAppIO, TipoDestinatario.APP_IO, getConnettore(appIo)); 

		this.apiClient = new AppIoAPIClient();
		this.apiClient.setBasePath(this.url.toExternalForm());

		this.operationID = operationID;
		this.componente = Componente.API_BACKEND_IO;
		this.setGiornale(giornale);
		this.getEventoCtx().setComponente(this.componente);
	}

	public LimitedProfile getProfile(String fiscalCode, String appIOAPIKey, String swaggerOperationId) throws ApiException {
		ApiKeyAuth SubscriptionKey = (ApiKeyAuth) this.apiClient.getAuthentication("SubscriptionKey");
		SubscriptionKey.setApiKey(appIOAPIKey);

		// Salvataggio Tipo Evento
		this.getEventoCtx().setTipoEvento(swaggerOperationId);
		HttpMethodEnum httpMethodEnum = fromHttpMethod(HttpRequestMethod.GET);

		int responseCode = 0;
		DumpRequest dumpRequest = new DumpRequest();
		DumpResponse dumpResponse = new DumpResponse();
		ServerInfoResponse serverInfoResponse = null;
		Map<String, List<String>> headerFields = null;
		byte[] msg = null;

		try {
			Object localVarPostBody = null;

			IContext ctx = ContextThreadLocal.get();

			ServerInfoRequest serverInfoRequest = new ServerInfoRequest();
			serverInfoResponse = new ServerInfoResponse();

			// create path and map variables
			String localVarPath = fiscalCode != null ? "/profiles/{fiscal_code}".replaceAll("\\{" + "fiscal_code" + "\\}", apiClient.escapeString(fiscalCode.toString())) : "/profiles/{fiscal_code}";

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
			List<Pair> localVarQueryParams = new ArrayList<Pair>();
			Map<String, String> localVarHeaderParams = new HashMap<String, String>();
			Map<String, Object> localVarFormParams = new HashMap<String, Object>();

			final String[] localVarAccepts = { "application/json" };
			final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

			final String[] localVarContentTypes = { };
			final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

			// Salvataggio content type ed header con l'apikey
			dumpRequest.getHeaders().put(SubscriptionKey.getParamName(), SubscriptionKey.getApiKey());
			dumpRequest.setPayload("".getBytes());

			dumpRequest.getHeaders().put("HTTP-Method", httpMethodEnum.name());
			dumpRequest.getHeaders().put("RequestPath", urlString);

			this.serverInfoContext.processBeforeSend(serverInfoRequest, dumpRequest);

			dumpResponse.getHeaders().put("HTTP-Method", httpMethodEnum.name());
			dumpResponse.getHeaders().put("RequestPath", urlString);

			String[] localVarAuthNames = new String[] { "SubscriptionKey" };

			GenericType<LimitedProfile> localVarReturnType = new GenericType<LimitedProfile>() {};
			LimitedProfile limitedProfile = apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);

			Map<String, List<String>> responseHeaders = apiClient.getResponseHeaders();
			responseCode = apiClient.getStatusCode();

			for(String key : responseHeaders.keySet()) {
				if(responseHeaders.get(key) != null) {
					//					if(key == null)
					//						dumpResponse.getHeaders().put("Status-line", apiClient.getResponseHeaders().get(key).get(0));
					//					else 
					if(responseHeaders.get(key).size() == 1)
						dumpResponse.getHeaders().put(key, responseHeaders.get(key).get(0));
					else
						dumpResponse.getHeaders().put(key, ArrayUtils.toString(responseHeaders.get(key)));
				}
			}
			dumpResponse.getHeaders().put("Status-line", ""+responseCode);

			try {
				String msgRes = ConverterUtils.toJSON(limitedProfile, null);
				msg = msgRes != null ? msgRes.getBytes() : new byte[]{};
			} catch (ServiceException e) {
				log.warn("Errore durante la serializzazione del messaggio di risposta per il giornale eventi: " + e.getMessage(), e);
			}

			return limitedProfile;
		} catch (ApiException e) {
			responseCode = e.getCode();
			Map<String, List<String>> responseHeaders = e.getResponseHeaders();
			for(String key : responseHeaders.keySet()) {
				if(responseHeaders.get(key) != null) {
					//					if(key == null)
					//						dumpResponse.getHeaders().put("Status-line", apiClient.getResponseHeaders().get(key).get(0));
					//					else 
					if(responseHeaders.get(key).size() == 1)
						dumpResponse.getHeaders().put(key, responseHeaders.get(key).get(0));
					else
						dumpResponse.getHeaders().put(key, ArrayUtils.toString(responseHeaders.get(key)));
				}
			}
			dumpResponse.getHeaders().put("Status-line", ""+responseCode);
			msg = e.getResponseBody() != null ? e.getResponseBody().getBytes() : new byte[]{};
			throw e;
		} finally {
			serverInfoResponse.setResponseCode(responseCode);
			this.serverInfoContext.processAfterSend(serverInfoResponse, dumpResponse);
			if(msg != null && msg.length > 0) dumpResponse.setPayload(msg);
			if(log.isTraceEnabled() && headerFields != null) {
				StringBuffer sb = new StringBuffer();
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
		ApiKeyAuth SubscriptionKey = (ApiKeyAuth) apiClient.getAuthentication("SubscriptionKey");
		SubscriptionKey.setApiKey(appIOAPIKey);

		// Salvataggio Tipo Evento
		this.getEventoCtx().setTipoEvento(swaggerOperationId);
		HttpMethodEnum httpMethodEnum = fromHttpMethod(HttpRequestMethod.POST);

		int responseCode = 0;
		DumpRequest dumpRequest = new DumpRequest();
		DumpResponse dumpResponse = new DumpResponse();
		ServerInfoResponse serverInfoResponse = null;
		Map<String, List<String>> headerFields = null;
		byte[] msg = null;
		try {
			// create path and map variables
			String localVarPath = "/messages";

			IContext ctx = ContextThreadLocal.get();

			ServerInfoRequest serverInfoRequest = new ServerInfoRequest();
			serverInfoResponse = new ServerInfoResponse();

			// Url Completa che viene invocata
			String urlString = this.url.toExternalForm();
			if(urlString.endsWith("/")) urlString = urlString.substring(0, urlString.length() - 1);
			urlString = urlString.concat(localVarPath);

			this.getEventoCtx().setUrl(urlString);

			this.serverInfoContext = new ServerInfoContextManuallyAdd(this.getServerConfig(ctx));
			serverInfoRequest.setAddress(urlString);
			serverInfoRequest.setHttpRequestMethod(HttpRequestMethod.POST);

			// query params
			List<Pair> localVarQueryParams = new ArrayList<Pair>();
			Map<String, String> localVarHeaderParams = new HashMap<String, String>();
			Map<String, Object> localVarFormParams = new HashMap<String, Object>();

			final String[] localVarAccepts = { "application/json" };
			final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

			final String[] localVarContentTypes = { "application/json" };
			final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

			String jsonBody = null;
			try {
				jsonBody = ConverterUtils.toJSON(messageWithCF, null);
			} catch (ServiceException e) {
				log.warn("Errore durante la serializzazione del messaggio di richiesta per il giornale eventi: " + e.getMessage(), e);
			} 

			// Salvataggio content type ed header con l'apikey
			dumpRequest.setContentType(localVarContentTypes[0]);
			dumpRequest.getHeaders().put(SubscriptionKey.getParamName(), SubscriptionKey.getApiKey());


			dumpRequest.setPayload(jsonBody != null ? jsonBody.getBytes() : "".getBytes());

			dumpRequest.getHeaders().put("HTTP-Method", httpMethodEnum.name());
			dumpRequest.getHeaders().put("RequestPath", urlString);

			this.serverInfoContext.processBeforeSend(serverInfoRequest, dumpRequest);

			dumpResponse.getHeaders().put("HTTP-Method", httpMethodEnum.name());
			dumpResponse.getHeaders().put("RequestPath", urlString);

			String[] localVarAuthNames = new String[] { "SubscriptionKey" };

			GenericType<MessageCreated> localVarReturnType = new GenericType<MessageCreated>() {};
			MessageCreated createdMessage = apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, messageWithCF, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);

			Map<String, List<String>> responseHeaders = apiClient.getResponseHeaders();
			responseCode = apiClient.getStatusCode();

			for(String key : responseHeaders.keySet()) {
				if(responseHeaders.get(key) != null) {
					//					if(key == null)
					//						dumpResponse.getHeaders().put("Status-line", apiClient.getResponseHeaders().get(key).get(0));
					//					else 
					if(responseHeaders.get(key).size() == 1)
						dumpResponse.getHeaders().put(key, responseHeaders.get(key).get(0));
					else
						dumpResponse.getHeaders().put(key, ArrayUtils.toString(responseHeaders.get(key)));
				}
			}
			dumpResponse.getHeaders().put("Status-line", ""+responseCode);

			try {
				String msgRes = ConverterUtils.toJSON(createdMessage, null);
				msg = msgRes != null ? msgRes.getBytes() : new byte[]{};
			} catch (ServiceException e) {
				log.warn("Errore durante la serializzazione del messaggio di risposta per il giornale eventi: " + e.getMessage(), e);
			}

			return createdMessage;
		} catch (ApiException e) {
			responseCode = e.getCode();
			Map<String, List<String>> responseHeaders = e.getResponseHeaders();
			for(String key : responseHeaders.keySet()) {
				if(responseHeaders.get(key) != null) {
					//					if(key == null)
					//						dumpResponse.getHeaders().put("Status-line", apiClient.getResponseHeaders().get(key).get(0));
					//					else 
					if(responseHeaders.get(key).size() == 1)
						dumpResponse.getHeaders().put(key, responseHeaders.get(key).get(0));
					else
						dumpResponse.getHeaders().put(key, ArrayUtils.toString(responseHeaders.get(key)));
				}
			}
			dumpResponse.getHeaders().put("Status-line", ""+responseCode);
			msg = e.getResponseBody() != null ? e.getResponseBody().getBytes() : new byte[]{};
			throw e;
		} finally {
			serverInfoResponse.setResponseCode(responseCode);
			this.serverInfoContext.processAfterSend(serverInfoResponse, dumpResponse);
			if(msg != null && msg.length > 0) dumpResponse.setPayload(msg);
			if(log.isTraceEnabled() && headerFields != null) {
				StringBuffer sb = new StringBuffer();
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
		return this.apiClient.getResponseHeaders().containsKey("Location") ? this.apiClient.getResponseHeaders().get("Location").get(0) : "";
	}


	@Override
	public String getOperationId() {
		return this.operationID;
	}

	private static Connettore getConnettore(AppIO appIo) {
		Connettore connettore = new Connettore();

		connettore.setUrl(appIo.getUrl());
		connettore.setTipoAutenticazione(EnumAuthType.NONE);
		connettore.setAzioneInUrl(false);

		return connettore;
	}
}
