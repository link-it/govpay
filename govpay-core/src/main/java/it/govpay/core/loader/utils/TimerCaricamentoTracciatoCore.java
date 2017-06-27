package it.govpay.core.loader.utils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.loader.OperazioniBD;
import it.govpay.bd.loader.model.Operazione;
import it.govpay.bd.loader.model.Tracciato;
import it.govpay.core.business.PagamentiAttesa;
import it.govpay.core.business.model.CaricaVersamentoDTO;
import it.govpay.core.loader.timers.model.AbstractOperazioneRequest;
import it.govpay.core.loader.timers.model.AbstractOperazioneResponse;
import it.govpay.core.loader.timers.model.AnnullamentoRequest;
import it.govpay.core.loader.timers.model.CaricamentoRequest;
import it.govpay.core.loader.timers.model.OperazioneNonValidaRequest;
import it.govpay.core.loader.timers.model.OperazioneNonValidaResponse;
import it.govpay.model.loader.Operazione.StatoOperazioneType;

import java.util.List;
import java.util.concurrent.Callable;

public class TimerCaricamentoTracciatoCore implements Callable<Integer> {

	private CaricamentoUtils caricamentoUtils;
	private AcquisizioneUtils acquisizioneUtils;
	private List<byte[]> lineaLst;
	private long lineaIniziale;
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
		long numLinea = this.lineaIniziale;
		for(byte[] linea: this.lineaLst) {
			AbstractOperazioneRequest request = this.acquisizioneUtils.acquisisci(linea, this.tracciato, this.tracciato.getOperatore(this.bd), numLinea++, domini, tributi);
			if(request != null) {
				AbstractOperazioneResponse response =  null;
				String codApplicazione = null;
				String codVersamentoEnte = null;
				if(request instanceof CaricamentoRequest) {
					CaricamentoRequest caricamentoRequest = (CaricamentoRequest) request;
					response = this.caricamentoUtils.caricaVersamento(this.tracciato, caricamentoRequest, this.bd);
					codApplicazione = caricamentoRequest.getCodApplicazione();
					codVersamentoEnte = caricamentoRequest.getCodVersamentoEnte();
				} else if(request instanceof AnnullamentoRequest) {
					AnnullamentoRequest annullamentoRequest = (AnnullamentoRequest) request;
					response = this.caricamentoUtils.annullaVersamento(this.tracciato, annullamentoRequest, this.bd);
					codApplicazione = annullamentoRequest.getCodApplicazione();
					codVersamentoEnte = annullamentoRequest.getCodVersamentoEnte();
				} else  if(request instanceof OperazioneNonValidaRequest) {
					OperazioneNonValidaRequest operazioneNonValidaRequest = (OperazioneNonValidaRequest) request;
					response = new OperazioneNonValidaResponse();
					response.setStato(StatoOperazioneType.NON_VALIDO);
					response.setDescrizioneEsito(operazioneNonValidaRequest.getDettaglioErrore());
				}
				
				OperazioniBD op = new OperazioniBD(bd);
				Operazione operazione = new Operazione();
				operazione.setCodVersamentoEnte(codVersamentoEnte);
				operazione.setDatiRichiesta(linea);
				operazione.setDatiRisposta(response.getDati());
				operazione.setStato(response.getStato());
				operazione.setDettaglioEsito(response.getDescrizioneEsito());
				
				if(codApplicazione != null)
					operazione.setIdApplicazione(AnagraficaManager.getApplicazione(bd, codApplicazione).getId());
				
				operazione.setIdTracciato(request.getIdTracciato());
				operazione.setLineaElaborazione(request.getLinea());
				operazione.setTipoOperazione(request.getTipoOperazione());
				op.insertOperazione(operazione);
				numLineeElaborate++;
				
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

	public long getLineaIniziale() {
		return lineaIniziale;
	}

	public void setLineaIniziale(long lineaIniziale) {
		this.lineaIniziale = lineaIniziale;
	}
	
}
