package it.govpay.bd.model;

import java.util.Date;

public class EstrattoConto extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date dataPagamento;
	private Double importoPagato;
	private String iur;
	private String iuv;
	private String codFlussoRendicontazione;
	private String codSingoloVersamentoEnte;
	private String note;
	private String codBicRiversamento;
	private String idRegolamento;
	private String ibanAccredito;
	
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
	public String getCodFlussoRendicontazione() {
		return codFlussoRendicontazione;
	}
	public void setCodFlussoRendicontazione(String codFlussoRendicontazione) {
		this.codFlussoRendicontazione = codFlussoRendicontazione;
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
	public String getIbanAccredito() {
		return ibanAccredito;
	}
	public void setIbanAccredito(String ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}

}
