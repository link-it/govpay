package it.govpay.core.business.model;

import it.govpay.bd.model.Versamento;

public class PrintAvvisoVersamentoDTO {
	
	private Versamento versamento;
	private String codDominio;
	private String iuv;
	private boolean update;

	public Versamento getVersamento() {
		return versamento;
	}
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public boolean isUpdate() {
		return update;
	}
	public void setUpdate(boolean update) {
		this.update = update;
	}

	
}
