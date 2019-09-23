package it.govpay.bd.configurazione.model;

import java.io.Serializable;

public class TracciatoCsv implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String headerRisposta;
	private String trasformazioneRichiesta;
	private String trasformazioneRisposta;
	

	public String getTrasformazioneRichiesta() {
		return trasformazioneRichiesta;
	}
	public void setTrasformazioneRichiesta(String trasformazioneRichiesta) {
		this.trasformazioneRichiesta = trasformazioneRichiesta;
	}
	public String getTrasformazioneRisposta() {
		return trasformazioneRisposta;
	}
	public void setTrasformazioneRisposta(String trasformazioneRisposta) {
		this.trasformazioneRisposta = trasformazioneRisposta;
	}
	public String getHeaderRisposta() {
		return headerRisposta;
	}
	public void setHeaderRisposta(String headerRisposta) {
		this.headerRisposta = headerRisposta;
	}
}
