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
	
	private List<String> ruoli;
	private transient List<String> idDominio;
	private transient List<String> idTributo;
	private transient List<Acl> acls;
	
	@Override
	public List<Acl> getAcls() {
		return this.acls;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getPrincipal() {
		return this.principal;
	}

	@Override
	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	@Override
	public List<String> getIdDominio() {
		return this.idDominio;
	}

	@Override
	public void setIdDominio(List<String> idDominio) {
		this.idDominio = idDominio;
	}

	@Override
	public List<String> getIdTributo() {
		return this.idTributo;
	}

	@Override
	public void setIdTributo(List<String> idTributo) {
		this.idTributo = idTributo;
	}

	@Override
	public void setAcls(List<Acl> acls) {
		this.acls = acls;
	}

	public boolean isAbilitato() {
		return this.abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	@Override
	public List<String> getRuoli() {
		return this.ruoli;
	}

	@Override
	public void setRuoli(List<String> ruoli) {
		this.ruoli = ruoli;
	}
}
