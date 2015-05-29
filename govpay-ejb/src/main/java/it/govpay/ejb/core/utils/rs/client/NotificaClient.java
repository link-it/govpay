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
package it.govpay.ejb.core.utils.rs.client;

import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.core.model.ScadenzarioModel;
import it.govpay.ejb.core.utils.rs.JaxbUtils;
import it.govpay.rs.EsitoRevoca;
import it.govpay.rs.VerificaPagamento;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;


public class NotificaClient extends BasicClient{

	public NotificaClient(String idEnteCreditore, ScadenzarioModel scadenzario) throws GovPayException {
		super(idEnteCreditore, scadenzario.getConnettoreNotifica());
	}

	public void notificaVerificaPagamento(VerificaPagamento verifica) throws Exception {

		// Creazione Connessione
		URLConnection connection = null;
		try { 
			connection = url.openConnection();
		} catch(Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_CONFIGURAZIONE,"Impossibile creare la connessione al servizio notifica: " + url.toExternalForm(), e);
		}
		HttpURLConnection httpConn = (HttpURLConnection) connection;	

		// Imposta Contesto SSL se attivo
		if(sslContext != null){
			HttpsURLConnection httpsConn = (HttpsURLConnection) httpConn;
			httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
			HostNameVerifierDisabled disabilitato = new HostNameVerifierDisabled();
			httpsConn.setHostnameVerifier(disabilitato);
		}

		// Impostazione Content-Type della Spedizione su HTTP
		String verificaTxt = null;
		try{
			verificaTxt = JaxbUtils.toString(verifica);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO,"Impossibile serializzare il messaggio di notifica", e);
		}
		byte[] b = verificaTxt.getBytes();


		// Impostazione transfer-length
		try {
			httpConn.setRequestMethod("POST");
		} catch (java.net.ProtocolException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);

		try {
			OutputStream out = httpConn.getOutputStream();
			out.write(b);
			out.close();
		} catch (IOException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO,"Impossibile serializzare i dati nella connessione HTTP.", e);
		}


		if(httpConn.getResponseCode() >= 300) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO,"Notifica al servizio dell'ente fallita. " + httpConn.getResponseCode() + ": " + httpConn.getResponseMessage());
		}
	}

	public void notificaEsitoRevoca(EsitoRevoca webEsitoRevoca) throws Exception {
		// Creazione Connessione
		URLConnection connection = null;
		try { 
			connection = url.openConnection();
		} catch(Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_CONFIGURAZIONE,"Impossibile creare la connessione al servizio notifica: " + url.toExternalForm(), e);
		}
		HttpURLConnection httpConn = (HttpURLConnection) connection;	

		// Imposta Contesto SSL se attivo
		if(sslContext != null){
			HttpsURLConnection httpsConn = (HttpsURLConnection) httpConn;
			httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
			HostNameVerifierDisabled disabilitato = new HostNameVerifierDisabled();
			httpsConn.setHostnameVerifier(disabilitato);
		}

		// Impostazione Content-Type della Spedizione su HTTP
		String verificaTxt = null;
		try{
			verificaTxt = JaxbUtils.toString(webEsitoRevoca);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO,"Impossibile serializzare il messaggio di notifica", e);
		}
		byte[] b = verificaTxt.getBytes();


		// Impostazione transfer-length
		try {
			httpConn.setRequestMethod("POST");
		} catch (java.net.ProtocolException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);

		try {
			OutputStream out = httpConn.getOutputStream();
			out.write(b);
			out.close();
		} catch (IOException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO,"Impossibile serializzare i dati nella connessione HTTP.", e);
		}


		if(httpConn.getResponseCode() >= 300) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO,"Notifica al servizio dell'ente fallita. " + httpConn.getResponseCode() + ": " + httpConn.getResponseMessage());
		}
	}
}
