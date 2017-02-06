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

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Elemento {
	
	public Elemento(long id, String titolo, String sottotitolo, URI urlDettaglio) {
		this.id = id;
		this.valori = new ArrayList<String>();
		this.sottotitolo = sottotitolo;
		this.titolo = titolo;
		this.uri = urlDettaglio;
		this.voci = new HashMap<String, Voce<String>>();
	}
	
	public Elemento(long id, List<String> valori, URI urlDettaglio) {
		this.id = id;
		this.valori = valori;
		this.uri = urlDettaglio;
	}
	
	private long id;
	private String titolo;
	private String sottotitolo;
	private URI uri;
	private String formatter;
	private Map<String, Voce<String>> voci; 
	
	private List<String> valori;
	
	public long getId() {
		return this.id;
	}
	public String getTitolo() {
		return this.titolo;
	}
	public String getSottotitolo() {
		return this.sottotitolo;
	}
	public URI getUri() {
		return this.uri;
	}
	public List<String> getValori() {
		return valori;
	}
	public void setValori(List<String> valori) {
		this.valori = valori;
	}
	public String getFormatter() {
		return formatter;
	}
	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}
	public Map<String, Voce<String>> getVoci() {
		return voci;
	}
	public void setVoci(Map<String, Voce<String>> voci) {
		this.voci = voci;
	}
}
