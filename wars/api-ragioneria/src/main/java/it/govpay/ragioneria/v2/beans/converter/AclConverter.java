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
	package it.govpay.ragioneria.v2.beans.converter;

import java.util.stream.Collectors;

import it.govpay.ragioneria.v2.beans.Acl;
import it.govpay.ragioneria.v2.beans.TipoServizio;

public class AclConverter {


	public static Acl toRsModel(it.govpay.bd.model.Acl acl) {
		Acl rsModel = new Acl();
		rsModel.principal(acl.getUtenzaPrincipalOriginale())
		.ruolo(acl.getRuolo());

		if(acl.getServizio() != null) {
			switch(acl.getServizio()) {
			case ANAGRAFICA_APPLICAZIONI:
				rsModel.setServizio(TipoServizio.ANAGRAFICA_APPLICAZIONI);
				break;
			case ANAGRAFICA_CREDITORE:
				rsModel.setServizio(TipoServizio.ANAGRAFICA_CREDITORE);
				break;
			case ANAGRAFICA_PAGOPA:
				rsModel.setServizio(TipoServizio.ANAGRAFICA_PAGOPA);
				break;
			case ANAGRAFICA_RUOLI:
				rsModel.setServizio(TipoServizio.ANAGRAFICA_RUOLI);
				break;
			case CONFIGURAZIONE_E_MANUTENZIONE:
				rsModel.setServizio(TipoServizio.CONFIGURAZIONE_E_MANUTENZIONE);
				break;
			case GIORNALE_DEGLI_EVENTI:
				rsModel.setServizio(TipoServizio.GIORNALE_DEGLI_EVENTI);
				break;
			case PAGAMENTI:
				rsModel.setServizio(TipoServizio.PAGAMENTI);
				break;
			case PENDENZE:
				rsModel.setServizio(TipoServizio.PENDENZE);
				break;
			case RENDICONTAZIONI_E_INCASSI:
				rsModel.setServizio(TipoServizio.RENDICONTAZIONI_E_INCASSI);
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
