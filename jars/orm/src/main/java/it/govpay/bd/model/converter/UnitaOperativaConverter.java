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
package it.govpay.bd.model.converter;

import it.govpay.model.Anagrafica;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.orm.IdDominio;
import it.govpay.orm.Uo;

public class UnitaOperativaConverter {

	public static UnitaOperativa toDTO(Uo vo) {
		UnitaOperativa dto = new UnitaOperativa();
		dto.setAbilitato(vo.isAbilitato());
		dto.setCodUo(vo.getCodUo());
		dto.setId(vo.getId());
		dto.setIdDominio(vo.getIdDominio().getId());
		Anagrafica anagrafica = new Anagrafica();
		anagrafica.setCap(toNull(vo.getUoCap()));
		anagrafica.setCivico(toNull(vo.getUoCivico()));
		anagrafica.setCodUnivoco(toNull(vo.getUoCodiceIdentificativo()));
		anagrafica.setRagioneSociale(toNull(vo.getUoDenominazione()));
		anagrafica.setIndirizzo(toNull(vo.getUoIndirizzo()));
		anagrafica.setLocalita(toNull(vo.getUoLocalita()));
		anagrafica.setNazione(toNull(vo.getUoNazione()));
		anagrafica.setProvincia(toNull(vo.getUoProvincia()));
		anagrafica.setArea(toNull(vo.getUoArea()));
		anagrafica.setEmail(toNull(vo.getUoEmail()));
		anagrafica.setPec(toNull(vo.getUoPec()));
		anagrafica.setUrlSitoWeb(toNull(vo.getUoUrlSitoWeb()));
		anagrafica.setTelefono(toNull(vo.getUoTel()));
		anagrafica.setFax(toNull(vo.getUoFax()));
		dto.setAnagrafica(anagrafica);
		return dto;
	}

	public static Uo toVO(UnitaOperativa dto) {
		Uo vo = new Uo();
		vo.setId(dto.getId());
		vo.setAbilitato(dto.isAbilitato());
		vo.setCodUo(toNull(dto.getCodUo()));
		vo.setUoCap(toNull(dto.getAnagrafica().getCap()));
		vo.setUoCivico(toNull(dto.getAnagrafica().getCivico()));
		vo.setUoCodiceIdentificativo(toNull(dto.getAnagrafica().getCodUnivoco()));
		vo.setUoDenominazione(toNull(dto.getAnagrafica().getRagioneSociale()));
		vo.setUoIndirizzo(toNull(dto.getAnagrafica().getIndirizzo()));
		vo.setUoLocalita(toNull(dto.getAnagrafica().getLocalita()));
		vo.setUoNazione(toNull(dto.getAnagrafica().getNazione()));
		vo.setUoProvincia(toNull(dto.getAnagrafica().getProvincia()));
		vo.setUoArea(toNull(dto.getAnagrafica().getArea()));
		vo.setUoEmail(toNull(dto.getAnagrafica().getEmail()));
		vo.setUoPec(toNull(dto.getAnagrafica().getPec()));
		vo.setUoUrlSitoWeb(toNull(dto.getAnagrafica().getUrlSitoWeb()));
		vo.setUoTel(toNull(dto.getAnagrafica().getTelefono()));
		vo.setUoFax(toNull(dto.getAnagrafica().getFax()));
		
		IdDominio idDominio = new IdDominio();
		idDominio.setId(dto.getIdDominio());
		vo.setIdDominio(idDominio);
		
		return vo;
	}
	
	private static String toNull(String s) {
		if(s == null || s.trim().length() == 0)
			return null;
		else
			return s.trim();
	}

}
