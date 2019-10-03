package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.viste.model.VersamentoIncasso;

public class GetAvvisoDTOResponse {
	
	private byte[] avvisoPdf;
	private String filenameAvviso;
	private VersamentoIncasso versamento;
	private Dominio dominio;
	private String barCode;
	private String qrCode;
	private boolean found;
	
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

	public VersamentoIncasso getVersamento() {
		return versamento;
	}

	public void setVersamento(VersamentoIncasso versamento) {
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

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

}
