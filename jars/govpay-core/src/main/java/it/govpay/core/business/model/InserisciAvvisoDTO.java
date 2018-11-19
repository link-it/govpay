package it.govpay.core.business.model;

import it.govpay.model.avvisi.AvvisoPagamento.StatoAvviso;

public class InserisciAvvisoDTO {

	private java.lang.String codDominio;
	private java.lang.String iuv;
	private java.util.Date dataCreazione;
	private StatoAvviso stato;
	
	public java.lang.String getCodDominio() {
		return this.codDominio;
	}
	public void setCodDominio(java.lang.String codDominio) {
		this.codDominio = codDominio;
	}
	public java.lang.String getIuv() {
		return this.iuv;
	}
	public void setIuv(java.lang.String iuv) {
		this.iuv = iuv;
	}
	public java.util.Date getDataCreazione() {
		return this.dataCreazione;
	}
	public void setDataCreazione(java.util.Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public StatoAvviso getStato() {
		return this.stato;
	}
	public void setStato(StatoAvviso stato) {
		this.stato = stato;
	}
}
