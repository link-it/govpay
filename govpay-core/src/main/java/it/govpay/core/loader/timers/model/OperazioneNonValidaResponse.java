package it.govpay.core.loader.timers.model;



public class OperazioneNonValidaResponse extends AbstractOperazioneResponse {

	@Override
	protected byte[] createDati() {
		switch(this.getStato()) {
		case ESEGUITO_KO: return this.getDescrizioneEsito().getBytes();
		case ESEGUITO_OK: return "OK".getBytes();
		default: return "".getBytes();
		}
	}

}
