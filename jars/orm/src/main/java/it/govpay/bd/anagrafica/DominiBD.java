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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.converter.ConnettoreNotificaPagamentiConverter;
import it.govpay.bd.model.converter.DominioConverter;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.model.ConnettoreNotificaPagamenti.Tipo;
import it.govpay.model.exception.CodificaInesistenteException;
import it.govpay.orm.IdDominio;
import it.govpay.orm.dao.jdbc.JDBCDominioServiceSearch;
import it.govpay.orm.dao.jdbc.converter.DominioFieldConverter;
import it.govpay.orm.model.DominioModel;

public class DominiBD extends BasicBD {

	public static final String tipoElemento = "DOMINIO";

	public DominiBD(BasicBD basicBD) {
		super(basicBD);
	}

	public DominiBD(String idTransaction) {
		super(idTransaction);
	}
	
	public DominiBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public DominiBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}
	
	private static String getIDConnettore(String codDominio, ConnettoreNotificaPagamenti.Tipo tipo) {
		return "DOM_" + codDominio + "_"+ tipo.toString();
	}
	
	public static String getIDConnettoreMyPivot(String codDominio) {
		return getIDConnettore(codDominio, Tipo.MYPIVOT);
	}
	
	public static String getIDConnettoreSecim(String codDominio) {
		return getIDConnettore(codDominio, Tipo.SECIM);
	}
	
	public static String getIDConnettoreGovPay(String codDominio) {
		return getIDConnettore(codDominio, Tipo.GOVPAY);
	}
	
	public static String getIDConnettoreHyperSicAPKappa(String codDominio) {
		return getIDConnettore(codDominio, Tipo.HYPER_SIC_APKAPPA);
	}
	
	public static String getIDConnettoreMaggioliJPPA(String codDominio) {
		return getIDConnettore(codDominio, Tipo.MAGGIOLI_JPPA);
	}
	
	public static String getIDConnettoreNetPay(String codDominio) {
		return getIDConnettore(codDominio, Tipo.NETPAY);
	}

	/**
	 * Recupera il Dominio tramite il codDominio
	 * 
	 * @param codDominio
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Dominio getDominio(String codDominio) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression expr = this.getDominioService().newExpression();
			expr.equals(it.govpay.orm.Dominio.model().COD_DOMINIO, codDominio);
			it.govpay.orm.Dominio dominioVO = this.getDominioService().find(expr);
			BDConfigWrapper configWrapper = new BDConfigWrapper(this.getIdTransaction(), this.isUseCache());
			Dominio dominio = DominioConverter.toDTO(dominioVO, configWrapper, this.getConnettori(dominioVO));
			return dominio;
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
	
	private Map<Tipo, ConnettoreNotificaPagamenti> getConnettori(it.govpay.orm.Dominio dominioVO) throws ServiceException {
		Map<Tipo, ConnettoreNotificaPagamenti> connettori = new HashMap<>();
		
		if(dominioVO.getCodConnettoreGovPay()!= null) {
			connettori.put(Tipo.GOVPAY, getConnettore(dominioVO.getCodConnettoreGovPay()));
		}
		if(dominioVO.getCodConnettoreHyperSicAPK()!= null) {
			connettori.put(Tipo.HYPER_SIC_APKAPPA, getConnettore(dominioVO.getCodConnettoreHyperSicAPK()));
		}
		if(dominioVO.getCodConnettoreMaggioliJPPA()!= null) {
			connettori.put(Tipo.MAGGIOLI_JPPA, getConnettore(dominioVO.getCodConnettoreMaggioliJPPA()));
		}
		if(dominioVO.getCodConnettoreMyPivot()!= null) {
			connettori.put(Tipo.MYPIVOT, getConnettore(dominioVO.getCodConnettoreMyPivot()));
		}
		if(dominioVO.getCodConnettoreNetPay()!= null) {
			connettori.put(Tipo.NETPAY, getConnettore(dominioVO.getCodConnettoreNetPay()));
		}
		if(dominioVO.getCodConnettoreSecim()!= null) {
			connettori.put(Tipo.SECIM, getConnettore(dominioVO.getCodConnettoreSecim()));
		}
		
		return connettori;
	}
	
	private ConnettoreNotificaPagamenti getConnettore(String codConnettore) throws ServiceException {
		try {
			ConnettoreNotificaPagamenti connettore = null;
			
			if(codConnettore != null) {
				IPaginatedExpression expIntegrazione = this.getConnettoreService().newPaginatedExpression();
				expIntegrazione.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, codConnettore);
				connettore = ConnettoreNotificaPagamentiConverter.toConnettoreNotificaPagamentiDTO(this.getConnettoreService().findAll(expIntegrazione));
			}
			return connettore;
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (CodificaInesistenteException e) {
			throw new ServiceException(e);
		} 
	}

	/**
	 * Recupera il Dominio tramite id
	 * 
	 * @param idDominio
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Dominio getDominio(Long idDominio) throws NotFoundException, MultipleResultException, ServiceException {
		if(idDominio == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		long id = idDominio.longValue();

		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			it.govpay.orm.Dominio dominioVO = ((JDBCDominioServiceSearch)this.getDominioService()).get(id);
			BDConfigWrapper configWrapper = new BDConfigWrapper(this.getIdTransaction(), this.isUseCache());
			Dominio dominio = DominioConverter.toDTO(dominioVO, configWrapper, this.getConnettori(dominioVO));
			return dominio;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}

	}

	/**
	 * Inserisce il dominio con i dati forniti
	 * @param dominio
	 * @throws NotPermittedException se si inserisce un Dominio gia' censito
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertDominio(Dominio dominio) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			if(this.isAtomica()) {
				// 	autocommit false		
				this.setAutoCommit(false);
			}
			
			it.govpay.orm.Dominio vo = DominioConverter.toVO(dominio);
			
			this.getDominioService().create(vo);
			dominio.setId(vo.getId());
			
			if(dominio.getConnettoreMyPivot() != null) {
				this.insertConnettore(dominio.getConnettoreMyPivot(), dominio.getConnettoreMyPivot().getIdConnettore()); 
			} 
			
			if(dominio.getConnettoreSecim() != null) {
				this.insertConnettore(dominio.getConnettoreSecim(), dominio.getConnettoreSecim().getIdConnettore()); 
			}
			
			if(dominio.getConnettoreGovPay() != null) {
				this.insertConnettore(dominio.getConnettoreGovPay(), dominio.getConnettoreGovPay().getIdConnettore()); 
			}
			
			if(dominio.getConnettoreHyperSicAPKappa() != null) {
				this.insertConnettore(dominio.getConnettoreHyperSicAPKappa(), dominio.getConnettoreHyperSicAPKappa().getIdConnettore()); 
			}
			
			if(dominio.getConnettoreMaggioliJPPA() != null) {
				this.insertConnettore(dominio.getConnettoreMaggioliJPPA(), dominio.getConnettoreMaggioliJPPA().getIdConnettore()); 
			}
			
			if(dominio.getConnettoreNetPay() != null) {
				this.insertConnettore(dominio.getConnettoreNetPay(), dominio.getConnettoreNetPay().getIdConnettore()); 
			}
			
			this.emitAudit(dominio);
			
			if(this.isAtomica()) {
				this.commit();
			}
		} catch (NotImplementedException | ExpressionException | ExpressionNotImplementedException e) {
			if(this.isAtomica()) {
				this.rollback();
			}
			throw new ServiceException(e);
		} catch (ServiceException e) {
			if(this.isAtomica()) {
				this.rollback();
			}
			throw e;
		}
		finally {
			if(this.isAtomica()) {
				// 	ripristino l'autocommit.
				this.setAutoCommit(true);
			}
						
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	private void insertConnettore(ConnettoreNotificaPagamenti connettoreDTO, String codConnettore) throws ServiceException, NotImplementedException, ExpressionNotImplementedException, ExpressionException {
		List<it.govpay.orm.Connettore> voConnettoreEsitoLst = ConnettoreNotificaPagamentiConverter.toConnettoreNotificaPagamentiVOList(connettoreDTO);

		IExpression expDelete = this.getConnettoreService().newExpression();
		expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, codConnettore);
		this.getConnettoreService().deleteAll(expDelete);

		for(it.govpay.orm.Connettore connettore: voConnettoreEsitoLst) {
			this.getConnettoreService().create(connettore);
		}
	}

	/**
	 * Aggiorna il dominio con i dati forniti
	 * @param dominio
	 * @throws NotPermittedException se si inserisce un Dominio gia' censito
	 * @throws ServiceException in caso di errore DB.
	 */
	public void updateDominio(Dominio dominio) throws NotFoundException, ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			if(this.isAtomica()) {
				// 	autocommit false		
				this.setAutoCommit(false);
			}
			
			it.govpay.orm.Dominio vo = DominioConverter.toVO(dominio);
			IdDominio id = this.getDominioService().convertToId(vo);

			if(!this.getDominioService().exists(id)) {
				throw new NotFoundException("Dominio con id ["+id+"] non esiste.");
			}
			this.getDominioService().update(id, vo);
			dominio.setId(vo.getId());
			
			this.updateConnettore(dominio.getConnettoreGovPay(), dominio.getCodDominio(), Tipo.GOVPAY);
			
			this.updateConnettore(dominio.getConnettoreHyperSicAPKappa(), dominio.getCodDominio(), Tipo.HYPER_SIC_APKAPPA);
			
			this.updateConnettore(dominio.getConnettoreMaggioliJPPA(), dominio.getCodDominio(), Tipo.MAGGIOLI_JPPA);
			
			this.updateConnettore(dominio.getConnettoreMyPivot(), dominio.getCodDominio(), Tipo.MYPIVOT);
			
			this.updateConnettore(dominio.getConnettoreNetPay(), dominio.getCodDominio(), Tipo.NETPAY);
			
			this.updateConnettore(dominio.getConnettoreSecim(), dominio.getCodDominio(), Tipo.SECIM);
			
			this.emitAudit(dominio);
			if(this.isAtomica()) {
				this.commit();
			}
		} catch (NotImplementedException | ExpressionException | ExpressionNotImplementedException | MultipleResultException e) {
			if(this.isAtomica()) {
				this.rollback();
			}
			throw new ServiceException(e);
		} catch (ServiceException e) {
			if(this.isAtomica()) {
				this.rollback();
			}
			throw e;
		} finally {
			if(this.isAtomica()) {
				// 	ripristino l'autocommit.
				this.setAutoCommit(true);
			}
			
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}

	}
	
	private void updateConnettore(ConnettoreNotificaPagamenti connettoreDTO, String codDominio, Tipo tipo) throws ExpressionNotImplementedException, ExpressionException, ServiceException, NotImplementedException {
		if(connettoreDTO != null) {
			List<it.govpay.orm.Connettore> voConnettoreEsitoLst = ConnettoreNotificaPagamentiConverter.toConnettoreNotificaPagamentiVOList(connettoreDTO);

			IExpression expDelete = this.getConnettoreService().newExpression();
			expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, connettoreDTO.getIdConnettore());
			this.getConnettoreService().deleteAll(expDelete);

			for(it.govpay.orm.Connettore connettore: voConnettoreEsitoLst) {
				this.getConnettoreService().create(connettore);
			}
		} else {
			IExpression expDelete = this.getConnettoreService().newExpression();
			expDelete.equals(it.govpay.orm.Connettore.model().COD_CONNETTORE, getIDConnettore(codDominio, tipo));
			this.getConnettoreService().deleteAll(expDelete);
		}
	}

	public DominioFilter newFilter() throws ServiceException {
		return new DominioFilter(this.getDominioService());
	}

	public DominioFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new DominioFilter(this.getDominioService(),simpleSearch);
	}

	public long count(DominioFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(DominioFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getDominioService());
			}
			return this.getDominioService().count(filter.toExpression()).longValue();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	private long _countConLimit(DominioFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			DominioModel model = it.govpay.orm.Dominio.model();
			DominioFieldConverter converter = new DominioFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.COD_DOMINIO));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.COD_DOMINIO), "id");
			sqlQueryObjectInterno.addSelectField(converter.toAliasColumn(model.COD_DOMINIO, true));
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.COD_DOMINIO, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getDominioService().nativeQuery(sql, returnTypes, parameters);
			
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

	private List<Dominio> _findAll(DominioFilter filter) throws ServiceException {
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(this.getIdTransaction(), this.isUseCache());
			
			List<Dominio> dtoList = new ArrayList<>();
			for(it.govpay.orm.Dominio dominioVO: this.getDominioService().findAll(filter.toPaginatedExpression())) {
				Dominio dominio = DominioConverter.toDTO(dominioVO, configWrapper, this.getConnettori(dominioVO));
				dtoList.add(dominio);
			}
			return dtoList;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Dominio> findAll(DominioFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getDominioService());
			}
			return this._findAll(filter);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	/**
	 * Ritorna la lista di tutti i domini censiti
	 * @return
	 * @throws ServiceException 
	 */
	public List<Dominio> getDomini() throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
//				filter.setExpressionConstructor(this.getDominioService());
			}
			return this._findAll(this.newFilter());
		}finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<String> getCodDomini() throws ServiceException {
		return findAllCodDominio(this.newFilter());
	}
	

	public List<String> findAllCodDominio(DominioFilter filter) throws ServiceException {
		List<String> lstDomini = new ArrayList<>();

		try {	
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getDominioService());
			}
			
			IPaginatedExpression exp = filter.toPaginatedExpression();
			exp.addOrder(it.govpay.orm.Dominio.model().COD_DOMINIO, SortOrder.ASC);
			List<Object> findAll = this.getDominioService().select(exp, it.govpay.orm.Dominio.model().COD_DOMINIO);

			for (Object object : findAll) {
				if(object instanceof String) {
					lstDomini.add((String) object);
				}
			}

			return lstDomini;
		} catch (NotFoundException e) {
			return lstDomini;
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
}
