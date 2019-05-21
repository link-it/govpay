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
package it.govpay.core.utils.client;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.dump.DumpRequest;
import org.openspcoop2.utils.service.context.dump.DumpResponse;
import org.openspcoop2.utils.service.context.server.ServerConfig;
import org.openspcoop2.utils.service.context.server.ServerInfoContextManuallyAdd;
import org.openspcoop2.utils.service.context.server.ServerInfoRequest;
import org.openspcoop2.utils.service.context.server.ServerInfoResponse;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;
import org.slf4j.Logger;

import it.govpay.bd.model.Applicazione;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.client.handler.IntegrationContext;
import it.govpay.core.utils.client.handler.IntegrationOutHandler;
import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.Connettore.EnumSslType;
import it.govpay.model.Intermediario;

public abstract class BasicClient {

	private static Logger log = LoggerWrapperFactory.getLogger(BasicClient.class);
	
	public class ClientException extends Exception {
		private static final long serialVersionUID = 1L;
		private Integer responseCode = null;
		private byte[] responseContent = null;

		public ClientException(String message, Exception e, Integer responseCode) {
			this(message, e, responseCode, null);
		}

		public ClientException(Exception e, Integer responseCode) {
			this(e, responseCode, null);
		}

		public ClientException(String string, Integer responseCode) {
			this(string, responseCode, null);
		}
		
		public ClientException(String message, Exception e) {
			super(message, e);
		}

		public ClientException(Exception e) {
			super(e);
		}

		public ClientException(String string) {
			super(string);
		}
		
		public ClientException(Exception e, Integer responseCode, byte[] responseContent) {
			super(e);
			this.responseCode = responseCode;
			this.responseContent = responseContent;
		}
		
		public ClientException(String string, Integer responseCode, byte[] responseContent) {
			super(string);
			this.responseCode = responseCode;
			this.responseContent = responseContent;
		}
		
		public ClientException(String message, Exception e, Integer responseCode, byte[] responseContent) {
			super(message, e);
			this.responseCode = responseCode;
			this.responseContent = responseContent;
		}
		
		public Integer getResponseCode() {
			return this.responseCode;
		}
		
		public byte[] getResponseContent () {
			return this.responseContent;
		}
	}
	
	protected static Map<String, SSLContext> sslContexts = new HashMap<>();
	protected URL url = null;
	protected SSLContext sslContext;
	protected boolean ishttpBasicEnabled=false, isSslEnabled=false;
	protected String httpBasicUser, httpBasicPassword;
	protected String errMsg;
	protected String destinatario;
	protected String mittente;
	protected ServerInfoContextManuallyAdd serverInfoContext = null;
	protected String operationID;
	protected String serverID;
	
	protected IntegrationContext integrationCtx;
	
	public enum TipoOperazioneNodo {
		AVVISATURA, NODO;
	}

	public enum TipoConnettore {
		VERIFICA, NOTIFICA;
	}
	
	public enum TipoDestinatario {
		APPLICAZIONE, INTERMEDIARIO;
	}
	
	protected BasicClient(Intermediario intermediario, TipoOperazioneNodo tipoOperazione) throws ClientException {
		this("I_" + intermediario.getCodIntermediario() + "_" + tipoOperazione, tipoOperazione.equals(TipoOperazioneNodo.NODO) ? intermediario.getConnettorePdd() : intermediario.getConnettorePddAvvisatura());
		errMsg = tipoOperazione.toString() + " dell'intermediario (" + intermediario.getCodIntermediario() + ")";
		mittente = intermediario.getDenominazione();
		destinatario = "NodoDeiPagamentiDellaPA";
		integrationCtx = new IntegrationContext();
		integrationCtx.setApplicazione(null);
		integrationCtx.setIntermediario(intermediario);
		integrationCtx.setTipoConnettore(null);
		integrationCtx.setTipoDestinatario(TipoDestinatario.INTERMEDIARIO);
	}
	
	protected BasicClient(Applicazione applicazione, TipoConnettore tipoConnettore) throws ClientException {
		this("A_" + tipoConnettore + "_" + applicazione.getCodApplicazione(), applicazione.getConnettoreIntegrazione());
		errMsg = tipoConnettore.toString() + " dell'applicazione (" + applicazione.getCodApplicazione() + ")";
		mittente = "GovPay";
		destinatario = applicazione.getCodApplicazione();
		integrationCtx = new IntegrationContext();
		integrationCtx.setApplicazione(applicazione);
		integrationCtx.setIntermediario(null);
		integrationCtx.setTipoConnettore(tipoConnettore);
		integrationCtx.setTipoDestinatario(TipoDestinatario.APPLICAZIONE);
	}
	
	private BasicClient(String bundleKey, Connettore connettore) throws ClientException {
		
		this.serverID = bundleKey;
		if(connettore == null) {
			throw new ClientException("Connettore non configurato");
		}
		
		try {
			this.url =  new URL(connettore.getUrl());
		} catch (Exception e) {
			throw new ClientException("La URL del connettore " + this.errMsg + " non e' valida: " + e);
		}
		this.sslContext = sslContexts.get(bundleKey);
		
		if(connettore.getTipoAutenticazione().equals(EnumAuthType.SSL)) {
			this.isSslEnabled = true;
			if(this.sslContext == null) {
				try  {
					KeyManager[] km = null;
					TrustManager[] tm = null;
			
					// Autenticazione CLIENT
					if(connettore.getTipoSsl().equals(EnumSslType.CLIENT)){
						
						if(connettore.getSslKsType() == null || 
								connettore.getSslKsLocation() == null ||
								connettore.getSslKsPasswd() == null ||
								connettore.getSslPKeyPasswd() == null)
								throw new ClientException("Configurazione SSL Client del connettore " + this.errMsg + " incompleta.");	
						
						KeyStore keystore = KeyStore.getInstance(connettore.getSslKsType()); // JKS,PKCS12,jceks,bks,uber,gkr
						try (FileInputStream finKeyStore = new FileInputStream(connettore.getSslKsLocation());){
							keystore.load(finKeyStore, connettore.getSslKsPasswd().toCharArray());
						}
						KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
						keyManagerFactory.init(keystore, connettore.getSslPKeyPasswd().toCharArray());
						km = keyManagerFactory.getKeyManagers();
					}
					
					if(connettore.getSslTsType() == null || 
							connettore.getSslTsLocation() == null ||
							connettore.getSslTsPasswd() == null || 
							connettore.getSslType() == null)
							throw new ClientException("Configurazione SSL Server del connettore " + this.errMsg + " incompleta.");	
			
					// Autenticazione SERVER
					KeyStore truststore = KeyStore.getInstance(connettore.getSslTsType()); // JKS,PKCS12,jceks,bks,uber,gkr
					try (FileInputStream finTrustStore = new FileInputStream(connettore.getSslTsLocation());){
						truststore.load(finTrustStore, connettore.getSslTsPasswd().toCharArray());
					}
					TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
					trustManagerFactory.init(truststore);
					tm = trustManagerFactory.getTrustManagers();
		
					// Creo contesto SSL
					this.sslContext = SSLContext.getInstance(connettore.getSslType());
					this.sslContext.init(km, tm, null);
					sslContexts.put(bundleKey, this.sslContext);
				} catch (Exception e) {
					throw new ClientException(e);
				} 
			}
		}
		
		if(connettore.getTipoAutenticazione().equals(EnumAuthType.HTTPBasic)) {
			this.ishttpBasicEnabled = true;
			this.httpBasicUser = connettore.getHttpUser();
			this.httpBasicPassword = connettore.getHttpPassw();
		}
	}
	
	private void invokeOutHandlers() throws ClientException {
		
		try {
			List<String> outHandlers = GovpayConfig.getInstance().getOutHandlers();
			if(outHandlers!= null && !outHandlers.isEmpty()) {
				log.debug("Applicazione al messaggio degli handlers configurati...");
				for(String handler: outHandlers) {
					Class<?> c = Class.forName(handler);
					IntegrationOutHandler instance = (IntegrationOutHandler) c.newInstance();
					log.debug("Applicazione al messaggio dell'handler ["+handler+"]...");
					instance.invoke(integrationCtx);
					log.debug("Applicazione al messaggio dell'handler ["+handler+"] completata con successo");
				}
				log.debug("Applicazione al messaggio degli handlers configurati completata con successo");
			} else {
				log.debug("Nessun handler configurato");
			}
		} catch(Exception e) {
			throw new ClientException("Errore durante l'applicazione al messaggio degli handlers configurati: " + e.getMessage(), e);
		}
	}
	
	
	public byte[] sendSoap(String azione, byte[] body, boolean isAzioneInUrl) throws ClientException, UtilsException { 
		return this.send(true, azione, body, isAzioneInUrl);
	}
	
	private byte[] send(boolean soap, String azione, byte[] body, boolean isAzioneInUrl) throws ClientException, UtilsException {

		ServerInfoRequest serverInfoRequest = new ServerInfoRequest();
		ServerInfoResponse serverInfoResponse = new ServerInfoResponse();
		DumpRequest dumpRequest = new DumpRequest();
		
		// Creazione Connessione
		int responseCode;
		HttpURLConnection connection = null;
		byte[] msg = null;
		IContext ctx = ContextThreadLocal.get();
		String urlString = this.url.toExternalForm();
		if(isAzioneInUrl) {
			if(!urlString.endsWith("/")) urlString = urlString.concat("/");
			try {
				this.url = new URL(urlString.concat(azione));
			} catch (MalformedURLException e) {
				throw new ClientException("Url di connessione malformata: " + urlString.concat(azione), e);
			}
		} 
		
		this.serverInfoContext = new ServerInfoContextManuallyAdd(this.getServerConfig(ctx));
		serverInfoRequest.setAddress(this.url.toString());
		serverInfoRequest.setHttpRequestMethod(HttpRequestMethod.POST);
		
		try {
			connection = (HttpURLConnection) this.url.openConnection();
			connection.setDoOutput(true);
			if(soap) {
				connection.setRequestProperty("SOAPAction", "\"" + azione + "\"");
				dumpRequest.getHeaders().put("SOAPAction", "\"" + azione + "\"");
			}
			dumpRequest.setContentType("text/xml");
			connection.setRequestProperty("Content-Type", "text/xml");
			connection.setRequestMethod("POST");
	
			// Imposta Contesto SSL se attivo
			if(this.sslContext != null){
				HttpsURLConnection httpsConn = (HttpsURLConnection) connection;
				httpsConn.setSSLSocketFactory(this.sslContext.getSocketFactory());
				HostNameVerifierDisabled disabilitato = new HostNameVerifierDisabled();
				httpsConn.setHostnameVerifier(disabilitato);
			}
	
			// Imposta l'autenticazione HTTP Basic se attiva
			if(this.ishttpBasicEnabled) {
				Base64 base = new Base64();
				String encoding = new String(base.encode((this.httpBasicUser + ":" + this.httpBasicPassword).getBytes()));
				connection.setRequestProperty("Authorization", "Basic " + encoding);
				dumpRequest.getHeaders().put("Authorization", "Basic " + encoding);
			}
			
			integrationCtx.setMsg(body);
			this.invokeOutHandlers();
			
			if(log.isTraceEnabled()) {
				StringBuffer sb = new StringBuffer();
				for(String key : connection.getRequestProperties().keySet()) {
					sb.append("\n\t" + key + ": " + connection.getRequestProperties().get(key));
				}
				sb.append("\n" + new String(integrationCtx.getMsg()));
				log.trace(sb.toString());
			}
			
			dumpRequest.setPayload(integrationCtx.getMsg());
			
			dumpRequest.getHeaders().put("RequestPath", this.url.toString());

			this.serverInfoContext.processBeforeSend(serverInfoRequest, dumpRequest);
			
			connection.getOutputStream().write(integrationCtx.getMsg());
	
		} catch (Exception e) {
			throw new ClientException(e);
		}
		try {
			responseCode = connection.getResponseCode();
		} catch (Exception e) {
			throw new ClientException(e);
		}
		
		DumpResponse dumpResponse = new DumpResponse();
		
		for(String key : connection.getHeaderFields().keySet()) {
			if(connection.getHeaderFields().get(key) != null) {
				if(key == null)
					dumpResponse.getHeaders().put("Status-line", connection.getHeaderFields().get(key).get(0));
				else if(connection.getHeaderFields().get(key).size() == 1)
					dumpResponse.getHeaders().put(key, connection.getHeaderFields().get(key).get(0));
				else
					dumpResponse.getHeaders().put(key, ArrayUtils.toString(connection.getHeaderFields().get(key)));
			}
		}
			
		try {
			if(responseCode < 300) {
				try {
					if(connection.getInputStream() == null) {
						return null;
					}
					msg = connection.getInputStream() != null ? IOUtils.toByteArray(connection.getInputStream()) : new byte[]{};
					if(msg.length > 0)
						dumpResponse.setPayload(msg);
					return msg;
				} catch (Exception e) {
					throw new ClientException("Messaggio di risposta non valido", e,responseCode);
				}
			} else {
				try {
					msg = connection.getErrorStream() != null ? IOUtils.toByteArray(connection.getErrorStream()) : new byte[]{};
					dumpResponse.setPayload(msg);
				} catch (IOException e) {
					msg = ("Impossibile serializzare l'ErrorStream della risposta: " + e).getBytes() ;
				} finally {
					log.warn("Errore nell'invocazione del Nodo dei Pagamenti [HTTP Response Code " + responseCode + "]\nRisposta: " + new String(msg));
				}
				
				if(soap)
					try {
						MimeHeaders headers = new MimeHeaders();
						headers.addHeader("Content-Type", "text/xml");
						SOAPMessage createMessage = MessageFactory.newInstance().createMessage(headers, new ByteArrayInputStream(msg));
						throw new ClientException("Ricevuto messaggio di errore: HTTP " + responseCode + " [SOAPFaultCode: " + createMessage.getSOAPBody().getFault().getFaultCode() + " - SOAPFaultString: " + createMessage.getSOAPBody().getFault().getFaultString() +"]",responseCode);
					} catch (IOException | SOAPException | NullPointerException e) {
						
					}
				
				throw new ClientException("Ricevuto [HTTP " + responseCode + "]",responseCode);
			}
		} finally {
			serverInfoResponse.setResponseCode(responseCode);
			this.serverInfoContext.processAfterSend(serverInfoResponse, dumpResponse);

			if(log.isTraceEnabled() && connection != null && connection.getHeaderFields() != null) {
				StringBuffer sb = new StringBuffer();
				for(String key : connection.getHeaderFields().keySet()) { 
					sb.append("\n\t" + key + ": " + connection.getHeaderField(key));
				}
				sb.append("\n" + new String(msg));
				log.trace(sb.toString());
			}
		}
		
	}
	
	public abstract String getOperationId();
	
	private ServerConfig getServerConfig(IContext ctx) {
		ServerConfig serverConfig = new ServerConfig();
		serverConfig.setDump(GovpayConfig.getInstance().isContextDumpEnabled());
		serverConfig.setOperationId(this.getOperationId());
		serverConfig.setServerId(this.serverID);
		return serverConfig;
	}

	public byte[] getJson(String path, List<Property> headerProperties) throws ClientException, UtilsException {
		return this.handleJsonRequest(path, null, headerProperties, HttpRequestMethod.GET, null);
	}
	
	public byte[] sendJson(String path, String jsonBody, List<Property> headerProperties) throws ClientException, UtilsException {
		return this.handleJsonRequest(path, jsonBody, headerProperties, HttpRequestMethod.POST, "application/json");
	}
	
	public byte[] sendJson(String path, String jsonBody, List<Property> headerProperties, HttpRequestMethod httpMethod) throws ClientException, UtilsException {
		return this.handleJsonRequest(path, jsonBody, headerProperties, httpMethod, "application/json");
	}

	private byte[] handleJsonRequest(String path, String jsonBody, List<Property> headerProperties, 
			HttpRequestMethod httpMethod, String contentType) throws ClientException, UtilsException {

		ServerInfoRequest serverInfoRequest = new ServerInfoRequest();
		ServerInfoResponse serverInfoResponse = new ServerInfoResponse();
		DumpRequest dumpRequest = new DumpRequest();
		
		// Creazione Connessione
		int responseCode;
		HttpURLConnection connection = null;
		byte[] msg = null;
		IContext ctx = ContextThreadLocal.get();
		String urlString = this.url.toExternalForm();
		if(!urlString.endsWith("/")) urlString = urlString.concat("/");
		try {
			// elimino la possibilita' di avere due '/'
			path = path.startsWith("/") ? path.substring(1) : path;
			this.url = new URL(urlString.concat(path));
			log.debug("La richiesta sara' spedita alla URL: ["+this.url+"].");
		} catch (MalformedURLException e) {
			throw new ClientException("Url di connessione malformata: " + urlString.concat(path), e);
		}
		
		this.serverInfoContext = new ServerInfoContextManuallyAdd(this.getServerConfig(ctx));
		serverInfoRequest.setAddress(this.url.toString());
		serverInfoRequest.setHttpRequestMethod(httpMethod);
		
		try {
			connection = (HttpURLConnection) this.url.openConnection();
			if(httpMethod.equals(HttpRequestMethod.POST) || StringUtils.isNotEmpty(jsonBody))
				connection.setDoOutput(true);
			
			if(contentType != null) {
				dumpRequest.setContentType(contentType);
				connection.setRequestProperty("Content-Type", contentType);
			}
			connection.setRequestMethod(httpMethod.name());
			
			if(headerProperties!= null  && headerProperties.size() > 0) {
				for (Property prop : headerProperties) {
					connection.setRequestProperty(prop.getName(), prop.getValue());
					dumpRequest.getHeaders().put(prop.getName(), prop.getValue());
				}
			}
	
			// Imposta Contesto SSL se attivo
			if(this.sslContext != null){
				HttpsURLConnection httpsConn = (HttpsURLConnection) connection;
				httpsConn.setSSLSocketFactory(this.sslContext.getSocketFactory());
				HostNameVerifierDisabled disabilitato = new HostNameVerifierDisabled();
				httpsConn.setHostnameVerifier(disabilitato);
			}
	
			// Imposta l'autenticazione HTTP Basic se attiva
			if(this.ishttpBasicEnabled) {
				Base64 base = new Base64();
				String encoding = new String(base.encode((this.httpBasicUser + ":" + this.httpBasicPassword).getBytes()));
				connection.setRequestProperty("Authorization", "Basic " + encoding);
				dumpRequest.getHeaders().put("Authorization", "Basic " + encoding);
			}
			
			
			integrationCtx.setMsg(jsonBody != null ? jsonBody.getBytes() : "".getBytes());
			this.invokeOutHandlers();
			
			if(log.isTraceEnabled()) {
				StringBuffer sb = new StringBuffer();
				for(String key : connection.getRequestProperties().keySet()) {
					sb.append("\n\t" + key + ": " + connection.getRequestProperties().get(key));
				}
				sb.append("\n" + new String(integrationCtx.getMsg()));
				log.trace(sb.toString());
			}
			
			dumpRequest.setPayload(integrationCtx.getMsg());
			
			dumpRequest.getHeaders().put("HTTP-Method", httpMethod.name());
			dumpRequest.getHeaders().put("RequestPath", this.url.toString());
			
			this.serverInfoContext.processBeforeSend(serverInfoRequest, dumpRequest);

			if(StringUtils.isNotEmpty(jsonBody))
				connection.getOutputStream().write(integrationCtx.getMsg());
	
		} catch (Exception e) {
			throw new ClientException(e);
		}
		try {
			responseCode = connection.getResponseCode();
		} catch (Exception e) {
			throw new ClientException(e);
		}
		
		DumpResponse dumpResponse = new DumpResponse();
		
		dumpResponse.getHeaders().put("HTTP-Method", httpMethod.name());
		dumpResponse.getHeaders().put("RequestPath", this.url.toString());
		
		for(String key : connection.getHeaderFields().keySet()) {
			if(connection.getHeaderFields().get(key) != null) {
				if(key == null)
					dumpResponse.getHeaders().put("Status-line", connection.getHeaderFields().get(key).get(0));
				else if(connection.getHeaderFields().get(key).size() == 1)
					dumpResponse.getHeaders().put(key, connection.getHeaderFields().get(key).get(0));
				else
					dumpResponse.getHeaders().put(key, ArrayUtils.toString(connection.getHeaderFields().get(key)));
			}
		}
			
		try {
			if(responseCode < 300) {
				try {
					if(connection.getInputStream() == null) {
						return null;
					}
					msg = connection.getInputStream() != null ? IOUtils.toByteArray(connection.getInputStream()) : new byte[]{};
					if(msg.length > 0)
						dumpResponse.setPayload(msg);
					return msg;
				} catch (Exception e) {
					throw new ClientException("Messaggio di risposta non valido", e,responseCode,msg);
				}
			} else {
				try {
					msg = connection.getErrorStream() != null ? IOUtils.toByteArray(connection.getErrorStream()) : new byte[]{};
					dumpResponse.setPayload(msg);
				} catch (IOException e) {
					msg = ("Impossibile serializzare l'ErrorStream della risposta: " + e).getBytes() ;
				} finally {
					log.warn("Errore nell'invocazione verso "+destinatario+" [HTTP Response Code " + responseCode + "]\nRisposta: " + new String(msg));
				}
				
				throw new ClientException("Ricevuto [HTTP " + responseCode + "]",responseCode, msg);
			}
		} finally {
			serverInfoResponse.setResponseCode(responseCode);
			this.serverInfoContext.processAfterSend(serverInfoResponse, dumpResponse);

			if(log.isTraceEnabled() && connection != null && connection.getHeaderFields() != null) {
				StringBuffer sb = new StringBuffer();
				for(String key : connection.getHeaderFields().keySet()) { 
					sb.append("\n\t" + key + ": " + connection.getHeaderField(key));
				}
				sb.append("\n" + new String(msg));
				log.trace(sb.toString());
			}
		}
		
	}

	public static boolean cleanCache(String bundleKey) {
		return sslContexts.remove(bundleKey) != null;
	}
	
	public static boolean cleanCache() {
		sslContexts = new HashMap<>();
		return true;
	}
	
}
