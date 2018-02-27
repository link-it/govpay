package it.govpay.model;

import java.util.List;

public class Utenza extends BasicModel implements IAutorizzato {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String principal;
	private boolean abilitato;
	
	private transient List<Long> idDominio;
	private transient List<Long> idTributo;
	private transient List<Acl> acls;
	
	@Override
	public List<Acl> getAcls() {
		return this.acls;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public List<Long> getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(List<Long> idDominio) {
		this.idDominio = idDominio;
	}

	public List<Long> getIdTributo() {
		return idTributo;
	}

	public void setIdTributo(List<Long> idTributo) {
		this.idTributo = idTributo;
	}

	public void setAcls(List<Acl> acls) {
		this.acls = acls;
	}

	public boolean isAbilitato() {
		return abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	
}
