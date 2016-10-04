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

package it.govpay.bd.model;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.model.BasicModel;
import it.govpay.model.Intermediario;

public class Stazione extends BasicModel {
	private static final long serialVersionUID = 1L;

	private Long id; 
	private long idIntermediario;
	private String codStazione;
	private String password;
	private boolean abilitato;
	private int applicationCode;

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


	// Business
	
	private Intermediario intermediario;
	
	public Intermediario getIntermediario(BasicBD bd) throws ServiceException {
		if(intermediario == null) {
			intermediario = AnagraficaManager.getIntermediario(bd, idIntermediario);
		}
		return intermediario;
	}
	
	public void setIntermediario(Intermediario intermediario) {
		this.intermediario = intermediario;
	}

}

