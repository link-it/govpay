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

@Entity
@Table(name = "cfg_fornitore_gateway")
@NamedQueries({
//	@NamedQuery(name = "getByBundleKey", query = "select cf from CfgFornitoreGateway cf where cf.bundleKey =:inBundleKey"),
//	@NamedQuery(name = "getCfgFornitoreGatewayAll", query = "select cf from CfgFornitoreGateway cf ") 
})
public class CfgFornitoreGateway extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	private String bundleKey;
	private String descrizione;
	private String stRiga;
	/*** Auto Generated Identity Property ***/
	private Long id;

	// private Set<CfgGatewayPagamento> cfgGatewayPagamenti;

	public CfgFornitoreGateway() {
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CfgFornitoreGateway [getBundleKey()=");
		builder.append(getBundleKey());
		builder.append(", getDescrizione()=");
		builder.append(getDescrizione());
		builder.append(", getStRiga()=");
		builder.append(getStRiga());
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

	// bi-directional many-to-one association to CfgGatewayPagamento
	// @OneToMany(mappedBy="cfgFornitoreGateway")
	// public Set<CfgGatewayPagamento> getCfgGatewayPagamenti() {
	// return this.cfgGatewayPagamenti;
	// }
	//
	// public void setCfgGatewayPagamenti(Set<CfgGatewayPagamento>
	// cfgGatewayPagamenti) {
	// this.cfgGatewayPagamenti = cfgGatewayPagamenti;
	// }

}