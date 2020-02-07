package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaFrDTOResponse extends BasicFindResponseDTO<LeggiFrDTOResponse> {

	public ListaFrDTOResponse(long totalResults, List<LeggiFrDTOResponse> results) {
		super(totalResults, results);
	}

}
