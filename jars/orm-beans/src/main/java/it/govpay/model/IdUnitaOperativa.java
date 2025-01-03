/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

public class IdUnitaOperativa extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	private Long idUnita; 
	private Long idDominio;
	private Long id;
	
	public Long getIdUnita() {
		return idUnita;
	}
	public void setIdUnita(Long idUnita) {
		this.idUnita = idUnita;
	}
	public Long getIdDominio() {
		return idDominio;
	}
	public void setIdDominio(Long idDominio) {
		this.idDominio = idDominio;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

}
