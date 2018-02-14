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

import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.GovpayConfig;


public class Psp extends it.govpay.model.Psp {	
	
	private static final long serialVersionUID = 1L;
	 
	private transient List<Canale> listaCanali;
	
	public List<Canale> getCanalis() {
		if(listaCanali == null) 
			listaCanali = new ArrayList<Canale>();
		return listaCanali;
	}

	public void setCanalis(List<Canale> canali) {
		this.listaCanali = canali;
	}

	public boolean isPostale() {
		return GovpayConfig.getInstance().getPspPostali().contains(super.getCodPsp());
	}

}
