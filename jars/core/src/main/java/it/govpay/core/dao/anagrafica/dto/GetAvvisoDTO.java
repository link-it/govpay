package it.govpay.core.dao.anagrafica.dto;

import org.openspcoop2.utils.json.ValidationException;
import org.springframework.security.core.Authentication;

import it.govpay.core.business.model.PrintAvvisoVersamentoDTO.LinguaSecondaria;
import it.govpay.core.utils.IuvUtils;

public class GetAvvisoDTO extends BasicRequestDTO {
	
	public enum FormatoAvviso  {PDF, JSON};
	
	private String codDominio;
	private String iuv;
	private String numeroAvviso;
	private FormatoAvviso formato;
	private String cfDebitore;
	private String identificativoCreazionePendenza;
	private String recaptcha;
	
	private boolean linguaSecondariaAbilitata = false;
	private LinguaSecondaria linguaSecondaria = null;
	
	public GetAvvisoDTO(Authentication user, String codDominio) throws ValidationException {
		this(user, codDominio, null);
	}
	public GetAvvisoDTO(Authentication user, String codDominio, String numeroAvviso) throws ValidationException {
		super(user);
		this.setCodDominio(codDominio);
		this.setNumeroAvviso(numeroAvviso);
		this.setIuv(IuvUtils.toIuv(numeroAvviso));
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
	public String getIdentificativoCreazionePendenza() {
		return identificativoCreazionePendenza;
	}
	public void setIdentificativoCreazionePendenza(String identificativoCreazionePendenza) {
		this.identificativoCreazionePendenza = identificativoCreazionePendenza;
	}
	public String getRecaptcha() {
		return recaptcha;
	}
	public void setRecaptcha(String recaptcha) {
		this.recaptcha = recaptcha;
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
