/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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

import jakarta.xml.bind.JAXBElement;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.context.core.BaseServer;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.bd.model.Dominio;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.MaggioliJPPAUtils;
import it.govpay.core.utils.client.beans.TipoConnettore;
import it.govpay.core.utils.client.beans.TipoDestinatario;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.client.exception.ClientInitializeException;
import it.govpay.core.utils.logger.MessaggioDiagnosticoCostanti;
import it.govpay.core.utils.logger.MessaggioDiagnosticoUtils;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.model.configurazione.Giornale;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.internal.CtRichiestaStandard;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.internal.CtRispostaStandard;
import it.maggioli.informatica.jcitygov.pagopa.payservice.pdp.connector.jppapdp.internal.ObjectFactory;

public class MaggioliJPPAClient extends BasicClientCORE {
	
	public enum Azione {
		maggioliInviaEsitoPagamento, maggioliVerificaPagamentoInAttesa
	}
	
	public static final String SOAP_ACTION_INVIA_ESITO_PAGAMENTO = "http://jcitygov.informatica.maggioli.it/pagopa/payservice/pdp/connector/jppapdp/internal/InviaEsitoPagamento";
	
	private boolean isAzioneInUrl;
	private static Logger log = LoggerWrapperFactory.getLogger(MaggioliJPPAClient.class);
	private static ObjectFactory objectFactory;

	public MaggioliJPPAClient(Dominio dominio, ConnettoreNotificaPagamenti connettore, String operationID, Giornale giornale, EventoContext eventoCtx) throws ClientInitializeException {
		super(dominio, TipoConnettore.MAGGIOLI_JPPA, TipoDestinatario.MAGGIOLI_JPPA, connettore, eventoCtx);
		if(objectFactory == null || log == null ){
			objectFactory = new ObjectFactory();
		}
		
		this.isAzioneInUrl = connettore.isAzioneInUrl();
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

	public CtRispostaStandard send(String azione, byte[] body) throws ClientException {
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
		
		MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_JPPAPDP_CLIENT_INVIO_RICHIESTA);

		try {
			byte[] response = super.sendSoap(azione, body, this.isAzioneInUrl);
			if(response == null) {
				throw new ClientException("Il Nodo dei Pagamenti ha ritornato un messaggio vuoto.");
			}
			JAXBElement<?> jaxbElement = MaggioliJPPAUtils.toJaxbJPPAPdPInternal(response, null);
			CtRispostaStandard r = (CtRispostaStandard) jaxbElement.getValue();
			MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_JPPAPDP_CLIENT_INVIO_RICHIESTA_OK);
			return r;
		} catch (ClientException e) {
			MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_JPPAPDP_CLIENT_INVIO_RICHIESTA_KO, e.getMessage());
			throw e;
		} catch (Exception e) {
			MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_JPPAPDP_CLIENT_INVIO_RICHIESTA_KO, "Errore interno");
			throw new ClientException("Messaggio di risposta dal Servizio Maggioli JPPA non valido", e);
		}
	}
	
	public CtRispostaStandard maggioliJPPAInviaEsitoPagamentoRichiesta(CtRichiestaStandard richiestaStandard) throws ClientException {
		byte [] body = MaggioliJPPAUtils.getBody(true, objectFactory.createInviaEsitoPagamentoRichiesta(richiestaStandard), null);
		this.setTipoEventoCustom(Azione.maggioliInviaEsitoPagamento.toString());
		return this.send(SOAP_ACTION_INVIA_ESITO_PAGAMENTO, body);
	}
	
}
