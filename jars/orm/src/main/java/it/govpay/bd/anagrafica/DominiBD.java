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

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.converter.DominioConverter;
import it.govpay.orm.IdDominio;
import it.govpay.orm.dao.jdbc.JDBCDominioServiceSearch;

public class DominiBD extends BasicBD {

	public static final String tipoElemento = "DOMINIO";

	public DominiBD(BasicBD basicBD) {
		super(basicBD);
	}

	public DominiBD(String idTransaction) {
		super(idTransaction);
	}
	
	public DominiBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public DominiBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	/**
	 * Recupera il Dominio tramite il codDominio
	 * 
	 * @param codDominio
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Dominio getDominio(String codDominio) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			IdDominio id = new IdDominio();
			id.setCodDominio(codDominio);
			it.govpay.orm.Dominio dominioVO = this.getDominioService().get(id);
			BDConfigWrapper configWrapper = new BDConfigWrapper(this.getIdTransaction(), this.isUseCache());
			Dominio dominio = DominioConverter.toDTO(dominioVO, configWrapper);
			return dominio;
		} catch (NotImplementedException e) {
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
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			it.govpay.orm.Dominio dominioVO = ((JDBCDominioServiceSearch)this.getDominioService()).get(id);
			BDConfigWrapper configWrapper = new BDConfigWrapper(this.getIdTransaction(), this.isUseCache());
			Dominio dominio = DominioConverter.toDTO(dominioVO, configWrapper);
			return dominio;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
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
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			it.govpay.orm.Dominio vo = DominioConverter.toVO(dominio);
			this.getDominioService().create(vo);
			dominio.setId(vo.getId());
			this.emitAudit(dominio);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
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
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			it.govpay.orm.Dominio vo = DominioConverter.toVO(dominio);
			IdDominio id = this.getDominioService().convertToId(vo);

			if(!this.getDominioService().exists(id)) {
				throw new NotFoundException("Dominio con id ["+id+"] non esiste.");
			}
			this.getDominioService().update(id, vo);
			dominio.setId(vo.getId());
			this.emitAudit(dominio);
		} catch (NotImplementedException e) {
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
	 * Recupera gli ibanAccredito per un idDominio (join tra dominio, ente, tributo, ibanAccredito)
	 * 
	 * @param idIbanAccredito
	 * @return
	 * @throws NotFoundException se non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
//	public List<IbanAccredito> getIbanAccreditoByIdDominio(long idDominio) throws ServiceException {
//		try {
//			if(this.isAtomica()) {
//				this.setupConnection(this.getIdTransaction());
//			}
//			IPaginatedExpression exp = this.getIbanAccreditoService().newPaginatedExpression();
//			IbanAccreditoFieldConverter converter = new IbanAccreditoFieldConverter(this.getJdbcProperties().getDatabase());
//			exp.equals(new CustomField("id_dominio",  Long.class, "id_dominio", converter.toTable(it.govpay.orm.IbanAccredito.model())), idDominio);
//			List<it.govpay.orm.IbanAccredito> lstIban = this.getIbanAccreditoService().findAll(exp);
//
//			return IbanAccreditoConverter.toDTOList(lstIban);
//		} catch (NotImplementedException e) {
//			throw new ServiceException(e);
//		} catch (ExpressionNotImplementedException e) {
//			throw new ServiceException(e);
//		} catch (ExpressionException e) {
//			throw new ServiceException(e);
//		} finally {
//			if(this.isAtomica()) {
//				this.closeConnection();
//			}
//		}
// 
//	} TODO Togliere


	public DominioFilter newFilter() throws ServiceException {
		return new DominioFilter(this.getDominioService());
	}

	public DominioFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new DominioFilter(this.getDominioService(),simpleSearch);
	}

	public long count(DominioFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getDominioService());
			}
			return this.getDominioService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private List<Dominio> _findAll(DominioFilter filter) throws ServiceException {
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(this.getIdTransaction(), this.isUseCache());
			return DominioConverter.toDTOList(this.getDominioService().findAll(filter.toPaginatedExpression()), configWrapper);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Dominio> findAll(DominioFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getDominioService());
			}
			return this._findAll(filter);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Ritorna la lista di tutti i domini censiti
	 * @return
	 * @throws ServiceException 
	 */
	public List<Dominio> getDomini() throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
//				filter.setExpressionConstructor(this.getDominioService());
			}
			return this._findAll(this.newFilter());
		}finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

}
