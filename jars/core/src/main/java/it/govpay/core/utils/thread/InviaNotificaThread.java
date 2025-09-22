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
package it.govpay.core.utils.thread;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.beans.EventoContext.Esito;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.EventoUtils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.LogUtils;
import it.govpay.core.utils.client.INotificaClient;
import it.govpay.core.utils.client.NotificaClient;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.client.exception.ClientInitializeException;
import it.govpay.core.utils.logger.MessaggioDiagnosticoCostanti;
import it.govpay.core.utils.logger.MessaggioDiagnosticoUtils;
import it.govpay.model.Connettore;
import it.govpay.model.Notifica.StatoSpedizione;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Versionabile.Versione;
import it.govpay.model.configurazione.Giornale;

public class InviaNotificaThread implements Runnable {

	public static final String CONNETTORE_NOTIFICA_DISABILITATO = "Connettore Notifica non configurato";
	public static final String PA_NOTIFICA_TRANSAZIONE = "paNotificaTransazione";
	private static Logger log = LoggerWrapperFactory.getLogger(InviaNotificaThread.class);
	private Notifica notifica;
	private Versamento versamento;
	private Rpt rpt;
	private Dominio dominio = null;
	private boolean completed = false;
	private boolean errore = false;
	private Applicazione applicazione = null;
	private Connettore connettoreNotifica = null;
	private IContext ctx = null;
	private Giornale giornale = null;
	private String rptKey = null;
	private List<Pagamento> pagamenti  = null;
	private PagamentoPortale pagamentoPortale = null;

	public InviaNotificaThread(Notifica notifica, IContext ctx) throws ServiceException, IOException {
		// Verifico che tutti i campi siano valorizzati
		this.ctx = ctx;
		BDConfigWrapper configWrapper = new BDConfigWrapper(this.ctx.getTransactionId(), true);
		this.notifica = notifica;
		this.rpt = this.notifica.getRpt() != null ? this.notifica.getRpt() : this.notifica.getRpt(configWrapper);
		this.applicazione = this.notifica.getApplicazione(configWrapper);
		this.versamento = this.rpt.getVersamento();
		this.dominio = this.versamento.getDominio(configWrapper);
		this.connettoreNotifica = this.applicazione.getConnettoreIntegrazione();
		this.pagamenti = this.rpt.getPagamenti(configWrapper);
		
		this.giornale = new it.govpay.core.business.Configurazione().getConfigurazione().getGiornale();
		this.rptKey = this.notifica.getRptKey();
		this.pagamentoPortale = this.rpt.getPagamentoPortale() != null ? this.rpt.getPagamentoPortale() : this.rpt.getPagamentoPortale(configWrapper);;
	}

	@Override
	public void run() {
		ContextThreadLocal.set(this.ctx);
		
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		MDC.put(MD5Constants.TRANSACTION_ID, ctx.getTransactionId());
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		TipoNotifica tipoNotifica = this.notifica.getTipo();
		INotificaClient client = null;
		EventoContext eventoCtx = new EventoContext(Componente.API_ENTE);
		try {
			String url = this.connettoreNotifica!= null ? this.connettoreNotifica.getUrl() : GpContext.NOT_SET;
			Versione versione = this.connettoreNotifica != null ? this.connettoreNotifica.getVersione() : Versione.GP_REST_01;
			String operationId = appContext.setupPaClient(applicazione.getCodApplicazione(), PA_NOTIFICA_TRANSAZIONE, url, versione);
			
			if(this.rpt.getCodCarrello() != null) {
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_CARRELLO, rpt.getCodCarrello()));
			} 
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_COD_DOMINIO, this.dominio.getCodDominio()));
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_IUV, this.rpt.getIuv()));
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_CCP, this.rpt.getCcp()));
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property(MessaggioDiagnosticoCostanti.PROPERTY_TIPO_NOTIFICA, tipoNotifica.name().toLowerCase()));
			
			switch (tipoNotifica) {
			case ATTIVAZIONE:
				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_RPT);
				break;
			case ANNULLAMENTO:
				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_ANNULLAMENTO_RPT);
				break;
			case FALLIMENTO:
				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_FALLIMENTO_RPT);
				break;
			case RICEVUTA:
				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_RT);
				break;
			}
			 
			LogUtils.logInfo(log, "Spedizione della notifica di "+tipoNotifica.name().toLowerCase()+" pagamento della transazione [" + this.rptKey +"] all'applicazione [CodApplicazione: " + this.applicazione.getCodApplicazione() + "]");
			if(connettoreNotifica == null || connettoreNotifica.getUrl() == null) {
				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_ANNULLATA);
				LogUtils.logInfo(log, "Connettore Notifica non configurato per l'applicazione [CodApplicazione: " + applicazione.getCodApplicazione() + "]. Spedizione inibita.");
				NotificheBD notificheBD = new NotificheBD(configWrapper);
				long tentativi = this.notifica.getTentativiSpedizione() + 1;
				Date prossima = new GregorianCalendar(9999,1,1).getTime();
				notificheBD.updateAnnullata(this.notifica.getId(), "Connettore Notifica non configurato, notifica annullata.", tentativi, prossima);
				return;
			}
			
			MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_SPEDIZIONE);
			
			// salvataggio id Rpt/ versamento/ pagamento
			eventoCtx.setCodDominio(this.rpt.getCodDominio());
			eventoCtx.setIuv(this.rpt.getIuv());
			eventoCtx.setCcp(this.rpt.getCcp());
			eventoCtx.setIdA2A(this.applicazione.getCodApplicazione());
			eventoCtx.setIdPendenza(this.versamento.getCodVersamentoEnte());
			if(this.pagamentoPortale != null)
				eventoCtx.setIdPagamento(this.pagamentoPortale.getIdSessione());
			
			client = new NotificaClient(this.applicazione, this.rpt, this.versamento, this.pagamenti, operationId, this.giornale, eventoCtx);
			
			client.invoke(this.notifica);
			
			this.notifica.setStato(StatoSpedizione.SPEDITO);
			this.notifica.setDescrizioneStato(null);
			this.notifica.setDataAggiornamento(new Date());

			NotificheBD notificheBD = new NotificheBD(configWrapper);
			notificheBD.updateSpedito(this.notifica.getId());
			
			switch (tipoNotifica) {
			case ATTIVAZIONE:
				if(rpt.getCodCarrello() != null) {
					MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_CARRELLOOK);
				} else {
					MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_RPTOK);
				}
				break;
			case ANNULLAMENTO:
				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_ANNULLAMENTO_RPTOK);
				break;
			case FALLIMENTO:
				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_FALLIMENTO_RPTOK);
				break;
			case RICEVUTA:
				MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_RTOK);
				break;
			}
			 
			eventoCtx.setEsito(Esito.OK);
			LogUtils.logInfo(log, "Notifica consegnata con successo");
		} catch(Exception e) {
			errore = true;
			if(e instanceof GovPayException || e instanceof ClientException)
				LogUtils.logWarn(log, "Errore nella consegna della notifica: " + e.getMessage());
			else if(e instanceof ClientInitializeException)
				LogUtils.logError(log, "Errore nella creazione del client per la consegna della notifica: " + e.getMessage(), e);
			else
				LogUtils.logError(log, "Errore nella consegna della notifica", e);
			
			if(eventoCtx != null) {
				if(e instanceof GovPayException govPayException) {
					eventoCtx.setSottotipoEsito(govPayException.getCodEsito().toString());
				} else if(e instanceof ClientException clientException) {
					eventoCtx.setSottotipoEsito(clientException.getResponseCode() + "");
				} else {
					eventoCtx.setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
				}
				
				eventoCtx.setEsito(Esito.FAIL);
				eventoCtx.setDescrizioneEsito(e.getMessage());
				eventoCtx.setException(e);
			}			
			try {
				long tentativi = this.notifica.getTentativiSpedizione() + 1;
				NotificheBD notificheBD = new NotificheBD(configWrapper);
				
				Date today = new Date();
				Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
				Date prossima = new Date(today.getTime() + (tentativi * tentativi * 60 * 1000));
				
				// Limito la rispedizione al giorno dopo.
				if(prossima.after(tomorrow)) prossima = tomorrow;
				
				if(tentativi == 1 || !e.getMessage().equals(this.notifica.getDescrizioneStato())) {
					switch (tipoNotifica) {
					case ATTIVAZIONE:
						if(rpt.getCodCarrello() != null) {
							MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_CARRELLOKO, e.getMessage());
						} else {
							MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_RPTKO, e.getMessage());
						}
						break;
					case ANNULLAMENTO:
						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_ANNULLAMENTO_RPTKO, e.getMessage());
						break;
					case FALLIMENTO:
						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_FALLIMENTO_RPTKO, e.getMessage());
						break;
					case RICEVUTA:
						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_RTKO, e.getMessage());
						break;
					}
				} else {
					switch (tipoNotifica) {
					case ATTIVAZIONE:
						if(rpt.getCodCarrello() != null) {
							MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_CARRELLO_RETRYKO, e.getMessage(), prossima.toString());
						} else {
							MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_RPT_RETRYKO, e.getMessage(), prossima.toString());
						}
						break;
					case ANNULLAMENTO:
						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_ANNULLAMENTO_RPT_RETRYKO, e.getMessage(), prossima.toString());
						break;
					case FALLIMENTO:
						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_FALLIMENTO_RPT_RETRYKO, e.getMessage(), prossima.toString());
						break;
					case RICEVUTA:
						MessaggioDiagnosticoUtils.logMessaggioDiagnostico(log, ctx, MessaggioDiagnosticoCostanti.MSG_DIAGNOSTICO_NOTIFICA_RT_RETRYKO, e.getMessage(), prossima.toString());
						break;
					}
				}
				
				notificheBD.updateDaSpedire(this.notifica.getId(), e.getMessage(), tentativi, prossima);
				
			} catch (Exception ee) {
				// Andato male l'aggiornamento. Non importa, verra' rispedito.
			}
		} finally {
			if(eventoCtx != null && eventoCtx.isRegistraEvento()) {
				EventiBD eventiBD = new EventiBD(configWrapper);
				try {
					eventiBD.insertEvento(EventoUtils.toEventoDTO(eventoCtx,log));
				} catch (ServiceException e) {
					LogUtils.logError(log, "Errore durante il salvataggio dell'evento: ", e);
				}
			}
			
			this.completed = true;
			ContextThreadLocal.unset();
		}
	}

	public boolean isCompleted() {
		return this.completed;
	}
	
	public boolean isErrore() {
		return this.errore;
	}
}
