package it.govpay.core.utils.tracciati.operazioni;

import org.openspcoop2.generic_project.exception.ValidationException;

import it.govpay.model.Operazione.TipoOperazioneType;


public class OperazioneNonValidaRequest extends AbstractOperazioneRequest {

	private String codiceErrore;
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

	public String getCodiceErrore() {
		return codiceErrore;
	}

	public void setCodiceErrore(String codiceErrore) {
		this.codiceErrore = codiceErrore;
	}
	
}
