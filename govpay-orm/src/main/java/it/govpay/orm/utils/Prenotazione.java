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
package it.govpay.orm.utils;

import it.govpay.orm.BaseEntity;
import it.govpay.orm.profilazione.Intestatario;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "prenotazioni")
public class Prenotazione extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Intestatario intestatario;
	private String operatoreRich;
	private String codRich;
	private String tipoServizio;
	private String tipoEsportazione;
	private Timestamp tsBegin;
	private Timestamp tsEnd;
	private String stato;

	private Set<Esportazione> esportazione;
	/*** Auto Generated Identity Property ***/
	private Long id;

	@OneToMany(targetEntity = Esportazione.class, mappedBy = "prenotazione", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	public Set<Esportazione> getEsportazioni() {
		return this.esportazione;
	}

	public void setEsportazioni(Set<Esportazione> esportazione) {
		this.esportazione = esportazione;
	}

	@ManyToOne
	@JoinColumn(name = "INTESTATARIO")
	public Intestatario getIntestatario() {
		return this.intestatario;
	}

	public void setIntestatario(Intestatario intestatario) {
		this.intestatario = intestatario;
	}

	@Column(name = "OPERATORE_RICH")
	public String getOperatoreRich() {
		return operatoreRich;
	}

	public void setOperatoreRich(String operatoreRich) {
		this.operatoreRich = operatoreRich;
	}

	@Column(name = "COD_RICH")
	public String getCodRich() {
		return codRich;
	}

	public void setCodRich(String codRich) {
		this.codRich = codRich;
	}

	@Column(name = "TIPOSERVIZIO")
	public String getTipoServizio() {
		return tipoServizio;
	}

	public void setTipoServizio(String tipoServizio) {
		this.tipoServizio = tipoServizio;
	}

	@Column(name = "TIPO_ESPORTAZIONE")
	public String getTipoEsportazione() {
		return tipoEsportazione;
	}

	public void setTipoEsportazione(String tipoEsportazione) {
		this.tipoEsportazione = tipoEsportazione;
	}

	@Column(name = "TIMESTAMP_BEGIN")
	public Timestamp getTsBegin() {
		return tsBegin;
	}

	public void setTsBegin(Timestamp tsBegin) {
		this.tsBegin = tsBegin;
	}

	@Column(name = "TIMESTAMP_END")
	public Timestamp getTsEnd() {
		return tsEnd;
	}

	public void setTsEnd(Timestamp tsEnd) {
		this.tsEnd = tsEnd;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}