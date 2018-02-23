package it.govpay.core.dao.anagrafica;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.PspBD;
import it.govpay.bd.anagrafica.filters.PspFilter;
import it.govpay.bd.model.Psp;
import it.govpay.core.dao.anagrafica.dto.ListaPspDTO;
import it.govpay.core.dao.anagrafica.dto.ListaPspDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;

public class PspDAO extends BasicBD{

	public PspDAO(BasicBD basicBD) {
		super(basicBD);
	}

	public ListaPspDTOResponse listaPsp(ListaPspDTO listaPspDTO) throws ServiceException,PagamentoPortaleNonTrovatoException{
		PspBD rptBD = new PspBD(this);
		PspFilter filter = rptBD.newFilter();

		filter.setOffset(listaPspDTO.getOffset());
		filter.setLimit(listaPspDTO.getLimit());
		filter.setSearchAbilitato(listaPspDTO.getAbilitato());
		filter.setBollo(listaPspDTO.getBollo());
		filter.setStorno(listaPspDTO.getStorno()); 
		filter.setFilterSortList(listaPspDTO.getFieldSortList());
		
		long count = rptBD.count(filter);

		List<Psp> resList = new ArrayList<Psp>();
		if(count > 0) {
			resList = rptBD.findAll(filter);
		} 

		return new ListaPspDTOResponse(count, resList);
	}
}
