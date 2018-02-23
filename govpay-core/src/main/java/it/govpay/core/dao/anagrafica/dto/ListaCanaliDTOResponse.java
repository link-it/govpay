package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.bd.model.Canale;

public class ListaCanaliDTOResponse  extends BasicFindResponseDTO<Canale> {

		public ListaCanaliDTOResponse(long totalResults, List<Canale> results) {
			super(totalResults, results);
		}


}
