package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.bd.model.PagamentoPortale;
import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaPagamentiPortaleDTOResponse extends BasicFindResponseDTO<PagamentoPortale> {

	public ListaPagamentiPortaleDTOResponse(long totalResults, List<PagamentoPortale> results) {
		super(totalResults, results);
	}

}
