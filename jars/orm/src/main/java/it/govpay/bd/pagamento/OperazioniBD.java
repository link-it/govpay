/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2018 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.converter.OperazioneConverter;
import it.govpay.bd.pagamento.filters.OperazioneFilter;
import it.govpay.orm.Operazione;
import it.govpay.orm.dao.jdbc.converter.OperazioneFieldConverter;
import it.govpay.orm.model.OperazioneModel;

public class OperazioniBD extends BasicBD {

	public OperazioniBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public OperazioniBD(String idTransaction) {
		super(idTransaction);
	}
	
	public OperazioniBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public OperazioniBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}
	
	private List<it.govpay.bd.model.Operazione> findAll(IPaginatedExpression exp) throws ServiceException, NotImplementedException {
		List<Operazione> findAll = this.getOperazioneService().findAll(exp);
		List<it.govpay.bd.model.Operazione> findAllDTO = new ArrayList<>(); 
		for(Operazione caricamento : findAll) {
			findAllDTO.add(OperazioneConverter.toDTO(caricamento));
		}
		return findAllDTO;
	}
	
	
	public void insertOperazione(it.govpay.bd.model.Operazione caricamento) throws ServiceException {
		try{
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			Operazione caricamentoVo = OperazioneConverter.toVO(caricamento);
			this.getOperazioneService().create(caricamentoVo);
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void updateOperazione(it.govpay.bd.model.Operazione caricamento) throws ServiceException {
		try{
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			Operazione caricamentoVo = OperazioneConverter.toVO(caricamento);
			this.getOperazioneService().update(caricamentoVo);
		} catch(NotImplementedException | NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public OperazioneFilter newFilter() {
		return new OperazioneFilter(this.getOperazioneService());
	}
	
	public OperazioneFilter newFilter(boolean simpleSearch) {
		return new OperazioneFilter(this.getOperazioneService(),simpleSearch);
	}
	
	public long count(OperazioneFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(OperazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getOperazioneService());
			}
			
			return this.getOperazioneService().count(filter.toExpression()).longValue();
	
		} catch (NotImplementedException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private long _countConLimit(OperazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			OperazioneFieldConverter converter = new OperazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			OperazioneModel model = it.govpay.orm.Operazione.model();
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.STATO));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.STATO), "id");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.LINEA_ELABORAZIONE, true), true);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getOperazioneService().nativeQuery(sql, returnTypes, parameters);
			
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

	public List<it.govpay.bd.model.Operazione> findAll(OperazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getOperazioneService());
			}
			
			return this.findAll(filter.toPaginatedExpression());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public it.govpay.bd.model.Operazione getOperazione(long idTracciato, long linea) throws ServiceException, NotFoundException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression expr = this.getOperazioneService().newExpression();
			OperazioneFieldConverter converter = new OperazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			CustomField idTracciatoCustomField = new CustomField("id_tracciato",  Long.class, "id_tracciato",converter.toTable(it.govpay.orm.Operazione.model()));
			expr.equals(idTracciatoCustomField, idTracciato);
			expr.equals(Operazione.model().LINEA_ELABORAZIONE, linea);
			it.govpay.orm.Operazione operazione = this.getOperazioneService().find(expr);
			return OperazioneConverter.toDTO(operazione);
		} catch (NotImplementedException | MultipleResultException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
}
