package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

public class ListaCanaliDTOResponse  extends BasicFindResponseDTO<LeggiCanaleDTOResponse> {

		public ListaCanaliDTOResponse(long totalResults, List<LeggiCanaleDTOResponse> results) {
			super(totalResults, results);
		}


}
