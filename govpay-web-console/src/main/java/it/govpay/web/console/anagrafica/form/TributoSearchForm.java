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
package it.govpay.web.console.anagrafica.form;

import java.io.Serializable;

import org.openspcoop2.generic_project.web.core.Utils;
import org.openspcoop2.generic_project.web.form.BaseSearchForm;
import org.openspcoop2.generic_project.web.form.field.FormField;
import org.openspcoop2.generic_project.web.form.field.TextField;

public class TributoSearchForm extends BaseSearchForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FormField<String> idEnteCreditore;

	@Override
	protected void init() {
		
		this.idEnteCreditore = new TextField();
		this.idEnteCreditore.setRequired(true);
		this.idEnteCreditore.setLabel(Utils.getMessageFromResourceBundle("tributo.idEnteCreditore"));
		this.idEnteCreditore.setName("idEnteCreditore");
		this.idEnteCreditore.setValue(null);
		
		reset();
		
	}

	@Override
	public void reset() {
		resetParametriPaginazione();
		
		this.idEnteCreditore.reset();
	}

	public FormField<String> getIdEnteCreditore() {
		return idEnteCreditore;
	}

	public void setIdEnteCreditore(FormField<String> idEnteCreditore) {
		this.idEnteCreditore = idEnteCreditore;
	}

	
}
