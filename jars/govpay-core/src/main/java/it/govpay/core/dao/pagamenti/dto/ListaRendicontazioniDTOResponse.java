package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaRendicontazioniDTOResponse extends BasicFindResponseDTO<LeggiRendicontazioneDTOResponse> {

	public ListaRendicontazioniDTOResponse(long totalResults, List<LeggiRendicontazioneDTOResponse> results) {
		super(totalResults, results);
	}

}
