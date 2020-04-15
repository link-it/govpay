package it.govpay.core.utils.thread;

import java.util.Date;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.BasicBD;
import it.govpay.bd.configurazione.model.AppIOBatch;
import it.govpay.bd.configurazione.model.AvvisaturaViaAppIo;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.configurazione.model.PromemoriaAvvisoBase;
import it.govpay.bd.model.Configurazione;
import it.govpay.bd.model.NotificaAppIo;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.NotificheAppIoBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.appio.AppIOUtils;
import it.govpay.core.utils.appio.impl.ApiException;
import it.govpay.core.utils.appio.model.LimitedProfile;
import it.govpay.core.utils.appio.model.MessageCreated;
import it.govpay.core.utils.appio.model.NewMessage;
import it.govpay.core.utils.client.AppIoClient;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.model.TipoVersamento;

public class InviaNotificaAppIoThread implements Runnable{
	
	public static final String SWAGGER_OPERATION_GET_PROFILE = "getProfile";
	public static final String SWAGGER_OPERATION_POST_MESSAGE = "submitMessageforUserWithFiscalCodeInBody";

	private IContext ctx = null;
	private Giornale giornale = null;
	private AppIOBatch appIo = null; 
	private AvvisaturaViaAppIo avvisaturaViaAppIo = null; 
	private static Logger log = LoggerWrapperFactory.getLogger(InviaNotificaAppIoThread.class);
	private NotificaAppIo notifica= null;
	private boolean completed = false;
	private boolean errore = false;
	private TipoVersamentoDominio tipoVersamentoDominio = null;
	private TipoVersamento tipoVersamento = null;

	public InviaNotificaAppIoThread(NotificaAppIo notifica, BasicBD bd, IContext ctx) throws ServiceException {
		this.ctx = ctx;
		Configurazione configurazione = new it.govpay.core.business.Configurazione(bd).getConfigurazione();
		this.giornale = configurazione.getGiornale();
		this.appIo = configurazione.getBatchSpedizioneAppIo();
		this.avvisaturaViaAppIo = configurazione.getAvvisaturaViaAppIo();
		this.notifica = notifica;
		this.tipoVersamentoDominio = notifica.getTipoVersamentoDominio(bd);
		this.tipoVersamento = this.tipoVersamentoDominio.getTipoVersamento(bd);
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
		Versamento versamento = null;
		try {
			String url = this.appIo.getUrl(); // Base url del servizio
			try {
				String operationId = appContext.setupAppIOClient(SWAGGER_OPERATION_GET_PROFILE, url);
				
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", this.notifica.getCodDominio()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codTipoVersamento", this.tipoVersamento.getCodTipoVersamento()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("idA2A", this.notifica.getCodApplicazione()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("idPendenza", this.notifica.getCodVersamentoEnte()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("iuv", this.notifica.getIuv()));
				
				log.info("Lettura Profilo del Debitore "+ this.notifica.getDebitoreIdentificativo() +" per la Pendenza [Id: "+this.notifica.getCodVersamentoEnte()+", IdA2A: " + this.notifica.getCodApplicazione() + "]");
				
				clientGetProfile = new AppIoClient(SWAGGER_OPERATION_GET_PROFILE, this.appIo, operationId, this.giornale);

				clientGetProfile.getEventoCtx().setCodDominio(this.notifica.getCodDominio());
				clientGetProfile.getEventoCtx().setIdA2A(this.notifica.getCodApplicazione());
				clientGetProfile.getEventoCtx().setIdPendenza(this.notifica.getCodVersamentoEnte());
				clientGetProfile.getEventoCtx().setIuv(this.notifica.getIuv());
				
				LimitedProfile profile = clientGetProfile.getProfile(this.notifica.getDebitoreIdentificativo(), this.tipoVersamentoDominio.getAppIOAPIKey(), SWAGGER_OPERATION_GET_PROFILE);
						
				// prendo la connessione mi servira' in entrambi i casi
				bd = setupConnection(bd);
					
				if(profile.isSenderAllowed()) { // spedizione abilitata procedo
					postMessage = true;
					versamento = this.notifica.getVersamento(bd);
				} else { // termino il processo indicando che la notifica non verra' spedita 
					clientGetProfile.getEventoCtx().setDescrizioneEsito("Utente non abilitato alla spedizione della notifica");
					bd = aggiornaNotificaAnnullata(bd, "Utente non abilitato alla spedizione della notifica");
				}
				
				clientGetProfile.getEventoCtx().setEsito(Esito.OK);
				log.info("Lettura Profilo del Debitore completata con successo, "+(postMessage ? "" : "non ")+" verra' spedito il messaggio di notifica.");
			} catch(ClientException e) {
				errore = true;
				log.error("Errore nella creazione del client di spedizione: " + e.getMessage());
				bd = aggiornaNotificaDaSpedire(bd, e.getMessage());
			} catch(ApiException e) {
				errore = true;
				log.error("Invocazione AppIO terminata con codice di errore [" + e.getCode() + "]: " + e.getMessage());
				
				if(clientGetProfile != null) {
					clientGetProfile.getEventoCtx().setSottotipoEsito(e.getCode() + "");
					
					if(e.getCode() == 429 || e.getCode() == 404) {
						clientGetProfile.getEventoCtx().setEsito(Esito.KO);
					} else {
						clientGetProfile.getEventoCtx().setEsito(Esito.FAIL);
					}
					clientGetProfile.getEventoCtx().setDescrizioneEsito(e.getMessage());
				}
				
				if(e.getCode() == 429) { // troppe chiamate al servizio in questo momento rischedulo invio
					bd = aggiornaNotificaDaSpedire(bd, e.getMessage());
				} else { // invio notifica terminato con errore
					bd = aggiornaNotificaAnnullata(bd, e.getMessage());
				}
				
			} catch(ServiceException e) {
				errore = true;
				log.error("Errore durante il salvataggio l'accesso alla base dati: " + e.getMessage());
			
				if(clientGetProfile != null) {
					clientGetProfile.getEventoCtx().setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
					clientGetProfile.getEventoCtx().setEsito(Esito.FAIL);
					clientGetProfile.getEventoCtx().setDescrizioneEsito(e.getMessage());
				}
				
				// provo a salvare l'errore 
				bd = aggiornaNotificaDaSpedire(bd, e.getMessage());
			} catch(Throwable e) {
				errore = true;
				log.error("Errore non previsto durante l'invocazione del servizio: " + e.getMessage(), e);
			
				if(clientGetProfile != null) {
					clientGetProfile.getEventoCtx().setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
					clientGetProfile.getEventoCtx().setEsito(Esito.FAIL);
					clientGetProfile.getEventoCtx().setDescrizioneEsito(e.getMessage());
				}
				
				// provo a salvare l'errore 
				bd = aggiornaNotificaDaSpedire(bd, e.getMessage());
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
					
					appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", this.notifica.getCodDominio()));
					appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codTipoVersamento", this.tipoVersamento.getCodTipoVersamento()));
					appContext.getServerByOperationId(operationId).addGenericProperty(new Property("idA2A", this.notifica.getCodApplicazione()));
					appContext.getServerByOperationId(operationId).addGenericProperty(new Property("idPendenza", this.notifica.getCodVersamentoEnte()));
					appContext.getServerByOperationId(operationId).addGenericProperty(new Property("iuv", this.notifica.getIuv()));
					
					log.info("Invio della notifica al Debitore "+ this.notifica.getDebitoreIdentificativo()+" per la Pendenza [Id: "+this.notifica.getCodVersamentoEnte()+", IdA2A: " + this.notifica.getCodApplicazione() + "]");
					
					clientPostMessage = new AppIoClient(SWAGGER_OPERATION_POST_MESSAGE, this.appIo, operationId, this.giornale);
					
					clientPostMessage.getEventoCtx().setCodDominio(this.notifica.getCodDominio());
					clientPostMessage.getEventoCtx().setIdA2A(this.notifica.getCodApplicazione());
					clientPostMessage.getEventoCtx().setIdPendenza(this.notifica.getCodVersamentoEnte());
					clientPostMessage.getEventoCtx().setIuv(this.notifica.getIuv());
					
					PromemoriaAvvisoBase promemoriaAvviso = this.avvisaturaViaAppIo.getPromemoriaAvviso() != null ? this.avvisaturaViaAppIo.getPromemoriaAvviso() : new PromemoriaAvvisoBase();
					NewMessage messageWithCF = AppIOUtils.creaNuovoMessaggio(log, versamento, this.tipoVersamentoDominio, promemoriaAvviso, this.appIo.getTimeToLive());
					MessageCreated messageCreated = clientPostMessage.postMessage(messageWithCF , this.tipoVersamentoDominio.getAppIOAPIKey(), SWAGGER_OPERATION_POST_MESSAGE);
					//String location = clientPostMessage.getMessageLocation();
					
					// salvataggio stato notifica
					bd = setupConnection(bd);
					
					NotificheAppIoBD notificheBD = new NotificheAppIoBD(bd);
					notificheBD.updateSpedito(this.notifica.getId(), messageCreated.getId(), null);
					
					clientPostMessage.getEventoCtx().setEsito(Esito.OK);
				} catch(ClientException e) {
					errore = true;
					log.error("Errore nella creazione del client di spedizione: " + e.getMessage());
					bd = aggiornaNotificaDaSpedire(bd, e.getMessage());
				} catch(ApiException e) {
					errore = true;
					log.error("Invocazione AppIO terminata con codice di errore [" + e.getCode() + "]: " + e.getMessage());
					
					if(clientPostMessage != null) {
						clientPostMessage.getEventoCtx().setSottotipoEsito(e.getCode() + "");
						clientPostMessage.getEventoCtx().setEsito(Esito.FAIL);
						clientPostMessage.getEventoCtx().setDescrizioneEsito(e.getMessage());
					}
					
					if(e.getCode() == 429 || e.getCode() == 500) { // troppe chiamate al servizio in questo momento oppure errore interno del servizio rischedulo invio
						bd = aggiornaNotificaDaSpedire(bd, e.getMessage());
					} else { // invio notifica terminato con errore
						bd = aggiornaNotificaAnnullata(bd, e.getMessage());
					}

				} catch(GovPayException e) {
					errore = true;
					log.error("Invocazione durante la creazione del messaggio [" + e.getCodEsito() + "]: " + e.getMessage());
					
					if(clientPostMessage != null) {
						clientPostMessage.getEventoCtx().setSottotipoEsito(e.getCodEsito().toString());
						clientPostMessage.getEventoCtx().setEsito(Esito.FAIL);
						clientPostMessage.getEventoCtx().setDescrizioneEsito(e.getMessage());
					}
					
					bd = aggiornaNotificaDaSpedire(bd, e.getMessage());

				} catch(ServiceException e) {
					errore = true;
					log.error("Errore durante il salvataggio dello stato della notifica: " + e.getMessage());
					
					if(clientPostMessage != null) {
						clientPostMessage.getEventoCtx().setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
						clientPostMessage.getEventoCtx().setEsito(Esito.FAIL);
						clientPostMessage.getEventoCtx().setDescrizioneEsito(e.getMessage());
					}
					
					// provo a salvare l'errore 
					bd = aggiornaNotificaDaSpedire(bd, e.getMessage());
				} catch(Throwable e) {
					errore = true;
					log.error("Errore non previsto durante l'invocazione del servizio: " + e.getMessage(), e);
				
					if(clientGetProfile != null) {
						clientGetProfile.getEventoCtx().setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
						clientGetProfile.getEventoCtx().setEsito(Esito.FAIL);
						clientGetProfile.getEventoCtx().setDescrizioneEsito(e.getMessage());
					}
					
					// provo a salvare l'errore 
					bd = aggiornaNotificaDaSpedire(bd, e.getMessage());
				} finally {
					if(clientPostMessage != null && clientPostMessage.getEventoCtx().isRegistraEvento()) {
						GiornaleEventi giornaleEventi = new GiornaleEventi(bd);
						giornaleEventi.registraEvento(clientPostMessage.getEventoCtx().toEventoDTO());
					}
					
					if(bd != null) bd.closeConnection(); 
				}
			}
		} catch(Exception e) {
			errore = true;
			log.error("Errore: " + e.getMessage(), e);
		} finally {
			this.completed = true;
			try {
				if(bd != null && !bd.isClosed()) bd.closeConnection();
			} catch (ServiceException e) {
				log.error("Errore chiusura connessione: " + e.getMessage(), e);
			} 
			ContextThreadLocal.unset();
		}
	}

	private BasicBD setupConnection(BasicBD bd) throws ServiceException {
		if(bd == null) {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
		} else {
			if(bd.isClosed())
				bd.setupConnection(ContextThreadLocal.get().getTransactionId());
		}
		
		return bd;
	}
	
	private BasicBD aggiornaNotificaAnnullata(BasicBD bd, String message) {
		// Il client non e' stato creato non devo salvare info evento solo effettuare lo scheduling di un nuovo invio
		try {
			bd = setupConnection(bd);
			
			long tentativi = this.notifica.getTentativiSpedizione() + 1;
			NotificheAppIoBD notificheBD = new NotificheAppIoBD(bd);
			
			Date today = new Date();
			Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
			Date prossima = new Date(today.getTime() + (tentativi * tentativi * 60 * 1000));
			
			// Limito la rispedizione al giorno dopo.
			if(prossima.after(tomorrow)) prossima = tomorrow;
			
//					if(tentativi == 1 || !e.getMessage().equals(this.notifica.getDescrizioneStato())) {
//						ctx.getApplicationLogger().log("notifica.carrelloko", e.getMessage());
//					} else {
//						ctx.getApplicationLogger().log("notifica.carrelloRetryko", e.getMessage(), prossima.toString());
//					}
			
			log.debug("Aggiornamento Notifica del Debitore "+ this.notifica.getDebitoreIdentificativo() +" per la Pendenza [Id: "+this.notifica.getCodVersamentoEnte()+", IdA2A: " + this.notifica.getCodApplicazione() + "] in stato ANNULLATA.");
			notificheBD.updateAnnullata(this.notifica.getId(), message, tentativi, prossima);
			
		} catch (Exception ee) {
			log.error("Errore durante l'aggiornamento della notifica: " + ee.getMessage(),ee);
			// Andato male l'aggiornamento. Non importa, verra' rispedito.
		}
		return bd;
	}
	
	private BasicBD aggiornaNotificaDaSpedire(BasicBD bd, String message) {
		try {
			bd = setupConnection(bd);
			
			long tentativi = this.notifica.getTentativiSpedizione() + 1;
			NotificheAppIoBD notificheBD = new NotificheAppIoBD(bd);
			
			Date today = new Date();
			Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
			Date prossima = new Date(today.getTime() + (tentativi * tentativi * 60 * 1000));
			
			// Limito la rispedizione al giorno dopo.
			if(prossima.after(tomorrow)) prossima = tomorrow;
			
//					if(tentativi == 1 || !e.getMessage().equals(this.notifica.getDescrizioneStato())) {
//						ctx.getApplicationLogger().log("notifica.carrelloko", e.getMessage());
//					} else {
//						ctx.getApplicationLogger().log("notifica.carrelloRetryko", e.getMessage(), prossima.toString());
//					}
			log.debug("Aggiornamento Notifica del Debitore "+ this.notifica.getDebitoreIdentificativo() +" per la Pendenza [Id: "+this.notifica.getCodVersamentoEnte()+", IdA2A: " + this.notifica.getCodApplicazione() + "] in stato DA SPEDIRE.");
			notificheBD.updateDaSpedire(this.notifica.getId(), message, tentativi, prossima);
			
		} catch (Exception ee) {
			// Andato male l'aggiornamento. Non importa, verra' rispedito.
			log.error("Errore durante l'aggiornamento della notifica: " + ee.getMessage(),ee);
		}
		return bd;
	}

	public boolean isCompleted() {
		return this.completed;
	}
	
	public boolean isErrore() {
		return this.errore;
	}
}
