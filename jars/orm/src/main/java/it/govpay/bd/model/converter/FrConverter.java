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

import it.govpay.bd.model.Fr;
import it.govpay.model.Fr.StatoFr;
import it.govpay.orm.IdIncasso;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class FrConverter {

	public static List<Fr> toDTOList(List<it.govpay.orm.FR> lst) throws ServiceException {
		List<Fr> lstDTO = new ArrayList<>();
		if(lst != null && !lst.isEmpty()) {
			for(it.govpay.orm.FR vo: lst){
				lstDTO.add(toDTO(vo));
			}
		}
		return lstDTO;
	}

	public static Fr toDTO(it.govpay.orm.FR vo) throws ServiceException {
		Fr dto = new Fr();
		dto.setStato(StatoFr.valueOf(vo.getStato()));
		dto.setCodBicRiversamento(vo.getCodBicRiversamento());
		dto.setCodFlusso(vo.getCodFlusso());
		dto.setCodDominio(vo.getCodDominio());
		dto.setCodPsp(vo.getCodPsp());
		dto.setDataAcquisizione(vo.getDataAcquisizione());
		dto.setDataFlusso(vo.getDataOraFlusso());
		dto.setDataRegolamento(vo.getDataRegolamento());
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		dto.setId(vo.getId());
		dto.setImportoTotalePagamenti(BigDecimal.valueOf(vo.getImportoTotalePagamenti()));
		dto.setIur(vo.getIur());
		dto.setNumeroPagamenti(vo.getNumeroPagamenti());
		dto.setXml(vo.getXml());
		if(vo.getIdIncasso() != null)
			dto.setIdIncasso(vo.getIdIncasso().getId());
		dto.setRagioneSocialeDominio(vo.getRagioneSocialeDominio());
		dto.setRagioneSocialePsp(vo.getRagioneSocialePsp());
		dto.setObsoleto(vo.getObsoleto());
		return dto;
	}

	public static it.govpay.orm.FR toVO(Fr dto) {
		it.govpay.orm.FR vo = new it.govpay.orm.FR();
		vo.setStato(dto.getStato().toString());
		vo.setCodBicRiversamento(dto.getCodBicRiversamento());
		vo.setCodFlusso(dto.getCodFlusso());
		vo.setCodDominio(dto.getCodDominio());
		vo.setCodPsp(dto.getCodPsp());
		vo.setDataAcquisizione(dto.getDataAcquisizione());
		vo.setDataOraFlusso(dto.getDataFlusso());
		vo.setDataRegolamento(dto.getDataRegolamento());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		vo.setId(dto.getId());
		vo.setImportoTotalePagamenti(dto.getImportoTotalePagamenti().doubleValue());
		vo.setIur(dto.getIur());
		vo.setNumeroPagamenti(dto.getNumeroPagamenti());
		vo.setObsoleto(dto.getObsoleto());
		
		vo.setXml(dto.getXml());
		
		if(dto.getIdIncasso() != null) {
			IdIncasso idIncasso = new IdIncasso();
			idIncasso.setId(dto.getIdIncasso());
			vo.setIdIncasso(idIncasso);
		}
		
		vo.setRagioneSocialeDominio(dto.getRagioneSocialeDominio());
		vo.setRagioneSocialePsp(dto.getRagioneSocialePsp());
		return vo;
	}

}
