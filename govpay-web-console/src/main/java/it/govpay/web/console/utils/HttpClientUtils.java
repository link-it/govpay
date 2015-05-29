/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.web.console.utils;

import it.govpay.ejb.core.utils.rs.JaxbUtils;
import it.govpay.rs.RichiestaPagamento;

import java.io.ByteArrayInputStream;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.Logger;


public class HttpClientUtils {

	public static HttpResponse sendRichiestaPagamento(String urlToInvoke,RichiestaPagamento richiestaPagamento, Logger log) throws Exception{
		HttpResponse responseGET = null;
		try{
			log.debug("Invio del pagamento in corso...");

			URL urlObj = new URL(urlToInvoke);
			HttpHost target = new HttpHost(urlObj.getHost(), urlObj.getPort(), urlObj.getProtocol());
			CloseableHttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();
			HttpPost richiestaPost = new HttpPost();

			richiestaPost.setURI(urlObj.toURI());

			log.debug("Serializzazione pagamento in corso...");
			byte[] bufferPagamento = JaxbUtils.toBytes(richiestaPagamento);
			log.debug("Serializzazione pagamento completata.");

			HttpEntity bodyEntity = new InputStreamEntity(new ByteArrayInputStream(bufferPagamento),ContentType.APPLICATION_XML);
			richiestaPost.setEntity(bodyEntity);
			richiestaPost.setHeader("Content-Type", ContentType.APPLICATION_XML.getMimeType());

			log.debug("Invio tramite client Http in corso...");
			responseGET = client.execute(target, richiestaPost);
			
			if(responseGET == null)
				throw new NullPointerException("La Response HTTP e' null");
			
			log.debug("Invio tramite client Http completato.");
			return responseGET;
		}catch(Exception e){
			log.error("Errore durante l'invio della richiesta di pagamento: " + e.getMessage() , e);
			throw e;
		}
	}
	
	public static HttpResponse getEsitoPagamento(String urlToInvoke, Logger log) throws Exception{
		HttpResponse responseGET = null;
		try{
			log.debug("Richiesta Esito del pagamento in corso...");

			URL urlObj = new URL(urlToInvoke);
			HttpHost target = new HttpHost(urlObj.getHost(), urlObj.getPort(), urlObj.getProtocol());
			CloseableHttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();
			HttpGet richiestaPost = new HttpGet();

			richiestaPost.setURI(urlObj.toURI());

			log.debug("Invio tramite client Http in corso...");
			responseGET = client.execute(target, richiestaPost);
			
			if(responseGET == null)
				throw new NullPointerException("La Response HTTP e' null");
			
			log.debug("Invio tramite client Http completato.");
			return responseGET;
		}catch(Exception e){
			log.error("Errore durante l'invio della richiesta di pagamento: " + e.getMessage() , e);
			throw e;
		}
	}
}
