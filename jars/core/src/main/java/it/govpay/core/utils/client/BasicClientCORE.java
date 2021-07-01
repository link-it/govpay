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
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
import org.apache.http.util.EntityUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
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
import org.openspcoop2.utils.transport.TransportUtils;
import org.openspcoop2.utils.transport.http.HttpBodyParameters;
import org.openspcoop2.utils.transport.http.HttpConstants;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;
import org.openspcoop2.utils.transport.http.HttpUtilities;
import org.openspcoop2.utils.transport.http.WrappedLogSSLSocketFactory;
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

public abstract class BasicClientCORE {

	private static final String SOAP_ACTION = "SOAPAction";
	private static Logger log = LoggerWrapperFactory.getLogger(BasicClientCORE.class);

	protected boolean debug = true;
	//	protected static Map<String, SSLContext> sslContexts = new HashMap<>();
	protected URL url = null;
	//	protected SSLContext sslContext;
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

	private static boolean USE_POOL = true;

	private HttpEntity httpEntityResponse = null;
	private HttpClient httpClient = null;

	private HttpRequestBase httpRequest;
	private InputStream isResponse = null;

	protected Integer connectionTimeout;
	protected Integer readTimeout;

	protected DumpRequest dumpRequest = null;
	protected DumpResponse dumpResponse = null;
	protected ServerInfoResponse serverInfoResponse = null;
	protected ServerInfoRequest serverInfoRequest = null;

	protected Connettore connettore = null;

	protected static Map<String, SSLSocketFactory> sslContextFactorys = new HashMap<>();
	protected SSLSocketFactory sslContextFactory;

	protected BasicClientCORE(Intermediario intermediario, TipoOperazioneNodo tipoOperazione) throws ClientException {
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

	protected BasicClientCORE(Applicazione applicazione, TipoConnettore tipoConnettore) throws ClientException {
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

	protected BasicClientCORE(String operazioneSwagger, TipoDestinatario tipoDestinatario, Connettore connettore) throws ClientException {
		this(tipoDestinatario +"_" + operazioneSwagger, connettore);
		errMsg = operazioneSwagger + " per invocazione APP_IO";
		mittente = "GovPay";
		destinatario = "APP_IO";
		integrationCtx = new IntegrationContext();
		integrationCtx.setApplicazione(null);
		integrationCtx.setIntermediario(null);
		integrationCtx.setTipoConnettore(null);
		integrationCtx.setTipoDestinatario(tipoDestinatario);
	}

	protected BasicClientCORE(Dominio dominio, TipoConnettore tipoConnettore, ConnettoreNotificaPagamenti connettore) throws ClientException {
		this("D_" + tipoConnettore + "_" + dominio.getCodDominio(), connettore);
		errMsg = tipoConnettore.toString() + " del dominio (" + dominio.getCodDominio() + ")";
		mittente = "GovPay";
		destinatario = "ServizioMyPivot";
		integrationCtx = new IntegrationContext();
		integrationCtx.setApplicazione(null);
		integrationCtx.setIntermediario(null);
		integrationCtx.setTipoConnettore(tipoConnettore);
		integrationCtx.setTipoDestinatario(TipoDestinatario.MYPIVOT);
	}

	private BasicClientCORE(String bundleKey, Connettore connettore) throws ClientException {
		this.readTimeout = 180000;
		this.connectionTimeout = 10000;
		
		this.dumpRequest = new DumpRequest();
		this.dumpResponse = new DumpResponse();
		this.serverInfoRequest = new ServerInfoRequest();
		this.serverInfoResponse = new ServerInfoResponse();
		IContext ctx = ContextThreadLocal.get();
		this.serverInfoContext = new ServerInfoContextManuallyAdd(this.getServerConfig(ctx));

		// inizializzazione base del context evento
		this.eventoCtx = new EventoContext();
		this.getEventoCtx().setCategoriaEvento(Categoria.INTERFACCIA);
		this.getEventoCtx().setRole(Role.CLIENT);
		this.getEventoCtx().setDataRichiesta(new Date());

		this.serverID = bundleKey;
		this.connettore = connettore;
		if(this.connettore == null) {
			throw new ClientException("Connettore non configurato");
		}

		try {
			this.url =  new URL(this.connettore.getUrl());
		} catch (Exception e) {
			throw new ClientException("La URL del connettore " + this.errMsg + " non e' valida: " + e);
		}
		this.sslContextFactory = sslContextFactorys.get(bundleKey);
		//		this.sslContext = sslContexts.get(bundleKey);

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
							throw new ClientException("Configurazione SSL Client del connettore " + this.errMsg + " incompleta.");	

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
						throw new ClientException("Configurazione SSL Server del connettore " + this.errMsg + " incompleta.");	

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

	private static Map<String, PoolingHttpClientConnectionManager> cmMap = new HashMap<String, PoolingHttpClientConnectionManager>();
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
			cm.setMaxTotal(200);
			// Increase default max connection per route to 20
			cm.setDefaultMaxPerRoute(5);
			// Increase max connections for localhost:80 to 50
			//HttpHost localhost = new HttpHost("locahost", 80);
			//cm.setMaxPerRoute(new HttpRoute(localhost), 50);

			BasicClientCORE.cmMap.put(key, cm);
		}
	}

	private HttpClient buildHttpClient(ConnectionKeepAliveStrategy keepAliveStrategy, SSLSocketFactory sslSocketFactory, boolean usePool, String key, Connettore connettore) throws UtilsException{

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
			
			log.debug("-----GET CONNECTION [START] ----");
			log.debug("PRIMA CLOSE AVAILABLE["+cm.getTotalStats().getAvailable()+"] LEASED["
					+cm.getTotalStats().getLeased()+"] MAX["+cm.getTotalStats().getMax()+"] PENDING["+cm.getTotalStats().getPending()+"]");
//			 BLOCKED ConnettoreHTTPCORE.cm.closeExpiredConnections();
//			 BLOCKED ConnettoreHTTPCORE.cm.closeIdleConnections(30, java.util.concurrent.TimeUnit.SECONDS);
			log.debug("DOPO CLOSE AVAILABLE["+cm.getTotalStats().getAvailable()+"] LEASED["
					+cm.getTotalStats().getLeased()+"] MAX["+cm.getTotalStats().getMax()+"] PENDING["+cm.getTotalStats().getPending()+"]");
			

			//System.out.println("-----GET CONNECTION [START] ----");
			//System.out.println("PRIMA CLOSE AVAILABLE["+cm.getTotalStats().getAvailable()+"] LEASED["
			//		+cm.getTotalStats().getLeased()+"] MAX["+cm.getTotalStats().getMax()+"] PENDING["+cm.getTotalStats().getPending()+"]");
			// BLOCKED ConnettoreHTTPCORE.cm.closeExpiredConnections();
			// BLOCKED ConnettoreHTTPCORE.cm.closeIdleConnections(30, java.util.concurrent.TimeUnit.SECONDS);
			//System.out.println("DOPO CLOSE AVAILABLE["+cm.getTotalStats().getAvailable()+"] LEASED["
			//		+cm.getTotalStats().getLeased()+"] MAX["+cm.getTotalStats().getMax()+"] PENDING["+cm.getTotalStats().getPending()+"]");
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
				//		+cm.getTotalStats().getLeased()+"] MAX["+cm.getTotalStats().getMax()+"] PENDING["+cm.getTotalStats().getPending()+"]");
				//System.out.println("-----GET CONNECTION [END] ----");

		return httpClientBuilder.build();
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

	private byte[] send(boolean soap, String azione, byte[] body, boolean isAzioneInUrl, 
			String contentType, List<Property> headerProperties, String swaggerOperationId, String path, HttpRequestMethod httpMethod)  throws ClientException {
		int responseCode = 0;
		byte [] msg = null;

		if(soap) {
			this.getEventoCtx().setTipoEvento(azione);
		} else {
			this.getEventoCtx().setTipoEvento(swaggerOperationId);
		}

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
						//					responseCode = 500;
						throw new ClientException("Url di connessione malformata: " + location.concat(azione), e);
					}
				}
			} else {
				if(!location.endsWith("/")) location = location.concat("/");
				try {
					// elimino la possibilita' di avere due '/'
					path = path.startsWith("/") ? path.substring(1) : path;
					this.url = new URL(location.concat(path));
					log.debug("La richiesta sara' spedita alla URL: ["+this.url+"].");
				} catch (MalformedURLException e) {
					throw new ClientException("Url di connessione malformata: " + location.concat(path), e);
				}
			}

			this.serverInfoContext = new ServerInfoContextManuallyAdd(this.getServerConfig(ctx));
			this.serverInfoRequest.setAddress(this.url.toString());
			this.serverInfoRequest.setHttpRequestMethod(httpMethod);

			if(this.debug)
				log.debug("Creazione URL ["+location+"]...");

			// Keep-alive
			ConnectionKeepAliveStrategy keepAliveStrategy = null; //new ConnectionKeepAliveStrategyCustom();

			// Creazione Connessione
			if(this.debug)
				log.info("Creazione connessione alla URL ["+location+"]...",false);

			// creazione client
			try {
				this.httpClient = buildHttpClient(keepAliveStrategy, this.sslContextFactory, BasicClientCORE.USE_POOL, this.serverID, this.connettore); 
			} catch (UtilsException e) {
				responseCode = 500;
				throw new ClientException(e);
			}

			// HttpMethod
			if(httpMethod==null){
				throw new ClientException("HttpRequestMethod non definito");
			}
			this.httpRequest = null;
			switch (httpMethod) {
			case GET:
				this.httpRequest = new HttpGet(url.toString());
				break;
			case DELETE:
				this.httpRequest = new HttpDelete(url.toString());
				break;
			case HEAD:
				this.httpRequest = new HttpHead(url.toString());
				break;
			case POST:
				this.httpRequest = new HttpPost(url.toString());
				break;
			case PUT:
				this.httpRequest = new HttpPost(url.toString());
				break;
			case OPTIONS:
				this.httpRequest = new HttpOptions(url.toString());
				break;
			case TRACE:
				this.httpRequest = new HttpTrace(url.toString());
				break;
			case PATCH:
				this.httpRequest = new HttpPatch(url.toString());
				break;	
			default:
				this.httpRequest = new CustomHttpEntity(httpMethod, url.toString());
				break;
			}

			RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();

			// Impostazione Content-Type della Spedizione su HTTP

			if(this.debug)
				log.debug("Impostazione content type...");

			if(contentType != null) {
				this.dumpRequest.setContentType(contentType);
				this.httpRequest.addHeader("Content-Type", contentType);
			}

			// Impostazione transfer-length
			if(this.debug)
				log.debug("Impostazione transfer-length...");
			boolean transferEncodingChunked = false;
			String tlm = null;
			int chunkLength = -1;

			//			TODO
			//			if(ConsegnaContenutiApplicativi.ID_MODULO.equals(this.idModulo)){
			//				tlm = this.openspcoopProperties.getTransferLengthModes_consegnaContenutiApplicativi();
			//				chunkLength = this.openspcoopProperties.getChunkLength_consegnaContenutiApplicativi();
			//			}
			//			else{
			//				// InoltroBuste e InoltroRisposte
			//				tlm = this.openspcoopProperties.getTransferLengthModes_inoltroBuste();
			//				chunkLength = this.openspcoopProperties.getChunkLength_inoltroBuste();
			//			}
			//			transferEncodingChunked = TransferLengthModes.TRANSFER_ENCODING_CHUNKED.equals(tlm);
			//			if(transferEncodingChunked){
			//				//this.httpConn.setChunkedStreamingMode(chunkLength);
			//			}
			if(this.debug)
				log.info("Impostazione transfer-length effettuata (chunkLength:"+chunkLength+"): "+tlm,false);



			// Impostazione timeout
			if(this.debug)
				log.debug("Impostazione timeout...");
			int connectionTimeout = -1;
			int readConnectionTimeout = -1;
			if(this.connectionTimeout!=null){
				connectionTimeout = this.connectionTimeout.intValue();
			}
			if(connectionTimeout==-1){
				connectionTimeout = HttpUtilities.HTTP_CONNECTION_TIMEOUT;
			}
			if(this.readTimeout !=null){
				readConnectionTimeout = this.readTimeout.intValue();
			}
			if(readConnectionTimeout==-1){
				readConnectionTimeout = HttpUtilities.HTTP_READ_CONNECTION_TIMEOUT;
			}
			if(this.debug)
				log.info("Impostazione http timeout CT["+connectionTimeout+"] RT["+readConnectionTimeout+"]",false);

			requestConfigBuilder.setConnectionRequestTimeout(connectionTimeout);
			requestConfigBuilder.setConnectTimeout(connectionTimeout);
			requestConfigBuilder.setSocketTimeout(readConnectionTimeout);

			// Gestione automatica del redirect
			//this.httpConn.setInstanceFollowRedirects(true); 






			// Aggiunga del SoapAction Header in caso di richiesta SOAP
			if(soap) {
				if(this.debug)
					log.debug("Impostazione soap action...");
				this.dumpRequest.getHeaders().put(SOAP_ACTION, "\"" + azione + "\"");
				this.httpRequest.addHeader(SOAP_ACTION, "\"" + azione + "\"");
				if(this.debug)
					log.info("SOAP Action inviata ["+azione+"]",false);
			}

			// Authentication BASIC
			if(this.debug)
				log.debug("Impostazione autenticazione...");
			if(this.ishttpBasicEnabled) {
				Base64 base = new Base64();
				String encoding = new String(base.encode((this.httpBasicUser + ":" + this.httpBasicPassword).getBytes()));

				this.dumpRequest.getHeaders().put("Authorization", "Basic " + encoding);
				this.httpRequest.addHeader("Authorization", "Basic " + encoding);
			}


			// Impostazione Proprieta del trasporto
			if(this.debug)
				log.debug("Impostazione header di trasporto...");
			if(headerProperties!= null  && headerProperties.size() > 0) {
				for (Property prop : headerProperties) {
					this.httpRequest.addHeader(prop.getName(), prop.getValue());
					dumpRequest.getHeaders().put(prop.getName(), prop.getValue());
				}
			}


			// Impostazione Metodo
			HttpBodyParameters httpBody = null;
			try {
				httpBody = new HttpBodyParameters(httpMethod, contentType);
			} catch (UtilsException e) {
				responseCode = 500;
				throw new ClientException(e);
			}

			// Preparazione messaggio da spedire
			// Spedizione byte
			integrationCtx.setMsg(body);
			this.invokeOutHandlers();

			dumpRequest.setPayload(integrationCtx.getMsg());

			dumpRequest.getHeaders().put("HTTP-Method", httpMethod.name());
			dumpRequest.getHeaders().put("RequestPath", this.url.toString());

			this.serverInfoContext.processBeforeSend(serverInfoRequest, dumpRequest);

			if(httpBody.isDoOutput()){
				if(this.debug)
					log.debug("Spedizione byte...");

				HttpEntity httpEntity = new ByteArrayEntity(body);
				if(this.httpRequest instanceof HttpEntityEnclosingRequestBase){
					((HttpEntityEnclosingRequestBase)this.httpRequest).setEntity(httpEntity);
				}
				else{
					throw new ClientException("Tipo ["+this.httpRequest.getClass().getName()+"] non utilizzabile per una richiesta di tipo ["+httpMethod+"]");
				}
			}

			// Imposto Configurazione
			this.httpRequest.setConfig(requestConfigBuilder.build());

			// Spedizione byte
			if(this.debug)
				log.debug("Spedizione byte...");
			// Eseguo la richiesta e prendo la risposta
			HttpResponse httpResponse = null;
			try {
				httpResponse = this.httpClient.execute(this.httpRequest);
			} catch (ClientProtocolException e) {
				responseCode = 500;
				throw new ClientException(e);
			} catch (IOException e) {
				responseCode = 500;
				throw new ClientException(e);
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
					String value = null;

					if(hdrRisposta[i].getName()==null){
						// Check per evitare la coppia che ha come chiave null e come valore HTTP OK 200
						if(this.debug)
							log.debug("HTTP risposta ["+HttpConstants.RETURN_CODE+"] ["+hdrRisposta[i].getValue()+"]...");
						key = HttpConstants.RETURN_CODE;
						value = hdrRisposta[i].getValue();
					}
					else{
						if(this.debug)
							log.debug("HTTP risposta ["+hdrRisposta[i].getName()+"] ["+hdrRisposta[i].getValue()+"]...");
						key = hdrRisposta[i].getName();
						value = hdrRisposta[i].getValue();
					}

					List<String> list = null;
					if(dumpResponse.getHeaders().containsKey(key)) {
						list = Arrays.asList(StringUtils.split(dumpResponse.getHeaders().remove(key),","));
					}
					if(list==null) {
						list = new ArrayList<String>();

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
			//			this.resultHTTPMessage = httpResponse.getStatusLine().getReasonPhrase();

			try {
				if(responseCode < 300) {
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

				if(log.isTraceEnabled() && dumpResponse.getHeaders() != null) {
					StringBuffer sb = new StringBuffer();
					for(String key : dumpResponse.getHeaders().keySet()) { 
						sb.append("\n\t" + key + ": " + dumpResponse.getHeaders().get(key));
					}
					sb.append("\n" + new String(msg));
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
				throw e;
			}
		}

	}


	//	@Override
	public void disconnect() throws ClientException{
		List<Throwable> listExceptionChiusura = new ArrayList<Throwable>();
		try{
			// Gestione finale della connessione    		
			//System.out.println("CHECK CLOSE STREAM...");
			if(this.isResponse!=null){
				if(this.debug && log!=null)
					log.debug("Chiusura socket...");
				//System.out.println("CLOSE STREAM...");
				this.isResponse.close();
				//System.out.println("CLOSE STREAM");
			}				
		}
		catch(Throwable t) {
			log.debug("Chiusura socket fallita: "+t.getMessage(),t);
			listExceptionChiusura.add(t);
		}
		try{
			// Gestione finale della connessione
			//System.out.println("CHECK ENTITY...");
			if(this.httpEntityResponse!=null){
				if(this.debug && log!=null)
					log.debug("Chiusura httpEntityResponse...");
				//System.out.println("CLOSE ENTITY...");
				EntityUtils.consume(this.httpEntityResponse);
				//System.out.println("CLOSE ENTITY");
			}

			if(this.httpEntityResponse!=null){

			}

		}catch(Throwable t) {
			log.debug("Chiusura connessione fallita: "+t.getMessage(),t);
			listExceptionChiusura.add(t);
		}

		if(listExceptionChiusura!=null && !listExceptionChiusura.isEmpty()) {
			org.openspcoop2.utils.UtilsMultiException multiException = new org.openspcoop2.utils.UtilsMultiException(listExceptionChiusura.toArray(new Throwable[1]));
			throw new ClientException("Chiusura connessione non riuscita: "+multiException.getMessage(),multiException);
		}
	}


	public byte[] sendSoap(String azione, byte[] body, boolean isAzioneInUrl) throws ClientException { 
		return this.send(true, azione, body, isAzioneInUrl, azione, null, null, null, HttpRequestMethod.POST);
	}

	public byte[] getJson(String path, List<Property> headerProperties, String swaggerOperationId) throws ClientException {
		return this.send(false, null, null, false, null, headerProperties, swaggerOperationId, path, HttpRequestMethod.GET);
	}

	public byte[] sendJson(String path, byte[] jsonBody, List<Property> headerProperties, HttpRequestMethod httpMethod, String swaggerOperationId) throws ClientException {
		return this.send(false, null, jsonBody, false, "application/json", headerProperties, swaggerOperationId, path, httpMethod);
	}

	public byte[] sendJson(String path, byte[] jsonBody, List<Property> headerProperties, HttpRequestMethod httpMethod, String contentType, String swaggerOperationId) throws ClientException {
		return this.send(false, null, jsonBody, false, contentType, headerProperties, swaggerOperationId, path, httpMethod);
	}

	protected void popolaContextEvento(HttpMethodEnum httpMethod, int responseCode, DumpRequest dumpRequest, DumpResponse dumpResponse) {
		if(GovpayConfig.getInstance().isGiornaleEventiEnabled()) {
			boolean logEvento = false;
			boolean dumpEvento = false;
			GdeInterfaccia configurazioneInterfaccia = GiornaleEventi.getConfigurazioneComponente(this.componente, this.getGiornale());

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
		if(USE_POOL)
			cmMap.remove(bundleKey);
		
		return sslContextFactorys.remove(bundleKey) != null;
		//		return sslContexts.remove(bundleKey) != null;
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
