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
import it.govpay.bd.anagrafica.filters.StazioneFilter;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.converter.StazioneConverter;
import it.govpay.orm.IdStazione;
import it.govpay.orm.dao.jdbc.JDBCStazioneServiceSearch;
import it.govpay.orm.dao.jdbc.converter.StazioneFieldConverter;
import it.govpay.orm.model.StazioneModel;

public class StazioniBD extends BasicBD {

	public StazioniBD(BasicBD basicBD) {
		super(basicBD);
	}

	public StazioniBD(String idTransaction) {
		super(idTransaction);
	}
	
	public StazioniBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public StazioniBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	public void insertStazione(Stazione stazione) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			it.govpay.orm.Stazione vo = StazioneConverter.toVO(stazione);

			this.getStazioneService().create(vo);
			stazione.setId(vo.getId());			
			this.emitAudit(stazione);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public void updateStazione(Stazione stazione) throws ServiceException, NotFoundException {

		try {

			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			it.govpay.orm.Stazione vo = StazioneConverter.toVO(stazione);
			IdStazione idStazione = this.getStazioneService().convertToId(vo);

			if(!this.getStazioneService().exists(idStazione)) {
				throw new NotFoundException("IdStazione con id ["+idStazione.toJson()+"] non trovata");
			}

			this.getStazioneService().update(idStazione, vo);
			stazione.setId(vo.getId());
			this.emitAudit(stazione);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}



	public Stazione getStazione(Long idStazione) throws ServiceException, NotFoundException, MultipleResultException {

		if(idStazione== null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}
		long id = idStazione.longValue();
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			it.govpay.orm.Stazione stazioneVO = ((JDBCStazioneServiceSearch)this.getStazioneService()).get(id);
			Stazione stazione = StazioneConverter.toDTO(stazioneVO);
			return stazione;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}

	}

	public Stazione getStazione(String codStazione) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			IExpression expr = this.getStazioneService().newExpression();
			expr.equals(it.govpay.orm.Stazione.model().COD_STAZIONE, codStazione);

			it.govpay.orm.Stazione stazioneVO = this.getStazioneService().find(expr);
			Stazione stazione = StazioneConverter.toDTO(stazioneVO);
			return stazione;
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) { 
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}

	}

	public StazioneFilter newFilter() throws ServiceException {
		return new StazioneFilter(this.getStazioneService());
	}

	public StazioneFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new StazioneFilter(this.getStazioneService(), simpleSearch);
	}

	public long count(StazioneFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(StazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getStazioneService());
			}
			return this.getStazioneService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	private long _countConLimit(StazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			StazioneModel model = it.govpay.orm.Stazione.model();
			StazioneFieldConverter converter = new StazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.COD_STAZIONE));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.COD_STAZIONE), "id");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.COD_STAZIONE, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getStazioneService().nativeQuery(sql, returnTypes, parameters);
			
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

	public List<Stazione> findAll(StazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getStazioneService());
			}
			return this._findAll(filter);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<Stazione> _findAll(StazioneFilter filter) throws ServiceException {
		try {
			return StazioneConverter.toDTOList(this.getStazioneService().findAll(filter.toPaginatedExpression()));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Stazione> getStazioni() throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			return this._findAll(this.newFilter());
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

}
