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
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.NotificaAppIo;
import it.govpay.bd.model.converter.NotificaAppIoConverter;
import it.govpay.bd.pagamento.filters.NotificaAppIoFilter;
import it.govpay.model.NotificaAppIo.StatoMessaggio;
import it.govpay.model.NotificaAppIo.StatoSpedizione;
import it.govpay.orm.dao.jdbc.JDBCNotificaAppIOService;

public class NotificheAppIoBD extends BasicBD {

	public NotificheAppIoBD(BasicBD basicBD) {
		super(basicBD);
	}

	public NotificaAppIo insertNotifica(NotificaAppIo dto) throws ServiceException {
		it.govpay.orm.NotificaAppIO vo = NotificaAppIoConverter.toVO(dto);
		try {
			this.getNotificaAppIOService().create(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
		dto.setId(vo.getId());
		return dto;
	}

	public List<NotificaAppIo> findNotificheDaSpedire(Integer offset, Integer limit) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getNotificaAppIOService().newPaginatedExpression();
			exp.lessThan(it.govpay.orm.NotificaAppIO.model().DATA_PROSSIMA_SPEDIZIONE, new Date());
			exp.equals(it.govpay.orm.NotificaAppIO.model().STATO, NotificaAppIo.StatoSpedizione.DA_SPEDIRE.toString());
			
			if(offset != null) {
				exp.offset(offset);
			}
			
			if(limit != null) {
				exp.limit(limit);
			}
			
			exp.addOrder(it.govpay.orm.NotificaAppIO.model().DATA_PROSSIMA_SPEDIZIONE, SortOrder.DESC);
			
			List<it.govpay.orm.NotificaAppIO> findAll = this.getNotificaAppIOService().findAll(exp);
			return NotificaAppIoConverter.toDTOList(findAll);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	public long countNotificheDaSpedire() throws ServiceException {
		try {
			IExpression exp = this.getNotificaAppIOService().newExpression();
			exp.lessThan(it.govpay.orm.NotificaAppIO.model().DATA_PROSSIMA_SPEDIZIONE, new Date());
			exp.equals(it.govpay.orm.NotificaAppIO.model().STATO, NotificaAppIo.StatoSpedizione.DA_SPEDIRE.toString());
			return this.getNotificaAppIOService().count(exp).longValue();
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	public long countNotificheInAttesa() throws ServiceException {
		try {
			IExpression exp = this.getNotificaAppIOService().newExpression();
			exp.equals(it.govpay.orm.NotificaAppIO.model().STATO, NotificaAppIo.StatoSpedizione.DA_SPEDIRE.toString());
			return this.getNotificaAppIOService().count(exp).longValue();
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	public void updateSpedito(long id, String idMessaggio, StatoMessaggio statoMessaggio) throws ServiceException {
		this.update(id,  StatoSpedizione.SPEDITO, null, null, null, idMessaggio, statoMessaggio);
	}

	public void updateDaSpedire(Long id, String message, long tentativi, Date prossima) throws ServiceException {
		// Non aggiorno il campo a DA_SPEDIRE. Se lo e' gia' tutto bene, se per concorrenza e' a spedito, non voglio sovrascriverlo. 
		this.update(id, null, message, tentativi, prossima, null, null);
	}
	
	public void updateAnnullata(Long id, String message, long tentativi, Date prossima) throws ServiceException {
		this.update(id, StatoSpedizione.ANNULLATA, message, tentativi, prossima, null, null);
	}

	private void update(long id, StatoSpedizione stato, String descrizione, Long tentativi, Date prossimaSpedizione, String idMessaggio, StatoMessaggio statoMessaggio) throws ServiceException {
		try {
//			IdNotifica idVO = ((JDBCNotificaServiceSearch)this.getNotificaService()).findId(id, true);
			List<UpdateField> lstUpdateFields = new ArrayList<>();
			if(stato != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.NotificaAppIO.model().STATO, stato.toString()));
			if(descrizione != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.NotificaAppIO.model().DESCRIZIONE_STATO, descrizione));
			if(tentativi != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.NotificaAppIO.model().TENTATIVI_SPEDIZIONE, tentativi));
			if(prossimaSpedizione != null) 
				lstUpdateFields.add(new UpdateField(it.govpay.orm.NotificaAppIO.model().DATA_PROSSIMA_SPEDIZIONE, prossimaSpedizione));
			lstUpdateFields.add(new UpdateField(it.govpay.orm.NotificaAppIO.model().DATA_AGGIORNAMENTO_STATO, new Date()));
			
			// dati ricevuti da AppIO
			if(idMessaggio != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.NotificaAppIO.model().ID_MESSAGGIO, idMessaggio));
			if(statoMessaggio != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.NotificaAppIO.model().STATO_MESSAGGIO, statoMessaggio.toString()));

			((JDBCNotificaAppIOService)this.getNotificaAppIOService()).updateFields(id, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
	
	public NotificaAppIoFilter newFilter() throws ServiceException {
		return new NotificaAppIoFilter(this.getNotificaAppIOService());
	}

	public NotificaAppIoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new NotificaAppIoFilter(this.getNotificaAppIOService(),simpleSearch);
	}

	public long count(NotificaAppIoFilter filter) throws ServiceException {
		try {
			return this.getNotificaAppIOService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<NotificaAppIo> findAll(NotificaAppIoFilter filter) throws ServiceException {
		try {
			List<NotificaAppIo> notificaLst = new ArrayList<>();
			List<it.govpay.orm.NotificaAppIO> notificaVOLst = this.getNotificaAppIOService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.NotificaAppIO notificaVO: notificaVOLst) {
				notificaLst.add(NotificaAppIoConverter.toDTO(notificaVO));
			}
			return notificaLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
}
