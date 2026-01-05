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
package it.govpay.core.dao.pagamenti.dto;

import org.springframework.security.core.Authentication;

public class PatchPendenzaDTO extends AbstractPatchDTO  {
	
	public enum STATO_PENDENZA { ANNULLATO, DA_PAGARE}
	
	private String idA2a;
	private String idPendenza;
	private boolean infoIncasso = false;

	public PatchPendenzaDTO(Authentication user) {
		super(user);
	}

	public String getIdA2a() {
		return this.idA2a;
	}

	public void setIdA2a(String idA2a) {
		this.idA2a = idA2a;
	}

	public String getIdPendenza() {
		return this.idPendenza;
	}

	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}

	public boolean isInfoIncasso() {
		return infoIncasso;
	}

	public void setInfoIncasso(boolean infoIncasso) {
		this.infoIncasso = infoIncasso;
	}

}
