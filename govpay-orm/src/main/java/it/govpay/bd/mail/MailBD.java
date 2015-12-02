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
package it.govpay.bd.mail;

import it.govpay.bd.BasicBD;
import it.govpay.bd.IFilter;
import it.govpay.bd.model.Mail;
import it.govpay.bd.model.converter.MailConverter;
import it.govpay.orm.IdMail;
import it.govpay.orm.dao.jdbc.JDBCMailServiceSearch;

import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;

public class MailBD extends BasicBD {

	public MailBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera l'Mail identificato dalla chiave fisica
	 * 
	 * @param idMail
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Mail getMail(long idMail) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			return MailConverter.toDTO(((JDBCMailServiceSearch)this.getServiceManager().getMailServiceSearch()).get(idMail));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Inserisce una nuova mail
	 * 
	 * @param codMsgRicevuta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public void insertMail(Mail mail) throws ServiceException {
		try {
			
			it.govpay.orm.Mail vo = MailConverter.toVO(mail);
			
			this.getServiceManager().getMailService().create(vo);
			mail.setId(vo.getId());
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Aggiorna lo stato di una mail
	 * 
	 * @param codMsgRicevuta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public void updateMail(Mail mail) throws ServiceException, NotFoundException {
		try {
			
			IdMail idMail = new IdMail();
			idMail.setIdMail(mail.getId());
			
			if(!this.getServiceManager().getMailServiceSearch().exists(idMail)) {
				throw new NotFoundException("Mail con id ["+idMail.toJson()+"] non trovata, impossibile aggiornarla.");
			}
			
			this.getServiceManager().getMailService().update(idMail, MailConverter.toVO(mail));
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

	}
	
	public IFilter newFilter() throws ServiceException {
		try {
			return new MailFilter(this.getServiceManager().getMailServiceSearch());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public long count(IFilter filter) throws ServiceException {
		try {
			return this.getServiceManager().getMailServiceSearch().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Mail> findAll(IFilter filter) throws ServiceException {
		try {
			return MailConverter.toDTOList(this.getServiceManager().getMailServiceSearch().findAll(filter.toPaginatedExpression()));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

}
