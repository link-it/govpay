package it.govpay.model;

import java.util.Date;

import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.Versamento.StatoVersamento;

public class EstrattoConto {

	private Date dataPagamento;
	private Double importoPagato;
	private Double importoDovuto;
	private String iur;
	private String iuv;
	private String codSingoloVersamentoEnte;
	private String note;
	private String ibanAccredito;
	private String codFlussoRendicontazione;
	private StatoVersamento statoVersamento;	
	private String causale;
	// Integrazione CSV
	private String codVersamentoEnte;
	private StatoSingoloVersamento statoSingoloVersamento;
	// dalla tabella FR.
	private String codBicRiversamento;
	private String idRegolamento;
	private Long idPagamento;
	private Long idSingoloVersamento;
	private Long idVersamento;
	private Long idRr;
	
	private String debitoreIdentificativo;
	
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	public Double getImportoPagato() {
		return importoPagato;
	}
	public void setImportoPagato(Double importoPagato) {
		this.importoPagato = importoPagato;
	}
	public Double getImportoDovuto() {
		return importoDovuto;
	}
	public void setImportoDovuto(Double importoDovuto) {
		this.importoDovuto = importoDovuto;
	}
	public String getIur() {
		return iur;
	}
	public void setIur(String iur) {
		this.iur = iur;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCodSingoloVersamentoEnte() {
		return codSingoloVersamentoEnte;
	}
	public void setCodSingoloVersamentoEnte(String codSingoloVersamentoEnte) {
		this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getIbanAccredito() {
		return ibanAccredito;
	}
	public void setIbanAccredito(String ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}
	public String getCodFlussoRendicontazione() {
		return codFlussoRendicontazione;
	}
	public void setCodFlussoRendicontazione(String codFlussoRendicontazione) {
		this.codFlussoRendicontazione = codFlussoRendicontazione;
	}
	public StatoVersamento getStatoVersamento() {
		return statoVersamento;
	}
	public void setStatoVersamento(StatoVersamento statoVersamento) {
		this.statoVersamento = statoVersamento;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public String getCodVersamentoEnte() {
		return codVersamentoEnte;
	}
	public void setCodVersamentoEnte(String codVersamentoEnte) {
		this.codVersamentoEnte = codVersamentoEnte;
	}
	public StatoSingoloVersamento getStatoSingoloVersamento() {
		return statoSingoloVersamento;
	}
	public void setStatoSingoloVersamento(StatoSingoloVersamento statoSingoloVersamento) {
		this.statoSingoloVersamento = statoSingoloVersamento;
	}
	public String getCodBicRiversamento() {
		return codBicRiversamento;
	}
	public void setCodBicRiversamento(String codBicRiversamento) {
		this.codBicRiversamento = codBicRiversamento;
	}
	public String getIdRegolamento() {
		return idRegolamento;
	}
	public void setIdRegolamento(String idRegolamento) {
		this.idRegolamento = idRegolamento;
	}
	public String getDebitoreIdentificativo() {
		return debitoreIdentificativo;
	}
	public void setDebitoreIdentificativo(String debitoreIdentificativo) {
		this.debitoreIdentificativo = debitoreIdentificativo;
	}
	public Long getIdPagamento() {
		return idPagamento;
	}
	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}
	public Long getIdSingoloVersamento() {
		return idSingoloVersamento;
	}
	public void setIdSingoloVersamento(Long idSingoloVersamento) {
		this.idSingoloVersamento = idSingoloVersamento;
	}
	public Long getIdVersamento() {
		return idVersamento;
	}
	public void setIdVersamento(Long idVersamento) {
		this.idVersamento = idVersamento;
	}
	public Long getIdRr() {
		return idRr;
	}
	public void setIdRr(Long idRr) {
		this.idRr = idRr;
	}
}
