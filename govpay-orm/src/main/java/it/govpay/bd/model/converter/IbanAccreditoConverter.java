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

import it.govpay.bd.model.IbanAccredito;
import it.govpay.orm.IdDominio;

import java.util.ArrayList;
import java.util.List;

public class IbanAccreditoConverter {

	public static List<IbanAccredito> toDTOList(List<it.govpay.orm.IbanAccredito> ibanAccreditoLst) {
		List<IbanAccredito> lstDTO = new ArrayList<IbanAccredito>();
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
		dto.setCodIban(vo.getCodIban());
		dto.setCodBicAccredito(vo.getBicAccredito());
		dto.setCodIbanAppoggio(vo.getIbanAppoggio());
		dto.setCodBicAppoggio(vo.getBicAppoggio());
		dto.setIdSellerBank(vo.getIdSellerBank());
		dto.setIdNegozio(vo.getIdNegozio());
		dto.setPostale(vo.getPostale());
		dto.setAttivatoObep(vo.getAttivato());
		dto.setAbilitato(vo.getAbilitato());
		dto.setIdDominio(vo.getIdDominio().getId());

		return dto;
	}

	public static it.govpay.orm.IbanAccredito toVO(IbanAccredito dto) {
		it.govpay.orm.IbanAccredito vo = new it.govpay.orm.IbanAccredito();
		vo.setId(dto.getId());
		vo.setCodIban(dto.getCodIban());
		vo.setBicAccredito(dto.getCodBicAccredito());
		vo.setIbanAppoggio(dto.getCodIbanAppoggio());
		vo.setBicAppoggio(dto.getCodBicAppoggio());
		vo.setIdSellerBank(dto.getIdSellerBank());
		vo.setIdNegozio(dto.getIdNegozio());
		vo.setPostale(dto.isPostale());
		vo.setAttivato(dto.isAttivatoObep());
		vo.setAbilitato(dto.isAbilitato());
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
