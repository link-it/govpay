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
package it.govpay.pagamento.v1.beans.converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AclConverter {
	
	public static it.govpay.pagamento.v1.beans.AclPost toRsModel(it.govpay.bd.model.Acl acl) {
		it.govpay.pagamento.v1.beans.AclPost rsModel = new it.govpay.pagamento.v1.beans.AclPost();
		rsModel.principal(acl.getUtenzaPrincipalOriginale())
		.ruolo(acl.getRuolo());
		
		if(acl.getServizio() != null) {
			switch(acl.getServizio()) {
			case ANAGRAFICA_APPLICAZIONI:
			case ANAGRAFICA_CREDITORE:
			case ANAGRAFICA_PAGOPA:
			case ANAGRAFICA_RUOLI:
			case CONFIGURAZIONE_E_MANUTENZIONE:
			case GIORNALE_DEGLI_EVENTI:
			case PAGAMENTI:
			case PENDENZE:
			case RENDICONTAZIONI_E_INCASSI:
				rsModel.setServizio(acl.getServizio().getCodifica());
				break;
			case API_PAGAMENTI:
			case API_PENDENZE:
			case API_RAGIONERIA:
				return null;
			}
		}
		
		if(acl.getListaDiritti() != null) {
			List<String> diritti = acl.getListaDiritti().stream().map(a -> a.toString()).collect(Collectors.toList());
			Collections.sort(diritti);
			rsModel.autorizzazioni(diritti);
		}
		
		return rsModel;
	}
	
}
