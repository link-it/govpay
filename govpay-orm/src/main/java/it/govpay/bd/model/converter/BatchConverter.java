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

import it.govpay.model.Batch;

import java.util.ArrayList;
import java.util.List;

public class BatchConverter {

	public static it.govpay.model.Batch toDTO(it.govpay.orm.Batch vo) {
		Batch dto = new Batch();
		dto.setCodBatch(vo.getCodBatch());
		dto.setNodo(vo.getNodo());
		dto.setInizio(vo.getInizio());
		dto.setAggiornamento(vo.getAggiornamento());

		return dto;
	}

	public static List<it.govpay.orm.Batch> toVO(List<Batch> lstDTO) {
		List<it.govpay.orm.Batch> lst = new ArrayList<>();
		if(lstDTO != null && !lstDTO.isEmpty()) {
			for(Batch vo: lstDTO) {
				lst.add(toVO(vo));
			}
		}

		return lst;
	}

	public static List<Batch> toDTO(List<it.govpay.orm.Batch> lstDTO) {
		List<Batch> lst = new ArrayList<>();
		if(lstDTO != null && !lstDTO.isEmpty()) {
			for(it.govpay.orm.Batch vo: lstDTO) {
				lst.add(toDTO(vo));
			}
		}

		return lst;
	}

	public static it.govpay.orm.Batch toVO(Batch dto) {
		it.govpay.orm.Batch vo = new it.govpay.orm.Batch();
		vo.setCodBatch(dto.getCodBatch());
		vo.setNodo(dto.getNodo());
		vo.setInizio(dto.getInizio());
		vo.setAggiornamento(dto.getAggiornamento());

		return vo;
	}


}
