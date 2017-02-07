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
import java.util.Map;

public class Console {
	
	private String titolo;
	private Menu menu;
	private About about;
	private URI logout;
	private Map<String, Map<String, String>> lingue;
	
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
	public Map<String, Map<String, String>> getLingue() {
		return lingue;
	}
	public void setLingue(Map<String, Map<String, String>> lingue) {
		this.lingue = lingue;
	}
}
