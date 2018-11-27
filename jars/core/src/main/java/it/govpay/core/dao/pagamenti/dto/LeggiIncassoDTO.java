package it.govpay.core.dao.pagamenti.dto;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;

public class LeggiIncassoDTO extends BasicFindRequestDTO {
	
	public LeggiIncassoDTO(IAutorizzato user) {
		super(user);
	}
	
	private String principal;
	private String idDominio;
	private String idIncasso;
	
	public String getPrincipal() {
		return this.getUser() != null ? this.getUser().getPrincipal() : this.principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getIdDominio() {
		return this.idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getIdIncasso() {
		return this.idIncasso;
	}
	public void setIdIncasso(String idIncasso) {
		this.idIncasso = idIncasso;
	}

}
