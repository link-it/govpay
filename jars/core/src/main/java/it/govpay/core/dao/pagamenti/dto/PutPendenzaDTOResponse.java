package it.govpay.core.dao.pagamenti.dto;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.core.dao.anagrafica.dto.BasicCreateResponseDTO;

public class PutPendenzaDTOResponse extends BasicCreateResponseDTO {

	private Versamento versamento;
	private Dominio dominio;
	private String qrCode; 
	private String barCode;
	private String pdf;
	private UnitaOperativa uo;
	
	public Versamento getVersamento() {
		return this.versamento;
	}
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}
	public Dominio getDominio() {
		return this.dominio;
	}
	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}
	public String getQrCode() {
		return this.qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getBarCode() {
		return this.barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getPdf() {
		return this.pdf;
	}
	public void setPdf(String pdf) {
		this.pdf = pdf;
	}
	public UnitaOperativa getUo() {
		return uo;
	}
	public void setUo(UnitaOperativa uo) {
		this.uo = uo;
	}

}
