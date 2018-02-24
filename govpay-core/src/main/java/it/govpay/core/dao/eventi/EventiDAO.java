package it.govpay.core.dao.eventi;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.filters.EventiFilter;
import it.govpay.core.dao.eventi.dto.ListaEventiDTO;
import it.govpay.core.dao.eventi.dto.ListaEventiDTOResponse;
import it.govpay.model.Evento;

public class EventiDAO extends BasicBD{

	public EventiDAO(BasicBD basicBD) {
		super(basicBD);
	}

	public ListaEventiDTOResponse listaEventi(ListaEventiDTO listaEventiDTO) throws ServiceException {
		EventiBD eventiBD = new EventiBD(this);
		EventiFilter filter = eventiBD.newFilter();

		filter.setOffset(listaEventiDTO.getOffset());
		filter.setLimit(listaEventiDTO.getLimit());
		filter.setCodDominio(listaEventiDTO.getIdDominio());
		filter.setIuv(listaEventiDTO.getIuv());
		filter.setFilterSortList(listaEventiDTO.getFieldSortList());

		long count = eventiBD.count(filter);

		List<Evento> resList = new ArrayList<Evento>();
		if(count > 0) {
			resList = eventiBD.findAll(filter);
		} 

		return new ListaEventiDTOResponse(count, resList);
	}

}
