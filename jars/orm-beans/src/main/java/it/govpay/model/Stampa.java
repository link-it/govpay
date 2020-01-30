package it.govpay.model;

import java.util.Date;

public class Stampa extends BasicModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum TIPO {AVVISO};
	
	private Long id;
	private Long idVersamento;
	private TIPO tipo;
	private byte [] pdf;
	private Date dataCreazione;
	
	
	@Override
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdVersamento() {
		return idVersamento;
	}
	public void setIdVersamento(Long idVersamento) {
		this.idVersamento = idVersamento;
	}
	public TIPO getTipo() {
		return tipo;
	}
	public void setTipo(TIPO tipo) {
		this.tipo = tipo;
	}
	public byte[] getPdf() {
		return pdf;
	}
	public void setPdf(byte[] pdf) {
		this.pdf = pdf;
	}
	public Date getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	
	
}
