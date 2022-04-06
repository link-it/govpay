package it.govpay.model;

import java.util.Date;

public class Allegato extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long idVersamento;
	private Date dataCreazione;
	private String nome;
	private String tipo;
	private byte[] rawContenuto;
	
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
	public Date getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public byte[] getRawContenuto() {
		return rawContenuto;
	}
	public void setRawContenuto(byte[] rawContenuto) {
		this.rawContenuto = rawContenuto;
	}	
}
