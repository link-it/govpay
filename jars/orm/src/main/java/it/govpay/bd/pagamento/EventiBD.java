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
import it.govpay.bd.pagamento.filters.EventiFilter.VISTA;
import it.govpay.orm.dao.jdbc.JDBCEventoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.EventoFieldConverter;
import it.govpay.orm.dao.jdbc.converter.VistaEventiVersamentoFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.EventoFetch;
import it.govpay.orm.model.EventoModel;

public class EventiBD extends BasicBD {

	private VISTA vista;
	
	public EventiBD(BasicBD basicBD) {
		this(basicBD, null);
	}

	public EventiBD(BasicBD basicBD, VISTA vista) {
		super(basicBD);
		this.vista = vista;
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
		if(this.vista == null)
			return new EventiFilter(this.getEventoService(), this.vista);

		switch (this.vista) {
		case PAGAMENTI:
		case RPT:
			return new EventiFilter(this.getEventoService(), this.vista);
		case VERSAMENTI:
			return  new EventiFilter(this.getVistaEventiVersamentoService(), this.vista);
		}

		return new EventiFilter(this.getEventoService(), this.vista);
	}

	public EventiFilter newFilter(boolean simpleSearch) throws ServiceException {
		if(this.vista == null)
			return new EventiFilter(this.getEventoService(),simpleSearch, this.vista);

		switch (this.vista) {
		case PAGAMENTI:
		case RPT:
			return new EventiFilter(this.getEventoService(),simpleSearch, this.vista);
		case VERSAMENTI:
			return  new EventiFilter(this.getVistaEventiVersamentoService(),simpleSearch, this.vista);
		}

		return new EventiFilter(this.getEventoService(),simpleSearch, this.vista);
	}

	public long count(EventiFilter filter) throws ServiceException {
		try {
			if(this.vista == null)
				return this.getEventoService().count(filter.toExpression()).longValue();

			switch (this.vista) {
			case PAGAMENTI:
			case RPT:
				return this.getEventoService().count(filter.toExpression()).longValue();
			case VERSAMENTI:
				return this.getVistaEventiVersamentoService().count(filter.toExpression()).longValue();
			}

			return 0;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Evento> findAll(EventiFilter filter) throws ServiceException {
		try {
			List<Evento> eventoLst = new ArrayList<>();

			List<it.govpay.orm.Evento> eventoVOLst = new ArrayList<>();

			if(this.vista == null) {
				eventoVOLst = this.getEventoService().findAll(filter.toPaginatedExpression());
			}else {
				switch (this.vista) {
				case PAGAMENTI:
				case RPT:
					eventoVOLst = this.getEventoService().findAll(filter.toPaginatedExpression());
					break;
				case VERSAMENTI:
					eventoVOLst =  this.getVistaEventiVersamentoService().findAll(filter.toPaginatedExpression());
					break;
				}
			}

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

			String tableName = getTableName(it.govpay.orm.Evento.model());

			List<IField> fields = new ArrayList<>();

			fields.add(new CustomField("id", Long.class, "id", tableName));
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

			List<Map<String, Object>> select = new ArrayList<Map<String,Object>>();

			if(this.vista == null) {
				select = this.getEventoService().select(filter.toPaginatedExpression(), fields.toArray(new IField[fields.size()]));
			} else {
				switch (this.vista) {
				case PAGAMENTI:
				case RPT:
					select = this.getEventoService().select(filter.toPaginatedExpression(), fields.toArray(new IField[fields.size()]));
					break;
				case VERSAMENTI:
					select =  this.getVistaEventiVersamentoService().select(filter.toPaginatedExpression(), fields.toArray(new IField[fields.size()]));
					break;
				}
			}

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

	private String getTableName(EventoModel model) throws ServiceException, ExpressionException {
		String tableName = null;

		if(this.vista == null) {
			EventoFieldConverter eventoFieldConverter = new EventoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			tableName = eventoFieldConverter.toTable(model);
		} else {
			switch (this.vista) {
			case PAGAMENTI:
			case RPT:
				EventoFieldConverter eventoFieldConverter = new EventoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
				tableName = eventoFieldConverter.toTable(model);
				break;
			case VERSAMENTI:
				VistaEventiVersamentoFieldConverter vistaEventiVersamentoFieldConverter = new VistaEventiVersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
				tableName = vistaEventiVersamentoFieldConverter.toTable(model);
				break;
			}
		}
		return tableName;
	}
}
