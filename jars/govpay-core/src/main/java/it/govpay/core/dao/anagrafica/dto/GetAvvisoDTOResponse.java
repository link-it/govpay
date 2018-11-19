package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Versamento;

public class GetAvvisoDTOResponse {
	
	private byte[] avvisoPdf;
	private String filenameAvviso;
	private Versamento versamento;
	private Dominio dominio;
	private String barCode;
	private String qrCode;
	
	public GetAvvisoDTOResponse() {
	}

	public byte[] getAvvisoPdf() {
		return avvisoPdf;
	}

	public void setAvvisoPdf(byte[] avviso) {
		this.avvisoPdf = avviso;
	}

	public String getFilenameAvviso() {
		return filenameAvviso;
	}

	public void setFilenameAvviso(String filenameAvviso) {
		this.filenameAvviso = filenameAvviso;
	}

	public Versamento getVersamento() {
		return versamento;
	}

	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}

	public Dominio getDominio() {
		return dominio;
	}

	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

}
