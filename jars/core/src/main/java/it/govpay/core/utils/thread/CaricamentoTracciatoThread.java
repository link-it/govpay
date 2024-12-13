/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Operazione;
import it.govpay.bd.pagamento.OperazioniBD;
import it.govpay.core.business.model.tracciati.CostantiCaricamento;
import it.govpay.core.business.model.tracciati.operazioni.AbstractOperazioneResponse;
import it.govpay.core.business.model.tracciati.operazioni.CaricamentoRequest;
import it.govpay.core.business.model.tracciati.operazioni.CaricamentoResponse;
import it.govpay.core.business.model.tracciati.operazioni.OperazioneFactory;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.LogUtils;
import it.govpay.core.utils.tracciati.TracciatiPendenzeManager;
import it.govpay.core.utils.tracciati.TracciatiUtils;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.orm.IdTracciato;

public class CaricamentoTracciatoThread implements Runnable {
	
	private List<CaricamentoRequest> richieste = null;
	private boolean completed = false;
	private boolean commit = false;
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
	private TracciatiPendenzeManager manager = null;
	private String nomeThread = "";
	
	public CaricamentoTracciatoThread(List<CaricamentoRequest> richieste, IdTracciato idTracciato, String id, TracciatiPendenzeManager manager, IContext ctx) {
		this.richieste = richieste;
		this.idTracciato = idTracciato;
		this.ctx = ctx;
		this.manager = manager;
		this.nomeThread = id;
	}

	@Override
	public void run() {
		ContextThreadLocal.set(this.ctx);
		MDC.put(MD5Constants.TRANSACTION_ID, ctx.getTransactionId());
		BDConfigWrapper configWrapper = new BDConfigWrapper(this.ctx.getTransactionId(), true);
		this.lineeElaborate = new ArrayList<>();
		this.risposte = new ArrayList<>();
		OperazioneFactory factory = new OperazioneFactory();
		OperazioniBD operazioniBD = null;
		try {
			operazioniBD = new OperazioniBD(configWrapper);
			
			operazioniBD.setupConnection(configWrapper.getTransactionID());
			
			operazioniBD.setAtomica(false);
			
			operazioniBD.setAutoCommit(false);
			
			LogUtils.logDebug(log, this.nomeThread + ": Elaborazione di " + this.richieste.size() + " operazioni...");
			
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
					
					AbstractOperazioneResponse operazioneResponse = factory.elaboraLineaCSV(request, this.manager, this.nomeThread, operazioniBD);
					
					operazione.setCodVersamentoEnte(operazioneResponse.getIdPendenza());
					operazione.setDatiRichiesta(operazioneResponse.getJsonRichiesta().getBytes());
					operazione.setDatiRisposta(operazioneResponse.getEsitoOperazionePendenza().toJSON(null).getBytes());
					operazione.setStato(operazioneResponse.getStato());
					TracciatiUtils.setDescrizioneEsito(operazioneResponse, operazione);
					TracciatiUtils.setApplicazione(operazioneResponse, operazione, configWrapper);
					operazione.setIdTracciato(idTracciato.getId());
					operazione.setLineaElaborazione(operazioneResponse.getNumero());
					operazione.setCodDominio(request.getCodDominio());
					
					TipoOperazioneType tipoOperazioneType = operazioneResponse.getTipo();
					
					operazione.setTipoOperazione(tipoOperazioneType);
					
					if(tipoOperazioneType.equals(TipoOperazioneType.ADD)) {
						CaricamentoResponse caricamentoResponse = (CaricamentoResponse) operazioneResponse;
						operazione.setIdStampa(caricamentoResponse.getIdStampa());
						operazione.setIdVersamento(caricamentoResponse.getIdVersamento());
					}
					
					if(created) {
						LogUtils.logDebug(log, this.nomeThread + " Inserimento operazione ["+ (operazione.getLineaElaborazione()) + "] nel DB in corso...");
						operazioniBD.insertOperazione(operazione);
						LogUtils.logDebug(log, this.nomeThread + " Inserimento operazione ["+ (operazione.getLineaElaborazione()) + "] nel DB completato.");
					} else {
						LogUtils.logDebug(log, this.nomeThread + " Update operazione ["+ (operazione.getLineaElaborazione()) + "] nel DB in corso...");
						operazioniBD.updateOperazione(operazione);
						LogUtils.logDebug(log, this.nomeThread + " Update operazione ["+ (operazione.getLineaElaborazione()) + "] nel DB completato.");
					}
					
					if(operazione.getStato().equals(StatoOperazioneType.ESEGUITO_OK)) {
						if(tipoOperazioneType.equals(TipoOperazioneType.ADD)) {
							this.numeroAddElaborateOk ++;
						} else {
							this.numeroDelElaborateOk ++;
						}
						
					} else {
						if(!operazioneResponse.getEsito().equals(CostantiCaricamento.EMPTY)) {
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
					operazioniBD.commit();
					LogUtils.logDebug(log, this.nomeThread + " Inserimento Pendenza Numero ["+ (request.getLinea() -1) + "] elaborata con esito [" +operazione.getStato() + "]: " + operazione.getDettaglioEsito() + " Raw: [" + new String(request.getDati()) + "]");
				}catch(ServiceException e) {
					LogUtils.logError(log, this.nomeThread + " Errore durante il salvataggio l'accesso alla base dati: " + e.getMessage());
				} catch (IOException e) {
					LogUtils.logError(log, this.nomeThread + " Errore durante la serializzazione dei dati della risposta: " + e.getMessage());
				} finally {
					//donothing
				}
			}
		}catch(ServiceException e) {
			LogUtils.logError(log, this.nomeThread + " Errore durante il salvataggio l'accesso alla base dati: " + e.getMessage());
			
		} finally {
			this.completed = true;
			if(operazioniBD != null) operazioniBD.closeConnection(); 
			
			LogUtils.logDebug(log, this.nomeThread + " Linee elaborate: " + this.lineeElaborate.size());
			LogUtils.logDebug(log, this.nomeThread + " Risposte prodotte: " + this.risposte.size());
			ContextThreadLocal.unset();
		}
		
	}
	
	public boolean isCompleted() {
		return this.completed;
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

	public boolean isCommit() {
		return commit;
	}

	public void setCommit(boolean commit) {
		this.commit = commit;
	}
	
	public String getNomeThread() {
		return nomeThread;
	}
}
