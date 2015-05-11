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

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SistemaEnteId implements Serializable {

	private static final long serialVersionUID = 1L;
	private String idEnte;
	private String idSystem;

	public SistemaEnteId() {
	}

	public SistemaEnteId(String idEnte, String idSystem) {
		this.idEnte = idEnte;
		this.idSystem = idSystem;
	}

	@Column(name="ID_ENTE")
	public String getIdEnte() {
		return this.idEnte;
	}
	public void setIdEnte(String idEnte) {
		this.idEnte = idEnte;
	}

	@Column(name="ID_SYSTEM")
	public String getIdSystem() {
		return this.idSystem;
	}
	public void setIdSystem(String idSystem) {
		this.idSystem = idSystem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idEnte == null) ? 0 : idEnte.hashCode());
		result = prime * result
				+ ((idSystem == null) ? 0 : idSystem.hashCode());
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
		SistemaEnteId other = (SistemaEnteId) obj;
		if (idEnte == null) {
			if (other.idEnte != null)
				return false;
		} else if (!idEnte.equals(other.idEnte))
			return false;
		if (idSystem == null) {
			if (other.idSystem != null)
				return false;
		} else if (!idSystem.equals(other.idSystem))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SistemaEnteId [idEnte=" + idEnte + ", idSystem=" + idSystem
				+ "]";
	}


}
