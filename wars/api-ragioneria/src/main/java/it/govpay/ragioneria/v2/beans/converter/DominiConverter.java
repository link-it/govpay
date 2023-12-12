/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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

import it.govpay.ragioneria.v2.beans.Dominio;

public class DominiConverter {


	public static Dominio toRsModelIndex(it.govpay.bd.model.Dominio dominio) {
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
//		rsModel.setAuxDigit("" + dominio.getAuxDigit());
//		if(dominio.getSegregationCode() != null)
//			rsModel.setSegregationCode(String.format("%02d", dominio.getSegregationCode()));
//		if(dominio.getLogo() != null) {
//			rsModel.setLogo(UriBuilderUtils.getLogoDominio(dominio.getCodDominio()));
//		}
//		rsModel.setIuvPrefix(dominio.getIuvPrefix());
//		rsModel.setStazione(dominio.getStazione().getCodStazione());
//		rsModel.setAbilitato(dominio.isAbilitato());

		return rsModel;
	}

}
