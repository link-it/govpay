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
	private String avvisaturaMail;
	private String avvisaturaAppIo;
	private String appIOBatch;
	private String confSvecchiamento;
	
	@Override
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
	public String getAvvisaturaMail() {
		return avvisaturaMail;
	}
	public void setAvvisaturaMail(String avvisaturaMail) {
		this.avvisaturaMail = avvisaturaMail;
	}
	public String getAvvisaturaAppIo() {
		return avvisaturaAppIo;
	}
	public void setAvvisaturaAppIo(String avvisaturaAppIo) {
		this.avvisaturaAppIo = avvisaturaAppIo;
	}
	public String getAppIOBatch() {
		return appIOBatch;
	}
	public void setAppIOBatch(String appIOBatch) {
		this.appIOBatch = appIOBatch;
	}
	public String getConfSvecchiamento() {
		return confSvecchiamento;
	}
	public void setConfSvecchiamento(String confSvecchiamento) {
		this.confSvecchiamento = confSvecchiamento;
	}

}
