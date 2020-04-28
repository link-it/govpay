package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Versamento;

public class GetDocumentoAvvisiDTOResponse {
	
	private byte[] documentoPdf;
	private String filenameDocumento;
	private Versamento versamento;
	private Documento documento;
	private Dominio dominio;
	private String barCode;
	private String qrCode;
	private boolean found;
	private Applicazione applicazione;
	
	public GetDocumentoAvvisiDTOResponse() {
	}

	public byte[] getDocumentoPdf() {
		return documentoPdf;
	}

	public void setDocumentoPdf(byte[] documentoPdf) {
		this.documentoPdf = documentoPdf;
	}

	public String getFilenameDocumento() {
		return filenameDocumento;
	}

	public void setFilenameDocumento(String filenameDocumento) {
		this.filenameDocumento = filenameDocumento;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
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

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public Applicazione getApplicazione() {
		return applicazione;
	}

	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}

}
