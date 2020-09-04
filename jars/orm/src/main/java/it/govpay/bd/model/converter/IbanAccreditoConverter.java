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

import it.govpay.bd.model.IbanAccredito;
import it.govpay.orm.IdDominio;

import java.util.ArrayList;
import java.util.List;

public class IbanAccreditoConverter {

	public static List<IbanAccredito> toDTOList(List<it.govpay.orm.IbanAccredito> ibanAccreditoLst) {
		List<IbanAccredito> lstDTO = new ArrayList<>();
		if(ibanAccreditoLst != null && !ibanAccreditoLst.isEmpty()) {
			for(it.govpay.orm.IbanAccredito ibanAccredito: ibanAccreditoLst){
				lstDTO.add(toDTO(ibanAccredito));
			}
		}
		return lstDTO;
	}

	public static IbanAccredito toDTO(it.govpay.orm.IbanAccredito vo) {
		IbanAccredito dto = new IbanAccredito();
		dto.setId(vo.getId());
		dto.setCodIban(toNull(vo.getCodIban()));
		dto.setCodBic(toNull(vo.getBicAccredito()));
		dto.setPostale(vo.getPostale());
		dto.setDescrizione(vo.getDescrizione());
		dto.setAbilitato(vo.getAbilitato());
		dto.setIdDominio(vo.getIdDominio().getId());
		dto.setIntestatario(vo.getIntestatario());
		
		return dto;
	}

	public static it.govpay.orm.IbanAccredito toVO(it.govpay.model.IbanAccredito dto) {
		it.govpay.orm.IbanAccredito vo = new it.govpay.orm.IbanAccredito();
		vo.setId(dto.getId());
		vo.setCodIban(toNull(dto.getCodIban()));
		vo.setBicAccredito(toNull(dto.getCodBic()));
		vo.setPostale(dto.isPostale());
		vo.setDescrizione(dto.getDescrizione());
		vo.setAbilitato(dto.isAbilitato());
		IdDominio idDominio = new IdDominio();
		idDominio.setId(dto.getIdDominio());
		vo.setIdDominio(idDominio);
		vo.setIntestatario(dto.getIntestatario());

		return vo;
	}
	
	private static String toNull(String s) {
		if(s == null || s.trim().length() == 0)
			return null;
		else
			return s.trim();
	}

}
