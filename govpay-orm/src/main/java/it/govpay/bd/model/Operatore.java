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

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Operatore extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private List<Long> idEnti;
	private List<Long> idApplicazioni;
	private String principal;
	private ProfiloOperatore profilo;
	private Anagrafica anagrafica;
	private boolean abilitato;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Long> getIdEnti() {
		return idEnti;
	}
	public void setIdEnti(List<Long> idEnti) {
		this.idEnti = idEnti;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public ProfiloOperatore getProfilo() {
		return profilo;
	}
	public void setProfilo(ProfiloOperatore profilo) {
		this.profilo = profilo;
	}
	public Anagrafica getAnagrafica() {
		return anagrafica;
	}
	public void setAnagrafica(Anagrafica anagrafica) {
		this.anagrafica = anagrafica;
	}
	public boolean isAbilitato() {
		return abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	
	public enum ProfiloOperatore {
		ADMIN("A"), ENTE("E");

		private String codifica;

		ProfiloOperatore(String codifica) {
			this.codifica = codifica;
		}
		
		public String getCodifica() {
			return codifica;
		}
		
		public static ProfiloOperatore toEnum(String codifica) throws ServiceException {
			for(ProfiloOperatore p : ProfiloOperatore.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per ProfiloOperatore. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(ProfiloOperatore.values()));
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		Operatore operatore = null;
		if(obj instanceof Operatore) {
			operatore = (Operatore) obj;
		} else {
			return false;
		}
		
		boolean equal =
				equals(idEnti, operatore.getIdEnti()) &&
				equals(getIdApplicazioni(), operatore.getIdApplicazioni()) &&
				equals(principal, operatore.getPrincipal()) &&
				equals(profilo, operatore.getProfilo()) &&
				equals(anagrafica, operatore.getAnagrafica()) &&
				abilitato == operatore.isAbilitato();
		
		return equal;
	}
	public List<Long> getIdApplicazioni() {
		return idApplicazioni;
	}
	public void setIdApplicazioni(List<Long> idApplicazioni) {
		this.idApplicazioni = idApplicazioni;
	}
}
