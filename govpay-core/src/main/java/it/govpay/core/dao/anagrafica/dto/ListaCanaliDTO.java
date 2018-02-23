package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IAutorizzato;
import it.govpay.orm.Psp;

public class ListaCanaliDTO extends BasicFindRequestDTO{

	private Boolean abilitato;
	private Boolean bollo;
	private Boolean storno;
	
	public ListaCanaliDTO(IAutorizzato user) {
		super(user);
		this.addSortField("ragioneSociale", Psp.model().RAGIONE_SOCIALE);
	}

	public Boolean getAbilitato() {
		return abilitato;
	}

	public void setAbilitato(Boolean abilitato) {
		this.abilitato = abilitato;
	}

	public Boolean getBollo() {
		return bollo;
	}

	public void setBollo(Boolean bollo) {
		this.bollo = bollo;
	}

	public Boolean getStorno() {
		return storno;
	}

	public void setStorno(Boolean storno) {
		this.storno = storno;
	}

}
