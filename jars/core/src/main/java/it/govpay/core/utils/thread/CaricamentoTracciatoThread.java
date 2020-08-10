package it.govpay.core.utils.thread;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Operazione;
import it.govpay.bd.pagamento.OperazioniBD;
import it.govpay.core.business.model.tracciati.CostantiCaricamento;
import it.govpay.core.business.model.tracciati.operazioni.AbstractOperazioneResponse;
import it.govpay.core.business.model.tracciati.operazioni.CaricamentoRequest;
import it.govpay.core.business.model.tracciati.operazioni.CaricamentoResponse;
import it.govpay.core.business.model.tracciati.operazioni.OperazioneFactory;
import it.govpay.core.utils.tracciati.TracciatiUtils;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.orm.IdTracciato;

public class CaricamentoTracciatoThread implements Runnable {
	
	private List<CaricamentoRequest> richieste = null;
	private boolean completed = false;
	private static Logger log = LoggerWrapperFactory.getLogger(CaricamentoTracciatoThread.class);
	private IdTracciato idTracciato = null;
	private List<Long> lineeElaborate = null;
	private int numeroAddElaborateOk = 0;
	private int numeroAddElaborateKo = 0;
	private int numeroDelElaborateOk = 0;
	private int numeroDelElaborateKo = 0;
	private String descrizioneEsito = null;
	private List<AbstractOperazioneResponse> risposte = null;
	private IContext ctx = null;
	
	public CaricamentoTracciatoThread(List<CaricamentoRequest> richieste, IdTracciato idTracciato, IContext ctx) {
		this.richieste = richieste;
		this.idTracciato = idTracciato;
		this.ctx = ctx;
	}

	@Override
	public void run() {
		ContextThreadLocal.set(this.ctx);
		
		this.lineeElaborate = new ArrayList<>();
		this.risposte = new ArrayList<AbstractOperazioneResponse>();
		OperazioneFactory factory = new OperazioneFactory();
		BasicBD bd = null;
		try {
			bd = setupConnection(bd);
			OperazioniBD operazioniBD = new OperazioniBD(bd);
			log.debug("Elaborazione di " + this.richieste.size() + " operazioni...");
			
			for (CaricamentoRequest request : this.richieste) {
				try {
					// check operazione gia' presente
					Operazione operazione = null;
					boolean created = false;
					try {
						operazione = operazioniBD.getOperazione(this.idTracciato.getId(), request.getLinea());
					}catch(NotFoundException e) {
						operazione = new Operazione();
						created = true;
					}
					
					AbstractOperazioneResponse operazioneResponse = factory.elaboraLineaCSV(request, bd);
					
					bd.setAutoCommit(false);

					operazione.setCodVersamentoEnte(operazioneResponse.getIdPendenza());
					operazione.setDatiRichiesta(operazioneResponse.getJsonRichiesta().getBytes());
					operazione.setDatiRisposta(operazioneResponse.getEsitoOperazionePendenza().toJSON(null).getBytes());
					operazione.setStato(operazioneResponse.getStato());
					TracciatiUtils.setDescrizioneEsito(operazioneResponse, operazione);
					TracciatiUtils.setApplicazione(operazioneResponse, operazione, bd);
					operazione.setIdTracciato(idTracciato.getId());
					operazione.setLineaElaborazione(operazioneResponse.getNumero());
					operazione.setCodDominio(request.getCodDominio());
					
					TipoOperazioneType tipoOperazioneType = operazioneResponse.getTipo();
					
					operazione.setTipoOperazione(tipoOperazioneType);
					
					if(tipoOperazioneType.equals(TipoOperazioneType.ADD)) {
						CaricamentoResponse caricamentoResponse = (CaricamentoResponse) operazioneResponse;
						operazione.setIdStampa(caricamentoResponse.getIdStampa());
						operazione.setIdVersamento(caricamentoResponse.getIdVersamento());
					} else {
						
					}
					
					if(created) {
						operazioniBD.insertOperazione(operazione);
					} else {
						operazioniBD.updateOperazione(operazione);
					}
					
					if(operazione.getStato().equals(StatoOperazioneType.ESEGUITO_OK)) {
						if(tipoOperazioneType.equals(TipoOperazioneType.ADD)) {
							this.numeroAddElaborateOk ++;
						} else {
							this.numeroDelElaborateOk ++;
						}
						
					} else {
						if(!operazioneResponse.getEsito().equals(CostantiCaricamento.EMPTY.toString())) {
							if(tipoOperazioneType.equals(TipoOperazioneType.ADD)) {
								this.numeroAddElaborateKo ++;
							} else {
								this.numeroDelElaborateKo ++;
							}
							this.descrizioneEsito = operazioneResponse.getDescrizioneEsito();
						}
					}
					
					this.lineeElaborate.add(request.getLinea());
					this.risposte.add(operazioneResponse);
					log.debug("Inserimento Pendenza Numero ["+ (request.getLinea() -1) + "] elaborata con esito [" +operazione.getStato() + "]: " + operazione.getDettaglioEsito() + " Raw: [" + new String(request.getDati()) + "]");
					bd.commit();
				}catch(ServiceException e) {
					log.error("Errore durante il salvataggio l'accesso alla base dati: " + e.getMessage());
				} finally {
					
				}
			}
		}catch(ServiceException e) {
			log.error("Errore durante il salvataggio l'accesso alla base dati: " + e.getMessage());
			
		} finally {
			this.completed = true;
			if(bd != null) bd.closeConnection(); 
			
			log.debug("Linee elaborate: " + this.lineeElaborate.size());
			log.debug("Risposte prodotte: " + this.risposte.size());
			ContextThreadLocal.unset();
		}
		
	}
	
	public boolean isCompleted() {
		return this.completed;
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

	public List<Long> getLineeElaborate() {
		return lineeElaborate;
	}

	public int getNumeroAddElaborateOk() {
		return numeroAddElaborateOk;
	}

	public int getNumeroAddElaborateKo() {
		return numeroAddElaborateKo;
	}

	public int getNumeroDelElaborateOk() {
		return numeroDelElaborateOk;
	}

	public int getNumeroDelElaborateKo() {
		return numeroDelElaborateKo;
	}

	public String getDescrizioneEsito() {
		return descrizioneEsito;
	}

	public List<AbstractOperazioneResponse> getRisposte() {
		return risposte;
	}

}
