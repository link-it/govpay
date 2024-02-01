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
package it.govpay.ragioneria.v1.beans.converter;

import java.util.stream.Collectors;

import it.govpay.ragioneria.v1.beans.AclPost;

public class AclConverter {


	public static AclPost toRsModel(it.govpay.bd.model.Acl acl) {
		AclPost rsModel = new AclPost();
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

		if(acl.getListaDiritti() != null)
			rsModel.autorizzazioni(acl.getListaDiritti().stream().map(a -> a.getCodifica()).collect(Collectors.toList()));

		return rsModel;
	}
}
