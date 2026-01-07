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
package it.govpay.model;

public class Configurazione extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Long id;
	private String giornaleEventi;
	private String tracciatoCSV;
	private String confHardening;
	private String mailBatch;
	private String avvisaturaMail;
	private String avvisaturaAppIo;
	private String appIOBatch;
	
	@Override
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGiornaleEventi() {
		return giornaleEventi;
	}
	public void setGiornaleEventi(String giornaleEventi) {
		this.giornaleEventi = giornaleEventi;
	}
	public String getTracciatoCSV() {
		return tracciatoCSV;
	}
	public void setTracciatoCSV(String tracciatoCSV) {
		this.tracciatoCSV = tracciatoCSV;
	}
	public String getConfHardening() {
		return confHardening;
	}
	public void setConfHardening(String confHardening) {
		this.confHardening = confHardening;
	}
	public String getMailBatch() {
		return mailBatch;
	}
	public void setMailBatch(String mailBatch) {
		this.mailBatch = mailBatch;
	}
	public String getAvvisaturaMail() {
		return avvisaturaMail;
	}
	public void setAvvisaturaMail(String avvisaturaMail) {
		this.avvisaturaMail = avvisaturaMail;
	}
	public String getAvvisaturaAppIo() {
		return avvisaturaAppIo;
	}
	public void setAvvisaturaAppIo(String avvisaturaAppIo) {
		this.avvisaturaAppIo = avvisaturaAppIo;
	}
	public String getAppIOBatch() {
		return appIOBatch;
	}
	public void setAppIOBatch(String appIOBatch) {
		this.appIOBatch = appIOBatch;
	}

}
