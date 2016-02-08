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
package it.govpay.bd.anagrafica;

import it.govpay.bd.BasicBD;
import it.govpay.bd.IFilter;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.converter.IbanAccreditoConverter;
import it.govpay.orm.IdIbanAccredito;
import it.govpay.orm.dao.jdbc.JDBCIbanAccreditoServiceSearch;

import java.util.List;

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;

public class IbanAccreditoBD extends BasicBD {

	public IbanAccreditoBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera l'ibanAccredito tramite l'id fisico
	 * 
	 * @param idIbanAccredito
	 * @return
	 * @throws NotFoundException se non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public IbanAccredito getIbanAccredito(Long idIbanAccredito) throws NotFoundException, ServiceException, MultipleResultException {
		if(idIbanAccredito == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}
		
		long id = idIbanAccredito.longValue();

		try {
			it.govpay.orm.IbanAccredito ibanAccreditoVO = ((JDBCIbanAccreditoServiceSearch)this.getIbanAccreditoService()).get(id);
			IbanAccredito ibanAccredito = getIbanAccredito(ibanAccreditoVO);
			
			return ibanAccredito;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Recupera l'ibanAccredito tramite l'id logico
	 * 
	 * @param idIbanAccredito
	 * @return
	 * @throws NotFoundException se non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public IbanAccredito getIbanAccredito(String codIban) throws NotFoundException, ServiceException, MultipleResultException {
		try {
			IdIbanAccredito id = new IdIbanAccredito();
			id.setCodIban(codIban);
			
			it.govpay.orm.IbanAccredito ibanAccreditoVO = this.getIbanAccreditoService().get(id);
			IbanAccredito ibanAccredito = getIbanAccredito(ibanAccreditoVO);
			
			return ibanAccredito;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}

	}
	
	private IbanAccredito getIbanAccredito(it.govpay.orm.IbanAccredito ibanAccreditoVO) throws ServiceException,
			NotImplementedException, ExpressionNotImplementedException,
			ExpressionException {
		return IbanAccreditoConverter.toDTO(ibanAccreditoVO);
	}
	
	/**
	 * Aggiorna l'ibanAccredito con i dati forniti
	 * @param ente
	 * @throws NotFoundException se non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateIbanAccredito(IbanAccredito ibanAccredito) throws NotFoundException, ServiceException {
		try {
			it.govpay.orm.IbanAccredito vo = IbanAccreditoConverter.toVO(ibanAccredito);
			IdIbanAccredito id = this.getIbanAccreditoService().convertToId(vo);
			
			if(!this.getIbanAccreditoService().exists(id)) {
				throw new NotFoundException("IbanAccredito con id ["+id+"] non esiste.");
			}
			
			this.getIbanAccreditoService().update(id, vo);
			ibanAccredito.setId(vo.getId());
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Crea una nuova ibanAccredito con i dati forniti
	 * @param ibanAccredito
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertIbanAccredito(IbanAccredito ibanAccredito) throws ServiceException{
		try {
			it.govpay.orm.IbanAccredito vo = IbanAccreditoConverter.toVO(ibanAccredito);
			this.getIbanAccreditoService().create(vo);
			ibanAccredito.setId(vo.getId());

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public IFilter newFilter() throws ServiceException {
		return new it.govpay.bd.AbstractFilter(this.getIbanAccreditoService()) {
			
			@Override
			public IExpression toExpression() throws ServiceException {
				try {
					return newExpression();
				} catch (NotImplementedException e) {
					throw new ServiceException(e);
				}
			}
		};
	}

	public long count(IFilter filter) throws ServiceException {
		try {
			return this.getIbanAccreditoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<IbanAccredito> findAll(IFilter filter) throws ServiceException {
		try {
			return IbanAccreditoConverter.toDTOList(this.getIbanAccreditoService().findAll(filter.toPaginatedExpression()));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}


	
}
