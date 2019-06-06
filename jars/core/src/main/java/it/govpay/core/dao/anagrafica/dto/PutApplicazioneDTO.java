package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Applicazione;

public class PutApplicazioneDTO extends BasicCreateRequestDTO  {
	
	private Applicazione applicazione;
	private String idApplicazione;
	private String idUtenza;

	public PutApplicazioneDTO(Authentication user) {
		super(user);
	}

	public Applicazione getApplicazione() {
		return this.applicazione;
	}

	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}

	public String getIdApplicazione() {
		return this.idApplicazione;
	}

	public void setIdApplicazione(String idApplicazione) {
		this.idApplicazione = idApplicazione;
	}

	public String getIdUtenza() {
		return this.idUtenza;
	}

	public void setIdUtenza(String idUtenza) {
		this.idUtenza = idUtenza;
	}

}
