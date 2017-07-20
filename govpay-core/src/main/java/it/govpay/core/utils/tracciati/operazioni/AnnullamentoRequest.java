package it.govpay.core.utils.tracciati.operazioni;

import it.govpay.core.utils.Utils;
import it.govpay.model.Operazione.TipoOperazioneType;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.csv.Record;


public class AnnullamentoRequest extends AbstractOperazioneRequest {

	private String motivoAnnullamento;
	private String codApplicazione;
	private String codVersamentoEnte;

	public AnnullamentoRequest(Record record) throws ValidationException{
		super(TipoOperazioneType.DEL);
		this.setMotivoAnnullamento(Utils.validaESettaRecord(record, "motivoAnnullamento", 35, null, false));
		this.setCodApplicazione(Utils.validaESettaRecord(record, "codApplicazione", 35, null, false));
		this.setCodVersamentoEnte(Utils.validaESettaRecord(record, "codiceVersamentoEnte", 35, null, false));
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
