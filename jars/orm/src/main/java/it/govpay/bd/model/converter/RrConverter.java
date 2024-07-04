/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.model.Rr;
import it.govpay.model.Rr.StatoRr;
import it.govpay.orm.IdRpt;
import it.govpay.orm.RR;

public class RrConverter {
	
	private RrConverter() {}

	public static Rr toDTO(RR vo) {
		Rr dto = new Rr();
		dto.setCcp(vo.getCcp());
		dto.setCodDominio(vo.getCodDominio());
		dto.setCodMsgEsito(vo.getCodMsgEsito());
		dto.setCodMsgRevoca(vo.getCodMsgRevoca());
		dto.setDataMsgEsito(vo.getDataMsgEsito());
		dto.setDataMsgRevoca(vo.getDataMsgRevoca());
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		dto.setId(vo.getId());
		dto.setIdRpt(vo.getIdRpt().getId());
		dto.setIdTransazioneRr(vo.getCodTransazioneRR());
		dto.setIdTransazioneEr(vo.getCodTransazioneER());
		dto.setImportoTotaleRevocato(vo.getImportoTotaleRevocato());
		dto.setImportoTotaleRichiesto(BigDecimal.valueOf(vo.getImportoTotaleRichiesto()));
		dto.setIuv(vo.getIuv());
		dto.setStato(StatoRr.valueOf(vo.getStato()));
		dto.setXmlEr(vo.getXmlER());
		dto.setXmlRr(vo.getXmlRR());
		return dto;
	}
	
	public static RR toVO(Rr dto) {
		RR vo = new RR();
		vo.setCcp(dto.getCcp());
		vo.setCodDominio(dto.getCodDominio());
		vo.setCodMsgEsito(dto.getCodMsgEsito());
		vo.setCodMsgRevoca(dto.getCodMsgRevoca());
		vo.setCodTransazioneRR(dto.getIdTransazioneRr());
		vo.setCodTransazioneER(dto.getIdTransazioneEr());
		vo.setDataMsgEsito(dto.getDataMsgEsito());
		vo.setDataMsgRevoca(dto.getDataMsgRevoca());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		vo.setId(dto.getId());
		IdRpt idRpt = new IdRpt();
		idRpt.setId(dto.getIdRpt());
		vo.setIdRpt(idRpt);
		if(dto.getImportoTotaleRichiesto() != null)
			vo.setImportoTotaleRichiesto(dto.getImportoTotaleRichiesto().doubleValue());
		vo.setImportoTotaleRevocato(dto.getImportoTotaleRevocato());
		vo.setIuv(dto.getIuv());
		vo.setStato(dto.getStato().toString());
		vo.setXmlER(dto.getXmlEr());
		vo.setXmlRR(dto.getXmlRr());
		return vo;
	}

	public static List<Rr> toDTOList(List<RR> findAll) {
		List<Rr> listRr = new ArrayList<>();
		for(RR rr : findAll) {
			listRr.add(toDTO(rr));
		}
		return listRr;
	}

}
