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
package it.govpay.pendenze.v2.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.pendenze.v2.beans.Dominio;

public class DominiConverter {

	public static Dominio toRsModel(it.govpay.bd.model.Dominio dominio) throws ServiceException {
		Dominio rsModel = new Dominio();
//		rsModel.setWeb(dominio.getAnagrafica().getUrlSitoWeb());
		rsModel.setIdDominio(dominio.getCodDominio());
		rsModel.setRagioneSociale(dominio.getRagioneSociale());
//		rsModel.setIndirizzo(dominio.getAnagrafica().getIndirizzo());
//		rsModel.setCivico(dominio.getAnagrafica().getCivico());
//		rsModel.setCap(dominio.getAnagrafica().getCap());
//		rsModel.setLocalita(dominio.getAnagrafica().getLocalita());
//		rsModel.setProvincia(dominio.getAnagrafica().getProvincia());
//		rsModel.setNazione(dominio.getAnagrafica().getNazione());
//		rsModel.setEmail(dominio.getAnagrafica().getEmail());
//		rsModel.setPec(dominio.getAnagrafica().getPec());
//		rsModel.setTel(dominio.getAnagrafica().getTelefono());
//		rsModel.setFax(dominio.getAnagrafica().getFax());
//		rsModel.setGln(dominio.getGln());
//		if(dominio.getLogo() != null) {
//			rsModel.setLogo(UriBuilderUtils.getLogoDominio(dominio.getCodDominio()));
//		}

		return rsModel;
	}
}
