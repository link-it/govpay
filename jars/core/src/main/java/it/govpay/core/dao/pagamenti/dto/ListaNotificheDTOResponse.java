package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.bd.model.Notifica;
import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaNotificheDTOResponse extends BasicFindResponseDTO<Notifica> {

	public ListaNotificheDTOResponse(Long totalResults, List<Notifica> results) {
		super(totalResults, results);
	}

}
