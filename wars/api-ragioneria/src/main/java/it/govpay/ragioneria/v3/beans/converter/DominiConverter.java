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
package it.govpay.ragioneria.v3.beans.converter;

import it.govpay.ragioneria.v3.beans.Dominio;
import it.govpay.ragioneria.v3.beans.UnitaOperativa;

public class DominiConverter {
	
	private DominiConverter() {}

	public static Dominio toRsModelIndex(it.govpay.bd.model.Dominio dominio)  {
		if(dominio == null)
			return null;

		Dominio rsModel = new Dominio();
		rsModel.setIdDominio(dominio.getCodDominio());
		rsModel.setRagioneSociale(dominio.getRagioneSociale());
		return rsModel;
	}

	public static UnitaOperativa toRsModelIndex(it.govpay.bd.model.UnitaOperativa uo)  {
		if(uo == null)
			return null;

		UnitaOperativa rsModel = new UnitaOperativa();
		rsModel.setIdUnita(uo.getCodUo());
		rsModel.setRagioneSociale(uo.getAnagrafica().getRagioneSociale());
		return rsModel;
	}
}
