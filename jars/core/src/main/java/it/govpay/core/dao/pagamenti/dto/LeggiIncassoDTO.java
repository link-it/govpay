package it.govpay.core.dao.pagamenti.dto;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;

public class LeggiIncassoDTO extends BasicFindRequestDTO {
	
	public LeggiIncassoDTO(Authentication user) {
		super(user);
	}
	
	private String idDominio;
	private String idIncasso;
	
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
