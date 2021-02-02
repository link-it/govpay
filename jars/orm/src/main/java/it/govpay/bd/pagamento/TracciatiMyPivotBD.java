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
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.TracciatoMyPivot;
import it.govpay.bd.model.converter.TracciatoMyPivotConverter;
import it.govpay.bd.pagamento.filters.TracciatoMyPivotFilter;
import it.govpay.orm.IdTracciatoMyPivot;
import it.govpay.orm.dao.jdbc.JDBCDominioServiceSearch;
import it.govpay.orm.dao.jdbc.converter.TracciatoMyPivotFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.TracciatoMyPivotFetch;
import it.govpay.orm.model.TracciatoMyPivotModel;

public class TracciatiMyPivotBD extends BasicBD {

	public TracciatiMyPivotBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public TracciatiMyPivotBD(String idTransaction) {
		super(idTransaction);
	}
	
	public TracciatiMyPivotBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public TracciatiMyPivotBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	/**
	 * Aggiorna l'intermediari con i dati forniti
	 * @param intermediari
	 * @throws NotPermittedException se si inserisce un intermediari gia' censito
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertTracciato(TracciatoMyPivot tracciato) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.TracciatoMyPivot vo = TracciatoMyPivotConverter.toVO(tracciato);

			this.getTracciatoMyPivotService().create(vo);
			tracciato.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}

	}


	public TracciatoMyPivotFilter newFilter() {
		return new TracciatoMyPivotFilter(this.getTracciatoMyPivotService());
	}

	public TracciatoMyPivotFilter newFilter(boolean simpleSearch) {
		return new TracciatoMyPivotFilter(this.getTracciatoMyPivotService(),simpleSearch);
	}
	
	public long count(TracciatoMyPivotFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(TracciatoMyPivotFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getTracciatoMyPivotService());
			}
			
			return this.getTracciatoMyPivotService().count(filter.toExpression()).longValue();
	
		} catch (NotImplementedException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private long _countConLimit(TracciatoMyPivotFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();

			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());

			TracciatoMyPivotFieldConverter converter = new TracciatoMyPivotFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			TracciatoMyPivotModel model = it.govpay.orm.TracciatoMyPivot.model();
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
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.DATA_CARICAMENTO), "data_caricamento");
			sqlQueryObjectInterno.setANDLogicOperator(true);

			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);

			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.DATA_CARICAMENTO, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);

			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);

			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count

			List<List<Object>> nativeQuery = this.getTracciatoMyPivotService().nativeQuery(sql, returnTypes, parameters);

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

	public List<TracciatoMyPivot> findAll(TracciatoMyPivotFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getTracciatoMyPivotService());
			}
			
			TracciatoMyPivotFieldConverter converter = new TracciatoMyPivotFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			TracciatoMyPivotModel model = it.govpay.orm.TracciatoMyPivot.model();
			TracciatoMyPivotFetch tracciatoFetch = new TracciatoMyPivotFetch();
			org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID;

			List<TracciatoMyPivot> lst = new ArrayList<>();
			List<it.govpay.orm.TracciatoMyPivot> lstTracciatoVO = new ArrayList<>();
			List<IField> fields = getListaFieldsRicerca(filter.isIncludiRawContenuto(), converter, model);

			IPaginatedExpression pagExpr = filter.toPaginatedExpression();

			eseguiRicerca(model, tracciatoFetch, idMappingResolutionBehaviour, lstTracciatoVO, fields, pagExpr);

			//			List<it.govpay.orm.Tracciato> lstTracciatoVO = this.getTracciatoMyPivotService().findAll(filter.toPaginatedExpression());
			for(it.govpay.orm.TracciatoMyPivot tracciatoVO: lstTracciatoVO) {
				lst.add(TracciatoMyPivotConverter.toDTO(tracciatoVO));
			}
			return lst;
		}catch(ServiceException e) {
			throw e;
		} catch (NotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

//	public void update(Tracciato tracciato) throws ServiceException {
//		try {
//			it.govpay.orm.Tracciato vo = TracciatoConverter.toVO(tracciato);
//			this.getTracciatoMyPivotService().update(this.getTracciatoMyPivotService().convertToId(vo), vo);
//		} catch (NotImplementedException e) {
//			throw new ServiceException(e);
//		} catch (NotFoundException e) {
//			throw new ServiceException(e);
//		}
//	}
	
	public void updateBeanDati(TracciatoMyPivot tracciato) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdTracciatoMyPivot convertToId = this.getTracciatoMyPivotService().convertToId(TracciatoMyPivotConverter.toVO(tracciato));
			this.updateBeanDati(convertToId, tracciato.getBeanDati());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public void updateBeanDati(TracciatoMyPivot tracciato, String beanDati) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdTracciatoMyPivot convertToId = this.getTracciatoMyPivotService().convertToId(TracciatoMyPivotConverter.toVO(tracciato));
			this.updateBeanDati(convertToId, beanDati);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private void updateBeanDati(IdTracciatoMyPivot idTracciato, String beanDati) throws ServiceException {
		try {
			this.getTracciatoMyPivotService().updateFields(idTracciato, new UpdateField(it.govpay.orm.TracciatoMyPivot.model().BEAN_DATI, beanDati));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}

	public void updateZipContenuto(TracciatoMyPivot tracciato, byte[] zipContenuto) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdTracciatoMyPivot convertToId = this.getTracciatoMyPivotService().convertToId(TracciatoMyPivotConverter.toVO(tracciato));
			this.updateZipContenuto(convertToId, zipContenuto);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private void updateZipContenuto(IdTracciatoMyPivot idTracciato, byte[] zipContenuto) throws ServiceException {
		try {
			this.getTracciatoMyPivotService().updateFields(idTracciato, new UpdateField(it.govpay.orm.TracciatoMyPivot.model().RAW_CONTENUTO, zipContenuto));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}

	public void updateFineElaborazione(TracciatoMyPivot tracciato) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdTracciatoMyPivot convertToId = this.getTracciatoMyPivotService().convertToId(TracciatoMyPivotConverter.toVO(tracciato));

			//			log.info("aggiorno bean dati del tracciato: %s" , convertToId.getId());
			List<UpdateField> listaUpdateFields = new ArrayList<>();
			listaUpdateFields.add(new UpdateField(it.govpay.orm.Tracciato.model().BEAN_DATI, tracciato.getBeanDati()));
			listaUpdateFields.add(new UpdateField(it.govpay.orm.Tracciato.model().STATO, tracciato.getStato().name()));
//			listaUpdateFields.add(new UpdateField(it.govpay.orm.Tracciato.model().DESCRIZIONE_STATO, tracciato.getDescrizioneStato()));
			listaUpdateFields.add(new UpdateField(it.govpay.orm.Tracciato.model().DATA_COMPLETAMENTO, tracciato.getDataCompletamento()));

			this.getTracciatoMyPivotService().updateFields(convertToId, listaUpdateFields.toArray(new UpdateField[listaUpdateFields.size()]));
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

//	public void updateFineElaborazioneStampeOid(Tracciato tracciato, long oid) throws ServiceException {
//		PreparedStatement prepareStatement = null;
//
//		try {
//			if(this.isAtomica()) {
//				this.setupConnection(this.getIdTransaction());
//			}
//			
//			JDBC_SQLObjectFactory jdbcSqlObjectFactory = new JDBC_SQLObjectFactory();
//			ISQLQueryObject sqlQueryObject = jdbcSqlObjectFactory.createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
//
//			TracciatoFieldConverter converter = new TracciatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
//			TracciatoModel model = it.govpay.orm.Tracciato.model();
//
//			sqlQueryObject.addUpdateTable(converter.toTable(model.STATO));
//			sqlQueryObject.addUpdateField(converter.toColumn(model.STATO, false), "?");
//			sqlQueryObject.addUpdateField(converter.toColumn(model.BEAN_DATI, false), "?");
//			sqlQueryObject.addUpdateField(converter.toColumn(model.DESCRIZIONE_STATO, false), "?");
//			if(tracciato.getDataCompletamento() != null)
//				sqlQueryObject.addUpdateField(converter.toColumn(model.DATA_COMPLETAMENTO, false), "?");
//			sqlQueryObject.addUpdateField(converter.toColumn(model.ZIP_STAMPE, false), "?");
//			sqlQueryObject.addWhereCondition(true, converter.toTable(model.STATO, true) + ".id" + " = ? ");
//
//			String sql = sqlQueryObject.createSQLUpdate();
//
//			prepareStatement = this.getConnection().prepareStatement(sql);
//
//			int idx = 1;
//			prepareStatement.setString(idx ++, tracciato.getStato().name());
//			prepareStatement.setString(idx ++, tracciato.getBeanDati());
//			prepareStatement.setString(idx ++, tracciato.getDescrizioneStato());
//			if(tracciato.getDataCompletamento() != null)
//				prepareStatement.setTimestamp(idx ++, new Timestamp(tracciato.getDataCompletamento().getTime()));
//			prepareStatement.setLong(idx ++, oid);
//			prepareStatement.setLong(idx ++, tracciato.getId());
//
//			prepareStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new ServiceException(e);
//		} catch (SQLQueryObjectException e) {
//			throw new ServiceException(e);
//		} catch (ExpressionException e) {
//			throw new ServiceException(e);
//		} finally {
//			try {
//				if(prepareStatement != null)
//					prepareStatement.close();
//			} catch (SQLException e) { }
//			
//			if(this.isAtomica()) {
//				this.closeConnection();
//			}
//		}
//	}
//	
//	public void updateFineElaborazioneStampeBlob(Tracciato tracciato, Blob blob) throws ServiceException {
//		PreparedStatement prepareStatement = null;
//
//		try {
//			if(this.isAtomica()) {
//				this.setupConnection(this.getIdTransaction());
//			}
//			
//			JDBC_SQLObjectFactory jdbcSqlObjectFactory = new JDBC_SQLObjectFactory();
//			ISQLQueryObject sqlQueryObject = jdbcSqlObjectFactory.createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
//
//			TracciatoFieldConverter converter = new TracciatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
//			TracciatoModel model = it.govpay.orm.Tracciato.model();
//
//			sqlQueryObject.addUpdateTable(converter.toTable(model.STATO));
//			sqlQueryObject.addUpdateField(converter.toColumn(model.STATO, false), "?");
//			sqlQueryObject.addUpdateField(converter.toColumn(model.BEAN_DATI, false), "?");
//			sqlQueryObject.addUpdateField(converter.toColumn(model.DESCRIZIONE_STATO, false), "?");
//			if(tracciato.getDataCompletamento() != null)
//				sqlQueryObject.addUpdateField(converter.toColumn(model.DATA_COMPLETAMENTO, false), "?");
//			sqlQueryObject.addUpdateField(converter.toColumn(model.ZIP_STAMPE, false), "?");
//			sqlQueryObject.addWhereCondition(true, converter.toTable(model.STATO, true) + ".id" + " = ? ");
//
//			String sql = sqlQueryObject.createSQLUpdate();
//
//			prepareStatement = this.getConnection().prepareStatement(sql);
//
//			int idx = 1;
//			prepareStatement.setString(idx ++, tracciato.getStato().name());
//			prepareStatement.setString(idx ++, tracciato.getBeanDati());
//			prepareStatement.setString(idx ++, tracciato.getDescrizioneStato());
//			if(tracciato.getDataCompletamento() != null)
//				prepareStatement.setTimestamp(idx ++, new Timestamp(tracciato.getDataCompletamento().getTime()));
//			prepareStatement.setBlob(idx ++, blob);
//			prepareStatement.setLong(idx ++, tracciato.getId());
//
//			prepareStatement.executeUpdate();
//		} catch (SQLException e) {
//			throw new ServiceException(e);
//		} catch (SQLQueryObjectException e) {
//			throw new ServiceException(e);
//		} catch (ExpressionException e) {
//			throw new ServiceException(e);
//		} finally {
//			try {
//				if(prepareStatement != null)
//					prepareStatement.close();
//			} catch (SQLException e) { }
//
//			if(this.isAtomica()) {
//				this.closeConnection();
//			}
//		}
//	}

	/**
	 * Recupera l'tracciato tramite l'id fisico
	 * 
	 * @param idTracciato
	 * @param includiRawRichiesta
	 * @param includiRawEsito
	 * @param includiZipStampe
	 * @return
	 * @throws NotFoundException se il tracciato non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public TracciatoMyPivot getTracciato(Long idTracciato, boolean includiRawContenuto) throws NotFoundException, ServiceException, MultipleResultException {
		if(idTracciato == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}
		
		List<it.govpay.orm.TracciatoMyPivot> list = new ArrayList<>();
		try{
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
	
			TracciatoMyPivotFieldConverter converter = new TracciatoMyPivotFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			TracciatoMyPivotModel model = it.govpay.orm.TracciatoMyPivot.model();
			TracciatoMyPivotFetch tracciatoFetch = new TracciatoMyPivotFetch();
			org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID;


			List<IField> fields = getListaFieldsRicerca(includiRawContenuto, converter, model);

			IPaginatedExpression pagExpr = getFiltriRicerca(idTracciato, converter, model);

			eseguiRicerca(model, tracciatoFetch, idMappingResolutionBehaviour, list, fields, pagExpr);


		}catch(ServiceException e) {
			throw e;
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}

		if(list.size() <=0)
			throw new NotFoundException("Nessuna entry corrisponde ai criteri indicati.");

		if(list.size() > 1)
			throw new MultipleResultException("I criteri indicati individuano piu' entry.");

		return TracciatoMyPivotConverter.toDTO(list.get(0));
	}

	private void eseguiRicerca(TracciatoMyPivotModel model, TracciatoMyPivotFetch tracciatoFetch,
			org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour,
			List<it.govpay.orm.TracciatoMyPivot> list, List<IField> fields, IPaginatedExpression pagExpr)
					throws ServiceException, NotImplementedException {
		try{
			List<Map<String, Object>> returnMap =  this.getTracciatoMyPivotService().select(pagExpr, false, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				Object idDominioObj = map.remove("id_dominio");

				it.govpay.orm.TracciatoMyPivot tracciato = (it.govpay.orm.TracciatoMyPivot) tracciatoFetch.fetch(ConnectionManager.getJDBCServiceManagerProperties().getDatabase(), model, map);


				if(idDominioObj instanceof Long) {
					Long idDominio = (Long) idDominioObj;
					it.govpay.orm.IdDominio id_tracciato_dominio = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_tracciato_dominio = ((JDBCDominioServiceSearch)(this.getDominioService())).findId(idDominio, false);
					}else{
						id_tracciato_dominio = new it.govpay.orm.IdDominio();
					}
					id_tracciato_dominio.setId(idDominio);
					tracciato.setIdDominio(id_tracciato_dominio);
				}

				list.add(tracciato);
			}
		} catch(NotFoundException e) {}
	}

	private IPaginatedExpression getFiltriRicerca(Long idTracciato, TracciatoMyPivotFieldConverter converter, TracciatoMyPivotModel model)
			throws ServiceException, NotImplementedException, ExpressionException, ExpressionNotImplementedException {
		IExpression expr = this.getTracciatoMyPivotService().newExpression();
		CustomField idTracciatoCustomField = new CustomField("id", Long.class, "id", converter.toTable(model));
		expr.equals(idTracciatoCustomField, idTracciato);

		IPaginatedExpression pagExpr = this.getTracciatoMyPivotService().toPaginatedExpression(expr);
		return pagExpr;
	}

	private List<IField> getListaFieldsRicerca(boolean includiRawContenuto,
			TracciatoMyPivotFieldConverter converter, TracciatoMyPivotModel model) throws ExpressionException {
		List<IField> fields = new ArrayList<>();
		fields.add(new CustomField("id", Long.class, "id", converter.toTable(model)));
		fields.add(model.AUTHORIZATION_TOKEN);
		fields.add(model.BEAN_DATI);
		fields.add(model.DATA_CARICAMENTO);
		fields.add(model.DATA_COMPLETAMENTO);
		fields.add(model.DATA_CREAZIONE);
		fields.add(model.DATA_RT_A);
		fields.add(model.DATA_RT_DA);
		fields.add(model.NOME_FILE);
		fields.add(model.REQUEST_TOKEN);
		fields.add(model.STATO);
		fields.add(model.UPLOAD_URL);
		if(includiRawContenuto) {
			fields.add(model.RAW_CONTENUTO);	
		}
		fields.add(new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(model)));
		return fields;
	}
}

