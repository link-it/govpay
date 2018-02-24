package it.govpay.core.dao.anagrafica;

import java.util.stream.Collectors;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.RuoliBD;
import it.govpay.bd.anagrafica.filters.RuoloFilter;
import it.govpay.core.dao.anagrafica.dto.FindRuoliDTO;
import it.govpay.core.dao.anagrafica.dto.FindRuoliDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetRuoloDTO;
import it.govpay.core.dao.anagrafica.dto.GetRuoloDTOResponse;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.NotFoundException;
import it.govpay.core.utils.GpThreadLocal;

public class RuoliDAO extends BasicBD{
	
	public RuoliDAO(BasicBD basicBD) {
		super(basicBD);
	}

	public FindRuoliDTOResponse findRuoli(FindRuoliDTO listaRuoliDTO) throws NotAuthorizedException, ServiceException {
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
			
			RuoliBD ruoliBD = new RuoliBD(bd);
			RuoloFilter filter = null;
			if(listaRuoliDTO.isSimpleSearch()) {
				filter = ruoliBD.newFilter(true);
				filter.setSimpleSearchString(listaRuoliDTO.getSimpleSearch());
			} else {
				filter = ruoliBD.newFilter(false);
				filter.setSearchAbilitato(listaRuoliDTO.getAbilitato());
				filter.setListaRuoli(listaRuoliDTO.getListaRuoli().stream().map(r -> r.getCodRuolo()).collect(Collectors.toList()));
			}
			filter.setOffset(listaRuoliDTO.getOffset());
			filter.setLimit(listaRuoliDTO.getLimit());
			filter.getFilterSortList().addAll(listaRuoliDTO.getFieldSortList());
			
			return new FindRuoliDTOResponse(ruoliBD.count(filter), ruoliBD.findAll(filter));
			
		} finally {
			bd.closeConnection();
		}
	}
	
	public GetRuoloDTOResponse getRuolo(GetRuoloDTO getRuoloDTO) throws NotAuthorizedException, NotFoundException, ServiceException {
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
			GetRuoloDTOResponse response = new GetRuoloDTOResponse(AnagraficaManager.getRuolo(bd, getRuoloDTO.getCodRuolo()));
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new NotFoundException("Ruolo " + getRuoloDTO.getCodRuolo() + " non censito in Anagrafica");
		} finally {
			bd.closeConnection();
		}
	}
}
