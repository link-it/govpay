package it.govpay.core.dao.reportistica.dto;

import java.util.List;

import it.govpay.bd.reportistica.statistiche.model.StatisticaRiscossione;
import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaRiscossioniDTOResponse extends BasicFindResponseDTO<StatisticaRiscossione> {

	public ListaRiscossioniDTOResponse(long totalResults, List<StatisticaRiscossione> results) {
		super(totalResults, results);
	}

}
