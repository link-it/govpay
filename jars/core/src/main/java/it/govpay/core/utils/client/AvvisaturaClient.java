package it.govpay.core.utils.client;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.context.core.BaseServer;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import gov.telematici.pagamenti.ws.avvisi_digitali.CtNodoInviaAvvisoDigitale;
import gov.telematici.pagamenti.ws.avvisi_digitali.CtNodoInviaAvvisoDigitaleRisposta;
import gov.telematici.pagamenti.ws.avvisi_digitali.CtRisposta;
import gov.telematici.pagamenti.ws.avvisi_digitali.ObjectFactory;
import gov.telematici.pagamenti.ws.ppthead.richiesta_avvisi.IntestazionePPT;
import it.govpay.bd.BasicBD;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.eventi.DatiPagoPA;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.EventoContext.Componente;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.model.Intermediario;
import it.govpay.model.Stazione;

public class AvvisaturaClient extends BasicClient {

	public final static QName _NodoInviaAvvisoDigitale_QNAME = new QName("http://ws.pagamenti.telematici.gov/", "nodoInviaAvvisoDigitale");
	public final static QName _NodoInviaAvvisoDigitaleRisposta_QNAME = new QName("http://ws.pagamenti.telematici.gov/", "nodoInviaAvvisoDigitaleRisposta");


	public enum Azione {
		nodoInviaAvvisoDigitale
	}

	private boolean isAzioneInUrl;
	private static Logger log = LoggerWrapperFactory.getLogger(AvvisaturaClient.class);
	private String errore, faultCode;
	private ObjectFactory objectFactory = null;
	private Intermediario intermediario = null;
	private Stazione stazione = null;
//	private Versamento versamento = null;

	public AvvisaturaClient(Versamento versamento, Intermediario intermediario, Stazione stazione, Giornale giornale, String operationID, BasicBD bd) throws ClientException, ServiceException {
		super(intermediario, TipoOperazioneNodo.AVVISATURA);
		if(objectFactory == null || log == null ){
			objectFactory = new ObjectFactory();
		}
		this.isAzioneInUrl = intermediario.getConnettorePdd().isAzioneInUrl();
		this.operationID = operationID;
		this.componente = Componente.API_PAGOPA;
		this.setGiornale(giornale);
		this.stazione = stazione;
		this.intermediario = intermediario;
//		this.versamento = versamento;
		
		this.getEventoCtx().setComponente(this.componente); 
		
		DatiPagoPA datiPagoPA = new DatiPagoPA();
		datiPagoPA.setCodStazione(this.stazione.getCodStazione());
		datiPagoPA.setErogatore(Evento.NDP);
		datiPagoPA.setFruitore(this.intermediario.getCodIntermediario());
		datiPagoPA.setCodIntermediario(this.intermediario.getCodIntermediario());
		this.getEventoCtx().setDatiPagoPA(datiPagoPA);
	}

	@Override
	public String getOperationId() {
		return this.operationID;
	}

	public CtNodoInviaAvvisoDigitaleRisposta nodoInviaAvvisoDigitale(Intermediario intermediario, Stazione stazione, CtNodoInviaAvvisoDigitale ctNodoInviaAvvisoDigitale) throws GovPayException, ClientException, UtilsException{
		byte[] body = getByteRichiesta(intermediario, stazione, ctNodoInviaAvvisoDigitale);
		CtRisposta response = send(Azione.nodoInviaAvvisoDigitale.toString(), body);
		return (CtNodoInviaAvvisoDigitaleRisposta) response;
	}

	private byte[] getByteRichiesta(Intermediario intermediario, Stazione stazione,
			CtNodoInviaAvvisoDigitale ctNodoInviaAvvisoDigitale) throws ClientException {
		JAXBElement<?> bodyJAXB = new JAXBElement<CtNodoInviaAvvisoDigitale>(_NodoInviaAvvisoDigitale_QNAME, CtNodoInviaAvvisoDigitale.class, null, ctNodoInviaAvvisoDigitale);

		this.getEventoCtx().getDatiPagoPA().setCodDominio(ctNodoInviaAvvisoDigitale.getAvvisoDigitaleWS().getIdentificativoDominio()); 
		
		IntestazionePPT intestazione = new IntestazionePPT();
		intestazione.setIdentificativoDominio(ctNodoInviaAvvisoDigitale.getAvvisoDigitaleWS().getIdentificativoDominio());
		intestazione.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
		intestazione.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());

		byte [] body = this.getBody(true, bodyJAXB, intestazione);
		return body;
	}

	public byte[] getByteRisposta(CtRisposta ctRisposta) throws ClientException {
		JAXBElement<?> bodyJAXB = new JAXBElement<CtRisposta>(_NodoInviaAvvisoDigitaleRisposta_QNAME, CtRisposta.class, null, ctRisposta);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			JaxbUtils.marshalAvvisaturaDigitaleService(bodyJAXB, baos);
		}catch(Exception e) {
			throw new ClientException(e);
		}
		byte [] body = baos.toByteArray();
		return body;
	}

	public byte[] getBody(boolean soap, JAXBElement<?> body, Object header) throws ClientException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			if(soap) {
				SOAPUtils.writeAvvisaturaDigitaleMessage(body, header, baos);
			} else {
				JaxbUtils.marshalAvvisaturaDigitaleService(body, baos);
			}
		}catch(Exception e) {
			throw new ClientException(e);
		}

		return baos.toByteArray();
	}

	public CtRisposta send(String azione, byte[] body) throws GovPayException, ClientException, UtilsException {
		String urlString = this.url.toExternalForm();
		if(this.isAzioneInUrl) {
			if(!urlString.endsWith("/")) urlString = urlString.concat("/");
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
			JAXBElement<?> jaxbElement = SOAPUtils.toJaxbAvvisaturaDigitale(response, null);
			CtRisposta r = (CtRisposta) jaxbElement.getValue();
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
		}
	}

	public String getErrore() {
		return errore;
	}
}
