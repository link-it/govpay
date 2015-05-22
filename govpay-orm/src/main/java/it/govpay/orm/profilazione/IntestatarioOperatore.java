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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "intestatari_operatori")
public class IntestatarioOperatore implements Serializable {

	private static final long serialVersionUID = 1L;

	/*** PK Reference ***/
	private IntestatarioOperatoreId opeId;

	/*** Persistent properties ***/
	private String tipoOperatore;
	private Integer locked;

	/*** Persistent References ***/
	private Operatore operatore;
	private Intestatario intestatario;

	public IntestatarioOperatore() {
		this.opeId = new IntestatarioOperatoreId();
	}

	public IntestatarioOperatore(IntestatarioOperatoreId opeId) {
		this.opeId = opeId;
	}

	@Id
	public IntestatarioOperatoreId getOpeId() {
		return opeId;
	}

	public void setOpeId(IntestatarioOperatoreId opeId) {
		this.opeId = opeId;
	}

	@Column(name = "TP_OPERATORE")
	public String getTipoOperatore() {
		return this.tipoOperatore;
	}

	public void setTipoOperatore(String tipoOperatore) {
		this.tipoOperatore = tipoOperatore;
	}

	@Column(name = "BLOCCATO")
	public Integer getLocked() {
		return this.locked;
	}

	public void setLocked(Integer lock) {
		this.locked = lock;
	}

	/*** Unidirectional ManyToOne Association to Intestatario ***/
	@ManyToOne(targetEntity = Intestatario.class)
	@JoinColumn(name = "INTESTATARIO", insertable = false, updatable = false)
	public Intestatario getIntestatario() {
		return this.intestatario;
	}

	public void setIntestatario(Intestatario intestatario) {
		this.intestatario = (Intestatario) intestatario;
	}

	/*** Bidirectional many-to-one association to Operatore - inverse side ***/
	@ManyToOne(targetEntity = Operatore.class)
	@JoinColumn(name = "OPERATORE", insertable = false, updatable = false)
	public Operatore getOperatore() {
		return operatore;
	}

	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}

}
