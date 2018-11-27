package it.govpay.bd.wrapper;

import java.util.Date;

public class StatoNdP {
	
	private Integer codice;
	private String operazione;
	private String descrizione;
	private Date data;
	
	public Integer getCodice() {
		return this.codice;
	}
	public void setCodice(Integer codice) {
		this.codice = codice;
	}
	public String getOperazione() {
		return this.operazione;
	}
	public void setOperazione(String operazione) {
		this.operazione = operazione;
	}
	public String getDescrizione() {
		return this.descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Date getData() {
		return this.data;
	}
	public void setData(Date data) {
		this.data = data;
	}
}
