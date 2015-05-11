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
package it.govpay.orm.configurazione;

import it.govpay.orm.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "cfg_strumento_pagamento")
@NamedQueries({
//		@NamedQuery(name = "getCfgStrumentoPagamentoAll", query = "select cf from CfgStrumentoPagamento cf "
//				+ "order by cf.descrizione desc  "),
//		@NamedQuery(name = "getCfgStrumentoPagamentoById", query = "select cf from CfgStrumentoPagamento cf "
//				+ "where cf.id =:id") 
})
public class CfgStrumentoPagamento extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	private String bundleKey;
	private String descrizione;
	private String stRiga;
	private Boolean enabled;

	/*** Auto Generated Identity Property ***/
	private Long id;

	public CfgStrumentoPagamento() {
	}

	@Column(name = "BUNDLE_KEY", nullable = false)
	public String getBundleKey() {
		return this.bundleKey;
	}

	public void setBundleKey(String bundleKey) {
		this.bundleKey = bundleKey;
	}

	@Column(nullable = false)
	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Column(name = "ST_RIGA", nullable = false)
	public String getStRiga() {
		return this.stRiga;
	}

	public void setStRiga(String stRiga) {
		this.stRiga = stRiga;
	}

	@Transient
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Transient
	public Boolean isDocumentoPagamento() {
		return "DDP".equals(bundleKey);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CfgStrumentoPagamento [getBundleKey()=");
		builder.append(getBundleKey());
		builder.append(", getDescrizione()=");
		builder.append(getDescrizione());
		builder.append(", getStRiga()=");
		builder.append(getStRiga());
		builder.append(", getEnabled()=");
		builder.append(getEnabled());
		builder.append(", isDocumentoPagamento()=");
		builder.append(isDocumentoPagamento());
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