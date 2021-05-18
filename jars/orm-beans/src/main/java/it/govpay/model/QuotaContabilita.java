package it.govpay.model;

import java.math.BigDecimal;

public class QuotaContabilita {

	private String capitolo;
	private int annoEsercizio;
	private String accertamento;
	private BigDecimal importo;
	private String proprietaCustom;
	
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
	public String getProprietaCustom() {
		return proprietaCustom;
	}
	public void setProprietaCustom(String proprietaCustom) {
		this.proprietaCustom = proprietaCustom;
	} 
	
	
}