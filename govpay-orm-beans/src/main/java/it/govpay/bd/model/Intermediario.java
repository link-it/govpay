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
package it.govpay.bd.model;

public class Intermediario extends BasicModel{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String codIntermediario;
	private String denominazione;
    private Connettore connettorePdd;
    private boolean abilitato;
    
    public Intermediario() {}
        
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodIntermediario() {
		return codIntermediario;
	}
	public void setCodIntermediario(String codIntermediario) {
		this.codIntermediario = codIntermediario;
	}
	public Connettore getConnettorePdd() {
		return connettorePdd;
	}
	public void setConnettorePdd(Connettore connettorePdd) {
		this.connettorePdd = connettorePdd;
	}
	public boolean isAbilitato() {
		return abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String nomeSPC) {
		this.denominazione = nomeSPC;
	}

	@Override
	public boolean equals(Object obj) {
		Intermediario intermediario = null;
		if(obj instanceof Intermediario) {
			intermediario = (Intermediario) obj;
		} else {
			return false;
		}
		
		boolean equal = 
				equals(codIntermediario, intermediario.getCodIntermediario()) &&
				equals(denominazione, intermediario.getDenominazione()) &&
				equals(connettorePdd, intermediario.getConnettorePdd()) &&
				abilitato == intermediario.isAbilitato();
		
		return equal;
	}
    
}
