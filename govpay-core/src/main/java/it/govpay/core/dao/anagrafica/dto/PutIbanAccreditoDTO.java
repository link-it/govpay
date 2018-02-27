package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.IbanAccredito;
import it.govpay.model.IAutorizzato;

public class PutIbanAccreditoDTO extends BasicCreateRequestDTO  {
	
	private IbanAccredito iban;
	private String idDominio;
	private String ibanAccredito;
	
	public PutIbanAccreditoDTO(IAutorizzato user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	public IbanAccredito getIban() {
		return iban;
	}

	public void setIban(IbanAccredito iban) {
		this.iban = iban;
	}

	public String getIbanAccredito() {
		return ibanAccredito;
	}

	public void setIbanAccredito(String ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}

	public String getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}

}
