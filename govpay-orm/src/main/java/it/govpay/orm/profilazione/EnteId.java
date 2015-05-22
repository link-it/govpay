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

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EnteId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String idEntePk;
	private String cdTrbEntePk;

	public EnteId() {
	}

	public EnteId(String idEnte, String cdTrbEnte) {
		this.idEntePk = idEnte;
		this.cdTrbEntePk = cdTrbEnte;
	}

	@Column(name="ID_ENTE")
	public String getIdEntePk() {
		return this.idEntePk;
	}

	public void setIdEntePk(String idEnte) {
		this.idEntePk = idEnte;
	}

	@Column(name="CD_TRB_ENTE")
	public String getCdTrbEntePk() {
		return this.cdTrbEntePk;
	}

	public void setCdTrbEntePk(String cdTrbEnte) {
		this.cdTrbEntePk = cdTrbEnte;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EnteId))
			return false;
		EnteId castOther = (EnteId) other;

		return ((this.getIdEntePk() == castOther.getIdEntePk()) || (this.getIdEntePk() != null
				&& castOther.getIdEntePk() != null && this.getIdEntePk().equals(castOther.getIdEntePk())))
				&& ((this.getCdTrbEntePk() == castOther.getCdTrbEntePk()) || (this.getCdTrbEntePk() != null
						&& castOther.getCdTrbEntePk() != null && this.getCdTrbEntePk().equals(castOther.getCdTrbEntePk())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getIdEntePk() == null ? 0 : this.getIdEntePk().hashCode());
		result = 37 * result + (getCdTrbEntePk() == null ? 0 : this.getCdTrbEntePk().hashCode());
		return result;
	}

}
