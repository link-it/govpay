package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;
import it.govpay.bd.model.Operazione;

public class ListaOperazioniTracciatoDTOResponse extends BasicFindResponseDTO<Operazione> {

	public ListaOperazioniTracciatoDTOResponse(long totalResults, List<Operazione> results) {
		super(totalResults, results);
	}

}
