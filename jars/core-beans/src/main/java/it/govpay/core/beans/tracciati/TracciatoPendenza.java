package it.govpay.core.beans.tracciati;

import java.util.Date;

public class TracciatoPendenza {

	private long numAddTotali;
	private long numAddOk;
	private long numAddKo;
	private long lineaElaborazioneAdd;
	
	private long numDelTotali;
	private long numDelOk;
	private long numDelKo;
	private long lineaElaborazioneDel;
	
	private Date dataUltimoAggiornamento;
	private String stepElaborazione;
	private String descrizioneStepElaborazione;
	
	private Boolean avvisaturaAbilitata;
	private String avvisaturaModalita;
	
	public String getStepElaborazione() {
		return this.stepElaborazione;
	}
	public void setStepElaborazione(String stepElaborazione) {
		this.stepElaborazione = stepElaborazione;
	}
	public String getDescrizioneStepElaborazione() {
		return this.descrizioneStepElaborazione;
	}
	public void setDescrizioneStepElaborazione(String descrizioneStepElaborazione) {
		this.descrizioneStepElaborazione = descrizioneStepElaborazione;
	}
	public Date getDataUltimoAggiornamento() {
		return this.dataUltimoAggiornamento;
	}
	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}
	public long getNumAddTotali() {
		return this.numAddTotali;
	}
	public void setNumAddTotali(long numAddTotali) {
		this.numAddTotali = numAddTotali;
	}
	public long getNumAddOk() {
		return this.numAddOk;
	}
	public void setNumAddOk(long numAddOk) {
		this.numAddOk = numAddOk;
	}
	public long getNumAddKo() {
		return this.numAddKo;
	}
	public void setNumAddKo(long numAddKo) {
		this.numAddKo = numAddKo;
	}
	public long getLineaElaborazioneAdd() {
		return this.lineaElaborazioneAdd;
	}
	public void setLineaElaborazioneAdd(long lineaElaborazioneAdd) {
		this.lineaElaborazioneAdd = lineaElaborazioneAdd;
	}
	public long getNumDelTotali() {
		return this.numDelTotali;
	}
	public void setNumDelTotali(long numDelTotali) {
		this.numDelTotali = numDelTotali;
	}
	public long getNumDelOk() {
		return this.numDelOk;
	}
	public void setNumDelOk(long numDelOk) {
		this.numDelOk = numDelOk;
	}
	public long getNumDelKo() {
		return this.numDelKo;
	}
	public void setNumDelKo(long numDelKo) {
		this.numDelKo = numDelKo;
	}
	public long getLineaElaborazioneDel() {
		return this.lineaElaborazioneDel;
	}
	public void setLineaElaborazioneDel(long lineaElaborazioneDel) {
		this.lineaElaborazioneDel = lineaElaborazioneDel;
	}
	public Boolean getAvvisaturaAbilitata() {
		return avvisaturaAbilitata;
	}
	public void setAvvisaturaAbilitata(Boolean avvisaturaAbilitata) {
		this.avvisaturaAbilitata = avvisaturaAbilitata;
	}
	public String getAvvisaturaModalita() {
		return avvisaturaModalita;
	}
	public void setAvvisaturaModalita(String avvisaturaModalita) {
		this.avvisaturaModalita = avvisaturaModalita;
	}
}
