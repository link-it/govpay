package it.govpay.core.utils.tracciati.operazioni;

import it.govpay.core.rs.v1.beans.base.Avviso;

public class CaricamentoResponse extends AbstractOperazioneResponse {

	public static final String ESITO_ADD_OK = "ADD_OK";
	public static final String ESITO_ADD_KO = "ADD_KO";

	public CaricamentoResponse() {

	}
	private String iuv;
	private byte[] qrCode;
	private byte[] barCode;
	private Avviso avviso;

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
	public Avviso getAvviso() {
		return avviso;
	}
	public void setAvviso(Avviso avviso) {
		this.avviso = avviso;
	}

	@Override
	public Object getDati() {
		switch(this.getStato()) {
		case ESEGUITO_KO:
			return this.getFaultBean(); 
		case ESEGUITO_OK:
			return this.getAvviso();
		case NON_VALIDO:
		default:
			break;
		}
		
		return null;
	}
}
