/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.client;

import java.io.ByteArrayOutputStream;

import jakarta.xml.bind.JAXBElement;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.context.core.BaseServer;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import gov.telematici.pagamenti.ws.rpt.NodoChiediElencoFlussiRendicontazione;
import gov.telematici.pagamenti.ws.rpt.NodoChiediElencoFlussiRendicontazioneRisposta;
import gov.telematici.pagamenti.ws.rpt.NodoChiediFlussoRendicontazione;
import gov.telematici.pagamenti.ws.rpt.NodoChiediFlussoRendicontazioneRisposta;
import gov.telematici.pagamenti.ws.rpt.NodoInviaCarrelloRPT;
import gov.telematici.pagamenti.ws.rpt.NodoInviaCarrelloRPTRisposta;
import gov.telematici.pagamenti.ws.rpt.NodoInviaRPT;
import gov.telematici.pagamenti.ws.rpt.NodoInviaRPTRisposta;
import gov.telematici.pagamenti.ws.rpt.ObjectFactory;
import gov.telematici.pagamenti.ws.rpt.Risposta;
import gov.telematici.pagamenti.ws.rpt.ppthead.IntestazioneCarrelloPPT;
import gov.telematici.pagamenti.ws.rpt.ppthead.IntestazionePPT;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.client.beans.TipoOperazioneNodo;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.client.exception.ClientInitializeException;
import it.govpay.model.Intermediario;
import it.govpay.model.Rpt;
import it.govpay.model.Stazione;
import it.govpay.model.configurazione.Giornale;
import it.govpay.pagopa.beans.utils.JaxbUtils;

public class NodoClient extends BasicClientCORE {

	private static ObjectFactory objectFactory;
	private boolean isAzioneInUrl;
	private static Logger log = LoggerWrapperFactory.getLogger(NodoClient.class);
	
	public NodoClient(Intermediario intermediario, String operationID, Giornale giornale, EventoContext eventoCtx) throws ClientInitializeException {
		super(intermediario, TipoOperazioneNodo.NODO, intermediario.getConnettorePdd(), eventoCtx);
		if(objectFactory == null || log == null ){
			objectFactory = new ObjectFactory();
		}
		this.isAzioneInUrl = intermediario.getConnettorePdd().isAzioneInUrl();
		this.operationID = operationID;
		this.setGiornale(giornale);
	}
	
	@Override
	public String getOperationId() {
		return this.operationID;
	}
	
	public void setOperationId(String operationID) {
		this.operationID = operationID;
	}

	public Risposta send(String azione, byte[] body) throws ClientException, UtilsException {
		String urlString = this.url.toExternalForm();
		if(this.isAzioneInUrl) {
			if(!urlString.endsWith("/")) {
				urlString = urlString.concat("/");
			}
		} 
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		
		if(operationID != null) {
			BaseServer serverByOperationId = appContext.getServerByOperationId(this.operationID);
			if(serverByOperationId != null) {
				serverByOperationId.setEndpoint(urlString);
			}
		} else 
			appContext.getTransaction().getLastServer().setEndpoint(urlString);
		
		ctx.getApplicationLogger().log("ndp_client.invioRichiesta");

		try {
			byte[] response = super.sendSoap(azione, body, this.isAzioneInUrl);
			if(response == null) {
				throw new ClientException("Il Nodo dei Pagamenti ha ritornato un messaggio vuoto.");
			}
			JAXBElement<?> jaxbElement = SOAPUtils.toJaxbRPT(response, null);
			Risposta r = (Risposta) jaxbElement.getValue();
			if(r.getFault() != null) {
				String faultCode = r.getFault().getFaultCode() != null ? r.getFault().getFaultCode() : "<Fault Code vuoto>";
				String faultString = r.getFault().getFaultString() != null ? r.getFault().getFaultString() : "<Fault String vuoto>";
				String faultDescription = r.getFault().getDescription() != null ? r.getFault().getDescription() : "<Fault Description vuoto>";
				ctx.getApplicationLogger().log("ndp_client.invioRichiestaFault", faultCode, faultString, faultDescription);
			} else {
				ctx.getApplicationLogger().log("ndp_client.invioRichiestaOk");
			}
			return r;
		} catch (ClientException e) {
			ctx.getApplicationLogger().log("ndp_client.invioRichiestaKo", e.getMessage());
			throw e;
		} catch (Exception e) {
			ctx.getApplicationLogger().log("ndp_client.invioRichiestaKo", "Errore interno");
			throw new ClientException("Messaggio di risposta dal Nodo dei Pagamenti non valido", e);
		}

	}

	public NodoInviaRPTRisposta nodoInviaRPT(Intermediario intermediario, Stazione stazione, Rpt rpt, NodoInviaRPT inviaRPT) throws ClientException, UtilsException {
		IntestazionePPT intestazione = new IntestazionePPT();
		intestazione.setCodiceContestoPagamento(rpt.getCcp());
		intestazione.setIdentificativoDominio(rpt.getCodDominio());
		intestazione.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
		intestazione.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
		intestazione.setIdentificativoUnivocoVersamento(rpt.getIuv());

		byte [] body = this.getBody(true,objectFactory.createNodoInviaRPT(inviaRPT), intestazione);
		Risposta response = send(EventoContext.Azione.NODOINVIARPT.toString(), body);
		return (NodoInviaRPTRisposta) response;
	}

	public NodoInviaCarrelloRPTRisposta nodoInviaCarrelloRPT(Intermediario intermediario, Stazione stazione, NodoInviaCarrelloRPT inviaCarrelloRPT, String codCarrello) throws ClientException, UtilsException {
		IntestazioneCarrelloPPT intestazione = new IntestazioneCarrelloPPT();
		intestazione.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
		intestazione.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
		intestazione.setIdentificativoCarrello(codCarrello);
		byte [] body = this.getBody(true, objectFactory.createNodoInviaCarrelloRPT(inviaCarrelloRPT), intestazione);
		Risposta response = this.send(EventoContext.Azione.NODOINVIACARRELLORPT.toString(), body);
		return (NodoInviaCarrelloRPTRisposta) response;
	}

	public NodoChiediElencoFlussiRendicontazioneRisposta nodoChiediElencoFlussiRendicontazione(NodoChiediElencoFlussiRendicontazione nodoChiediElencoFlussiRendicontazione, String nomeSoggetto) throws ClientException, UtilsException {
		byte [] body = this.getBody(true,objectFactory.createNodoChiediElencoFlussiRendicontazione(nodoChiediElencoFlussiRendicontazione), null);
		Risposta response = this.send(EventoContext.Azione.NODOCHIEDIELENCOFLUSSIRENDICONTAZIONE.toString(), body);
		return (NodoChiediElencoFlussiRendicontazioneRisposta) response;
	}

	public NodoChiediFlussoRendicontazioneRisposta nodoChiediFlussoRendicontazione(NodoChiediFlussoRendicontazione nodoChiediFlussoRendicontazione, String nomeSoggetto) throws ClientException, UtilsException {
		byte [] body = this.getBody(true, objectFactory.createNodoChiediFlussoRendicontazione(nodoChiediFlussoRendicontazione), null);
		Risposta response = this.send(EventoContext.Azione.NODOCHIEDIFLUSSORENDICONTAZIONE.toString(), body);
		return (NodoChiediFlussoRendicontazioneRisposta) response;
	}

	public byte[] getBody(boolean soap, JAXBElement<?> body, Object header) throws ClientException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			if(soap) {
				SOAPUtils.writeRPTMessage(body, header, baos);
			} else {
				JaxbUtils.marshalRptService(body, baos);
			}
		}catch(Exception e) {
			throw new ClientException(e);
		}

		return baos.toByteArray();
	}

}
