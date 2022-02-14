package it.govpay.model;

import java.math.BigDecimal;

public class QuotaContabilita {

	private String capitolo;
	private int annoEsercizio;
	private String accertamento;
	private BigDecimal importo;
	private Object proprietaCustom;
	private String titolo;
	private String tipologia;
	private String categoria;
	private String articolo;

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
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public Object getProprietaCustom() {
		return proprietaCustom;
	}
	public void setProprietaCustom(Object proprietaCustom) {
		this.proprietaCustom = proprietaCustom;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getTipologia() {
		return tipologia;
	}
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getArticolo() {
		return articolo;
	}
	public void setArticolo(String articolo) {
		this.articolo = articolo;
	} 


}