package it.govpay.core.utils.tracciati.operazioni;

import it.govpay.model.Operazione.TipoOperazioneType;


public class OperazioneNonValidaRequest extends AbstractOperazioneRequest {

	private String codiceErrore;
	private String dettaglioErrore;
	
	public OperazioneNonValidaRequest() {
		super(TipoOperazioneType.N_V);
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
