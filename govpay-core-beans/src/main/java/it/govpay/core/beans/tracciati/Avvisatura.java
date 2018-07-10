/**
 * 
 */
package it.govpay.core.beans.tracciati;

import java.util.Date;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 09 lug 2018 $
 * 
 */
public class Avvisatura {

	private String intermediario;
	private long numeroAvvisi;
	private String stepElaborazione;
	private String descrizioneStepElaborazione;
	private Date dataUltimoAggiornamento;
	private int percentualeStep;
	private int codiceEsitoPresaInCarico;
	private String descrizioneEsitoPresaInCarico;

	public String getIntermediario() {
		return intermediario;
	}
	public void setIntermediario(String intermediario) {
		this.intermediario = intermediario;
	}
	public long getNumeroAvvisi() {
		return numeroAvvisi;
	}
	public void setNumeroAvvisi(long numeroAvvisi) {
		this.numeroAvvisi = numeroAvvisi;
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
	public Date getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}
	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}
	public int getPercentualeStep() {
		return percentualeStep;
	}
	public void setPercentualeStep(int percentualeStep) {
		this.percentualeStep = percentualeStep;
	}
	public int getCodiceEsitoPresaInCarico() {
		return codiceEsitoPresaInCarico;
	}
	public void setCodiceEsitoPresaInCarico(int codiceEsitoPresaInCarico) {
		this.codiceEsitoPresaInCarico = codiceEsitoPresaInCarico;
	}
	public String getDescrizioneEsitoPresaInCarico() {
		return descrizioneEsitoPresaInCarico;
	}
	public void setDescrizioneEsitoPresaInCarico(String descrizioneEsitoPresaInCarico) {
		this.descrizioneEsitoPresaInCarico = descrizioneEsitoPresaInCarico;
	}

}
