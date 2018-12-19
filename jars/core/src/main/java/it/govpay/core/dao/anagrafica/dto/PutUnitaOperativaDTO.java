package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

import it.govpay.bd.model.UnitaOperativa;

public class PutUnitaOperativaDTO extends BasicCreateRequestDTO  {
	
	
	private UnitaOperativa uo;
	private String idDominio;
	private String idUo;
	
	public PutUnitaOperativaDTO(Authentication user) {
		super(user);
	}

	public UnitaOperativa getUo() {
		return this.uo;
	}

	public void setUo(UnitaOperativa uo) {
		this.uo = uo;
	}

	public String getIdUo() {
		return this.idUo;
	}

	public void setIdUo(String idUo) {
		this.idUo = idUo;
	}

	public String getIdDominio() {
		return this.idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}

}
