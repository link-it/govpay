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
import it.govpay.bd.anagrafica.filters.PortaleFilter;
import it.govpay.bd.model.Portale;
import it.govpay.bd.model.converter.PortaleConverter;
import it.govpay.orm.IdPortale;
import it.govpay.orm.dao.jdbc.JDBCPortaleServiceSearch;

import java.util.List;

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.utils.UtilsException;

public class PortaliBD extends BasicBD {

	public PortaliBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera l'portale tramite l'id fisico
	 * 
	 * @param idPortale
	 * @return
	 * @throws NotFoundException se non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Portale getPortale(Long idPortale) throws NotFoundException, ServiceException, MultipleResultException {
		if(idPortale== null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}
		
		long id = idPortale.longValue();


		try {
			it.govpay.orm.Portale portaleVO = ((JDBCPortaleServiceSearch)this.getServiceManager().getPortaleServiceSearch()).get(id);
			Portale ente = PortaleConverter.toDTO(portaleVO);
			return ente;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Recupera l'portale tramite l'id logico
	 * 
	 * @param codPortale
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Portale getPortale(String codPortale) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IdPortale id = new IdPortale();
			id.setCodPortale(codPortale);
			it.govpay.orm.Portale portaleVO = this.getServiceManager().getPortaleServiceSearch().get(id);
			Portale ente = PortaleConverter.toDTO(portaleVO);
			return ente;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	
	
	/**
	 * Recupera l'portale identificata dal Principal fornito
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Portale getPortaleByPrincipal(String principal) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IExpression exp = this.getServiceManager().getPortaleServiceSearch().newExpression();
			exp.equals(it.govpay.orm.Portale.model().PRINCIPAL, principal);
			it.govpay.orm.Portale portaleVO = this.getServiceManager().getPortaleServiceSearch().find(exp);
			Portale ente = PortaleConverter.toDTO(portaleVO);
			return ente;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Aggiorna l'portale con i dati forniti
	 * @param portale
	 * @throws NotFoundException se non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updatePortale(Portale portale) throws NotFoundException, ServiceException {
		try {
			
			it.govpay.orm.Portale vo = PortaleConverter.toVO(portale);
			IdPortale idPortale = this.getServiceManager().getPortaleServiceSearch().convertToId(vo);

			if(!this.getServiceManager().getPortaleServiceSearch().exists(idPortale)) {
				throw new NotFoundException("Portale con id ["+idPortale.toJson()+"] non trovato");
			}

			this.getServiceManager().getPortaleService().update(idPortale, vo);
			portale.setId(vo.getId());
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Crea una nuova portale con i dati forniti
	 * @param portale
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertPortale(Portale portale) throws ServiceException{
		try {
			it.govpay.orm.Portale vo = PortaleConverter.toVO(portale);
			
			this.getServiceManager().getPortaleService().create(vo);
			portale.setId(vo.getId());

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Recupera tutti i portali censiti.
	 * @return
	 * @throws ServiceException 
	 */
	public List<Portale> getPortali() throws ServiceException {
		return this.findAll(this.newFilter());
	}
	
	public PortaleFilter newFilter() throws ServiceException {
		try {
			return new PortaleFilter(this.getServiceManager().getPortaleServiceSearch());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public long count(PortaleFilter filter) throws ServiceException {
		try {
			return this.getServiceManager().getPortaleServiceSearch().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Portale> findAll(PortaleFilter filter) throws ServiceException {
		try {
			return PortaleConverter.toDTOList(this.getServiceManager().getPortaleServiceSearch().findAll(filter.toPaginatedExpression()));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}


}
