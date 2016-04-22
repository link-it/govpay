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
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.FieldType;
import it.govpay.web.rs.dars.model.input.RefreshableParamField;

public abstract class MultiSelectList<K, T extends List<K>> extends RefreshableParamField<T> {
	
	public MultiSelectList(String id, String label, URI refreshUri, List<RawParamValue> paramValues, Object ... objects) {
		super(id, label, refreshUri, paramValues, FieldType.MULTI_SELECT_LIST);
		//init(paramValues, objects);
	}

	@Override
	public void init(List<RawParamValue> paramValues, Object... objects) {
		super.init(paramValues, objects); 
		try {
			this.values = getValues(paramValues, objects);
		} catch (ServiceException e) {
			this.values = new ArrayList<Voce<K>>();
			this.values.add(new Voce<K>("!! ERRORE !!", null));
		}
	}
	
	@Override
	public void aggiornaParametro(List<RawParamValue> values, Object... objects) {
		super.aggiornaParametro(values, objects);
		try {
			this.values = getValues(values, objects);
		} catch (ServiceException e) {
			this.values = new ArrayList<Voce<K>>();
			this.values.add(new Voce<K>("!! ERRORE !!", null));
		}
	}
	
	@JsonIgnore
	protected abstract List<Voce<K>> getValues(List<RawParamValue> paramValues, Object ... objects) throws ServiceException;
	
	private List<Voce<K>> values; 
	
	public List<Voce<K>> getValues() {
		return values;
	}
}

