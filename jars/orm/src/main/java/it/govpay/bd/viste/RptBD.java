package it.govpay.bd.viste;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.viste.filters.RptFilter;
import it.govpay.bd.viste.model.converter.RptConverter;
import it.govpay.orm.dao.jdbc.converter.VistaRptVersamentoFieldConverter;
import it.govpay.orm.model.VistaRptVersamentoModel;

public class RptBD extends BasicBD {

	public RptBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public RptFilter newFilter() throws ServiceException {
		return new RptFilter(this.getVistaRptVersamentoServiceSearch());
	}
	
	public RptFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new RptFilter(this.getVistaRptVersamentoServiceSearch(),simpleSearch);
	}

	public long count(RptFilter filter) throws ServiceException {
		try {
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			VistaRptVersamentoFieldConverter converter = new VistaRptVersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			VistaRptVersamentoModel model = it.govpay.orm.VistaRptVersamento.model();
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
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.DATA_MSG_RICHIESTA, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getVistaRptVersamentoServiceSearch().nativeQuery(sql, returnTypes, parameters);
			
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
		}
	}

	public List<Rpt> findAll(RptFilter filter) throws ServiceException {
		try {
			List<Rpt> rptLst = new ArrayList<>();
			List<it.govpay.orm.VistaRptVersamento> rptVOLst = this.getVistaRptVersamentoServiceSearch().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.VistaRptVersamento rptVO: rptVOLst) {
				rptLst.add(RptConverter.toDTO(rptVO));
			}
			return rptLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

}
