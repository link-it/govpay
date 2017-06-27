package it.govpay.core.loader.timers.model;

import it.govpay.model.loader.Operazione.TipoOperazioneType;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.csv.Record;


public class AnnullamentoRequest extends AbstractOperazioneRequest {

	private String motivoAnnullamento;
	private String codApplicazione;
	private String codVersamentoEnte;

	public AnnullamentoRequest(Record record) throws ValidationException{
		super(TipoOperazioneType.DEL, record);
		this.setMotivoAnnullamento(validaESettaRecord(record, "motivoAnnullamento", 35, null, false));
		this.setCodApplicazione(validaESettaRecord(record, "codApplicazione", 35, null, false));
		this.setCodVersamentoEnte(validaESettaRecord(record, "codiceVersamentoEnte", 35, null, false));
	}

	public String getMotivoAnnullamento() {
		return motivoAnnullamento;
	}

	public void setMotivoAnnullamento(String motivoAnnullamento) {
		this.motivoAnnullamento = motivoAnnullamento;
	}
	public String getCodApplicazione() {
		return codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}
	public String getCodVersamentoEnte() {
		return codVersamentoEnte;
	}

	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}

}
