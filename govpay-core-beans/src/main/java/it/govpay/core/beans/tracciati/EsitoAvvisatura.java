/**
 * 
 */
package it.govpay.core.beans.tracciati;

import java.util.Date;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 11 lug 2018 $
 * 
 */
public class EsitoAvvisatura {

	private long numeroEsiti; 
	private String stepElaborazione;
	private String descrizioneStepElaborazione;
	private Date dataUltimoAggiornamento;
	private int percentualeStep;
	private int codiceEsitoPresaInCarico;
	private String descrizioneEsitoPresaInCarico;

	public long getNumeroEsiti() {
		return this.numeroEsiti;
	}
	public void setNumeroEsiti(long numeroEsiti) {
		this.numeroEsiti = numeroEsiti;
	}
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
	public int getPercentualeStep() {
		return this.percentualeStep;
	}
	public void setPercentualeStep(int percentualeStep) {
		this.percentualeStep = percentualeStep;
	}
	public int getCodiceEsitoPresaInCarico() {
		return this.codiceEsitoPresaInCarico;
	}
	public void setCodiceEsitoPresaInCarico(int codiceEsitoPresaInCarico) {
		this.codiceEsitoPresaInCarico = codiceEsitoPresaInCarico;
		if(codiceEsitoPresaInCarico == 0)
			this.descrizioneEsitoPresaInCarico = null;
	}
	public String getDescrizioneEsitoPresaInCarico() {
		return this.descrizioneEsitoPresaInCarico;
	}
	public void setDescrizioneEsitoPresaInCarico(String descrizioneEsitoPresaInCarico) {
		this.descrizioneEsitoPresaInCarico = descrizioneEsitoPresaInCarico;
	}
}
