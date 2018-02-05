/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.rs.v1.beans;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.utils.UriBuilderUtils;

@JsonFilter(value="domini")  
public class Dominio extends it.govpay.rs.v1.beans.base.Dominio {

	@Override
	public String getJsonIdFilter() {
		return "domini";
	}
	
	public static Dominio parse(String json) {
		return (Dominio) parse(json, Dominio.class);
	}
	
	public Dominio(it.govpay.bd.model.Dominio dominio) throws ServiceException {
//		this.setHref(uriBuilder.build().toString());
		this.setIdDominio(dominio.getCodDominio()); 
		this.setRagioneSociale(dominio.getRagioneSociale());
		this.setIndirizzo(dominio.getAnagrafica().getIndirizzo());
		this.setCivico(dominio.getAnagrafica().getCivico());
		this.setCap(dominio.getAnagrafica().getCap());
		this.setLocalita(dominio.getAnagrafica().getLocalita());
//		this.setProvincia(dominio.getAnagrafica().getProvincia());
//		this.setNazione(dominio.getAnagrafica().getNazione());
		this.setGln(dominio.getGln());
		this.setAuxDigit("" + dominio.getAuxDigit());
		this.setSegregationCode("" + dominio.getSegregationCode());
		if(dominio.getLogo() != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(dominio.getLogo(), false)));
			this.setLogo(sb.toString());
		}
		this.setIuvPrefix(dominio.getIuvPrefix());
		this.setStazione(dominio.getStazione().getCodStazione());
		this.setUnitaOperative(UriBuilderUtils.getListUoByDominio(dominio.getCodDominio()));
		this.setIbanAccredito(UriBuilderUtils.getIbanAccreditoByDominio(dominio.getCodDominio()));
		this.setEntrate(UriBuilderUtils.getEntrateByDominio(dominio.getCodDominio()));
		this.setAbilitato(dominio.isAbilitato());
	}

}