package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

import it.govpay.bd.model.IbanAccredito;

public class PutIbanAccreditoDTO extends BasicCreateRequestDTO  {
	
	private IbanAccredito iban;
	private String idDominio;
	private String ibanAccredito;
	
	public PutIbanAccreditoDTO(Authentication user) {
		super(user);
	}

	public IbanAccredito getIban() {
		return this.iban;
	}

	public void setIban(IbanAccredito iban) {
		this.iban = iban;
	}

	public String getIbanAccredito() {
		return this.ibanAccredito;
	}

	public void setIbanAccredito(String ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}

	public String getIdDominio() {
		return this.idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}

}
