package it.govpay.model;

import java.util.List;

public class Utenza extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum TIPO_UTENZA { DEFAULT, CITTADINO, APPLICAZIONE, OPERATORE };
	
	protected Long id;
	protected String principal;
	protected String principalOriginale;
	protected boolean abilitato;
	protected List<Long> idDomini;
	protected List<Long> idTributi;
	protected boolean checkSubject;
	
	public String getPrincipal() {
		return this.principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrincipalOriginale() {
		return this.principalOriginale;
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
		return this.idTributi;
	}

	public void setIdTributi(List<Long> idTributi) {
		this.idTributi = idTributi;
	}

	public List<Long> getIdDomini() {
		return this.idDomini;
	}

	public void setIdDomini(List<Long> idDomini) {
		this.idDomini = idDomini;
	}

	public boolean isCheckSubject() {
		return this.checkSubject;
	}

	public void setCheckSubject(boolean checkSubject) {
		this.checkSubject = checkSubject;
	}
}
