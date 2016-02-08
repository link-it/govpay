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

import it.govpay.bd.model.Rt;
import it.govpay.bd.model.Rt.EsitoPagamento;
import it.govpay.bd.model.Rt.StatoRt;
import it.govpay.orm.IdRpt;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class RtConverter {

	public static List<Rt> toDTOList(List<it.govpay.orm.RT> applicazioneLst) throws ServiceException {
		List<Rt> lstDTO = new ArrayList<Rt>();
		if(applicazioneLst != null && !applicazioneLst.isEmpty()) {
			for(it.govpay.orm.RT applicazione: applicazioneLst){
				lstDTO.add(toDTO(applicazione));
			}
		}
		return lstDTO;
	}

	public static Rt toDTO(it.govpay.orm.RT vo) throws ServiceException {
		Rt dto = new Rt();
		dto.setId(vo.getId());
		dto.setCodMsgRicevuta(vo.getCodMsgRicevuta());
		dto.setDataOraMsgRicevuta(vo.getDataOraMsgRicevuta());
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		dto.setEsitoPagamento(EsitoPagamento.toEnum(vo.getCodEsitoPagamento()));
		dto.setIdRpt(vo.getIdRPT().getId());
		dto.setIdTracciatoXML(vo.getIdTracciato().getId());
		dto.setStato(StatoRt.toEnum(vo.getStato().charAt(0)));
		return dto;
	}

	public static it.govpay.orm.RT toVO(Rt dto) {
		it.govpay.orm.RT vo = new it.govpay.orm.RT();
		vo.setId(dto.getId());
		vo.setCodMsgRicevuta(dto.getCodMsgRicevuta());
		vo.setDataOraMsgRicevuta(dto.getDataOraMsgRicevuta());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		vo.setCodEsitoPagamento(dto.getEsitoPagamento().getCodifica());
		
		IdRpt idRpt = new IdRpt();
		idRpt.setId(dto.getIdRpt());
		vo.setIdRPT(idRpt);
		
		vo.setStato(dto.getStato().name());

		return vo;
	}

}
