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

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Psp;

public class PspConverter {

	public static List<Psp> toDTOList(List<it.govpay.orm.Psp> lstVO) throws ServiceException {
		List<Psp> lst = new ArrayList<Psp>();
		if(lstVO != null && !lstVO.isEmpty()) {
			for(it.govpay.orm.Psp vo: lstVO) {
				lst.add(toDTO(vo));
			}
		}

		return lst;
	}

	public static Psp toDTO(it.govpay.orm.Psp vo) throws ServiceException {
		Psp dto = new Psp();
		dto.setId(vo.getId());
		dto.setCodPsp(vo.getCodPsp());
		dto.setRagioneSociale(vo.getRagioneSociale());
		dto.setUrlInfo(vo.getUrlInfo());
		dto.setAbilitato(vo.isAbilitato());
		dto.setBolloGestito(vo.getMarcaBollo());
		dto.setStornoGestito(vo.getStorno());

		return dto;
	}

	public static it.govpay.orm.Psp toVO(Psp dto) {
		it.govpay.orm.Psp vo = new it.govpay.orm.Psp();
		vo.setCodPsp(dto.getCodPsp());
		vo.setRagioneSociale(dto.getRagioneSociale());
		vo.setUrlInfo(dto.getUrlInfo());
		vo.setAbilitato(dto.isAbilitato());
		vo.setMarcaBollo(dto.isBolloGestito());
		vo.setStorno(dto.isStornoGestito());
		
		return vo;

	}

}
