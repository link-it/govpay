/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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
package it.govpay.model;

import it.govpay.model.Tributo.TipoContabilita;

public class TipoTributo extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	private Long id; 
	private String codTributo;
	private String descrizione;
	private TipoContabilita tipoContabilitaDefault;
	private String codContabilitaDefault;
	
	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodTributo() {
		return this.codTributo;
	}
	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}
	public String getDescrizione() {
		return this.descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public TipoContabilita getTipoContabilitaDefault() {
		return this.tipoContabilitaDefault;
	}
	public void setTipoContabilitaDefault(TipoContabilita tipoContabilitaDefault) {
		this.tipoContabilitaDefault = tipoContabilitaDefault;
	}
	public String getCodContabilitaDefault() {
		return this.codContabilitaDefault;
	}
	public void setCodContabilitaDefault(String codContabilitaDefault) {
		this.codContabilitaDefault = codContabilitaDefault;
	}
}
