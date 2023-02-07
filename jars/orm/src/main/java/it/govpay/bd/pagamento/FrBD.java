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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.converter.FrConverter;
import it.govpay.bd.pagamento.filters.FrFilter;
import it.govpay.model.Fr.StatoFr;
import it.govpay.orm.FR;
import it.govpay.orm.IdFr;
import it.govpay.orm.dao.jdbc.JDBCFRServiceSearch;
import it.govpay.orm.dao.jdbc.converter.FRFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.FRFetch;
import it.govpay.orm.model.FRModel;

public class FrBD extends BasicBD { 

	public FrBD(BasicBD basicBD) {
		super(basicBD);
	}

	public FrBD(String idTransaction) {
		super(idTransaction);
	}

	public FrBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}

	public FrBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	/**
	 * Recupera l'Fr identificato dalla chiave fisica
	 * 
	 * @param idFr
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Fr getFr(long idFr) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			FR vo = ((JDBCFRServiceSearch)this.getFrService()).get(idFr);
			return FrConverter.toDTO(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
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
	 * Recupera l'Fr identificato dalla chiave logica
	 * 
	 * @param idFr
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	
	public Fr getFr(String codDominio, String codFlusso) throws NotFoundException, ServiceException {
		return getFr(codDominio, codFlusso, null, false, false);
	}

	public Fr getFr(String codDominio, String codFlusso, Date dataOraFlusso) throws NotFoundException, ServiceException {
		return getFr(codDominio, codFlusso, dataOraFlusso, null, false);
	}

	public Fr getFr(String codDominio, String codFlusso, Date dataOraFlusso, Boolean obsoleto) throws NotFoundException, ServiceException {
		return getFr(codDominio, codFlusso, dataOraFlusso, obsoleto, false);
	}

	public Fr getFr(String codDominio, String codFlusso, Date dataOraFlusso, Boolean obsoleto, boolean ricercaCaseInsensitive) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			FR vo = null;
			IExpression expr = this.getFrService().newExpression();


			if(ricercaCaseInsensitive) {
				expr.ilike(FR.model().COD_FLUSSO, codFlusso, LikeMode.EXACT);
			} else {
				expr.equals(FR.model().COD_FLUSSO, codFlusso);
			}

			if(obsoleto != null) {
				expr.equals(FR.model().OBSOLETO, obsoleto);
			}
			
			if(codDominio != null) {
				expr.equals(FR.model().COD_DOMINIO, codDominio);
			}
			
			if(dataOraFlusso != null) {
				expr.equals(FR.model().DATA_ORA_FLUSSO, dataOraFlusso);
			}
			
			vo = this.getFrService().find(expr);

			return FrConverter.toDTO(vo);
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

	public boolean exists(String codDominio, String codFlusso, Date dataOraFlusso) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			IdFr id = new IdFr();
			id.setCodDominio(codDominio);
			id.setCodFlusso(codFlusso);
			id.setDataOraFlusso(dataOraFlusso);
			return this.getFrService().exists(id);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public boolean existsFlussoConDataDiversa(String codDominio, String codFlusso, Date dataOraFlusso) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}


			IExpression expr = this.getFrService().newExpression();
			expr.equals(FR.model().COD_DOMINIO, codDominio);
			expr.equals(FR.model().COD_FLUSSO, codFlusso);
			expr.notEquals(FR.model().DATA_ORA_FLUSSO, dataOraFlusso);

			return this.getFrService().count(expr).longValue() > 0;
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
	 * Inserisce un nuovo fr
	 * 
	 * @param codMsgRicevuta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public void insertFr(Fr fr) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			it.govpay.orm.FR vo = FrConverter.toVO(fr);
			this.getFrService().create(vo);
			fr.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public FrFilter newFilter() throws ServiceException {
		return new FrFilter(this.getFrService());
	}

	public FrFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new FrFilter(this.getFrService(),simpleSearch);
	}

	public long count(FrFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}

	private long _countSenzaLimit(FrFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getFrService());
			}

			return this.getFrService().count(filter.toExpression()).longValue();

		} catch (NotImplementedException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private long _countConLimit(FrFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();

			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());

			FRFieldConverter converter = new FRFieldConverter(this.getJdbcProperties().getDatabase());
			FRModel model = it.govpay.orm.FR.model();
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

			sqlQueryObjectInterno.addFromTable(converter.toTable(model.COD_FLUSSO));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.COD_FLUSSO), "id");
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.DATA_ORA_FLUSSO), "data_ora_flusso");
			sqlQueryObjectInterno.setANDLogicOperator(true);

			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);

			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.DATA_ORA_FLUSSO, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);

			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);

			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count

			List<List<Object>> nativeQuery = this.getFrService().nativeQuery(sql, returnTypes, parameters);

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

	public List<Fr> findAll(FrFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getFrService());
			}

			List<Fr> frLst = new ArrayList<>();
			List<it.govpay.orm.FR> frVOLst = this.getFrService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.FR frVO: frVOLst) {
				frLst.add(FrConverter.toDTO(frVO));
			}
			return frLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<Fr> findAllNoXml(FrFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getFrService());
			}

			FRFieldConverter converter = new FRFieldConverter(this.getJdbcProperties().getDatabase());
			FRModel model = it.govpay.orm.FR.model();

			List<IField> fields = new ArrayList<>();
			fields.add(new CustomField("id", Long.class, "id", converter.toTable(model)));
			fields.add(model.COD_FLUSSO);
			fields.add(model.COD_PSP);
			fields.add(model.COD_DOMINIO);
			fields.add(model.STATO);
			if(!this.getJdbcProperties().getDatabase().equals(TipiDatabase.ORACLE))
				fields.add(model.DESCRIZIONE_STATO);
			fields.add(model.DATA_ACQUISIZIONE);
			fields.add(model.DATA_ORA_FLUSSO);
			fields.add(model.DATA_REGOLAMENTO);
			fields.add(model.NUMERO_PAGAMENTI);
			fields.add(model.IMPORTO_TOTALE_PAGAMENTI);
			//			fields.add(model.XML);
			fields.add(model.IUR);
			fields.add(model.COD_BIC_RIVERSAMENTO);
			fields.add(model.RAGIONE_SOCIALE_DOMINIO);
			fields.add(model.RAGIONE_SOCIALE_PSP);
			fields.add(model.OBSOLETO);
			fields.add(new CustomField("id_incasso", Long.class, "id_incasso", converter.toTable(model)));

			List<Map<String, Object>> returnMap = this.getFrService().select(filter.toPaginatedExpression(), fields.toArray(new IField[fields.size()]));

			FRFetch fetch = new FRFetch();
			List<it.govpay.orm.FR> frVOLst = new ArrayList<>();
			for (Map<String, Object> map : returnMap) {
                
                Long idIncasso = null;
                
                Object idIncassoObj = map.remove("id_incasso");

                if(idIncassoObj instanceof Long)
                        idIncasso = (Long) idIncassoObj;
                
                
                FR fr = (FR) fetch.fetch(ConnectionManager.getJDBCServiceManagerProperties().getDatabase(), FR.model(), map);
                
                if(idIncasso != null) {
                    it.govpay.orm.IdIncasso id_pagamento_incasso = new it.govpay.orm.IdIncasso();
                    id_pagamento_incasso.setId(idIncasso);
                    fr.setIdIncasso(id_pagamento_incasso);
                }
                
                frVOLst.add(fr);
            }
			List<Fr> frLst = new ArrayList<>();
			for(it.govpay.orm.FR frVO: frVOLst) {
				frLst.add(FrConverter.toDTO(frVO));
			}
			return frLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return new ArrayList<>();
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public void updateIdIncasso(long idFr, long idIncasso) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			IdFr idVO = new IdFr();
			idVO.setId(idFr);

			List<UpdateField> lstUpdateFields = new ArrayList<>();
			FRFieldConverter fieldConverter = new FRFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField cfIdIncasso = new CustomField("id_incasso", Long.class, "id_incasso", fieldConverter.toTable(FR.model()));
			lstUpdateFields.add(new UpdateField(cfIdIncasso, idIncasso));

			this.getFrService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public void updateObsoleto(long idFr, Boolean obsoleto) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			IdFr idVO = new IdFr();
			idVO.setId(idFr);

			List<UpdateField> lstUpdateFields = new ArrayList<>();
			lstUpdateFields.add(new UpdateField(FR.model().OBSOLETO, obsoleto));

			this.getFrService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public void updateObsoleto(String codDominio, String codFlusso, Boolean obsoleto) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			FRFieldConverter fieldConverter = new FRFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField cfId = new CustomField("id", Long.class, "id", fieldConverter.toTable(FR.model()));
			IExpression expr = this.getFrService().newExpression();
			expr.equals(FR.model().COD_FLUSSO, codFlusso);
			expr.equals(FR.model().COD_DOMINIO, codDominio);
			IPaginatedExpression pagExpr = this.getFrService().toPaginatedExpression(expr );
			pagExpr.addOrder(FR.model().COD_DOMINIO, SortOrder.ASC);
			pagExpr.addOrder(FR.model().COD_FLUSSO, SortOrder.ASC);
			List<Object> select = this.getFrService().select(pagExpr , cfId); 

			for (Object object : select) {
				Long idFr = (Long) object;

				IdFr idVO = new IdFr();
				idVO.setId(idFr);

				List<UpdateField> lstUpdateFields = new ArrayList<>();
				lstUpdateFields.add(new UpdateField(FR.model().OBSOLETO, obsoleto));

				this.getFrService().updateFields(idVO, lstUpdateFields.toArray(new UpdateField[]{}));
			}
		} catch (NotImplementedException | ExpressionException | ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public List<Long> getIdsFlusso(String codDominio, String codFlusso) throws ServiceException {
		List<Long> ids = new ArrayList<Long>();
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			FRFieldConverter fieldConverter = new FRFieldConverter(this.getJdbcProperties().getDatabase());
			CustomField cfId = new CustomField("id", Long.class, "id", fieldConverter.toTable(FR.model()));
			IExpression expr = this.getFrService().newExpression();
			expr.equals(FR.model().COD_FLUSSO, codFlusso);
			expr.equals(FR.model().COD_DOMINIO, codDominio);
			IPaginatedExpression pagExpr = this.getFrService().toPaginatedExpression(expr );
			pagExpr.addOrder(FR.model().COD_DOMINIO, SortOrder.ASC);
			pagExpr.addOrder(FR.model().COD_FLUSSO, SortOrder.ASC);
			List<Object> select = this.getFrService().select(pagExpr , cfId); 

			for (Object object : select) {
				ids.add((Long) object);
			}
		} catch (NotImplementedException | ExpressionException | ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
		return ids;
	}

	public List<Fr> ricercaFrDominio(String codDominio, Date dataAcquisizioneDa, Date dataAcquisizioneA, List<String> listaTipiPendenza, Integer offset, Integer limit) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}

			IExpression exp = this.getFrService().newExpression();
			exp.equals(FR.model().COD_DOMINIO, codDominio).and();
			if(dataAcquisizioneDa != null) {
				exp.greaterEquals(FR.model().DATA_ACQUISIZIONE, dataAcquisizioneDa);
			}
			exp.lessEquals(FR.model().DATA_ACQUISIZIONE, dataAcquisizioneA);
			exp.equals(FR.model().STATO, StatoFr.ACCETTATA.toString());
			if(listaTipiPendenza != null && !listaTipiPendenza.isEmpty()) {
				listaTipiPendenza.removeAll(Collections.singleton(null));
				exp.in(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, listaTipiPendenza);
			}

			IPaginatedExpression pagExp = this.getFrService().toPaginatedExpression(exp);
			pagExp.offset(offset).limit(limit);
			pagExp.addOrder(FR.model().DATA_ACQUISIZIONE, SortOrder.ASC);

			List<Fr> frLst = new ArrayList<>();
			List<it.govpay.orm.FR> frVOLst = this.getFrService().findAll(pagExp); 
			for(it.govpay.orm.FR frVO: frVOLst) {
				frLst.add(FrConverter.toDTO(frVO));
			}
			return frLst;
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
