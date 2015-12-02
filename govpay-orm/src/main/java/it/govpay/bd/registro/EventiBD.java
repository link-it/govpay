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
package it.govpay.bd.registro;

import it.govpay.bd.BasicBD;
import it.govpay.bd.IFilter;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.converter.EventoConverter;
import it.govpay.orm.dao.jdbc.JDBCEventoServiceSearch;

import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class EventiBD extends BasicBD {

	public EventiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera l'Evento identificato dalla chiave fisica
	 * 
	 * @param idEvento
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Evento getEvento(long idEvento) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			return EventoConverter.toDTO(((JDBCEventoServiceSearch)this.getServiceManager().getEventoServiceSearch()).get(idEvento));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Inserisce un nuovo evento
	 * 
	 * @param codMsgRicevuta
	 * @return
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public void insertEvento(Evento evento) throws ServiceException {
		try {
			
			it.govpay.orm.Evento vo = EventoConverter.toVO(evento);
			
			this.getServiceManager().getEventoService().create(vo);
			evento.setId(vo.getId());
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
	public EventoFilter newFilter() throws ServiceException {
		try {
			return new EventoFilter(this.getServiceManager().getEventoServiceSearch());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public long count(IFilter filter) throws ServiceException {
		try {
			return this.getServiceManager().getEventoServiceSearch().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Evento> findAll(IFilter filter) throws ServiceException {
		try {
			return EventoConverter.toDTOList(this.getServiceManager().getEventoServiceSearch().findAll(filter.toPaginatedExpression()));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

}
