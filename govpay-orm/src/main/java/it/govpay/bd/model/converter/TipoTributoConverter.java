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

import it.govpay.model.TipoTributo;

public class TipoTributoConverter {

	public static List<TipoTributo> toDTOList(List<it.govpay.orm.TipoTributo> lstVO) throws ServiceException {
		List<TipoTributo> lst = new ArrayList<TipoTributo>();
		if(lstVO != null && !lstVO.isEmpty()) {
			for(it.govpay.orm.TipoTributo vo: lstVO) {
				lst.add(toDTO(vo));
			}
		}
		return lst;
	}

	public static TipoTributo toDTO(it.govpay.orm.TipoTributo vo) throws ServiceException {
		TipoTributo dto = new TipoTributo();
		dto.setId(vo.getId());
		dto.setCodTributo(vo.getCodTributo());
		dto.setDescrizione(vo.getDescrizione());
		return dto;
	}

	public static it.govpay.orm.TipoTributo toVO(TipoTributo dto) {
		it.govpay.orm.TipoTributo vo = new it.govpay.orm.TipoTributo();
		vo.setId(dto.getId());
		vo.setCodTributo(dto.getCodTributo());
		vo.setDescrizione(dto.getDescrizione());
		return vo;
	}


}
