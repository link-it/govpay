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

import it.govpay.model.Stampa;
import it.govpay.model.Stampa.TIPO;
import it.govpay.orm.IdDocumento;
import it.govpay.orm.IdVersamento;

public class StampaConverter {

	public static Stampa toDTO(it.govpay.orm.Stampa vo) {
		Stampa dto = new Stampa();
		
		dto.setId(vo.getId());
		if(vo.getIdVersamento() != null)
			dto.setIdVersamento(vo.getIdVersamento().getId());
		if(vo.getIdDocumento() != null)
			dto.setIdDocumento(vo.getIdDocumento().getId());
		dto.setDataCreazione(vo.getDataCreazione());
		dto.setTipo(TIPO.valueOf(vo.getTipo()));
		dto.setPdf(vo.getPdf());

		return dto;
	}
	
	
	public static it.govpay.orm.Stampa toVO(Stampa dto) {
		it.govpay.orm.Stampa vo = new it.govpay.orm.Stampa();
		
		vo.setId(dto.getId());
		
		if(dto.getIdVersamento() != null && dto.getIdVersamento() > 0) {
			IdVersamento idVersamento = new IdVersamento();
			idVersamento.setId(dto.getIdVersamento());
			vo.setIdVersamento(idVersamento);
		}
		if(dto.getIdDocumento() != null && dto.getIdDocumento() > 0) {
			IdDocumento idDocumento = new IdDocumento();
			idDocumento.setId(dto.getIdDocumento());
			vo.setIdDocumento(idDocumento);
		}
		vo.setDataCreazione(dto.getDataCreazione());
		vo.setTipo(dto.getTipo().name());
		vo.setPdf(dto.getPdf());
		
		return vo;
	}
}
