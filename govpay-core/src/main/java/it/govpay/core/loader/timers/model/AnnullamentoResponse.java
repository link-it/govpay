package it.govpay.core.loader.timers.model;



public class AnnullamentoResponse extends AbstractOperazioneResponse {

	@Override
	protected byte[] createDati() {
		switch(this.getStato()) {
		case ESEGUITO_KO: return (this.getEsito() + this.getDelim() + this.getDescrizioneEsito()).getBytes();
		case ESEGUITO_OK: return "OK".getBytes();
		default: return "".getBytes();
		}
	}

}
