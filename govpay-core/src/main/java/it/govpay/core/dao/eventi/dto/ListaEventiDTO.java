package it.govpay.core.dao.eventi.dto;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;

public class ListaEventiDTO extends BasicFindRequestDTO{

	public ListaEventiDTO(IAutorizzato user) {
		super(user);
	}

	private String idDominio;
	private String iuv;
	private String idA2A;
	private String idPendenza;
	
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
	public String getIdA2A() {
		return idA2A;
	}
	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}
	public String getIdPendenza() {
		return idPendenza;
	}
	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}
}
