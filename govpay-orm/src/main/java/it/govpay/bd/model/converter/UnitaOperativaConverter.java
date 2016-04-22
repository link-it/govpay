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

import it.govpay.bd.model.Anagrafica;
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
		anagrafica.setCap(vo.getUoCap());
		anagrafica.setCivico(vo.getUoCivico());
		anagrafica.setCodUnivoco(vo.getUoCodiceIdentificativo());
		anagrafica.setRagioneSociale(vo.getUoDenominazione());
		anagrafica.setIndirizzo(vo.getUoIndirizzo());
		anagrafica.setLocalita(vo.getUoLocalita());
		anagrafica.setNazione(vo.getUoNazione());
		anagrafica.setProvincia(vo.getUoProvincia());
		dto.setAnagrafica(anagrafica);
		return dto;
	}

	public static Uo toVO(UnitaOperativa dto) {
		Uo vo = new Uo();
		vo.setId(dto.getId());
		vo.setAbilitato(dto.isAbilitato());
		vo.setCodUo(dto.getCodUo());
		vo.setUoCap(dto.getAnagrafica().getCap());
		vo.setUoCivico(dto.getAnagrafica().getCivico());
		vo.setUoCodiceIdentificativo(dto.getAnagrafica().getCodUnivoco());
		vo.setUoDenominazione(dto.getAnagrafica().getRagioneSociale());
		vo.setUoIndirizzo(dto.getAnagrafica().getIndirizzo());
		vo.setUoLocalita(dto.getAnagrafica().getLocalita());
		vo.setUoNazione(dto.getAnagrafica().getNazione());
		vo.setUoProvincia(dto.getAnagrafica().getProvincia());
		
		IdDominio idDominio = new IdDominio();
		idDominio.setId(dto.getIdDominio());
		vo.setIdDominio(idDominio);
		
		return vo;
	}

}
