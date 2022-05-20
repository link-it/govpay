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

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBC_SQLObjectFactory;
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
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.model.converter.TracciatoConverter;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;
import it.govpay.orm.IdTracciato;
import it.govpay.orm.dao.jdbc.JDBCOperatoreServiceSearch;
import it.govpay.orm.dao.jdbc.converter.TracciatoFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.TracciatoFetch;
import it.govpay.orm.model.TracciatoModel;

public class TracciatiBD extends BasicBD {

	public TracciatiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public TracciatiBD(String idTransaction) {
		super(idTransaction);
	}
	
	public TracciatiBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public TracciatiBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	/**
	 * Aggiorna l'intermediari con i dati forniti
	 * @param intermediari
	 * @throws NotPermittedException se si inserisce un intermediari gia' censito
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertTracciato(Tracciato tracciato) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Tracciato vo = TracciatoConverter.toVO(tracciato);

			this.getTracciatoService().create(vo);
			tracciato.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}

	}


	public TracciatoFilter newFilter() {
		return new TracciatoFilter(this.getTracciatoService());
	}

	public TracciatoFilter newFilter(boolean simpleSearch) {
		return new TracciatoFilter(this.getTracciatoService(),simpleSearch);
	}
	
	public long count(TracciatoFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(TracciatoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getTracciatoService());
			}
			
			return this.getTracciatoService().count(filter.toExpression()).longValue();
	
		} catch (NotImplementedException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private long _countConLimit(TracciatoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();

			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());

			TracciatoFieldConverter converter = new TracciatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			TracciatoModel model = it.govpay.orm.Tracciato.model();
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

			List<List<Object>> nativeQuery = this.getTracciatoService().nativeQuery(sql, returnTypes, parameters);

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

	public List<Tracciato> findAll(TracciatoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getTracciatoService());
			}
			
			TracciatoFieldConverter converter = new TracciatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			TracciatoModel model = it.govpay.orm.Tracciato.model();
			TracciatoFetch tracciatoFetch = new TracciatoFetch();
			org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID;

			List<Tracciato> lst = new ArrayList<>();
			List<it.govpay.orm.Tracciato> lstTracciatoVO = new ArrayList<>();
			List<IField> fields = getListaFieldsRicerca(filter.isIncludiRawRichiesta(), filter.isIncludiRawEsito(), filter.isIncludiZipStampe(), converter, model);

			IPaginatedExpression pagExpr = filter.toPaginatedExpression();

			eseguiRicerca(model, tracciatoFetch, idMappingResolutionBehaviour, lstTracciatoVO, fields, pagExpr);

			//			List<it.govpay.orm.Tracciato> lstTracciatoVO = this.getTracciatoService().findAll(filter.toPaginatedExpression());
			for(it.govpay.orm.Tracciato tracciatoVO: lstTracciatoVO) {
				lst.add(TracciatoConverter.toDTO(tracciatoVO));
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
//			this.getTracciatoService().update(this.getTracciatoService().convertToId(vo), vo);
//		} catch (NotImplementedException e) {
//			throw new ServiceException(e);
//		} catch (NotFoundException e) {
//			throw new ServiceException(e);
//		}
//	}
	
	public void updateBeanDati(Tracciato tracciato) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdTracciato convertToId = this.getTracciatoService().convertToId(TracciatoConverter.toVO(tracciato));
			this.updateBeanDati(convertToId, tracciato.getBeanDati());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	public void updateBeanDati(Tracciato tracciato, String beanDati) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdTracciato convertToId = this.getTracciatoService().convertToId(TracciatoConverter.toVO(tracciato));
			this.updateBeanDati(convertToId, beanDati);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private void updateBeanDati(IdTracciato idTracciato, String beanDati) throws ServiceException {
		try {
			this.getTracciatoService().updateFields(idTracciato, new UpdateField(it.govpay.orm.Tracciato.model().BEAN_DATI, beanDati));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}

	public void updateZipStampe(Tracciato tracciato, byte[] zipStampe) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdTracciato convertToId = this.getTracciatoService().convertToId(TracciatoConverter.toVO(tracciato));
			this.updateZipStampe(convertToId, zipStampe);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private void updateZipStampe(IdTracciato idTracciato, byte[] zipStampe) throws ServiceException {
		try {
			this.getTracciatoService().updateFields(idTracciato, new UpdateField(it.govpay.orm.Tracciato.model().ZIP_STAMPE, zipStampe));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}

	public void updateFineElaborazione(Tracciato tracciato) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdTracciato convertToId = this.getTracciatoService().convertToId(TracciatoConverter.toVO(tracciato));

			//			log.info("aggiorno bean dati del tracciato: %s" , convertToId.getId());
			List<UpdateField> listaUpdateFields = new ArrayList<>();
			listaUpdateFields.add(new UpdateField(it.govpay.orm.Tracciato.model().BEAN_DATI, tracciato.getBeanDati()));
			listaUpdateFields.add(new UpdateField(it.govpay.orm.Tracciato.model().FILE_NAME_ESITO, tracciato.getFileNameEsito()));
			listaUpdateFields.add(new UpdateField(it.govpay.orm.Tracciato.model().RAW_ESITO, tracciato.getRawEsito()));
			listaUpdateFields.add(new UpdateField(it.govpay.orm.Tracciato.model().STATO, tracciato.getStato().name()));
			listaUpdateFields.add(new UpdateField(it.govpay.orm.Tracciato.model().DESCRIZIONE_STATO, tracciato.getDescrizioneStato()));
			listaUpdateFields.add(new UpdateField(it.govpay.orm.Tracciato.model().DATA_COMPLETAMENTO, tracciato.getDataCompletamento()));

			this.getTracciatoService().updateFields(convertToId, listaUpdateFields.toArray(new UpdateField[listaUpdateFields.size()]));
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

	public void updateFineElaborazioneStampe(Tracciato tracciato) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IdTracciato convertToId = this.getTracciatoService().convertToId(TracciatoConverter.toVO(tracciato));

			//			log.info("aggiorno bean dati del tracciato: %s" , convertToId.getId());
			List<UpdateField> listaUpdateFields = new ArrayList<>();
			listaUpdateFields.add(new UpdateField(it.govpay.orm.Tracciato.model().BEAN_DATI, tracciato.getBeanDati()));
			listaUpdateFields.add(new UpdateField(it.govpay.orm.Tracciato.model().STATO, tracciato.getStato().name()));
			listaUpdateFields.add(new UpdateField(it.govpay.orm.Tracciato.model().DESCRIZIONE_STATO, tracciato.getDescrizioneStato()));
			listaUpdateFields.add(new UpdateField(it.govpay.orm.Tracciato.model().DATA_COMPLETAMENTO, tracciato.getDataCompletamento()));
			listaUpdateFields.add(new UpdateField(it.govpay.orm.Tracciato.model().ZIP_STAMPE, tracciato.getZipStampe()));

			this.getTracciatoService().updateFields(convertToId, listaUpdateFields.toArray(new UpdateField[listaUpdateFields.size()]));
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

	public void updateFineElaborazioneStampeOid(Tracciato tracciato, long oid) throws ServiceException {
		PreparedStatement prepareStatement = null;

		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			JDBC_SQLObjectFactory jdbcSqlObjectFactory = new JDBC_SQLObjectFactory();
			ISQLQueryObject sqlQueryObject = jdbcSqlObjectFactory.createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());

			TracciatoFieldConverter converter = new TracciatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			TracciatoModel model = it.govpay.orm.Tracciato.model();

			sqlQueryObject.addUpdateTable(converter.toTable(model.STATO));
			sqlQueryObject.addUpdateField(converter.toColumn(model.STATO, false), "?");
			sqlQueryObject.addUpdateField(converter.toColumn(model.BEAN_DATI, false), "?");
			sqlQueryObject.addUpdateField(converter.toColumn(model.DESCRIZIONE_STATO, false), "?");
			if(tracciato.getDataCompletamento() != null)
				sqlQueryObject.addUpdateField(converter.toColumn(model.DATA_COMPLETAMENTO, false), "?");
			sqlQueryObject.addUpdateField(converter.toColumn(model.ZIP_STAMPE, false), "?");
			sqlQueryObject.addWhereCondition(true, converter.toTable(model.STATO, true) + ".id" + " = ? ");

			String sql = sqlQueryObject.createSQLUpdate();

			prepareStatement = this.getConnection().prepareStatement(sql);

			int idx = 1;
			prepareStatement.setString(idx ++, tracciato.getStato().name());
			prepareStatement.setString(idx ++, tracciato.getBeanDati());
			prepareStatement.setString(idx ++, tracciato.getDescrizioneStato());
			if(tracciato.getDataCompletamento() != null)
				prepareStatement.setTimestamp(idx ++, new Timestamp(tracciato.getDataCompletamento().getTime()));
			prepareStatement.setLong(idx ++, oid);
			prepareStatement.setLong(idx ++, tracciato.getId());

			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			throw new ServiceException(e);
		} catch (SQLQueryObjectException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			try {
				if(prepareStatement != null)
					prepareStatement.close();
			} catch (SQLException e) { }
			
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public void updateFineElaborazioneStampeBlob(Tracciato tracciato, Blob blob) throws ServiceException {
		PreparedStatement prepareStatement = null;

		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			JDBC_SQLObjectFactory jdbcSqlObjectFactory = new JDBC_SQLObjectFactory();
			ISQLQueryObject sqlQueryObject = jdbcSqlObjectFactory.createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());

			TracciatoFieldConverter converter = new TracciatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			TracciatoModel model = it.govpay.orm.Tracciato.model();

			sqlQueryObject.addUpdateTable(converter.toTable(model.STATO));
			sqlQueryObject.addUpdateField(converter.toColumn(model.STATO, false), "?");
			sqlQueryObject.addUpdateField(converter.toColumn(model.BEAN_DATI, false), "?");
			sqlQueryObject.addUpdateField(converter.toColumn(model.DESCRIZIONE_STATO, false), "?");
			if(tracciato.getDataCompletamento() != null)
				sqlQueryObject.addUpdateField(converter.toColumn(model.DATA_COMPLETAMENTO, false), "?");
			sqlQueryObject.addUpdateField(converter.toColumn(model.ZIP_STAMPE, false), "?");
			sqlQueryObject.addWhereCondition(true, converter.toTable(model.STATO, true) + ".id" + " = ? ");

			String sql = sqlQueryObject.createSQLUpdate();

			prepareStatement = this.getConnection().prepareStatement(sql);

			int idx = 1;
			prepareStatement.setString(idx ++, tracciato.getStato().name());
			prepareStatement.setString(idx ++, tracciato.getBeanDati());
			prepareStatement.setString(idx ++, tracciato.getDescrizioneStato());
			if(tracciato.getDataCompletamento() != null)
				prepareStatement.setTimestamp(idx ++, new Timestamp(tracciato.getDataCompletamento().getTime()));
			prepareStatement.setBlob(idx ++, blob);
			prepareStatement.setLong(idx ++, tracciato.getId());

			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			throw new ServiceException(e);
		} catch (SQLQueryObjectException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			try {
				if(prepareStatement != null)
					prepareStatement.close();
			} catch (SQLException e) { }

			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

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
	public Tracciato getTracciato(Long idTracciato, boolean includiRawRichiesta, boolean includiRawEsito, boolean includiZipStampe) throws NotFoundException, ServiceException, MultipleResultException {
		if(idTracciato == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}
		
		List<it.govpay.orm.Tracciato> list = new ArrayList<>();
		try{
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
	
			TracciatoFieldConverter converter = new TracciatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			TracciatoModel model = it.govpay.orm.Tracciato.model();
			TracciatoFetch tracciatoFetch = new TracciatoFetch();
			org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID;


			List<IField> fields = getListaFieldsRicerca(includiRawRichiesta, includiRawEsito, includiZipStampe, converter, model);

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

		return TracciatoConverter.toDTO(list.get(0));
	}

	private void eseguiRicerca(TracciatoModel model, TracciatoFetch tracciatoFetch,
			org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour,
			List<it.govpay.orm.Tracciato> list, List<IField> fields, IPaginatedExpression pagExpr)
					throws ServiceException, NotImplementedException {
		try{
			List<Map<String, Object>> returnMap =  this.getTracciatoService().select(pagExpr, false, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				Object idOperatoreObj = map.remove("id_operatore");

				it.govpay.orm.Tracciato tracciato = (it.govpay.orm.Tracciato) tracciatoFetch.fetch(ConnectionManager.getJDBCServiceManagerProperties().getDatabase(), model, map);


				if(idOperatoreObj instanceof Long) {
					Long idOperatore = (Long) idOperatoreObj;
					it.govpay.orm.IdOperatore id_tracciato_operatore = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_tracciato_operatore = ((JDBCOperatoreServiceSearch)(this.getOperatoreService())).findId(idOperatore, false);
					}else{
						id_tracciato_operatore = new it.govpay.orm.IdOperatore();
					}
					id_tracciato_operatore.setId(idOperatore);
					tracciato.setIdOperatore(id_tracciato_operatore);
				}

				list.add(tracciato);
			}
		} catch(NotFoundException e) {}
	}

	private IPaginatedExpression getFiltriRicerca(Long idTracciato, TracciatoFieldConverter converter, TracciatoModel model)
			throws ServiceException, NotImplementedException, ExpressionException, ExpressionNotImplementedException {
		IExpression expr = this.getTracciatoService().newExpression();
		CustomField idTracciatoCustomField = new CustomField("id", Long.class, "id", converter.toTable(model));
		expr.equals(idTracciatoCustomField, idTracciato);

		IPaginatedExpression pagExpr = this.getTracciatoService().toPaginatedExpression(expr);
		return pagExpr;
	}

	private List<IField> getListaFieldsRicerca(boolean includiRawRichiesta, boolean includiRawEsito, boolean includiZipStampe,
			TracciatoFieldConverter converter, TracciatoModel model) throws ExpressionException {
		List<IField> fields = new ArrayList<>();
		fields.add(new CustomField("id", Long.class, "id", converter.toTable(model)));
		fields.add(model.COD_DOMINIO);
		fields.add(model.COD_TIPO_VERSAMENTO);
		fields.add(model.FORMATO);
		fields.add(model.TIPO);
		fields.add(model.STATO);
		fields.add(model.DESCRIZIONE_STATO);
		fields.add(model.DATA_CARICAMENTO);
		fields.add(model.DATA_COMPLETAMENTO);
		fields.add(model.BEAN_DATI);
		fields.add(model.FILE_NAME_RICHIESTA);
		if(includiRawRichiesta) {
			fields.add(model.RAW_RICHIESTA);
		}
		fields.add(model.FILE_NAME_ESITO);
		if(includiRawEsito) {
			fields.add(model.RAW_ESITO);	
		}
//		if(includiZipStampe) {
//			fields.add(model.ZIP_STAMPE);
//		}
		fields.add(new CustomField("id_operatore", Long.class, "id_operatore", converter.toTable(model)));
		return fields;
	}
	
	
	public long countTracciatiDaElaborare() throws ServiceException {
		TracciatoFilter filter = this.newFilter();
		filter.setTipo(Arrays.asList(TIPO_TRACCIATO.PENDENZA));
		filter.setStato(STATO_ELABORAZIONE.ELABORAZIONE);
		
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
}

