package it.govpay.core.dao.pagamenti.dto;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;

public class ListaRendicontazioniDTO extends BasicFindRequestDTO{
	
	public ListaRendicontazioniDTO(IAutorizzato user) {
		super(user);
	}
	private String idDominio;

	public String getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	
}
