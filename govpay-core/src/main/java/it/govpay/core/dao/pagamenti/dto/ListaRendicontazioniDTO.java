package it.govpay.core.dao.pagamenti.dto;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.orm.FR;

public class ListaRendicontazioniDTO extends BasicFindRequestDTO{
	
	public ListaRendicontazioniDTO(IAutorizzato user) {
		super(user);
		this.addSortField("dataAcquisizione", FR.model().DATA_ACQUISIZIONE);
	}
	private String idDominio;

	public String getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	
}
