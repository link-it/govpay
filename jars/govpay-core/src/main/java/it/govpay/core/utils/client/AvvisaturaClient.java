package it.govpay.core.utils.client;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import gov.telematici.pagamenti.ws.CtNodoInviaAvvisoDigitale;
import gov.telematici.pagamenti.ws.CtNodoInviaAvvisoDigitaleRisposta;
import gov.telematici.pagamenti.ws.CtRisposta;
import gov.telematici.pagamenti.ws.ObjectFactory;
import gov.telematici.pagamenti.ws.sachead.IntestazionePPT;
import it.govpay.bd.BasicBD;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpThreadLocal;
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
	private String azione, dominio, stazione, errore, faultCode;
	private BasicBD bd;
	private ObjectFactory objectFactory = null;

	public AvvisaturaClient(Intermediario intermediario, BasicBD bd) throws ClientException {
		super(intermediario);
		this.bd = bd;
		if(objectFactory == null || log == null ){
			objectFactory = new ObjectFactory();
		}
		this.isAzioneInUrl = intermediario.getConnettorePdd().isAzioneInUrl();
	}
	
	public CtNodoInviaAvvisoDigitaleRisposta nodoInviaAvvisoDigitale(Intermediario intermediario, Stazione stazione, CtNodoInviaAvvisoDigitale ctNodoInviaAvvisoDigitale) throws GovPayException, ClientException{
		
		JAXBElement<?> body = new JAXBElement<CtNodoInviaAvvisoDigitale>(_NodoInviaAvvisoDigitale_QNAME, CtNodoInviaAvvisoDigitale.class, null, ctNodoInviaAvvisoDigitale);
		
		IntestazionePPT intestazione = new IntestazionePPT();
		intestazione.setIdentificativoDominio(ctNodoInviaAvvisoDigitale.getAvvisoDigitaleWS().getIdentificativoDominio());
		intestazione.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
		intestazione.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
		
		CtRisposta response = send(Azione.nodoInviaAvvisoDigitale.toString(), body, intestazione);
		return (CtNodoInviaAvvisoDigitaleRisposta) response;
	}
	
	public CtRisposta send(String azione, JAXBElement<?> body, Object header) throws GovPayException, ClientException {
		this.azione = azione;
		String urlString = this.url.toExternalForm();
		if(this.isAzioneInUrl) {
			if(!urlString.endsWith("/")) urlString = urlString.concat("/");
		} 
		GpThreadLocal.get().getTransaction().getServer().setEndpoint(urlString);
		GpThreadLocal.get().log("ndp_client.invioRichiesta");
		
		try {
			byte[] response = super.sendSoap(azione, body, header, this.isAzioneInUrl);
			if(response == null) {
				throw new ClientException("Il Nodo dei Pagamenti ha ritornato un messaggio vuoto.");
			}
			JAXBElement<?> jaxbElement = SOAPUtils.toJaxb(response, null);
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
}
