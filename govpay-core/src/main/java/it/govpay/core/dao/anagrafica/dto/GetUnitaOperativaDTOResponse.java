package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.UnitaOperativa;

public class GetUnitaOperativaDTOResponse {
	
	private UnitaOperativa unitaOperativa;
	
	public GetUnitaOperativaDTOResponse(UnitaOperativa unitaOperativa) {
		this.unitaOperativa = unitaOperativa;
	}

	public UnitaOperativa getUnitaOperativa() {
		return unitaOperativa;
	}

}
