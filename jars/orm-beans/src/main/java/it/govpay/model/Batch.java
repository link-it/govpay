/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.model;

import java.util.Date;

public class Batch {
	
	private String codBatch;
	private String nodo;
	private Date inizio;
	private Date aggiornamento;
	public String getCodBatch() {
		return this.codBatch;
	}
	public void setCodBatch(String codBatch) {
		this.codBatch = codBatch;
	}
	public String getNodo() {
		return this.nodo;
	}
	public void setNodo(String nodo) {
		this.nodo = nodo;
	}
	public Date getInizio() {
		return this.inizio;
	}
	public void setInizio(Date inizio) {
		this.inizio = inizio;
	}
	public Date getAggiornamento() {
		return this.aggiornamento;
	}
	public void setAggiornamento(Date aggiornamento) {
		this.aggiornamento = aggiornamento;
	}

}
