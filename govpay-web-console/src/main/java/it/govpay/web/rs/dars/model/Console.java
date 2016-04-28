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
package it.govpay.web.rs.dars.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Console {
	
	public class VoceMenu {
		private String label;
		private URI uri;
		private boolean avanzato;
		
		public VoceMenu(String label, URI uri, boolean avanzato) {
			this.label = label;
			this.uri = uri;
			this.avanzato = avanzato;
		}
		public String getLabel() {
			return this.label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public URI getUri() {
			return this.uri;
		}
		public void setUri(URI uri) {
			this.uri = uri;
		}
		public boolean isAvanzato() {
			return this.avanzato;
		}
		public void setAvanzato(boolean avanzato) {
			this.avanzato = avanzato;
		}
	}

	public class SezioneMenu {
		private String titolo;
		private List<VoceMenu> vociMenu;
		
		public SezioneMenu(String titolo) {
			this.titolo = titolo;
			this.vociMenu = new ArrayList<VoceMenu>();
		}
		
		public String getTitolo() {
			return this.titolo;
		}
		public void setTitolo(String titolo) {
			this.titolo = titolo;
		}
		public List<VoceMenu> getVociMenu() {
			return this.vociMenu;
		}
		public void setVociMenu(List<VoceMenu> vociMenu) {
			this.vociMenu = vociMenu;
		}
	}

	public class About {
		private String titolo;
		private String versione;
		private String build;
		private URI projectPage;
		private URI manualeUso;
		private String copyright;
		private URI licenza;
		
		public URI getProjectPage() {
			return this.projectPage;
		}
		public void setProjectPage(URI projectPage) {
			this.projectPage = projectPage;
		}
		public URI getManualeUso() {
			return this.manualeUso;
		}
		public void setManualeUso(URI manualeUso) {
			this.manualeUso = manualeUso;
		}
		public String getVersione() {
			return this.versione;
		}
		public void setVersione(String versione) {
			this.versione = versione;
		}
		public String getBuild() {
			return this.build;
		}
		public void setBuild(String build) {
			this.build = build;
		}
		public URI getLicenza() {
			return this.licenza;
		}
		public void setLicenza(URI licenza) {
			this.licenza = licenza;
		}
		public String getTitolo() {
			return this.titolo;
		}
		public void setTitolo(String titolo) {
			this.titolo = titolo;
		}
		public String getCopyright() {
			return this.copyright;
		}
		public void setCopyright(String copyright) {
			this.copyright = copyright;
		}
	}
	
	public class Menu {
		private String titolo;
		private VoceMenu home;
		private List<SezioneMenu> sezioni;
		
		public Menu(String titolo) {
			this.titolo = titolo;
			this.sezioni = new ArrayList<SezioneMenu>();
		}
		
		public String getTitolo() {
			return this.titolo;
		}
		public void setTitolo(String titolo) {
			this.titolo = titolo;
		}
		public List<SezioneMenu> getSezioni() {
			return this.sezioni;
		}
		public void setSezioni(List<SezioneMenu> sezioni) {
			this.sezioni = sezioni;
		}

		public VoceMenu getHome() {
			return this.home;
		}

		public void setHome(VoceMenu home) {
			this.home = home;
		} 
	}
	
	private String titolo;
	private Menu menu;
	private About about;
	private URI logout;
	
	public Console(String titolo, URI logout) {
		this.titolo = titolo;
		this.logout = logout;
	}
	
	public String getTitolo() {
		return this.titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public Menu getMenu() {
		return this.menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	public About getAbout() {
		return this.about;
	}
	public void setAbout(About about) {
		this.about = about;
	}
	public URI getLogout() {
		return this.logout;
	}
	public void setLogout(URI logout) {
		this.logout = logout;
	}

	
}
