package it.govpay.core.business.model;

import it.govpay.bd.model.Documento;

public class PrintAvvisoDocumentoDTO {
	
	private Documento documento;
	private boolean update;
	private boolean salvaSuDB = true;
	
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
}
