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
package it.govpay.bd.pagamento;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.IFilter;
import it.govpay.bd.model.converter.EventoConverter;
import it.govpay.bd.pagamento.filters.EventiFilter;
import it.govpay.model.Evento;

public class EventiBD extends BasicBD {

	public EventiBD(BasicBD basicBD) {
		super(basicBD);
	}

	public Evento insertEvento(Evento dto) throws ServiceException {
		it.govpay.orm.Evento vo = EventoConverter.toVO(dto);
		try {
			this.getEventoService().create(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
		dto.setId(vo.getId());
		return dto;
	}
	
	public EventiFilter newFilter() throws ServiceException {
		return new EventiFilter(this.getEventoService());
	}
	
	public EventiFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new EventiFilter(this.getEventoService(),simpleSearch);
	}

	public long count(IFilter filter) throws ServiceException {
		try {
			return this.getEventoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Evento> findAll(IFilter filter) throws ServiceException {
		try {
			List<Evento> eventoLst = new ArrayList<>();
			List<it.govpay.orm.Evento> eventoVOLst = this.getEventoService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.Evento eventoVO: eventoVOLst) {
				eventoLst.add(EventoConverter.toDTO(eventoVO));
			}
			return eventoLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
}
