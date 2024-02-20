/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import java.util.Date;

import javax.ws.rs.core.MediaType;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.dto.BasicCreateRequestDTO;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiFrDTO extends BasicCreateRequestDTO {

	private Boolean obsoleto = null;
	private String idFlusso;
	private String idDominio;
	private Date dataOraFlusso;
	private String accept;	

	public LeggiFrDTO(Authentication user, String idFlusso) {
		super(user);
		this.idFlusso=idFlusso;
		this.accept = MediaType.APPLICATION_JSON;
	}

	public String getIdFlusso() {
		return this.idFlusso;
	}

	public void setIdFlusso(String idFlusso) {
		this.idFlusso = idFlusso;
	}

	public Boolean getObsoleto() {
		return obsoleto;
	}

	public void setObsoleto(Boolean obsoleto) {
		this.obsoleto = obsoleto;
	}

	public Date getDataOraFlusso() {
		return dataOraFlusso;
	}

	public void setDataOraFlusso(Date dataOraFlusso) {
		this.dataOraFlusso = dataOraFlusso;
	}

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public String getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(String idDominio) {
		this.idDominio = idDominio;
	}

}
