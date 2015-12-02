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

import org.openspcoop2.generic_project.exception.ServiceException;

public class Tributo extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	public enum TipoContabilta {
	    CAPITOLO("0"),
	    SPECIALE("1"),
	    SIOPE("2"),
	    ALTRO("9");
	    
		private String codifica;

		TipoContabilta(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return codifica;
		}
		
		public static TipoContabilta toEnum(String codifica) throws ServiceException {
			String codifiche = "{";
			for(TipoContabilta p : TipoContabilta.values()){
				codifiche += " " +p.getCodifica() + ",";
				if(p.getCodifica().equals(codifica))
					return p;
			}
			codifiche += "}";
			throw new ServiceException("Codifica inesistente per TipoContabilta. Valore fornito [" + codifica + "] valori possibili " + codifiche);
		}
	}
	
	private Long id; 
	private long idEnte;
	private String codTributo;
	private String descrizione;
	private long ibanAccredito;
	private boolean abilitato;
	private TipoContabilta tipoContabilita;
	private String codContabilita;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getIdEnte() {
		return idEnte;
	}
	public void setIdEnte(long idEnte) {
		this.idEnte = idEnte;
	}
	public String getCodTributo() {
		return codTributo;
	}
	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public long getIbanAccredito() {
		return ibanAccredito;
	}
	public void setIbanAccredito(long ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}
	public boolean isAbilitato() {
		return abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	public TipoContabilta getTipoContabilita() {
		return tipoContabilita;
	}
	public void setTipoContabilita(TipoContabilta tipoContabilita) {
		this.tipoContabilita = tipoContabilita;
	}
	public String getCodContabilita() {
		return codContabilita;
	}
	public void setCodContabilita(String codContabilita) {
		this.codContabilita = codContabilita;
	}

	@Override
	public boolean equals(Object obj) {
		Tributo tributo = null;
		if(obj instanceof Tributo) {
			tributo = (Tributo) obj;
		} else {
			return false;
		}
		
		boolean equal =
				equals(codTributo, tributo.getCodTributo()) &&
				equals(descrizione, tributo.getDescrizione()) &&
				equals(ibanAccredito, tributo.getIbanAccredito()) &&
				equals(tipoContabilita, tributo.getTipoContabilita()) &&
				equals(codContabilita, tributo.getCodContabilita()) &&
				idEnte == tributo.getIdEnte() &&
				abilitato == tributo.isAbilitato();
		
		return equal;
	}

}
