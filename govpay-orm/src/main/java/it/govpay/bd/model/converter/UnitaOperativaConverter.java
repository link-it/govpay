/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
