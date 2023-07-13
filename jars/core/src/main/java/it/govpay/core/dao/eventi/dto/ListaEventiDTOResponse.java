package it.govpay.core.dao.eventi.dto;

import java.util.List;

import it.govpay.bd.model.Evento;
import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaEventiDTOResponse  extends BasicFindResponseDTO<Evento> {

	public ListaEventiDTOResponse(Long totalResults, List<Evento> results) {
		super(totalResults, results);
	}

}
