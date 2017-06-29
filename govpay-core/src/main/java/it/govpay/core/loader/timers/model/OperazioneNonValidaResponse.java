package it.govpay.core.loader.timers.model;



public class OperazioneNonValidaResponse extends AbstractOperazioneResponse {

	@Override
	protected byte[] createDati() {
		switch(this.getStato()) {
		case NON_VALIDO: return (this.getEsito() + this.getDelim() + this.getDescrizioneEsito()).getBytes();
		default: return "".getBytes();
		}
	}

}
