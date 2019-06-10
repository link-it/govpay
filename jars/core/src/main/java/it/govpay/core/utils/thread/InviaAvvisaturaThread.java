package it.govpay.core.utils.thread;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import gov.telematici.pagamenti.ws.avvisi_digitali.CtAvvisoDigitale;
import gov.telematici.pagamenti.ws.avvisi_digitali.CtEsitoAvvisatura;
import gov.telematici.pagamenti.ws.avvisi_digitali.CtEsitoAvvisoDigitale;
import gov.telematici.pagamenti.ws.avvisi_digitali.CtFaultBean;
import gov.telematici.pagamenti.ws.avvisi_digitali.CtNodoInviaAvvisoDigitale;
import gov.telematici.pagamenti.ws.avvisi_digitali.CtNodoInviaAvvisoDigitaleRisposta;
import gov.telematici.pagamenti.ws.avvisi_digitali.StEsitoOperazione;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.EsitoAvvisatura;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.AvvisaturaUtils;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.client.AvvisaturaClient;
import it.govpay.core.utils.client.AvvisaturaClient.Azione;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.model.Intermediario;
import it.govpay.model.Versamento.ModoAvvisatura;

public class InviaAvvisaturaThread implements Runnable {

	private static Logger log = LoggerWrapperFactory.getLogger(InviaAvvisaturaThread.class);
	private boolean completed = false;
	private Versamento versamento = null;
	private Intermediario intermediario = null;
	private Stazione stazione = null;
	private CtAvvisoDigitale avviso = null;
	private String idTransazione = null;
	private Dominio dominio = null;
	private IContext ctx = null;
	private Giornale giornale = null;
	private Applicazione applicazione = null;

	public InviaAvvisaturaThread(Versamento versamento, String idTransazione, BasicBD bd, IContext ctx) throws ServiceException {
		this.idTransazione = idTransazione;
		this.versamento = versamento;
		//leggo le info annidate
		this.versamento.getDominio(bd);
		this.dominio = this.versamento.getDominio(bd);
		this.stazione = dominio.getStazione();
		this.intermediario = this.stazione.getIntermediario(bd);
		this.applicazione = this.versamento.getApplicazione(bd);
		
		List<SingoloVersamento> singoliVersamenti = this.versamento.getSingoliVersamenti(bd);
		for(SingoloVersamento singoloVersamento: singoliVersamenti) {
			singoloVersamento.getIbanAccredito(bd);
			singoloVersamento.getIbanAppoggio(bd);
		}

		try {
			this.avviso = AvvisaturaUtils.toCtAvvisoDigitale(versamento);
		} catch (UnsupportedEncodingException | DatatypeConfigurationException e) {
			throw new ServiceException(e);
		}
		this.ctx = ctx;
		this.giornale = AnagraficaManager.getConfigurazione(bd).getGiornale();
	}

	@Override
	public void run() {
		ContextThreadLocal.set(this.ctx);
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		MDC.put(MD5Constants.TRANSACTION_ID, ctx.getTransactionId());
		BasicBD bd = null;
		VersamentiBD versamentiBD = null;
		AvvisaturaClient client = null;
		try {
			String operationId = appContext.setupNodoClient(this.stazione.getCodStazione(), this.avviso.getIdentificativoDominio(), Azione.nodoInviaAvvisoDigitale);
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", this.avviso.getIdentificativoDominio()));
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codAvviso", this.avviso.getCodiceAvviso()));
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("tipoOperazione", this.avviso.getTipoOperazione().name()));

			ctx.getApplicationLogger().log("versamento.avvisaturaDigitale");
			ctx.getApplicationLogger().log("versamento.avvisaturaDigitaleSpedizione");

			log.info("Spedizione Avvisatura al Nodo [Dominio: " + this.avviso.getIdentificativoDominio() + ", NumeroAvviso: "+this.avviso.getCodiceAvviso()+", TipoAvvisatura: "+this.avviso.getTipoOperazione()+"]");

			client = new AvvisaturaClient(this.versamento, this.intermediario, this.stazione, this.giornale, operationId, bd);
			
			client.getEventoCtx().setIdA2A(this.applicazione.getCodApplicazione());
			client.getEventoCtx().setIdPendenza(this.versamento.getCodVersamentoEnte());

			CtNodoInviaAvvisoDigitale ctNodoInviaAvvisoDigitale = new CtNodoInviaAvvisoDigitale();
			ctNodoInviaAvvisoDigitale.setAvvisoDigitaleWS(this.avviso);
			ctNodoInviaAvvisoDigitale.setPassword(this.stazione.getPassword()); 
			
			CtNodoInviaAvvisoDigitaleRisposta risposta = client.nodoInviaAvvisoDigitale(this.intermediario, this.stazione, ctNodoInviaAvvisoDigitale ); 
			
			bd = BasicBD.newInstance(this.idTransazione);
			versamentiBD = new VersamentiBD(bd);
			
			if(risposta.getEsitoOperazione() == null || !risposta.getEsitoOperazione().equals(StEsitoOperazione.OK)) {
				CtFaultBean fault = risposta.getFault();
				
				// se l'avvisatura e' andata male allora passo il controllo alla modalita' asincrona
				//this.versamento.setAvvisaturaModalita(ModoAvvisatura.ASICNRONA.getValue());
				// segnalo che il versamento non e' piu' da avvisare
				this.versamento.setAvvisaturaDaInviare(false);
				versamentiBD.updateVersamentoStatoAvvisatura(this.versamento.getId(), false);

				// emetto un evento ok
				log.info("Avvisatura Digitale inviata con errore al nodo");
				ctx.getApplicationLogger().log("versamento.avvisaturaDigitaleKo", fault.getDescription());
				client.getEventoCtx().setSottotipoEsito(fault.getFaultCode());
				client.getEventoCtx().setEsito(Esito.KO);
				client.getEventoCtx().setDescrizioneEsito(fault.getDescription());
			} else { // ok
				List<it.govpay.model.EsitoAvvisatura> esitoAvvisaturaLst = new ArrayList<>();
				CtEsitoAvvisoDigitale esitoAvvisoDigitale = risposta.getEsitoAvvisoDigitaleWS();
				if(esitoAvvisoDigitale != null) {
					List<CtEsitoAvvisatura> leggiListaAvvisiDigitali = esitoAvvisoDigitale.getEsitoAvvisatura();

					for(CtEsitoAvvisatura ctEsitoAvvisatura : leggiListaAvvisiDigitali) {
						EsitoAvvisatura esitoAvvisatura = new EsitoAvvisatura();
						esitoAvvisatura.setCodDominio(esitoAvvisoDigitale.getIdentificativoDominio());
						esitoAvvisatura.setIdentificativoAvvisatura(esitoAvvisoDigitale.getIdentificativoMessaggioRichiesta());

						esitoAvvisatura.setTipoCanale(Integer.parseInt(ctEsitoAvvisatura.getTipoCanaleEsito()));
						esitoAvvisatura.setCodCanale(ctEsitoAvvisatura.getIdentificativoCanale());
						esitoAvvisatura.setData(ctEsitoAvvisatura.getDataEsito());
						esitoAvvisatura.setCodEsito(ctEsitoAvvisatura.getCodiceEsito());
						esitoAvvisatura.setDescrizioneEsito(ctEsitoAvvisatura.getDescrizioneEsito());

						esitoAvvisaturaLst.add(esitoAvvisatura);
					}
				}

				// segnalo che il versamento non e' piu' da avvisare
				this.versamento.setAvvisaturaDaInviare(false);
				versamentiBD.updateVersamentoStatoAvvisatura(this.versamento.getId(), false);

				// emetto un evento ok
				log.info("Avvisatura Digitale inviata correttamente al nodo");
				ctx.getApplicationLogger().log("versamento.avvisaturaDigitaleOk");
				client.getEventoCtx().setEsito(Esito.OK);
			}
		} catch (ClientException e) {
			log.error("Errore nella spedizione della Avvisatura Digitale", e);
			if(client != null) {
				client.getEventoCtx().setSottotipoEsito(e.getResponseCode() + "");
				client.getEventoCtx().setEsito(Esito.KO);
				client.getEventoCtx().setDescrizioneEsito(e.getMessage());
			}
			try {
				ctx.getApplicationLogger().log("versamento.avvisaturaDigitaleFail", e.getMessage());
			} catch (UtilsException e2) {
				log.error("Errore durante il log dell'operazione: " + e.getMessage(), e2);
			}

			// se l'avvisatura in modalita sincrona e' andata male allora passo il controllo alla modalita' asincrona
			if(this.versamento.getAvvisaturaModalita().equals(ModoAvvisatura.SINCRONA.getValue())) {
				this.versamento.setAvvisaturaModalita(ModoAvvisatura.ASICNRONA.getValue());
				try {
					if(versamentiBD == null)
						versamentiBD = new VersamentiBD(bd);
					
					versamentiBD.updateVersamentoModalitaAvvisatura(this.versamento.getId(), ModoAvvisatura.ASICNRONA.getValue());
				} catch (ServiceException e1) {
					log.error("Errore aggiornamento modalita avvisatura versaemento", e);
				}
			}
		} catch(Exception e) {
			log.error("Errore nella spedizione della Avvisatura Digitale", e);
			if(client != null) {
				if(e instanceof GovPayException) {
					client.getEventoCtx().setSottotipoEsito(((GovPayException)e).getCodEsito().toString());
				} else {
					client.getEventoCtx().setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
				}
				client.getEventoCtx().setEsito(Esito.FAIL);
				client.getEventoCtx().setDescrizioneEsito(e.getMessage());
			}
			try {
				ctx.getApplicationLogger().log("versamento.avvisaturaDigitaleFail", e.getMessage());
			} catch (UtilsException e2) {
				log.error("Errore durante il log dell'operazione: " + e.getMessage(), e2);
			}

			// se l'avvisatura in modalita sincrona e' andata male allora passo il controllo alla modalita' asincrona
			if(this.versamento.getAvvisaturaModalita().equals(ModoAvvisatura.SINCRONA.getValue())) {
				this.versamento.setAvvisaturaModalita(ModoAvvisatura.ASICNRONA.getValue());
				try {
					versamentiBD.updateVersamentoModalitaAvvisatura(this.versamento.getId(), ModoAvvisatura.ASICNRONA.getValue());
				} catch (ServiceException e1) {
					log.error("Errore aggiornamento modalita avvisatura versaemento", e);
				}
			}
		} finally {
			if(client != null && client.getEventoCtx().isRegistraEvento()) {
				GiornaleEventi giornaleEventi = new GiornaleEventi(bd);
				giornaleEventi.registraEvento(client.getEventoCtx().toEventoDTO());
			}
			this.completed = true;
			if(bd != null) bd.closeConnection(); 
			ContextThreadLocal.unset();
		}
	}

	public boolean isCompleted() {
		return this.completed;
	}

}
