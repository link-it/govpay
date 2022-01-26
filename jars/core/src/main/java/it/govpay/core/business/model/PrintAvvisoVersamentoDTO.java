package it.govpay.core.business.model;

import java.text.SimpleDateFormat;

import it.govpay.bd.model.Versamento;
import it.govpay.core.beans.tracciati.LinguaSecondaria;

public class PrintAvvisoVersamentoDTO {
	
	private Versamento versamento;
	private String codDominio;
	private String iuv;
	private boolean update;
	private boolean salvaSuDB = true;
	private LinguaSecondaria linguaSecondaria = null;
	private SimpleDateFormat sdfDataScadenza;

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
	public LinguaSecondaria getLinguaSecondaria() {
		return linguaSecondaria;
	}
	public void setLinguaSecondaria(LinguaSecondaria linguaSecondaria) {
		this.linguaSecondaria = linguaSecondaria;
	}
	public SimpleDateFormat getSdfDataScadenza() {
		return sdfDataScadenza;
	}
	public void setSdfDataScadenza(SimpleDateFormat sdfDataScadenza) {
		this.sdfDataScadenza = sdfDataScadenza;
	}
}
