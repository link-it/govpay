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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.converter.FrConverter;
import it.govpay.bd.nativequeries.NativeQueries;
import it.govpay.bd.pagamento.filters.FrFilter;
import it.govpay.model.Fr.StatoFr;
import it.govpay.orm.FR;
import it.govpay.orm.IdFr;
import it.govpay.orm.dao.jdbc.JDBCFRServiceSearch;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;
import it.govpay.orm.dao.jdbc.converter.FRFieldConverter;
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
	 * Recupera l'Fr identificato dalla chiave fisica
	 * 
	 * @param idFr
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public Fr getFrExt(long idFr) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			FrFilter filter = this.newFilter();
			filter.setIdFr(Arrays.asList(idFr));
			List<Fr> lstFrExt = this.findAllExt(filter);
			if(lstFrExt.isEmpty()) {
				throw new NotFoundException("id ["+idFr+"]");
			}
			return lstFrExt.get(0);
		} catch (NotFoundException e) {
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
	public Fr getFr(String codFlusso) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression expr = this.getFrService().newExpression();
			expr.equals(FR.model().COD_FLUSSO, codFlusso);
			FR vo = this.getFrService().find(expr );
			
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
	
	public boolean exists(String codFlusso) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdFr id = new IdFr();
			id.setCodFlusso(codFlusso);
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

	/**
	 * Inserisce un nuovo fr
	 * 
	 * @param codMsgRicevuta
	 * @return
	 * @throws NotFoundException
	 * @throws MultipleResultException
	 * @throws ServiceException
	 */
	public void insertFr(Fr fr) throws ServiceException, NotFoundException {
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
	
	private static Logger log = LoggerWrapperFactory.getLogger(JDBCServiceManager.class);

	public long countExt(FrFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			List<Class<?>> lstReturnType = new ArrayList<>();
			lstReturnType.add(Long.class);
			String nativeCount = NativeQueries.getInstance().getFrCountQuery();
			log.info("NATIVE: "+ nativeCount);
			
			String sqlFilterString = filter.getSQLFilterString(nativeCount);
			log.info("NATIVE filtered: "+ sqlFilterString);
			Object[] fields = filter.getFields(true).toArray(new Object[]{});
			List<List<Object>> count = this.getFrService().nativeQuery(sqlFilterString, lstReturnType, fields);
			
			if(count.size() > 0) {
				return ((Long) count.get(0).get(0)).longValue();
			}
			
			return 0;
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}

	}

	public List<Fr> findAllExt(FrFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			List<Class<?>> lstReturnType = new ArrayList<>();

			lstReturnType.add(FR.model().COD_FLUSSO.getFieldType());
			lstReturnType.add(FR.model().STATO.getFieldType());
			lstReturnType.add(FR.model().DESCRIZIONE_STATO.getFieldType());
			lstReturnType.add(FR.model().IUR.getFieldType());
			lstReturnType.add(FR.model().DATA_ORA_FLUSSO.getFieldType());
			lstReturnType.add(FR.model().DATA_REGOLAMENTO.getFieldType());
			lstReturnType.add(FR.model().DATA_ACQUISIZIONE.getFieldType());
			lstReturnType.add(FR.model().NUMERO_PAGAMENTI.getFieldType());
			lstReturnType.add(FR.model().IMPORTO_TOTALE_PAGAMENTI.getFieldType());
			lstReturnType.add(FR.model().COD_BIC_RIVERSAMENTO.getFieldType());
			lstReturnType.add(FR.model().XML.getFieldType());
			lstReturnType.add(Long.class); //id
			lstReturnType.add(FR.model().COD_PSP.getFieldType());
			lstReturnType.add(FR.model().COD_DOMINIO.getFieldType());
			lstReturnType.add(Long.class); //count rendicontazioni ok
			lstReturnType.add(Long.class); //count rendicontazioni anomale
			lstReturnType.add(Long.class); //count rendicontazioni altro intermediario


			String initialNativeQuery = NativeQueries.getInstance().getFrQuery();
			String nativeQueryString = filter.getSQLFilterString(initialNativeQuery);

			Object[] array = filter.getFields(false).toArray(new Object[]{});
			List<List<Object>> lstRecords = this.getRendicontazionePagamentoServiceSearch().nativeQuery(nativeQueryString, lstReturnType, array);
			List<Fr> lstFr = new ArrayList<>();

			for(List<Object> record: lstRecords) {
				lstFr.add(this.getFr(record));
			}
			return lstFr;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			return new ArrayList<>();
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private Fr getFr(List<Object> record) {
		int i =0;
		Fr fr = new Fr();
		fr.setCodFlusso((String)record.get(i++));
		fr.setStato(StatoFr.valueOf((String)record.get(i++)));
		fr.setDescrizioneStato((String)record.get(i++));
		fr.setIur((String)record.get(i++));
		fr.setDataFlusso((Date)record.get(i++));
		fr.setDataRegolamento((Date)record.get(i++));
		fr.setDataAcquisizione((Date)record.get(i++));
		fr.setNumeroPagamenti((Long)record.get(i++));
		fr.setImportoTotalePagamenti(BigDecimal.valueOf((Double)record.get(i++)));
		fr.setCodBicRiversamento((String)record.get(i++));
		fr.setXml((byte[])record.get(i++));
		fr.setId((Long)record.get(i++));
		fr.setCodPsp((String)record.get(i++));
		fr.setCodDominio((String)record.get(i++));
		fr.setNumOk((Long)record.get(i++));
		fr.setNumAnomale((Long)record.get(i++));
		fr.setNumAltroIntermediario((Long)record.get(i++));
		return fr;
	}
	

	public long count(FrFilter filter) throws ServiceException {
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
}
