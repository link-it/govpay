package it.govpay.web.rs.model;

import java.util.Date;

import it.govpay.bd.model.BasicModel;

public class Pagamento extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Pagamento(){super();}
	
	private String identificativoVersamento;
	private Double importoPagato;
	private Date dataPagamento;
	private String codiceRiversamento;
	private String codiceRendicontazione;
	private String note;

	public String getIdentificativoVersamento() {
		return identificativoVersamento;
	}
	public void setIdentificativoVersamento(String identificativoVersamento) {
		this.identificativoVersamento = identificativoVersamento;
	}
	public Double getImportoPagato() {
		return importoPagato;
	}
	public void setImportoPagato(Double importoPagato) {
		this.importoPagato = importoPagato;
	}
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	public String getCodiceRiversamento() {
		return codiceRiversamento;
	}
	public void setCodiceRiversamento(String codiceRiversamento) {
		this.codiceRiversamento = codiceRiversamento;
	}
	public String getCodiceRendicontazione() {
		return codiceRendicontazione;
	}
	public void setCodiceRendicontazione(String codiceRendicontazione) {
		this.codiceRendicontazione = codiceRendicontazione;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	

}
