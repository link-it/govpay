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

/**
 * Rapppresenta una Unita' Operativa
 */

public class UnitaOperativa extends it.govpay.model.UnitaOperativa {
	private static final long serialVersionUID = 1L;
	
	// Business
	
	private transient Dominio dominio;
	
	public Dominio getDominio(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.dominio == null) {
			try {
				this.dominio = AnagraficaManager.getDominio(configWrapper, this.getIdDominio());
			} catch (NotFoundException e) {
			}
		} 
		return this.dominio;
	}
	
}

