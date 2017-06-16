package it.govpay.model.loader;

import java.util.Date;

public class Tracciato {

	public enum StatoTracciatoType {
		ANNULLATO ,NUOVO ,IN_CARICAMENTO ,CARICAMENTO_OK ,CARICAMENTO_KO;
	}
	
	
	private long id;
	private long idOperatore;
	private Date dataCaricamento;
	private Date dataUltimoAggiornamento;
	private StatoTracciatoType stato;
	private long lineaElaborazione;
	private String descrizioneStato;
	private long numLineeTotali;
	private long numOperazioniOk;
	private long numOperazioniKo;
	private String nomeFile;
	private byte[] rawDataRichiesta;
	private byte[] rawDataRisposta;
	
	public long getIdOperatore() {
		return idOperatore;
	}
	public void setIdOperatore(long idOperatore) {
		this.idOperatore = idOperatore;
	}
	public Date getDataCaricamento() {
		return dataCaricamento;
	}
	public void setDataCaricamento(Date dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
	}
	public StatoTracciatoType getStato() {
		return stato;
	}
	public void setStato(StatoTracciatoType stato) {
		this.stato = stato;
	}
	public long getLineaElaborazione() {
		return lineaElaborazione;
	}
	public void setLineaElaborazione(long lineaElaborazione) {
		this.lineaElaborazione = lineaElaborazione;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public long getNumLineeTotali() {
		return numLineeTotali;
	}
	public void setNumLineeTotali(long numLineeTotali) {
		this.numLineeTotali = numLineeTotali;
	}
	public long getNumOperazioniOk() {
		return numOperazioniOk;
	}
	public void setNumOperazioniOk(long numOperazioniOk) {
		this.numOperazioniOk = numOperazioniOk;
	}
	public long getNumOperazioniKo() {
		return numOperazioniKo;
	}
	public void setNumOperazioniKo(long numOperazioniKo) {
		this.numOperazioniKo = numOperazioniKo;
	}
	public String getNomeFile() {
		return nomeFile;
	}
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public Date getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}
	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}
	public byte[] getRawDataRichiesta() {
		return rawDataRichiesta;
	}
	public void setRawDataRichiesta(byte[] rawDataRichiesta) {
		this.rawDataRichiesta = rawDataRichiesta;
	}
	public byte[] getRawDataRisposta() {
		return rawDataRisposta;
	}
	public void setRawDataRisposta(byte[] rawDataRisposta) {
		this.rawDataRisposta = rawDataRisposta;
	}
	

}
