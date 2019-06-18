package it.govpay.core.dao.pagamenti.dto;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;
import it.govpay.core.dao.commons.Versamento;
import it.govpay.model.Versamento.ModoAvvisatura;

public class PutPendenzaDTO extends BasicCreateRequestDTO  {
	
	private String customReq;
	private String codDominio;
	private String codTipoVersamento;
	
	private Versamento versamento;
	private Boolean stampaAvviso;
	private Boolean avvisaturaDigitale;
	private ModoAvvisatura avvisaturaModalita;
	public PutPendenzaDTO(Authentication user) {
		super(user);
	}

	public Versamento getVersamento() {
		return this.versamento;
	}

	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}

	public boolean isStampaAvviso() {
		return this.stampaAvviso != null ? this.stampaAvviso : false;
	}

	public void setStampaAvviso(Boolean stampaAvviso) {
		this.stampaAvviso = stampaAvviso;
	}

	public Boolean getAvvisaturaDigitale() {
		return this.avvisaturaDigitale;
	}

	public void setAvvisaturaDigitale(Boolean avvisaturaDigitale) {
		this.avvisaturaDigitale = avvisaturaDigitale;
	}

	public ModoAvvisatura getAvvisaturaModalita() {
		return avvisaturaModalita;
	}

	public void setAvvisaturaModalita(ModoAvvisatura avvisaturaModalita) {
		this.avvisaturaModalita = avvisaturaModalita;
	}

	public String getCustomReq() {
		return customReq;
	}

	public void setCustomReq(String customReq) {
		this.customReq = customReq;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getCodTipoVersamento() {
		return codTipoVersamento;
	}

	public void setCodTipoVersamento(String codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}
}
