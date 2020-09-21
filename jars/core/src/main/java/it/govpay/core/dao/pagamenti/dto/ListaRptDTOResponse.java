package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaRptDTOResponse extends BasicFindResponseDTO<LeggiRptDTOResponse> {

	public ListaRptDTOResponse(Long totalResults, List<LeggiRptDTOResponse> results) {
		super(totalResults, results);
	}

}
