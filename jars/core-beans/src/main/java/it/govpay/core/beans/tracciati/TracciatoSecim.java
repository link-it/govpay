package it.govpay.core.beans.tracciati;

import java.util.Date;

public class TracciatoSecim {
	
	private long numRtTotali;
	private long lineaElaborazione;
	
	private Date dataUltimoAggiornamento;
	private String stepElaborazione;
	private String descrizioneStepElaborazione;
	
	public long getNumRtTotali() {
		return numRtTotali;
	}
	public void setNumRtTotali(long numRtTotali) {
		this.numRtTotali = numRtTotali;
	}
	public long getLineaElaborazione() {
		return lineaElaborazione;
	}
	public void setLineaElaborazione(long lineaElaborazione) {
		this.lineaElaborazione = lineaElaborazione;
	}
	public Date getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}
	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}
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
}
