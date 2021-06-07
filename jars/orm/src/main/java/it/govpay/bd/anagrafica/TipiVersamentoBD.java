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
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.filters.TipoVersamentoFilter;
import it.govpay.bd.model.converter.TipoVersamentoConverter;
import it.govpay.model.TipoVersamento;
import it.govpay.orm.IdTipoVersamento;
import it.govpay.orm.dao.jdbc.JDBCTipoVersamentoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.TipoVersamentoFieldConverter;
import it.govpay.orm.model.TipoVersamentoModel;

public class TipiVersamentoBD extends BasicBD {

	public TipiVersamentoBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public TipiVersamentoBD(String idTransaction) {
		super(idTransaction);
	}
	
	public TipiVersamentoBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public TipiVersamentoBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	/**
	 * Recupera il tipoVersamento identificato dalla chiave fisica
	 * 
	 * @param idTipoVersamento
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public TipoVersamento getTipoVersamento(Long idTipoVersamento) throws NotFoundException, MultipleResultException, ServiceException {
		if(idTipoVersamento == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		long id = idTipoVersamento.longValue();

		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			return TipoVersamentoConverter.toDTO(((JDBCTipoVersamentoServiceSearch)this.getTipoVersamentoService()).get(id));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}


	/**
	 * Recupera il tipoVersamento identificato dal codice
	 * 
	 * @param codTipoVersamento
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public TipoVersamento getTipoVersamento(String codTipoVersamento) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression expr = this.getTipoVersamentoService().newExpression();
			expr.equals(it.govpay.orm.TipoVersamento.model().COD_TIPO_VERSAMENTO, codTipoVersamento);
			
			return TipoVersamentoConverter.toDTO( this.getTipoVersamentoService().find(expr));
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Aggiorna il tipoVersamento
	 * 
	 * @param tipoVersamento
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void updateTipoVersamento(TipoVersamento tipoVersamento) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.TipoVersamento vo = TipoVersamentoConverter.toVO(tipoVersamento);
			IdTipoVersamento idVO = this.getTipoVersamentoService().convertToId(vo);
			if(!this.getTipoVersamentoService().exists(idVO)) {
				throw new NotFoundException("TipoVersamento con id ["+idVO.toJson()+"] non trovato.");
			}
			this.getTipoVersamentoService().update(idVO, vo);
			tipoVersamento.setId(vo.getId());
			this.emitAudit(tipoVersamento);
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
	 * Crea un nuovo tipoVersamento
	 * @param ente
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public TipoVersamento autoCensimentoTipoVersamento(String codTipoVersamento) throws ServiceException {
		TipoVersamento tipoVersamento = new TipoVersamento();
		tipoVersamento.setCodTipoVersamento(codTipoVersamento);
		tipoVersamento.setDescrizione(codTipoVersamento);
		tipoVersamento.setAbilitatoDefault(true);
		tipoVersamento.setCodificaIuvDefault(null);
		tipoVersamento.setPagaTerziDefault(false);
		this.insertTipoVersamento(tipoVersamento);
		return tipoVersamento;
	}

	/**
	 * Crea un nuovo tipoVersamento
	 * @param ente
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public void insertTipoVersamento(TipoVersamento tipoVersamento) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.TipoVersamento vo = TipoVersamentoConverter.toVO(tipoVersamento);
			this.getTipoVersamentoService().create(vo);
			tipoVersamento.setId(vo.getId());
			this.emitAudit(tipoVersamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public TipoVersamentoFilter newFilter() throws ServiceException {
		return new TipoVersamentoFilter(this.getTipoVersamentoService());
	}

	public TipoVersamentoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new TipoVersamentoFilter(this.getTipoVersamentoService(),simpleSearch);
	}

	public long count(TipoVersamentoFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(TipoVersamentoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getTipoVersamentoService());
			}
			
			return this.getTipoVersamentoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	private long _countConLimit(TipoVersamentoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			TipoVersamentoModel model = it.govpay.orm.TipoVersamento.model();
			TipoVersamentoFieldConverter converter = new TipoVersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.COD_TIPO_VERSAMENTO));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.COD_TIPO_VERSAMENTO), "id");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.COD_TIPO_VERSAMENTO, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getTipoVersamentoService().nativeQuery(sql, returnTypes, parameters);
			
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

	public List<TipoVersamento> findAll(TipoVersamentoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getTipoVersamentoService());
			}
			
			return TipoVersamentoConverter.toDTOList(this.getTipoVersamentoService().findAll(filter.toPaginatedExpression()));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
}
