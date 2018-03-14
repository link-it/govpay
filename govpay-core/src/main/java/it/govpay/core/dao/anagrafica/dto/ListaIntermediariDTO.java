package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IAutorizzato;

public class ListaIntermediariDTO extends BasicFindRequestDTO{

	private Boolean abilitato;
	
	public ListaIntermediariDTO(IAutorizzato user) {
		super(user);
	}

	public Boolean getAbilitato() {
		return abilitato;
	}

	public void setAbilitato(Boolean abilitato) {
		this.abilitato = abilitato;
	}

}
