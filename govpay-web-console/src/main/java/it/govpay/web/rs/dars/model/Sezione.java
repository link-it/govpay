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


public class Sezione {
	private String etichetta;
	private List<Voce<String>> voci;
	
	public Sezione(String etichetta) {
		this.etichetta = etichetta;
		this.voci = new ArrayList<Voce<String>>();
	}
	
	public String getEtichetta() {
		return this.etichetta;
	}
	
	public void setEtichetta(String etichetta) {
		this.etichetta = etichetta;
	}

	public List<Voce<String>> getVoci() {
		return this.voci;
	}
	
	public void addVoce(String etichetta, String valore) {
		this.addVoce(etichetta, valore, false);
	}
	
	public void addVoce(String etichetta, String valore, boolean avanzata) {
		this.voci.add(new Voce<String>(etichetta, valore,avanzata));
	}
	
	public void addVoce(String etichetta, String valore, URI reference) {
		this.addVoce(etichetta, valore, reference, false);
	}
	
	public void addVoce(String etichetta, String valore, URI reference, boolean avanzata) {
		this.voci.add(new VoceRiferimento<String>(etichetta, valore, avanzata, reference));
	}
	
	public void addDownloadLink(String etichetta, String valore, URI reference) {
		this.addDownloadLink(etichetta, valore, reference, false);
	}
	
	public void addDownloadLink(String etichetta, String valore, URI reference, boolean avanzata) {
		this.voci.add(new VoceDownload<String>(etichetta, valore, avanzata, reference));
	}
	
	public void addVoce(Voce<String> voce ) {
		this.voci.add(voce);
	}
	
}

