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
package it.govpay.rs.eventi;

import org.apache.cxf.ext.logging.AbstractLoggingInterceptor;

import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.utils.GovpayConfig;

public class GiornaleEventiConfig {
	
	private boolean abilitaGiornaleEventi;
	private String apiName;
	private Integer limit = AbstractLoggingInterceptor.DEFAULT_LIMIT;	
	
	public GiornaleEventiConfig() {
		this.abilitaGiornaleEventi = GovpayConfig.getInstance().isGiornaleEventiEnabled();
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	
	public Componente getApiNameEnum() {
		return Componente.valueOf(this.apiName);
	}

	public boolean isAbilitaGiornaleEventi() {
		return abilitaGiornaleEventi;
	}

	public void setAbilitaGiornaleEventi(boolean abilitaGiornaleEventi) {
		this.abilitaGiornaleEventi = abilitaGiornaleEventi;
	}
	public Integer getLimit() {
		return this.limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
