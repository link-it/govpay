package it.govpay.core.dao.eventi.dto;

import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;
import it.govpay.bd.model.Evento;

public class ListaEventiDTOResponse  extends BasicFindResponseDTO<Evento> {

	public ListaEventiDTOResponse(long totalResults, List<Evento> results) {
		super(totalResults, results);
	}

}
