package it.govpay.core.loader.utils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.loader.OperazioniBD;
import it.govpay.bd.loader.model.Operazione;
import it.govpay.bd.loader.model.Tracciato;
import it.govpay.core.loader.timers.model.AbstractOperazioneRequest;
import it.govpay.core.loader.timers.model.AbstractOperazioneResponse;
import it.govpay.core.loader.timers.model.AnnullamentoRequest;
import it.govpay.core.loader.timers.model.CaricamentoRequest;
import it.govpay.core.loader.timers.model.OperazioneNonValidaRequest;
import it.govpay.model.Applicazione;
import it.govpay.model.loader.Operazione.StatoOperazioneType;

import java.util.List;
import java.util.concurrent.Callable;

public class TimerCaricamentoTracciatoCore implements Callable<Integer> {

	private CaricamentoUtils caricamentoUtils;
	private AcquisizioneUtils acquisizioneUtils;
	private List<byte[]> lineaLst;
	private Tracciato tracciato;
	private BasicBD bd;
	
	public TimerCaricamentoTracciatoCore() {
		this.caricamentoUtils = new CaricamentoUtils();
		this.acquisizioneUtils = new AcquisizioneUtils();
	}

	public Integer call() throws Exception {
		int numLineeElaborate = 0;
		List<String> tributi = this.acquisizioneUtils.getTributi(bd);
		List<String> domini = this.acquisizioneUtils.getDomini(bd);
		for(byte[] linea: this.lineaLst) {
			AbstractOperazioneRequest request = this.acquisizioneUtils.acquisisci(linea, this.tracciato, this.tracciato.getOperatore(this.bd), this.tracciato.getLineaElaborazione(), domini, tributi);
			if(request != null) {
				AbstractOperazioneResponse response =  null;
				if(request instanceof CaricamentoRequest) {
					response = this.caricamentoUtils.caricaVersamento((CaricamentoRequest) request, this.bd);
				} else if(request instanceof AnnullamentoRequest) {
					response = this.caricamentoUtils.annullaVersamento((AnnullamentoRequest) request, this.bd);
				} else  if(request instanceof OperazioneNonValidaRequest) {
					OperazioniBD op = new OperazioniBD(bd);
					Operazione operazione = new Operazione();
					operazione.setCodVersamentoEnte(request.getCodVersamentoEnte());
					operazione.setDatiRichiesta(linea);
					operazione.setStato(StatoOperazioneType.NON_VALIDO);
					if(request.getCodApplicazione() != null) {
						Applicazione applicazione = AnagraficaManager.getApplicazione(bd, request.getCodApplicazione());
						if(applicazione != null) {
							operazione.setIdApplicazione(applicazione.getId());
						}
					}
					operazione.setIdTracciato(request.getIdTracciato());
					operazione.setLineaElaborazione(request.getLinea());
					operazione.setTipoOperazione(request.getTipoOperazione());
					op.insertOperazione(operazione);
					numLineeElaborate++;
				}
				
				if(response != null) {
					OperazioniBD op = new OperazioniBD(bd);
					Operazione operazione = new Operazione();
					operazione.setCodVersamentoEnte(request.getCodVersamentoEnte());
					operazione.setDatiRichiesta(linea);
					operazione.setDatiRisposta(response.getDati());
					
					operazione.setStato(response.getStato());
					operazione.setDettaglioEsito(response.getDescrizioneEsito());
					operazione.setIdApplicazione(AnagraficaManager.getApplicazione(bd, request.getCodApplicazione()).getId());
					operazione.setIdTracciato(request.getIdTracciato());
					operazione.setLineaElaborazione(request.getLinea());
					operazione.setTipoOperazione(request.getTipoOperazione());
					op.insertOperazione(operazione);
					numLineeElaborate++;
				}
				
			}
		}
		return numLineeElaborate;
	}

	public void setLineaLst(List<byte[]> lineaLst) {
		this.lineaLst = lineaLst;
	}

	public void setBd(BasicBD bd) {
		this.bd = bd;
	}

	public void setTracciato(Tracciato tracciato) {
		this.tracciato = tracciato;
	}
	
}
