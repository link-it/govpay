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
package it.govpay.bd.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

public class UtenzaAnonima extends Utenza {

	public static final String ID_UTENZA_ANONIMA = "UTENZA_ANONIMA"; 
	
	public UtenzaAnonima() {
		super();
		this.setPrincipal(null); 
		this.setIdDominiUo(new ArrayList<>());
		this.setIdTipiVersamento(new ArrayList<>());
		this.setDominiUo(new ArrayList<>());
		this.setTipiVersamento(new ArrayList<>());
		this.headers = new HashMap<>();
		this.autorizzazioneDominiStar = true;
		this.autorizzazioneTipiVersamentoStar = true;
		this.abilitato = true;
		this.ruoli = new ArrayList<>();
		this.setPassword(null);
	}
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public TIPO_UTENZA getTipoUtenza() {
		return TIPO_UTENZA.ANONIMO;
	}

	@Override
	public String getIdentificativo() {
		return StringUtils.isEmpty(this.getPrincipal()) ?  ID_UTENZA_ANONIMA : this.getPrincipal();
	}
}
