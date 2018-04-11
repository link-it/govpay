package it.govpay.core.dao.operazioni.dto;

import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.model.IAutorizzato;

public class LeggiOperazioneDTO extends BasicFindRequestDTO {
	
	public LeggiOperazioneDTO(IAutorizzato user) {
		super(user);
	}
	
	private String idOperazione = null;
	
	public String getIdOperazione() {
		return idOperazione;
	}

	public void setIdOperazione(String idOperazione) {
		this.idOperazione = idOperazione;
	}

}
