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
package it.govpay.model;

public class ConnettoreSftp extends BasicModel {
	
	private static final long serialVersionUID = 1L;
	public static final String P_URL_NAME_IN = "URL_IN";
	public static final String P_USER_NAME_IN = "USER_IN";
	public static final String P_PASS_NAME_IN = "PASS_IN";

	public static final String P_URL_NAME_OUT = "URL_OUT";
	public static final String P_USER_NAME_OUT = "USER_OUT";
	public static final String P_PASS_NAME_OUT = "PASS_OUT";
	
	private String idConnettore;
	private String httpUserIn;
	private String httpPasswIn;
	private String urlIn;
	private String httpUserOut;
	private String httpPasswOut;
	private String urlOut;
	
	public ConnettoreSftp() {
	}

	public String getIdConnettore() {
		return idConnettore;
	}

	public void setIdConnettore(String idConnettore) {
		this.idConnettore = idConnettore;
	}

	public String getHttpUserIn() {
		return httpUserIn;
	}

	public void setHttpUserIn(String httpUserIn) {
		this.httpUserIn = httpUserIn;
	}

	public String getHttpPasswIn() {
		return httpPasswIn;
	}

	public void setHttpPasswIn(String httpPasswIn) {
		this.httpPasswIn = httpPasswIn;
	}

	public String getUrlIn() {
		return urlIn;
	}

	public void setUrlIn(String urlIn) {
		this.urlIn = urlIn;
	}

	public String getHttpUserOut() {
		return httpUserOut;
	}

	public void setHttpUserOut(String httpUserOut) {
		this.httpUserOut = httpUserOut;
	}

	public String getHttpPasswOut() {
		return httpPasswOut;
	}

	public void setHttpPasswOut(String httpPasswOut) {
		this.httpPasswOut = httpPasswOut;
	}

	public String getUrlOut() {
		return urlOut;
	}

	public void setUrlOut(String urlOut) {
		this.urlOut = urlOut;
	}

}
