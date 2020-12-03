package it.govpay.core.dao.anagrafica.dto;

import java.util.List;
import java.util.Map;

public class ListaMappingTipiEventoDTOResponse  extends BasicFindResponseDTO<Map.Entry<String, String>> {

		public ListaMappingTipiEventoDTOResponse(Long totalResults, List<Map.Entry<String, String>> results) {
			super(totalResults, results);
		}


}
