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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Acl extends BasicModel {
	
	public enum Diritti {
		LETTURA ("R"), SCRITTURA ("W") , ESECUZIONE ("X");
		
		
		private String codifica;

		Diritti(String codifica) {
			this.codifica = codifica;
		}
		
		public String getCodifica() {
			return codifica;
		}
		
		public static Diritti toEnum(String codifica) throws ServiceException {
			for(Diritti p : Diritti.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per Diritto. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(Diritti.values()));
		}
	}
	
	private String ruolo;
	private String principal;
	private int diritti;
	private Servizio servizio;
	private List<Diritti> listaDiritti= null;

	private static final long serialVersionUID = 1L;
	public enum Servizio {
		PAGAMENTI_ATTESA("A"),
		PAGAMENTI_ONLINE("O"),
		VERSAMENTI("V"),
		NOTIFICHE("N"),
		RENDICONTAZIONE("R"),
		INCASSI("I"),
		CRUSCOTTO("C"),
		Anagrafica_PagoPa("A_PPA"),
		Anagrafica_Contabile("A_CON"),
		Anagrafica_Applicazioni("A_APP"),
		Anagrafica_Utenti("A_USR"),
		Gestione_Pagamenti("G_PAG"),
		Gestione_Rendicontazioni("G_RND"),
		Giornale_Eventi("GDE"),
		Manutenzione("MAN"),
		Statistiche("STAT");
		
		private String codifica;

		Servizio(String codifica) {
			this.codifica = codifica;
		}
		
		public String getCodifica() {
			return codifica;
		}
		
		public static Servizio toEnum(String codifica) throws ServiceException {
			for(Servizio p : Servizio.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			throw new ServiceException("Codifica inesistente per Servizio. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(Servizio.values()));
		}
	}
	public String getRuolo() {
		return ruolo;
	}
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public int getDiritti() {
		return diritti;
	}
	public void setDiritti(int diritti) {
		this.diritti = diritti;
	}
	public Servizio getServizio() {
		return servizio;
	}
	public void setServizio(Servizio servizio) {
		this.servizio = servizio;
	}
	public List<Diritti> getListaDiritti() {
		return listaDiritti;
	}
	public String getListaDirittiString() {
		StringBuffer sb = new StringBuffer();
		
		if(this.listaDiritti != null && this.listaDiritti.size() >0 ) {
			for (Diritti diritti : this.listaDiritti) {
				sb.append(diritti.getCodifica());
			}
		}
		
		return sb.toString();
	}
	public void setListaDiritti(List<Diritti> listaDiritti) {
		this.listaDiritti = listaDiritti;
	}
	public void setListaDiritti(String diritti) {
		this.listaDiritti = new ArrayList<Acl.Diritti>();
		
		if(StringUtils.isNotEmpty(diritti)) {
			for(Diritti p : Diritti.values()){
				if(diritti.toUpperCase().contains(p.getCodifica()))
					this.listaDiritti.add(p);
			}
		}
	}
	
}
