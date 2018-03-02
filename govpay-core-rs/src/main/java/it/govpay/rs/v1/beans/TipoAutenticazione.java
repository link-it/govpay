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
package it.govpay.rs.v1.beans;

import org.codehaus.jackson.map.annotate.JsonFilter;

@JsonFilter(value="tipoAutenticazione")  
public class TipoAutenticazione extends it.govpay.rs.v1.beans.base.TipoAutenticazione {

	public TipoAutenticazione(it.govpay.model.Connettore connettore) {
		this.username(connettore.getHttpUser())
		.password(connettore.getHttpPassw())
		.ksLocation(connettore.getSslKsLocation())
		.ksPassword(connettore.getSslKsPasswd())
		.tsLocation(connettore.getSslTsLocation())
		.tsPassword(connettore.getSslTsPasswd());
		
		if(connettore.getTipo() != null)
			this.tipo(TipoEnum.fromValue(connettore.getTipo().toString()));
		
	}
	
	@Override
	public String getJsonIdFilter() {
		return "tipoAutenticazione";
	}
}

