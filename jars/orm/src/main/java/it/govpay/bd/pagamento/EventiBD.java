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
import java.util.Map;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.converter.EventoConverter;
import it.govpay.bd.pagamento.filters.EventiFilter;
import it.govpay.orm.dao.jdbc.JDBCEventoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.EventoFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.EventoFetch;

public class EventiBD extends BasicBD {

	public EventiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public Evento getEvento(long id) throws ServiceException, NotFoundException {
		try {
			it.govpay.orm.Evento vo = ((JDBCEventoServiceSearch)this.getEventoService()).get(id);
			return EventoConverter.toDTO(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
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

	public long count(EventiFilter filter) throws ServiceException {
		try {
			return this.getEventoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Evento> findAll(EventiFilter filter) throws ServiceException {
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
	
	public List<Evento> findAllNoMessaggi(EventiFilter filter) throws ServiceException {
		
		try {
			List<Evento> eventoLst = new ArrayList<>();
			
			EventoFieldConverter eventoFieldConverter = new EventoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			
			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id", Long.class, "id", eventoFieldConverter.toTable(it.govpay.orm.Evento.model())));
			fields.add(it.govpay.orm.Evento.model().CATEGORIA_EVENTO);
			fields.add(it.govpay.orm.Evento.model().COMPONENTE);
			fields.add(it.govpay.orm.Evento.model().DATA);
			fields.add(it.govpay.orm.Evento.model().DATI_PAGO_PA);
			fields.add(it.govpay.orm.Evento.model().DETTAGLIO_ESITO);
			fields.add(it.govpay.orm.Evento.model().ESITO);
			fields.add(it.govpay.orm.Evento.model().INTERVALLO);
//			fields.add(it.govpay.orm.Evento.model().PARAMETRI_RICHIESTA);
//			fields.add(it.govpay.orm.Evento.model().PARAMETRI_RISPOSTA);
			fields.add(it.govpay.orm.Evento.model().RUOLO);
			fields.add(it.govpay.orm.Evento.model().SOTTOTIPO_ESITO);
			fields.add(it.govpay.orm.Evento.model().SOTTOTIPO_EVENTO);
			fields.add(it.govpay.orm.Evento.model().TIPO_EVENTO);
			fields.add(it.govpay.orm.Evento.model().COD_VERSAMENTO_ENTE);
			fields.add(it.govpay.orm.Evento.model().COD_APPLICAZIONE);
			fields.add(it.govpay.orm.Evento.model().IUV);
			fields.add(it.govpay.orm.Evento.model().CCP);
			fields.add(it.govpay.orm.Evento.model().COD_DOMINIO);
			fields.add(it.govpay.orm.Evento.model().ID_SESSIONE);
		
			
			List<Map<String, Object>> select = this.getEventoService().select(filter.toPaginatedExpression(), fields.toArray(new IField[fields.size()]));
			
			EventoFetch eventoFetch = new EventoFetch();
			
			for (Map<String, Object> map : select) {
				it.govpay.orm.Evento evento = (it.govpay.orm.Evento) eventoFetch.fetch(ConnectionManager.getJDBCServiceManagerProperties().getDatabase(), it.govpay.orm.Evento.model(), map);
				eventoLst.add(EventoConverter.toDTO(evento));
			}
			return eventoLst;
		} catch (NotFoundException e) {
			return new ArrayList<>();
		} catch (NotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		}
	}
}
