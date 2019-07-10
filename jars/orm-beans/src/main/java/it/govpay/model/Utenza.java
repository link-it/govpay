package it.govpay.model;

import java.util.List;

public class Utenza extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum TIPO_UTENZA { ANONIMO, CITTADINO, APPLICAZIONE, OPERATORE };
	
	protected Long id;
	protected String principal;
	protected String principalOriginale;
	protected boolean abilitato;
	protected List<Long> idDomini;
	protected List<Long> idTipiVersamento;
	protected boolean checkSubject;
	protected boolean autorizzazioneDominiStar;
	protected boolean autorizzazioneTipiVersamentoStar;
	protected List<String> ruoli;
	
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

	public List<Long> getIdTipiVersamento() {
		return idTipiVersamento;
	}

	public void setIdTipiVersamento(List<Long> idTipiVersamento) {
		this.idTipiVersamento = idTipiVersamento;
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

	public boolean isAutorizzazioneDominiStar() {
		return autorizzazioneDominiStar;
	}

	public void setAutorizzazioneDominiStar(boolean autorizzazioneDominiStar) {
		this.autorizzazioneDominiStar = autorizzazioneDominiStar;
	}

	public boolean isAutorizzazioneTipiVersamentoStar() {
		return autorizzazioneTipiVersamentoStar;
	}

	public void setAutorizzazioneTipiVersamentoStar(boolean autorizzazioneTipiVersamentoStar) {
		this.autorizzazioneTipiVersamentoStar = autorizzazioneTipiVersamentoStar;
	}
	public List<String> getRuoli() {
		return this.ruoli;
	}
	public void setRuoli(List<String> ruoli) {
		this.ruoli = ruoli;
	}
}
