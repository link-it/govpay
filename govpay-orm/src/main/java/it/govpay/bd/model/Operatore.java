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

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.model.Tributo;
import it.govpay.model.Utenza;

public class Operatore extends it.govpay.model.Operatore{
	
	private static final long serialVersionUID = 1L;
	
	public Operatore() {
		super();
	}
	
	private transient List<Dominio> domini;
	private transient List<Tributo> tributi;
	private transient Utenza utenza;
	
	
	public Operatore(BasicBD bd, long idUtenza) throws ServiceException {
		super();
		this.setIdUtenza(idUtenza); 
		this.setUtenza(AnagraficaManager.getUtenza(bd, this.getIdUtenza())); 
	}


	public Utenza getUtenza() {
		return utenza;
	}


	public void setUtenza(Utenza utenza) {
		this.utenza = utenza;
	}

	public String getPrincipal() {
		return this.utenza != null ? this.utenza.getPrincipal() : null;
	}
}

