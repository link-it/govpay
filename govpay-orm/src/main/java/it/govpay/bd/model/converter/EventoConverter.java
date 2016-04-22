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

import it.govpay.bd.model.Evento;

public class EventoConverter {

	public static it.govpay.orm.Evento toVO(Evento dto) {
		it.govpay.orm.Evento vo = new it.govpay.orm.Evento();
		vo.setCategoriaEvento(dto.getCategoriaEvento().getCodifica());
		vo.setCcp(dto.getCcp());
		vo.setCodCanale(dto.getCodCanale());
		vo.setCodDominio(dto.getCodDominio());
		vo.setCodPsp(dto.getCodPsp());
		vo.setCodStazione(dto.getCodStazione());
		vo.setComponente(dto.getComponente());
		vo.setData1(dto.getDataRichiesta());
		vo.setData2(dto.getDataRisposta());
		vo.setErogatore(dto.getErogatore());
		vo.setEsito(dto.getEsito());
		vo.setFruitore(dto.getFruitore());
		vo.setId(dto.getId());
		vo.setIuv(dto.getIuv());
		vo.setParametri1(dto.getAltriParametriRichiesta());
		vo.setParametri2(dto.getAltriParametriRisposta());
		vo.setTipoEvento(dto.getTipoEvento().toString());
		if(dto.getTipoVersamento() != null)
			vo.setTipoVersamento(dto.getTipoVersamento().getCodifica());
		return vo;
	}

}
