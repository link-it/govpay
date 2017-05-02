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
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Dettaglio {
	
	private String titolo;
	private List<Sezione> sezioni;
	private List<ElementoCorrelato> elementiCorrelati;
	private URI esportazione;
	private InfoForm infoCancellazione;
	private InfoForm infoModifica;
	
	public Dettaglio(String titolo, URI esportazione, InfoForm infoCancellazione, InfoForm modifica) {
		this.titolo = titolo;
		this.sezioni = new ArrayList<Sezione>();
		this.sezioni.add(new Sezione(null));
		this.elementiCorrelati = new ArrayList<ElementoCorrelato>();
		this.esportazione = esportazione;
		this.infoCancellazione = infoCancellazione;
		this.infoModifica = modifica;
	}
	
	public List<Sezione> getSezioni() {
		return this.sezioni;
	}

	public List<ElementoCorrelato> getElementiCorrelati() {
		return this.elementiCorrelati;
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
	
	public ElementoCorrelato addElementoCorrelato(String etichetta, URI uri) {
		ElementoCorrelato elemento = new ElementoCorrelato(etichetta, uri);
		this.elementiCorrelati.add(elemento);
		return elemento;
	}

	public String getTitolo() {
		return this.titolo;
	}
	
	public URI getEsportazione() {
		return this.esportazione;
	}

	public InfoForm getInfoCancellazione() {
		return this.infoCancellazione;
	}

	public InfoForm getInfoModifica() {
		return this.infoModifica;
	}

}
