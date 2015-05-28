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
package it.govpay.ejb.utils.rs.client;

import it.govpay.ejb.exception.GovPayEnteException;
import it.govpay.ejb.exception.GovPayEnteException.EnteFaultCode;
import it.govpay.ejb.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.exception.GovPayException;
import it.govpay.ejb.model.EnteCreditoreModel;
import it.govpay.ejb.model.ScadenzarioModel;
import it.govpay.ejb.utils.rs.HttpUrlUtils;
import it.govpay.ejb.utils.rs.JaxbUtils;
import it.govpay.rs.ErroreEnte;
import it.govpay.rs.Pagamento;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;


public class ScadenzarioRemotoClient extends BasicClient {
	
	public ScadenzarioRemotoClient(EnteCreditoreModel ente, ScadenzarioModel scadenzario) throws GovPayException {
		super(ente.getIdEnteCreditore(), scadenzario.getConnettoreVerifica());
	}
	
	public Pagamento ricercaPagamento(String identificativoBeneficiario, String iuv) throws GovPayException {
		if(url == null) {
			throw new GovPayEnteException(EnteFaultCode.PAGAMENTO_SCONOSCIUTO, "Connetto al servizio di ricerca pagamento non specificato.");
		} else {
			try {
				url = HttpUrlUtils.addParameter(url, "identificativoBeneficiario", identificativoBeneficiario);
				url = HttpUrlUtils.addParameter(url, "iuv", iuv);
			} catch (MalformedURLException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore nella costruzione della URL di invocazione del servizio di ricerca", e);
			}
		}
		
		// Creazione Connessione
		URLConnection connection = null;
		try { 
			connection = url.openConnection();
		} catch(Exception e) {
			throw new GovPayEnteException(EnteFaultCode.RS_ERRORE_RETE, e);
		}
		HttpURLConnection httpConn = (HttpURLConnection) connection;	

		// Imposta Contesto SSL se attivo
		if(sslContext != null){
			HttpsURLConnection httpsConn = (HttpsURLConnection) httpConn;
			httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
			HostNameVerifierDisabled disabilitato = new HostNameVerifierDisabled();
			httpsConn.setHostnameVerifier(disabilitato);
		}

		try {
			httpConn.setRequestMethod("GET");
		} catch (java.net.ProtocolException e) {
			throw new GovPayEnteException(EnteFaultCode.RS_ERRORE_RETE, e);
		}
		
		int responseCode;
		
		try {
			responseCode = httpConn.getResponseCode();
		} catch (Exception e) {
			throw new GovPayEnteException(EnteFaultCode.RS_ERRORE_RETE, e);
		}
		if(responseCode < 300) {
			Object responseObj;
			try {
				responseObj = JaxbUtils.unMarshall(httpConn.getInputStream());
			} catch (Exception e) {
				throw new GovPayEnteException(EnteFaultCode.RS_RISPOSTA_SCONOSCIUTA, e);
			}
			if(responseObj instanceof Pagamento)
				return (Pagamento) responseObj;
			if(responseObj instanceof ErroreEnte) {
				ErroreEnte errore = (ErroreEnte) responseObj;
				throw new GovPayEnteException(errore);
			}
			throw new GovPayEnteException(EnteFaultCode.RS_RISPOSTA_SCONOSCIUTA, "Atteso un messaggio Pagamento o Errore, ritornato un " + responseObj.getClass().getName());
		} else {
			throw new GovPayEnteException(EnteFaultCode.RS_ERRORE_HTTP, "Il servizio di ricercaPagamento ha ritornato un HTTP " + responseCode);
		}
	}
}
