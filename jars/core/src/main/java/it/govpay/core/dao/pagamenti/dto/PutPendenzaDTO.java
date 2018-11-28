package it.govpay.core.dao.pagamenti.dto;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;
import it.govpay.core.dao.commons.Versamento;

public class PutPendenzaDTO extends BasicCreateRequestDTO  {
	
	private Versamento versamento;
	private boolean stampaAvviso;
	private boolean avvisaturaDigitale;
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
		return this.stampaAvviso;
	}

	public void setStampaAvviso(boolean stampaAvviso) {
		this.stampaAvviso = stampaAvviso;
	}

	public boolean isAvvisaturaDigitale() {
		return this.avvisaturaDigitale;
	}

	public void setAvvisaturaDigitale(boolean avvisaturaDigitale) {
		this.avvisaturaDigitale = avvisaturaDigitale;
	}

}
