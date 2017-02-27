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

import java.util.Date;

import it.govpay.web.rs.dars.model.input.FieldType;
import it.govpay.web.rs.dars.model.input.ParamField;

public class InputDate extends ParamField<Date> {

	private Date minDate;
	private Date maxDate;
	
	public InputDate(String id, String label, Date defaultValue, boolean required, boolean hidden, boolean editable, Date minDate, Date maxDate) {
		super(id, label, defaultValue, required, hidden, editable, FieldType.INPUT_DATE);
		this.minDate = minDate;
		this.maxDate = maxDate;
	}

	public Date getMinDate() {
		return this.minDate;
	}

	public Date getMaxDate() {
		return this.maxDate;
	}

}


