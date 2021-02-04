package it.govpay.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TracciatoMyPivot extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	public enum STATO_ELABORAZIONE {FILE_DUPLICATO, ERROR_LOAD, IMPORT_ESEGUITO, FILE_CARICATO, FILE_DA_CARICARE, FILE_NUOVO}
	
	public enum FORMATO_TRACCIATO { CSV };
	
	public static List<STATO_ELABORAZIONE> statiNonTerminali = new ArrayList<TracciatoMyPivot.STATO_ELABORAZIONE>();
	
	static {
		statiNonTerminali.add(STATO_ELABORAZIONE.FILE_CARICATO);
		statiNonTerminali.add(STATO_ELABORAZIONE.FILE_DA_CARICARE);
		statiNonTerminali.add(STATO_ELABORAZIONE.FILE_NUOVO);
	}
	
	private String nomeFile;
	private STATO_ELABORAZIONE stato;
	private Date dataCreazione;
	private Date dataRtDa;
	private Date dataRtA;
	private Date dataCaricamento;
	private Date dataCompletamento;
	private String requestToken;
	private String uploadUrl;
	private String authorizationToken;
	private String beanDati;
	private Long id;
	private Long idDominio;
	private byte[] rawContenuto;
	
	public String getNomeFile() {
		return nomeFile;
	}
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	public STATO_ELABORAZIONE getStato() {
		return stato;
	}
	public void setStato(STATO_ELABORAZIONE stato) {
		this.stato = stato;
	}
	public Date getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public Date getDataRtDa() {
		return dataRtDa;
	}
	public void setDataRtDa(Date dataRtDa) {
		this.dataRtDa = dataRtDa;
	}
	public Date getDataRtA() {
		return dataRtA;
	}
	public void setDataRtA(Date dataRtA) {
		this.dataRtA = dataRtA;
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
	public String getRequestToken() {
		return requestToken;
	}
	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}
	public String getUploadUrl() {
		return uploadUrl;
	}
	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}
	public String getAuthorizationToken() {
		return authorizationToken;
	}
	public void setAuthorizationToken(String authorizationToken) {
		this.authorizationToken = authorizationToken;
	}
	public String getBeanDati() {
		return beanDati;
	}
	public void setBeanDati(String beanDati) {
		this.beanDati = beanDati;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(Long idDominio) {
		this.idDominio = idDominio;
	}
	public byte[] getRawContenuto() {
		return rawContenuto;
	}
	public void setRawContenuto(byte[] rawContenuto) {
		this.rawContenuto = rawContenuto;
	}
	
	
}
