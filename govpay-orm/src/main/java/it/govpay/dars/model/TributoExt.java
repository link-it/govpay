/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.dars.model;

import java.io.Serializable;

import it.govpay.bd.model.Ente;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Tributo;

public class TributoExt implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Ente ente = null;
	private IbanAccredito ibanAccredito = null;
	private Tributo tributo = null;
	
	public TributoExt(){}
	
	public TributoExt(Tributo tributo, Ente ente, IbanAccredito ibanAccredito){
		this.setTributo(tributo);
		this.setEnte(ente);
		this.setIbanAccredito(ibanAccredito);
	}

	public Ente getEnte() { 
		return ente;
	}

	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	public Tributo getTributo() {
		return tributo;
	}

	public void setTributo(Tributo tributo) {
		this.tributo = tributo;
	}

	public IbanAccredito getIbanAccredito() {
		return ibanAccredito;
	}

	public void setIbanAccredito(IbanAccredito ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}
}
