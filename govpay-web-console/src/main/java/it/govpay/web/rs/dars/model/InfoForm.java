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
package it.govpay.web.rs.dars.model;

import it.govpay.web.rs.dars.model.input.ParamField;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class InfoForm {
	
	private URI uri;
	private List<Sezione> sezioni;
	private List<String> titolo;
	
	public InfoForm(URI uri) {
		this(uri, null);
	}
	
	public InfoForm(URI uri,String titolo) {
		this.uri = uri;
		this.titolo = new ArrayList<String>();
		this.titolo.add(titolo);
		this.sezioni = new ArrayList<Sezione>();
		this.sezioni.add(new Sezione(null));
	}
	
	public List<Sezione> getSezioni() {
		return this.sezioni;
	}

	@JsonIgnore
	public Sezione getSezioneRoot() {
		return this.sezioni.get(0);
	}
	
	public Sezione addSezione(String etichetta) {
		Sezione sezione = new Sezione(etichetta);
		this.sezioni.add(sezione);
		return sezione;
	}
	
	public URI getUri() {
		return this.uri;
	}

	public class Sezione {
		private String etichetta;
		private List<ParamField<?>> fields;
		
		public Sezione(String etichetta) {
			this.etichetta = etichetta;
			this.fields = new ArrayList<ParamField<?>>();
		}
		
		public String getEtichetta() {
			return this.etichetta;
		}
		
		public List<ParamField<?>> getFields() {
			return this.fields;
		}
		
		public void addField(ParamField<?> field) {
			this.fields.add(field);
		}
	}

	public List<String> getTitolo() {
		return titolo;
	}

	public void setTitolo(List<String> titolo) {
		this.titolo = titolo;
	}
	
}
