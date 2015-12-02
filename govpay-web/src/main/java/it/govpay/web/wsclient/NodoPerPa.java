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

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.apache.commons.lang.StringUtils;
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
import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirpt.PagamentiTelematiciRPT;
import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirpt.PagamentiTelematiciRPTservice;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Stazione;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.utils.NdpUtils;
import it.govpay.web.handler.EventLoggingHandler;
import it.govpay.web.handler.MessageLoggingHandler;

public class NodoPerPa extends BasicClient {

	private static ObjectFactory objectFactory;
	private static PagamentiTelematiciRPTservice service;
	private boolean isConnettoreServizioRPTAzioneInUrl;
	private Logger log;
	private PagamentiTelematiciRPT port;

	public NodoPerPa(Intermediario intermediario) throws GovPayException {
		super(intermediario);
		
		if(service == null || objectFactory == null || log == null){
			objectFactory = new ObjectFactory();
			log = LogManager.getLogger();
			service = new PagamentiTelematiciRPTservice();
		}
		
		this.isConnettoreServizioRPTAzioneInUrl = intermediario.getConnettorePdd().isAzioneInUrl();
		port = service.getPagamentiTelematiciRPTPort();

		if(ishttpBasicEnabled) {
			((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, httpBasicUser);
			((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, httpBasicPassword);
		} 

		if(isSslEnabled){
			((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory", sslContext.getSocketFactory() );
		}
		
		@SuppressWarnings("rawtypes")
		List<Handler> handlerChain = new ArrayList<Handler>();
		handlerChain.add( new MessageLoggingHandler());
		handlerChain.add(new EventLoggingHandler(true));
		Binding binding = ( ( BindingProvider )port ).getBinding();
		binding.setHandlerChain(handlerChain);
	}

	public PagamentiTelematiciRPT configuraClient(String azione) throws GovPayException {
		String urlString = url.toExternalForm();
		if(this.isConnettoreServizioRPTAzioneInUrl) {
			if(!urlString.endsWith("/")) urlString = urlString.concat("/");
			((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString.concat(azione));
		} else {
			((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);
		}
		return port;
		
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

		PagamentiTelematiciRPT port = configuraClient("nodoInviaRPT");
		NodoInviaRPTRisposta nodoInviaRisposta;
		try {
			nodoInviaRisposta = port.nodoInviaRPT(inviaRPT, intestazione);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, "Riscontrata eccezione nell'invocazione del servizio PagamentiTelematiciRPT", e);
		}
		return nodoInviaRisposta;
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

		PagamentiTelematiciRPT port = configuraClient("nodoInviaCarrelloRPT");
		NodoInviaCarrelloRPTRisposta nodoInviaCarrelloRPTRisposta;
		try {
			nodoInviaCarrelloRPTRisposta = port.nodoInviaCarrelloRPT(inviaCarrelloRPT, intestazione);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, "Riscontrata eccezione nell'invocazione del servizio PagamentiTelematiciRPT", e);
		}
		return nodoInviaCarrelloRPTRisposta;
	}

	public NodoChiediInformativaPSPRisposta nodoChiediInformativaPSP(NodoChiediInformativaPSP nodoChiediInformativaPSP, String nomeSoggetto) throws GovPayException {
		NdpUtils.setThreadContextNdpParams(nodoChiediInformativaPSP.getIdentificativoDominio(),
				null, 
				null, 
				null, 
				null, 
				nodoChiediInformativaPSP.getIdentificativoStazioneIntermediarioPA(), 
				null, null, "nodoChiediInformativaPSP", nomeSoggetto);
		PagamentiTelematiciRPT port = configuraClient("nodoChiediInformativaPSP");
		NodoChiediInformativaPSPRisposta nodoChiediInformativaPSPRisposta;
		try {
			nodoChiediInformativaPSPRisposta = port.nodoChiediInformativaPSP(nodoChiediInformativaPSP);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, "Riscontrata eccezione nell'invocazione del servizio PagamentiTelematiciRPT", e);
		}
		return nodoChiediInformativaPSPRisposta;
	}

	public NodoChiediStatoRPTRisposta nodoChiediStatoRpt(NodoChiediStatoRPT nodoChiediStatoRPT, String nomeSoggetto) throws GovPayException {
		NdpUtils.setThreadContextNdpParams(nodoChiediStatoRPT.getIdentificativoDominio(),
				nodoChiediStatoRPT.getIdentificativoUnivocoVersamento(), 
				nodoChiediStatoRPT.getCodiceContestoPagamento(), 
				null, 
				null, 
				nodoChiediStatoRPT.getIdentificativoStazioneIntermediarioPA(), 
				null, null, "nodoChiediStatoRPT", nomeSoggetto);
		PagamentiTelematiciRPT port = configuraClient("nodoChiediStatoRPT");
		NodoChiediStatoRPTRisposta nodoChiediStatoRPTRisposta;
		try {
			nodoChiediStatoRPTRisposta = port.nodoChiediStatoRPT(nodoChiediStatoRPT);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, e.getMessage(), e);
		}
		return nodoChiediStatoRPTRisposta;
	}

	public NodoChiediCopiaRTRisposta nodoChiediCopiaRT(NodoChiediCopiaRT nodoChiediCopiaRT, String nomeSoggetto) throws GovPayException {
		NdpUtils.setThreadContextNdpParams(nodoChiediCopiaRT.getIdentificativoDominio(),
				nodoChiediCopiaRT.getIdentificativoUnivocoVersamento(), 
				nodoChiediCopiaRT.getCodiceContestoPagamento(), 
				null, 
				null, 
				nodoChiediCopiaRT.getIdentificativoStazioneIntermediarioPA(), 
				null, null, "nodoChiediCopiaRT", nomeSoggetto);
		PagamentiTelematiciRPT port = configuraClient("nodoChiediCopiaRT");
		NodoChiediCopiaRTRisposta nodoChiediCopiaRTRisposta;
		try {
			nodoChiediCopiaRTRisposta = port.nodoChiediCopiaRT(nodoChiediCopiaRT);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, "Riscontrata eccezione nell'invocazione del servizio PagamentiTelematiciRPT", e);
		}
		return nodoChiediCopiaRTRisposta;
	}

	public NodoChiediListaPendentiRPTRisposta nodoChiediListaPendentiRPT(NodoChiediListaPendentiRPT nodoChiediListaPendentiRPT, String nomeSoggetto) throws GovPayException {
		NdpUtils.setThreadContextNdpParams(nodoChiediListaPendentiRPT.getIdentificativoDominio(),
				null, 
				null, 
				null, 
				null, 
				nodoChiediListaPendentiRPT.getIdentificativoStazioneIntermediarioPA(), 
				null, null, "nodoChiediListaPendentiRPT", nomeSoggetto);
		PagamentiTelematiciRPT port = configuraClient("nodoChiediListaPendentiRPT");
		NodoChiediListaPendentiRPTRisposta nodoChiediListaPendentiRPTRisposta;
		try {
			nodoChiediListaPendentiRPTRisposta = port.nodoChiediListaPendentiRPT(nodoChiediListaPendentiRPT);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, "Riscontrata eccezione nell'invocazione del servizio PagamentiTelematiciRPT", e);
		}
		return nodoChiediListaPendentiRPTRisposta;
	}

	public NodoInviaRichiestaStornoRisposta nodoInviaRichiestaStorno(NodoInviaRichiestaStorno nodoInviaRichiestaStorno, String nomeSoggetto) throws GovPayException {
		NdpUtils.setThreadContextNdpParams(nodoInviaRichiestaStorno.getIdentificativoDominio(),
				nodoInviaRichiestaStorno.getIdentificativoUnivocoVersamento(), 
				nodoInviaRichiestaStorno.getCodiceContestoPagamento(), 
				null, 
				null, 
				nodoInviaRichiestaStorno.getIdentificativoStazioneIntermediarioPA(), 
				null, null, "nodoInviaRichiestaStorno", nomeSoggetto);
		PagamentiTelematiciRPT port = configuraClient("nodoInviaRichiestaStorno");
		NodoInviaRichiestaStornoRisposta nodoInviaRichiestaStornoRisposta;
		try {
			nodoInviaRichiestaStornoRisposta = port.nodoInviaRichiestaStorno(nodoInviaRichiestaStorno);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, "Riscontrata eccezione nell'invocazione del servizio PagamentiTelematiciRPT", e);
		}
		return nodoInviaRichiestaStornoRisposta;
	}

	public NodoChiediElencoFlussiRendicontazioneRisposta nodoChiediElencoFlussiRendicontazione(NodoChiediElencoFlussiRendicontazione nodoChiediElencoFlussiRendicontazione, String nomeSoggetto) throws GovPayException {
		NdpUtils.setThreadContextNdpParams(nodoChiediElencoFlussiRendicontazione.getIdentificativoDominio(),
				null, 
				null, 
				null, 
				null, 
				nodoChiediElencoFlussiRendicontazione.getIdentificativoStazioneIntermediarioPA(), 
				null, null, "nodoChiediElencoFlussiRendicontazione", nomeSoggetto);
		PagamentiTelematiciRPT port = configuraClient("nodoChiediElencoFlussiRendicontazione");
		NodoChiediElencoFlussiRendicontazioneRisposta nodoChiediElencoFlussiRendicontazioneRisposta;
		try {
			nodoChiediElencoFlussiRendicontazioneRisposta = port.nodoChiediElencoFlussiRendicontazione(nodoChiediElencoFlussiRendicontazione);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, "Riscontrata eccezione nell'invocazione del servizio PagamentiTelematiciRPT", e);
		}
		return nodoChiediElencoFlussiRendicontazioneRisposta;
	}

	public NodoChiediFlussoRendicontazioneRisposta nodoChiediFlussoRendicontazione(NodoChiediFlussoRendicontazione nodoChiediFlussoRendicontazione, String nomeSoggetto) throws GovPayException {
		NdpUtils.setThreadContextNdpParams(nodoChiediFlussoRendicontazione.getIdentificativoDominio(),
				null, 
				null, 
				null, 
				null, 
				null, 
				null, null, "nodoChiediFlussoRendicontazione", nomeSoggetto);
		PagamentiTelematiciRPT port = configuraClient("nodoChiediFlussoRendicontazione");
		NodoChiediFlussoRendicontazioneRisposta nodoChiediFlussoRendicontazioneRisposta;
		try {
			nodoChiediFlussoRendicontazioneRisposta = port.nodoChiediFlussoRendicontazione(nodoChiediFlussoRendicontazione);
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP_WEB, "Riscontrata eccezione nell'invocazione del servizio PagamentiTelematiciRPT", e);
		}
		return nodoChiediFlussoRendicontazioneRisposta;
	}
}
