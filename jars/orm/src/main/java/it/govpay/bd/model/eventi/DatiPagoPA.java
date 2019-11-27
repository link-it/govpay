package it.govpay.bd.model.eventi;

import java.io.Serializable;

import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Canale.TipoVersamento;

public class DatiPagoPA implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DatiPagoPA() {
//		this.codPsp = Rpt.codPspWISP20;
//		this.codCanale = Rpt.codCanaleWISP20;
//		this.tipoVersamento = Rpt.tipoVersamentoWISP20;
//		this.codIntermediarioPsp = Rpt.codIntermediarioPspWISP20;
//		this.modelloPagamento = Rpt.modelloPagamentoWISP20;
	}
	
	private String codPsp;
	private TipoVersamento tipoVersamento;
	private ModelloPagamento modelloPagamento;
	private String fruitore;
	private String erogatore;
	private String codStazione;
	private String codCanale;
	private String codIntermediario;
	private String codIntermediarioPsp;
	private String codDominio;
	private String codFlusso;
	private String trn;
	private String sct;
	private Long idTracciato;

	public String getCodPsp() {
		return codPsp;
	}
	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}
	public TipoVersamento getTipoVersamento() {
		return tipoVersamento;
	}
	public void setTipoVersamento(TipoVersamento tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}
	public String getFruitore() {
		return fruitore;
	}
	public void setFruitore(String fruitore) {
		this.fruitore = fruitore;
	}
	public String getErogatore() {
		return erogatore;
	}
	public void setErogatore(String erogatore) {
		this.erogatore = erogatore;
	}
	public String getCodStazione() {
		return codStazione;
	}
	public void setCodStazione(String codStazione) {
		this.codStazione = codStazione;
	}
	public String getCodCanale() {
		return codCanale;
	}
	public void setCodCanale(String codCanale) {
		this.codCanale = codCanale;
	}
	public ModelloPagamento getModelloPagamento() {
		return modelloPagamento;
	}
	public void setModelloPagamento(ModelloPagamento modelloPagamento) {
		this.modelloPagamento = modelloPagamento;
	}
	public String getCodIntermediario() {
		return codIntermediario;
	}
	public void setCodIntermediario(String codIntermediario) {
		this.codIntermediario = codIntermediario;
	}
	public String getCodIntermediarioPsp() {
		return codIntermediarioPsp;
	}
	public void setCodIntermediarioPsp(String codIntermediarioPsp) {
		this.codIntermediarioPsp = codIntermediarioPsp;
	}
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getCodFlusso() {
		return codFlusso;
	}
	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
	}
	public String getTrn() {
		return trn;
	}
	public void setTrn(String trn) {
		this.trn = trn;
	}
	public Long getIdTracciato() {
		return idTracciato;
	}
	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}
	public String getSct() {
		return sct;
	}
	public void setSct(String sct) {
		this.sct = sct;
	}
	
}
