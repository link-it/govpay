package it.govpay.core.utils.tracciati.operazioni;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.csv.Record;

import it.govpay.core.utils.Utils;

public class AnnullamentoResponse extends AbstractOperazioneResponse {
	
	public AnnullamentoResponse() {	}
	
	public static final String ESITO_DEL_OK = "DEL_OK";
	public static final String ESITO_DEL_KO = "DEL_KO";
	
	public AnnullamentoResponse(Record record) throws ValidationException{
		this.setEsito(Utils.validaESettaRecord(record,"esito",null, null, false));
		this.setCodApplicazione(Utils.validaESettaRecord(record, "codApplicazione", null, null, false));
		this.setCodVersamentoEnte(Utils.validaESettaRecord(record, "codVersamentoEnte", null, null, false));
		this.setDescrizioneEsito(Utils.validaESettaRecord(record,"descrizioneEsito",null, null, true));
	}
	
	private String codApplicazione;
	private String codVersamentoEnte;

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
	
	@Override
	protected byte[] createDati() {
		switch(this.getStato()) {
		case ESEGUITO_KO: return (ESITO_DEL_KO + this.getDelim() + this.codApplicazione + this.getDelim() + this.codVersamentoEnte + this.getDelim() + this.getDescrizioneEsito()).getBytes();
		case ESEGUITO_OK: return (ESITO_DEL_OK + this.getDelim() + this.codApplicazione + this.getDelim() + this.codVersamentoEnte).getBytes();
		default: return "DEL_INTERNAL: STATO NON VALIDO".getBytes();
		}
	}

}
