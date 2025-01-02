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
package it.govpay.ragioneria.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.ragioneria.v1.beans.UnitaOperativa;

public class UnitaOperativaConverter {


	public static UnitaOperativa toRsModel(it.govpay.bd.model.UnitaOperativa uo) throws ServiceException {
		UnitaOperativa rsModel = new UnitaOperativa();

		rsModel.setAbilitato(uo.isAbilitato());
		rsModel.setArea(uo.getAnagrafica().getArea());
		rsModel.setCap(uo.getAnagrafica().getCap());
		rsModel.setCivico(uo.getAnagrafica().getCivico());
		rsModel.setEmail(uo.getAnagrafica().getEmail());
		rsModel.setFax(uo.getAnagrafica().getFax());
		rsModel.setIdUnita(uo.getCodUo());
		rsModel.setIndirizzo(uo.getAnagrafica().getIndirizzo());
		rsModel.setLocalita(uo.getAnagrafica().getLocalita());
		rsModel.setNazione(uo.getAnagrafica().getNazione());
		rsModel.setPec(uo.getAnagrafica().getPec());
		rsModel.setProvincia(uo.getAnagrafica().getProvincia());
		rsModel.setRagioneSociale(uo.getAnagrafica().getRagioneSociale());
		rsModel.setTel(uo.getAnagrafica().getTelefono());
		rsModel.setWeb(uo.getAnagrafica().getUrlSitoWeb());
		return rsModel;
	}

}
