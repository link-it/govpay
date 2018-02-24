package it.govpay.core.dao.eventi.dto;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;

public class ListaEventiDTO extends BasicFindRequestDTO{

	public ListaEventiDTO(IAutorizzato user) {
		super(user);
	}

	private String idDominio;
	private String iuv;
	
	public String getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
}
