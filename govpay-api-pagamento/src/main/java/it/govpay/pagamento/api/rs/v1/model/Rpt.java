package it.govpay.pagamento.api.rs.v1.model;

import java.util.Date;

public class Rpt {
	private String idDominio = null;
	private String iuv = null;
	private String ccp = null;
	private String idA2A = null;
	private String idPendenza = null;
	private Canale canalePagamento = null;
	private String modelloPagamento = null;
	private String stato = null;
	private String dettaglioStato = null;
	private Date dataRpt= null;
	private Date dataRt= null;
	private String esitoRt = null;
	private double importoRt;
	
	public String getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCcp() {
		return ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
	public String getIdA2A() {
		return idA2A;
	}
	public void setIdA2A(String idA2A) {
		this.idA2A = idA2A;
	}
	public String getIdPendenza() {
		return idPendenza;
	}
	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}
	public Canale getCanalePagamento() {
		return canalePagamento;
	}
	public void setCanalePagamento(Canale canalePagamento) {
		this.canalePagamento = canalePagamento;
	}
	public String getModelloPagamento() {
		return modelloPagamento;
	}
	public void setModelloPagamento(String modelloPagamento) {
		this.modelloPagamento = modelloPagamento;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getDettaglioStato() {
		return dettaglioStato;
	}
	public void setDettaglioStato(String dettaglioStato) {
		this.dettaglioStato = dettaglioStato;
	}
	public Date getDataRpt() {
		return dataRpt;
	}
	public void setDataRpt(Date dataRpt) {
		this.dataRpt = dataRpt;
	}
	public Date getDataRt() {
		return dataRt;
	}
	public void setDataRt(Date dataRt) {
		this.dataRt = dataRt;
	}
	public String getEsitoRt() {
		return esitoRt;
	}
	public void setEsitoRt(String esitoRt) {
		this.esitoRt = esitoRt;
	}
	public double getImportoRt() {
		return importoRt;
	}
	public void setImportoRt(double importoRt) {
		this.importoRt = importoRt;
	}
	
}
