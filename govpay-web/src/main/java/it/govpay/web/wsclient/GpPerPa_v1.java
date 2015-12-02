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

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.NdpFaultCode;
import it.govpay.bd.model.Rt.FaultPa;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.exception.GovPayNdpException;
import it.govpay.rs.ErroreEnte;
import it.govpay.rs.Pagamento;
import it.govpay.utils.JaxbUtils;
import it.govpay.utils.UrlUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GpPerPa_v1 extends BasicClient {

	private Logger log = LogManager.getLogger();

	public GpPerPa_v1(Applicazione applicazione, TipoConnettore tipoConnettore) throws GovPayException {
		super(applicazione, TipoConnettore.VERIFICA);
	}
	
	public Pagamento verifica(String codDominio, String iuv) throws GovPayException {
		if(url == null) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_NDP, FaultPa.PAA_PAGAMENTO_SCONOSCIUTO, "Connettore al servizio di ricerca pagamento non specificato.");
		} else {
			try {
				url = UrlUtils.addParameter(url, "identificativoBeneficiario", codDominio);
				url = UrlUtils.addParameter(url, "iuv", iuv);
			} catch (MalformedURLException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore nella costruzione della URL di invocazione del servizio di ricerca", e);
			}
		}
		
		// Creazione Connessione
		int responseCode;
		HttpURLConnection connection = null;
		try { 
			connection = (HttpURLConnection) url.openConnection();
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
			}
			connection.setRequestProperty("Accept", "text/xml");
			connection.setRequestMethod("GET");
			responseCode = connection.getResponseCode();
		} catch (Exception e) {
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_ENTE_WEB, FaultPa.PAA_ERRORE_INTERNO, "Errore nell'invocazione del connettore di verifica.", e);
		}
		if(responseCode < 300) {
			Object responseObj;
			try {
				responseObj = JaxbUtils.toRestObject(connection.getInputStream());
			} catch (Exception e) {
				log.error("Messaggio di risposta non valido: " + e);
				try {
					log.debug("HTTP Response message: " + new String(IOUtils.toByteArray(connection.getInputStream())));
				} catch (IOException ee) {
				}
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_ENTE_WEB, FaultPa.PAA_ERRORE_INTERNO, "Risposta dal servizio di verifica sconosciuta.", e);
			}
			if(responseObj instanceof Pagamento)
				return (Pagamento) responseObj;
			if(responseObj instanceof ErroreEnte) {
				throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_ENTE_WEB, toFaultPA((ErroreEnte) responseObj), "Risposta dal servizio di verifica sconosciuto.");
			}
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_ENTE_WEB, FaultPa.PAA_ERRORE_INTERNO, "Risposta dal servizio di verifica sconosciuta.");
		} else {
			log.error("Errore nell'invocazione del servizio di verifica: [HTTP Response Code " + responseCode + "]");
			try {
				log.debug("HTTP Response message: " + new String(IOUtils.toByteArray(connection.getErrorStream())));
			} catch (IOException e) {
				log.error("Impossibile serializzare l'ErrorStream della risposta: " + e);
			}
			throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_ENTE_WEB, FaultPa.PAA_ERRORE_INTERNO, "Errore nell'invocazione del connettore di verifica (HTTP Response Code " + responseCode + ").");
		}
	}
	
	private NdpFaultCode toFaultPA(ErroreEnte erroreEnte) throws GovPayNdpException {
		if(erroreEnte.getCodiceErrore().equals("PAGAMENTO_SCONOSCIUTO"))
			return FaultPa.PAA_PAGAMENTO_SCONOSCIUTO;
		if(erroreEnte.getCodiceErrore().equals("PAGAMENTO_DUPLICATO"))
			return FaultPa.PAA_PAGAMENTO_DUPLICATO;
		if(erroreEnte.getCodiceErrore().equals("PAGAMENTO_ANNULLATO"))
			return FaultPa.PAA_PAGAMENTO_ANNULLATO;
		if(erroreEnte.getCodiceErrore().equals("PAGAMENTO_SCADUTO"))
			return FaultPa.PAA_PAGAMENTO_SCADUTO;
		throw new GovPayNdpException(GovPayExceptionEnum.ERRORE_ENTE_WEB, FaultPa.PAA_ERRORE_INTERNO, "Codifica del codice di errore sconosciuta [" + erroreEnte.getCodiceErrore() + "].");
	}
}
