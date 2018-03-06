package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.bd.model.Operatore;
import it.govpay.model.IAutorizzato;

public class PutOperatoreDTO extends BasicCreateRequestDTO  {
	
	private Operatore operatore;
	private String principal;
	
	private List<String> idDomini;
	private List<String> idTributi;
	
	public PutOperatoreDTO(IAutorizzato user) {
		super(user);
	}

	public Operatore getOperatore() {
		return operatore;
	}

	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public List<String> getIdTributi() {
		return idTributi;
	}

	public void setIdTributi(List<String> idTributi) {
		this.idTributi = idTributi;
	}

	public List<String> getIdDomini() {
		return idDomini;
	}

	public void setIdDomini(List<String> idDomini) {
		this.idDomini = idDomini;
	}


}
