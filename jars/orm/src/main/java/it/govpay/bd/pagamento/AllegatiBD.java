package it.govpay.bd.pagamento;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBC_SQLObjectFactory;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.Allegato;
import it.govpay.bd.model.converter.AllegatoConverter;
import it.govpay.bd.pagamento.filters.AllegatoFilter;
import it.govpay.orm.dao.jdbc.JDBCAllegatoServiceSearch;
import it.govpay.orm.dao.jdbc.JDBCVersamentoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.AllegatoFieldConverter;
import it.govpay.orm.dao.jdbc.fetch.AllegatoFetch;
import it.govpay.orm.model.AllegatoModel;

public class AllegatiBD extends BasicBD {

	public AllegatiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public AllegatiBD(String idTransaction) {
		super(idTransaction);
	}
	
	public AllegatiBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public AllegatiBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	public Allegato getAllegato(long id, boolean includiRawContenuto) throws ServiceException, NotFoundException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Allegato vo = ((JDBCAllegatoServiceSearch)this.getAllegatoService()).get(id);
			return AllegatoConverter.toDTO(vo);
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
	
	public Allegato insertAllegato(Allegato dto) throws ServiceException {
		it.govpay.orm.Allegato vo = AllegatoConverter.toVO(dto);
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			// la insert deve essere fatta in due parti, prima la entry senza contenuto e poi in streaming
			// N.B. la create non inserisce il contenuto
			this.getAllegatoService().create(vo);
			
			// inserimento della colonna contenuto in streaming
			OutputStream oututStreamDestinazione = null;
			Long oid = null;
			Blob blobContenuto = null;
			
			TipiDatabase tipoDatabase = ConnectionManager.getJDBCServiceManagerProperties().getDatabase();
			
			switch (tipoDatabase) {
			case MYSQL:
				try {
					blobContenuto = this.getConnection().createBlob();
					oututStreamDestinazione = blobContenuto.setBinaryStream(1);
				} catch (SQLException e) {
					log.error("Errore durante la creazione del blob: " + e.getMessage(), e);
					throw new ServiceException(e);
				}
				break;
			case ORACLE:
				try {
					blobContenuto = this.getConnection().createBlob();
					oututStreamDestinazione = blobContenuto.setBinaryStream(1);
				} catch (SQLException e) {
					log.error("Errore durante la creazione del blob: " + e.getMessage(), e);
					throw new ServiceException(e);
				}
				break;
			case SQLSERVER:
				try {
					blobContenuto = this.getConnection().createBlob();
					oututStreamDestinazione = blobContenuto.setBinaryStream(1);
				} catch (SQLException e) {
					log.error("Errore durante la creazione del blob: " + e.getMessage(), e);
					throw new ServiceException(e);
				}
				break;
			case POSTGRESQL:
				org.openspcoop2.utils.datasource.Connection wrappedConn = (org.openspcoop2.utils.datasource.Connection) this.getConnection();
				Connection wrappedConnection = wrappedConn.getWrappedConnection();
	
				Connection underlyingConnection = null;
				try {
					Method method = wrappedConnection.getClass().getMethod("getUnderlyingConnection");
	
					Object invoke = method.invoke(wrappedConnection);
	
					underlyingConnection = (Connection) invoke;
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					log.error("Errore durante la lettura dell'oggetto connessione: " + e.getMessage(), e);
					throw new ServiceException(e);
				}
	
				org.postgresql.PGConnection pgConnection = null;
				try {
					if(underlyingConnection.isWrapperFor(org.postgresql.PGConnection.class)) {
						pgConnection = underlyingConnection.unwrap(org.postgresql.PGConnection.class);
					} else {
						pgConnection = (org.postgresql.PGConnection) underlyingConnection;				
					}
	
					// Get the Large Object Manager to perform operations with
					LargeObjectManager lobj = pgConnection.getLargeObjectAPI();
	
					// Create a new large object
					oid = lobj.createLO(LargeObjectManager.WRITE);
	
					// Open the large object for writing
					LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);
	
					oututStreamDestinazione = obj.getOutputStream();
				} catch (SQLException e) {
					log.error("Errore durante la creazione dell'outputstream: " + e.getMessage(), e);
					throw new ServiceException(e);
				}
				break;
			case DB2:
			case DEFAULT:
			case DERBY:
			case HSQL:
			default:
				throw new ServiceException("TipoDatabase ["+tipoDatabase+"] non gestito.");
			}
			
			ByteArrayInputStream bais = new ByteArrayInputStream(dto.getRawContenuto());
			
			
			try {
				IOUtils.copy(bais, oututStreamDestinazione);
				
				oututStreamDestinazione.flush();
				oututStreamDestinazione.close();
			} catch (IOException e) {
				throw new ServiceException(e);
			}
			
			PreparedStatement prepareStatement = null;
			try {
				JDBC_SQLObjectFactory jdbcSqlObjectFactory = new JDBC_SQLObjectFactory();
				ISQLQueryObject sqlQueryObject = jdbcSqlObjectFactory.createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
	
				AllegatoFieldConverter converter = new AllegatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				AllegatoModel model = it.govpay.orm.Allegato.model();
				
				sqlQueryObject.addUpdateTable(converter.toTable(model.NOME));
				sqlQueryObject.addUpdateField(converter.toColumn(model.RAW_CONTENUTO, false), "?");
				sqlQueryObject.addWhereCondition(true, converter.toTable(model.NOME, true) + ".id" + " = ? ");
				
				String sql = sqlQueryObject.createSQLUpdate();
	
				prepareStatement = this.getConnection().prepareStatement(sql);
				int idx = 1;
				
				switch (tipoDatabase) {
				case MYSQL:
				case ORACLE:
				case SQLSERVER:
					prepareStatement.setBlob(idx ++, blobContenuto);
					break;
				case POSTGRESQL:
					prepareStatement.setLong(idx ++, oid);
					break;
				case DB2:
				case DEFAULT:
				case DERBY:
				case HSQL:
				default:
					throw new ServiceException("TipoDatabase ["+tipoDatabase+"] non gestito.");
				}
				
				// id allegato
				prepareStatement.setLong(idx ++, vo.getId());
				
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
			}
			
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
		dto.setId(vo.getId());
		return dto;
	}

	public AllegatoFilter newFilter() throws ServiceException {
		return new AllegatoFilter(this.getAllegatoService());
	}

	public AllegatoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new AllegatoFilter(this.getAllegatoService(),simpleSearch);
	}
	
	public long count(AllegatoFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(AllegatoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getAllegatoService());
			}
			
			return this.getAllegatoService().count(filter.toExpression()).longValue();
	
		} catch (NotImplementedException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private long _countConLimit(AllegatoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			AllegatoModel model = it.govpay.orm.Allegato.model();
			AllegatoFieldConverter converter = new AllegatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
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
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.DATA_CREAZIONE), "data_creazione");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.DATA_CREAZIONE, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getAllegatoService().nativeQuery(sql, returnTypes, parameters);
			
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
	
	public List<Allegato> getAllegati(Long idVersamento) throws ServiceException {
		AllegatoFilter allegatoFilter = this.newFilter();
		allegatoFilter.setIdVersamento(idVersamento);
		return this.findAll(allegatoFilter);
	}
	
	public List<Allegato> findAll(AllegatoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getAllegatoService());
			}
			
			AllegatoFieldConverter converter = new AllegatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			AllegatoModel model = it.govpay.orm.Allegato.model();
			AllegatoFetch allegatoFetch = new AllegatoFetch();
			org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID;

			List<Allegato> lst = new ArrayList<>();
			List<it.govpay.orm.Allegato> lstAllegatoVO = new ArrayList<>();
			List<IField> fields = getListaFieldsRicerca(filter.isIncludiRawContenuto(), converter, model);

			IPaginatedExpression pagExpr = filter.toPaginatedExpression();

			eseguiRicerca(model, allegatoFetch, idMappingResolutionBehaviour, lstAllegatoVO, fields, pagExpr);

			//			List<it.govpay.orm.Allegato> lstAllegatoVO = this.getAllegatoService().findAll(filter.toPaginatedExpression());
			for(it.govpay.orm.Allegato allegatoVO: lstAllegatoVO) {
				lst.add(AllegatoConverter.toDTO(allegatoVO));
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
	
	private List<IField> getListaFieldsRicerca(boolean includiRawContenuto, AllegatoFieldConverter converter, AllegatoModel model) throws ExpressionException {
		List<IField> fields = new ArrayList<>();
		fields.add(new CustomField("id", Long.class, "id", converter.toTable(model)));
		fields.add(model.NOME);
		fields.add(model.TIPO);
		fields.add(model.DATA_CREAZIONE);
		fields.add(model.DESCRIZIONE);
//		if(includiRawContenuto) {
//			fields.add(model.RAW_CONTENUTO);
//		}
		fields.add(new CustomField("id_versamento", Long.class, "id_versamento", converter.toTable(model)));
		return fields;
	}
	
	private void eseguiRicerca(AllegatoModel model, AllegatoFetch allegatoFetch,
			org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour,
			List<it.govpay.orm.Allegato> list, List<IField> fields, IPaginatedExpression pagExpr)
					throws ServiceException, NotImplementedException {
		try{
			List<Map<String, Object>> returnMap =  this.getAllegatoService().select(pagExpr, false, fields.toArray(new IField[1]));

			for(Map<String, Object> map: returnMap) {
				Object idVersamentoObj = map.remove("id_versamento");

				it.govpay.orm.Allegato allegato = (it.govpay.orm.Allegato) allegatoFetch.fetch(ConnectionManager.getJDBCServiceManagerProperties().getDatabase(), model, map);


				if(idVersamentoObj instanceof Long) {
					Long idVersamento = (Long) idVersamentoObj;
					it.govpay.orm.IdVersamento id_allegato_versamento = null;
					if(idMappingResolutionBehaviour==null || org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour)){
						id_allegato_versamento = ((JDBCVersamentoServiceSearch)(this.getVersamentoService())).findId(idVersamento, false);
					}else{
						id_allegato_versamento = new it.govpay.orm.IdVersamento();
					}
					id_allegato_versamento.setId(idVersamento);
					allegato.setIdVersamento(id_allegato_versamento);
				}

				list.add(allegato);
			}
		} catch(NotFoundException e) {}
	}

	public void deleteAllegato(Allegato allegato) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Allegato vo = AllegatoConverter.toVO(allegato);
			this.getAllegatoService().delete(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

}
