package it.govpay.core.beans.tracciati;

import java.util.Date;

public class Pendenza {

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
	private String operatore;
	
	public String getStepElaborazione() {
		return stepElaborazione;
	}
	public void setStepElaborazione(String stepElaborazione) {
		this.stepElaborazione = stepElaborazione;
	}
	public String getDescrizioneStepElaborazione() {
		return descrizioneStepElaborazione;
	}
	public void setDescrizioneStepElaborazione(String descrizioneStepElaborazione) {
		this.descrizioneStepElaborazione = descrizioneStepElaborazione;
	}
	public Date getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}
	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}
	public String getOperatore() {
		return operatore;
	}
	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}
	public long getNumAddTotali() {
		return numAddTotali;
	}
	public void setNumAddTotali(long numAddTotali) {
		this.numAddTotali = numAddTotali;
	}
	public long getNumAddOk() {
		return numAddOk;
	}
	public void setNumAddOk(long numAddOk) {
		this.numAddOk = numAddOk;
	}
	public long getNumAddKo() {
		return numAddKo;
	}
	public void setNumAddKo(long numAddKo) {
		this.numAddKo = numAddKo;
	}
	public long getLineaElaborazioneAdd() {
		return lineaElaborazioneAdd;
	}
	public void setLineaElaborazioneAdd(long lineaElaborazioneAdd) {
		this.lineaElaborazioneAdd = lineaElaborazioneAdd;
	}
	public long getNumDelTotali() {
		return numDelTotali;
	}
	public void setNumDelTotali(long numDelTotali) {
		this.numDelTotali = numDelTotali;
	}
	public long getNumDelOk() {
		return numDelOk;
	}
	public void setNumDelOk(long numDelOk) {
		this.numDelOk = numDelOk;
	}
	public long getNumDelKo() {
		return numDelKo;
	}
	public void setNumDelKo(long numDelKo) {
		this.numDelKo = numDelKo;
	}
	public long getLineaElaborazioneDel() {
		return lineaElaborazioneDel;
	}
	public void setLineaElaborazioneDel(long lineaElaborazioneDel) {
		this.lineaElaborazioneDel = lineaElaborazioneDel;
	}
}
