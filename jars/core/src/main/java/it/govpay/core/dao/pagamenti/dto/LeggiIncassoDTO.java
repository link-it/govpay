package it.govpay.core.dao.pagamenti.dto;

import org.springframework.security.core.Authentication;

import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;

public class LeggiIncassoDTO extends BasicFindRequestDTO {
	
	public LeggiIncassoDTO(Authentication user) {
		super(user);
	}
	
	private String principal;
	private String idDominio;
	private String idIncasso;
	
	public String getPrincipal() {
		return AutorizzazioneUtils.getPrincipal(this.getUser()) != null ? AutorizzazioneUtils.getPrincipal(this.getUser()) : this.principal;
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
