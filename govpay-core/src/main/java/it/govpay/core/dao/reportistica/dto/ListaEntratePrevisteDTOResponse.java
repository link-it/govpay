package it.govpay.core.dao.reportistica.dto;

import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaEntratePrevisteDTOResponse extends BasicFindResponseDTO<LeggiPendenzaDTOResponse> {

	public ListaEntratePrevisteDTOResponse(long totalResults, List<LeggiPendenzaDTOResponse> results) {
		super(totalResults, results);
	}

}
