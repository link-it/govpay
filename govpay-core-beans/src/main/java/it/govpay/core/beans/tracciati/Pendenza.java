package it.govpay.core.beans.tracciati;

import java.util.Date;

public class Pendenza {

	private long numLineeTotali;
	private long numOperazioniOk;
	private long numOperazioniKo;
	private long lineaElaborazione;
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
	public String getOperatore() {
		return operatore;
	}
	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}
	
	
}
