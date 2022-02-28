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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.certificate.CertificateUtils;
import org.openspcoop2.utils.certificate.PrincipalType;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.filters.OperatoreFilter;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.converter.OperatoreConverter;
import it.govpay.orm.IdOperatore;
import it.govpay.orm.dao.jdbc.JDBCOperatoreServiceSearch;
import it.govpay.orm.dao.jdbc.converter.OperatoreFieldConverter;
import it.govpay.orm.model.OperatoreModel;

public class OperatoriBD extends BasicBD {

	public OperatoriBD(BasicBD basicBD) {
		super(basicBD);
	}

	public OperatoriBD(String idTransaction) {
		super(idTransaction);
	}

	public OperatoriBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}

	public OperatoriBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	/**
	 * Recupera l'operatore identificato dalla chiave fisica
	 * 
	 * @param idOperatore
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Operatore getOperatore(Long idOperatore) throws NotFoundException, MultipleResultException, ServiceException {

		if(idOperatore== null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		long id = idOperatore.longValue();


		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			it.govpay.orm.Operatore operatoreVO = ((JDBCOperatoreServiceSearch)this.getOperatoreService()).get(id);
			return this.getOperatore(operatoreVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Recupera l'operatore identificato dalla chiave logica
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Operatore getOperatoreByPrincipal(String principal) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			IExpression expr = this.getOperatoreService().newExpression();
			expr.equals(it.govpay.orm.Operatore.model().ID_UTENZA.PRINCIPAL, principal);

			it.govpay.orm.Operatore operatoreVO = this.getOperatoreService().find(expr);
			return this.getOperatore(operatoreVO);
		} catch (NotImplementedException | MultipleResultException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Recupera l'operatore identificato dalla chiave logica
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Operatore getOperatoreBySubject(String principal) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			IExpression expr = this.getOperatoreService().newExpression();

			Hashtable<String,List<String>> hashSubject = null;
			try {
				hashSubject = CertificateUtils.getPrincipalIntoHashtable(principal,PrincipalType.subject);
			}catch(UtilsException e) {
				throw new NotFoundException("Utenza " + principal + " non autorizzata");
			}
			Enumeration<String> keys = hashSubject.keys(); 
			while(keys.hasMoreElements()){
				String key = keys.nextElement();
				List<String> listValues = hashSubject.get(key);
				for (String value : listValues) {
					expr.like(it.govpay.orm.Operatore.model().ID_UTENZA.PRINCIPAL, "/"+CertificateUtils.formatKeyPrincipal(key)+"="+CertificateUtils.formatValuePrincipal(value)+"/", LikeMode.ANYWHERE);
				}
			}

			it.govpay.orm.Operatore operatoreVO = this.getOperatoreService().find(expr);
			return this.getOperatore(operatoreVO);
		} catch (NotImplementedException | MultipleResultException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}


	/**
	 * Recupera l'operatore identificato dalla chiave logica
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Operatore getOperatore(String principal) throws NotFoundException, ServiceException {
		return this.getOperatore(principal,false);
	}

	/**
	 * Recupera l'operatore identificato dalla chiave logica
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Operatore getOperatore(String principal, boolean checkIgnoreCase) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			IExpression expr = this.getOperatoreService().newExpression();
			if(checkIgnoreCase)
				expr.ilike(it.govpay.orm.Operatore.model().ID_UTENZA.PRINCIPAL_ORIGINALE, principal, LikeMode.EXACT);
			else 
				expr.equals(it.govpay.orm.Operatore.model().ID_UTENZA.PRINCIPAL_ORIGINALE, principal);

			it.govpay.orm.Operatore operatoreVO = this.getOperatoreService().find(expr);
			return this.getOperatore(operatoreVO);
		} catch (NotImplementedException | MultipleResultException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}


	private Operatore getOperatore(it.govpay.orm.Operatore operatoreVO) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException {
		Operatore operatore = OperatoreConverter.toDTO(operatoreVO);
		operatore.setUtenza(new UtenzeBD(this).getUtenza(operatoreVO.getIdUtenza().getId()));
		return operatore;
	}

	/**
	 * Recupera tutti gli operatori censiti sul sistema
	 * 
	 * @param principal
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public List<Operatore> getOperatori() throws ServiceException {
		return this.findAll(this.newFilter());
	}

	/**
	 * Aggiorna il operatore
	 * 
	 * @param operatore
	 * @throws NotFoundException
	 * @throws ServiceException
	 */
	public void updateOperatore(Operatore operatore) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			try {
				UtenzeBD utenzeBD = new UtenzeBD(this);
				// autocommit false		
				this.setAutoCommit(false);

				if(!utenzeBD.exists(operatore.getUtenza())) {
					utenzeBD.insertUtenza(operatore.getUtenza());
				} else {
					try {
						utenzeBD.updateUtenza(operatore.getUtenza());
					} catch(NotFoundException e) {
						throw new ServiceException(e);
					}
				}
				operatore.setIdUtenza(operatore.getUtenza().getId());

				it.govpay.orm.Operatore vo = OperatoreConverter.toVO(operatore);
				IdOperatore idOperatore = this.getOperatoreService().convertToId(vo);
				if(!this.getOperatoreService().exists(idOperatore)) {
					throw new NotFoundException("Operatore con id ["+idOperatore.toJson()+"] non trovato");
				}

				this.getOperatoreService().update(idOperatore, vo);
				operatore.setId(vo.getId());

				this.emitAudit(operatore);
				this.commit();
			} catch (NotImplementedException | MultipleResultException | UtilsException e) {
				this.rollback();
				throw new ServiceException(e);
			} finally {
				// ripristino l'autocommit.
				this.setAutoCommit(true); 
			} 
		}finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Crea un nuovo operatore.
	 * 
	 * @param ente
	 * @throws NotPermittedException
	 * @throws ServiceException
	 */
	public void insertOperatore(Operatore operatore) throws  ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			UtenzeBD utenzeBD = new UtenzeBD(this);
			// autocommit false		
			this.setAutoCommit(false); 
			utenzeBD.insertUtenza(operatore.getUtenza());
			operatore.setIdUtenza(operatore.getUtenza().getId());
			it.govpay.orm.Operatore vo = OperatoreConverter.toVO(operatore);
			this.getOperatoreService().create(vo);
			operatore.setId(vo.getId());
			this.commit();
			// ripristino l'autocommit.
			this.setAutoCommit(true); 
			this.emitAudit(operatore);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public OperatoreFilter newFilter() throws ServiceException {
		return new OperatoreFilter(this.getOperatoreService());
	}

	public OperatoreFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new OperatoreFilter(this.getOperatoreService(),simpleSearch);
	}

	public long count(OperatoreFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(OperatoreFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getOperatoreService());
			}

			return this.getOperatoreService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	private long _countConLimit(OperatoreFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			OperatoreModel model = it.govpay.orm.Operatore.model();
			OperatoreFieldConverter converter = new OperatoreFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.NOME));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.NOME), "id");
			sqlQueryObjectInterno.addSelectField(converter.toAliasColumn(model.NOME, true));
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.NOME, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getOperatoreService().nativeQuery(sql, returnTypes, parameters);
			
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

	public List<Operatore> findAll(OperatoreFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getOperatoreService());
			}

			List<Operatore> lst = new ArrayList<>();
			List<it.govpay.orm.Operatore> lstVO = this.getOperatoreService().findAll(filter.toPaginatedExpression());

			for(it.govpay.orm.Operatore operatoreVO : lstVO) {
				lst.add(this.getOperatore(operatoreVO));
			}
			return lst;
		} catch (NotImplementedException | NotFoundException | MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public void deleteOperatore(String principal) throws ServiceException, NotFoundException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			boolean oldAutoCommit = this.isAutoCommit();
			try {

				this.setAutoCommit(false); 

				Operatore operatore = this.getOperatore(principal);

				if(!this.exists(operatore )) {
					throw new NotFoundException();
				}

				Utenza utenza = operatore.getUtenza();
				this.getOperatoreService().delete(OperatoreConverter.toVO(operatore));

				// chiama utenza bd. delete
				new UtenzeBD(this).deleteUtenza(utenza);

				this.commit();
			} catch (NotImplementedException e) {
				this.rollback(); 
				throw new ServiceException(e);
			} catch (ServiceException e) {
				this.rollback();
				throw e;
			} finally {
				this.setAutoCommit(oldAutoCommit); 
			}  
		}	finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Recupera operatore identificata dalla chiave fisica
	 * 
	 * @param operatore
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public boolean exists(Operatore operatore) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			IExpression expr = this.getOperatoreService().newExpression();
			expr.equals(it.govpay.orm.Operatore.model().ID_UTENZA.PRINCIPAL_ORIGINALE,operatore.getPrincipal());
			return this.getOperatoreService().count(expr).longValue() > 0;
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
}
