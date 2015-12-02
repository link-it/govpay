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

import it.govpay.bd.model.SingolaRevoca;
import it.govpay.bd.model.SingolaRevoca.Stato;
import it.govpay.orm.IdSingoloVersamento;

import java.util.ArrayList;
import java.util.List;

public class SingolaRevocaConverter {

	public static List<SingolaRevoca> toDTOList(List<it.govpay.orm.SingolaRevoca> lst) {
		List<SingolaRevoca> lstDTO = new ArrayList<SingolaRevoca>();
		if(lst != null && !lst.isEmpty()) {
			for(it.govpay.orm.SingolaRevoca vo: lst){
				lstDTO.add(toDTO(vo));
			}
		}
		return lstDTO;
	}

	public static SingolaRevoca toDTO(it.govpay.orm.SingolaRevoca vo) {
		SingolaRevoca dto = new SingolaRevoca();
		dto.setId(vo.getId());
		dto.setIdSingoloVersamento(vo.getIdSingoloVersamento().getId());
		dto.setStato(Stato.valueOf(vo.getStato()));
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		dto.setCausaleRevoca(vo.getCausaleRevoca());
		dto.setDatiAggiuntiviRevoca(vo.getDatiAggiuntiviRevoca());
		dto.setSingoloImporto(vo.getSingoloImporto());
		dto.setSingoloImportoRevocato(vo.getSingoloImportoRevocato());
		dto.setCausaleEsito(vo.getCausaleEsito());
		dto.setDatiAggiuntiviEsito(vo.getDatiAggiuntiviEsito());
		dto.setIdEr(vo.getIdER());
		return dto;
	}

	public static it.govpay.orm.SingolaRevoca toVO(SingolaRevoca dto) {
		it.govpay.orm.SingolaRevoca vo = new it.govpay.orm.SingolaRevoca();
		vo.setId(dto.getId());
		IdSingoloVersamento idSingoloVersamento = new IdSingoloVersamento();
		idSingoloVersamento.setId(dto.getIdSingoloVersamento());
		vo.setIdSingoloVersamento(idSingoloVersamento);
		vo.setStato(dto.getStato().toString());
		vo.setDescrizioneStato(dto.getDescrizioneStato());
		vo.setCausaleRevoca(dto.getCausaleRevoca());
		vo.setDatiAggiuntiviRevoca(dto.getDatiAggiuntiviRevoca());
		vo.setSingoloImporto(dto.getSingoloImporto());
		vo.setSingoloImportoRevocato(dto.getSingoloImportoRevocato());
		vo.setCausaleEsito(dto.getCausaleEsito());
		vo.setDatiAggiuntiviEsito(dto.getDatiAggiuntiviEsito());

		vo.setIdER(dto.getIdEr());
		return vo;
	}
}
