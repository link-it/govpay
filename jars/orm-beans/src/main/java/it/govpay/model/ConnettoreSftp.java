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
	public static final String P_HOST_NAME_IN = "HOST_IN";
	public static final String P_PORTA_NAME_IN = "PORTA_IN";
	public static final String P_USER_NAME_IN = "USER_IN";
	public static final String P_PASS_NAME_IN = "PASS_IN";

	public static final String P_HOST_NAME_OUT = "HOST_OUT";
	public static final String P_PORTA_NAME_OUT = "PORTA_OUT";
	public static final String P_USER_NAME_OUT = "USER_OUT";
	public static final String P_PASS_NAME_OUT = "PASS_OUT";
	
	private String idConnettore;
	private String httpUserIn;
	private String httpPasswIn;
	private String hostIn;
	private String portaIn;
	private String httpUserOut;
	private String httpPasswOut;
	private String hostOut;
	private String portaOut;
	
	public ConnettoreSftp() {
	}

	public String getIdConnettore() {
		return this.idConnettore;
	}

	public void setIdConnettore(String idConnettore) {
		this.idConnettore = idConnettore;
	}

	public String getHttpUserIn() {
		return this.httpUserIn;
	}

	public void setHttpUserIn(String httpUserIn) {
		this.httpUserIn = httpUserIn;
	}

	public String getHttpPasswIn() {
		return this.httpPasswIn;
	}

	public void setHttpPasswIn(String httpPasswIn) {
		this.httpPasswIn = httpPasswIn;
	}

	public String getHostIn() {
		return hostIn;
	}

	public void setHostIn(String hostIn) {
		this.hostIn = hostIn;
	}

	public String getPortaIn() {
		return portaIn;
	}

	public void setPortaIn(String portaIn) {
		this.portaIn = portaIn;
	}

	public String getHttpUserOut() {
		return this.httpUserOut;
	}

	public void setHttpUserOut(String httpUserOut) {
		this.httpUserOut = httpUserOut;
	}

	public String getHttpPasswOut() {
		return this.httpPasswOut;
	}

	public void setHttpPasswOut(String httpPasswOut) {
		this.httpPasswOut = httpPasswOut;
	}

	public String getHostOut() {
		return hostOut;
	}

	public void setHostOut(String hostOut) {
		this.hostOut = hostOut;
	}

	public String getPortaOut() {
		return portaOut;
	}

	public void setPortaOut(String portaOut) {
		this.portaOut = portaOut;
	}
}
