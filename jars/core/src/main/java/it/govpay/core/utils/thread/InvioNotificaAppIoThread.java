package it.govpay.core.utils.thread;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.BasicBD;
import it.govpay.bd.configurazione.model.AppIO;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Configurazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Versamento;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.appio.AppIOUtils;
import it.govpay.core.utils.appio.impl.ApiException;
import it.govpay.core.utils.appio.model.LimitedProfile;
import it.govpay.core.utils.appio.model.MessageCreated;
import it.govpay.core.utils.appio.model.NewMessage;
import it.govpay.core.utils.client.AppIoClient;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.model.TipoVersamento;

public class InvioNotificaAppIoThread implements Runnable{
	
	public static final String SWAGGER_OPERATION_GET_PROFILE = "getProfile";
	public static final String SWAGGER_OPERATION_POST_MESSAGE = "submitMessageforUserWithFiscalCodeInBody";

	private IContext ctx = null;
	private Giornale giornale = null;
	private AppIO appIo = null; 
	private static Logger log = LoggerWrapperFactory.getLogger(InvioNotificaAppIoThread.class);
	private Versamento versamento;
	private Dominio dominio = null;
	private boolean completed = false;
	private TipoVersamento tipoVersamento= null;
	private TipoVersamentoDominio tipoVersamentoDominio = null;
	private Applicazione applicazione = null;

	public InvioNotificaAppIoThread(Versamento versamento, BasicBD bd, IContext ctx) throws ServiceException {
		this.ctx = ctx;
		Configurazione configurazione = new it.govpay.core.business.Configurazione(bd).getConfigurazione();
		this.giornale = configurazione.getGiornale();
		this.appIo = configurazione.getAppIo();
		this.versamento = versamento;
		this.dominio = versamento.getDominio(bd);
		this.tipoVersamento = versamento.getTipoVersamento(bd);
		this.tipoVersamentoDominio = versamento.getTipoVersamentoDominio(bd);
		this.applicazione = versamento.getApplicazione(bd);
	}


	@Override
	public void run() {
		ContextThreadLocal.set(this.ctx);
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		MDC.put(MD5Constants.TRANSACTION_ID, ctx.getTransactionId());
		BasicBD bd = null;
		AppIoClient clientGetProfile = null;
		AppIoClient clientPostMessage = null;
		boolean postMessage = false;
		try {
			String url = this.appIo.getUrl(); // Base url del servizio
			try {
				String operationId = appContext.setupAppIOClient(SWAGGER_OPERATION_GET_PROFILE, url);
				
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", this.dominio.getCodDominio()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codTipoVersamento", this.tipoVersamento.getCodTipoVersamento()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("idA2A", this.applicazione.getCodApplicazione()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("idPendenza", this.versamento.getCodVersamentoEnte()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("iuv", this.versamento.getIuvVersamento()));
				
				log.info("Lettura Profilo del Debitore "+ this.versamento.getAnagraficaDebitore().getCodUnivoco()+"per la Pendenza [Id: "+this.versamento.getCodVersamentoEnte()+", IdA2A: " + this.applicazione.getCodApplicazione() + "]");
				
				clientGetProfile = new AppIoClient(SWAGGER_OPERATION_GET_PROFILE, this.appIo, operationId, this.giornale);

				clientGetProfile.getEventoCtx().setCodDominio(this.dominio.getCodDominio());
				clientGetProfile.getEventoCtx().setIdA2A(this.applicazione.getCodApplicazione());
				clientGetProfile.getEventoCtx().setIdPendenza(this.versamento.getCodVersamentoEnte());
				clientGetProfile.getEventoCtx().setIuv(this.versamento.getIuvVersamento());
				
				LimitedProfile profile = clientGetProfile.getProfile(this.versamento.getAnagraficaDebitore().getCodUnivoco(), this.tipoVersamentoDominio.getAppIOAPIKey(), SWAGGER_OPERATION_GET_PROFILE);
							
				if(profile.isSenderAllowed()) { // spedizione abilitata procedo
					postMessage = true;
				} else { // termino il processo indicando che la notifica non verra' spedita 
					
					if(bd == null)
						bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
					
					// DO SOMETHING...
				}
				
				clientGetProfile.getEventoCtx().setEsito(Esito.OK);
				log.info("Lettura Profilo del Debitore completata con successo, "+(postMessage ? "" : "non ")+" verra' spedito il messaggio di notifica.");
			} catch(ClientException e) {
				log.error("Errore nella creazione del client di spedizione: " + e.getMessage());
				
				// Il client non e' stato creato non devo salvare info evento solo effettuare lo scheduling di un nuovo invio
				
				// DO SOMETHING...
				
			} catch(ApiException e) {
				log.error("Invocazione AppIO terminata con codice di errore [" + e.getCode() + "]: " + e.getMessage());
				
				if(clientGetProfile != null) {
					clientGetProfile.getEventoCtx().setSottotipoEsito(e.getCode() + "");
					clientGetProfile.getEventoCtx().setEsito(Esito.FAIL);
					clientGetProfile.getEventoCtx().setDescrizioneEsito(e.getMessage());
				}
				
				if(e.getCode() == 429) { // troppe chiamate al servizio in questo momento rischedulo invio
					// DO SOMETHING...
				} else { // invio notifica terminato con errore
					// DO SOMETHING...
				}
				
			} catch(ServiceException e) {
				log.error("Errore durante il salvataggio dello stato della notifica: " + e.getMessage());
			
				if(clientGetProfile != null) {
					clientGetProfile.getEventoCtx().setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
					clientGetProfile.getEventoCtx().setEsito(Esito.FAIL);
					clientGetProfile.getEventoCtx().setDescrizioneEsito(e.getMessage());
				}
				
				// provo a salvare l'errore 
				
				// DO SOMETHING...
			} finally {
				if(clientGetProfile != null && clientGetProfile.getEventoCtx().isRegistraEvento()) {
					GiornaleEventi giornaleEventi = new GiornaleEventi(bd);
					giornaleEventi.registraEvento(clientGetProfile.getEventoCtx().toEventoDTO());
				}
				
				if(bd != null) bd.closeConnection(); 
			}

			if(postMessage) { // Effettuo la POST Message
				try {
					String operationId = appContext.setupAppIOClient(SWAGGER_OPERATION_POST_MESSAGE, url);
					
					appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", this.dominio.getCodDominio()));
					appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codTipoVersamento", this.tipoVersamento.getCodTipoVersamento()));
					appContext.getServerByOperationId(operationId).addGenericProperty(new Property("idA2A", this.applicazione.getCodApplicazione()));
					appContext.getServerByOperationId(operationId).addGenericProperty(new Property("idPendenza", this.versamento.getCodVersamentoEnte()));
					appContext.getServerByOperationId(operationId).addGenericProperty(new Property("iuv", this.versamento.getIuvVersamento()));
					
					log.info("Invio della notifica al Debitore "+ this.versamento.getAnagraficaDebitore().getCodUnivoco()+"per la Pendenza [Id: "+this.versamento.getCodVersamentoEnte()+", IdA2A: " + this.applicazione.getCodApplicazione() + "]");
					
					clientPostMessage = new AppIoClient(SWAGGER_OPERATION_POST_MESSAGE, this.appIo, operationId, this.giornale);
					
					clientPostMessage.getEventoCtx().setCodDominio(this.dominio.getCodDominio());
					clientPostMessage.getEventoCtx().setIdA2A(this.applicazione.getCodApplicazione());
					clientPostMessage.getEventoCtx().setIdPendenza(this.versamento.getCodVersamentoEnte());
					clientPostMessage.getEventoCtx().setIuv(this.versamento.getIuvVersamento());
					
					NewMessage messageWithCF = AppIOUtils.creaNuovoMessaggio(log, this.versamento, this.tipoVersamentoDominio, this.appIo);
					MessageCreated messageCreated = clientPostMessage.postMessage(messageWithCF , this.tipoVersamentoDominio.getAppIOAPIKey(), SWAGGER_OPERATION_POST_MESSAGE);
					String location = clientPostMessage.getMessageLocation();
					
					// salvataggio stato notifica
					
					if(bd == null)
						bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
					
					// DO SOMETHING...
					
				} catch(ClientException e) {
					log.error("Errore nella creazione del client di spedizione: " + e.getMessage());
					
					// Il client non e' stato creato non devo salvare info evento solo effettuare lo scheduling di un nuovo invio
					
					// DO SOMETHING...
					
				} catch(ApiException e) {
					log.error("Invocazione AppIO terminata con codice di errore [" + e.getCode() + "]: " + e.getMessage());
					
					if(clientPostMessage != null) {
						clientPostMessage.getEventoCtx().setSottotipoEsito(e.getCode() + "");
						clientPostMessage.getEventoCtx().setEsito(Esito.FAIL);
						clientPostMessage.getEventoCtx().setDescrizioneEsito(e.getMessage());
					}
					
					if(e.getCode() == 429 || e.getCode() == 500) { // troppe chiamate al servizio in questo momento oppure errore interno del servizio rischedulo invio
						// DO SOMETHING...
					} else { // invio notifica terminato con errore
						// DO SOMETHING...
					}

				} catch(GovPayException e) {
					log.error("Invocazione durante la creazione del messaggio [" + e.getCodEsito() + "]: " + e.getMessage());
					
					if(clientPostMessage != null) {
						clientPostMessage.getEventoCtx().setSottotipoEsito(e.getCodEsito().toString());
						clientPostMessage.getEventoCtx().setEsito(Esito.FAIL);
						clientPostMessage.getEventoCtx().setDescrizioneEsito(e.getMessage());
					}
					
					// DO SOMETHING...

				} catch(ServiceException e) {
					log.error("Errore durante il salvataggio dello stato della notifica: " + e.getMessage());
					
					if(clientPostMessage != null) {
						clientPostMessage.getEventoCtx().setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
						clientPostMessage.getEventoCtx().setEsito(Esito.FAIL);
						clientPostMessage.getEventoCtx().setDescrizioneEsito(e.getMessage());
					}
					
					// provo a salvare l'errore 
					
					// DO SOMETHING...
				}finally {
					if(clientPostMessage != null && clientPostMessage.getEventoCtx().isRegistraEvento()) {
						GiornaleEventi giornaleEventi = new GiornaleEventi(bd);
						giornaleEventi.registraEvento(clientPostMessage.getEventoCtx().toEventoDTO());
					}
				}
			}
		} catch(Exception e) {

		} finally {
			this.completed = true;
			if(bd != null) bd.closeConnection(); 
			ContextThreadLocal.unset();
		}
	}

	public boolean isCompleted() {
		return this.completed;
	}
}
