package it.govpay.core.loader.utils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.loader.OperazioniBD;
import it.govpay.bd.loader.model.Operazione;
import it.govpay.bd.loader.model.Tracciato;
import it.govpay.core.loader.timers.model.AbstractOperazioneRequest;
import it.govpay.core.loader.timers.model.AbstractOperazioneResponse;

import java.util.List;
import java.util.concurrent.Callable;

public class TimerCaricamentoTracciatoCore implements Callable<Integer> {

	private AcquisizioneUtils acquisizioneUtils;
	private List<byte[]> lineaLst;
	private long lineaIniziale;
	private Tracciato tracciato;
	private BasicBD bd;
	
	public TimerCaricamentoTracciatoCore() {
		this.acquisizioneUtils = new AcquisizioneUtils();
	}

	public Integer call() throws Exception {
		int numLineeElaborate = 0;
		long numLinea = this.lineaIniziale;
		for(byte[] linea: this.lineaLst) {
			AbstractOperazioneRequest request = this.acquisizioneUtils.acquisisci(linea, this.tracciato, numLinea++);

			if(request != null) {
				AbstractOperazioneResponse response = this.acquisizioneUtils.acquisisciResponse(request, this.tracciato, this.bd);
				String codApplicazione = this.acquisizioneUtils.getCodiceApplicazione(request);
				String codVersamentoEnte = this.acquisizioneUtils.getCodVersamentoEnte(request);
				
				OperazioniBD op = new OperazioniBD(this.bd);
				Operazione operazione = new Operazione();
				operazione.setCodVersamentoEnte(codVersamentoEnte);
				operazione.setDatiRichiesta(linea);
				operazione.setDatiRisposta(response.getDati());
				operazione.setStato(response.getStato());
				if(response.getDescrizioneEsito() != null)
					operazione.setDettaglioEsito(response.getDescrizioneEsito().length() > 255 ? response.getDescrizioneEsito().substring(0, 255) : response.getDescrizioneEsito());
				
				if(codApplicazione != null)
					operazione.setIdApplicazione(AnagraficaManager.getApplicazione(this.bd, codApplicazione).getId());
				
				operazione.setIdTracciato(request.getIdTracciato());
				operazione.setLineaElaborazione(request.getLinea());
				operazione.setTipoOperazione(request.getTipoOperazione());
				op.insertOperazione(operazione);
			}
			numLineeElaborate++;

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
