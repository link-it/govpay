package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.bd.model.Promemoria;
import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaPromemoriaDTOResponse extends BasicFindResponseDTO<Promemoria> {

	public ListaPromemoriaDTOResponse(long totalResults, List<Promemoria> results) {
		super(totalResults, results);
	}

}
