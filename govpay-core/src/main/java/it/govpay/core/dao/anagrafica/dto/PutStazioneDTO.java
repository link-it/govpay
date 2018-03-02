package it.govpay.core.dao.anagrafica.dto;

import it.govpay.bd.model.Stazione;
import it.govpay.model.IAutorizzato;

public class PutStazioneDTO extends BasicCreateRequestDTO  {
	
	private Stazione stazione;
	private String idStazione;
	private String idIntermediario;
	
	public PutStazioneDTO(IAutorizzato user) {
		super(user);
	}

	public Stazione getStazione() {
		return stazione;
	}

	public void setStazione(Stazione stazione) {
		this.stazione = stazione;
	}

	public String getIdStazione() {
		return idStazione;
	}

	public void setIdStazione(String idStazione) {
		this.idStazione = idStazione;
	}

	public String getIdIntermediario() {
		return idIntermediario;
	}

	public void setIdIntermediario(String idIntermediario) {
		this.idIntermediario = idIntermediario;
	}

}
