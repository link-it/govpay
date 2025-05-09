/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

import it.govpay.bd.model.Allegato;
import it.govpay.orm.IdVersamento;

public class AllegatoConverter {
	
	private AllegatoConverter(){}

	public static Allegato toDTO(it.govpay.orm.Allegato vo) {
		Allegato dto = new Allegato();
		dto.setId(vo.getId());
		dto.setDataCreazione(vo.getDataCreazione());
		dto.setNome(vo.getNome());
		dto.setTipo(vo.getTipo());
		if(vo.getIdVersamento() != null) {
			dto.setIdVersamento(vo.getIdVersamento().getId());
		}
		dto.setRawContenuto(vo.getRawContenuto());
		dto.setDescrizione(vo.getDescrizione());

		return dto;
	}

	public static it.govpay.orm.Allegato toVO(Allegato dto)  {
		it.govpay.orm.Allegato vo = new it.govpay.orm.Allegato();
		vo.setId(dto.getId());
		
		vo.setDataCreazione(dto.getDataCreazione());
		vo.setNome(dto.getNome());
		vo.setTipo(dto.getTipo());
		if(dto.getIdVersamento() != null) {
			IdVersamento idVersamento = new IdVersamento();
			idVersamento.setId(dto.getIdVersamento());
			vo.setIdVersamento(idVersamento);
		}
		vo.setRawContenuto(dto.getRawContenuto());
		vo.setDescrizione(dto.getDescrizione());
		
		return vo;
	}
}
