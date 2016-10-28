/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.client.handler.IntegrationContext;
import it.govpay.core.utils.client.handler.IntegrationOutHandler;
import it.govpay.model.Applicazione;
import it.govpay.model.Connettore;
import it.govpay.model.Intermediario;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.Connettore.EnumSslType;

import java.io.ByteArrayOutputStream;
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
import javax.xml.bind.JAXBElement;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.utils.logger.beans.Message;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.constants.MessageType;

public class BasicClient {

	private static Logger log = LogManager.getLogger();
	
	public class ClientException extends Exception {
		private static final long serialVersionUID = 1L;

		public ClientException(String message, Exception e) {
			super(message, e);
		}

		public ClientException(Exception e) {
			super(e);
		}

		public ClientException(String string) {
			super(string);
		}
	}
	
	protected static Map<String, SSLContext> sslContexts = new HashMap<String, SSLContext>();
	protected URL url = null;
	protected SSLContext sslContext;
	protected boolean ishttpBasicEnabled=false, isSslEnabled=false;
	protected String httpBasicUser, httpBasicPassword;
	protected String errMsg;
	protected String destinatario;
	protected String mittente;
	protected TipoDestinatario tipoDestinatario;

	public enum TipoConnettore {
		VERIFICA, NOTIFICA;
	}
	
	public enum TipoDestinatario {
		APPLICAZIONE, INTERMEDIARIO;
	}
	
	protected BasicClient(Intermediario intermediario) throws ClientException {
		this("I_" + intermediario.getCodIntermediario(), intermediario.getConnettorePdd());
		errMsg = "Pdd dell'intermediario (" + intermediario.getCodIntermediario() + ")";
		mittente = intermediario.getDenominazione();
		destinatario = "NodoDeiPagamentiDellaPA";
	}
	
	protected BasicClient(Applicazione applicazione, TipoConnettore tipoConnettore) throws ClientException {
		this("A_" + tipoConnettore + applicazione.getCodApplicazione(), tipoConnettore == TipoConnettore.NOTIFICA ? applicazione.getConnettoreNotifica() : applicazione.getConnettoreVerifica());
		GpThreadLocal.get().getIntegrationCtx().setApplicazione(applicazione);
		errMsg = tipoConnettore.toString() + " dell'applicazione (" + applicazione.getCodApplicazione() + ")";
		mittente = "GovPay";
		destinatario = applicazione.getCodApplicazione();
	}
	
	private BasicClient(String bundleKey, Connettore connettore) throws ClientException {
		
		if(connettore == null) {
			throw new ClientException("Connettore non configurato");
		}
		
		try {
			this.url =  new URL(connettore.getUrl());
		} catch (Exception e) {
			throw new ClientException("La URL del connettore " + errMsg + " non e' valida: " + e);
		}
		sslContext = sslContexts.get(bundleKey);
		
		if(connettore.getTipoAutenticazione().equals(EnumAuthType.SSL)) {
			isSslEnabled = true;
			if(sslContext == null) {
				try {
					FileInputStream finKeyStore = null;
					FileInputStream finTrustStore = null;
						
					KeyManager[] km = null;
					TrustManager[] tm = null;
			
					// Autenticazione CLIENT
					if(connettore.getTipoSsl().equals(EnumSslType.CLIENT)){
						
						if(connettore.getSslKsType() == null || 
								connettore.getSslKsLocation() == null ||
								connettore.getSslKsPasswd() == null ||
								connettore.getSslPKeyPasswd() == null)
								throw new ClientException("Configurazione SSL Client del connettore " + errMsg + " incompleta.");	
						
						KeyStore keystore = KeyStore.getInstance(connettore.getSslKsType()); // JKS,PKCS12,jceks,bks,uber,gkr
						finKeyStore = new FileInputStream(connettore.getSslKsLocation());
						keystore.load(finKeyStore, connettore.getSslKsPasswd().toCharArray());
						KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
						keyManagerFactory.init(keystore, connettore.getSslPKeyPasswd().toCharArray());
						km = keyManagerFactory.getKeyManagers();
					}
					
					if(connettore.getSslTsType() == null || 
							connettore.getSslTsLocation() == null ||
							connettore.getSslTsPasswd() == null || 
							connettore.getSslType() == null)
							throw new ClientException("Configurazione SSL Server del connettore " + errMsg + " incompleta.");	
			
					// Autenticazione SERVER
					KeyStore truststore = KeyStore.getInstance(connettore.getSslTsType()); // JKS,PKCS12,jceks,bks,uber,gkr
					finTrustStore = new FileInputStream(connettore.getSslTsLocation());
					truststore.load(finTrustStore, connettore.getSslTsPasswd().toCharArray());
					TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
					trustManagerFactory.init(truststore);
					tm = trustManagerFactory.getTrustManagers();
		
					// Creo contesto SSL
					sslContext = SSLContext.getInstance(connettore.getSslType());
					sslContext.init(km, tm, null);
					sslContexts.put(bundleKey, sslContext);
				} catch (Exception e) {
					throw new ClientException(e);
				}
			}
		}
		
		if(connettore.getTipoAutenticazione().equals(EnumAuthType.HTTPBasic)) {
			ishttpBasicEnabled = true;
			httpBasicUser = connettore.getHttpUser();
			httpBasicPassword = connettore.getHttpPassw();
		}
	}
	
	
	public byte[] sendXml(JAXBElement<?> body, boolean isAzioneInUrl) throws ClientException {
		return send(false, null, body, null, isAzioneInUrl);
	}

	private void invokeOutHandlers() throws ClientException {
		
		try {
			List<String> outHandlers = GovpayConfig.getInstance().getOutHandlers();
			if(outHandlers!= null && !outHandlers.isEmpty()) {
				IntegrationContext ic = GpThreadLocal.get().getIntegrationCtx();
	
				log.info("Applicazione al messaggio degli handlers configurati...");
				for(String handler: outHandlers) {
					Class<?> c = Class.forName(handler);
					IntegrationOutHandler instance = (IntegrationOutHandler) c.newInstance();
					log.info("Applicazione al messaggio dell'handler ["+handler+"]...");
					instance.invoke(ic);
					log.info("Applicazione al messaggio dell'handler ["+handler+"] completata con successo");
				}
				log.info("Applicazione al messaggio degli handlers configurati completata con successo");
			} else {
				log.info("Nessun handler configurato");
			}
		} catch(Exception e) {
			throw new ClientException("Errore durante l'applicazione al messaggio degli handlers configurati: " + e.getMessage(), e);
		}
	}
	
	
	public byte[] sendSoap(String azione, JAXBElement<?> body, Object header, boolean isAzioneInUrl) throws ClientException {
		return send(true, azione, body, header, isAzioneInUrl);
	}
	
	private byte[] send(boolean soap, String azione, JAXBElement<?> body, Object header, boolean isAzioneInUrl) throws ClientException {

		// Creazione Connessione
		int responseCode;
		HttpURLConnection connection = null;
		byte[] msg = null;
		GpContext ctx = GpThreadLocal.get();
		String urlString = url.toExternalForm();
		if(isAzioneInUrl) {
			if(!urlString.endsWith("/")) urlString = urlString.concat("/");
			try {
				url = new URL(urlString.concat(azione));
			} catch (MalformedURLException e) {
				throw new ClientException("Url di connessione malformata: " + urlString.concat(azione), e);
			}
		} 
	
		try {
			Message requestMsg = new Message();
			requestMsg.setType(MessageType.REQUEST_OUT);
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			if(soap) {
				connection.setRequestProperty("SOAPAction", "\"" + azione + "\"");
				requestMsg.addHeader(new Property("SOAPAction", "\"" + azione + "\""));
			}
			requestMsg.setContentType("text/xml");
			connection.setRequestProperty("Content-Type", "text/xml");
			connection.setRequestMethod("POST");
	
			// Imposta Contesto SSL se attivo
			if(sslContext != null){
				HttpsURLConnection httpsConn = (HttpsURLConnection) connection;
				httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
				HostNameVerifierDisabled disabilitato = new HostNameVerifierDisabled();
				httpsConn.setHostnameVerifier(disabilitato);
			}
	
			// Imposta l'autenticazione HTTP Basic se attiva
			if(ishttpBasicEnabled) {
				Base64 base = new Base64();
				String encoding = new String(base.encode((httpBasicUser + ":" + httpBasicPassword).getBytes()));
				connection.setRequestProperty("Authorization", "Basic " + encoding);
				requestMsg.addHeader(new Property("Authorization", "Basic " + encoding));
			}
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if(soap) {
				SOAPUtils.writeMessage(body, header, baos);
			} else {
				JaxbUtils.marshal(body, baos);
			}

			ctx.getIntegrationCtx().setMsg(baos.toByteArray());
			invokeOutHandlers();
			
			if(log.getLevel().isMoreSpecificThan(Level.TRACE)) {
				StringBuffer sb = new StringBuffer();
				for(String key : connection.getRequestProperties().keySet()) {
					sb.append("\n\t" + key + ": " + connection.getRequestProperties().get(key));
				}
				sb.append("\n" + new String(ctx.getIntegrationCtx().getMsg()));
				log.trace(sb.toString());
			}
			
			requestMsg.setContent(ctx.getIntegrationCtx().getMsg());
			
			ctx.getContext().getRequest().setOutDate(new Date());
			ctx.getContext().getRequest().setOutSize(Long.valueOf(ctx.getIntegrationCtx().getMsg().length));
			ctx.log(requestMsg);
			
			connection.getOutputStream().write(ctx.getIntegrationCtx().getMsg());
	
		} catch (Exception e) {
			throw new ClientException(e);
		}
		try {
			responseCode = connection.getResponseCode();
			ctx.getTransaction().getServer().setTransportCode(Integer.toString(responseCode));
		} catch (Exception e) {
			throw new ClientException(e);
		}
		
		Message responseMsg = new Message();
		responseMsg.setType(MessageType.RESPONSE_IN);
		
		for(String key : connection.getHeaderFields().keySet()) {
			if(connection.getHeaderFields().get(key) != null) {
				if(key == null)
					responseMsg.addHeader(new Property("Status-line", connection.getHeaderFields().get(key).get(0)));
				else if(connection.getHeaderFields().get(key).size() == 1)
					responseMsg.addHeader(new Property(key, connection.getHeaderFields().get(key).get(0)));
				else
					responseMsg.addHeader(new Property(key, ArrayUtils.toString(connection.getHeaderFields().get(key))));
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
						responseMsg.setContent(msg);
					return msg;
				} catch (Exception e) {
					throw new ClientException("Messaggio di risposta non valido", e);
				}
			} else {
				try {
					msg = connection.getErrorStream() != null ? IOUtils.toByteArray(connection.getErrorStream()) : new byte[]{};
					responseMsg.setContent(msg);
				} catch (IOException e) {
					msg = ("Impossibile serializzare l'ErrorStream della risposta: " + e).getBytes() ;
				} finally {
					log.error("Errore nell'invocazione del Nodo dei Pagamenti: [HTTP Response Code " + responseCode + "]\nRisposta: " + new String(msg));
				}
				
				String txt = new String(msg);
				
				if(txt.length() > 300) {
					throw new ClientException("Ricevuto [HTTP " + responseCode + "]\nRisposta: " + txt.substring(0, 300) + "[--omissis--]");
				} else {
					throw new ClientException("Ricevuto [HTTP " + responseCode + "]\nRisposta: " + txt);
				}
			}
		} finally {
			if(responseMsg != null)
				ctx.log(responseMsg);

			if(log.getLevel().isMoreSpecificThan(Level.TRACE) && connection != null && connection.getHeaderFields() != null) {
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
		sslContexts = new HashMap<String, SSLContext>();
		return true;
	}
	
}
