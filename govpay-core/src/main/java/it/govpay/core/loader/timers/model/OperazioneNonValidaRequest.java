package it.govpay.core.loader.timers.model;

import org.openspcoop2.generic_project.exception.ValidationException;

import it.govpay.model.loader.Operazione.TipoOperazioneType;


public class OperazioneNonValidaRequest extends AbstractOperazioneRequest {

	private String dettaglioErrore;
	
	public OperazioneNonValidaRequest() throws ValidationException {
		super(TipoOperazioneType.N_V, null);
	}

	public String getDettaglioErrore() {
		return dettaglioErrore;
	}

	public void setDettaglioErrore(String dettaglioErrore) {
		this.dettaglioErrore = dettaglioErrore;
	}
	
}
