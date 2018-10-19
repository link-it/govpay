package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

public class ListaRuoliDTOResponse extends BasicFindResponseDTO<String> {

	public ListaRuoliDTOResponse(long totalResults, List<String> results) {
		super(totalResults, results);
	}

}
