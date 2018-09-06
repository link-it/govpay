package it.govpay.model;

import java.util.Date;

public class Tracciato extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String getDescrizioneStato() {
		return this.descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public Date getDataCaricamento() {
		return this.dataCaricamento;
	}
	public void setDataCaricamento(Date dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
	}
	public Date getDataCompletamento() {
		return this.dataCompletamento;
	}
	public void setDataCompletamento(Date dataCompletamento) {
		this.dataCompletamento = dataCompletamento;
	}
	public String getBeanDati() {
		return this.beanDati;
	}
	public void setBeanDati(String beanDati) {
		this.beanDati = beanDati;
	}
	public String getFileNameRichiesta() {
		return this.fileNameRichiesta;
	}
	public void setFileNameRichiesta(String fileNameRichiesta) {
		this.fileNameRichiesta = fileNameRichiesta;
	}
	public byte[] getRawRichiesta() {
		return this.rawRichiesta;
	}
	public void setRawRichiesta(byte[] rawRichiesta) {
		this.rawRichiesta = rawRichiesta;
	}
	public String getFileNameEsito() {
		return this.fileNameEsito;
	}
	public void setFileNameEsito(String fileNameEsito) {
		this.fileNameEsito = fileNameEsito;
	}
	public byte[] getRawEsito() {
		return this.rawEsito;
	}
	public void setRawEsito(byte[] rawEsito) {
		this.rawEsito = rawEsito;
	}
	
	public TIPO_TRACCIATO getTipo() {
		return this.tipo;
	}
	public void setTipo(TIPO_TRACCIATO tipo) {
		this.tipo = tipo;
	}

	public STATO_ELABORAZIONE getStato() {
		return this.stato;
	}
	public void setStato(STATO_ELABORAZIONE stato) {
		this.stato = stato;
	}

	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getCodDominio() {
		return this.codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	
	public Long getIdOperatore() {
		return this.idOperatore;
	}
	public void setIdOperatore(Long idOperatore) {
		this.idOperatore = idOperatore;
	}

	public enum STATO_ELABORAZIONE {ELABORAZIONE, COMPLETATO, SCARTATO}
	
	public enum TIPO_TRACCIATO {AV, AV_ESITO, PENDENZA}
	
	private TIPO_TRACCIATO tipo;
	private STATO_ELABORAZIONE stato;
	private String descrizioneStato;
	private String codDominio;
	private Date dataCaricamento;
	private Date dataCompletamento;
	private String beanDati;
	private String fileNameRichiesta;
	private byte[] rawRichiesta;
	private String fileNameEsito;
	private byte[] rawEsito;
	private Long id;
	private Long idOperatore;



}
