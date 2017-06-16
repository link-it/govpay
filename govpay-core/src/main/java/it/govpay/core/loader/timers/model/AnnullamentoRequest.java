package it.govpay.core.loader.timers.model;

import it.govpay.model.loader.Operazione.TipoOperazioneType;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.csv.Parser;
import org.openspcoop2.utils.csv.Record;


public class AnnullamentoRequest extends AbstractOperazioneRequest {

	private String motivoAnnullamento;
	
	public AnnullamentoRequest(Parser parser, Record record){
		super(TipoOperazioneType.DEL, parser, record);
		try {
			this.setMotivoAnnullamento(validaESettaRecord(record, "motivoAnnullamento", 35, null, false));
		} catch(ValidationException e) {
			this.setValid(false);
		}
	}

	public String getMotivoAnnullamento() {
		return motivoAnnullamento;
	}

	public void setMotivoAnnullamento(String motivoAnnullamento) {
		this.motivoAnnullamento = motivoAnnullamento;
	}

}
