package it.govpay.core.loader.timers.model;


public class OperazioneNonValidaRequest extends AbstractOperazioneRequest {

	public OperazioneNonValidaRequest() {
		super("NON_VALIDA", null, null);
		this.setValid(false);
	}
	
}
