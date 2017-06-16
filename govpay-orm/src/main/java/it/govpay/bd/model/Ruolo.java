package it.govpay.bd.model;


public class Ruolo {
	
	private long id;
	private String codRuolo;
	private String descrizione;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCodRuolo() {
		return codRuolo;
	}
	public void setCodRuolo(String codRuolo) {
		this.codRuolo = codRuolo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
