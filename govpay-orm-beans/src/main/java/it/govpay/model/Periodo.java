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
package it.govpay.model;

public class Periodo {
	private String da;
	private String a;
	
	public Periodo(){}
	
	public String getDa() {
		return this.da;
	}
	public void setDa(String da) {
		this.da = da;
	}
	public String getA() {
		return this.a;
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
		
		return this.da.equals(periodo.getDa()) && this.a.equals(periodo.getA()); 
	}
	
	@Override
	public String toString() {
		return this.da + "-" + this.a;
	}
}
