package it.govpay.core.loader.timers.model;

import it.govpay.model.loader.Operazione.TipoOperazioneType;


public class OperazioneNonValidaRequest extends AbstractOperazioneRequest {

	public OperazioneNonValidaRequest() {
		super(TipoOperazioneType.N_A, null, null);
		this.setValid(false);
	}
	
}
