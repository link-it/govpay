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
package it.govpay.bd.anagrafica;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.utils.UtilsException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.converter.UtenzaConverter;
import it.govpay.model.Utenza;
import it.govpay.orm.IdUtenza;
import it.govpay.orm.dao.jdbc.JDBCUtenzaServiceSearch;

public class UtenzeBD extends BasicBD {

	public UtenzeBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera l'utenza identificata dalla chiave fisica
	 * 
	 * @param idUtenza
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Utenza getUtenza(Long idUtenza) throws NotFoundException, MultipleResultException, ServiceException {

		if(idUtenza== null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		long id = idUtenza.longValue();


		try {
			it.govpay.orm.Utenza utenzaVO = ((JDBCUtenzaServiceSearch)this.getUtenzaService()).get(id);
			return getUtenza(utenzaVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Recupera l'utenza identificato dalla chiave logica
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Utenza getUtenza(String principal) throws NotFoundException, MultipleResultException, ServiceException {
		return getUtenza(principal, false);
	}
	
	/**
	 * Recupera l'utenza identificato dalla chiave logica
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Utenza getUtenza(String principal, boolean checkIgnoreCase) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IExpression expr = this.getUtenzaService().newExpression();
			if(checkIgnoreCase)
				expr.ilike(it.govpay.orm.Utenza.model().PRINCIPAL, principal, LikeMode.EXACT);
			else 
				expr.equals(it.govpay.orm.Utenza.model().PRINCIPAL, principal);
			
			it.govpay.orm.Utenza utenzaVO = this.getUtenzaService().find(expr);
			return getUtenza(utenzaVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}


	private Utenza getUtenza(it.govpay.orm.Utenza utenzaVO) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException {
		Utenza utenza = UtenzaConverter.toDTO(utenzaVO, this);
		return utenza;
	}

	/**
	 * Aggiorna il utenza
	 * 
	 * @param utenza
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void updateUtenza(Utenza utenza) throws NotFoundException, ServiceException {
		try {

			it.govpay.orm.Utenza vo = UtenzaConverter.toVO(utenza);
			IdUtenza idUtenza = this.getUtenzaService().convertToId(vo);
			if(!this.getUtenzaService().exists(idUtenza)) {
				throw new NotFoundException("Utenza con id ["+idUtenza.toJson()+"] non trovato");
			}
			this.getUtenzaService().update(idUtenza, vo);
			utenza.setId(vo.getId());
			emitAudit(utenza);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Crea un nuovo utenza.
	 * 
	 * @param ente
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public void insertUtenza(Utenza utenza) throws  ServiceException{
		try {
			it.govpay.orm.Utenza vo = UtenzaConverter.toVO(utenza);
			this.getUtenzaService().create(vo);
			utenza.setId(vo.getId());
			emitAudit(utenza);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

}
