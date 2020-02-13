package it.govpay.model;

public class IdUnitaOperativa extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	private Long idUnita; 
	private Long idDominio;
	private Long id;
	
	public Long getIdUnita() {
		return idUnita;
	}
	public void setIdUnita(Long idUnita) {
		this.idUnita = idUnita;
	}
	public Long getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(Long idDominio) {
		this.idDominio = idDominio;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

}
