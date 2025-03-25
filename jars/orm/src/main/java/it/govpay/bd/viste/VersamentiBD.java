/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.bd.viste;

import java.io.UnsupportedEncodingException;
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
import it.govpay.bd.model.Versamento;
import it.govpay.bd.viste.filters.VersamentoFilter;
import it.govpay.bd.viste.model.converter.VersamentoConverter;
import it.govpay.model.exception.CodificaInesistenteException;
import it.govpay.orm.dao.jdbc.converter.VistaVersamentoFieldConverter;
import it.govpay.orm.model.VistaVersamentoModel;

public class VersamentiBD  extends BasicBD {

	public VersamentiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public VersamentiBD(String idTransaction) {
		super(idTransaction);
	}
	
	public VersamentiBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public VersamentiBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	public VersamentoFilter newFilter() {
		return new VersamentoFilter(this.getVistaVersamentoServiceSearch());
	}

	public VersamentoFilter newFilter(boolean simpleSearch) {
		return new VersamentoFilter(this.getVistaVersamentoServiceSearch(),simpleSearch);
	}
	
	public long count(VersamentoFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this.countConLimitEngine(filter) : this.countSenzaLimitEngine(filter);
	}
	
	private long countSenzaLimitEngine(VersamentoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVistaVersamentoServiceSearch());
			}
			
			return this.getVistaVersamentoServiceSearch().count(filter.toExpression()).longValue();
	
		} catch (NotImplementedException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private long countConLimitEngine(VersamentoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			VistaVersamentoFieldConverter converter = new VistaVersamentoFieldConverter(this.getJdbcProperties().getDatabase());
			VistaVersamentoModel model = it.govpay.orm.VistaVersamento.model();
			/*
			SELECT count(distinct id) 
				FROM
				  (
				  SELECT versamenti.id
				  FROM versamenti
				  WHERE ...restrizioni di autorizzazione o ricerca...
				  LIMIT K
				  ) a
				);
			*/
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.COD_VERSAMENTO_ENTE));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.COD_VERSAMENTO_ENTE), "id");
//			sqlQueryObjectInterno.addSelectField(converter.toTable(model.DATA_CREAZIONE), "data_creazione");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
//			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.DATA_CREAZIONE, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getVistaVersamentoServiceSearch().nativeQuery(sql, returnTypes, parameters);
			
			Long count = 0L;
			for (List<Object> row : nativeQuery) {
				count = BasicBD.getValueOrNull(row.get(0), Long.class);
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

	public List<Versamento> findAll(VersamentoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVistaVersamentoServiceSearch());
			}
			
			List<Versamento> versamentoLst = new ArrayList<>();

			List<it.govpay.orm.VistaVersamento> versamentoVOLst = this.getVistaVersamentoServiceSearch().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.VistaVersamento versamentoVO: versamentoVOLst) {
				versamentoLst.add(VersamentoConverter.toDTO(versamentoVO));
			}
			return versamentoLst;
		} catch (NotImplementedException | CodificaInesistenteException |  UnsupportedEncodingException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
}
