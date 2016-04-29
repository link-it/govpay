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
package it.govpay.web.rs.dars.model.input;

import it.govpay.web.rs.dars.model.RawParamValue;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class RefreshableParamField<T> extends ParamField<T> {
	
	protected Logger log ;

	public RefreshableParamField(String id, String label, URI refreshUri, List<RawParamValue> values, FieldType paramType) {
		super(id, label,paramType);
		this.refreshUri = refreshUri;
		this.refreshUriParamIds = new ArrayList<String>();
		for(RawParamValue field : values){
			this.refreshUriParamIds.add(field.getId());
		}
		
		//aggiornaParametro(values);
	}

	public void aggiornaParametro(List<RawParamValue> values, Object ... objects) {
		this.defaultValue = this.getDefaultValue(values,objects);
		this.required = this.isRequired(values,objects);
		this.hidden = this.isHidden(values,objects);
		this.editable = this.isEditable(values,objects);
	}
	
	public void init(List<RawParamValue> values, Object ... objects){
		this.defaultValue = this.getDefaultValue(values,objects);
		this.required = this.isRequired(values,objects);
		this.hidden = this.isHidden(values,objects);
		this.editable = this.isEditable(values,objects);
	}

	private URI refreshUri; // URI di refresh del campo
	private List<String> refreshUriParamIds; // Identificativi dei field i cui valori devono essere passati alla refresh Uri
	

	public URI getRefreshUri() {
		return this.refreshUri;
	}

	public List<String> getRefreshUriParamIds() {
		return this.refreshUriParamIds;
	}
	
	@JsonIgnore
	protected abstract T getDefaultValue(List<RawParamValue> values, Object ... objects); 
	
	@JsonIgnore
	protected abstract boolean isRequired(List<RawParamValue> values, Object ... objects); 
	
	@JsonIgnore
	protected abstract boolean isHidden(List<RawParamValue> values, Object ... objects); 
	
	@JsonIgnore
	protected abstract boolean isEditable(List<RawParamValue> values, Object ... objects);
	
	public void addDependencyField(ParamField<?> field) {
		field.refreshParamIds.add(this.id);
	}
}
