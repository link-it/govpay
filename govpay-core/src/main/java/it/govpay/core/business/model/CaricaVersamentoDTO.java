/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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

package it.govpay.core.business.model;

import it.govpay.bd.model.Versamento;
import it.govpay.model.Applicazione;
import it.govpay.bd.model.Operatore;

public class CaricaVersamentoDTO {
	
	private Operatore operatore;
	private Applicazione applicazione;
	private Versamento versamento;
	private boolean generaIuv;
	private boolean aggiornaSeEsiste;
	
	public CaricaVersamentoDTO(Operatore operatore, Versamento versamento) {
		this.operatore = operatore;
		this.versamento = versamento;
	}
	
	public CaricaVersamentoDTO(Applicazione applicazione, Versamento versamento) {
		this.applicazione = applicazione;
		this.versamento = versamento;
	}
	
	public Applicazione getApplicazione() {
		return this.applicazione;
	}
	
	public it.govpay.bd.model.Versamento getVersamento() {
		return this.versamento;
	}
	
	public Operatore getOperatore() {
		return this.operatore;
	}
	
	public boolean isGeneraIuv() {
		return this.generaIuv;
	}
	
	public void setGeneraIuv(boolean generaIuv) {
		this.generaIuv = generaIuv;
	}
	
	public boolean isAggiornaSeEsiste() {
		return this.aggiornaSeEsiste;
	}
	
	public void setAggiornaSeEsiste(boolean aggiornaSeEsiste) {
		this.aggiornaSeEsiste = aggiornaSeEsiste;
	}

}
