package it.govpay.core.dao.pagamenti.dto;

import it.govpay.core.dao.commons.Versamento;
import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;
import it.govpay.model.IAutorizzato;

public class PutPendenzaDTO extends BasicCreateRequestDTO  {
	
	private Versamento versamento;

	public PutPendenzaDTO(IAutorizzato user) {
		super(user);
	}

	public Versamento getVersamento() {
		return versamento;
	}

	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}

}
