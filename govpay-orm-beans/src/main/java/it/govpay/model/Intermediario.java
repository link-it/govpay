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

public class Intermediario extends BasicModel{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String codIntermediario;
	private String denominazione;
    private Connettore connettorePdd;
    private ConnettoreSftp connettoreSftp;
    private boolean abilitato;
    
    private String principal;
    private String principalOriginale;
    
    public Intermediario() {}
        
	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodIntermediario() {
		return this.codIntermediario;
	}
	public void setCodIntermediario(String codIntermediario) {
		this.codIntermediario = codIntermediario;
	}
	public Connettore getConnettorePdd() {
		return this.connettorePdd;
	}
	public void setConnettorePdd(Connettore connettorePdd) {
		this.connettorePdd = connettorePdd;
	}
	public boolean isAbilitato() {
		return this.abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	public String getDenominazione() {
		return this.denominazione;
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
				equals(this.codIntermediario, intermediario.getCodIntermediario()) &&
				equals(this.denominazione, intermediario.getDenominazione()) &&
				equals(this.connettorePdd, intermediario.getConnettorePdd()) &&
				equals(this.connettoreSftp, intermediario.getConnettoreSftp()) &&
				equals(this.principal, intermediario.getPrincipal()) &&
				equals(this.principalOriginale, intermediario.getPrincipalOriginale()) &&
				this.abilitato == intermediario.isAbilitato();
		
		return equal;
	}

	public ConnettoreSftp getConnettoreSftp() {
		return this.connettoreSftp;
	}

	public void setConnettoreSftp(ConnettoreSftp connettoreSftp) {
		this.connettoreSftp = connettoreSftp;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getPrincipalOriginale() {
		return principalOriginale;
	}

	public void setPrincipalOriginale(String principalOriginale) {
		this.principalOriginale = principalOriginale;
	}

}
