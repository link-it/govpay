/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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

import javax.xml.bind.JAXBElement;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.context.core.BaseServer;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import gov.telematici.pagamenti.ws.rpt.NodoChiediCopiaRT;
import gov.telematici.pagamenti.ws.rpt.NodoChiediCopiaRTRisposta;
import gov.telematici.pagamenti.ws.rpt.NodoChiediElencoFlussiRendicontazione;
import gov.telematici.pagamenti.ws.rpt.NodoChiediElencoFlussiRendicontazioneRisposta;
import gov.telematici.pagamenti.ws.rpt.NodoChiediFlussoRendicontazione;
import gov.telematici.pagamenti.ws.rpt.NodoChiediFlussoRendicontazioneRisposta;
import gov.telematici.pagamenti.ws.rpt.NodoChiediListaPendentiRPT;
import gov.telematici.pagamenti.ws.rpt.NodoChiediListaPendentiRPTRisposta;
import gov.telematici.pagamenti.ws.rpt.NodoChiediStatoRPT;
import gov.telematici.pagamenti.ws.rpt.NodoChiediStatoRPTRisposta;
import gov.telematici.pagamenti.ws.rpt.NodoInviaCarrelloRPT;
import gov.telematici.pagamenti.ws.rpt.NodoInviaCarrelloRPTRisposta;
import gov.telematici.pagamenti.ws.rpt.NodoInviaRPT;
import gov.telematici.pagamenti.ws.rpt.NodoInviaRPTRisposta;
import gov.telematici.pagamenti.ws.rpt.NodoInviaRichiestaStorno;
import gov.telematici.pagamenti.ws.rpt.NodoInviaRichiestaStornoRisposta;
import gov.telematici.pagamenti.ws.rpt.ObjectFactory;
import gov.telematici.pagamenti.ws.rpt.Risposta;
import gov.telematici.pagamenti.ws.rpt.ppthead.IntestazioneCarrelloPPT;
import gov.telematici.pagamenti.ws.rpt.ppthead.IntestazionePPT;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.model.Dominio;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.model.Intermediario;
import it.govpay.model.Rpt;
import it.govpay.model.Stazione;

public class NodoClient extends BasicClient {



	public enum Azione {
		nodoInviaRPT, nodoInviaCarrelloRPT, nodoChiediStatoRPT, nodoChiediCopiaRT, nodoChiediListaPendentiRPT, nodoInviaRichiestaStorno, nodoChiediElencoFlussiRendicontazione, nodoChiediFlussoRendicontazione
	}

	private static ObjectFactory objectFactory;
	private boolean isAzioneInUrl;
	private static Logger log = LoggerWrapperFactory.getLogger(NodoClient.class);
	private String azione, dominio, stazione, errore, faultCode;
	private BasicBD bd;
	
	public NodoClient(Intermediario intermediario, String operationID, BasicBD bd) throws ClientException {
		super(intermediario, TipoOperazioneNodo.NODO);
		this.bd = bd;
		if(objectFactory == null || log == null ){
			objectFactory = new ObjectFactory();
		}
		this.isAzioneInUrl = intermediario.getConnettorePdd().isAzioneInUrl();
		this.operationID = operationID;
	}
	
	@Override
	public String getOperationId() {
		return this.operationID;
	}
	
	public void setOperationId(String operationID) {
		this.operationID = operationID;
	}

	public Risposta send(String azione, byte[] body) throws GovPayException, ClientException, UtilsException {
		this.azione = azione;
		String urlString = this.url.toExternalForm();
		if(this.isAzioneInUrl) {
			if(!urlString.endsWith("/")) urlString = urlString.concat("/");
		} 
		IContext ctx = GpThreadLocal.get();
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
				this.faultCode = r.getFault().getFaultCode() != null ? r.getFault().getFaultCode() : "<Fault Code vuoto>";
				String faultString = r.getFault().getFaultString() != null ? r.getFault().getFaultString() : "<Fault String vuoto>";
				String faultDescription = r.getFault().getDescription() != null ? r.getFault().getDescription() : "<Fault Description vuoto>";
				this.errore = "Errore applicativo " + this.faultCode + ": " + faultString;
				ctx.getApplicationLogger().log("ndp_client.invioRichiestaFault", this.faultCode, faultString, faultDescription);
			} else {
				ctx.getApplicationLogger().log("ndp_client.invioRichiestaOk");
			}
			return r;
		} catch (ClientException e) {
			this.errore = "Errore rete: " + e.getMessage();
			ctx.getApplicationLogger().log("ndp_client.invioRichiestaKo", e.getMessage());
			throw e;
		} catch (Exception e) {
			this.errore = "Errore interno: " + e.getMessage();
			ctx.getApplicationLogger().log("ndp_client.invioRichiestaKo", "Errore interno");
			throw new ClientException("Messaggio di risposta dal Nodo dei Pagamenti non valido", e);
		} finally {
			this.updateStato();
		}

	}

	public NodoInviaRPTRisposta nodoInviaRPT(Intermediario intermediario, Stazione stazione, Rpt rpt, NodoInviaRPT inviaRPT) throws GovPayException, ClientException, UtilsException {
		this.stazione = stazione.getCodStazione();
		this.dominio = rpt.getCodDominio();

		IntestazionePPT intestazione = new IntestazionePPT();
		intestazione.setCodiceContestoPagamento(rpt.getCcp());
		intestazione.setIdentificativoDominio(rpt.getCodDominio());
		intestazione.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
		intestazione.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
		intestazione.setIdentificativoUnivocoVersamento(rpt.getIuv());

		byte [] body = this.getBody(true,objectFactory.createNodoInviaRPT(inviaRPT), intestazione);
		Risposta response = send(Azione.nodoInviaRPT.toString(), body);
		return (NodoInviaRPTRisposta) response;
	}

	public NodoInviaCarrelloRPTRisposta nodoInviaCarrelloRPT(Intermediario intermediario, Stazione stazione, NodoInviaCarrelloRPT inviaCarrelloRPT, String codCarrello) throws GovPayException, ClientException, UtilsException {
		IntestazioneCarrelloPPT intestazione = new IntestazioneCarrelloPPT();
		intestazione.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
		intestazione.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
		intestazione.setIdentificativoCarrello(codCarrello);
		byte [] body = this.getBody(true, objectFactory.createNodoInviaCarrelloRPT(inviaCarrelloRPT), intestazione);
		Risposta response = this.send(Azione.nodoInviaCarrelloRPT.toString(), body);
		return (NodoInviaCarrelloRPTRisposta) response;
	}

	public NodoChiediStatoRPTRisposta nodoChiediStatoRpt(NodoChiediStatoRPT nodoChiediStatoRPT, String nomeSoggetto) throws GovPayException, ClientException, UtilsException {
		this.stazione = nodoChiediStatoRPT.getIdentificativoStazioneIntermediarioPA();
		this.dominio = nodoChiediStatoRPT.getIdentificativoDominio();
		byte [] body = this.getBody(true,objectFactory.createNodoChiediStatoRPT(nodoChiediStatoRPT), null);
		Risposta response = this.send(Azione.nodoChiediStatoRPT.toString(), body);
		return (NodoChiediStatoRPTRisposta) response;
	}

	public NodoChiediCopiaRTRisposta nodoChiediCopiaRT(NodoChiediCopiaRT nodoChiediCopiaRT, String nomeSoggetto) throws GovPayException, ClientException, UtilsException {
		this.stazione = nodoChiediCopiaRT.getIdentificativoStazioneIntermediarioPA();
		this.dominio = nodoChiediCopiaRT.getIdentificativoDominio();
		byte [] body = this.getBody(true,objectFactory.createNodoChiediCopiaRT(nodoChiediCopiaRT), null);
		Risposta response = this.send(Azione.nodoChiediCopiaRT.toString(), body);
		return (NodoChiediCopiaRTRisposta) response;
	}

	public NodoChiediListaPendentiRPTRisposta nodoChiediListaPendentiRPT(NodoChiediListaPendentiRPT nodoChiediListaPendentiRPT, String nomeSoggetto) throws GovPayException, ClientException, UtilsException {
		this.stazione = nodoChiediListaPendentiRPT.getIdentificativoStazioneIntermediarioPA();
		this.dominio = nodoChiediListaPendentiRPT.getIdentificativoDominio();
		byte [] body = this.getBody(true,objectFactory.createNodoChiediListaPendentiRPT(nodoChiediListaPendentiRPT), null);
		Risposta response = this.send(Azione.nodoChiediListaPendentiRPT.toString(), body);
		return (NodoChiediListaPendentiRPTRisposta) response;
	}

	public NodoInviaRichiestaStornoRisposta nodoInviaRichiestaStorno(NodoInviaRichiestaStorno nodoInviaRichiestaStorno) throws GovPayException, ClientException, UtilsException {
		this.stazione = nodoInviaRichiestaStorno.getIdentificativoStazioneIntermediarioPA();
		this.dominio = nodoInviaRichiestaStorno.getIdentificativoDominio();
		byte [] body = this.getBody(true,objectFactory.createNodoInviaRichiestaStorno(nodoInviaRichiestaStorno), null);
		Risposta response = this.send(Azione.nodoInviaRichiestaStorno.toString(), body);
		return (NodoInviaRichiestaStornoRisposta) response;
	}

	public NodoChiediElencoFlussiRendicontazioneRisposta nodoChiediElencoFlussiRendicontazione(NodoChiediElencoFlussiRendicontazione nodoChiediElencoFlussiRendicontazione, String nomeSoggetto) throws GovPayException, ClientException, UtilsException {
		this.stazione = nodoChiediElencoFlussiRendicontazione.getIdentificativoStazioneIntermediarioPA();
		this.dominio = nodoChiediElencoFlussiRendicontazione.getIdentificativoDominio();
		byte [] body = this.getBody(true,objectFactory.createNodoChiediElencoFlussiRendicontazione(nodoChiediElencoFlussiRendicontazione), null);
		Risposta response = this.send(Azione.nodoChiediElencoFlussiRendicontazione.toString(), body);
		return (NodoChiediElencoFlussiRendicontazioneRisposta) response;
	}

	public NodoChiediFlussoRendicontazioneRisposta nodoChiediFlussoRendicontazione(NodoChiediFlussoRendicontazione nodoChiediFlussoRendicontazione, String nomeSoggetto) throws GovPayException, ClientException, UtilsException {
		this.stazione = nodoChiediFlussoRendicontazione.getIdentificativoStazioneIntermediarioPA();
		this.dominio = nodoChiediFlussoRendicontazione.getIdentificativoDominio();
		byte [] body = this.getBody(true, objectFactory.createNodoChiediFlussoRendicontazione(nodoChiediFlussoRendicontazione), null);
		Risposta response = this.send(Azione.nodoChiediFlussoRendicontazione.toString(), body);
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

	private void updateStato() {
		boolean wasClosed = false;
		boolean wasNull = false;
		try {

			if(this.bd == null) {
				wasNull = true;
				this.bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			} else {
				wasClosed = this.bd.isClosed();
				if(wasClosed) this.bd.setupConnection("--");
			}

			if(this.dominio != null) {
				DominiBD dominiBD = new DominiBD(this.bd);
				Dominio dominio = AnagraficaManager.getDominio(this.bd, this.dominio);
				if(this.faultCode == null || !this.faultCode.startsWith("PPT")) {
					dominiBD.setStatoNdp(dominio.getId(), 0, this.azione, null);
				} else {
					dominiBD.setStatoNdp(dominio.getId(), 1, this.azione, this.errore);
				}
			}

			if(this.stazione != null) {
				StazioniBD stazioniBD = new StazioniBD(this.bd);
				Stazione stazione = AnagraficaManager.getStazione(this.bd, this.stazione);
				if(this.faultCode == null || !this.faultCode.startsWith("PPT")) {
					stazioniBD.setStatoNdp(stazione.getId(), 0, this.azione, null);
				} else if(this.dominio == null) { // Aggiorno in errore solo se e' un'operazione di stazione
					stazioniBD.setStatoNdp(stazione.getId(), 1, this.azione, this.errore);
				}
			}
		} catch (Exception e ) {
			LoggerWrapperFactory.getLogger(NodoClient.class).error("Fallito aggiornamento dello stato ndp Dominio/Stazione", e);
		} finally {
			if(wasNull || wasClosed) this.bd.closeConnection();
		}

	}
}
