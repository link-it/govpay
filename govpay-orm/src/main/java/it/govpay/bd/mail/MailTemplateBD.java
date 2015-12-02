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
import it.govpay.bd.model.MailTemplate;
import it.govpay.bd.model.converter.MailTemplateConverter;
import it.govpay.orm.IdMailTemplate;
import it.govpay.orm.dao.jdbc.JDBCMailTemplateServiceSearch;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;

public class MailTemplateBD extends BasicBD {

	public MailTemplateBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera l'MailTemplate identificato dalla chiave fisica
	 * 
	 * @param idMailTemplate
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public MailTemplate getMailTemplate(Long idMailTemplate) throws NotFoundException, MultipleResultException, ServiceException {
		if(idMailTemplate== null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}
		
		long id = idMailTemplate.longValue();

		try {
			return MailTemplateConverter.toDTO(((JDBCMailTemplateServiceSearch)this.getServiceManager().getMailTemplateServiceSearch()).get(id));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Inserisce una nuova mailTemplate
	 * 
	 * @param codMsgRicevuta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public void insertMailTemplate(MailTemplate mailTemplate) throws ServiceException {
		try {
			
			it.govpay.orm.MailTemplate vo = MailTemplateConverter.toVO(mailTemplate);
			
			this.getServiceManager().getMailTemplateService().create(vo);
			mailTemplate.setId(vo.getId());
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Aggiorna lo stato di una mailTemplate
	 * 
	 * @param codMsgRicevuta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public void updateMailTemplate(MailTemplate mailTemplate) throws ServiceException, NotFoundException {
		try {
			
			IdMailTemplate idMailTemplate = new IdMailTemplate();
			idMailTemplate.setIdMailTemplate(mailTemplate.getId());
			
			if(!this.getServiceManager().getMailTemplateServiceSearch().exists(idMailTemplate)) {
				throw new NotFoundException("MailTemplate con id ["+idMailTemplate.toJson()+"] non trovata, impossibile aggiornarla.");
			}
			
			this.getServiceManager().getMailTemplateService().update(idMailTemplate, MailTemplateConverter.toVO(mailTemplate));
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

	}
	
}
