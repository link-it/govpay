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
import it.govpay.model.Intermediario;

public class Stazione extends it.govpay.model.Stazione {
	private static final long serialVersionUID = 1L;

	// Business
	
	private transient Intermediario intermediario;
	
	public Intermediario getIntermediario(BasicBD bd) throws ServiceException {
		if(intermediario == null) {
			intermediario = AnagraficaManager.getIntermediario(bd, this.getIdIntermediario());
		}
		return intermediario;
	}
	
	public void setIntermediario(Intermediario intermediario) {
		this.intermediario = intermediario;
	}

}

