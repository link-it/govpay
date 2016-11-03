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

import java.util.ArrayList;
import java.util.List;

import it.govpay.model.Intermediario;

public class IntermediarioConverter {

	public static List<Intermediario> toDTOList(List<it.govpay.orm.Intermediario> intermediarioLst) {
		List<Intermediario> lstDTO = new ArrayList<Intermediario>();
		if(intermediarioLst != null && !intermediarioLst.isEmpty()) {
			for(it.govpay.orm.Intermediario intermediario: intermediarioLst){
				lstDTO.add(toDTO(intermediario));
			}
		}
		return lstDTO;
	}

	public static Intermediario toDTO(it.govpay.orm.Intermediario vo) {
		Intermediario dto = new Intermediario();
		dto.setCodIntermediario(vo.getCodIntermediario());
		dto.setId(vo.getId());
		dto.setAbilitato(vo.isAbilitato());
		dto.setDenominazione(vo.getDenominazione());
		dto.setSegregationCode(vo.getSegregationCode());
		return dto;
	}

	public static it.govpay.orm.Intermediario toVO(Intermediario dto) {
		it.govpay.orm.Intermediario vo = new it.govpay.orm.Intermediario();
		vo.setCodIntermediario(dto.getCodIntermediario());
		vo.setId(dto.getId());
		vo.setAbilitato(dto.isAbilitato());
		vo.setDenominazione(dto.getDenominazione());
		vo.setCodConnettorePdd(dto.getCodIntermediario());
		vo.setSegregationCode(dto.getSegregationCode());
		return vo;
	}

}
