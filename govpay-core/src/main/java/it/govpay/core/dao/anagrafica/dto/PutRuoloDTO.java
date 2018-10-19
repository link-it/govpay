package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.model.Acl;
import it.govpay.model.IAutorizzato;

public class PutRuoloDTO extends BasicCreateRequestDTO  {
	
	private List<Acl> acls;
	private String idRuolo;
	public PutRuoloDTO(IAutorizzato user) {
		super(user);
	}
	public String getIdRuolo() {
		return this.idRuolo;
	}
	public void setIdRuolo(String idRuolo) {
		this.idRuolo = idRuolo;
	}
	public List<Acl> getAcls() {
		return this.acls;
	}
	public void setAcls(List<Acl> acls) {
		this.acls = acls;
	}


}
