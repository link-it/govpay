/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.web.rs.dars.model;

public class Voce<T> {
	private String etichetta;
	private T valore;
	private boolean avanzata;

	public Voce(String etichetta, T valore) {
		this(etichetta, valore, false);
	}
	
	public Voce(String etichetta, T valore, boolean avanzata) {
		this.etichetta = etichetta;
		this.valore = valore;
		this.avanzata = avanzata;
	}
	
	public String getEtichetta() {
		return this.etichetta;
	}
	
	public T getValore() {
		return this.valore;
	}

	public boolean isAvanzata() {
		return this.avanzata;
	}

	
}

