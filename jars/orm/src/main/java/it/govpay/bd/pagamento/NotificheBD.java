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
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.converter.NotificaConverter;
import it.govpay.bd.pagamento.filters.NotificaFilter;
import it.govpay.model.Notifica.StatoSpedizione;
import it.govpay.orm.dao.jdbc.JDBCNotificaService;

public class NotificheBD extends BasicBD {

	public NotificheBD(BasicBD basicBD) {
		super(basicBD);
	}

	public Notifica insertNotifica(Notifica dto) throws ServiceException {
		it.govpay.orm.Notifica vo = NotificaConverter.toVO(dto);
		try {
			this.getNotificaService().create(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
		dto.setId(vo.getId());
		return dto;
	}

	public List<Notifica> findNotificheDaSpedire(Integer offset, Integer limit, String codApplicazione) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getNotificaService().newPaginatedExpression();
			exp.lessThan(it.govpay.orm.Notifica.model().DATA_PROSSIMA_SPEDIZIONE, new Date());
			exp.equals(it.govpay.orm.Notifica.model().STATO, Notifica.StatoSpedizione.DA_SPEDIRE.toString());
			
			if(offset != null) {
				exp.offset(offset);
			}
			
			if(limit != null) {
				exp.limit(limit);
			}
			
			if(codApplicazione != null) {
				exp.equals(it.govpay.orm.Notifica.model().ID_APPLICAZIONE.COD_APPLICAZIONE, codApplicazione);
			}
			
			exp.addOrder(it.govpay.orm.Notifica.model().DATA_PROSSIMA_SPEDIZIONE, SortOrder.DESC);
			
			List<it.govpay.orm.Notifica> findAll = this.getNotificaService().findAll(exp);
			return NotificaConverter.toDTOList(findAll);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<String> findApplicazioniConNotificheDaSpedire() throws ServiceException {
		List<String> lstApplicazioni = new ArrayList<String>();
		try {
			IPaginatedExpression exp = this.getNotificaService().newPaginatedExpression();
			exp.lessThan(it.govpay.orm.Notifica.model().DATA_PROSSIMA_SPEDIZIONE, new Date());
			exp.equals(it.govpay.orm.Notifica.model().STATO, Notifica.StatoSpedizione.DA_SPEDIRE.toString());
			
			List<Object> findAll = this.getNotificaService().select(exp, true, it.govpay.orm.Notifica.model().ID_APPLICAZIONE.COD_APPLICAZIONE);
			
			for (Object object : findAll) {
				if(object instanceof String) {
					lstApplicazioni.add((String) object);
				}
			}
		} catch(NotImplementedException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) { }
		return lstApplicazioni;
	}

	public long countNotificheDaSpedire() throws ServiceException {
		try {
			IExpression exp = this.getNotificaService().newExpression();
			exp.lessThan(it.govpay.orm.Notifica.model().DATA_PROSSIMA_SPEDIZIONE, new Date());
			exp.equals(it.govpay.orm.Notifica.model().STATO, Notifica.StatoSpedizione.DA_SPEDIRE.toString());
			return this.getNotificaService().count(exp).longValue();
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
			IExpression exp = this.getNotificaService().newExpression();
			exp.equals(it.govpay.orm.Notifica.model().STATO, Notifica.StatoSpedizione.DA_SPEDIRE.toString());
			return this.getNotificaService().count(exp).longValue();
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	public void updateSpedito(long id) throws ServiceException {
		this.update(id,  StatoSpedizione.SPEDITO, null, null, null);
	}

	public void updateDaSpedire(Long id, String message, long tentativi, Date prossima) throws ServiceException {
		// Non aggiorno il campo a DA_SPEDIRE. Se lo e' gia' tutto bene, se per concorrenza e' a spedito, non voglio sovrascriverlo. 
		this.update(id, null, message, tentativi, prossima);
	}
	
	public void updateAnnullata(Long id, String message, long tentativi, Date prossima) throws ServiceException {
		this.update(id, StatoSpedizione.ANNULLATA, message, tentativi, prossima);
	}

	private void update(long id, StatoSpedizione stato, String descrizione, Long tentativi, Date prossimaSpedizione) throws ServiceException {
		try {
//			IdNotifica idVO = ((JDBCNotificaServiceSearch)this.getNotificaService()).findId(id, true);
			List<UpdateField> lstUpdateFields = new ArrayList<>();
			if(stato != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Notifica.model().STATO, stato.toString()));
			if(descrizione != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Notifica.model().DESCRIZIONE_STATO, descrizione));
			if(tentativi != null)
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Notifica.model().TENTATIVI_SPEDIZIONE, tentativi));
			if(prossimaSpedizione != null) 
				lstUpdateFields.add(new UpdateField(it.govpay.orm.Notifica.model().DATA_PROSSIMA_SPEDIZIONE, prossimaSpedizione));
			lstUpdateFields.add(new UpdateField(it.govpay.orm.Notifica.model().DATA_AGGIORNAMENTO_STATO, new Date()));

			((JDBCNotificaService)this.getNotificaService()).updateFields(id, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
	
	public NotificaFilter newFilter() throws ServiceException {
		return new NotificaFilter(this.getNotificaService());
	}

	public NotificaFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new NotificaFilter(this.getNotificaService(),simpleSearch);
	}

	public long count(NotificaFilter filter) throws ServiceException {
		try {
			return this.getNotificaService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<Notifica> findAll(NotificaFilter filter) throws ServiceException {
		try {
			List<Notifica> notificaLst = new ArrayList<>();
			List<it.govpay.orm.Notifica> notificaVOLst = this.getNotificaService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.Notifica notificaVO: notificaVOLst) {
				notificaLst.add(NotificaConverter.toDTO(notificaVO));
			}
			return notificaLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
}
