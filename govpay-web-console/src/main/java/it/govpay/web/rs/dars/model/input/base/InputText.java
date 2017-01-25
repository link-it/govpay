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

import it.govpay.web.rs.dars.model.input.FieldType;
import it.govpay.web.rs.dars.model.input.Input;

public class InputText extends Input<String> {

	public InputText(String id, String label, String defaultValue, boolean required, boolean hidden, boolean editable, int minLength, int maxLength, FieldType paramType) {
		super(id, label, defaultValue, required, hidden, editable, minLength, maxLength, paramType);
	}
	
	public InputText(String id, String label, String defaultValue, boolean required, boolean hidden, boolean editable, int minLength, int maxLength) {
		super(id, label, defaultValue, required, hidden, editable, minLength, maxLength, FieldType.INPUT_TEXT);
	}

}


