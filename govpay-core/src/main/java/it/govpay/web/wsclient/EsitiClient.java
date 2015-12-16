/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.web.wsclient;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Applicazione.Versione;
import it.govpay.bd.model.Esito;
import it.govpay.exception.GovPayException;

import org.apache.commons.codec.binary.Base64;

public class EsitiClient extends BasicClient {

	private static Logger log = LogManager.getLogger();
	
	public EsitiClient(Applicazione applicazione) throws GovPayException {
		super(applicazione, TipoConnettore.ESITO);
	}
	
	public SendEsitoResponse invoke(Versione versione, Esito esito) throws Exception {
		log.debug("Spedisco l'esito ["+esito.getId()+"] all'url ["+url+"]");
		URLConnection con = url.openConnection();
		if(ishttpBasicEnabled) {
			String auth =  httpBasicUser + ":" +  httpBasicPassword; 
			String authentication = "Basic " + Base64.encodeBase64(auth.getBytes());
			((HttpURLConnection) con).setRequestProperty("Authorization", authentication);
			((HttpURLConnection) con).setRequestMethod("POST");
		} 
		
		if(isSslEnabled){
			((HttpsURLConnection) con).setSSLSocketFactory(sslContext.getSocketFactory());
			((HttpsURLConnection) con).setRequestMethod("POST");
		}
		
		con.setDoOutput(true);
		con.setDoInput(true);
		
		//TODO portare l'informazione sugli headers in tabella
		if(versione.equals(Versione.GPv2)) {
			con.setRequestProperty("SOAPAction", "paInviaEsitoPagamento");
			con.setRequestProperty("Content-Type", "text/xml");
		}
		
		con.getOutputStream().write(esito.getXml());
		con.getOutputStream().close();

		SendEsitoResponse response = new SendEsitoResponse();
		
		response.setResponseCode(((HttpURLConnection) con).getResponseCode());
		InputStream	is = (response.getResponseCode() <= 299) ? ((HttpURLConnection) con).getInputStream(): ((HttpURLConnection) con).getErrorStream();
		
		if(is != null) {
			String detail = IOUtils.toString(is);
			IOUtils.closeQuietly(is);
			response.setDetail(detail);
		}
		
		log.debug("Spedizione eseguita [HttpResponse: " + response.responseCode + "]");
		log.trace("Contenuto risposta [" + response.detail + "]");
		
		return response;
	}
	
	public class SendEsitoResponse {

		private int responseCode;
		private String detail;
		public int getResponseCode() {
			return responseCode;
		}
		public void setResponseCode(int responseCode) {
			this.responseCode = responseCode;
		}
		public String getDetail() {
			return detail;
		}
		public void setDetail(String detail) {
			this.detail = detail;
		}
	}
}
