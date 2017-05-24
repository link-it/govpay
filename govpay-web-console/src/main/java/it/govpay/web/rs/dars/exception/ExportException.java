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
package it.govpay.web.rs.dars.exception;

import java.util.ArrayList;
import java.util.List;

import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;

public class ExportException extends Exception {

	private EsitoOperazione esito = null;

	private List<String> messaggi;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExportException(String msg) {
		super(msg);
		this.messaggi = new ArrayList<String>();
		this.messaggi.add(msg);
	}

	public ExportException(Throwable t){
		super(t);
	}

	public ExportException(String msg,EsitoOperazione esito) {
		super(msg);
		this.esito = esito;
		this.messaggi = new ArrayList<String>();
		this.messaggi.add(msg);
	}

	public ExportException(List<String> msg,EsitoOperazione esito) {
		super();
		this.esito = esito;
		this.messaggi = new ArrayList<String>();
		this.messaggi.addAll(msg);
	}

	public ExportException(Throwable t,EsitoOperazione esito){
		super(t);
		this.esito = esito;
		this.messaggi = new ArrayList<String>();
	}

	public EsitoOperazione getEsito() {
		return this.esito;
	}

	public void setEsito(EsitoOperazione esito) {
		this.esito = esito;
	}

	public List<String> getMessaggi() {
		return messaggi;
	}

	public void setMessaggi(List<String> messaggi) {
		this.messaggi = messaggi;
	}

}
