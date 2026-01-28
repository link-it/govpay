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
package it.govpay.pagamento.v2.beans.converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import it.govpay.pagamento.v2.beans.TipoServizio;

public class AclConverter {

	public static it.govpay.pagamento.v2.beans.Acl toRsModel(it.govpay.bd.model.Acl acl) {
		it.govpay.pagamento.v2.beans.Acl rsModel = new it.govpay.pagamento.v2.beans.Acl();
		rsModel.principal(acl.getUtenzaPrincipalOriginale())
		.ruolo(acl.getRuolo());

		TipoServizio serv = null;
		if(acl.getServizio() != null) {
			switch(acl.getServizio()) {
			case ANAGRAFICA_APPLICAZIONI:
				serv = TipoServizio.ANAGRAFICA_APPLICAZIONI;
				break;
			case ANAGRAFICA_CREDITORE:
				serv = TipoServizio.ANAGRAFICA_CREDITORE;
				break;
			case ANAGRAFICA_PAGOPA:
				serv = TipoServizio.ANAGRAFICA_PAGOPA;
				break;
			case ANAGRAFICA_RUOLI:
				serv = TipoServizio.ANAGRAFICA_RUOLI;
				break;
			case CONFIGURAZIONE_E_MANUTENZIONE:
				serv = TipoServizio.CONFIGURAZIONE_E_MANUTENZIONE;
				break;
			case GIORNALE_DEGLI_EVENTI:
				serv = TipoServizio.GIORNALE_DEGLI_EVENTI;
				break;
			case PAGAMENTI:
				serv = TipoServizio.PAGAMENTI;
				break;
			case PENDENZE:
				serv = TipoServizio.PENDENZE;
				break;
			case RENDICONTAZIONI_E_INCASSI:
				serv = TipoServizio.RENDICONTAZIONI_E_INCASSI;
				break;
			case API_PAGAMENTI:
			case API_PENDENZE:
			case API_RAGIONERIA:
				return null;
			}
		}

		// se l'acl non deve uscire allora ritorno null
		if(serv ==null)
			return null;

		rsModel.setServizio(serv);

		if(acl.getListaDiritti() != null) {
			List<String> diritti = acl.getListaDiritti().stream().map(a -> a.toString()).collect(Collectors.toList());
			Collections.sort(diritti);
			rsModel.autorizzazioni(diritti);
		}

		return rsModel;
	}

}
