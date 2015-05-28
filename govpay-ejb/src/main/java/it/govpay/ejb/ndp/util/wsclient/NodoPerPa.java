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
package it.govpay.ejb.ndp.util.wsclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.io.IOUtils;
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
import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirpt.PagamentiTelematiciRPT;
import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirpt.PagamentiTelematiciRPTservice;
import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.core.utils.rs.client.BasicClient;
import it.govpay.ejb.ndp.model.IntermediarioModel;
import it.govpay.ejb.ndp.util.NdpUtils;

public class NodoPerPa extends BasicClient {

	private static PagamentiTelematiciRPTservice service;
	private static ObjectFactory objectFactory;
	
	private boolean isConnettoreServizioRPTAzioneInUrl;
    private Logger log;
    
    public static void init() {
		service = new PagamentiTelematiciRPTservice();
		objectFactory = new ObjectFactory();
    }
	
	public NodoPerPa(IntermediarioModel intermediarioEnte, Logger log) throws GovPayException {
		super(intermediarioEnte.getIdIntermediarioPA(), intermediarioEnte.getConnettoreServizioRPT());
		this.log = log;
		this.isConnettoreServizioRPTAzioneInUrl = intermediarioEnte.getConnettoreServizioRPT().isAzioneInUrl();
	}
	
	public PagamentiTelematiciRPT configuraClient(String azione) {
		PagamentiTelematiciRPT port = service.getPagamentiTelematiciRPTPort();
		
		if(ishttpBasicEnabled) {
			((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, httpBasicUser);
			((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, httpBasicPassword);
		} 
		
		if(isSslEnabled){
			((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory", sslContext.getSocketFactory() );
		}
		
		String urlString = url.toExternalForm();
		if(this.isConnettoreServizioRPTAzioneInUrl) {
			if(!urlString.endsWith("/")) urlString = urlString.concat("/");
			((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString.concat(azione));
		} else {
			((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);
		}
		return port;
	}

	@SuppressWarnings("unchecked")
	public NodoInviaRPTRisposta nodoInviaRPT(Holder<Map<String,List<String>>> responseHeaders, NodoInviaRPT inviaRPT, IntestazionePPT intestazione) throws GovPayException {
		PagamentiTelematiciRPT port = configuraClient("nodoInviaRPT");
		
		dump(intestazione, objectFactory.createNodoInviaRPT(inviaRPT));
		NodoInviaRPTRisposta nodoInviaRisposta;
		try {
			nodoInviaRisposta = port.nodoInviaRPT(inviaRPT, intestazione);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, e);
		}
		dump(intestazione,  objectFactory.createNodoInviaRPTRisposta(nodoInviaRisposta));
		Map<String, Object> ctx = ((BindingProvider) port).getResponseContext();
		responseHeaders.value = (Map<String,List<String>>) ctx.get(MessageContext.HTTP_RESPONSE_HEADERS);
		return nodoInviaRisposta;
	}
	
	@SuppressWarnings("unchecked")
	public NodoInviaCarrelloRPTRisposta nodoInviaCarrelloRPT(Holder<Map<String,List<String>>> responseHeaders, NodoInviaCarrelloRPT inviaCarrelloRPT, IntestazioneCarrelloPPT intestazione) throws GovPayException {
		PagamentiTelematiciRPT port = configuraClient("nodoInviaCarrelloRPT");
		dump(intestazione, objectFactory.createNodoInviaCarrelloRPT(inviaCarrelloRPT));
		NodoInviaCarrelloRPTRisposta nodoInviaCarrelloRPTRisposta;
		try {
			nodoInviaCarrelloRPTRisposta = port.nodoInviaCarrelloRPT(inviaCarrelloRPT, intestazione);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, e);
		}
		dump(intestazione,  objectFactory.createNodoInviaCarrelloRPTRisposta(nodoInviaCarrelloRPTRisposta));
		Map<String, Object> ctx = ((BindingProvider) port).getResponseContext();
		responseHeaders.value = (Map<String,List<String>>) ctx.get(MessageContext.HTTP_RESPONSE_HEADERS);
		return nodoInviaCarrelloRPTRisposta;
	}
	
	public NodoChiediInformativaPSPRisposta nodoChiediInformativaPSP(NodoChiediInformativaPSP nodoChiediInformativaPSP) throws GovPayException {
		PagamentiTelematiciRPT port = configuraClient("nodoChiediInformativaPSP");
		NodoChiediInformativaPSPRisposta nodoChiediInformativaPSPRisposta;
		try {
			nodoChiediInformativaPSPRisposta = port.nodoChiediInformativaPSP(nodoChiediInformativaPSP);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, e);
		}
		dump(objectFactory.createNodoChiediInformativaPSPRisposta(nodoChiediInformativaPSPRisposta));
		return nodoChiediInformativaPSPRisposta;
	}
	
	public NodoChiediStatoRPTRisposta nodoChiediStatoRpt(NodoChiediStatoRPT nodoChiediStatoRPT) throws GovPayException {
		PagamentiTelematiciRPT port = configuraClient("nodoChiediStatoRPT");
		dump(objectFactory.createNodoChiediStatoRPT(nodoChiediStatoRPT));
		NodoChiediStatoRPTRisposta nodoChiediStatoRPTRisposta;
		try {
			nodoChiediStatoRPTRisposta = port.nodoChiediStatoRPT(nodoChiediStatoRPT);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, e.getMessage(), e);
		}
		dump(objectFactory.createNodoChiediStatoRPTRisposta(nodoChiediStatoRPTRisposta));
		return nodoChiediStatoRPTRisposta;
	}
	
	@SuppressWarnings("unchecked")
	public NodoChiediCopiaRTRisposta nodoChiediCopiaRT(Holder<Map<String,List<String>>> responseHeaders, NodoChiediCopiaRT nodoChiediCopiaRT) throws GovPayException {
		PagamentiTelematiciRPT port = configuraClient("nodoChiediCopiaRT");
		dump(objectFactory.createNodoChiediCopiaRT(nodoChiediCopiaRT));
		NodoChiediCopiaRTRisposta nodoChiediCopiaRTRisposta;
		try {
			nodoChiediCopiaRTRisposta = port.nodoChiediCopiaRT(nodoChiediCopiaRT);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, e);
		}
		dump(objectFactory.createNodoChiediCopiaRTRisposta(nodoChiediCopiaRTRisposta));
		Map<String, Object> ctx = ((BindingProvider) port).getResponseContext();
		responseHeaders.value = (Map<String,List<String>>) ctx.get(MessageContext.HTTP_RESPONSE_HEADERS);
		return nodoChiediCopiaRTRisposta;
	}
	
	public NodoChiediListaPendentiRPTRisposta nodoChiediListaPendentiRPT(NodoChiediListaPendentiRPT nodoChiediListaPendentiRPT) throws GovPayException {
		PagamentiTelematiciRPT port = configuraClient("nodoChiediCopiaRT");
		dump(objectFactory.createNodoChiediListaPendentiRPT(nodoChiediListaPendentiRPT));
		NodoChiediListaPendentiRPTRisposta nodoChiediListaPendentiRPTRisposta;
		try {
			nodoChiediListaPendentiRPTRisposta = port.nodoChiediListaPendentiRPT(nodoChiediListaPendentiRPT);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, e);
		}
		dump(objectFactory.createNodoChiediListaPendentiRPTRisposta(nodoChiediListaPendentiRPTRisposta));
		return nodoChiediListaPendentiRPTRisposta;
	}
	
	@SuppressWarnings("unchecked")
	public NodoInviaRichiestaStornoRisposta nodoInviaRichiestaStorno(Holder<Map<String,List<String>>> responseHeaders, NodoInviaRichiestaStorno nodoInviaRichiestaStorno) throws GovPayException {
		PagamentiTelematiciRPT port = configuraClient("nodoInviaRichiestaStorno");
		dump(objectFactory.createNodoInviaRichiestaStorno(nodoInviaRichiestaStorno));
		NodoInviaRichiestaStornoRisposta nodoInviaRichiestaStornoRisposta;
		try {
			nodoInviaRichiestaStornoRisposta = port.nodoInviaRichiestaStorno(nodoInviaRichiestaStorno);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, e);
		}
		dump(objectFactory.createNodoInviaRichiestaStornoRisposta(nodoInviaRichiestaStornoRisposta));
		Map<String, Object> ctx = ((BindingProvider) port).getResponseContext();
		responseHeaders.value = (Map<String,List<String>>) ctx.get(MessageContext.HTTP_RESPONSE_HEADERS);
		return nodoInviaRichiestaStornoRisposta;
	}
	
	public NodoChiediElencoFlussiRendicontazioneRisposta nodoChiediElencoFlussiRendicontazione(NodoChiediElencoFlussiRendicontazione nodoChiediElencoFlussiRendicontazione) throws GovPayException {
		PagamentiTelematiciRPT port = configuraClient("nodoChiediElencoFlussiRendicontazione");
		dump(objectFactory.createNodoChiediElencoFlussiRendicontazione(nodoChiediElencoFlussiRendicontazione));
		NodoChiediElencoFlussiRendicontazioneRisposta nodoChiediElencoFlussiRendicontazioneRisposta;
		try {
			nodoChiediElencoFlussiRendicontazioneRisposta = port.nodoChiediElencoFlussiRendicontazione(nodoChiediElencoFlussiRendicontazione);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, e);
		}
		dump(objectFactory.createNodoChiediElencoFlussiRendicontazioneRisposta(nodoChiediElencoFlussiRendicontazioneRisposta));
		return nodoChiediElencoFlussiRendicontazioneRisposta;
	}
	
	public NodoChiediFlussoRendicontazioneRisposta nodoChiediFlussoRendicontazione(NodoChiediFlussoRendicontazione nodoChiediFlussoRendicontazione) throws GovPayException {
		PagamentiTelematiciRPT port = configuraClient("nodoChiediFlussoRendicontazione");
		dump(objectFactory.createNodoChiediFlussoRendicontazione(nodoChiediFlussoRendicontazione));
		NodoChiediFlussoRendicontazioneRisposta nodoChiediFlussoRendicontazioneRisposta;
		try {
			nodoChiediFlussoRendicontazioneRisposta = port.nodoChiediFlussoRendicontazione(nodoChiediFlussoRendicontazione);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, e);
		}
		dump(objectFactory.createNodoChiediFlussoRendicontazioneRisposta(nodoChiediFlussoRendicontazioneRisposta));
		return nodoChiediFlussoRendicontazioneRisposta;
	}
	
	private void dump(IntestazioneCarrelloPPT intestazione, JAXBElement<?> jaxb) {
		StringBuffer content = new StringBuffer(); 
		content.append("Messaggio di " + jaxb.getDeclaredType());
		content.append("\n\tidentificativoIntermediarioPA" + intestazione.getIdentificativoIntermediarioPA());
		content.append("\n\tidentificativoStazioneIntermediarioPA" + intestazione.getIdentificativoStazioneIntermediarioPA());
		content.append("\n\tidentificativoUnivocoVersamento" + intestazione.getIdentificativoCarrello());
	}

	private void dump(IntestazionePPT intestazione, JAXBElement<?> jaxb) {
		StringBuffer content = new StringBuffer(); 
		content.append("Messaggio di " + jaxb.getDeclaredType());
		content.append("\n\tidentificativoDominio: " + intestazione.getIdentificativoDominio());
		content.append("\n\tidentificativoUnivocoVersamento: " + intestazione.getIdentificativoUnivocoVersamento());
		content.append("\n\tcodiceContestoPagamento: " + intestazione.getCodiceContestoPagamento());
		content.append("\n\tidentificativoIntermediarioPA: " + intestazione.getIdentificativoIntermediarioPA());
		content.append("\n\tidentificativoStazioneIntermediarioPA: " + intestazione.getIdentificativoStazioneIntermediarioPA());
	}
	
	private void dump(JAXBElement<?> jaxb) {
		StringBuffer content = new StringBuffer(); 
		content.append("Messaggio di " + jaxb.getDeclaredType());
		dump(content, jaxb);
	}
	
	private void dump(StringBuffer content, JAXBElement<?> jaxb) {
		if(jaxb.getValue() instanceof NodoInviaRPT) {
			NodoInviaRPT obj = ((NodoInviaRPT) jaxb.getValue());
			content.append("\n\tidentificativoCanale: " + obj.getIdentificativoCanale());
			content.append("\n\tidentificativoIntermediarioPSP: " + obj.getIdentificativoIntermediarioPSP());
			content.append("\n\tidentificativoPSP: " + obj.getIdentificativoPSP());
			content.append("\n\ttipoFirma: " + obj.getTipoFirma());
			content.append("\n\t"+new String(obj.getRpt()));
			log.debug(content); 
			return;
		}
		
		if(jaxb.getValue() instanceof NodoInviaCarrelloRPT) {
			NodoInviaCarrelloRPT obj = ((NodoInviaCarrelloRPT) jaxb.getValue());
			content.append("\n\tidentificativoCanale: " + obj.getIdentificativoCanale());
			content.append("\n\tidentificativoIntermediarioPSP: " + obj.getIdentificativoIntermediarioPSP());
			content.append("\n\tidentificativoPSP: " + obj.getIdentificativoPSP());
			for(TipoElementoListaRPT ele : obj.getListaRPT().getElementoListaRPT()) {
				content.append("\n\tidentificativoDominio: " + ele.getIdentificativoDominio());
				content.append("\n\tidentificativoUnivocoVersamento: " + ele.getIdentificativoUnivocoVersamento());
				content.append("\n\ttipoFirma: " + ele.getTipoFirma());
				content.append("\n\t"+new String(ele.getRpt()));
			}
			log.debug(content); 
			return;
		}
		
		if(jaxb.getValue() instanceof NodoChiediInformativaPSPRisposta) {
			NodoChiediInformativaPSPRisposta obj = ((NodoChiediInformativaPSPRisposta) jaxb.getValue());
			try {
				if(obj.getXmlInformativa() != null)
					content.append(IOUtils.toString(obj.getXmlInformativa().getInputStream()));
				else
					log.warn("Informativa PSP vuota");
			} catch (IOException e) {
				log.warn("Impossibile serializzare l'Informativa PSP: " + e);
			}
			log.debug(content); 
			return;
		}
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			NdpUtils.marshal(jaxb, baos);
			content.append("Contenuto XML: \n" + baos.toString());
		} catch (Exception e) {
			log.error("Impossibile serializzare il messaggio: " + e);
		}
		
		log.debug(content); 
	}
}
