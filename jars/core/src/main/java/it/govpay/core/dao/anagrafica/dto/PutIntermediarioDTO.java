package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

import it.govpay.model.Intermediario;

public class PutIntermediarioDTO extends BasicCreateRequestDTO  {
	
	private Intermediario intermediario;
	private String idIntermediario;
	
	public PutIntermediarioDTO(Authentication user) {
		super(user);
	}

	public Intermediario getIntermediario() {
		return this.intermediario;
	}

	public void setIntermediario(Intermediario intermediario) {
		this.intermediario = intermediario;
	}

	public String getIdIntermediario() {
		return this.idIntermediario;
	}

	public void setIdIntermediario(String idIntermediario) {
		this.idIntermediario = idIntermediario;
	}

}
