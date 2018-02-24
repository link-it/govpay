package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.model.Intermediario;

public class ListaIntermediariDTOResponse  extends BasicFindResponseDTO<Intermediario> {

		public ListaIntermediariDTOResponse(long totalResults, List<Intermediario> results) {
			super(totalResults, results);
		}


}
