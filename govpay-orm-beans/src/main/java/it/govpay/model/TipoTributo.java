package it.govpay.model;

public class TipoTributo extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	private Long id; 
	private String codTributo;
	private String descrizione;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodTributo() {
		return codTributo;
	}
	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
