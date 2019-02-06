package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.IuvUtils;

public class GetAvvisoDTO extends BasicRequestDTO {
	
	public enum FormatoAvviso  {PDF, JSON};
	
	private String codDominio;
	private String iuv;
	private String numeroAvviso;
	private FormatoAvviso formato;
	private boolean accessoAnonimo;
	private String cfDebitore;
	
	public GetAvvisoDTO(Authentication user, String codDominio) throws GovPayException {
		this(user, codDominio, null);
	}
	public GetAvvisoDTO(Authentication user, String codDominio, String numeroAvviso) throws GovPayException {
		super(user);
		this.setCodDominio(codDominio);
		this.setNumeroAvviso(numeroAvviso);
		this.setIuv(IuvUtils.toIuv(numeroAvviso));
		this.setAccessoAnonimo(false);
		this.formato = FormatoAvviso.JSON;
	}

	public String getIuv() {
		return this.iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public FormatoAvviso getFormato() {
		return formato;
	}

	public void setFormato(FormatoAvviso formato) {
		this.formato = formato;
	}

	public boolean isAccessoAnonimo() {
		return accessoAnonimo;
	}

	public void setAccessoAnonimo(boolean accessoAnonimo) {
		this.accessoAnonimo = accessoAnonimo;
	}

	public String getCfDebitore() {
		return cfDebitore;
	}

	public void setCfDebitore(String cfDebitore) {
		this.cfDebitore = cfDebitore;
	}

	public String getNumeroAvviso() {
		return numeroAvviso;
	}

	public void setNumeroAvviso(String numeroAvviso) {
		this.numeroAvviso = numeroAvviso;
	}

	
}
