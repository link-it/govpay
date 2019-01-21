package it.govpay.core.utils.client;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import gov.telematici.pagamenti.ws.avvisi_digitali.CtNodoInviaAvvisoDigitale;
import gov.telematici.pagamenti.ws.avvisi_digitali.CtNodoInviaAvvisoDigitaleRisposta;
import gov.telematici.pagamenti.ws.avvisi_digitali.CtRisposta;
import gov.telematici.pagamenti.ws.avvisi_digitali.ObjectFactory;
import gov.telematici.pagamenti.ws.ppthead.richiesta_avvisi.IntestazionePPT;
import it.govpay.bd.BasicBD;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpThreadLocal;
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

	public AvvisaturaClient(Intermediario intermediario, BasicBD bd) throws ClientException {
		super(intermediario, TipoOperazioneNodo.AVVISATURA);
		if(objectFactory == null || log == null ){
			objectFactory = new ObjectFactory();
		}
		this.isAzioneInUrl = intermediario.getConnettorePdd().isAzioneInUrl();
	}

	public CtNodoInviaAvvisoDigitaleRisposta nodoInviaAvvisoDigitale(Intermediario intermediario, Stazione stazione, CtNodoInviaAvvisoDigitale ctNodoInviaAvvisoDigitale) throws GovPayException, ClientException{
		byte[] body = getByteRichiesta(intermediario, stazione, ctNodoInviaAvvisoDigitale);
		CtRisposta response = send(Azione.nodoInviaAvvisoDigitale.toString(), body);
		return (CtNodoInviaAvvisoDigitaleRisposta) response;
	}

	public byte[] getByteRichiesta(Intermediario intermediario, Stazione stazione,
			CtNodoInviaAvvisoDigitale ctNodoInviaAvvisoDigitale) throws ClientException {
		JAXBElement<?> bodyJAXB = new JAXBElement<CtNodoInviaAvvisoDigitale>(_NodoInviaAvvisoDigitale_QNAME, CtNodoInviaAvvisoDigitale.class, null, ctNodoInviaAvvisoDigitale);

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

	public CtRisposta send(String azione, byte[] body) throws GovPayException, ClientException {
		String urlString = this.url.toExternalForm();
		if(this.isAzioneInUrl) {
			if(!urlString.endsWith("/")) urlString = urlString.concat("/");
		} 
		GpThreadLocal.get().getTransaction().getServer().setEndpoint(urlString);
		GpThreadLocal.get().log("ndp_client.invioRichiesta");

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
				GpThreadLocal.get().log("ndp_client.invioRichiestaFault", this.faultCode, faultString, faultDescription);
			} else {
				GpThreadLocal.get().log("ndp_client.invioRichiestaOk");
			}
			return r;
		} catch (ClientException e) {
			this.errore = "Errore rete: " + e.getMessage();
			GpThreadLocal.get().log("ndp_client.invioRichiestaKo", e.getMessage());
			throw e;
		} catch (Exception e) {
			this.errore = "Errore interno: " + e.getMessage();
			GpThreadLocal.get().log("ndp_client.invioRichiestaKo", "Errore interno");
			throw new ClientException("Messaggio di risposta dal Nodo dei Pagamenti non valido", e);
		} finally {
		}
	}
	
	public String getErrore() {
		return errore;
	}
}
