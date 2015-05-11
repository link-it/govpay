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
public class IntestatarioOperatoreId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String intestatario;
	private String operatore;

	/** default constructor */
	public IntestatarioOperatoreId() {
	}

	/** full constructor */
	public IntestatarioOperatoreId(String intestatario, String operatore) {
		this.intestatario = intestatario;
		this.operatore = operatore;
	}

	// Property accessors
	@Column(name = "INTESTATARIO", nullable = false)
	public String getIntestatario() {
		return this.intestatario;
	}

	public void setIntestatario(String intestatario) {
		this.intestatario = intestatario;
	}

	@Column(name = "OPERATORE", nullable = false)
	public String getOperatore() {
		return this.operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof IntestatarioOperatoreId))
			return false;
		IntestatarioOperatoreId castOther = (IntestatarioOperatoreId) other;

		return ((this.getIntestatario() == castOther.getIntestatario()) || (this.getIntestatario() != null
				&& castOther.getIntestatario() != null && this.getIntestatario().equals(castOther.getIntestatario())))
				&& ((this.getOperatore() == castOther.getOperatore()) || (this.getOperatore() != null
						&& castOther.getOperatore() != null && this.getOperatore().equals(castOther.getOperatore())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getIntestatario() == null ? 0 : this.getIntestatario().hashCode());
		result = 37 * result + (getOperatore() == null ? 0 : this.getOperatore().hashCode());
		return result;
	}

}
