package it.govpay.core.dao.anagrafica.dto;

import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.model.IAutorizzato;
import it.govpay.orm.ACL;

public class ListaRuoliDTO extends BasicFindRequestDTO{

	public ListaRuoliDTO(IAutorizzato user) {
		super(user);
		this.setDefaultSort(ACL.model().RUOLO,SortOrder.DESC);
	}
}
