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

import it.govpay.bd.model.Ente;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdMailTemplate;

import java.util.ArrayList;
import java.util.List;

public class EnteConverter {

	public static List<Ente> toDTOList(List<it.govpay.orm.Ente> enteLst) {
		List<Ente> lstDTO = new ArrayList<Ente>();
		if(enteLst != null && !enteLst.isEmpty()) {
			for(it.govpay.orm.Ente ente: enteLst){
				lstDTO.add(toDTO(ente));
			}
		}
		return lstDTO;
	}

	public static Ente toDTO(it.govpay.orm.Ente vo) {
		Ente dto = new Ente();
		dto.setId(vo.getId());
		dto.setCodEnte(vo.getCodEnte());
		dto.setAttivo(vo.isAbilitato());
		dto.setIdDominio(vo.getIdDominio().getId());
		dto.setInvioMailRptAbilitato(vo.isInvioMailRPTAbilitato());
		dto.setInvioMailRtAbilitato(vo.isInvioMailRTAbilitato());
		if(vo.getIdTemplateRPT() != null) {
			dto.setIdMailTemplateRPT(vo.getIdTemplateRPT().getId());
		}
		
		if(vo.getIdTemplateRT() != null) {
			dto.setIdMailTemplateRT(vo.getIdTemplateRT().getId());
		}
		return dto;
	}

	public static it.govpay.orm.Ente toVO(Ente dto) {
		it.govpay.orm.Ente vo = new it.govpay.orm.Ente();
		vo.setId(dto.getId());
		vo.setCodEnte(dto.getCodEnte());
		vo.setAbilitato(dto.isAttivo());
		IdDominio dominio = new IdDominio();
		dominio.setId(dto.getIdDominio());
		vo.setIdDominio(dominio);
		vo.setInvioMailRPTAbilitato(dto.isInvioMailRptAbilitato());
		vo.setInvioMailRTAbilitato(dto.isInvioMailRtAbilitato());
		
		if(dto.getIdMailTemplateRPT() != null) {
			IdMailTemplate idTemplateRPT = new IdMailTemplate();
			idTemplateRPT.setId(dto.getIdMailTemplateRPT());
			vo.setIdTemplateRPT(idTemplateRPT);
		}
		
		if(dto.getIdMailTemplateRT() != null) {
			IdMailTemplate idTemplateRT = new IdMailTemplate();
			idTemplateRT.setId(dto.getIdMailTemplateRT());
			vo.setIdTemplateRT(idTemplateRT);
		}

		return vo;
	}

}
