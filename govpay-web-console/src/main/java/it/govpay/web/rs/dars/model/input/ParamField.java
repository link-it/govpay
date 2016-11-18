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

import java.util.ArrayList;
import java.util.List;

public abstract class ParamField<T> {

	protected String id; // Identificativo del field
	protected String label; // Etichetta da visualizzare
	protected String suggestion; // Tooltip da mostrare al passaggio sul field
	protected boolean required; // Indica se la valorizzazione e' obbligatoria
	protected boolean hidden;	 // Indica se il field deve essere o meno visualizzato
	protected boolean editable;	 // Indica se il field deve essere o meno modificabile
	protected T defaultValue; // Valorizzazione di default
	protected List<String> refreshParamIds; // Campi da aggiornare se il field viene modificato
	protected FieldType paramType; // Tipo del field
	protected boolean avanzata;

	protected ParamField(String id, String label, T defaultValue, boolean required, boolean hidden, boolean editable, FieldType paramType) {
		this.id = id;
		this.label = label;
		this.required = required;
		this.hidden = hidden;
		this.editable = editable;
		this.defaultValue = defaultValue;
		this.refreshParamIds = new ArrayList<String>();
		this.paramType = paramType;
	}
	
	protected ParamField(String id, String label, FieldType paramType) {
		this.id = id;
		this.label = label;
		this.paramType = paramType;
		this.refreshParamIds = new ArrayList<String>();
	}
	
	public String getId() {
		return this.id;
	}

	public String getLabel() {
		return this.label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isRequired() {
		return this.required;
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isHidden() {
		return this.hidden;
	}

	public String getSuggestion() {
		return this.suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
	
	public T getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
	}

	public List<String> getRefreshParamIds() {
		return this.refreshParamIds;
	}

	public String getParamType(){
		return this.paramType.getValue();
	}
	
	public boolean isEditable() {
		return this.editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isAvanzata() {
		return this.avanzata;
	}

	public void setAvanzata(boolean avanzata) {
		this.avanzata = avanzata;
	}
}
