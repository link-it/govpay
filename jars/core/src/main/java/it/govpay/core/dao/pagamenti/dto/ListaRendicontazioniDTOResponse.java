package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.bd.viste.model.Rendicontazione;
import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaRendicontazioniDTOResponse extends BasicFindResponseDTO<Rendicontazione> {

	public ListaRendicontazioniDTOResponse(Long totalResults, List<Rendicontazione> results) {
		super(totalResults, results);
	}

}
