package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaRiscossioniDTOResponse extends BasicFindResponseDTO<it.govpay.bd.viste.model.Pagamento> {

	public ListaRiscossioniDTOResponse(Long totalResults, List<it.govpay.bd.viste.model.Pagamento> results) {
		super(totalResults, results);
	}

}
