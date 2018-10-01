package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.bd.model.Applicazione;
import it.govpay.model.IAutorizzato;

public class PutApplicazioneDTO extends BasicCreateRequestDTO  {
	
	private Applicazione applicazione;
	private String idApplicazione;
	private String idUtenza;
	private List<String> idDomini;
	private List<String> idTributi;
	
	public List<String> getIdDomini() {
		return this.idDomini;
	}

	public void setIdDomini(List<String> idDomini) {
		this.idDomini = idDomini;
	}

	public List<String> getIdTributi() {
		return this.idTributi;
	}

	public void setIdTributi(List<String> idTributi) {
		this.idTributi = idTributi;
	}

	public PutApplicazioneDTO(IAutorizzato user) {
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
