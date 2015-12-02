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
import it.govpay.bd.model.ContoAccredito;
import it.govpay.bd.model.converter.ContoAccreditoConverter;
import it.govpay.orm.IdContoAccredito;
import it.govpay.orm.dao.jdbc.JDBCContoAccreditoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.ContoAccreditoFieldConverter;

import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

public class ContiAccreditoBD extends BasicBD {

	public ContiAccreditoBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera il ContoAccredito tramite l'id fisico
	 * 
	 * @param idContoAccredito
	 * @return
	 * @throws NotFoundException se non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public ContoAccredito getContoAccredito(Long idContoAccredito) throws NotFoundException, ServiceException, MultipleResultException {
		
		if(idContoAccredito == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}
		
		long id = idContoAccredito.longValue();

		try {
			it.govpay.orm.ContoAccredito contoAccreditoVO = ((JDBCContoAccreditoServiceSearch)this.getServiceManager().getContoAccreditoServiceSearch()).get(id);
			ContoAccredito contoAccredito = getContoAccredito(contoAccreditoVO);
			
			return contoAccredito;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Recupera l'contoAccredito tramite l'id logico
	 * 
	 * @param codEnte
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public ContoAccredito getLastContoAccredito(long idDominio) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IPaginatedExpression exp = this.getServiceManager().getContoAccreditoServiceSearch().newPaginatedExpression();
			
			ContoAccreditoFieldConverter converter = new ContoAccreditoFieldConverter(this.getServiceManager().getJdbcProperties().getDatabase()); 
			
			exp.equals(new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(it.govpay.orm.ContoAccredito.model())));
			exp.addOrder(it.govpay.orm.ContoAccredito.model().DATA_ORA_PUBBLICAZIONE, SortOrder.DESC);
			exp.limit(1);
			List<it.govpay.orm.ContoAccredito> lstContoAccredito = this.getServiceManager().getContoAccreditoServiceSearch().findAll(exp);
			
			if(lstContoAccredito == null || lstContoAccredito.isEmpty()) {
				throw new NotFoundException("Nessuna ContoAccredito con idDominio ["+idDominio+"] trovata");
			}
			
			if(lstContoAccredito.size() != 1) {
				throw new MultipleResultException("Errore nella ricerca dell'ultima ContoAccredito con idDominio ["+idDominio+"]");
			}
			
			ContoAccredito contoAccredito = getContoAccredito(lstContoAccredito.get(0));
			
			return contoAccredito;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	private ContoAccredito getContoAccredito(it.govpay.orm.ContoAccredito contoAccreditoVO) throws ServiceException,
			NotImplementedException, ExpressionNotImplementedException,
			ExpressionException {
		return ContoAccreditoConverter.toDTO(contoAccreditoVO);
	}
	
	/**
	 * Aggiorna l'contoAccredito con i dati forniti
	 * @param ente
	 * @throws NotFoundException se non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateContoAccredito(ContoAccredito contoAccredito) throws NotFoundException, ServiceException {
		try {
			it.govpay.orm.ContoAccredito vo = ContoAccreditoConverter.toVO(contoAccredito);
			IdContoAccredito id = this.getServiceManager().getContoAccreditoServiceSearch().convertToId(vo);
			
			if(!this.getServiceManager().getContoAccreditoServiceSearch().exists(id)) {
				throw new NotFoundException("ContoAccredito con id ["+id+"] non esiste.");
			}
			
			this.getServiceManager().getContoAccreditoService().update(id, vo);
			contoAccredito.setId(vo.getId());
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Crea una nuova contoAccredito con i dati forniti
	 * @param contoAccredito
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertContoAccredito(ContoAccredito contoAccredito) throws ServiceException{
		try {
			it.govpay.orm.ContoAccredito vo = ContoAccreditoConverter.toVO(contoAccredito);
			this.getServiceManager().getContoAccreditoService().create(vo);
			contoAccredito.setId(vo.getId());

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
}
