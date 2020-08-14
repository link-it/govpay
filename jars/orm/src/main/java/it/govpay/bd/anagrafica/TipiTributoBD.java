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

import java.util.List;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.TipoTributoFilter;
import it.govpay.bd.model.converter.TipoTributoConverter;
import it.govpay.model.TipoTributo;
import it.govpay.orm.IdTipoTributo;
import it.govpay.orm.dao.jdbc.JDBCTipoTributoServiceSearch;

public class TipiTributoBD extends BasicBD {

	public TipiTributoBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public TipiTributoBD(String idTransaction) {
		super(idTransaction);
	}
	
	public TipiTributoBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public TipiTributoBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}
	
	/**
	 * Recupera il tributo identificato dalla chiave fisica
	 * 
	 * @param idTipoTributo
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public TipoTributo getTipoTributo(Long idTipoTributo) throws NotFoundException, MultipleResultException, ServiceException {
		if(idTipoTributo == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}
		
		long id = idTipoTributo.longValue();

		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			return TipoTributoConverter.toDTO(((JDBCTipoTributoServiceSearch)this.getTipoTributoService()).get(id));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	
	/**
	 * Recupera il tributo identificato dal codice
	 * 
	 * @param codTributo
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public TipoTributo getTipoTributo(String codTributo) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdTipoTributo idTipoTributo = new IdTipoTributo();
			idTipoTributo.setCodTributo(codTributo);
			return TipoTributoConverter.toDTO( this.getTipoTributoService().get(idTipoTributo));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	/**
	 * Aggiorna il tributo
	 * 
	 * @param tributo
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void updateTipoTributo(TipoTributo tributo) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.TipoTributo vo = TipoTributoConverter.toVO(tributo);
			IdTipoTributo idVO = this.getTipoTributoService().convertToId(vo);
			if(!this.getTipoTributoService().exists(idVO)) {
				throw new NotFoundException("TipoTributo con id ["+idVO.toJson()+"] non trovato.");
			}
			this.getTipoTributoService().update(idVO, vo);
			tributo.setId(vo.getId());
			this.emitAudit(tributo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}

	}
	
	
	/**
	 * Crea un nuovo tributo
	 * @param ente
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public void insertTipoTributo(TipoTributo tributo) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.TipoTributo vo = TipoTributoConverter.toVO(tributo);
			this.getTipoTributoService().create(vo);
			tributo.setId(vo.getId());
			this.emitAudit(tributo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public TipoTributoFilter newFilter() throws ServiceException {
		return new TipoTributoFilter(this.getTipoTributoService());
	}
	
	public TipoTributoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new TipoTributoFilter(this.getTipoTributoService(),simpleSearch);
	}

	public long count(TipoTributoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getTipoTributoService());
			}
			
			return this.getTipoTributoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<TipoTributo> findAll(TipoTributoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getTipoTributoService());
			}
			
			return TipoTributoConverter.toDTOList(this.getTipoTributoService().findAll(filter.toPaginatedExpression()));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
}
