package it.govpay.core.utils.tracciati.operazioni;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.csv.Record;

import it.govpay.core.utils.Utils;

public class CaricamentoResponse extends AbstractOperazioneResponse {
	
	public static final String ESITO_ADD_OK = "ADD_OK";
	public static final String ESITO_ADD_KO = "ADD_KO";

	public CaricamentoResponse() {
		
	}
	
	public CaricamentoResponse(Record record) throws ValidationException{
		this.setEsito(Utils.validaESettaRecord(record, "esito", null, null, false));
		this.setCodApplicazione(Utils.validaESettaRecord(record, "codApplicazione", null, null, false));
		this.setCodVersamentoEnte(Utils.validaESettaRecord(record, "codVersamentoEnte", null, null, false));
		
		if(this.getEsito().equals(ESITO_ADD_OK)) {
			this.setIuv(Utils.validaESettaRecord(record,"iuv",null, null, false));
			this.setQrCode(Utils.validaESettaRecord(record,"qrCode",null, null, false).getBytes());
			this.setBarCode(Utils.validaESettaRecord(record,"barCode",null, null, false).getBytes());
		}
		else 
			this.setDescrizioneEsito(Utils.validaESettaRecord(record,"descrizioneEsito",null, null, true));
	}
	
	private String codApplicazione;
	private String codVersamentoEnte;
	private String iuv;
	private byte[] qrCode;
	private byte[] barCode;

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
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public byte[] getQrCode() {
		return qrCode;
	}
	public void setQrCode(byte[] qrCode) {
		this.qrCode = qrCode;
	}
	public byte[] getBarCode() {
		return barCode;
	}
	public void setBarCode(byte[] barCode) {
		this.barCode = barCode;
	}
	
	@Override
	protected byte[] createDati() {
		switch(this.getStato()) {
		case ESEGUITO_KO: return (ESITO_ADD_KO + this.getDelim() + this.codApplicazione + this.getDelim() + this.codVersamentoEnte + this.getDelim() + this.getDescrizioneEsito()).getBytes();
		case ESEGUITO_OK: return (ESITO_ADD_OK + this.getDelim() + this.codApplicazione + this.getDelim() + this.codVersamentoEnte + this.getDelim() + this.iuv + this.getDelim() + new String(this.qrCode) + this.getDelim() + new String(this.barCode)).getBytes(); 
		default: return "".getBytes();
		}
	}

	


}
