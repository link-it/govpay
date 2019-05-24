package it.govpay.core.utils.thread;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import gov.telematici.pagamenti.ws.avvisi_digitali.CtAvvisoDigitale;
import gov.telematici.pagamenti.ws.avvisi_digitali.CtEsitoAvvisatura;
import gov.telematici.pagamenti.ws.avvisi_digitali.CtEsitoAvvisoDigitale;
import gov.telematici.pagamenti.ws.avvisi_digitali.CtFaultBean;
import gov.telematici.pagamenti.ws.avvisi_digitali.CtNodoInviaAvvisoDigitale;
import gov.telematici.pagamenti.ws.avvisi_digitali.CtNodoInviaAvvisoDigitaleRisposta;
import gov.telematici.pagamenti.ws.avvisi_digitali.StEsitoOperazione;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.EsitoAvvisatura;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.eventi.Controparte;
import it.govpay.bd.model.eventi.DettaglioRichiesta;
import it.govpay.bd.model.eventi.DettaglioRisposta;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.utils.AvvisaturaUtils;
import it.govpay.core.utils.EventoContext.Componente;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.client.AvvisaturaClient;
import it.govpay.core.utils.client.AvvisaturaClient.Azione;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.model.Evento.CategoriaEvento;
import it.govpay.model.Evento.EsitoEvento;
import it.govpay.model.Evento.RuoloEvento;
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

	public InviaAvvisaturaThread(Versamento versamento, String idTransazione, BasicBD bd, IContext ctx) throws ServiceException {
		this.idTransazione = idTransazione;
		this.versamento = versamento;
		//leggo le info annidate
		this.versamento.getDominio(bd);
		this.dominio = this.versamento.getDominio(bd);
		this.stazione = dominio.getStazione();
		this.intermediario = this.stazione.getIntermediario(bd);

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
	}

	@Override
	public void run() {
		ContextThreadLocal.set(this.ctx);
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		BasicBD bd = null;
		GiornaleEventi giornaleEventi = null;
		VersamentiBD versamentiBD = null;
		Evento evento = new Evento();
		String messaggioRichiesta = null;
		String messaggioRisposta = null;
		try {
			bd = BasicBD.newInstance(this.idTransazione);
			giornaleEventi = new GiornaleEventi(bd);
			versamentiBD = new VersamentiBD(bd);

			String operationId = appContext.setupNodoClient(this.stazione.getCodStazione(), this.avviso.getIdentificativoDominio(), Azione.nodoInviaAvvisoDigitale);
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", this.avviso.getIdentificativoDominio()));
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codAvviso", this.avviso.getCodiceAvviso()));
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("tipoOperazione", this.avviso.getTipoOperazione().name()));

			ctx.getApplicationLogger().log("versamento.avvisaturaDigitale");

			evento.setData(new Date());
			
			ctx.getApplicationLogger().log("versamento.avvisaturaDigitaleSpedizione");

			log.info("Spedizione Avvisatura al Nodo [Dominio: " + this.avviso.getIdentificativoDominio() + ", NumeroAvviso: "+this.avviso.getCodiceAvviso()+", TipoAvvisatura: "+this.avviso.getTipoOperazione()+"]");

			AvvisaturaClient client = new AvvisaturaClient(this.intermediario,operationId,bd);

			CtNodoInviaAvvisoDigitale ctNodoInviaAvvisoDigitale = new CtNodoInviaAvvisoDigitale();
			ctNodoInviaAvvisoDigitale.setAvvisoDigitaleWS(this.avviso);
			ctNodoInviaAvvisoDigitale.setPassword(this.stazione.getPassword()); 
			
			messaggioRichiesta = new String(client.getByteRichiesta(this.intermediario, this.stazione, ctNodoInviaAvvisoDigitale));

			CtNodoInviaAvvisoDigitaleRisposta risposta = client.nodoInviaAvvisoDigitale(this.intermediario, this.stazione, ctNodoInviaAvvisoDigitale );
			
			messaggioRisposta = new String(client.getByteRisposta(risposta));

			if(risposta.getEsitoOperazione() == null || !risposta.getEsitoOperazione().equals(StEsitoOperazione.OK)) {
				CtFaultBean fault = risposta.getFault();
				
				String esito = fault.getFaultCode() != null ? fault.getFaultCode() : StEsitoOperazione.KO.name();

				buildEventoCoperazione(evento, AvvisaturaClient.Azione.nodoInviaAvvisoDigitale.name(), esito, fault.getDescription(), messaggioRichiesta, messaggioRisposta, bd);
				giornaleEventi.registraEvento(evento);

				// se l'avvisatura e' andata male allora passo il controllo alla modalita' asincrona
				//this.versamento.setAvvisaturaModalita(ModoAvvisatura.ASICNRONA.getValue());
				// segnalo che il versamento non e' piu' da avvisare
				this.versamento.setAvvisaturaDaInviare(false);
				versamentiBD.updateVersamentoStatoAvvisatura(this.versamento.getId(), false);

				// emetto un evento ok
				log.info("Avvisatura Digitale inviata con errore al nodo");
				ctx.getApplicationLogger().log("versamento.avvisaturaDigitaleKo", fault.getDescription());
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
				buildEventoCoperazione(evento, AvvisaturaClient.Azione.nodoInviaAvvisoDigitale.name(), StEsitoOperazione.OK.name(), null, messaggioRichiesta, messaggioRisposta, bd);
				giornaleEventi.registraEvento(evento);

				log.info("Avvisatura Digitale inviata correttamente al nodo");
				ctx.getApplicationLogger().log("versamento.avvisaturaDigitaleOk");

			}
		} catch (ClientException e) {
			log.error("Errore nella spedizione della Avvisatura Digitale", e);
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

			if(giornaleEventi != null) {
				buildEventoCoperazione(evento, AvvisaturaClient.Azione.nodoInviaAvvisoDigitale.name(), StEsitoOperazione.KO.name(), e.getMessage(), messaggioRichiesta, messaggioRisposta, bd);
				giornaleEventi.registraEvento(evento);
			}
		} catch(Exception e) {
			log.error("Errore nella spedizione della Avvisatura Digitale", e);
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

			if(giornaleEventi != null) {
				buildEventoCoperazione(evento, AvvisaturaClient.Azione.nodoInviaAvvisoDigitale.name(), StEsitoOperazione.KO.name(), e.getMessage(), messaggioRichiesta, messaggioRisposta, bd);
				giornaleEventi.registraEvento(evento);
			}
		} finally {
			this.completed = true;
			if(bd != null) bd.closeConnection(); 
//			if(ctx != null)
//				try {
//					ctx.getApplicationLogger().log();
//				} catch (UtilsException e) {
//					log.error("Errore durante il log dell'operazione: " + e.getMessage(), e);
//				}
			ContextThreadLocal.unset();
		}
	}

	public boolean isCompleted() {
		return this.completed;
	}

	private void buildEventoCoperazione(Evento evento, String tipoEvento, String esito, String descrizioneEsito, String messaggioRichiesta, String messaggioRisposta, BasicBD bd) {
		
		Controparte controparte = new Controparte();
		controparte.setCodStazione(this.stazione.getCodStazione());
		controparte.setErogatore(Evento.NDP);
		controparte.setFruitore(this.intermediario.getDenominazione());
		evento.setControparte(controparte);
		evento.setCategoriaEvento(CategoriaEvento.INTERFACCIA);
		evento.setRuoloEvento(RuoloEvento.CLIENT);
		evento.setComponente(Componente.API_PAGOPA.name());
		evento.setIntervalloFromData(new Date());
		evento.setEsitoEvento(EsitoEvento.OK); // TODO rivedere
		evento.setDettaglioEsito(descrizioneEsito);
		evento.setSottotipoEvento(this.avviso.getTipoOperazione().name());
		evento.setTipoEvento(tipoEvento);
		DettaglioRichiesta dettaglioRichiesta = new DettaglioRichiesta();
		dettaglioRichiesta.setPayload(messaggioRichiesta);
		evento.setDettaglioRichiesta(dettaglioRichiesta);
		DettaglioRisposta dettaglioRisposta = new DettaglioRisposta();
		dettaglioRisposta.setPayload(messaggioRisposta);
		evento.setDettaglioRisposta(dettaglioRisposta );

		evento.setIdVersamento(this.versamento.getId());
	}
}
