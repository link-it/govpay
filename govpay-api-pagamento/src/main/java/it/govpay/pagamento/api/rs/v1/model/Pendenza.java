package it.govpay.pagamento.api.rs.v1.model;

import java.util.Date;
import java.util.List;

public class Pendenza {

	private String idA2A = null;
	private String idPendenza = null;
	private String idDominio = null;
	private String idUnitaOperativa = null;
	private String idCartellaPagamento = null;
	private Anagrafica soggettoPagatore = null;
	private double importo;
	private String causale = null;
	private Date dataValidita = null;
	private Date dataScadenza = null;
	private int annoRiferimento;
	private String datiAllegati = null;
	private String tassonomia = null;
	private List<VocePendenza> voci = null;
	private String iuv = null;
	private String stato = null;
	private Date dataCaricamento = null;
	private List<Rpt> rpts = null;
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
	public String getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}
	public String getIdUnitaOperativa() {
		return idUnitaOperativa;
	}
	public void setIdUnitaOperativa(String idUnitaOperativa) {
		this.idUnitaOperativa = idUnitaOperativa;
	}
	public String getIdCartellaPagamento() {
		return idCartellaPagamento;
	}
	public void setIdCartellaPagamento(String idCartellaPagamento) {
		this.idCartellaPagamento = idCartellaPagamento;
	}
	public Anagrafica getSoggettoPagatore() {
		return soggettoPagatore;
	}
	public void setSoggettoPagatore(Anagrafica soggettoPagatore) {
		this.soggettoPagatore = soggettoPagatore;
	}
	public double getImporto() {
		return importo;
	}
	public void setImporto(double importo) {
		this.importo = importo;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public Date getDataValidita() {
		return dataValidita;
	}
	public void setDataValidita(Date dataValidita) {
		this.dataValidita = dataValidita;
	}
	public Date getDataScadenza() {
		return dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public int getAnnoRiferimento() {
		return annoRiferimento;
	}
	public void setAnnoRiferimento(int annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}
	public String getDatiAllegati() {
		return datiAllegati;
	}
	public void setDatiAllegati(String datiAllegati) {
		this.datiAllegati = datiAllegati;
	}
	public String getTassonomia() {
		return tassonomia;
	}
	public void setTassonomia(String tassonomia) {
		this.tassonomia = tassonomia;
	}
	public List<VocePendenza> getVoci() {
		return voci;
	}
	public void setVoci(List<VocePendenza> voci) {
		this.voci = voci;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public Date getDataCaricamento() {
		return dataCaricamento;
	}
	public void setDataCaricamento(Date dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
	}
	public List<Rpt> getRpts() {
		return rpts;
	}
	public void setRpts(List<Rpt> rpts) {
		this.rpts = rpts;
	}
	
	
}
