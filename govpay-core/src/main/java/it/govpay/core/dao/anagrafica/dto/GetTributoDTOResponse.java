package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IbanAccredito;
import it.govpay.bd.model.Tributo;

public class GetTributoDTOResponse {
	
	private Tributo tributo;
	private IbanAccredito ibanAccreditoPostale;
	
	public GetTributoDTOResponse(Tributo tributo, IbanAccredito ibanAccreditoPostale) {
		this.tributo = tributo;
	}

	public Tributo getTributo() {
		return tributo;
	}

	public IbanAccredito getIbanAccreditoPostale() {
		return ibanAccreditoPostale;
	}

	public void setIbanAccreditoPostale(IbanAccredito ibanAccreditoPostale) {
		this.ibanAccreditoPostale = ibanAccreditoPostale;
	}

}
