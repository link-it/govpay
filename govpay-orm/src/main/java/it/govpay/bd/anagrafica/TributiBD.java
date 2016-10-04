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

import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.TributoFilter;
import it.govpay.bd.model.converter.TributoConverter;
import it.govpay.bd.model.Tributo;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdTipoTributo;
import it.govpay.orm.IdTributo;
import it.govpay.orm.dao.jdbc.JDBCTributoServiceSearch;

public class TributiBD extends BasicBD {

	public TributiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	/**
	 * Recupera il tributo identificato dalla chiave fisica
	 * 
	 * @param idTributo
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Tributo getTributo(Long idTributo) throws NotFoundException, MultipleResultException, ServiceException {
		if(idTributo == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}
		
		long id = idTributo.longValue();
		try {
			return TributoConverter.toDTO(((JDBCTributoServiceSearch)this.getTributoService()).get(id));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
  	/**
	 * Recupera il tributo identificato dalla chiave logica
	 * 
	 * @param idApplicazione
	 * @param codTributo
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Tributo getTributo(Long idDominio, String codTributo) throws NotFoundException, MultipleResultException, ServiceException {
		if(idDominio == null) {
			throw new ServiceException("Parameter 'idDominio' cannot be NULL");
		}
		
		try {
			IdTributo idTributo = new IdTributo();
			IdDominio idDominioOrm = new IdDominio();
			IdTipoTributo idTipoTributo = new IdTipoTributo();
			idDominioOrm.setId(idDominio);
			idTributo.setIdDominio(idDominioOrm);
			idTipoTributo.setCodTributo(codTributo);
			idTributo.setIdTipoTributo(idTipoTributo); 
			return TributoConverter.toDTO(this.getTributoService().get(idTributo));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Aggiorna il tributo
	 * 
	 * @param tributo
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void updateTributo(Tributo tributo) throws NotFoundException, ServiceException {
		try {
			it.govpay.orm.Tributo vo = TributoConverter.toVO(tributo);
			IdTributo idVO = this.getTributoService().convertToId(vo);
			if(!this.getTributoService().exists(idVO)) {
				throw new NotFoundException("Tributo con id ["+idVO.toJson()+"] non trovato.");
			}
			this.getTributoService().update(idVO, vo);
			tributo.setId(vo.getId());
			AnagraficaManager.removeFromCache(tributo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

	}
	
	
	/**
	 * Crea un nuovo tributo
	 * @param ente
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public void insertTributo(Tributo tributo) throws ServiceException {
		try {
			it.govpay.orm.Tributo vo = TributoConverter.toVO(tributo);
			this.getTributoService().create(vo);
			tributo.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public TributoFilter newFilter() throws ServiceException {
		return new TributoFilter(this.getTributoService());
	}

	public long count(TributoFilter filter) throws ServiceException {
		try {
			return this.getTributoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Tributo> findAll(TributoFilter filter) throws ServiceException {
		try {
			return TributoConverter.toDTOList(this.getTributoService().findAll(filter.toPaginatedExpression()));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
}
