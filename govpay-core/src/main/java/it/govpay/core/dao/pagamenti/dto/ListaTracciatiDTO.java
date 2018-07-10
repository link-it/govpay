package it.govpay.core.dao.pagamenti.dto;

import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.orm.Tracciato;

public class ListaTracciatiDTO extends BasicFindRequestDTO{

	public ListaTracciatiDTO(IAutorizzato user) {
		super(user);
		this.setDefaultSort(Tracciato.model().DATA_CARICAMENTO,SortOrder.DESC);
	}
}
