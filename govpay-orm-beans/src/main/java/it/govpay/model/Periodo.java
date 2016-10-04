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
package it.govpay.model;

import it.govpay.bd.model.BasicModel;
import it.govpay.bd.model.Periodo;

public class Periodo extends BasicModel {
	private static final long serialVersionUID = 1L;
	private String da;
	private String a;
	
	public Periodo(){}
	
	public String getDa() {
		return da;
	}
	public void setDa(String da) {
		this.da = da;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	
	@Override
	public boolean equals(Object obj) {
		Periodo periodo = null;
		if(obj instanceof Periodo) {
			periodo = (Periodo) obj;
		} else {
			return false;
		}
		
		return da.equals(periodo.getDa()) && a.equals(periodo.getA()); 
	}
	
	@Override
	public String toString() {
		return da + "-" + a;
	}
}
