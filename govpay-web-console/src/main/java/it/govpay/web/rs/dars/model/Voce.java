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
package it.govpay.web.rs.dars.model;

public class Voce<T> {
	
	public enum TipoVoce { TEXT, IMAGE};
	
	private String etichetta;
	private T valore;
	private boolean avanzata;
	private TipoVoce tipo;
	
	public Voce(String etichetta, T valore) {
		this(etichetta, valore, false);
	}

	public Voce(String etichetta, T valore,boolean avanzata) {
		this(etichetta, valore, false, TipoVoce.TEXT);
	}
	
	public Voce(String etichetta, T valore, boolean avanzata, TipoVoce tipo) {
		this.etichetta = etichetta;
		this.valore = valore;
		this.avanzata = avanzata;
		this.tipo= tipo;
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

	public String getTipo() {
		return tipo != null ? this.tipo.name() : TipoVoce.TEXT.name();
	}

	public void setTipo(TipoVoce tipo) {
		this.tipo = tipo;
	}
}

