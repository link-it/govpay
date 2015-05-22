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
package it.govpay.orm.posizionedebitoria;

import it.govpay.orm.BaseEntity;
import it.govpay.orm.pagamenti.DocumentoDiPagamento;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "condizioni_documento")
public class CondizioneDocumento extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/*** Persistent Properties ***/
	private String opAnnullamento;
	private Timestamp tsAnnullamento;

	/*** Persistent References ***/
	private Condizione condizione;
	private DocumentoDiPagamento documento;

	/*** Auto Generated Identity Property ***/
	private Long id;

	@Column(name = "OP_ANNULLAMENTO", length = 160)
	public String getOpAnnullamento() {
		return this.opAnnullamento;
	}

	public void setOpAnnullamento(String opAnnullamento) {
		this.opAnnullamento = opAnnullamento;
	}

	@Column(name = "TS_ANNULLAMENTO")
	public Timestamp getTsAnnullamento() {
		return this.tsAnnullamento;
	}

	public void setTsAnnullamento(Timestamp tsAnnullamento) {
		this.tsAnnullamento = tsAnnullamento;
	}

	@ManyToOne
	@JoinColumn(name = "ID_CONDIZIONE")
	public Condizione getCondizionePagamento() {
		return this.condizione;
	}

	public void setCondizionePagamento(Condizione condizione) {
		this.condizione = condizione;
	}

	// bi-directional many-to-one association to DocumentoDiPagamento
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "ID_DOCUMENTO")
	public DocumentoDiPagamento getDocumento() {
		return this.documento;
	}

	public void setDocumento(DocumentoDiPagamento documento) {
		this.documento = documento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime
				* result
				+ ((getCondizionePagamento().getIdCondizione() == null || getDocumento().getId() == null) ? 0
						: getCondizionePagamento().getIdCondizione().hashCode() * getDocumento().getId().hashCode());

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
		CondizioneDocumento other = (CondizioneDocumento) obj;
		if (getCondizionePagamento().getIdCondizione() == null) {
			if (other.getCondizionePagamento().getIdCondizione() != null)
				return false;
		} else if (!getCondizionePagamento().getIdCondizione().equals(other.getCondizionePagamento().getIdCondizione()))
			return false;
		if (getDocumento().getId() == null) {
			if (other.getDocumento().getId() != null)
				return false;
		} else if (!getDocumento().getId().equals(other.getDocumento().getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CondizioneDocumento [opAnnullamento=");
		builder.append(opAnnullamento);
		builder.append(", tsAnnullamento=");
		builder.append(tsAnnullamento);
		builder.append(", condizione=");
		builder.append(condizione);
		builder.append(", documento=");
		builder.append(documento);
		builder.append(", getOpAnnullamento()=");
		builder.append(getOpAnnullamento());
		builder.append(", getTsAnnullamento()=");
		builder.append(getTsAnnullamento());
		builder.append(", getCondizionePagamento()=");
		builder.append(getCondizionePagamento());
		builder.append(", getDocumento()=");
		builder.append(getDocumento());
		builder.append(", getId()=");
		builder.append(getId());
		builder.append(", getVersion()=");
		builder.append(getVersion());
		builder.append(", getOpInserimento()=");
		builder.append(getOpInserimento());
		builder.append(", getOpAggiornamento()=");
		builder.append(getOpAggiornamento());
		builder.append(", getTsInserimento()=");
		builder.append(getTsInserimento());
		builder.append(", getTsAggiornamento()=");
		builder.append(getTsAggiornamento());
		builder.append("]");
		return builder.toString();
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