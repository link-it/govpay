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
import org.openspcoop2.utils.serialization.IOException;

import it.govpay.bd.model.Evento;
import it.govpay.model.Evento.CategoriaEvento;
import it.govpay.model.Evento.EsitoEvento;
import it.govpay.model.Evento.RuoloEvento;
import it.govpay.orm.IdPagamentoPortale;
import it.govpay.orm.IdRpt;
import it.govpay.orm.IdVersamento;

public class EventoConverter {

	public static it.govpay.orm.Evento toVO(Evento dto) {
		it.govpay.orm.Evento vo = new it.govpay.orm.Evento();
		if(dto.getCategoriaEvento() != null)
			vo.setCategoriaEvento(dto.getCategoriaEvento().getCodifica());
		vo.setComponente(dto.getComponente());
		vo.setData(dto.getData());
		try {
			vo.setDatiControparte(dto.getDettaglioAsString(dto.getControparte()));
		} catch (IOException e) {
		}
		if(dto.getDettaglioEsito() != null)
			vo.setDettaglioEsito(dto.getDettaglioEsito().length() < 255 ? dto.getDettaglioEsito() : dto.getDettaglioEsito().substring(0, 255));
		if(dto.getEsitoEvento() != null)
			vo.setEsito(dto.getEsitoEvento().toString());
		vo.setId(dto.getId());
		vo.setIntervallo(dto.getIntervallo() != null ? dto.getIntervallo() : 0l); 
		try {
			vo.setParametriRichiesta(dto.getDettaglioAsString(dto.getDettaglioRichiesta()));
		} catch (IOException e) {
		}
		try {
			vo.setParametriRisposta(dto.getDettaglioAsString(dto.getDettaglioRisposta()));
		} catch (IOException e) {
		}
		if(dto.getRuoloEvento() != null)
			vo.setRuolo(dto.getRuoloEvento().toString());
		vo.setSottotipoEsito(dto.getSottotipoEsito());
		vo.setSottotipoEvento(dto.getSottotipoEvento());
		vo.setTipoEvento(dto.getTipoEvento());
		if(dto.getIdVersamento() != null && dto.getIdVersamento() > 0) {
			IdVersamento idVersamento = new IdVersamento();
			idVersamento.setId(dto.getIdVersamento());
			vo.setIdVersamento(idVersamento );
		}
		
		if(dto.getIdPagamentoPortale() != null && dto.getIdPagamentoPortale() > 0) {
			IdPagamentoPortale idPagamentoPortale = new IdPagamentoPortale();
			idPagamentoPortale.setId(dto.getIdPagamentoPortale());
			vo.setIdPagamentoPortale(idPagamentoPortale);
		}
		
		if(dto.getIdRpt() != null && dto.getIdRpt() > 0) {
			IdRpt idRpt = new IdRpt();
			idRpt.setId(dto.getIdRpt());
			vo.setIdRpt(idRpt);
		}
		
		return vo;
	}
	
	public static Evento toDTO(it.govpay.orm.Evento vo) throws ServiceException{
		Evento dto = new Evento();
		if(vo.getCategoriaEvento() != null)
			dto.setCategoriaEvento(CategoriaEvento.toEnum(vo.getCategoriaEvento()));
		dto.setComponente(vo.getComponente());
		dto.setData(vo.getData());
		dto.setDatiControparte(vo.getDatiControparte());
		dto.setDettaglioEsito(vo.getDettaglioEsito());
		if(vo.getEsito() != null)
			dto.setEsitoEvento(EsitoEvento.toEnum(vo.getEsito()));
		dto.setId(vo.getId());
		dto.setIntervallo(vo.getIntervallo()); 
		dto.setParametriRichiesta(vo.getParametriRichiesta());
		dto.setParametriRisposta(vo.getParametriRisposta());
		if(vo.getRuolo() != null)
			dto.setRuoloEvento(RuoloEvento.toEnum(vo.getRuolo()));
		dto.setSottotipoEsito(vo.getSottotipoEsito());
		dto.setSottotipoEvento(vo.getSottotipoEvento());
		dto.setTipoEvento(vo.getTipoEvento());
		if(vo.getIdVersamento() != null)
			dto.setIdVersamento(vo.getIdVersamento().getId());
		
		if(vo.getIdPagamentoPortale() != null)
			dto.setIdPagamentoPortale(vo.getIdPagamentoPortale().getId());

		if(vo.getIdRpt() != null)
			dto.setIdRpt(vo.getIdRpt().getId());
		
		return dto;
	}
}
