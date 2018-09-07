package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.IAutorizzato;

public class GetAvvisoDTO extends BasicRequestDTO {
	
	private String codDominio;
	private String iuv;
	
	public GetAvvisoDTO(IAutorizzato user, String codDominio, String iuv) {
		super(user);
		this.setCodDominio(codDominio);
		this.setIuv(iuv);
	}

	public String getIuv() {
		return this.iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}


}
