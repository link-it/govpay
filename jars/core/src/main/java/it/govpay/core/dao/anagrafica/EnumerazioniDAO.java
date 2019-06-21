package it.govpay.core.dao.anagrafica;

import java.util.ArrayList;
import java.util.Map.Entry;

import it.govpay.core.dao.anagrafica.dto.ListaMappingTipiEventoDTO;
import it.govpay.core.dao.anagrafica.dto.ListaMappingTipiEventoDTOResponse;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.utils.EventiUtils;

public class EnumerazioniDAO extends BaseDAO{
	
	public EnumerazioniDAO() {
		super();
	}
	
	public EnumerazioniDAO(boolean useCacheData) {
		super(useCacheData);
	}

	public ListaMappingTipiEventoDTOResponse listaMappingTipiEvento(ListaMappingTipiEventoDTO listaMappingTipiEventoDTO) {
		ArrayList<Entry<String, String>> results = new ArrayList<>(); 
		results.addAll(EventiUtils.getInstance().getMappingLabelTipoEvento().entrySet());
		ListaMappingTipiEventoDTOResponse response = new ListaMappingTipiEventoDTOResponse(0, results);
		return response;
	}
}
