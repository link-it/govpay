package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Applicazione;

public class PutApplicazioneDTO extends BasicCreateRequestDTO  {
	
	private Applicazione applicazione;
	private String idApplicazione;
	private String idUtenza;
	private List<String> idDomini;
	private List<String> idTipiVersamento;
	
	public List<String> getIdDomini() {
		return this.idDomini;
	}

	public void setIdDomini(List<String> idDomini) {
		this.idDomini = idDomini;
	}


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

	public List<String> getIdTipiVersamento() {
		return idTipiVersamento;
	}

	public void setIdTipiVersamento(List<String> idTipiVersamento) {
		this.idTipiVersamento = idTipiVersamento;
	}

}
