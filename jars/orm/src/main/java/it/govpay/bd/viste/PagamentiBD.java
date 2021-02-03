package it.govpay.bd.viste;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.viste.model.Pagamento;
import it.govpay.bd.viste.filters.PagamentoFilter;
import it.govpay.bd.viste.model.converter.PagamentoConverter;
import it.govpay.orm.dao.jdbc.converter.VistaPagamentoFieldConverter;
import it.govpay.orm.model.VistaPagamentoModel;

public class PagamentiBD extends BasicBD {

	public PagamentiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public PagamentiBD(String idTransaction) {
		super(idTransaction);
	}
	
	public PagamentiBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public PagamentiBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}
	
	public PagamentoFilter newFilter() throws ServiceException {
		return new PagamentoFilter(this.getVistaPagamentoServiceSearch());
	}
	
	public PagamentoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new PagamentoFilter(this.getVistaPagamentoServiceSearch(),simpleSearch);
	}

	public long count(PagamentoFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(PagamentoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVistaPagamentoServiceSearch());
			}
			
			return this.getVistaPagamentoServiceSearch().count(filter.toExpression()).longValue();
	
		} catch (NotImplementedException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private long _countConLimit(PagamentoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			VistaPagamentoFieldConverter converter = new VistaPagamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			VistaPagamentoModel model = it.govpay.orm.VistaPagamento.model();
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.IUV));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.IUV), "id");
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.DATA_ACQUISIZIONE), "data_acquisizione");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.DATA_ACQUISIZIONE, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getVistaPagamentoServiceSearch().nativeQuery(sql, returnTypes, parameters);
			
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

	public List<Pagamento> findAll(PagamentoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVistaPagamentoServiceSearch());
			}
			
			List<Pagamento> rptLst = new ArrayList<>();
			List<it.govpay.orm.VistaPagamento> rptVOLst = this.getVistaPagamentoServiceSearch().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.VistaPagamento rptVO: rptVOLst) {
				rptLst.add(PagamentoConverter.toDTO(rptVO));
			}
			return rptLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

}
