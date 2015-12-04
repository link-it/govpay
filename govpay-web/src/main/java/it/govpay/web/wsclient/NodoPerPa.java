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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.JAXBElement;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.telematici.pagamenti.ws.ppthead.IntestazioneCarrelloPPT;
import gov.telematici.pagamenti.ws.ppthead.IntestazionePPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediCopiaRT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediCopiaRTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediElencoFlussiRendicontazione;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediElencoFlussiRendicontazioneRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediFlussoRendicontazione;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediFlussoRendicontazioneRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediInformativaPSP;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediInformativaPSPRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediListaPendentiRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediListaPendentiRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediStatoRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediStatoRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaCarrelloRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaCarrelloRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRichiestaStorno;
import it.gov.digitpa.schemas._2011.ws.paa.NodoInviaRichiestaStornoRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.ObjectFactory;
import it.gov.digitpa.schemas._2011.ws.paa.TipoElementoListaRPT;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Stazione;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.utils.NdpUtils;
import it.govpay.utils.SOAPUtils;

public class NodoPerPa extends BasicClient {

	private static ObjectFactory objectFactory;
	private boolean isConnettoreServizioRPTAzioneInUrl;
	private Logger log;

	public NodoPerPa(Intermediario intermediario) throws GovPayException {
		super(intermediario);
		
		if(objectFactory == null || log == null){
			objectFactory = new ObjectFactory();
			log = LogManager.getLogger();
		}
//		@SuppressWarnings("rawtypes")
//		List<Handler> handlerChain = new ArrayList<Handler>();
//		handlerChain.add( new MessageLoggingHandler());
//		handlerChain.add(new EventLoggingHandler(true));
//		Binding binding = ( ( BindingProvider )port ).getBinding();
//		binding.setHandlerChain(handlerChain);
	}
	
	public Object send(String azione, JAXBElement<?> body, Object header) throws GovPayException {
		
		// Creazione Connessione
		int responseCode;
		HttpURLConnection connection = null;
		try { 
			
			String urlString = url.toExternalForm();
			if(this.isConnettoreServizioRPTAzioneInUrl) {
				if(!urlString.endsWith("/")) urlString = urlString.concat("/");
				try {
					url = new URL(urlString.concat(azione));
				} catch (MalformedURLException e) {
					throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore nella costruzione della URL di invocazione del Nodo dei Pagamenti [url: " + urlString.concat(azione) + "]");
				}
			} 
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("SOAPAction", azione);
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
			}
			
			connection.getOutputStream().write(SOAPUtils.toMessage(body, header));
			responseCode = connection.getResponseCode();
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore nell'invocazione del Nodo dei Pagamenti.", e);
		}
		if(responseCode < 300) {
			try {
				return SOAPUtils.toJaxb(connection.getInputStream());
			} catch (Exception e) {
				log.error("Messaggio di risposta non valido: " + e);
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Impossibile acquisire la risposta del Nodo dei Pagamenti.", e);
			}
		} else {
			log.error("Errore nell'invocazione del Nodo dei Pagamenti: [HTTP Response Code " + responseCode + "]");
			try {
				if(log.getLevel().isMoreSpecificThan(Level.DEBUG));
					log.debug("HTTP Response message: " + new String(IOUtils.toByteArray(connection.getErrorStream())));
			} catch (IOException e) {
				log.error("Impossibile serializzare l'ErrorStream della risposta: " + e);
			}
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore nell'invocazione del Nodo dei Pagamenti: [HTTP Response Code " + responseCode + "]");
		}
	}

	public NodoInviaRPTRisposta nodoInviaRPT(Intermediario intermediario, Stazione stazione, Canale canale, Rpt rpt, NodoInviaRPT inviaRPT) throws GovPayException {

		IntestazionePPT intestazione = new IntestazionePPT();
		intestazione.setCodiceContestoPagamento(rpt.getCcp());
		intestazione.setIdentificativoDominio(rpt.getCodDominio());
		intestazione.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
		intestazione.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
		intestazione.setIdentificativoUnivocoVersamento(rpt.getIuv());

		NdpUtils.setThreadContextNdpParams(intestazione.getIdentificativoDominio(),
				intestazione.getIdentificativoUnivocoVersamento(), 
				intestazione.getCodiceContestoPagamento(), 
				canale.getPsp().getCodPsp(), 
				canale.getTipoVersamento().getCodifica(), 
				stazione.getCodStazione(), 
				canale.getCodCanale(), null, "nodoInviaRPT", intermediario.getDenominazione());

		Object response = send("nodoInviaRPT", objectFactory.createNodoInviaRPT(inviaRPT), intestazione);
		return (NodoInviaRPTRisposta) ((JAXBElement<?>)response).getValue();
	}

	public NodoInviaCarrelloRPTRisposta nodoInviaCarrelloRPT(Intermediario intermediario, Stazione stazione, Canale canale, NodoInviaCarrelloRPT inviaCarrelloRPT, String codCarrello) throws GovPayException {

		int size = inviaCarrelloRPT.getListaRPT().getElementoListaRPT().size();
		String[] domini = new String[size];
		String[] iuv = new String[size];
		String[] ccp = new String[size];

		for(int i = 0; i < size ; i++) {
			TipoElementoListaRPT rpt = inviaCarrelloRPT.getListaRPT().getElementoListaRPT().get(i);
			domini[i] = rpt.getIdentificativoDominio();
			iuv[i] = rpt.getIdentificativoUnivocoVersamento();
			ccp[i] = rpt.getCodiceContestoPagamento();
		}

		IntestazioneCarrelloPPT intestazione = new IntestazioneCarrelloPPT();
		intestazione.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
		intestazione.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
		intestazione.setIdentificativoCarrello(codCarrello);

		NdpUtils.setThreadContextNdpParams(StringUtils.join(domini, Evento.VALUES_SEPARATOR),
				StringUtils.join(iuv, Evento.VALUES_SEPARATOR), 
				StringUtils.join(ccp, Evento.VALUES_SEPARATOR), 
				canale.getPsp().getCodPsp(), 
				canale.getTipoVersamento().getCodifica(), 
				stazione.getCodStazione(), 
				canale.getCodCanale(), null, "nodoInviaCarrelloRPT", intermediario.getDenominazione());

		Object response = send("nodoInviaCarrelloRPT", objectFactory.createNodoInviaCarrelloRPT(inviaCarrelloRPT), intestazione);
		return (NodoInviaCarrelloRPTRisposta) ((JAXBElement<?>)response).getValue();
	}

	public NodoChiediInformativaPSPRisposta nodoChiediInformativaPSP(NodoChiediInformativaPSP nodoChiediInformativaPSP, String nomeSoggetto) throws GovPayException {
		NdpUtils.setThreadContextNdpParams(nodoChiediInformativaPSP.getIdentificativoDominio(),
				null, 
				null, 
				null, 
				null, 
				nodoChiediInformativaPSP.getIdentificativoStazioneIntermediarioPA(), 
				null, null, "nodoChiediInformativaPSP", nomeSoggetto);
		Object response = send("nodoChiediInformativaPSP", objectFactory.createNodoChiediInformativaPSP(nodoChiediInformativaPSP), null);
		return (NodoChiediInformativaPSPRisposta) ((JAXBElement<?>)response).getValue();
	}

	public NodoChiediStatoRPTRisposta nodoChiediStatoRpt(NodoChiediStatoRPT nodoChiediStatoRPT, String nomeSoggetto) throws GovPayException {
		NdpUtils.setThreadContextNdpParams(nodoChiediStatoRPT.getIdentificativoDominio(),
				nodoChiediStatoRPT.getIdentificativoUnivocoVersamento(), 
				nodoChiediStatoRPT.getCodiceContestoPagamento(), 
				null, 
				null, 
				nodoChiediStatoRPT.getIdentificativoStazioneIntermediarioPA(), 
				null, null, "nodoChiediStatoRPT", nomeSoggetto);
		Object response = send("nodoChiediStatoRPT", objectFactory.createNodoChiediStatoRPT(nodoChiediStatoRPT), null);
		return (NodoChiediStatoRPTRisposta) ((JAXBElement<?>)response).getValue();
	}

	public NodoChiediCopiaRTRisposta nodoChiediCopiaRT(NodoChiediCopiaRT nodoChiediCopiaRT, String nomeSoggetto) throws GovPayException {
		NdpUtils.setThreadContextNdpParams(nodoChiediCopiaRT.getIdentificativoDominio(),
				nodoChiediCopiaRT.getIdentificativoUnivocoVersamento(), 
				nodoChiediCopiaRT.getCodiceContestoPagamento(), 
				null, 
				null, 
				nodoChiediCopiaRT.getIdentificativoStazioneIntermediarioPA(), 
				null, null, "nodoChiediCopiaRT", nomeSoggetto);
		Object response = send("nodoChiediCopiaRT", objectFactory.createNodoChiediCopiaRT(nodoChiediCopiaRT), null);
		return (NodoChiediCopiaRTRisposta) ((JAXBElement<?>)response).getValue();
	}

	public NodoChiediListaPendentiRPTRisposta nodoChiediListaPendentiRPT(NodoChiediListaPendentiRPT nodoChiediListaPendentiRPT, String nomeSoggetto) throws GovPayException {
		NdpUtils.setThreadContextNdpParams(nodoChiediListaPendentiRPT.getIdentificativoDominio(),
				null, 
				null, 
				null, 
				null, 
				nodoChiediListaPendentiRPT.getIdentificativoStazioneIntermediarioPA(), 
				null, null, "nodoChiediListaPendentiRPT", nomeSoggetto);
		Object response = send("nodoChiediListaPendentiRPT", objectFactory.createNodoChiediListaPendentiRPT(nodoChiediListaPendentiRPT), null);
		return (NodoChiediListaPendentiRPTRisposta) ((JAXBElement<?>)response).getValue();
	}

	public NodoInviaRichiestaStornoRisposta nodoInviaRichiestaStorno(NodoInviaRichiestaStorno nodoInviaRichiestaStorno, String nomeSoggetto) throws GovPayException {
		NdpUtils.setThreadContextNdpParams(nodoInviaRichiestaStorno.getIdentificativoDominio(),
				nodoInviaRichiestaStorno.getIdentificativoUnivocoVersamento(), 
				nodoInviaRichiestaStorno.getCodiceContestoPagamento(), 
				null, 
				null, 
				nodoInviaRichiestaStorno.getIdentificativoStazioneIntermediarioPA(), 
				null, null, "nodoInviaRichiestaStorno", nomeSoggetto);
		Object response = send("nodoInviaRichiestaStorno", objectFactory.createNodoInviaRichiestaStorno(nodoInviaRichiestaStorno), null);
		return (NodoInviaRichiestaStornoRisposta) ((JAXBElement<?>)response).getValue();
	}

	public NodoChiediElencoFlussiRendicontazioneRisposta nodoChiediElencoFlussiRendicontazione(NodoChiediElencoFlussiRendicontazione nodoChiediElencoFlussiRendicontazione, String nomeSoggetto) throws GovPayException {
		NdpUtils.setThreadContextNdpParams(nodoChiediElencoFlussiRendicontazione.getIdentificativoDominio(),
				null, 
				null, 
				null, 
				null, 
				nodoChiediElencoFlussiRendicontazione.getIdentificativoStazioneIntermediarioPA(), 
				null, null, "nodoChiediElencoFlussiRendicontazione", nomeSoggetto);
		Object response = send("nodoChiediElencoFlussiRendicontazione", objectFactory.createNodoChiediElencoFlussiRendicontazione(nodoChiediElencoFlussiRendicontazione), null);
		return (NodoChiediElencoFlussiRendicontazioneRisposta) ((JAXBElement<?>)response).getValue();
	}

	public NodoChiediFlussoRendicontazioneRisposta nodoChiediFlussoRendicontazione(NodoChiediFlussoRendicontazione nodoChiediFlussoRendicontazione, String nomeSoggetto) throws GovPayException {
		NdpUtils.setThreadContextNdpParams(nodoChiediFlussoRendicontazione.getIdentificativoDominio(),
				null, 
				null, 
				null, 
				null, 
				null, 
				null, null, "nodoChiediFlussoRendicontazione", nomeSoggetto);
		Object response = send("nodoChiediFlussoRendicontazione", objectFactory.createNodoChiediFlussoRendicontazione(nodoChiediFlussoRendicontazione), null);
		return (NodoChiediFlussoRendicontazioneRisposta) ((JAXBElement<?>)response).getValue();
	}
}
