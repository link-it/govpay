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

package it.govpay.bd.model;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.AnagraficaManagerNoCache;

/**
 * Rapppresenta una Unita' Operativa
 */

public class UnitaOperativa extends it.govpay.model.UnitaOperativa {
	private static final long serialVersionUID = 1L;
	
	// Business
	
	private transient Dominio dominio;
	
	public Dominio getDominio(BasicBD bd) throws ServiceException {
		return getDominio(bd, true);
	}
	
	public Dominio getDominio(BasicBD bd, boolean useCacheData) throws ServiceException {
		if(dominio == null) {
			dominio = useCacheData ? AnagraficaManager.getDominio(bd, this.getIdDominio()) : AnagraficaManagerNoCache.getDominio(bd, this.getIdDominio());
		} 
		return dominio;
	}
	
}

