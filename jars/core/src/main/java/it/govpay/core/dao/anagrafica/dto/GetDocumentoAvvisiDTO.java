package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

public class GetDocumentoAvvisiDTO extends BasicRequestDTO {
	
	public enum FormatoDocumento  {PDF, JSON};
	
	private String codDominio;
	private String numeroDocumento;
	private FormatoDocumento formato;
	
	public GetDocumentoAvvisiDTO(Authentication user, String codDominio){
		this(user, codDominio, null);
	}
	public GetDocumentoAvvisiDTO(Authentication user, String codDominio, String numeroDocumento){
		super(user);
		this.setCodDominio(codDominio);
		this.setNumeroDocumento(numeroDocumento);
		this.formato = FormatoDocumento.PDF;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public FormatoDocumento getFormato() {
		return formato;
	}

	public void setFormato(FormatoDocumento formato) {
		this.formato = formato;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	
}
