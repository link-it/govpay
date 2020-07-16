package it.govpay.core.business.model;

import it.govpay.model.Stampa;

public class PrintAvvisoDTOResponse {

	private Stampa avviso;
	private String codDominio;
	private String codDocumento;
	private String numeroAvviso;

	public Stampa getAvviso() {
		return this.avviso;
	}

	public void setAvviso(Stampa avviso) {
		this.avviso = avviso;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getCodDocumento() {
		return codDocumento;
	}

	public void setCodDocumento(String codDocumento) {
		this.codDocumento = codDocumento;
	}

	public String getNumeroAvviso() {
		return numeroAvviso;
	}

	public void setNumeroAvviso(String numeroAvviso) {
		this.numeroAvviso = numeroAvviso;
	}
	
}
