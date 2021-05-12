package it.govpay.core.dao.pagamenti;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBC_SQLObjectFactory;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jdbc.BlobJDBCAdapter;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.model.TracciatoNotificaPagamenti;
import it.govpay.bd.pagamento.TracciatiNotificaPagamentiBD;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiTracciatoNotificaPagamentiDTO;
import it.govpay.core.dao.pagamenti.exception.TracciatoNonTrovatoException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.orm.dao.jdbc.converter.TracciatoNotificaPagamentiFieldConverter;
import it.govpay.orm.model.TracciatoNotificaPagamentiModel;

public class TracciatiNotificaPagamentiDAO extends BaseDAO{

	public TracciatiNotificaPagamentiDAO() {
	}
	
	public TracciatoNotificaPagamenti leggiTracciato(LeggiTracciatoNotificaPagamentiDTO leggiTracciatoDTO) throws ServiceException,TracciatoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		TracciatiNotificaPagamentiBD tracciatoBD = null;

		try {
			tracciatoBD = new TracciatiNotificaPagamentiBD(configWrapper);
			TracciatoNotificaPagamenti tracciato = tracciatoBD.getTracciato(leggiTracciatoDTO.getIdentificativo(), leggiTracciatoDTO.isIncludiRaw());
			return tracciato;

		} catch (NotFoundException e) {
			throw new TracciatoNonTrovatoException(e.getMessage(), e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(tracciatoBD != null)
				tracciatoBD.closeConnection();
		}
	}
	
	public StreamingOutput leggiBlobTracciato(String idTracciato, List<Long> idDomini, IField field) throws ServiceException,TracciatoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{

		try {
			BlobJDBCAdapter jdbcAdapter = new BlobJDBCAdapter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			JDBC_SQLObjectFactory jdbcSqlObjectFactory = new JDBC_SQLObjectFactory();
			ISQLQueryObject sqlQueryObject = jdbcSqlObjectFactory.createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());

			TracciatoNotificaPagamentiFieldConverter converter = new TracciatoNotificaPagamentiFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			TracciatoNotificaPagamentiModel model = it.govpay.orm.TracciatoNotificaPagamenti.model();

			String columnName = converter.toColumn(field, false);
			sqlQueryObject.addFromTable(converter.toTable(model.IDENTIFICATIVO));
			sqlQueryObject.addSelectField(converter.toTable(model.IDENTIFICATIVO), columnName);

			sqlQueryObject.addWhereCondition(true, converter.toColumn(model.IDENTIFICATIVO, true) + " = ? ");
			
			if(idDomini != null && !idDomini.isEmpty()){
				idDomini.removeAll(Collections.singleton(null));
				
				String [] idsDomini = idDomini.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[idDomini.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.IDENTIFICATIVO, true) + ".id_dominio", false, idsDomini );
			}

			String sql = sqlQueryObject.createSQLQuery();

			StreamingOutput zipStream = new StreamingOutput() {
				@Override
				public void write(OutputStream output) throws java.io.IOException, WebApplicationException {
					PreparedStatement prepareStatement = null;
					ResultSet resultSet = null;
					BasicBD bd = null;
					try {
						bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
						
						bd.setupConnection(ContextThreadLocal.get().getTransactionId());
						
						bd.setAutoCommit(false);
						
						prepareStatement = bd.getConnection().prepareStatement(sql);
						prepareStatement.setString(1, idTracciato);

						resultSet = prepareStatement.executeQuery();
						if(resultSet.next()){
							InputStream isRead = jdbcAdapter.getBinaryStream(resultSet, columnName);
							if(isRead != null) {
								IOUtils.copy(isRead, output);
							} else {
								output.write("".getBytes());
							}
						} else {
							throw new TracciatoNonTrovatoException("Tracciato ["+idTracciato+"] non trovato.");
						}
						
						bd.commit();
					} catch(Exception e) {
						bd.rollback();
						log.error("Errore durante la lettura dei bytes: " + e.getMessage(), e);
						throw new WebApplicationException("Errore durante la lettura del tracciato di esito.");
					} finally {
						try {
							if(resultSet != null)
								resultSet.close(); 
						} catch (SQLException e) { }
						try {
							if(prepareStatement != null)
								prepareStatement.close();
						} catch (SQLException e) { }
						
						if(bd != null) {
							try {
								bd.setAutoCommit(true);
							} catch (ServiceException e) {
							}
							bd.closeConnection();
						}
					}
				}
			};
			return zipStream;

		} catch (SQLQueryObjectException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
		}
	}
}
