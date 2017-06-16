package it.govpay.core.loader.timers.model;



public class CaricamentoResponse extends AbstractOperazioneResponse {
	
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
		case ESEGUITO_KO: return this.getDescrizioneEsito().getBytes();
		case ESEGUITO_OK: return ("OK;" + this.iuv + ";" + this.qrCode + ";" + this.barCode).getBytes();
		default: return "".getBytes();
		}
	}


}
