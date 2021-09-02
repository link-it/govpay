package it.govpay.core.utils.client;

import javax.xml.bind.JAXBElement;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.context.core.BaseServer;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Dominio;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.MaggioliJPPAUtils;
import it.govpay.core.utils.EventoContext.Componente;
import it.govpay.core.utils.client.beans.TipoConnettore;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.internal.CtRichiestaStandard;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.internal.CtRispostaStandard;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.internal.ObjectFactory;

public class MaggioliJPPAClient extends BasicClientCORE {
	
	public enum Azione {
		InviaEsitoPagamento, VerificaPagamentoInAttesa
	}
	
	private boolean isAzioneInUrl;
	private static Logger log = LoggerWrapperFactory.getLogger(MaggioliJPPAClient.class);
	private String faultCode;
	private static ObjectFactory objectFactory;

	public MaggioliJPPAClient(Dominio dominio, ConnettoreNotificaPagamenti connettore, String operationID, Giornale giornale) throws ClientException {
		super(dominio, TipoConnettore.MAGGIOLI_JPPA, connettore);
		if(objectFactory == null || log == null ){
			objectFactory = new ObjectFactory();
		}
		
		this.isAzioneInUrl = connettore.isAzioneInUrl();
		this.operationID = operationID;
		this.componente = Componente.API_MAGGIOLI_JPPA;
		this.setGiornale(giornale);
		
		this.getEventoCtx().setComponente(this.componente);
	}
	
	@Override
	public String getOperationId() {
		return this.operationID;
	}
	
	public void setOperationId(String operationID) {
		this.operationID = operationID;
	}

	public CtRispostaStandard send(String azione, byte[] body) throws GovPayException, ClientException, UtilsException {
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
		
		ctx.getApplicationLogger().log("jppapdp_client.invioRichiesta");

		try {
			byte[] response = super.sendSoap(azione, body, this.isAzioneInUrl);
			if(response == null) {
				throw new ClientException("Il Nodo dei Pagamenti ha ritornato un messaggio vuoto.");
			}
			JAXBElement<?> jaxbElement = MaggioliJPPAUtils.toJaxbJPPAPdPInternal(response, null);
			CtRispostaStandard r = (CtRispostaStandard) jaxbElement.getValue();
//			if(r.getFault() != null) {
//				this.faultCode = r.getFault().getFaultCode() != null ? r.getFault().getFaultCode() : "<Fault Code vuoto>";
//				String faultString = r.getFault().getFaultString() != null ? r.getFault().getFaultString() : "<Fault String vuoto>";
//				String faultDescription = r.getFault().getDescription() != null ? r.getFault().getDescription() : "<Fault Description vuoto>";
//				ctx.getApplicationLogger().log("jppapdp_client.invioRichiestaFault", this.faultCode, faultString, faultDescription);
//			} else {
			ctx.getApplicationLogger().log("jppapdp_client.invioRichiestaOk");
//			}
			return r;
		} catch (ClientException e) {
			ctx.getApplicationLogger().log("jppapdp_client.invioRichiestaKo", e.getMessage());
			throw e;
		} catch (Exception e) {
			ctx.getApplicationLogger().log("jppapdp_client.invioRichiestaKo", "Errore interno");
			throw new ClientException("Messaggio di risposta dal Servizio Maggioli JPPA non valido", e);
		}
	}
	
	public CtRispostaStandard maggioliJPPAInviaEsitoPagamentoRichiesta(CtRichiestaStandard richiestaStandard) throws GovPayException, ClientException, UtilsException {
		byte [] body = MaggioliJPPAUtils.getBody(true, objectFactory.createInviaEsitoPagamentoRichiesta(richiestaStandard), null);
		return this.send(Azione.InviaEsitoPagamento.toString(), body);
	}
	
	
}
