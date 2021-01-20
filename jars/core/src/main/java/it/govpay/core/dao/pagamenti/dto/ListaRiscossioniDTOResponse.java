package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaRiscossioniDTOResponse extends BasicFindResponseDTO<LeggiRiscossioneDTOResponse> {

	public ListaRiscossioniDTOResponse(Long totalResults, List<LeggiRiscossioneDTOResponse> results) {
		super(totalResults, results);
	}

}
