package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.bd.model.Rpt;
import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaRptDTOResponse extends BasicFindResponseDTO<Rpt> {

	public ListaRptDTOResponse(long totalResults, List<Rpt> results) {
		super(totalResults, results);
	}

}
