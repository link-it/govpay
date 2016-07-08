package it.govpay.web.rs.model;

import java.util.Date;

import it.govpay.bd.model.BasicModel;

public class Versamento extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Versamento(){super();}

	private String bundleKey;
	private String codiceCreditore;
	private String codiceTributo;
	private String codiceFiscaleContribuente;
	private String anagraficaContribuente;
	private String identificativoDebito;
	private String identificativoVersamento;
	private Double importo;
	private Date dataScadenza;
	private String causale;
	private String note;
	
	public Double getImporto() {
		return importo;
	}
	public void setImporto(Double importo) {
		this.importo = importo;
	}
	public Date getDataScadenza() {
		return dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public String getCodiceCreditore() {
		return codiceCreditore;
	}
	public void setCodiceCreditore(String codiceCreditore) {
		this.codiceCreditore = codiceCreditore;
	}
	public String getCodiceTributo() {
		return codiceTributo;
	}
	public void setCodiceTributo(String codiceTributo) {
		this.codiceTributo = codiceTributo;
	}
	public String getCodiceFiscaleContribuente() {
		return codiceFiscaleContribuente;
	}
	public void setCodiceFiscaleContribuente(String codiceFiscaleContribuente) {
		this.codiceFiscaleContribuente = codiceFiscaleContribuente;
	}
	public String getAnagraficaContribuente() {
		return anagraficaContribuente;
	}
	public void setAnagraficaContribuente(String anagraficaContribuente) {
		this.anagraficaContribuente = anagraficaContribuente;
	}
	public String getIdentificativoDebito() {
		return identificativoDebito;
	}
	public void setIdentificativoDebito(String identificativoDebito) {
		this.identificativoDebito = identificativoDebito;
	}
	public String getIdentificativoVersamento() {
		return identificativoVersamento;
	}
	public void setIdentificativoVersamento(String identificativoVersamento) {
		this.identificativoVersamento = identificativoVersamento;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getBundleKey() {
		return bundleKey;
	}
	public void setBundleKey(String bundleKey) {
		this.bundleKey = bundleKey;
	}
}
