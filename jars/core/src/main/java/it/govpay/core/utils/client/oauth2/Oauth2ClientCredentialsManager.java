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
package it.govpay.core.utils.client.oauth2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.cxf.rs.security.oauth2.client.Consumer;
import org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.cxf.rs.security.oauth2.grants.clientcred.ClientCredentialsGrant;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.model.Connettore;

/***
 * Classe per la gestione dell'acquisizione dei token oauth2
 * 
 * @author Giuliano Pintori
 *
 */
public class Oauth2ClientCredentialsManager {

	private static Logger log = LoggerWrapperFactory.getLogger(Oauth2ClientCredentialsManager.class);

	private static Oauth2ClientCredentialsManager instance;

	public static Oauth2ClientCredentialsManager getInstance() {
		if(instance == null) {
			init();
		}

		return Oauth2ClientCredentialsManager.instance;
	}

	private static synchronized void init() {
		if(instance == null) {
			instance = new Oauth2ClientCredentialsManager();
		}
	}

	private Map<String, ClientAccessToken> mappaToken = null;

	public Oauth2ClientCredentialsManager() {
		this.mappaToken = new ConcurrentHashMap<>();		
	}

	public ClientAccessToken getClientCredentialsAccessToken(String key, Connettore connettore)  throws ClientException{
		log.debug("Check esistenza token per il client [{}] ..." , key);
		ClientAccessToken accessToken = this.mappaToken.get(key);

		if(isAccessTokenExpired(accessToken)) {
			// elimino il vecchio token 
			this.mappaToken.remove(key);

			log.debug("Token per il client [{}] non valido, acquisizione nuovo token in corso..." , key);

			String tokenEndpoint = connettore.getOauth2ClientCredentialsUrlTokenEndpoint(); 
			String clientId = connettore.getOauth2ClientCredentialsClientId();
			String clientSecret = connettore.getOauth2ClientCredentialsClientSecret();
			String scope = connettore.getOauth2ClientCredentialsScope();

			// Creo oggetto credenziali 
			Consumer consumer = new Consumer(clientId, clientSecret);

			// creo oggetto grant impostando l'eventuale scope
			ClientCredentialsGrant clientCredentialsGrant = new ClientCredentialsGrant(scope);

			// Create a WebClient for the token endpoint
			log.debug("Acquisizione token per il client [{}]: Url servizio Token: [{}]." , key, tokenEndpoint);

			try {
				accessToken = OAuthClientUtils.getAccessToken(tokenEndpoint, consumer, clientCredentialsGrant, true);
				log.debug("Acquisizione token per il client [{}]: conclusa con esito OK, ricevuto token: [{}]." , key, accessToken);
				this.mappaToken.put(key, accessToken);
			} catch (OAuthServiceException e) {
				log.warn("Errore nell'acquisizione token per il client [{}]: {}" , key, e.getMessage());
				throw new ClientException("Errore nell'acquisizione del token per il client ["+key+"]",500, e.getMessage().getBytes());
			}
		}

		return accessToken;
	}

	private boolean isAccessTokenExpired(ClientAccessToken accessToken) {
		if (accessToken != null) {
			long expirationTime = accessToken.getIssuedAt() + accessToken.getExpiresIn();
			long currentTime = System.currentTimeMillis() / 1000; // Convert to seconds

			return currentTime >= expirationTime;
		}
		return true; // se il token non e' presente lo considero scaduto
	}
}
