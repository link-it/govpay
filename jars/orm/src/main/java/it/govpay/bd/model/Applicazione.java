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
package it.govpay.bd.model;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;

public class Applicazione extends it.govpay.model.Applicazione{

private static final long serialVersionUID = 1L;
	
	public Applicazione() {
		super();
	}
	
	private transient Utenza utenza;
	
	public Applicazione(BDConfigWrapper configWrapper, long idUtenza) throws ServiceException {
		super();
		this.setIdUtenza(idUtenza); 
		try {
			this.setUtenza(AnagraficaManager.getUtenza(configWrapper, this.getIdUtenza()));
		} catch (NotFoundException e) {
		}
	}


	public Utenza getUtenza() {
		return this.utenza;
	}


	public void setUtenza(Utenza utenza) {
		this.utenza = utenza;
	}

	public String getPrincipal() {
		return this.utenza != null ? this.utenza.getPrincipal() : null;
	}

}
