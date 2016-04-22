/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.web.rs.dars.model.input.dinamic;

import java.net.URI;
import java.util.List;

import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.input.FieldType;
import it.govpay.web.rs.dars.model.input.RefreshableInput;

public abstract class InputTextArea extends RefreshableInput<String> {
	public InputTextArea(String id, String label, int minLength, int maxLength, int rows, int columns, URI refreshUri, List<RawParamValue> values) {
		super(id, label, minLength, maxLength, refreshUri, values, FieldType.INPUT_TEXT_AREA);
		this.rows = rows;
		this.columns = columns;
	}

	private int rows; 
	private int columns; 
	
	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
}