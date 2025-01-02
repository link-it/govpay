/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.bd.reportistica.statistiche.filters;

import java.util.Date;

import org.openspcoop2.generic_project.dao.IExpressionConstructor;

import it.govpay.bd.AbstractFilter;
import it.govpay.model.reportistica.statistiche.TipoIntervallo;

public abstract class StatisticaFilter extends AbstractFilter {

	public StatisticaFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	private Date data;
	private TipoIntervallo tipoIntervallo;
	private Double soglia;
	
	public Date getData() {
		return this.data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public TipoIntervallo getTipoIntervallo() {
		return this.tipoIntervallo;
	}
	public void setTipoIntervallo(TipoIntervallo tipoIntervallo) {
		this.tipoIntervallo = tipoIntervallo;
	}
	public Double getSoglia() {
		return this.soglia;
	}
	public void setSoglia(Double soglia) {
		this.soglia = soglia;
	}
}
