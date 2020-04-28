package it.govpay.core.dao.reportistica.dto;

import java.util.List;

import it.govpay.bd.reportistica.statistiche.model.StatisticaRendicontazione;
import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaRendicontazioniDTOResponse extends BasicFindResponseDTO<StatisticaRendicontazione> {

	public ListaRendicontazioniDTOResponse(long totalResults, List<StatisticaRendicontazione> results) {
		super(totalResults, results);
	}

}
