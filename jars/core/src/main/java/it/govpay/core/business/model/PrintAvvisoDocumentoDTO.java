package it.govpay.core.business.model;

import it.govpay.bd.model.Documento;

public class PrintAvvisoDocumentoDTO {
	
	private Documento documento;
	private boolean update;
	
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
}
