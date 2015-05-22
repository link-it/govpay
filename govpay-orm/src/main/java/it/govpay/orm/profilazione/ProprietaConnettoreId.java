/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.orm.profilazione;

import java.io.Serializable;

public class ProprietaConnettoreId implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idConnettore;
	private String nomeProperieta;

	public ProprietaConnettoreId() {
	}

	public ProprietaConnettoreId(Long idConnettore, String nomeProperieta) {
		this.idConnettore = idConnettore;
		this.nomeProperieta = nomeProperieta;
	}

	public Long getIdConnettore() {
		return idConnettore;
	}

	public void setIdConnettore(Long idConnettore) {
		this.idConnettore = idConnettore;
	}

	public String getNomeProperieta() {
		return nomeProperieta;
	}

	public void setNomeProperieta(String nomeProperieta) {
		this.nomeProperieta = nomeProperieta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idConnettore == null) ? 0 : idConnettore.hashCode());
		result = prime * result + ((nomeProperieta == null) ? 0 : nomeProperieta.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProprietaConnettoreId other = (ProprietaConnettoreId) obj;
		if (idConnettore == null) {
			if (other.idConnettore != null)
				return false;
		} else if (!idConnettore.equals(other.idConnettore))
			return false;
		if (nomeProperieta == null) {
			if (other.nomeProperieta != null)
				return false;
		} else if (!nomeProperieta.equals(other.nomeProperieta))
			return false;
		return true;
	}
}
