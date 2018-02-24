package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.bd.model.Psp;

public class ListaIntermediariDTOResponse  extends BasicFindResponseDTO<Psp> {

		public ListaIntermediariDTOResponse(long totalResults, List<Psp> results) {
			super(totalResults, results);
		}


}
