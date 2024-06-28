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
package it.govpay.core.utils.client;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
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
import org.openspcoop2.utils.transport.TransportUtils;
import org.openspcoop2.utils.transport.http.HttpBodyParameters;
import org.openspcoop2.utils.transport.http.HttpConstants;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;
import org.openspcoop2.utils.transport.http.WrappedLogSSLSocketFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.EventoContext.Categoria;
import it.govpay.core.utils.ExceptionUtils;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.client.beans.TipoConnettore;
import it.govpay.core.utils.client.beans.TipoDestinatario;
import it.govpay.core.utils.client.beans.TipoOperazioneNodo;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.client.exception.ClientInitializeException;
import it.govpay.core.utils.client.handler.IntegrationContext;
import it.govpay.core.utils.client.handler.IntegrationOutHandler;
import it.govpay.core.utils.client.oauth2.Oauth2ClientCredentialsManager;
import it.govpay.core.utils.eventi.EventiUtils;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.Connettore.EnumSslType;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.model.Evento.RuoloEvento;
import it.govpay.model.Intermediario;
import it.govpay.model.configurazione.GdeInterfaccia;
import it.govpay.model.configurazione.Giornale;
import it.govpay.model.eventi.DettaglioRichiesta;
import it.govpay.model.eventi.DettaglioRisposta;

public abstract class BasicClientCORE {

	private static final String SOAP_ACTION = "SOAPAction";
	private static Logger log = LoggerWrapperFactory.getLogger(BasicClientCORE.class);

	protected boolean debug = true;
	protected URL url = null;
	protected boolean ishttpBasicEnabled=false;
	protected boolean isSslEnabled=false;
	protected boolean isSubscriptionKeyEnabled=false;
	protected boolean ishttpHeaderEnabled=false;
	protected boolean isApiKeyEnabled=false;
	protected boolean isOauth2ClientCredentialsEnabled=false;
	protected String httpBasicUser;
	protected String httpBasicPassword;
	protected String subscriptionKeyHeaderName;
	protected String subscriptionKeyHeaderValue;
	protected String httpHeaderName;
	protected String httpHeaderValue;
	protected String apiKey;
	protected String apiKeyValue;
	protected String apiId;
	protected String apiIdValue;
	protected String errMsg;
	protected String destinatario;
	protected String mittente;
	protected ServerInfoContextManuallyAdd serverInfoContext = null;
	protected String operationID;
	protected String serverID;
	private Giornale giornale;
	protected EventoContext eventoCtx;
	private String tipoEventoCustom;
	
	private Oauth2ClientCredentialsManager oauth2ClientCredentialsManager = Oauth2ClientCredentialsManager.getInstance();

	protected IntegrationContext integrationCtx;

	private static boolean USE_POOL = true;

	private HttpEntity httpEntityResponse = null;

	private InputStream isResponse = null;

	protected Integer connectionTimeout;
	protected Integer readTimeout;
	protected Integer connectionRequestTimeout;

	protected DumpRequest dumpRequest = null;
	protected DumpResponse dumpResponse = null;
	protected ServerInfoResponse serverInfoResponse = null;
	protected ServerInfoRequest serverInfoRequest = null;

	protected Connettore connettore = null;

	protected static Map<String, SSLSocketFactory> sslContextFactorys = new HashMap<>();
	protected SSLSocketFactory sslContextFactory;

	protected BasicClientCORE(Intermediario intermediario, TipoOperazioneNodo tipoOperazione, EventoContext eventoCtx) throws ClientInitializeException {
		this("I_" + intermediario.getCodIntermediario() + "_" + tipoOperazione, tipoOperazione.equals(TipoOperazioneNodo.NODO) ? intermediario.getConnettorePdd() : intermediario.getConnettorePddAvvisatura(), eventoCtx);
		errMsg = tipoOperazione.toString() + " dell'intermediario (" + intermediario.getCodIntermediario() + ")";
		mittente = intermediario.getDenominazione();
		destinatario = "NodoDeiPagamentiDellaPA";
		integrationCtx = new IntegrationContext();
		integrationCtx.setApplicazione(null);
		integrationCtx.setIntermediario(intermediario);
		integrationCtx.setTipoConnettore(null);
		integrationCtx.setTipoDestinatario(TipoDestinatario.INTERMEDIARIO);
	}

	protected BasicClientCORE(Applicazione applicazione, TipoConnettore tipoConnettore, EventoContext eventoCtx) throws ClientInitializeException {
		this("A_" + tipoConnettore + "_" + applicazione.getCodApplicazione()+ "_V_" + (applicazione.getConnettoreIntegrazione() != null ? applicazione.getConnettoreIntegrazione().getVersione() : "NON_CONFIGURATO"), applicazione.getConnettoreIntegrazione(), eventoCtx);
		errMsg = tipoConnettore.toString() + " dell'applicazione (" + applicazione.getCodApplicazione() + ")";
		mittente = "GovPay";
		destinatario = applicazione.getCodApplicazione();
		integrationCtx = new IntegrationContext();
		integrationCtx.setApplicazione(applicazione);
		integrationCtx.setIntermediario(null);
		integrationCtx.setTipoConnettore(tipoConnettore);
		integrationCtx.setTipoDestinatario(TipoDestinatario.APPLICAZIONE);
	}

	protected BasicClientCORE(String operazioneSwagger, TipoDestinatario tipoDestinatario, Connettore connettore, EventoContext eventoCtx) throws ClientInitializeException {
		this(tipoDestinatario +"_" + operazioneSwagger, connettore, eventoCtx);
		errMsg = operazioneSwagger + " per invocazione APP_IO";
		mittente = "GovPay";
		destinatario = "APP_IO";
		integrationCtx = new IntegrationContext();
		integrationCtx.setApplicazione(null);
		integrationCtx.setIntermediario(null);
		integrationCtx.setTipoConnettore(null);
		integrationCtx.setTipoDestinatario(tipoDestinatario);
	}

	protected BasicClientCORE(Dominio dominio, TipoConnettore tipoConnettore, ConnettoreNotificaPagamenti connettore, EventoContext eventoCtx) throws ClientInitializeException {
		this("D_" + tipoConnettore + "_" + dominio.getCodDominio(), connettore, eventoCtx);
		errMsg = tipoConnettore.toString() + " del dominio (" + dominio.getCodDominio() + ")";
		mittente = "GovPay";
		destinatario = "ServizioMyPivot";
		integrationCtx = new IntegrationContext();
		integrationCtx.setApplicazione(null);
		integrationCtx.setIntermediario(null);
		integrationCtx.setTipoConnettore(tipoConnettore);
		integrationCtx.setTipoDestinatario(TipoDestinatario.MYPIVOT);
	}

	private BasicClientCORE(String bundleKey, Connettore connettore, EventoContext eventoCtx) throws ClientInitializeException {
		this.readTimeout = GovpayConfig.getInstance().getReadTimeout();
		this.connectionTimeout = GovpayConfig.getInstance().getConnectionTimeout();
		this.connectionRequestTimeout = GovpayConfig.getInstance().getConnectionRequestTimeout();
		
		this.dumpRequest = new DumpRequest();
		this.dumpResponse = new DumpResponse();
		this.serverInfoRequest = new ServerInfoRequest();
		this.serverInfoResponse = new ServerInfoResponse();
		IContext ctx = ContextThreadLocal.get();
		this.serverInfoContext = new ServerInfoContextManuallyAdd(this.getServerConfig(ctx));

		// inizializzazione base del context evento
		this.eventoCtx = eventoCtx;
		this.getEventoCtx().setCategoriaEvento(Categoria.INTERFACCIA);
		this.getEventoCtx().setRole(RuoloEvento.CLIENT);
		this.getEventoCtx().setDataRichiesta(new Date());
		this.getEventoCtx().setTransactionId(ctx.getTransactionId());
		
		String clusterId = GovpayConfig.getInstance().getClusterId();
		if(clusterId != null)
			this.getEventoCtx().setClusterId(clusterId);
		else 
			this.getEventoCtx().setClusterId(GovpayConfig.getInstance().getAppName());

		this.serverID = bundleKey;
		this.connettore = connettore;
		if(this.connettore == null) {
			throw new ClientInitializeException("Connettore non configurato");
		}

		try {
			this.url =  new URL(this.connettore.getUrl());
		} catch (Exception e) {
			throw new ClientInitializeException("La URL del connettore " + this.errMsg + " non e' valida: " + e);
		}
		this.sslContextFactory = sslContextFactorys.get(bundleKey);

		if(this.connettore.getTipoAutenticazione().equals(EnumAuthType.SSL)) {
			this.getEventoCtx().setPrincipal("SSL Auth");
			this.isSslEnabled = true;
			if(this.sslContextFactory == null) {
				try  {
					KeyManager[] km = null;
					TrustManager[] tm = null;

					// Autenticazione CLIENT
					if(this.connettore.getTipoSsl().equals(EnumSslType.CLIENT)){

						if(this.connettore.getSslKsType() == null || 
								this.connettore.getSslKsLocation() == null ||
								this.connettore.getSslKsPasswd() == null ||
								this.connettore.getSslPKeyPasswd() == null)
							throw new ClientInitializeException("Configurazione SSL Client del connettore " + this.errMsg + " incompleta.");	

						KeyStore keystore = KeyStore.getInstance(this.connettore.getSslKsType()); // JKS,PKCS12,jceks,bks,uber,gkr
						try (FileInputStream finKeyStore = new FileInputStream(this.connettore.getSslKsLocation());){
							keystore.load(finKeyStore, this.connettore.getSslKsPasswd().toCharArray());
						}
						KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
						keyManagerFactory.init(keystore, this.connettore.getSslPKeyPasswd().toCharArray());
						km = keyManagerFactory.getKeyManagers();
					}

					if(this.connettore.getSslTsType() == null || 
							this.connettore.getSslTsLocation() == null ||
							this.connettore.getSslTsPasswd() == null || 
							this.connettore.getSslType() == null)
						throw new ClientInitializeException("Configurazione SSL Server del connettore " + this.errMsg + " incompleta.");	

					// Autenticazione SERVER
					KeyStore truststore = KeyStore.getInstance(this.connettore.getSslTsType()); // JKS,PKCS12,jceks,bks,uber,gkr
					try (FileInputStream finTrustStore = new FileInputStream(this.connettore.getSslTsLocation());){
						truststore.load(finTrustStore, this.connettore.getSslTsPasswd().toCharArray());
					}
					TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
					trustManagerFactory.init(truststore);
					tm = trustManagerFactory.getTrustManagers();

					// Creo contesto SSL
					SSLContext sslContext = SSLContext.getInstance(this.connettore.getSslType());
					sslContext.init(km, tm, null);
					this.sslContextFactory =  sslContext.getSocketFactory();
					sslContextFactorys.put(bundleKey, this.sslContextFactory);
				} catch (ClientInitializeException e) {
					throw e;
				} catch (Exception e) {
					throw new ClientInitializeException(e);
				} 
			}
		}

		if(connettore.getTipoAutenticazione().equals(EnumAuthType.HTTPBasic)) {
			this.ishttpBasicEnabled = true;
			this.httpBasicUser = connettore.getHttpUser();
			this.httpBasicPassword = connettore.getHttpPassw();

			this.getEventoCtx().setPrincipal(this.httpBasicUser);
		}
		
		if(connettore.getTipoAutenticazione().equals(EnumAuthType.HTTP_HEADER)) {
			this.ishttpHeaderEnabled = true;
			this.httpHeaderName = connettore.getHttpHeaderName();
			this.httpHeaderValue = connettore.getHttpHeaderValue();

			this.getEventoCtx().setPrincipal(this.httpHeaderValue);
		}
		
		if(connettore.getTipoAutenticazione().equals(EnumAuthType.API_KEY)) {
			this.isApiKeyEnabled = true;
			this.apiId = GovpayConfig.getInstance().getAutenticazioneApiKeyNomeHeaderApiIdFruizione();
			this.apiIdValue = connettore.getApiId();
			this.apiKey =  GovpayConfig.getInstance().getAutenticazioneApiKeyNomeHeaderApiKeyFruizione();
			this.apiKeyValue = connettore.getApiKey();

			this.getEventoCtx().setPrincipal(this.apiIdValue);
		}
		
		if(connettore.getSubscriptionKeyValue() != null) {
			// se non ho impostato nessuna autenticazione salvo SubscriptionKey come metodo di autenticazione per l'evento.
			if(connettore.getTipoAutenticazione().equals(EnumAuthType.NONE)) {
				this.getEventoCtx().setPrincipal("Subscription Key Auth");
			}
			this.isSubscriptionKeyEnabled = true;
			this.subscriptionKeyHeaderName = GovpayConfig.getInstance().getNomeHeaderSubscriptionKeyPagoPA();
			this.subscriptionKeyHeaderValue = connettore.getSubscriptionKeyValue();
		}
		
		// Oauth2 Client Credentials
		if(connettore.getTipoAutenticazione().equals(EnumAuthType.OAUTH2_CLIENT_CREDENTIALS)) {
			this.isOauth2ClientCredentialsEnabled = true;
		}
	}

	private static Map<String, PoolingHttpClientConnectionManager> cmMap = new HashMap<>();
	private static synchronized void initialize(String key, SSLConnectionSocketFactory sslConnectionSocketFactory){
		if(!BasicClientCORE.cmMap.containsKey(key)){

			PoolingHttpClientConnectionManager cm = null;
			if(sslConnectionSocketFactory!=null) {
				Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
						.<ConnectionSocketFactory> create().register("https", sslConnectionSocketFactory)
						.build();
				cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			}
			else {
				cm = new PoolingHttpClientConnectionManager();
			}
			// Increase max total connection to 200
			cm.setMaxTotal(GovpayConfig.getInstance().getNumeroMassimoConnessioniPerPool());
			// Increase default max connection per route to 20
			cm.setDefaultMaxPerRoute(GovpayConfig.getInstance().getNumeroMassimoConnessioniPerRouteDefault());
			// Increase max connections for localhost:80 to 50
			//HttpHost localhost = new HttpHost("locahost", 80)
			//cm.setMaxPerRoute(new HttpRoute(localhost), 50)

			BasicClientCORE.cmMap.put(key, cm);
		}
	}

	private HttpClient buildHttpClient(ConnectionKeepAliveStrategy keepAliveStrategy, SSLSocketFactory sslSocketFactory, boolean usePool, String key, Connettore connettore) {

		HttpClientBuilder httpClientBuilder = HttpClients.custom();

		// Imposta Contesto SSL se attivo

		SSLConnectionSocketFactory sslConnectionSocketFactory = null;
		if(this.isSslEnabled && 
				(!usePool || !BasicClientCORE.cmMap.containsKey(key))){
			if(this.debug) {
				String clientCertificateConfigurated = connettore.getSslKsLocation();
				sslSocketFactory = new WrappedLogSSLSocketFactory(sslSocketFactory, log, key, clientCertificateConfigurated);
			}		

			HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.getDefaultHostnameVerifier();

			if(hostnameVerifier==null) {
				hostnameVerifier = SSLConnectionSocketFactory.getDefaultHostnameVerifier();
			}
			sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslSocketFactory, hostnameVerifier);
		}

		if(usePool) {

			// Caso con pool
			if(!BasicClientCORE.cmMap.containsKey(key)){
				BasicClientCORE.initialize(key, sslConnectionSocketFactory);
			}

			PoolingHttpClientConnectionManager cm = BasicClientCORE.cmMap.get(key);
			
			log.trace("-----GET CONNECTION [START] ----");
			log.trace("PRIMA CLOSE AVAILABLE[{}] LEASED[{}] MAX[{}] PENDING[{}]", cm.getTotalStats().getAvailable(), cm.getTotalStats().getLeased(), cm.getTotalStats().getMax(), cm.getTotalStats().getPending());
//			 BLOCKED ConnettoreHTTPCORE.cm.closeExpiredConnections()
//			 BLOCKED ConnettoreHTTPCORE.cm.closeIdleConnections(30, java.util.concurrent.TimeUnit.SECONDS)
			log.trace("DOPO CLOSE AVAILABLE[{}] LEASED[{}] MAX[{}] PENDING[{}]", cm.getTotalStats().getAvailable(), cm.getTotalStats().getLeased(), cm.getTotalStats().getMax(), cm.getTotalStats().getPending());

			//System.out.println("-----GET CONNECTION [START] ----")
			//System.out.println("PRIMA CLOSE AVAILABLE["+cm.getTotalStats().getAvailable()+"] LEASED["
			//		+cm.getTotalStats().getLeased()+"] MAX["+cm.getTotalStats().getMax()+"] PENDING["+cm.getTotalStats().getPending()+"]")
			// BLOCKED ConnettoreHTTPCORE.cm.closeExpiredConnections()
			// BLOCKED ConnettoreHTTPCORE.cm.closeIdleConnections(30, java.util.concurrent.TimeUnit.SECONDS)
			//System.out.println("DOPO CLOSE AVAILABLE["+cm.getTotalStats().getAvailable()+"] LEASED["
			//		+cm.getTotalStats().getLeased()+"] MAX["+cm.getTotalStats().getMax()+"] PENDING["+cm.getTotalStats().getPending()+"]")
			httpClientBuilder.setConnectionManager(cm);
		}
		else {
			if(sslConnectionSocketFactory!=null) {
				httpClientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);		
			}
		}

		DefaultClientConnectionReuseStrategy defaultClientConnectionReuseStrategy = new DefaultClientConnectionReuseStrategy();
		httpClientBuilder.setConnectionReuseStrategy(defaultClientConnectionReuseStrategy);

		if(keepAliveStrategy!=null){
			httpClientBuilder.setKeepAliveStrategy(keepAliveStrategy);
		}

		log.debug("-----GET CONNECTION [END] ----");
		
		//System.out.println("PRESA LA CONNESSIONE AVAILABLE["+cm.getTotalStats().getAvailable()+"] LEASED["
				//		+cm.getTotalStats().getLeased()+"] MAX["+cm.getTotalStats().getMax()+"] PENDING["+cm.getTotalStats().getPending()+"]")
				//System.out.println("-----GET CONNECTION [END] ----")

		return httpClientBuilder.build();
	}


	private void invokeOutHandlers() throws ClientException {

		try {
			List<String> outHandlers = GovpayConfig.getInstance().getOutHandlers();
			if(outHandlers!= null && !outHandlers.isEmpty()) {
				log.debug("Applicazione al messaggio degli handlers configurati...");
				for(String handler: outHandlers) {
					Class<?> c = Class.forName(handler);
					IntegrationOutHandler instance = (IntegrationOutHandler) c.getConstructor().newInstance();
					log.debug("Applicazione al messaggio dell'handler [{}]...", handler);
					instance.invoke(integrationCtx);
					log.debug("Applicazione al messaggio dell'handler [{}] completata con successo", handler);
				}
				log.debug("Applicazione al messaggio degli handlers configurati completata con successo");
			} else {
				log.debug("Nessun handler configurato");
			}
		} catch(Exception e) {
			throw new ClientException("Errore durante l'applicazione al messaggio degli handlers configurati: " + e.getMessage(), e);
		}
	}

	private byte[] send(boolean soap, String azione, byte[] body, boolean isAzioneInUrl, 
			String contentType, List<Property> headerProperties, String swaggerOperationId, String path, HttpRequestMethod httpMethod)  throws ClientException {
		int responseCode = 0;
		byte [] msg = null;

		setTipoEvento(soap, azione, swaggerOperationId);

		try{

			// Creazione URL
			if(this.debug)
				log.debug("Creazione URL...");
			IContext ctx = ContextThreadLocal.get();
			String location = this.url.toExternalForm();
			if(soap) {
				if(isAzioneInUrl) {
					if(!location.endsWith("/")) location = location.concat("/");
					try {
						this.url = new URL(location.concat(azione));
					} catch (MalformedURLException e) {
						responseCode = 500;
						throw new ClientException("Url di connessione malformata: " + location.concat(azione), e, responseCode);
					}
				}
			} else {
				if(!location.endsWith("/")) location = location.concat("/");
				try {
					// elimino la possibilita' di avere due '/'
					path = path.startsWith("/") ? path.substring(1) : path;
					this.url = new URL(location.concat(path));
					log.debug("La richiesta sara' spedita alla URL: [{}].", this.url);
				} catch (MalformedURLException e) {
					responseCode = 500;
					throw new ClientException("Url di connessione malformata: " + location.concat(path), e, responseCode);
				}
			}

			this.serverInfoContext = new ServerInfoContextManuallyAdd(this.getServerConfig(ctx));
			this.serverInfoRequest.setAddress(this.url.toString());
			this.serverInfoRequest.setHttpRequestMethod(httpMethod);
			
			this.getEventoCtx().setUrl(this.url.toExternalForm());

			if(this.debug)
				log.debug("Creazione URL [{}]...", location);

			// Keep-alive
			ConnectionKeepAliveStrategy keepAliveStrategy = null; //new ConnectionKeepAliveStrategyCustom()

			// Creazione Connessione
			if(this.debug)
				log.info("Creazione connessione alla URL [{}]...", location);

			// creazione client
			HttpClient httpClient = buildHttpClient(keepAliveStrategy, this.sslContextFactory, BasicClientCORE.USE_POOL, this.serverID, this.connettore); 

			// HttpMethod
			if(httpMethod==null){
				throw new ClientException("HttpRequestMethod non definito");
			}
			HttpRequestBase httpRequest = null;
			switch (httpMethod) {
			case GET:
				httpRequest = new HttpGet(url.toString());
				break;
			case DELETE:
				httpRequest = new HttpDelete(url.toString());
				break;
			case HEAD:
				httpRequest = new HttpHead(url.toString());
				break;
			case POST:
				httpRequest = new HttpPost(url.toString());
				break;
			case PUT:
				httpRequest = new HttpPut(url.toString());
				break;
			case OPTIONS:
				httpRequest = new HttpOptions(url.toString());
				break;
			case TRACE:
				httpRequest = new HttpTrace(url.toString());
				break;
			case PATCH:
				httpRequest = new HttpPatch(url.toString());
				break;	
			default:
				httpRequest = new CustomHttpEntity(httpMethod, url.toString());
				break;
			}

			RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();

			// Impostazione timeout
			if(this.debug) {
				log.debug("Impostazione timeout...");
				log.debug("Impostazione http timeout: ConnectionTimeout[{}] ReadTimeout[{}] ConnectionRequestTimeout[{}]", this.connectionTimeout, this.readTimeout, this.connectionRequestTimeout);
			}
			requestConfigBuilder.setConnectionRequestTimeout(this.connectionRequestTimeout);
			requestConfigBuilder.setConnectTimeout(this.connectionTimeout);
			requestConfigBuilder.setSocketTimeout(this.readTimeout);

			// Gestione automatica del redirect
			//this.httpConn.setInstanceFollowRedirects(true) 

			// Impostazione Content-Type della Spedizione su HTTP
			if(contentType != null) {
				if(this.debug)
					log.debug("Impostazione content type [{}]", contentType);
				this.dumpRequest.setContentType(contentType);
				this.dumpRequest.getHeaders().put(HTTP.CONTENT_TYPE, contentType);
				httpRequest.addHeader(HTTP.CONTENT_TYPE, contentType);
			}

			// Aggiunga del SoapAction Header in caso di richiesta SOAP
			if(soap) {
				if(this.debug)
					log.debug("Impostazione soap action...");
				this.dumpRequest.getHeaders().put(SOAP_ACTION, "\"" + azione + "\"");
				httpRequest.addHeader(SOAP_ACTION, "\"" + azione + "\"");
				if(this.debug)
					log.debug("SOAP Action inviata [{}]",azione);
			}

			// Authentication BASIC
			if(this.ishttpBasicEnabled) {
				if(this.debug)
					log.debug("Impostazione autenticazione...");
				
				Base64 base = new Base64();
				String encoding = new String(base.encode((this.httpBasicUser + ":" + this.httpBasicPassword).getBytes()));
				String encodingValue = "Basic " + encoding;

				this.dumpRequest.getHeaders().put("Authorization", encodingValue);
				httpRequest.addHeader("Authorization", encodingValue);
				if(this.debug)
					log.debug("Impostato Header Authorization [{}]", encodingValue);
			}
			
			// Authentication HTTP Header
			if(this.ishttpHeaderEnabled) {
				if(this.debug)
					log.debug("Impostazione autenticazione...");
				
				this.dumpRequest.getHeaders().put(this.httpHeaderName, this.httpHeaderValue);
				httpRequest.addHeader(this.httpHeaderName, this.httpHeaderValue);
				if(this.debug)
					log.debug("Impostato Autenticazione tramite Header HTTP [{}:{}]", this.httpHeaderName, this.httpHeaderValue);
			}
			
			// Authentication API KEY
			if(this.isApiKeyEnabled) {
				if(this.debug)
					log.debug("Impostazione autenticazione...");
				
				this.dumpRequest.getHeaders().put(this.apiKey, this.apiKeyValue);
				httpRequest.addHeader(this.apiKey, this.apiKeyValue);
				
				this.dumpRequest.getHeaders().put(this.apiId, this.apiIdValue);
				httpRequest.addHeader(this.apiId, this.apiIdValue);
				if(this.debug) {
					log.debug("Impostato Autenticazione tramite API KEY -> API-KEY: [{}:{}]", this.apiKey, this.apiKeyValue);
					log.debug("Impostato Autenticazione tramite API KEY -> API-ID: [{}:{}]", this.apiId, this.apiIdValue);
				}
			}
			
			// Authentication Subscription Key
			if(this.isSubscriptionKeyEnabled) {
				if(this.debug)
					log.debug("Impostazione autenticazione...");
				
				this.dumpRequest.getHeaders().put(this.subscriptionKeyHeaderName, this.subscriptionKeyHeaderValue);
				httpRequest.addHeader(this.subscriptionKeyHeaderName, this.subscriptionKeyHeaderValue);
				if(this.debug)
					log.debug("Impostato Header Subscription Key [{}][{}]", this.subscriptionKeyHeaderName, this.subscriptionKeyHeaderValue);
			}
			
			// Authentication Oauth2 Client Credentials
			if(this.isOauth2ClientCredentialsEnabled) {
				if(this.debug) {
					log.debug("Impostazione autenticazione...");
					log.debug("Richiedo token...");
				}
				
				ClientAccessToken accessToken = this.oauth2ClientCredentialsManager.getClientCredentialsAccessToken(this.serverID, this.connettore);
				String oauth2ClientCredentialsBearer = accessToken.getTokenKey();
				this.getEventoCtx().setPrincipal(oauth2ClientCredentialsBearer);
				String headerValue = "Bearer " + oauth2ClientCredentialsBearer;
				
				this.dumpRequest.getHeaders().put("Authorization", headerValue);
				httpRequest.addHeader("Authorization", headerValue);
				
				if(this.debug)
					log.debug("Impostato Header Authorization [{}]",headerValue);
			}

			// Impostazione Proprieta del trasporto
			if(headerProperties!= null  && !headerProperties.isEmpty()) {
				if(this.debug)
					log.debug("Impostazione header di trasporto...");
				
				for (Property prop : headerProperties) {
					httpRequest.addHeader(prop.getName(), prop.getValue());
					this.dumpRequest.getHeaders().put(prop.getName(), prop.getValue());
					if(this.debug)
						log.debug("Aggiunto Header [{}]: [{}]", prop.getName(), prop.getValue());
				}
			}

			// Impostazione Metodo
			HttpBodyParameters httpBody = null;
			try {
				httpBody = new HttpBodyParameters(httpMethod, contentType);
			} catch (UtilsException e) {
				responseCode = 500;
				throw new ClientException(e,responseCode);
			}

			// Preparazione messaggio da spedire
			// Spedizione byte
			integrationCtx.setMsg(body);
			
			// salvo il messaggio originale prima dell'applicazione degli out handlers
			dumpRequest.setPayload(integrationCtx.getMsg());
			
			this.invokeOutHandlers();

			dumpRequest.setPayload(integrationCtx.getMsg());

			dumpRequest.getHeaders().put("HTTP-Method", httpMethod.name());
			dumpRequest.getHeaders().put("RequestPath", this.url.toString());

			this.serverInfoContext.processBeforeSend(serverInfoRequest, dumpRequest);

			if(httpBody.isDoOutput()){
				if(this.debug)
					log.debug("Spedizione byte...");

				HttpEntity httpEntity = new ByteArrayEntity(integrationCtx.getMsg());
				if(httpRequest instanceof HttpEntityEnclosingRequestBase){
					((HttpEntityEnclosingRequestBase)httpRequest).setEntity(httpEntity);
				}
				else{
					responseCode = 500;
					throw new ClientException("Tipo ["+httpRequest.getClass().getName()+"] non utilizzabile per una richiesta di tipo ["+httpMethod+"]", responseCode);
				}
				if(contentType != null) {
					((ByteArrayEntity) httpEntity).setContentType(contentType);
				}
			}

			// Imposto Configurazione
			httpRequest.setConfig(requestConfigBuilder.build());
			
			
			if(this.debug) {
				log.debug("Elenco Header impostati nella request:");
				
				for (Header prop : httpRequest.getAllHeaders()) {
					log.debug("Header [{}]: [{}]", prop.getName(), prop.getValue());
				}
				
				log.trace("Elenco Header impostati nella dumpRequest:");
				
				for (String key : this.dumpRequest.getHeaders().keySet()) {
					log.trace("Header [{}]: [{}]", key, this.dumpRequest.getHeaders().get(key));
				}
			}

			// Spedizione byte
			if(this.debug)
				log.debug("Spedizione byte...");
			// Eseguo la richiesta e prendo la risposta
			HttpResponse httpResponse = null;
			try {
				httpResponse = httpClient.execute(httpRequest);
			} catch (ClientProtocolException e) {
				responseCode = 500;
				throw new ClientException(e, responseCode);
			} catch (IOException e) {
				responseCode = 500;
				
				// retrocompatibilita'
				if(ExceptionUtils.getInnerException(e, java.net.UnknownHostException.class) != null) {
					responseCode = 999;
				}
				throw new ClientException(e, responseCode);
			}
			this.httpEntityResponse = httpResponse.getEntity();

			this.dumpResponse.getHeaders().put("HTTP-Method", httpMethod.name());
			this.dumpResponse.getHeaders().put("RequestPath", this.url.toString());

			if(this.debug)
				log.debug("Analisi risposta...");
			Header [] hdrRisposta = httpResponse.getAllHeaders();
			if(hdrRisposta!=null){
				for (int i = 0; i < hdrRisposta.length; i++) {

					String key = null;
					String value = hdrRisposta[i].getValue();;

					// Check per evitare la coppia che ha come chiave null e come valore HTTP OK 200
					if(hdrRisposta[i].getName()==null){
						key = HttpConstants.RETURN_CODE;
					}
					else{
						key = hdrRisposta[i].getName();
					}
					
					if(this.debug)
						log.debug("HTTP risposta [{}] [{}]...", key, value);
					

					List<String> list = null;
					if(dumpResponse.getHeaders().containsKey(key)) {
						list =  new ArrayList<>(Arrays.asList(StringUtils.split(dumpResponse.getHeaders().remove(key),",")));
					}
					if(list==null) {
						list = new ArrayList<>();

					}
					list.add(value);

					dumpResponse.getHeaders().put(key, StringUtils.join(list, ","));
				}
			}

			long contentLengthRisposta = 0l;
			String contentLengthHdr = TransportUtils.getObjectAsString(dumpResponse.getHeaders(), HttpConstants.CONTENT_LENGTH);
			if(contentLengthHdr!=null){
				contentLengthRisposta = Long.parseLong(contentLengthHdr);
			}
			else {
				if(this.httpEntityResponse.getContentLength()>0){
					contentLengthRisposta = this.httpEntityResponse.getContentLength();
					dumpResponse.getHeaders().put(HttpConstants.CONTENT_LENGTH, contentLengthRisposta+"");
				}
			}


			// Ricezione Risposta
			if(this.debug)
				log.debug("Analisi risposta input stream e risultato http...");

			responseCode = httpResponse.getStatusLine().getStatusCode();

			try {
				if(responseCode < 400) { // httpstatus 3xx sono casi ok
					try {
						if(httpBody.isDoInput()){
							isResponse = this.httpEntityResponse.getContent();
						}

						if(isResponse == null) {
							return null;
						}
						msg = isResponse != null ? IOUtils.toByteArray(isResponse) : new byte[]{};
						if(msg.length > 0)
							dumpResponse.setPayload(msg);
						return msg;
					} catch (IOException e) {
						throw new ClientException("Messaggio di risposta non valido", e,responseCode);
					}

				} else {
					try {
						isResponse = this.httpEntityResponse.getContent();
						msg = isResponse != null ? IOUtils.toByteArray(isResponse) : new byte[]{};
						dumpResponse.setPayload(msg);
					} catch (IOException e) {
						msg = ("Impossibile serializzare l'ErrorStream della risposta: " + e).getBytes() ;
					} finally {
						String errWarnMsg = new String(msg); 
						log.warn("Errore nell'esecuzione dell'operazione [{}, HTTP Response Code {}]\nRisposta: {}", this.errMsg, responseCode, errWarnMsg);
					}

					if(soap)
						try {
							MimeHeaders headers = new MimeHeaders();
							headers.addHeader("Content-Type", MediaType.TEXT_XML_VALUE);
							SOAPMessage createMessage = MessageFactory.newInstance().createMessage(headers, new ByteArrayInputStream(msg));
							throw new ClientException("Ricevuto messaggio di errore: HTTP " + responseCode + " [SOAPFaultCode: " + createMessage.getSOAPBody().getFault().getFaultCode() + " - SOAPFaultString: " + createMessage.getSOAPBody().getFault().getFaultString() +"]",responseCode);
						} catch (IOException | SOAPException | NullPointerException e) {

						}

					throw new ClientException("Ricevuto [HTTP " + responseCode + "]",responseCode);
				}
			} finally {
				serverInfoResponse.setResponseCode(responseCode);
				this.serverInfoContext.processAfterSend(serverInfoResponse, dumpResponse);

				if(log.isTraceEnabled() && dumpResponse.getHeaders() != null) {
					StringBuilder sb = new StringBuilder();
					for(String key : dumpResponse.getHeaders().keySet()) { 
						sb.append("\n\t" + key + ": " + dumpResponse.getHeaders().get(key));
					}
					if(msg != null) sb.append("\n" + new String(msg));
					log.trace(sb.toString());
				}
			}
		}  catch (ClientException e) {
			throw e;
		} finally { 
			// funzionalita' di log
			popolaContextEvento(fromHttpMethod(httpMethod), responseCode, dumpRequest, dumpResponse);
			
			// chiusura della connessione
			try {
				this.disconnect();
			}catch(ClientException e) {
				log.error("Errore in fase di chiusura delle risorse: " + e.getMessage(),e);
			}
		}

	}

	private void setTipoEvento(boolean soap, String azione, String swaggerOperationId) {
		if(this.tipoEventoCustom != null) {
			this.getEventoCtx().setTipoEvento(this.tipoEventoCustom);
		} else {
			if(soap) {
				this.getEventoCtx().setTipoEvento(azione);
			} else {
				this.getEventoCtx().setTipoEvento(swaggerOperationId);
			}
		}
	}

	//	@Override
	public void disconnect() throws ClientException{
		List<Throwable> listExceptionChiusura = new ArrayList<>();
		try{
			// Gestione finale della connessione    		
			if(this.isResponse!=null){
				if(this.debug) {
					log.debug("Chiusura socket...");
				}
				this.isResponse.close();
			}				
		}
		catch(Throwable t) {
			log.debug("Chiusura socket fallita: "+t.getMessage(),t);
			listExceptionChiusura.add(t);
		}
		try{
			// Gestione finale della connessione
			if(this.httpEntityResponse!=null){
				if(this.debug) {
					log.debug("Chiusura httpEntityResponse...");
				}
				EntityUtils.consume(this.httpEntityResponse);
			}
		}catch(Throwable t) {
			log.debug("Chiusura connessione fallita: "+t.getMessage(),t);
			listExceptionChiusura.add(t);
		}

		if(!listExceptionChiusura.isEmpty()) {
			org.openspcoop2.utils.UtilsMultiException multiException = new org.openspcoop2.utils.UtilsMultiException(listExceptionChiusura.toArray(new Throwable[1]));
			throw new ClientException("Chiusura connessione non riuscita: "+multiException.getMessage(),multiException,500);
		}
	}


	public byte[] sendSoap(String azione, byte[] body, boolean isAzioneInUrl) throws ClientException { 
		List<Property> headerProperties = new ArrayList<>();
		headerProperties.add(new Property(HttpHeaders.ACCEPT, MediaType.TEXT_XML_VALUE));
		return this.send(true, azione, body, isAzioneInUrl, MediaType.TEXT_XML_VALUE, headerProperties, null, null, HttpRequestMethod.POST);
	}

	public byte[] getJson(String path, List<Property> headerProperties, String swaggerOperationId) throws ClientException {
		return this.send(false, null, null, false, null, headerProperties, swaggerOperationId, path, HttpRequestMethod.GET);
	}

	public byte[] sendJson(String path, byte[] jsonBody, List<Property> headerProperties, HttpRequestMethod httpMethod, String swaggerOperationId) throws ClientException {
		return this.send(false, null, jsonBody, false, MediaType.APPLICATION_JSON_VALUE, headerProperties, swaggerOperationId, path, httpMethod);
	}

	public byte[] sendJson(String path, byte[] jsonBody, List<Property> headerProperties, HttpRequestMethod httpMethod, String contentType, String swaggerOperationId) throws ClientException {
		return this.send(false, null, jsonBody, false, contentType, headerProperties, swaggerOperationId, path, httpMethod);
	}

	protected void popolaContextEvento(HttpMethod httpMethod, int responseCode, DumpRequest dumpRequest, DumpResponse dumpResponse) {
		if(GovpayConfig.getInstance().isGiornaleEventiEnabled()) {
			boolean logEvento = false;
			boolean dumpEvento = false;
			GdeInterfaccia configurazioneInterfaccia = EventiUtils.getConfigurazioneComponente(this.getEventoCtx().getComponente(), this.getGiornale());

			log.debug("Log Evento Client: [{}], Operazione [{}], Method [{}], Url [{}], StatusCode [{}]", this.getEventoCtx().getComponente(), this.getEventoCtx().getTipoEvento(), httpMethod, this.url.toExternalForm(), responseCode);

			if(configurazioneInterfaccia != null) {
				try {
					log.debug("Configurazione Giornale Eventi API: [{}]: {}" ,this.getEventoCtx().getComponente() , ConverterUtils.toJSON(configurazioneInterfaccia));
				} catch (it.govpay.core.exceptions.IOException e) {
					log.error("Errore durante il log della configurazione giornale eventi: " +e.getMessage(), e);
				}

				if(EventiUtils.isRequestLettura(httpMethod, this.getEventoCtx().getComponente(), this.getEventoCtx().getTipoEvento())) {
					logEvento = EventiUtils.logEvento(configurazioneInterfaccia.getLetture(), responseCode);
					dumpEvento = EventiUtils.dumpEvento(configurazioneInterfaccia.getLetture(), responseCode);
					log.debug("Tipo Operazione 'Lettura', Log [{}], Dump [{}].", logEvento, dumpEvento);
				} else if(EventiUtils.isRequestScrittura(httpMethod, this.getEventoCtx().getComponente(), this.getEventoCtx().getTipoEvento())) {
					logEvento = EventiUtils.logEvento(configurazioneInterfaccia.getScritture(), responseCode);
					dumpEvento = EventiUtils.dumpEvento(configurazioneInterfaccia.getScritture(), responseCode);
					log.debug("Tipo Operazione 'Scrittura', Log [{}], Dump [{}].", logEvento, dumpEvento);
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
				log.warn("La configurazione per l'API [{}] non e' corretta, salvataggio evento non eseguito.", this.getEventoCtx().getComponente()); 
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

	protected HttpMethod fromHttpMethod(HttpRequestMethod httpMethod) {
		if(httpMethod != null) {
			switch (httpMethod) {
			case DELETE:
				return HttpMethod.DELETE;
			case GET:
				return HttpMethod.GET;
			case HEAD:
				return HttpMethod.HEAD;
			case LINK:
				return HttpMethod.LINK;
			case OPTIONS:
				return HttpMethod.OPTIONS;
			case PATCH:
				return HttpMethod.PATCH;
			case POST:
				return HttpMethod.POST;
			case PUT:
				return HttpMethod.PUT;
			case TRACE:
				return HttpMethod.TRACE;
			case UNLINK:
				return HttpMethod.UNLINK;
			}
		}

		return null;
	}

	public static boolean cleanCache(String bundleKey) {
		if(USE_POOL)
			cmMap.remove(bundleKey);
		
		return sslContextFactorys.remove(bundleKey) != null;
	}

	public static boolean cleanCache() {
		sslContextFactorys = new HashMap<>();
		
		if(USE_POOL)
			cmMap = new HashMap<>();
		
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

	public String getTipoEventoCustom() {
		return tipoEventoCustom;
	}

	public void setTipoEventoCustom(String tipoEventoCustom) {
		this.tipoEventoCustom = tipoEventoCustom;
	}

	class CustomHttpEntity extends HttpEntityEnclosingRequestBase{

		private HttpRequestMethod httpMethod;
		public CustomHttpEntity(HttpRequestMethod httpMethod) {
			super();
			this.httpMethod = httpMethod;
		} 

		public CustomHttpEntity(HttpRequestMethod httpMethod, final URI uri) {
			super();
			setURI(uri);
			this.httpMethod = httpMethod;
		}

		public CustomHttpEntity(HttpRequestMethod httpMethod, final String uri) {
			super();
			setURI(URI.create(uri));
			this.httpMethod = httpMethod;
		}

		@Override
		public String getMethod() {
			return this.httpMethod.name();
		}

	}
}
