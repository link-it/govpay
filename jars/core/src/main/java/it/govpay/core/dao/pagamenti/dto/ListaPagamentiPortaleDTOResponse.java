package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaPagamentiPortaleDTOResponse extends BasicFindResponseDTO<LeggiPagamentoPortaleDTOResponse> {

	public ListaPagamentiPortaleDTOResponse(long totalResults, List<LeggiPagamentoPortaleDTOResponse> results) {
		super(totalResults, results);
	}

}
