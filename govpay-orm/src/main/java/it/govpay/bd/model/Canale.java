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
import it.govpay.model.Psp;

public class Canale extends it.govpay.model.Canale {
	
	private static final long serialVersionUID = 1L;
	 
	
	// Business
	
	private Psp psp;
	
	public Psp getPsp(BasicBD bd) throws ServiceException {
		if(psp == null) {
			psp = AnagraficaManager.getPsp(bd, this.getIdPsp());
		}
		return psp;
	}
	
	public void setPsp(Psp psp) throws ServiceException {
		this.psp = psp;
	}
}
