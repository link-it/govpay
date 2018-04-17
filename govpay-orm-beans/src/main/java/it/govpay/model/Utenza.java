package it.govpay.model;

import java.util.List;

public class Utenza extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String principal;
	private String principalOriginale;
	private boolean abilitato;
	private List<Long> idDomini;
	private List<Long> idTributi;
	private boolean checkSubject;
	
	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrincipalOriginale() {
		return principalOriginale;
	}

	public void setPrincipalOriginale(String principalOriginale) {
		this.principalOriginale = principalOriginale;
	}

	public boolean isAbilitato() {
		return this.abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	public List<Long> getIdTributi() {
		return idTributi;
	}

	public void setIdTributi(List<Long> idTributi) {
		this.idTributi = idTributi;
	}

	public List<Long> getIdDomini() {
		return idDomini;
	}

	public void setIdDomini(List<Long> idDomini) {
		this.idDomini = idDomini;
	}

	public boolean isCheckSubject() {
		return checkSubject;
	}

	public void setCheckSubject(boolean checkSubject) {
		this.checkSubject = checkSubject;
	}
}
