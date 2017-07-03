package it.govpay.core.utils.tracciati.operazioni;

import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.utils.csv.Record;

import it.govpay.core.utils.Utils;

public class CaricamentoResponse extends AbstractOperazioneResponse {
	
	public static final String ESITO_CARICAMENTO_OK = "OK";

	public CaricamentoResponse() {
	}
	
	public CaricamentoResponse(Record record) throws ValidationException{
		this.setEsito(Utils.validaESettaRecord(record,"esito",null, null, false));
		if(this.getEsito().equals(ESITO_CARICAMENTO_OK))
			this.setIuv(Utils.validaESettaRecord(record,"descrizioneEsito",null, null, false));
		else 
			this.setDescrizioneEsito(Utils.validaESettaRecord(record,"descrizioneEsito",null, null, false));
		
		String qrCode = Utils.validaESettaRecord(record,"qrCode",null, null, true);
		this.setQrCode(qrCode != null ? qrCode.getBytes() : null);
		
		String barCode = Utils.validaESettaRecord(record,"barCode",null, null, true);
		this.setBarCode(barCode != null ? barCode.getBytes() : null);
	}
	
	private String iuv;
	
	private byte[] qrCode;
	private byte[] barCode;

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
		case ESEGUITO_KO: return (this.getEsito() + this.getDelim() + this.getDescrizioneEsito()).getBytes();
		case ESEGUITO_OK: return (ESITO_CARICAMENTO_OK + this.getDelim() + this.iuv + this.getDelim() + new String(this.qrCode) + this.getDelim() + new String(this.barCode)).getBytes(); 
		default: return "".getBytes();
		}
	}


}
