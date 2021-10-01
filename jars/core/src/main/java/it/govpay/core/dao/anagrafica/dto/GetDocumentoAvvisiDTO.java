package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import org.springframework.security.core.Authentication;

import it.govpay.core.beans.tracciati.LinguaSecondaria;

public class GetDocumentoAvvisiDTO extends BasicRequestDTO {
	
	public enum FormatoDocumento  {PDF, JSON};
	
	private String codDominio;
	private String numeroDocumento;
	private String codApplicazione;
	private FormatoDocumento formato;
	private LinguaSecondaria linguaSecondaria = null;
	private List<String> numeriAvviso = null;
	
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
	public String getCodApplicazione() {
		return codApplicazione;
	}
	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}
}
