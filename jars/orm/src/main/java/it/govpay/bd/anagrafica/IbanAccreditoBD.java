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

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.filters.IbanAccreditoFilter;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.converter.IbanAccreditoConverter;
import it.govpay.orm.IdIbanAccredito;
import it.govpay.orm.dao.jdbc.JDBCIbanAccreditoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.IbanAccreditoFieldConverter;
import it.govpay.orm.model.IbanAccreditoModel;

public class IbanAccreditoBD extends BasicBD {

	public IbanAccreditoBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public IbanAccreditoBD(String idTransaction) {
		super(idTransaction);
	}
	
	public IbanAccreditoBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public IbanAccreditoBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
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
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.IbanAccredito ibanAccreditoVO = ((JDBCIbanAccreditoServiceSearch)this.getIbanAccreditoService()).get(id);
			IbanAccredito ibanAccredito = IbanAccreditoConverter.toDTO(ibanAccreditoVO);
			
			return ibanAccredito;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
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
	public IbanAccredito getIbanAccredito(Long idDominio, String codIban) throws NotFoundException, ServiceException, MultipleResultException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IbanAccreditoFieldConverter converter = new IbanAccreditoFieldConverter(this.getJdbcProperties().getDatabaseType());
			
//			IdIbanAccredito id = new IdIbanAccredito();
//			id.setCodIban(codIban);
//			IdDominio idDominioVo = new IdDominio();
//			idDominioVo.setId(idDominio);
//			id.setIdDominio(idDominioVo);
			
			IExpression expr = this.getIbanAccreditoService().newExpression();
			expr.equals(it.govpay.orm.IbanAccredito.model().COD_IBAN, codIban);
			expr.and();
			expr.equals(new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(it.govpay.orm.IbanAccredito.model())), idDominio);
			
			it.govpay.orm.IbanAccredito ibanAccreditoVO = this.getIbanAccreditoService().find(expr);
			IbanAccredito ibanAccredito = IbanAccreditoConverter.toDTO(ibanAccreditoVO);
			return ibanAccredito;
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) { 
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	
	/**
	 * Aggiorna l'ibanAccredito con i dati forniti
	 * @param ente
	 * @throws NotFoundException se non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateIbanAccredito(it.govpay.model.IbanAccredito ibanAccredito) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.IbanAccredito vo = IbanAccreditoConverter.toVO(ibanAccredito);
			IdIbanAccredito id = this.getIbanAccreditoService().convertToId(vo);
			if(!this.getIbanAccreditoService().exists(id)) {
				throw new NotFoundException("IbanAccredito con id ["+id+"] non esiste.");
			}
			this.getIbanAccreditoService().update(id, vo);
			ibanAccredito.setId(vo.getId());
			this.emitAudit(ibanAccredito);
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
	public void insertIbanAccredito(it.govpay.model.IbanAccredito ibanAccredito) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.IbanAccredito vo = IbanAccreditoConverter.toVO(ibanAccredito);
			this.getIbanAccreditoService().create(vo);
			ibanAccredito.setId(vo.getId());
			this.emitAudit(ibanAccredito);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public IbanAccreditoFilter newFilter() throws ServiceException {
		return new IbanAccreditoFilter(this.getIbanAccreditoService());
	}
	
	public IbanAccreditoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new IbanAccreditoFilter(this.getIbanAccreditoService(),simpleSearch);
	}

	public long count(IbanAccreditoFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(IbanAccreditoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getIbanAccreditoService());
			}
			
			return this.getIbanAccreditoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		} 
	}
	
	private long _countConLimit(IbanAccreditoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			IbanAccreditoModel model = it.govpay.orm.IbanAccredito.model();
			IbanAccreditoFieldConverter converter = new IbanAccreditoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			/*
			SELECT count(distinct id) 
				FROM
				  (
				  SELECT versamenti.id
				  FROM versamenti
				  WHERE ...restrizioni di autorizzazione o ricerca...
				  ORDER BY data_richiesta 
				  LIMIT K
				  ) a
				);
			*/
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.COD_IBAN));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.COD_IBAN), "id");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.COD_IBAN, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getIbanAccreditoService().nativeQuery(sql, returnTypes, parameters);
			
			Long count = 0L;
			for (List<Object> row : nativeQuery) {
				int pos = 0;
				count = BasicBD.getValueOrNull(row.get(pos++), Long.class);
			}
			
			return count.longValue();
		} catch (NotImplementedException | SQLQueryObjectException | ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<IbanAccredito> findAll(IbanAccreditoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getIbanAccreditoService());
			}
			
			return IbanAccreditoConverter.toDTOList(this.getIbanAccreditoService().findAll(filter.toPaginatedExpression()));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}


	
}
