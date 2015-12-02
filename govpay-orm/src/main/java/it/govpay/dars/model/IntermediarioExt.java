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

import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.Stazione;

import java.util.List;

public class IntermediarioExt extends Intermediario {
	
	private static final long serialVersionUID = 1L;
	
	public IntermediarioExt(){ super();}
	
	public IntermediarioExt(Intermediario intermediario, List<Stazione> stazioni) {
		this.setCodIntermediario(intermediario.getCodIntermediario());
		this.setConnettorePdd(intermediario.getConnettorePdd());
		this.setId(intermediario.getId());
		this.setAbilitato(intermediario.isAbilitato()); 
		this.setStazioni(stazioni);
		this.setDenominazione(intermediario.getDenominazione()); 
	}
	
	private List<Stazione> stazioni;

	public List<Stazione> getStazioni() {
		return stazioni;
	}

	public void setStazioni(List<Stazione> stazioni) {
		this.stazioni = stazioni;
	}
}
