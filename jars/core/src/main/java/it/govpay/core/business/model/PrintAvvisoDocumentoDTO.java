package it.govpay.core.business.model;

import java.text.SimpleDateFormat;
import java.util.List;

import it.govpay.bd.model.Documento;
import it.govpay.core.beans.tracciati.LinguaSecondaria;

public class PrintAvvisoDocumentoDTO {
	
	private Documento documento;
	private boolean update;
	private boolean salvaSuDB = true;
	private LinguaSecondaria linguaSecondaria = null;
	private List<String> numeriAvviso = null;
	private SimpleDateFormat sdfDataScadenza;
	
	public Documento getDocumento() {
		return documento;
	}
	public void setDocumento(Documento documento) {
		this.documento = documento;
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
	public List<String> getNumeriAvviso() {
		return numeriAvviso;
	}
	public void setNumeriAvviso(List<String> numeriAvviso) {
		this.numeriAvviso = numeriAvviso;
	}
	public SimpleDateFormat getSdfDataScadenza() {
		return sdfDataScadenza;
	}
	public void setSdfDataScadenza(SimpleDateFormat sdfDataScadenza) {
		this.sdfDataScadenza = sdfDataScadenza;
	}
}
