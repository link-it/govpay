package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.Tributo;
import it.govpay.model.IbanAccredito;

public class GetTributoDTOResponse {
	
	private Tributo tributo;
	private IbanAccredito ibanAccredito;
	private IbanAccredito ibanAppoggio;
	
	public GetTributoDTOResponse(Tributo tributo, IbanAccredito ibanAccredito, IbanAccredito ibanAppoggio) {
		this.tributo = tributo;
		this.ibanAccredito = ibanAccredito;
		this.ibanAppoggio = ibanAppoggio;
	}

	public Tributo getTributo() {
		return this.tributo;
	}

	public IbanAccredito getIbanAccredito() {
		return this.ibanAccredito;
	}

	public void setIbanAccredito(IbanAccredito ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}

	public IbanAccredito getIbanAppoggio() {
		return this.ibanAppoggio;
	}

	public void setIbanAppoggio(IbanAccredito ibanAppoggio) {
		this.ibanAppoggio = ibanAppoggio;
	}

}
