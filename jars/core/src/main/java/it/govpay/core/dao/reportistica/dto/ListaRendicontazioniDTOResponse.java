package it.govpay.core.dao.reportistica.dto;

import java.util.List;

import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;
import it.govpay.model.reportistica.statistiche.StatisticaRendicontazione;

public class ListaRendicontazioniDTOResponse extends BasicFindResponseDTO<StatisticaRendicontazione> {

	public ListaRendicontazioniDTOResponse(long totalResults, List<StatisticaRendicontazione> results) {
		super(totalResults, results);
	}

}
