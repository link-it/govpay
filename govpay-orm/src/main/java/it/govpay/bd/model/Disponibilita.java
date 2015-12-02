/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.bd.model;

import java.util.List;


public class Disponibilita extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum TipoPeriodo {GIORNALIERA, SETTIMANALE, MENSILE, ANNUALE}
	public enum TipoDisponibilita {DISPONIBILE, NON_DISPONIBILE}
	
	private Long id;
	private TipoPeriodo tipoPeriodo;
	private List<Periodo> fasceOrarieLst;
	private String giorno;
	private TipoDisponibilita tipoDisponibilita;
	
	public Disponibilita() { }

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public TipoPeriodo getTipoPeriodo() {
		return tipoPeriodo;
	}
	public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}
	public String getGiorno() {
		return giorno;
	}
	public void setGiorno(String giorno) {
		this.giorno = giorno;
	}
	public TipoDisponibilita getTipoDisponibilita() {
		return tipoDisponibilita;
	}
	public void setTipoDisponibilita(TipoDisponibilita tipoDisponibilita) {
		this.tipoDisponibilita = tipoDisponibilita;
	}
	
	@Override
	public boolean equals(Object obj) {
		Disponibilita disponibilita = null;
		if(obj instanceof Disponibilita) {
			disponibilita = (Disponibilita) obj;
		} else {
			return false;
		}
		
		boolean equal =
				equals(tipoPeriodo, disponibilita.getTipoPeriodo()) &&
				equals(giorno, disponibilita.getGiorno()) &&
				equals(fasceOrarieLst, disponibilita.getFasceOrarieLst()) &&
				equals(tipoDisponibilita, disponibilita.getTipoDisponibilita());
		return equal;
	}
	public List<Periodo> getFasceOrarieLst() {
		return fasceOrarieLst;
	}
	public void setFasceOrarieLst(List<Periodo> fasceOrarieLst) {
		this.fasceOrarieLst = fasceOrarieLst;
	}
	
}
