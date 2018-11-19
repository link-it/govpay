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

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.Evento;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.Evento.CategoriaEvento;
import it.govpay.model.Evento.TipoEvento;

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
	
	
	
	public static Evento toDTO(it.govpay.orm.Evento vo) throws ServiceException{
		Evento dto = new Evento();
		if(vo.getCategoriaEvento() != null)
		dto.setCategoriaEvento(CategoriaEvento.toEnum(vo.getCategoriaEvento()));
		dto.setCcp(vo.getCcp());
		dto.setCodCanale(vo.getCodCanale());
		dto.setCodDominio(vo.getCodDominio());
		dto.setCodPsp(vo.getCodPsp());
		dto.setCodStazione(vo.getCodStazione());
		dto.setComponente(vo.getComponente());
		dto.setDataRichiesta(vo.getData1());
		dto.setDataRisposta(vo.getData2());
		dto.setErogatore(vo.getErogatore());
		dto.setEsito(vo.getEsito());
		dto.setFruitore(vo.getFruitore());
		dto.setId(vo.getId());
		dto.setIuv(vo.getIuv());
		dto.setAltriParametriRichiesta(vo.getParametri1());
		dto.setAltriParametriRisposta(vo.getParametri2());
		if(vo.getTipoEvento() != null)
			dto.setTipoEvento(TipoEvento.valueOf(vo.getTipoEvento()));
		if(vo.getTipoVersamento() != null)
			dto.setTipoVersamento(TipoVersamento.toEnum(vo.getTipoVersamento()));
		return dto;
	}

}
