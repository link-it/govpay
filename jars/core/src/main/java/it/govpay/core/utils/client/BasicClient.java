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
import java.util.Date;
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
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.context.core.Role;
import org.openspcoop2.utils.service.beans.HttpMethodEnum;
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

import it.govpay.bd.configurazione.model.GdeInterfaccia;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.eventi.DettaglioRichiesta;
import it.govpay.bd.model.eventi.DettaglioRisposta;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.utils.EventoContext;
import it.govpay.core.utils.EventoContext.Categoria;
import it.govpay.core.utils.EventoContext.Componente;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.client.beans.TipoConnettore;
import it.govpay.core.utils.client.beans.TipoDestinatario;
import it.govpay.core.utils.client.beans.TipoOperazioneNodo;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.client.handler.IntegrationContext;
import it.govpay.core.utils.client.handler.IntegrationOutHandler;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.Connettore.EnumSslType;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.model.Intermediario;

public abstract class BasicClient {

	private static final String SOAP_ACTION = "SOAPAction";
	private static Logger log = LoggerWrapperFactory.getLogger(BasicClient.class);

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
	protected Componente componente;
	private Giornale giornale;
	protected EventoContext eventoCtx;

	protected IntegrationContext integrationCtx;

	protected BasicClient(Intermediario intermediario, TipoOperazioneNodo tipoOperazione) throws ClientException {
		this("I_" + intermediario.getCodIntermediario() + "_" + tipoOperazione, tipoOperazione.equals(TipoOperazioneNodo.NODO) ? intermediario.getConnettorePdd() : intermediario.getConnettorePddAvvisatura(),
				tipoOperazione.toString() + " dell'intermediario (" + intermediario.getCodIntermediario() + ")");
//		errMsg = tipoOperazione.toString() + " dell'intermediario (" + intermediario.getCodIntermediario() + ")";
		mittente = intermediario.getDenominazione();
		destinatario = "NodoDeiPagamentiDellaPA";
		integrationCtx = new IntegrationContext();
		integrationCtx.setApplicazione(null);
		integrationCtx.setIntermediario(intermediario);
		integrationCtx.setTipoConnettore(null);
		integrationCtx.setTipoDestinatario(TipoDestinatario.INTERMEDIARIO);
	}

	protected BasicClient(Applicazione applicazione, TipoConnettore tipoConnettore) throws ClientException {
		this("A_" + tipoConnettore + "_" + applicazione.getCodApplicazione(), applicazione.getConnettoreIntegrazione(),
				tipoConnettore.toString() + " dell'applicazione (" + applicazione.getCodApplicazione() + ")");
//		errMsg = tipoConnettore.toString() + " dell'applicazione (" + applicazione.getCodApplicazione() + ")";
		mittente = "GovPay";
		destinatario = applicazione.getCodApplicazione();
		integrationCtx = new IntegrationContext();
		integrationCtx.setApplicazione(applicazione);
		integrationCtx.setIntermediario(null);
		integrationCtx.setTipoConnettore(tipoConnettore);
		integrationCtx.setTipoDestinatario(TipoDestinatario.APPLICAZIONE);
	}
	
	protected BasicClient(String operazioneSwagger, TipoDestinatario tipoDestinatario, Connettore connettore) throws ClientException {
		this(tipoDestinatario +"_" + operazioneSwagger, connettore, operazioneSwagger + " per invocazione APP_IO");
//		errMsg = operazioneSwagger + " per invocazione APP_IO";
		mittente = "GovPay";
		destinatario = "APP_IO";
		integrationCtx = new IntegrationContext();
		integrationCtx.setApplicazione(null);
		integrationCtx.setIntermediario(null);
		integrationCtx.setTipoConnettore(null);
		integrationCtx.setTipoDestinatario(tipoDestinatario);
	}
	
	protected BasicClient(Dominio dominio, TipoConnettore tipoConnettore, ConnettoreNotificaPagamenti connettore) throws ClientException {
		this("D_" + tipoConnettore + "_" + dominio.getCodDominio(), connettore, tipoConnettore.toString() + " del dominio (" + dominio.getCodDominio() + ")");
//		errMsg = tipoConnettore.toString() + " del dominio (" + dominio.getCodDominio() + ")";
		mittente = "GovPay";
		
		integrationCtx = new IntegrationContext();
		integrationCtx.setApplicazione(null);
		integrationCtx.setIntermediario(null);
		integrationCtx.setTipoConnettore(tipoConnettore);
		
		switch (tipoConnettore) {
		case GOVPAY:
			integrationCtx.setTipoDestinatario(TipoDestinatario.GOVPAY);
			destinatario = dominio.getCodDominio();
			break;
		case MYPIVOT:
			integrationCtx.setTipoDestinatario(TipoDestinatario.MYPIVOT);
			destinatario = "ServizioMyPivot";
			break;
		default:
			break;
		}
	}

	private BasicClient(String bundleKey, Connettore connettore, String errMsg) throws ClientException {
		this.errMsg = errMsg;
		// inizializzazione base del context evento
		this.eventoCtx = new EventoContext();
		this.getEventoCtx().setCategoriaEvento(Categoria.INTERFACCIA);
		this.getEventoCtx().setRole(Role.CLIENT);
		this.getEventoCtx().setDataRichiesta(new Date());
				
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
			this.getEventoCtx().setPrincipal("SSL Auth");
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
			
			this.getEventoCtx().setPrincipal(this.httpBasicUser);
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


	public byte[] sendSoap(String azione, byte[] body, boolean isAzioneInUrl) throws ClientException { 
		return this.send(true, azione, body, isAzioneInUrl);
	}

	private byte[] send(boolean soap, String azione, byte[] body, boolean isAzioneInUrl) throws ClientException {
		// Salvataggio Tipo Evento
		HttpMethodEnum httpMethod = HttpMethodEnum.POST;
		this.getEventoCtx().setTipoEvento(azione);
		int responseCode = 0;
		DumpRequest dumpRequest = new DumpRequest();
		DumpResponse dumpResponse = new DumpResponse();
		try {

			ServerInfoRequest serverInfoRequest = new ServerInfoRequest();
			ServerInfoResponse serverInfoResponse = new ServerInfoResponse();
			
			// Creazione Connessione
			HttpURLConnection connection = null;
			byte[] msg = null;
			IContext ctx = ContextThreadLocal.get();
			String urlString = this.url.toExternalForm();
			if(isAzioneInUrl) {
				if(!urlString.endsWith("/")) urlString = urlString.concat("/");
				try {
					this.url = new URL(urlString.concat(azione));
				} catch (MalformedURLException e) {
					responseCode = 500;
					throw new ClientException("Url di connessione malformata: " + urlString.concat(azione), e);
				}
			} 
			
			this.getEventoCtx().setUrl(this.url.toExternalForm());

			ServerConfig serverConfig = this.getServerConfig(ctx);
			this.serverInfoContext = new ServerInfoContextManuallyAdd(serverConfig);
			serverInfoRequest.setAddress(this.url.toString());
			serverInfoRequest.setHttpRequestMethod(HttpRequestMethod.POST);

			try {
				connection = (HttpURLConnection) this.url.openConnection();
				connection.setDoOutput(true);
				if(soap) {
					connection.setRequestProperty(SOAP_ACTION, "\"" + azione + "\"");
					dumpRequest.getHeaders().put(SOAP_ACTION, "\"" + azione + "\"");
				}
				dumpRequest.setContentType("text/xml");
				connection.setRequestProperty("Content-Type", "text/xml");
				connection.setRequestMethod("POST");
				connection.setConnectTimeout(10000);
				connection.setReadTimeout(180000);

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
				responseCode = 500;
				throw new ClientException(e);
			}
			try {
				responseCode = connection.getResponseCode();
			} catch (Exception e) {
				responseCode = 500;
				throw new ClientException(e);
				
			}

			

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

		} catch (ClientException e) {
			throw e;

		} finally { // funzionalita' di log
			popolaContextEvento(httpMethod, responseCode, dumpRequest, dumpResponse);
		}
	}

	protected void popolaContextEvento(HttpMethodEnum httpMethod, int responseCode, DumpRequest dumpRequest, DumpResponse dumpResponse) {
		if(GovpayConfig.getInstance().isGiornaleEventiEnabled()) {
			boolean logEvento = false;
			boolean dumpEvento = false;
			GdeInterfaccia configurazioneInterfaccia = GiornaleEventi.getConfigurazioneComponente(this.getEventoCtx(), this.componente, this.getGiornale());
			
			log.debug("Log Evento Client: ["+this.componente +"] Method ["+httpMethod+"], Url ["+this.url.toExternalForm()+"], StatusCode ["+responseCode+"]");

			if(configurazioneInterfaccia != null) {
				try {
					log.debug("Configurazione Giornale Eventi API: ["+this.componente+"]: " + ConverterUtils.toJSON(configurazioneInterfaccia,null));
				} catch (ServiceException e) {
					log.error("Errore durante il log della configurazione giornale eventi: " +e.getMessage(), e);
				}
				
				if(GiornaleEventi.isRequestLettura(httpMethod, this.componente, this.getEventoCtx().getTipoEvento())) {
					logEvento = GiornaleEventi.logEvento(configurazioneInterfaccia.getLetture(), responseCode);
					dumpEvento = GiornaleEventi.dumpEvento(configurazioneInterfaccia.getLetture(), responseCode);
					log.debug("Tipo Operazione 'Lettura', Log ["+logEvento+"], Dump ["+dumpEvento+"].");
				} else if(GiornaleEventi.isRequestScrittura(httpMethod, this.componente, this.getEventoCtx().getTipoEvento())) {
					logEvento = GiornaleEventi.logEvento(configurazioneInterfaccia.getScritture(), responseCode);
					dumpEvento = GiornaleEventi.dumpEvento(configurazioneInterfaccia.getScritture(), responseCode);
					log.debug("Tipo Operazione 'Scrittura', Log ["+logEvento+"], Dump ["+dumpEvento+"].");
				} else {
					log.debug("Tipo Operazione non riconosciuta, l'evento non verra' salvato.");
				}
				
				this.getEventoCtx().setRegistraEvento(logEvento);

				if(logEvento) {
					Date dataIngresso = this.getEventoCtx().getDataRichiesta();
					Date dataUscita = new Date();
					// lettura informazioni dalla richiesta
					DettaglioRichiesta dettaglioRichiesta = new DettaglioRichiesta();

					dettaglioRichiesta.setPrincipal(this.getEventoCtx().getPrincipal());
					dettaglioRichiesta.setUtente(this.getEventoCtx().getUtente());
					dettaglioRichiesta.setUrl(this.getEventoCtx().getUrl());
					dettaglioRichiesta.setMethod(httpMethod.toString());
					dettaglioRichiesta.setDataOraRichiesta(dataIngresso);
					dettaglioRichiesta.setHeadersFromMap(dumpRequest.getHeaders());


					// lettura informazioni dalla response
					DettaglioRisposta dettaglioRisposta = new DettaglioRisposta();
					dettaglioRisposta.setHeadersFromMap(dumpResponse.getHeaders());
					dettaglioRisposta.setStatus(responseCode);
					dettaglioRisposta.setDataOraRisposta(dataUscita);

					this.getEventoCtx().setDataRisposta(dataUscita);
					this.getEventoCtx().setStatus(responseCode);
					this.getEventoCtx().setSottotipoEsito(responseCode + "");

					if(dumpEvento) {
						Base64 base = new Base64();
						// dump richiesta
						if(dumpRequest.getPayload() != null && dumpRequest.getPayload().length > 0)
							dettaglioRichiesta.setPayload(base.encodeToString(dumpRequest.getPayload()));

						// dump risposta
						if(dumpResponse.getPayload() != null && dumpResponse.getPayload().length > 0)
							dettaglioRisposta.setPayload(base.encodeToString(dumpResponse.getPayload()));
					} 

					this.getEventoCtx().setDettaglioRichiesta(dettaglioRichiesta);
					this.getEventoCtx().setDettaglioRisposta(dettaglioRisposta);
				}
			} else {
				log.warn("La configurazione per l'API ["+this.componente+"] non e' corretta, salvataggio evento non eseguito."); 
			}
		}
	}

	public abstract String getOperationId();

	protected ServerConfig getServerConfig(IContext ctx) {
		ServerConfig serverConfig = new ServerConfig();
		serverConfig.setDump(GovpayConfig.getInstance().isScritturaDumpFileEnabled());
		serverConfig.setOperationId(this.getOperationId());
		serverConfig.setServerId(this.serverID);
		return serverConfig;
	}

	public byte[] getJson(String path, List<Property> headerProperties, String swaggerOperationId) throws ClientException {
		return this.handleJsonRequest(path, null, headerProperties, HttpRequestMethod.GET, null,swaggerOperationId);
	}

	public byte[] sendJson(String path, byte[] jsonBody, List<Property> headerProperties, HttpRequestMethod httpMethod, String swaggerOperationId) throws ClientException {
		return this.handleJsonRequest(path, jsonBody, headerProperties, httpMethod, "application/json",swaggerOperationId);
	}
	
	public byte[] sendJson(String path, byte[] jsonBody, List<Property> headerProperties, HttpRequestMethod httpMethod, String contentType, String swaggerOperationId) throws ClientException {
		return this.handleJsonRequest(path, jsonBody, headerProperties, httpMethod, contentType,swaggerOperationId);
	}

	private byte[] handleJsonRequest(String path, byte[] jsonBody, List<Property> headerProperties, 
			HttpRequestMethod httpMethod, String contentType, String swaggerOperationId) throws ClientException {

		// Salvataggio Tipo Evento
		this.getEventoCtx().setTipoEvento(swaggerOperationId);
		HttpMethodEnum httpMethodEnum = fromHttpMethod(httpMethod);
		
		int responseCode = 0;
		DumpRequest dumpRequest = new DumpRequest();
		DumpResponse dumpResponse = new DumpResponse();
		ServerInfoResponse serverInfoResponse = null;
		Map<String, List<String>> headerFields = null;
		byte[] msg = null;
		try {

			ServerInfoRequest serverInfoRequest = new ServerInfoRequest();
			serverInfoResponse = new ServerInfoResponse();

			// Creazione Connessione
			
			HttpURLConnection connection = null;
			
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
			
			this.getEventoCtx().setUrl(this.url.toExternalForm());

			this.serverInfoContext = new ServerInfoContextManuallyAdd(this.getServerConfig(ctx));
			serverInfoRequest.setAddress(this.url.toString());
			serverInfoRequest.setHttpRequestMethod(httpMethod);

			try {
				connection = (HttpURLConnection) this.url.openConnection();
				if(httpMethod.equals(HttpRequestMethod.POST) || (jsonBody != null && jsonBody.length > 0))
					connection.setDoOutput(true);

				if(contentType != null) {
					dumpRequest.setContentType(contentType);
					connection.setRequestProperty("Content-Type", contentType);
				}
				connection.setRequestMethod(httpMethod.name());
				connection.setConnectTimeout(10000);
				connection.setReadTimeout(180000);
				
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


				integrationCtx.setMsg(jsonBody);
				this.invokeOutHandlers();

				if(log.isTraceEnabled()) {
					StringBuffer sb = new StringBuffer();
					for(String key : connection.getRequestProperties().keySet()) {
						sb.append("\n\t" + key + ": " + connection.getRequestProperties().get(key));
					}
					sb.append("\n" + ( integrationCtx.getMsg() != null ? new String(integrationCtx.getMsg()) : "Messaggio di richiesta non previsto dall'operazione"));
					log.trace(sb.toString());
				}

				dumpRequest.setPayload(integrationCtx.getMsg());

				dumpRequest.getHeaders().put("HTTP-Method", httpMethod.name());
				dumpRequest.getHeaders().put("RequestPath", this.url.toString());

				this.serverInfoContext.processBeforeSend(serverInfoRequest, dumpRequest);

				if(connection.getDoOutput())
					connection.getOutputStream().write(integrationCtx.getMsg());

			} catch (Exception e) {
				throw new ClientException(e);
			}
			
			dumpResponse.getHeaders().put("HTTP-Method", httpMethod.name());
			dumpResponse.getHeaders().put("RequestPath", this.url.toString());
			
			try {
				responseCode = connection.getResponseCode();
			} catch (Exception e) {
				responseCode = 999;
				try { 
					msg = (e.getClass().getName() + ": " + e.getMessage()).getBytes(); 
				} catch (Throwable t) {
					msg = "Errore sconosciuto".getBytes();
				}
				log.warn("Errore nell'invocazione verso "+destinatario+" Errore: " + new String(msg));
				throw new ClientException(e, responseCode);
			} finally {
				headerFields = connection.getHeaderFields();
			}

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

			if(responseCode < 300) {
				try {
					if(connection.getInputStream() != null) {
						msg = connection.getInputStream() != null ? IOUtils.toByteArray(connection.getInputStream()) : new byte[]{};
					}
				} catch (Exception e) {
					throw new ClientException("Messaggio di risposta non valido", e,responseCode,msg);
				}
			} else {
				try {
					msg = connection.getErrorStream() != null ? IOUtils.toByteArray(connection.getErrorStream()) : new byte[]{};
				} catch (IOException e) {
					msg = ("Impossibile serializzare l'ErrorStream della risposta: " + e).getBytes() ;
				} finally {
					log.warn("Errore nell'invocazione verso "+destinatario+" [HTTP Response Code " + responseCode + "]\nRisposta: " + new String(msg));
				}
				throw new ClientException("Ricevuto [HTTP " + responseCode + "]",responseCode, msg);
			}
		} finally { // funzionalita' di log
			serverInfoResponse.setResponseCode(responseCode);
			this.serverInfoContext.processAfterSend(serverInfoResponse, dumpResponse);
			if(msg != null && msg.length > 0) dumpResponse.setPayload(msg);
			if(log.isTraceEnabled() && headerFields != null) {
				StringBuffer sb = new StringBuffer();
				for(String key : headerFields.keySet()) { 
					sb.append("\n\t" + (key == null ? "Status-line" : key) + ": " + headerFields.get(key));
				}
				sb.append("\nResponse Body Size: " + (msg != null ? msg.length : 0));
				log.trace(sb.toString());
			}
			popolaContextEvento(httpMethodEnum, responseCode, dumpRequest, dumpResponse);
		}
		
		return msg;
	}
	
	protected HttpMethodEnum fromHttpMethod(HttpRequestMethod httpMethod) {
		if(httpMethod != null) {
			switch (httpMethod) {
			case DELETE:
				return HttpMethodEnum.DELETE;
			case GET:
				return HttpMethodEnum.GET;
			case HEAD:
				return HttpMethodEnum.HEAD;
			case LINK:
				return HttpMethodEnum.LINK;
			case OPTIONS:
				return HttpMethodEnum.OPTIONS;
			case PATCH:
				return HttpMethodEnum.PATCH;
			case POST:
				return HttpMethodEnum.POST;
			case PUT:
				return HttpMethodEnum.PUT;
			case TRACE:
				return HttpMethodEnum.TRACE;
			case UNLINK:
				return HttpMethodEnum.UNLINK;
			}
		}
		
		return null;
	}

	public static boolean cleanCache(String bundleKey) {
		return sslContexts.remove(bundleKey) != null;
	}

	public static boolean cleanCache() {
		sslContexts = new HashMap<>();
		return true;
	}

	public EventoContext getEventoCtx() {
		return eventoCtx;
	}

	public Giornale getGiornale() {
		return giornale;
	}

	public void setGiornale(Giornale giornale) {
		this.giornale = giornale;
	}

}
