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
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.filters.IntermediarioFilter;
import it.govpay.bd.model.converter.ConnettoreConverter;
import it.govpay.bd.model.converter.ConnettoreSftpConverter;
import it.govpay.bd.model.converter.IntermediarioConverter;
import it.govpay.model.Connettore;
import it.govpay.model.ConnettoreSftp;
import it.govpay.model.Intermediario;
import it.govpay.orm.IdIntermediario;
import it.govpay.orm.dao.jdbc.JDBCIntermediarioServiceSearch;
import it.govpay.orm.dao.jdbc.converter.IntermediarioFieldConverter;
import it.govpay.orm.model.IntermediarioModel;

public class IntermediariBD extends BasicBD {

	public IntermediariBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public IntermediariBD(String idTransaction) {
		super(idTransaction);
	}
	
	public IntermediariBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public IntermediariBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	/**
	 * Recupera l'intermediario tramite l'id fisico
	 * 
	 * @param idEnte
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Intermediario getIntermediario(Long idIntermediario) throws NotFoundException, MultipleResultException, ServiceException {
		if(idIntermediario == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		long id = idIntermediario.longValue();

		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Intermediario intermediarioVO = ((JDBCIntermediarioServiceSearch)this.getIntermediarioService()).get(id);
			return this.getIntermediario(intermediarioVO);

		} catch (NotImplementedException e) {
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

	/**
	 * Recupera l'intermediario tramite l'id logico
	 * 
	 * @param codEnte
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Intermediario getIntermediario(String codIntermediario) throws NotFoundException, MultipleResultException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression expr = this.getIntermediarioService().newExpression();
			expr.equals(it.govpay.orm.Intermediario.model().COD_INTERMEDIARIO, codIntermediario);
			
			it.govpay.orm.Intermediario intermediarioVO = this.getIntermediarioService().find(expr);

			return this.getIntermediario(intermediarioVO);
		} catch (NotImplementedException e) {
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

	private Intermediario getIntermediario(it.govpay.orm.Intermediario intermediarioVO) throws ExpressionNotImplementedException, ExpressionException, ServiceException, NotImplementedException {
		Intermediario intermediario = IntermediarioConverter.toDTO(intermediarioVO);

		if(intermediarioVO.getCodConnettorePdd() != null) {
			IPaginatedExpression exp = this.getConnettoreService().newPaginatedExpression();
			exp.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, intermediarioVO.getCodConnettorePdd());

			List<it.govpay.orm.Connettore> connettori = this.getConnettoreService().findAll(exp);
			Connettore connettorePdd = ConnettoreConverter.toDTO(connettori);
			intermediario.setConnettorePdd(connettorePdd);
		}
		if(intermediarioVO.getCodConnettoreFtp() != null) {
			IPaginatedExpression exp = this.getConnettoreService().newPaginatedExpression();
			exp.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, intermediarioVO.getCodConnettoreFtp());

			List<it.govpay.orm.Connettore> connettori = this.getConnettoreService().findAll(exp);
			ConnettoreSftp connettoreSftp = ConnettoreSftpConverter.toDTO(connettori);
			intermediario.setConnettoreSftp(connettoreSftp);
		}
		return intermediario;

	}

	/**
	 * Recupera la lista degli intermediari censiti sul sistema
	 * 
	 * @return
	 * @throws ServiceException in caso di errore DB.
	 */
	public List<Intermediario> getIntermediari() throws ServiceException{
		return this.findAll(this.newFilter());
	}

	/**
	 * Aggiorna l'intermediari con i dati forniti
	 * @param intermediari
	 * @throws NotFoundException se l'intermediari non esiste
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateIntermediario(Intermediario intermediario) throws NotFoundException, ServiceException {

		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Intermediario vo = IntermediarioConverter.toVO(intermediario);
			IdIntermediario id = this.getIntermediarioService().convertToId(vo);

			if(!this.getIntermediarioService().exists(id)) {
				throw new NotFoundException("Intermediario con id ["+id+"] non esiste.");
			}

			this.getIntermediarioService().update(id, vo);
			intermediario.setId(vo.getId());

			if(intermediario.getConnettorePdd() != null) {

				List<it.govpay.orm.Connettore> voConnettoreLst = ConnettoreConverter.toVOList(intermediario.getConnettorePdd());


				IExpression expDelete = this.getConnettoreService().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, intermediario.getConnettorePdd().getIdConnettore());
				this.getConnettoreService().deleteAll(expDelete);

				for(it.govpay.orm.Connettore connettore: voConnettoreLst) {
					this.getConnettoreService().create(connettore);
				}
			}

			if(intermediario.getConnettoreSftp() != null) {

				List<it.govpay.orm.Connettore> voConnettoreLst = ConnettoreSftpConverter.toVOList(intermediario.getConnettoreSftp());


				IExpression expDelete = this.getConnettoreService().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, intermediario.getConnettoreSftp().getIdConnettore());
				this.getConnettoreService().deleteAll(expDelete);

				for(it.govpay.orm.Connettore connettore: voConnettoreLst) {
					this.getConnettoreService().create(connettore);
				}
			}

			this.emitAudit(intermediario);
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

	/**
	 * Aggiorna l'intermediari con i dati forniti
	 * @param intermediari
	 * @throws NotPermittedException se si inserisce un intermediari gia' censito
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertIntermediario(Intermediario intermediario) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Intermediario vo = IntermediarioConverter.toVO(intermediario);

			this.getIntermediarioService().create(vo);
			intermediario.setId(vo.getId());

			if(intermediario.getConnettorePdd() != null) {

				List<it.govpay.orm.Connettore> voConnettoreLst = ConnettoreConverter.toVOList(intermediario.getConnettorePdd());

				IExpression expDelete = this.getConnettoreService().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, intermediario.getConnettorePdd().getIdConnettore());
				this.getConnettoreService().deleteAll(expDelete);

				for(it.govpay.orm.Connettore connettore: voConnettoreLst) {

					this.getConnettoreService().create(connettore);
				}
			}
			
			if(intermediario.getConnettoreSftp() != null) {

				List<it.govpay.orm.Connettore> voConnettoreLst = ConnettoreSftpConverter.toVOList(intermediario.getConnettoreSftp());


				IExpression expDelete = this.getConnettoreService().newExpression();
				expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, intermediario.getConnettoreSftp().getIdConnettore());
				this.getConnettoreService().deleteAll(expDelete);

				for(it.govpay.orm.Connettore connettore: voConnettoreLst) {
					this.getConnettoreService().create(connettore);
				}
			}

			this.emitAudit(intermediario);
		} catch (NotImplementedException e) {
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


	public IntermediarioFilter newFilter() throws ServiceException {
		return new IntermediarioFilter(this.getIntermediarioService());
	}
	
	public IntermediarioFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new IntermediarioFilter(this.getIntermediarioService(),simpleSearch);
	}

	public long count(IntermediarioFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(IntermediarioFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getIntermediarioService());
			}
			
			return this.getIntermediarioService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	private long _countConLimit(IntermediarioFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			IntermediarioModel model = it.govpay.orm.Intermediario.model();
			IntermediarioFieldConverter converter = new IntermediarioFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.COD_INTERMEDIARIO));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.COD_INTERMEDIARIO), "id");
			sqlQueryObjectInterno.addSelectField(converter.toAliasColumn(model.COD_INTERMEDIARIO, true));
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.COD_INTERMEDIARIO, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getIntermediarioService().nativeQuery(sql, returnTypes, parameters);
			
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

	public List<Intermediario> findAll(IntermediarioFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getIntermediarioService());
			}
			
			List<Intermediario> lst = new ArrayList<>();
			List<it.govpay.orm.Intermediario> lstIntermediarioVO = this.getIntermediarioService().findAll(filter.toPaginatedExpression());
			for(it.govpay.orm.Intermediario intermediarioVO: lstIntermediarioVO) {
				lst.add(this.getIntermediario(intermediarioVO));
			}
			return lst;
		} catch (NotImplementedException e) {
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
