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
package it.govpay.core.dao.anagrafica.dto;

import org.springframework.security.core.Authentication;

import it.govpay.core.dao.pagamenti.dto.AbstractPatchDTO;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 28 giu 2018 $
 * 
 */
public class PatchRuoloDTO extends AbstractPatchDTO {

	private String idRuolo;
	
	public PatchRuoloDTO(Authentication user) {
		super(user);
	}

	public String getIdRuolo() {
		return this.idRuolo;
	}

	public void setIdRuolo(String idRuolo) {
		this.idRuolo = idRuolo;
	}

	
}
