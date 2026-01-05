/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.model.configurazione;

import java.io.Serializable;

public class MailBatch implements Serializable{

	public static final String IT_GOVPAY_INVIOPROMEMORIA_ENABLED = "it.govpay.invioPromemoria.enabled";
	public static final String IT_GOVPAY_INVIOPROMEMORIA_MAILSERVER_HOST = "it.govpay.invioPromemoria.mailServer.host";
	public static final String IT_GOVPAY_INVIOPROMEMORIA_MAILSERVER_PORT = "it.govpay.invioPromemoria.mailServer.port";
	public static final String IT_GOVPAY_INVIOPROMEMORIA_MAILSERVER_USERNAME = "it.govpay.invioPromemoria.mailServer.username";
	public static final String IT_GOVPAY_INVIOPROMEMORIA_MAILSERVER_PASSWORD = "it.govpay.invioPromemoria.mailServer.password";
	public static final String IT_GOVPAY_INVIOPROMEMORIA_MAILSERVER_FROM = "it.govpay.invioPromemoria.mailServer.from";
	public static final String IT_GOVPAY_INVIOPROMEMORIA_MAILSERVER_READTIMEOUT = "it.govpay.invioPromemoria.mailServer.readTimeout";
	public static final String IT_GOVPAY_INVIOPROMEMORIA_MAILSERVER_CONNECTIONTIMEOUT = "it.govpay.invioPromemoria.mailServer.connectionTimeout";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean abilitato;
	private MailServer mailserver;

	public boolean isAbilitato() {
		return abilitato;
	}
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}
	public MailServer getMailserver() {
		return mailserver;
	}
	public void setMailserver(MailServer mailserver) {
		this.mailserver = mailserver;
	}
}
