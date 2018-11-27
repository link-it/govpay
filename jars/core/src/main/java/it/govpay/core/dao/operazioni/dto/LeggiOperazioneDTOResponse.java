package it.govpay.core.dao.operazioni.dto;

public class LeggiOperazioneDTOResponse {
	
	private String esito;
	private String nome;
	private Integer stato;
	private String descrizioneStato;
	
	
	public LeggiOperazioneDTOResponse() {
	}
	
	public LeggiOperazioneDTOResponse(String nome) {
		this.nome = nome;
	}

	public String getEsito() {
		return this.esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getStato() {
		return this.stato;
	}

	public void setStato(Integer stato) {
		this.stato = stato;
	}

	public String getDescrizioneStato() {
		return this.descrizioneStato;
	}

	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}


}
