package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaPendenzeDTOResponse extends BasicFindResponseDTO<LeggiPendenzaDTOResponse> {

	public ListaPendenzeDTOResponse(Long totalResults, List<LeggiPendenzaDTOResponse> results) {
		super(totalResults, results);
	}

}
