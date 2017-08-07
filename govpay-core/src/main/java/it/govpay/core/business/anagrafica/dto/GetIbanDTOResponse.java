package it.govpay.core.business.anagrafica.dto;

import it.govpay.bd.model.IbanAccredito;

public class GetIbanDTOResponse {
	
	private IbanAccredito ibanAccredito;
	
	public GetIbanDTOResponse(IbanAccredito ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}

	public IbanAccredito getIbanAccredito() {
		return ibanAccredito;
	}

}
