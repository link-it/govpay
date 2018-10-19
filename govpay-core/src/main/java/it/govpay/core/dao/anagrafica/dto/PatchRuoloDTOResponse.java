package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.model.Acl;

public class PatchRuoloDTOResponse {
	
	private List<Acl> listaAcl;
	private String idRuolo;
	
	public String getIdRuolo() {
		return idRuolo;
	}
	public void setIdRuolo(String idRuolo) {
		this.idRuolo = idRuolo;
	}
	public List<Acl> getListaAcl() {
		return listaAcl;
	}
	public void setListaAcl(List<Acl> listaAcl) {
		this.listaAcl = listaAcl;
	}

}
