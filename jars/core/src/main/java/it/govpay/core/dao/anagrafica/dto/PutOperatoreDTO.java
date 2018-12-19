package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Operatore;

public class PutOperatoreDTO extends BasicCreateRequestDTO  {
	
	private Operatore operatore;
	private String principal;
	
	private List<String> idDomini;
	private List<String> idTributi;
	
	public PutOperatoreDTO(Authentication user) {
		super(user);
	}

	public Operatore getOperatore() {
		return this.operatore;
	}

	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}

	public String getPrincipal() {
		return this.principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public List<String> getIdTributi() {
		return this.idTributi;
	}

	public void setIdTributi(List<String> idTributi) {
		this.idTributi = idTributi;
	}

	public List<String> getIdDomini() {
		return this.idDomini;
	}

	public void setIdDomini(List<String> idDomini) {
		this.idDomini = idDomini;
	}


}
