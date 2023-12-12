/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
		return this.intermediario;
	}
	public void setIntermediario(String intermediario) {
		this.intermediario = intermediario;
	}
	public long getNumeroAvvisi() {
		return this.numeroAvvisi;
	}
	public void setNumeroAvvisi(long numeroAvvisi) {
		this.numeroAvvisi = numeroAvvisi;
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
