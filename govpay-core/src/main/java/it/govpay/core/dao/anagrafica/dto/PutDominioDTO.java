package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.Dominio;
import it.govpay.model.IAutorizzato;

public class PutDominioDTO extends BasicCreateRequestDTO  {
	
	private Dominio dominio;
	private String idDominio;
	private String codStazione;
	
	public PutDominioDTO(IAutorizzato user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	public String getCodStazione() {
		return codStazione;
	}

	public void setCodStazione(String idStazione) {
		this.codStazione = idStazione;
	}

	public Dominio getDominio() {
		return dominio;
	}

	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	public String getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}

}
