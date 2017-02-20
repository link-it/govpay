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

package it.govpay.model;

public class Stazione extends BasicModel {
	private static final long serialVersionUID = 1L;

	private Long id; 
	private long idIntermediario;
	private String codStazione;
	private String password;
	private boolean abilitato;
	private int applicationCode;
	private int ndpStato;
	private String ndpOperazione;
	private String ndpDescrizione;

	public Stazione() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getIdIntermediario() {
		return idIntermediario;
	}

	public void setIdIntermediario(long idIntermediario) {
		this.idIntermediario = idIntermediario;
	}

	public String getCodStazione() {
		return codStazione;
	}

	public void setCodStazione(String codStazione) {
		this.codStazione = codStazione;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAbilitato() {
		return abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}


	public int getApplicationCode() {
		return applicationCode;
	}

	public void setApplicationCode(int applicationCode) {
		this.applicationCode = applicationCode;
	}
	
	public int getNdpStato() {
		return ndpStato;
	}

	public void setNdpStato(int ndpStato) {
		this.ndpStato = ndpStato;
	}

	public String getNdpOperazione() {
		return ndpOperazione;
	}

	public void setNdpOperazione(String ndpOperazione) {
		this.ndpOperazione = ndpOperazione;
	}

	public String getNdpDescrizione() {
		return ndpDescrizione;
	}

	public void setNdpDescrizione(String ndpDescrizione) {
		this.ndpDescrizione = ndpDescrizione;
	}

}

