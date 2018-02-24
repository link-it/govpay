package it.govpay.core.dao.anagrafica.dto;

import it.govpay.model.Intermediario;
import it.govpay.model.IAutorizzato;

public class PutIntermediarioDTO extends BasicCreateRequestDTO  {
	
	private Intermediario intermediario;
	
	public PutIntermediarioDTO(IAutorizzato user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	public Intermediario getIntermediario() {
		return intermediario;
	}

	public void setIntermediario(Intermediario intermediario) {
		this.intermediario = intermediario;
	}

}
