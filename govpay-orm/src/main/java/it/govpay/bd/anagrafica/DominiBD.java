/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.converter.DominioConverter;
import it.govpay.bd.model.converter.IbanAccreditoConverter;
import it.govpay.orm.IdDominio;
import it.govpay.orm.dao.jdbc.JDBCDominioServiceSearch;
import it.govpay.orm.dao.jdbc.converter.IbanAccreditoFieldConverter;

import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;

public class DominiBD extends BasicBD {

	public DominiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera il Dominio tramite il codDominio
	 * 
	 * @param codDominio
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Dominio getDominio(String codDominio) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			IdDominio id = new IdDominio();
			id.setCodDominio(codDominio);
			
			it.govpay.orm.Dominio dominioVO = this.getDominioService().get(id);
			Dominio dominio = DominioConverter.toDTO(dominioVO);

			return dominio;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Recupera il Dominio tramite id
	 * 
	 * @param idDominio
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Dominio getDominio(Long idDominio) throws NotFoundException, MultipleResultException, ServiceException {
		if(idDominio == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}
		
		long id = idDominio.longValue();
		
		try {
			it.govpay.orm.Dominio dominioVO = ((JDBCDominioServiceSearch)this.getDominioService()).get(id);
			Dominio dominio = DominioConverter.toDTO(dominioVO);

			return dominio;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Inserisce il dominio con i dati forniti
	 * @param dominio
	 * @throws NotPermittedException se si inserisce un Dominio gia' censito
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertDominio(Dominio dominio) throws ServiceException{
		try {
			it.govpay.orm.Dominio vo = DominioConverter.toVO(dominio);
			
			this.getDominioService().create(vo);
			dominio.setId(vo.getId());
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Aggiorna il dominio con i dati forniti
	 * @param dominio
	 * @throws NotPermittedException se si inserisce un Dominio gia' censito
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateDominio(Dominio dominio) throws NotFoundException, ServiceException{
		try {
			it.govpay.orm.Dominio vo = DominioConverter.toVO(dominio);
			IdDominio id = this.getDominioService().convertToId(vo);
			
			if(!this.getDominioService().exists(id)) {
				throw new NotFoundException("Dominio con id ["+id+"] non esiste.");
			}
			
			this.getDominioService().update(id, vo);
			dominio.setId(vo.getId());
			AnagraficaManager.removeFromCache(dominio);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

	}
	
	/**
	 * Recupera gli ibanAccredito per un idDominio (join tra dominio, ente, tributo, ibanAccredito)
	 * 
	 * @param idIbanAccredito
	 * @return
	 * @throws NotFoundException se non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public List<IbanAccredito> getIbanAccreditoByIdDominio(long idDominio) throws ServiceException {
		try {
			IPaginatedExpression exp = this.getIbanAccreditoService().newPaginatedExpression();
			IbanAccreditoFieldConverter converter = new IbanAccreditoFieldConverter(this.getJdbcProperties().getDatabase());
			exp.equals(new CustomField("id_dominio",  Long.class, "id_dominio", converter.toTable(it.govpay.orm.IbanAccredito.model())), idDominio);
			List<it.govpay.orm.IbanAccredito> lstIban = this.getIbanAccreditoService().findAll(exp);
			
			return IbanAccreditoConverter.toDTOList(lstIban);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}

	}

	
	public DominioFilter newFilter() throws ServiceException {
		return new DominioFilter(this.getDominioService());
	}

	public long count(IFilter filter) throws ServiceException {
		try {
			return this.getDominioService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Dominio> findAll(IFilter filter) throws ServiceException {
		try {
			return DominioConverter.toDTOList(this.getDominioService().findAll(filter.toPaginatedExpression()));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Ritorna la lista di tutti i domini censiti
	 * @return
	 * @throws ServiceException 
	 */
	public List<Dominio> getDomini() throws ServiceException {
		return findAll(newFilter());
	}

}
