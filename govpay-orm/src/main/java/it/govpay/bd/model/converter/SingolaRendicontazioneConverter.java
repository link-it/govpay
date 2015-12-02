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

import it.govpay.bd.model.SingolaRendicontazione;
import it.govpay.orm.IdSingoloVersamento;

import java.util.ArrayList;
import java.util.List;

public class SingolaRendicontazioneConverter {

	public static List<SingolaRendicontazione> toDTOList(List<it.govpay.orm.SingolaRendicontazione> lst) {
		List<SingolaRendicontazione> lstDTO = new ArrayList<SingolaRendicontazione>();
		if(lst != null && !lst.isEmpty()) {
			for(it.govpay.orm.SingolaRendicontazione vo: lst){
				lstDTO.add(toDTO(vo));
			}
		}
		return lstDTO;
	}

	public static SingolaRendicontazione toDTO(it.govpay.orm.SingolaRendicontazione vo) {
		SingolaRendicontazione dto = new SingolaRendicontazione();
		dto.setId(vo.getId());
		dto.setCodiceEsito(vo.getCodiceEsito());
		dto.setSingoloImporto(vo.getSingoloImporto());
		dto.setIdSingoloVersamento(vo.getIdSingoloVersamento().getId());
		dto.setIur(vo.getIur());
		dto.setIuv(vo.getIuv());
		dto.setDataEsito(vo.getDataEsito());
		return dto;
	}

	public static it.govpay.orm.SingolaRendicontazione toVO(SingolaRendicontazione dto) {
		it.govpay.orm.SingolaRendicontazione vo = new it.govpay.orm.SingolaRendicontazione();
		vo.setId(dto.getId());
		vo.setCodiceEsito(dto.getCodiceEsito());
		vo.setSingoloImporto(dto.getSingoloImporto());
		
		IdSingoloVersamento idSingoloVersamento = new IdSingoloVersamento();
		idSingoloVersamento.setId(dto.getIdSingoloVersamento());
		vo.setIdSingoloVersamento(idSingoloVersamento);
		
		vo.setIur(dto.getIur());
		vo.setIuv(dto.getIuv());
		vo.setDataEsito(dto.getDataEsito());
		return vo;
	}
}
