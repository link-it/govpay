/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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

import it.govpay.bd.model.MediaRilevamento;
import it.govpay.orm.IdSla;

import java.util.ArrayList;
import java.util.List;

public class MediaRilevamentoConverter {

	public static List<MediaRilevamento> toDTOList(List<it.govpay.orm.MediaRilevamento> lst) {
		List<MediaRilevamento> lstDTO = new ArrayList<MediaRilevamento>();
		if(lst != null && !lst.isEmpty()) {
			for(it.govpay.orm.MediaRilevamento obj: lst){
				lstDTO.add(toDTO(obj));
			}
		}
		return lstDTO;
	}

	public static MediaRilevamento toDTO(it.govpay.orm.MediaRilevamento vo) {
		MediaRilevamento dto = new MediaRilevamento();
		dto.setId(vo.getId());
		dto.setIdSLA(vo.getIdSLA().getId());
		dto.setIdApplicazione(vo.getIdApplicazione());
		dto.setDataOsservazione(vo.getDataOsservazione());
		dto.setNumRilevamentiA(vo.getNumRilevamentiA());
		dto.setPercentualeA(vo.getPercentualeA());
		dto.setNumRilevamentiB(vo.getNumRilevamentiB());
		dto.setPercentualeB(vo.getPercentualeB());
		dto.setNumRilevamentiOver(vo.getNumRilevamentiOver());
		return dto;
	}

	public static it.govpay.orm.MediaRilevamento toVO(MediaRilevamento dto) {
		it.govpay.orm.MediaRilevamento vo = new it.govpay.orm.MediaRilevamento();
		vo.setId(dto.getId());

		IdSla idSla = new IdSla();
		idSla.setId(dto.getIdSLA());
		vo.setIdSLA(idSla);
		
		vo.setIdApplicazione(dto.getIdApplicazione());
		vo.setDataOsservazione(dto.getDataOsservazione());
		vo.setNumRilevamentiA(dto.getNumRilevamentiA());
		vo.setPercentualeA(dto.getPercentualeA());
		vo.setNumRilevamentiB(dto.getNumRilevamentiB());
		vo.setPercentualeB(dto.getPercentualeB());
		vo.setNumRilevamentiOver(dto.getNumRilevamentiOver());
		return vo;
	}

}
