package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Stazione;

public class PutStazioneDTO extends BasicCreateRequestDTO  {
	
	private Stazione stazione;
	private String idStazione;
	private String idIntermediario;
	
	public PutStazioneDTO(Authentication user) {
		super(user);
	}

	public Stazione getStazione() {
		return this.stazione;
	}

	public void setStazione(Stazione stazione) {
		this.stazione = stazione;
	}

	public String getIdStazione() {
		return this.idStazione;
	}

	public void setIdStazione(String idStazione) {
		this.idStazione = idStazione;
	}

	public String getIdIntermediario() {
		return this.idIntermediario;
	}

	public void setIdIntermediario(String idIntermediario) {
		this.idIntermediario = idIntermediario;
	}

}
