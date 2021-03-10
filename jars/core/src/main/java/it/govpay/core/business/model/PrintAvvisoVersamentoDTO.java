package it.govpay.core.business.model;

import it.govpay.bd.model.Versamento;

public class PrintAvvisoVersamentoDTO {
	
	public enum LinguaSecondaria { DE, EN, FR, SL; }
	
	private Versamento versamento;
	private String codDominio;
	private String iuv;
	private boolean update;
	private boolean salvaSuDB = true;
	private boolean linguaSecondariaAbilitata = false;
	private LinguaSecondaria linguaSecondaria = null;

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
	public boolean isSalvaSuDB() {
		return salvaSuDB;
	}
	public void setSalvaSuDB(boolean salvaSuDB) {
		this.salvaSuDB = salvaSuDB;
	}
	public boolean isLinguaSecondariaAbilitata() {
		return linguaSecondariaAbilitata;
	}
	public void setLinguaSecondariaAbilitata(boolean linguaSecondariaAbilitata) {
		this.linguaSecondariaAbilitata = linguaSecondariaAbilitata;
	}
	public LinguaSecondaria getLinguaSecondaria() {
		return linguaSecondaria;
	}
	public void setLinguaSecondaria(LinguaSecondaria linguaSecondaria) {
		this.linguaSecondaria = linguaSecondaria;
	}
	
}
