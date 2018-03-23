package it.govpay.core.dao.pagamenti.dto;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Versamento;
import it.govpay.core.dao.anagrafica.dto.BasicCreateResponseDTO;

public class PutPendenzaDTOResponse extends BasicCreateResponseDTO {

	private Versamento versamento;
	private Dominio dominio;
	private String qrCode; 
	private String barCode;
	
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
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

}
