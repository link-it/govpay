package it.govpay.model;

public class Configurazione extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Long id;
	private String giornaleEventi;
	private String tracciatoCSV;
	
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

}
