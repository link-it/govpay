/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.utils;

import java.util.Map;

public class RtPlaceholder extends RptPlaceholder {

	private String importoPagato;
	private String esito;
	public String getImportoPagato() {
		return importoPagato;
	}
	public void setImportoPagato(String importoPagato) {
		this.importoPagato = importoPagato;
	}
	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	
	@Override
	public Map<String, String> getValuesMap() {
		Map<String,String> map = super.getValuesMap();
		map.put("importoPagato", this.importoPagato);
		map.put("esito", this.esito);
		return map;
	}

}
