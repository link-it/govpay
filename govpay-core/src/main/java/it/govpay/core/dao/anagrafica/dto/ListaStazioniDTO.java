package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IAutorizzato;
import it.govpay.orm.Canale;

public class ListaStazioniDTO extends BasicFindRequestDTO{

	private Boolean abilitato;
	private String modello;
	private String tipoVersamento;
	private String idPsp;
	
	public ListaStazioniDTO(IAutorizzato user) {
		super(user);
		this.addSortField("idCanale", Canale.model().COD_CANALE);
	}

	public Boolean getAbilitato() {
		return abilitato;
	}

	public void setAbilitato(Boolean abilitato) {
		this.abilitato = abilitato;
	}

	public String getTipoVersamento() {
		return tipoVersamento;
	}

	public void setTipoVersamento(String tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public String getIdPsp() {
		return idPsp;
	}

	public void setIdPsp(String idPsp) {
		this.idPsp = idPsp;
	}


}
