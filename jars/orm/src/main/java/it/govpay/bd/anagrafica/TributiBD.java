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
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.filters.TributoFilter;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.converter.TributoConverter;
import it.govpay.orm.IdTributo;
import it.govpay.orm.dao.jdbc.JDBCTributoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.TributoFieldConverter;
import it.govpay.orm.model.TributoModel;

public class TributiBD extends BasicBD {

	public TributiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public TributiBD(String idTransaction) {
		super(idTransaction);
	}
	
	public TributiBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public TributiBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	/**
	 * Recupera il tributo identificato dalla chiave fisica
	 * 
	 * @param idTributo
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Tributo getTributo(Long idTributo) throws NotFoundException, MultipleResultException, ServiceException {
		if(idTributo == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		long id = idTributo.longValue();
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(this.getIdTransaction(), this.isUseCache());
			
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			return TributoConverter.toDTO(((JDBCTributoServiceSearch)this.getTributoService()).get(id), configWrapper);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Recupera il tributo identificato dalla chiave logica
	 * 
	 * @param idApplicazione
	 * @param codTributo
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Tributo getTributo(Long idDominio, String codTributo) throws NotFoundException, MultipleResultException, ServiceException {
		if(idDominio == null) {
			throw new ServiceException("Parameter 'idDominio' cannot be NULL");
		}

		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(this.getIdTransaction(), this.isUseCache());
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			TributoFieldConverter converter = new TributoFieldConverter(this.getJdbcProperties().getDatabaseType());
			
			IExpression expr = this.getTributoService().newExpression();
			expr.equals(it.govpay.orm.Tributo.model().TIPO_TRIBUTO.COD_TRIBUTO, codTributo);
			expr.and();
			expr.equals(new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(it.govpay.orm.Tributo.model())), idDominio);
			
//			IdTributo idTributo = new IdTributo();
//			IdDominio idDominioOrm = new IdDominio();
//			IdTipoTributo idTipoTributo = new IdTipoTributo();
//			idDominioOrm.setId(idDominio);
//			idTributo.setIdDominio(idDominioOrm);
//			idTipoTributo.setCodTributo(codTributo);
//			idTributo.setIdTipoTributo(idTipoTributo); 
			return TributoConverter.toDTO(this.getTributoService().find(expr), configWrapper);
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) { 
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Aggiorna il tributo
	 * 
	 * @param tributo
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void updateTributo(Tributo tributo) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Tributo vo = TributoConverter.toVO(tributo);
			IdTributo idVO = this.getTributoService().convertToId(vo);
			if(!this.getTributoService().exists(idVO)) {
				throw new NotFoundException("Tributo con id ["+idVO.toJson()+"] non trovato.");
			}
			this.getTributoService().update(idVO, vo);
			tributo.setId(vo.getId());
			this.emitAudit(tributo);
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
	 * Crea un nuovo tributo
	 * @param ente
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public void insertTributo(it.govpay.model.Tributo tributo) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Tributo vo = TributoConverter.toVO(tributo);
			this.getTributoService().create(vo);
			tributo.setId(vo.getId());
			this.emitAudit(tributo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public TributoFilter newFilter() throws ServiceException {
		return new TributoFilter(this.getTributoService());
	}
	
	public TributoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new TributoFilter(this.getTributoService(),simpleSearch);
	}

	public long count(TributoFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(TributoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getTributoService());
			}
			
			return this.getTributoService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	private long _countConLimit(TributoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			TributoModel model = it.govpay.orm.Tributo.model();
			TributoFieldConverter converter = new TributoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.ABILITATO));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.ABILITATO), "id");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
//			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.TIPO_TRIBUTO.COD_TRIBUTO, true), false);
			sqlQueryObjectInterno.addOrderBy("id", false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getTributoService().nativeQuery(sql, returnTypes, parameters);
			
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

	public List<Tributo> findAll(TributoFilter filter) throws ServiceException {
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(this.getIdTransaction(), this.isUseCache());
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction()); 
				filter.setExpressionConstructor(this.getTributoService());
			}
			
			return TributoConverter.toDTOList(this.getTributoService().findAll(filter.toPaginatedExpression()), configWrapper);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<Long> getIdTipiTributiDefinitiPerDominio(Long idDominio) throws ServiceException {
		List<Long> lstIdTipiTributi = new ArrayList<>();

		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IPaginatedExpression pagExpr = this.getTributoService().newPaginatedExpression();

			TributoFieldConverter converter = new TributoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			CustomField cfIdDominio = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(it.govpay.orm.Tributo.model()));
			pagExpr.equals(cfIdDominio, idDominio);

			CustomField cfIdTipoTributo = new CustomField("id_tipo_tributo", Long.class, "id_tipo_tributo", converter.toTable(it.govpay.orm.Tributo.model()));
			List<Object> select = this.getTributoService().select(pagExpr, true, cfIdTipoTributo);

			if(select != null && select.size() > 0)
				for (Object object : select) {
					if(object instanceof Long){
						lstIdTipiTributi.add((Long) object); 
					}
				}
		}catch(ServiceException e){
			throw e;
		} catch (NotFoundException e) {
			return new ArrayList<>();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
		return lstIdTipiTributi;
	}
}
