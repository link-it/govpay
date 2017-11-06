package it.govpay.core.utils.tracciati.operazioni;

import java.util.ArrayList;
import java.util.List;

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
	protected List<String> listDati() {
		List<String> lst = new ArrayList<String>();
		
		switch(this.getStato()) {
		case ESEGUITO_KO:	lst.add(ESITO_DEL_KO);
							lst.add(this.codApplicazione);
							lst.add(this.codVersamentoEnte);
							lst.add(this.getDescrizioneEsito());
							break;
		case ESEGUITO_OK:	lst.add(ESITO_DEL_KO);
							lst.add(this.codApplicazione);
							lst.add(this.codVersamentoEnte);
							break; 
		default: lst.add("DEL_INTERNAL: STATO NON VALIDO");
		}

		
		return lst;
	}

}
