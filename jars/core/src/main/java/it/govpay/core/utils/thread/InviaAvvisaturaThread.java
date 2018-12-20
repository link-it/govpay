package it.govpay.core.utils.thread;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
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
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.EsitoAvvisatura;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.eventi.EventoCooperazione;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.utils.AvvisaturaUtils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.client.AvvisaturaClient;
import it.govpay.core.utils.client.AvvisaturaClient.Azione;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.model.Evento.CategoriaEvento;
import it.govpay.model.Versamento.ModoAvvisatura;
import it.govpay.model.Intermediario;

public class InviaAvvisaturaThread implements Runnable {

	private static Logger log = LoggerWrapperFactory.getLogger(InviaAvvisaturaThread.class);
	private boolean completed = false;
	private Versamento versamento = null;
	private Intermediario intermediario = null;
	private Stazione stazione = null;
	private CtAvvisoDigitale avviso = null;
	private String idTransazione = null;
	private Dominio dominio = null;

	public InviaAvvisaturaThread(Versamento versamento, String idTransazione, BasicBD bd) throws ServiceException {
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
	}
	
	@Override
	public void run() {
		
		GpContext ctx = null;
		BasicBD bd = null;
		GiornaleEventi giornaleEventi = null;
		VersamentiBD versamentiBD = null;
		EventoCooperazione evento = new EventoCooperazione();
		try {
			if(this.idTransazione != null)
				ctx = new GpContext(this.idTransazione);
			else 
				ctx = new GpContext();
			
			GpThreadLocal.set(ctx);
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			giornaleEventi = new GiornaleEventi(bd);
			versamentiBD = new VersamentiBD(bd);
			MDC.put("cmd", "InviaAvvisaturaThread");
			MDC.put("op", ctx.getTransactionId());
			
			ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", this.avviso.getIdentificativoDominio()));
			ctx.getContext().getRequest().addGenericProperty(new Property("codAvviso", this.avviso.getCodiceAvviso()));
			ctx.getContext().getRequest().addGenericProperty(new Property("tipoOperazione", this.avviso.getTipoOperazione().name()));
			
			ctx.log("versamento.avvisaturaDigitale");
			
			evento.setData(new Date());
			evento.setDataRichiesta(new Date());
			
			ctx.setupNodoClient(this.stazione.getCodStazione(), this.avviso.getIdentificativoDominio(), Azione.nodoInviaAvvisoDigitale);
			ctx.log("versamento.avvisaturaDigitaleSpedizione");
			
			log.info("Spedizione Avvisatura al Nodo [Dominio: " + this.avviso.getIdentificativoDominio() + ", NumeroAvviso: "+this.avviso.getCodiceAvviso()+", TipoAvvisatura: "+this.avviso.getTipoOperazione()+"]");
			
			AvvisaturaClient client = new AvvisaturaClient(this.intermediario,bd);
			
			CtNodoInviaAvvisoDigitale ctNodoInviaAvvisoDigitale = new CtNodoInviaAvvisoDigitale();
			ctNodoInviaAvvisoDigitale.setAvvisoDigitaleWS(this.avviso);
			ctNodoInviaAvvisoDigitale.setPassword(this.stazione.getPassword()); 
			
			CtNodoInviaAvvisoDigitaleRisposta risposta = client.nodoInviaAvvisoDigitale(this.intermediario, this.stazione, ctNodoInviaAvvisoDigitale );

			if(risposta.getEsitoOperazione() == null || !risposta.getEsitoOperazione().equals(StEsitoOperazione.OK)) {
				CtFaultBean fault = risposta.getFault();
				
				buildEventoCoperazione(evento, AvvisaturaClient.Azione.nodoInviaAvvisoDigitale.name(), StEsitoOperazione.KO.name(), fault.getDescription(), bd);
				giornaleEventi.registraEventoCooperazione(evento);
				
				// se l'avvisatura e' andata male allora passo il controllo alla modalita' asincrona
				//this.versamento.setAvvisaturaModalita(ModoAvvisatura.ASICNRONA.getValue());
				// segnalo che il versamento non e' piu' da avvisare
				this.versamento.setAvvisaturaDaInviare(false);
				versamentiBD.updateVersamentoStatoAvvisatura(this.versamento.getId(), false);
			
				// emetto un evento ok
				log.info("Avvisatura Digitale inviata con errore al nodo");
				ctx.log("versamento.avvisaturaDigitaleKo", fault.getDescription());
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
					
					// segnalo che il versamento non e' piu' da avvisare
					this.versamento.setAvvisaturaDaInviare(false);
					versamentiBD.updateVersamentoStatoAvvisatura(this.versamento.getId(), false);
					
					// emetto un evento ok
					buildEventoCoperazione(evento, AvvisaturaClient.Azione.nodoInviaAvvisoDigitale.name(), StEsitoOperazione.OK.name(), null, bd);
					giornaleEventi.registraEventoCooperazione(evento);
					
					log.info("Avvisatura Digitale inviata correttamente al nodo");
					ctx.log("versamento.avvisaturaDigitaleOk");
				}
			}
		} catch (ClientException e) {
			log.error("Errore nella spedizione della Avvisatura Digitale", e);
			ctx.log("versamento.avvisaturaDigitaleFail", e.getMessage());
			
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
				buildEventoCoperazione(evento, AvvisaturaClient.Azione.nodoInviaAvvisoDigitale.name(), StEsitoOperazione.KO.name(), e.getMessage(), bd);
				giornaleEventi.registraEventoCooperazione(evento);
			}
		} catch(Exception e) {
			log.error("Errore nella spedizione della Avvisatura Digitale", e);
			ctx.log("versamento.avvisaturaDigitaleFail", e.getMessage());
			
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
				buildEventoCoperazione(evento, AvvisaturaClient.Azione.nodoInviaAvvisoDigitale.name(), StEsitoOperazione.KO.name(), e.getMessage(), bd);
				giornaleEventi.registraEventoCooperazione(evento);
			}
		} finally {
			this.completed = true;
			if(bd != null) bd.closeConnection(); 
			if(ctx != null) ctx.log();
		}
	}

	public boolean isCompleted() {
		return this.completed;
	}
	
	private void buildEventoCoperazione(EventoCooperazione evento, String tipoEvento, String esito, String descrizioneEsito, BasicBD bd) {
		evento.setAltriParametriRichiesta(null);
		evento.setAltriParametriRisposta(null);
		evento.setCategoriaEvento(CategoriaEvento.INTERFACCIA_COOPERAZIONE);
		evento.setCodDominio(this.dominio.getCodDominio());
		evento.setCodStazione(this.stazione.getCodStazione());
		evento.setComponente(EventoCooperazione.COMPONENTE);
		evento.setDataRisposta(new Date());
		evento.setErogatore(EventoCooperazione.NDP);
		evento.setEsito(esito);
		evento.setDescrizioneEsito(descrizioneEsito);
		evento.setFruitore(this.intermediario.getDenominazione());
		evento.setIuv(this.versamento.getIuvVersamento());
		evento.setSottotipoEvento(this.avviso.getTipoOperazione().name());
		evento.setTipoEvento(tipoEvento);
		//evento.setTipoVersamento(this.versamento.getTipoPagamento());

		evento.setIdVersamento(this.versamento.getId());
	}
}
