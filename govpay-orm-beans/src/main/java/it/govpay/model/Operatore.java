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

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Acl;
import it.govpay.bd.model.BasicModel;

public class Operatore extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String principal;
	private String nome;
	private ProfiloOperatore profilo;
	private boolean abilitato;
	private List<Acl> acls;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	
	public List<Acl> getAcls() {
		return acls;
	}
	public void setAcls(List<Acl> acls) {
		this.acls = acls;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

}
