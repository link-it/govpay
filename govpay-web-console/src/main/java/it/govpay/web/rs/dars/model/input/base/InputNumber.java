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
package it.govpay.web.rs.dars.model.input.base;

import it.govpay.web.rs.dars.model.input.FieldType;
import it.govpay.web.rs.dars.model.input.Input;

public class InputNumber extends Input<Number> {

	public InputNumber(String id, String label, Number defaultValue, boolean required, boolean hidden, boolean editable, int minLength, int maxLength) {
		super(id, label, defaultValue, required, hidden, editable, minLength, maxLength,FieldType.INPUT_NUMBER);
	}

}
