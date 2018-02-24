package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.bd.model.Canale;

public class ListaStazioniDTOResponse  extends BasicFindResponseDTO<Canale> {

		public ListaStazioniDTOResponse(long totalResults, List<Canale> results) {
			super(totalResults, results);
		}


}
