package it.govpay.model;

public class Configurazione extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Long id;
	private String giornaleEventi;
	private String tracciatoCSV;
	private String confHardening;
	private String mailBatch;
	private String mailPromemoria;
	private String mailRicevuta;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGiornaleEventi() {
		return giornaleEventi;
	}
	public void setGiornaleEventi(String giornaleEventi) {
		this.giornaleEventi = giornaleEventi;
	}
	public String getTracciatoCSV() {
		return tracciatoCSV;
	}
	public void setTracciatoCSV(String tracciatoCSV) {
		this.tracciatoCSV = tracciatoCSV;
	}
	public String getConfHardening() {
		return confHardening;
	}
	public void setConfHardening(String confHardening) {
		this.confHardening = confHardening;
	}
	public String getMailBatch() {
		return mailBatch;
	}
	public void setMailBatch(String mailBatch) {
		this.mailBatch = mailBatch;
	}
	public String getMailPromemoria() {
		return mailPromemoria;
	}
	public void setMailPromemoria(String mailPromemoria) {
		this.mailPromemoria = mailPromemoria;
	}
	public String getMailRicevuta() {
		return mailRicevuta;
	}
	public void setMailRicevuta(String mailRicevuta) {
		this.mailRicevuta = mailRicevuta;
	}

}
