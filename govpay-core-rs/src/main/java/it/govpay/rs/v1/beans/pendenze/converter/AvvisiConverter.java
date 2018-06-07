package it.govpay.rs.v1.beans.pendenze.converter;

import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.rs.v1.beans.Avviso;

public class AvvisiConverter {

	public static Avviso toRsModel(GetAvvisoDTOResponse input) {
		Avviso rsModel = new Avviso();
		return rsModel;
	}

}
