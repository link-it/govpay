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
package it.govpay.bd.model;

import java.util.List;

public class Applicazione extends BasicModel {
	public enum Versione {GPv1, GPv2}
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String codApplicazione;
	private long idStazione;
	private String principal;
	private Versione versione;
	private String policyRispedizione;
	private boolean abilitato;
	private List<Long> idTributi;
    private Connettore connettoreEsito;
    private Connettore connettoreVerifica;
   
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodApplicazione() {
		return codApplicazione;
	}
	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}
	public Connettore getConnettoreEsito() {
		return connettoreEsito;
	}
	public void setConnettoreEsito(Connettore connettoreEsito) {
		this.connettoreEsito = connettoreEsito;
	}
	public Connettore getConnettoreVerifica() {
		return connettoreVerifica;
	}
	public void setConnettoreVerifica(Connettore connettoreVerifica) {
		this.connettoreVerifica = connettoreVerifica;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public boolean isAbilitato() {
		return abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	public List<Long> getIdTributi() {
		return idTributi;
	}
	public void setIdTributi(List<Long> idTributi) {
		this.idTributi = idTributi;
	}
	
	@Override
	public boolean equals(Object obj) {
		Applicazione applicazione = null;
		if(obj instanceof Applicazione) {
			applicazione = (Applicazione) obj;
		} else {
			return false;
		}
		
		boolean equal =
			equals(principal, applicazione.getPrincipal()) &&
			equals(codApplicazione, applicazione.getCodApplicazione()) &&
			equals(connettoreEsito, applicazione.getConnettoreEsito()) &&
			equals(connettoreVerifica, applicazione.getConnettoreVerifica()) &&
			equals(idTributi, applicazione.getIdTributi()) &&
			equals(versione, applicazione.getVersione()) &&
			equals(policyRispedizione, applicazione.getPolicyRispedizione()) &&
			idStazione == applicazione.getIdStazione() &&
			abilitato==applicazione.isAbilitato();
		
		return equal;
	}
	public long getIdStazione() {
		return idStazione;
	}
	public void setIdStazione(long idStazione) {
		this.idStazione = idStazione;
	}
	public String getPolicyRispedizione() {
		return policyRispedizione;
	}
	public void setPolicyRispedizione(String policyRispedizione) {
		this.policyRispedizione = policyRispedizione;
	}
	public Versione getVersione() {
		return versione;
	}
	public void setVersione(Versione versione) {
		this.versione = versione;
	}
}
