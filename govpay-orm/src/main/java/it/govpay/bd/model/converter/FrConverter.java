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

import it.govpay.model.Fr;
import it.govpay.model.Fr.StatoFr;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdPsp;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class FrConverter {

	public static List<Fr> toDTOList(List<it.govpay.orm.FR> lst) throws ServiceException {
		List<Fr> lstDTO = new ArrayList<Fr>();
		if(lst != null && !lst.isEmpty()) {
			for(it.govpay.orm.FR vo: lst){
				lstDTO.add(toDTO(vo));
			}
		}
		return lstDTO;
	}

	public static Fr toDTO(it.govpay.orm.FR vo) throws ServiceException {
		Fr dto = new Fr();
		dto.setAnnoRiferimento(vo.getAnnoRiferimento());
		dto.setCodBicRiversamento(vo.getCodBicRiversamento());
		dto.setCodFlusso(vo.getCodFlusso());
		dto.setDataAcquisizione(vo.getDataAcquisizione());
		dto.setDataFlusso(vo.getDataOraFlusso());
		dto.setDataRegolamento(vo.getDataRegolamento());
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		dto.setId(vo.getId());
		dto.setIdPsp(vo.getIdPsp().getId());
		dto.setIdDominio(vo.getIdDominio().getId());
		dto.setImportoTotalePagamenti(vo.getImportoTotalePagamenti());
		dto.setIur(vo.getIur());
		dto.setNumeroPagamenti(vo.getNumeroPagamenti());
		dto.setStato(StatoFr.valueOf(vo.getStato()));
		dto.setXml(vo.getXml());
		
		return dto;
	}

	public static it.govpay.orm.FR toVO(Fr dto) {
		it.govpay.orm.FR vo = new it.govpay.orm.FR();
		vo.setAnnoRiferimento(dto.getAnnoRiferimento());
		vo.setCodBicRiversamento(dto.getCodBicRiversamento());
		vo.setCodFlusso(dto.getCodFlusso());
		vo.setDataAcquisizione(dto.getDataAcquisizione());
		vo.setDataOraFlusso(dto.getDataFlusso());
		vo.setDataRegolamento(dto.getDataRegolamento());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		vo.setId(dto.getId());
		
		IdDominio idDominio = new IdDominio();
		idDominio.setId(dto.getIdDominio());
		vo.setIdDominio(idDominio);
		
		IdPsp idPsp = new IdPsp();
		idPsp.setId(dto.getIdPsp());
		vo.setIdPsp(idPsp);
		
		vo.setImportoTotalePagamenti(dto.getImportoTotalePagamenti());
		vo.setIur(dto.getIur());
		vo.setNumeroPagamenti(dto.getNumeroPagamenti());
		vo.setStato(dto.getStato().toString());
		vo.setXml(dto.getXml());
		return vo;
	}

}
