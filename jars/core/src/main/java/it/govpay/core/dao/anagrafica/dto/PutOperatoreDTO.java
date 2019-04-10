package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Operatore;

public class PutOperatoreDTO extends BasicCreateRequestDTO  {
	
	private Operatore operatore;
	private String principal;
	
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


}
