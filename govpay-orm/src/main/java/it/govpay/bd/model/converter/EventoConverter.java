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

import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Evento.CategoriaEvento;
import it.govpay.bd.model.Evento.TipoEvento;
import it.govpay.bd.model.Rpt.TipoVersamento;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class EventoConverter {

	public static List<Evento> toDTOList(List<it.govpay.orm.Evento> enteLst) throws ServiceException {
		List<Evento> lstDTO = new ArrayList<Evento>();
		if(enteLst != null && !enteLst.isEmpty()) {
			for(it.govpay.orm.Evento ente: enteLst){
				lstDTO.add(toDTO(ente));
			}
		}
		return lstDTO;
	}

	public static Evento toDTO(it.govpay.orm.Evento vo) throws ServiceException {
		Evento dto = new Evento();
		dto.setId(vo.getId()); 
		dto.setDataOraEvento(vo.getDataOraEvento());
		dto.setCodDominio(vo.getCodDominio());
		dto.setIuv(vo.getIuv());
		dto.setIdApplicazione(vo.getIdApplicazione());
		dto.setCcp(vo.getCcp());
		dto.setCodPsp(vo.getCodPsp());
		if(vo.getTipoVersamento() != null)
			dto.setTipoVersamento(TipoVersamento.toEnum(vo.getTipoVersamento()));
		
		dto.setComponente(vo.getComponente());
		
		if(vo.getCategoriaEvento() != null)
			dto.setCategoriaEvento(CategoriaEvento.valueOf(vo.getCategoriaEvento()));
		
		if(vo.getTipoEvento()!= null)
			dto.setTipoEvento(TipoEvento.valueOf(vo.getTipoEvento()));
		dto.setSottotipoEvento(vo.getSottotipoEvento());
		dto.setCodFruitore(vo.getCodFruitore());
		dto.setCodErogatore(vo.getCodErogatore());
		dto.setCodStazione(vo.getCodStazione());
		dto.setCanalePagamento(vo.getCanalePagamento());
		dto.setAltriParametri(vo.getAltriParametri());
		dto.setEsito(vo.getEsito());

		return dto;
	}

	public static it.govpay.orm.Evento toVO(Evento dto) {
		it.govpay.orm.Evento vo = new it.govpay.orm.Evento();
		vo.setDataOraEvento(dto.getDataOraEvento());
		vo.setCodDominio(dto.getCodDominio());
		vo.setIuv(dto.getIuv());
		vo.setIdApplicazione(dto.getIdApplicazione());
		
		vo.setCcp(dto.getCcp());
		vo.setCodPsp(dto.getCodPsp());
		if(dto.getTipoVersamento() != null)
			vo.setTipoVersamento(dto.getTipoVersamento().getCodifica());
		
		vo.setComponente(dto.getComponente());
		
		if(dto.getCategoriaEvento() != null)
			vo.setCategoriaEvento(dto.getCategoriaEvento().name());
		
		if(dto.getTipoEvento() != null)
			vo.setTipoEvento(dto.getTipoEvento().name());
		
		vo.setSottotipoEvento(dto.getSottotipoEvento());
		vo.setCodFruitore(dto.getCodFruitore());
		vo.setCodErogatore(dto.getCodErogatore());
		vo.setCodStazione(dto.getCodStazione());
		vo.setCanalePagamento(dto.getCanalePagamento());
		vo.setAltriParametri(dto.getAltriParametri());
		vo.setEsito(dto.getEsito());

		return vo;
	}

}
