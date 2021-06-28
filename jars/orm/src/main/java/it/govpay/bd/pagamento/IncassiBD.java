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
package it.govpay.bd.pagamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.converter.IncassoConverter;
import it.govpay.bd.pagamento.filters.IncassoFilter;
import it.govpay.bd.pagamento.filters.PagamentoFilter;
import it.govpay.orm.IdIncasso;
import it.govpay.orm.dao.jdbc.JDBCIncassoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.IncassoFieldConverter;
import it.govpay.orm.model.IncassoModel;

public class IncassiBD extends BasicBD {
	
	public IncassiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public IncassiBD(String idTransaction) {
		super(idTransaction);
	}
	
	public IncassiBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public IncassiBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}
	
	public Incasso getIncasso(long id) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Incasso pagamentoVO = ((JDBCIncassoServiceSearch)this.getIncassoService()).get(id);
			return IncassoConverter.toDTO(pagamentoVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public Incasso getIncasso(String codDominio, String idRiconciliazione) throws ServiceException, NotFoundException, MultipleResultException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression expr = this.getIncassoService().newExpression();
			expr.equals(it.govpay.orm.Incasso.model().COD_DOMINIO, codDominio);
			expr.and();
			expr.equals(it.govpay.orm.Incasso.model().IDENTIFICATIVO, idRiconciliazione);
			
			it.govpay.orm.Incasso pagamentoVO = this.getIncassoService().find(expr);
			return IncassoConverter.toDTO(pagamentoVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw e;
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	public void insertIncasso(Incasso incasso) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Incasso vo = IncassoConverter.toVO(incasso);
			this.getIncassoService().create(vo);
			incasso.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void updateIncasso(Incasso incasso) throws ServiceException, NotFoundException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Incasso vo = IncassoConverter.toVO(incasso);
			IdIncasso oldId = new IdIncasso();
			oldId.setCodDominio(incasso.getCodDominio());
			oldId.setIdentificativo(incasso.getIdRiconciliazione());
			
			this.getIncassoService().update(oldId ,vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public IncassoFilter newFilter() throws ServiceException {
		return new IncassoFilter(this.getIncassoService());
	}

	public IncassoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new IncassoFilter(this.getIncassoService(),simpleSearch);
	}
	
	public long count(IncassoFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(IncassoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getIncassoService());
			}
			
			return this.getIncassoService().count(filter.toExpression()).longValue();
	
		} catch (NotImplementedException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private long _countConLimit(IncassoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			IncassoFieldConverter converter = new IncassoFieldConverter(this.getJdbcProperties().getDatabase());
			IncassoModel model = it.govpay.orm.Incasso.model();
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.TRN));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.TRN), "id");
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.DATA_ORA_INCASSO), "data_ora_incasso");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.DATA_ORA_INCASSO, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getIncassoService().nativeQuery(sql, returnTypes, parameters);
			
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

	public List<Incasso> findAll(IncassoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getIncassoService());
			}
			
			List<Incasso> incassoLst = new ArrayList<>();

			// if(filter.getCodDomini() != null && filter.getCodDomini().isEmpty()) return incassoLst;
			List<it.govpay.orm.Incasso> incassoVOLst = this.getIncassoService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.Incasso incassoVO: incassoVOLst) {
				incassoLst.add(IncassoConverter.toDTO(incassoVO));
			}
			return incassoLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public List<Incasso> findRiconciliazioniDaAcquisire(BDConfigWrapper configWrapper, Integer offset, Integer limit) throws ServiceException {
		return findRiconciliazioniDaAcquisire(configWrapper,offset,limit,false);
		
	}
	
	public List<Incasso> findRiconciliazioniDaAcquisire(BDConfigWrapper configWrapper, Integer offset, Integer limit, boolean deep) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IPaginatedExpression exp = this.getIncassoService().newPaginatedExpression();
			exp.lessThan(it.govpay.orm.Incasso.model().DATA_ORA_INCASSO, new Date());
			exp.equals(it.govpay.orm.Incasso.model().STATO, it.govpay.model.Incasso.StatoIncasso.NUOVO.toString());
			
			if(offset != null) {
				exp.offset(offset);
			}
			
			if(limit != null) {
				exp.limit(limit);
			}
			
			exp.addOrder(it.govpay.orm.Incasso.model().DATA_ORA_INCASSO, SortOrder.DESC);
			
			List<Incasso> incassoLst = new ArrayList<>();
			
			List<it.govpay.orm.Incasso> findAll = this.getIncassoService().findAll(exp);
			for(it.govpay.orm.Incasso incassoVO: findAll) {
				incassoLst.add(IncassoConverter.toDTO(incassoVO));
			}
			
			if(deep) {
				PagamentiBD pagamentiBD = new PagamentiBD(this);
				pagamentiBD.setAtomica(false);
				
				for (Incasso incasso : incassoLst) {
					
					PagamentoFilter filter = pagamentiBD.newFilter();
					filter.setIdIncasso(incasso.getId());
					List<Pagamento> pagamenti = pagamentiBD.findAll(filter);
					
					// popolo valori
					if(pagamenti != null) {
						for(Pagamento pagamento: pagamenti) {
							pagamento.getDominio(configWrapper);
							SingoloVersamento singoloVersamento = pagamento.getSingoloVersamento(this);
							Versamento versamento = singoloVersamento.getVersamento(this);
							versamento.getApplicazione(configWrapper);
							versamento.getDominio(configWrapper);
							versamento.getUo(configWrapper);
							singoloVersamento.getIbanAccredito(configWrapper);
							pagamento.getRpt(this);
							pagamento.setIncasso(incasso);
						}
					}

					incasso.setPagamenti(pagamenti);
				}
			}
			
			return incassoLst;
		} catch(NotImplementedException e) {
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
}
