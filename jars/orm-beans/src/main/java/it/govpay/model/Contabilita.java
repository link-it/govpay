package it.govpay.model;

import java.math.BigDecimal;

public class Contabilita {

	private String ufficio;
	private String capitolo;
	private int annoEsercizio;
	private String accertamento;
	private Integer annoAccertamento;
	private String subAccertamento;
	private String siope;
	private BigDecimal importo;
	private String codGestionaleEnte;
	public String getUfficio() {
		return ufficio;
	}
	public void setUfficio(String ufficio) {
		this.ufficio = ufficio;
	}
	public String getCapitolo() {
		return capitolo;
	}
	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}
	public int getAnnoEsercizio() {
		return annoEsercizio;
	}
	public void setAnnoEsercizio(int annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}
	public String getAccertamento() {
		return accertamento;
	}
	public void setAccertamento(String accertamento) {
		this.accertamento = accertamento;
	}
	public Integer getAnnoAccertamento() {
		return annoAccertamento;
	}
	public void setAnnoAccertamento(Integer annoAccertamento) {
		this.annoAccertamento = annoAccertamento;
	}
	public String getSubAccertamento() {
		return subAccertamento;
	}
	public void setSubAccertamento(String subAccertamento) {
		this.subAccertamento = subAccertamento;
	}
	public String getSiope() {
		return siope;
	}
	public void setSiope(String siope) {
		this.siope = siope;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public String getCodGestionaleEnte() {
		return codGestionaleEnte;
	}
	public void setCodGestionaleEnte(String codGestionaleEnte) {
		this.codGestionaleEnte = codGestionaleEnte;
	}
	
}