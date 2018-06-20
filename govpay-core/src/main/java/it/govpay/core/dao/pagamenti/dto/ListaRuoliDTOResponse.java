package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaRuoliDTOResponse extends BasicFindResponseDTO<String> {

	public ListaRuoliDTOResponse(long totalResults, List<String> results) {
		super(totalResults, results);
	}

}
