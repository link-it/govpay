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
package it.govpay.pagamento.listener;

import it.govpay.core.beans.Costanti;
import it.govpay.core.utils.GpContext;
import it.govpay.web.listener.BaseInitListener;

public class InitListener extends BaseInitListener {
	
	@Override
	public String getDominioAnagraficaManager() {
		return Costanti.NOME_DOMINIO_CACHE_API_PAGAMENTO;
	}
	
	@Override
	public String getTipoServizioGovpay() {
		return GpContext.TIPO_SERVIZIO_GOVPAY_JSON;
	}
	
	@Override
	public String getWarName() {
		return Costanti.WAR_NAME_API_PAGAMENTO;
	}
}
