package it.govpay.core.utils.tracciati.operazioni;

import it.govpay.model.Operazione.StatoOperazioneType;

public class OperazioneNonValidaResponse extends AbstractOperazioneResponse {

	@Override
	protected byte[] createDati() {
		switch(this.getStato()) {
		case NON_VALIDO: return (StatoOperazioneType.NON_VALIDO + this.getDelim() + this.getDescrizioneEsito()).getBytes();
		default: return "".getBytes();
		}
	}

}
