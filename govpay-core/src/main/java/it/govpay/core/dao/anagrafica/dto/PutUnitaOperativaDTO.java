package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.UnitaOperativa;
import it.govpay.model.IAutorizzato;

public class PutUnitaOperativaDTO extends BasicCreateRequestDTO  {
	
	
	private UnitaOperativa uo;
	private String idDominio;
	private String idUo;
	
	public PutUnitaOperativaDTO(IAutorizzato user) {
		super(user);
	}

	public UnitaOperativa getUo() {
		return uo;
	}

	public void setUo(UnitaOperativa uo) {
		this.uo = uo;
	}

	public String getIdUo() {
		return idUo;
	}

	public void setIdUo(String idUo) {
		this.idUo = idUo;
	}

	public String getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}

}
