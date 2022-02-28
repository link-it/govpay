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
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.filters.UnitaOperativaFilter;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.converter.UnitaOperativaConverter;
import it.govpay.orm.IdUo;
import it.govpay.orm.dao.jdbc.JDBCUoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.UoFieldConverter;
import it.govpay.orm.model.UoModel;

public class UnitaOperativeBD extends BasicBD {

	public UnitaOperativeBD(BasicBD basicBD) {
		super(basicBD);
	}

	public UnitaOperativeBD(String idTransaction) {
		super(idTransaction);
	}
	
	public UnitaOperativeBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public UnitaOperativeBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	public UnitaOperativa getUnitaOperativa(Long id) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			it.govpay.orm.Uo uoVO = ((JDBCUoServiceSearch) this.getUoService()).get(id);
			UnitaOperativa uo = UnitaOperativaConverter.toDTO(uoVO);
			return uo;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public UnitaOperativa getUnitaOperativa(Long idDominio, String codUnitaOperativa) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			UoFieldConverter converter = new UoFieldConverter(this.getJdbcProperties().getDatabaseType());
			
			IExpression expr = this.getUoService().newExpression();
			expr.equals(it.govpay.orm.Uo.model().COD_UO, codUnitaOperativa);
			expr.and();
			expr.equals(new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(it.govpay.orm.Uo.model())), idDominio);

//			IdUo id = new IdUo();
//			id.setCodUo(codUnitaOperativa);
//			IdDominio idDominioOrm = new IdDominio();
//			idDominioOrm.setId(idDominio);
//			id.setIdDominio(idDominioOrm);
			it.govpay.orm.Uo uoVO = this.getUoService().find(expr);
			return UnitaOperativaConverter.toDTO(uoVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public UnitaOperativa getUnitaOperativaByCodUnivocoUo(Long idDominio, String codUnivocoUnitaOperativa) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			UnitaOperativaFilter filter = this.newFilter();
			filter.setCodIdentificativo(codUnivocoUnitaOperativa);
			filter.setDominioFilter(idDominio);
			List<UnitaOperativa> findAll = this._findAll(filter);
			if(findAll.size() == 0) {
				throw new NotFoundException();
			} else {
				return findAll.get(0);
			}
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public void updateUnitaOperativa(UnitaOperativa uo) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			it.govpay.orm.Uo vo = UnitaOperativaConverter.toVO(uo);
			IdUo idUnitaOperativa = this.getUoService().convertToId(vo);
			if(!this.getUoService().exists(idUnitaOperativa)) {
				throw new NotFoundException("UnitaOperativa con id ["+idUnitaOperativa.toJson()+"] non trovato");
			}
			this.getUoService().update(idUnitaOperativa, vo);
			this.emitAudit(uo);
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

	public void insertUnitaOperativa(UnitaOperativa uo) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			it.govpay.orm.Uo vo = UnitaOperativaConverter.toVO(uo);
			this.getUoService().create(vo);
			uo.setId(vo.getId());
			this.emitAudit(uo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public UnitaOperativaFilter newFilter() throws ServiceException {
		return new UnitaOperativaFilter(this.getUoService());
	}

	public UnitaOperativaFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new UnitaOperativaFilter(this.getUoService(),simpleSearch);
	}

	public long count(UnitaOperativaFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(UnitaOperativaFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getUoService());
			}
			return this.getUoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	private long _countConLimit(UnitaOperativaFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			UoModel model = it.govpay.orm.Uo.model();
			UoFieldConverter converter = new UoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.COD_UO));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.COD_UO), "id");
			sqlQueryObjectInterno.addSelectField(converter.toAliasColumn(model.COD_UO, true));
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.COD_UO, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getUoService().nativeQuery(sql, returnTypes, parameters);
			
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

	public List<UnitaOperativa> findAll(UnitaOperativaFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getUoService());
			}
			return this._findAll(filter);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<UnitaOperativa> _findAll(UnitaOperativaFilter filter) throws ServiceException {
		try {
			List<UnitaOperativa> lst = new ArrayList<>();
			List<it.govpay.orm.Uo> lstuoVo = this.getUoService().findAll(filter.toPaginatedExpression());
			for(it.govpay.orm.Uo uoVO: lstuoVo) {
				lst.add(UnitaOperativaConverter.toDTO(uoVO));
			}
			return lst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} 
	}

}
