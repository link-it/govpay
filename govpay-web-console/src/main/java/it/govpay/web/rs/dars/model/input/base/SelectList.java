/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.web.rs.dars.model.input.base;

import java.util.List;

import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.FieldType;
import it.govpay.web.rs.dars.model.input.ParamField;

public class SelectList<T> extends ParamField<T> {
	
	public SelectList(String id, String label, T defaultValue, boolean required, boolean hidden, boolean editable, List<Voce<T>> values) {
		super(id, label, defaultValue, required, hidden, editable, FieldType.SELECT_LIST);
		this.values = values;
	}
	
	public SelectList(String id, String label, T defaultValue, boolean required, boolean hidden, boolean editable, List<Voce<T>> values, FieldType paramType) {
		super(id, label, defaultValue, required, hidden, editable,paramType);
		this.values = values;
	}

	private List<Voce<T>> values; 
	
	public List<Voce<T>> getValues() {
		return this.values;
	}

	public void setValues(List<Voce<T>> values) {
		this.values = values;
	}
	
	
}

