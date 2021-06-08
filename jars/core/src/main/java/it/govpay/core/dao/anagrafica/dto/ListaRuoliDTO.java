package it.govpay.core.dao.anagrafica.dto;

import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.orm.ACL;

public class ListaRuoliDTO extends BasicFindRequestDTO{

	public ListaRuoliDTO(Authentication user) {
		super(user);
		this.addDefaultSort(ACL.model().RUOLO,SortOrder.ASC);
		this.ricercaAnagrafica = true;
	}
}
