package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;
import it.govpay.model.Tracciato;

public class ListaTracciatiDTOResponse extends BasicFindResponseDTO<Tracciato> {

	public ListaTracciatiDTOResponse(long totalResults, List<Tracciato> results) {
		super(totalResults, results);
	}

}
