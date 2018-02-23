package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.bd.model.Psp;

public class ListaPspDTOResponse  extends BasicFindResponseDTO<Psp> {

		public ListaPspDTOResponse(long totalResults, List<Psp> results) {
			super(totalResults, results);
		}


}
