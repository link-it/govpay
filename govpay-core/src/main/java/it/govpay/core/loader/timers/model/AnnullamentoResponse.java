package it.govpay.core.loader.timers.model;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.csv.Record;

import it.govpay.core.utils.Utils;

public class AnnullamentoResponse extends AbstractOperazioneResponse {
	
	public AnnullamentoResponse() {	}
	
	public AnnullamentoResponse(Record record) throws ValidationException{
		this.setEsito(Utils.validaESettaRecord(record,"esito",null, null, false));
		this.setDescrizioneEsito(Utils.validaESettaRecord(record,"descrizioneEsito",null, null, true));
	}

	@Override
	protected byte[] createDati() {
		switch(this.getStato()) {
		case ESEGUITO_KO: return (this.getEsito() + this.getDelim() + this.getDescrizioneEsito()).getBytes();
		case ESEGUITO_OK: return "OK".getBytes();
		default: return "".getBytes();
		}
	}

}
