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

import it.govpay.bd.model.Iuv;
import it.govpay.orm.IdApplicazione;

import java.util.ArrayList;
import java.util.List;

public class IuvConverter {

	public static List<Iuv> toDTOList(List<it.govpay.orm.IUV> applicazioneLst) {
		List<Iuv> lstDTO = new ArrayList<Iuv>();
		if(applicazioneLst != null && !applicazioneLst.isEmpty()) {
			for(it.govpay.orm.IUV applicazione: applicazioneLst){
				lstDTO.add(toDTO(applicazione));
			}
		}
		return lstDTO;
	}

	public static Iuv toDTO(it.govpay.orm.IUV vo) {
		Iuv dto = new Iuv();
		dto.setId(vo.getId());
		dto.setCodDominio(vo.getCodDominio());
		dto.setPrg(vo.getPrg());

		dto.setDataGenerazione(vo.getDataGenerazione());
		dto.setIdApplicazione(vo.getIdApplicazione().getId());
		dto.setIuv(vo.getIuv());
		dto.setApplicationCode(vo.getApplicationCode());
		dto.setAuxDigit(vo.getAuxDigit());
		
		return dto;
	}

	public static it.govpay.orm.IUV toVO(Iuv dto) {
		it.govpay.orm.IUV vo = new it.govpay.orm.IUV();
		vo.setId(dto.getId());
		vo.setId(dto.getId());
		vo.setCodDominio(dto.getCodDominio());
		vo.setPrg(dto.getPrg());

		vo.setDataGenerazione(dto.getDataGenerazione());
		IdApplicazione idApp = new IdApplicazione();
		idApp.setId(dto.getIdApplicazione());
		vo.setIdApplicazione(idApp);
		vo.setIuv(dto.getIuv());
		vo.setApplicationCode(dto.getApplicationCode());
		vo.setAuxDigit(dto.getAuxDigit());
				
		
		return vo;
	}

}
