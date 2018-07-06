package it.govpay.model;

import java.util.Date;

public class Tracciato extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public Date getDataCaricamento() {
		return dataCaricamento;
	}
	public void setDataCaricamento(Date dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
	}
	public Date getDataCompletamento() {
		return dataCompletamento;
	}
	public void setDataCompletamento(Date dataCompletamento) {
		this.dataCompletamento = dataCompletamento;
	}
	public String getBeanDati() {
		return beanDati;
	}
	public void setBeanDati(String beanDati) {
		this.beanDati = beanDati;
	}
	public String getFileNameRichiesta() {
		return fileNameRichiesta;
	}
	public void setFileNameRichiesta(String fileNameRichiesta) {
		this.fileNameRichiesta = fileNameRichiesta;
	}
	public byte[] getRawRichiesta() {
		return rawRichiesta;
	}
	public void setRawRichiesta(byte[] rawRichiesta) {
		this.rawRichiesta = rawRichiesta;
	}
	public String getFileNameEsito() {
		return fileNameEsito;
	}
	public void setFileNameEsito(String fileNameEsito) {
		this.fileNameEsito = fileNameEsito;
	}
	public byte[] getRawEsito() {
		return rawEsito;
	}
	public void setRawEsito(byte[] rawEsito) {
		this.rawEsito = rawEsito;
	}
	private String tipo;
	private String stato;
	private String descrizioneStato;
	private Date dataCaricamento;
	private Date dataCompletamento;
	private String beanDati;
	private String fileNameRichiesta;
	private byte[] rawRichiesta;
	private String fileNameEsito;
	private byte[] rawEsito;

}
