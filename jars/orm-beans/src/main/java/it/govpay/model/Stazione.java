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

public class Stazione extends BasicModel {
	private static final long serialVersionUID = 1L;

	private Long id; 
	private long idIntermediario;
	private String codStazione;
	private String password;
	private boolean abilitato;
	private int applicationCode;
	private Versione versione;

	public enum Versione {

		V2,
		V1;

		public static Versione toEnum(String s) {
			try {
				return Versione.valueOf(s);
			} catch (IllegalArgumentException e) {
				return V1;
			}
		}
	}

	public Stazione() {
		//donothing
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getIdIntermediario() {
		return this.idIntermediario;
	}

	public void setIdIntermediario(long idIntermediario) {
		this.idIntermediario = idIntermediario;
	}

	public String getCodStazione() {
		return this.codStazione;
	}

	public void setCodStazione(String codStazione) {
		this.codStazione = codStazione;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAbilitato() {
		return this.abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}


	public int getApplicationCode() {
		return this.applicationCode;
	}

	public void setApplicationCode(int applicationCode) {
		this.applicationCode = applicationCode;
	}

	public Versione getVersione() {
		return versione;
	}

	public void setVersione(Versione versione) {
		this.versione = versione;
	}

}

