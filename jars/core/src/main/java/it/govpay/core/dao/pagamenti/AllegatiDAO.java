package it.govpay.core.dao.pagamenti;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBC_SQLObjectFactory;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jdbc.BlobJDBCAdapter;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.model.Allegato;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.AllegatiBD;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiAllegatoDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiAllegatoDTOResponse;
import it.govpay.core.dao.pagamenti.exception.AllegatoNonTrovatoException;
import it.govpay.core.dao.pagamenti.exception.AllegatoNonTrovatoException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.TipoVersamento;
import it.govpay.orm.dao.jdbc.converter.AllegatoFieldConverter;
import it.govpay.orm.model.AllegatoModel;

public class AllegatiDAO  extends BaseDAO {

	
	public LeggiAllegatoDTOResponse leggiAllegato(LeggiAllegatoDTO leggiAllegatoDTO) throws ServiceException,AllegatoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		AllegatiBD allegatoBD = null;

		LeggiAllegatoDTOResponse response = new LeggiAllegatoDTOResponse();
		try {
			allegatoBD = new AllegatiBD(configWrapper);
			Allegato allegato = allegatoBD.getAllegato(leggiAllegatoDTO.getId(), leggiAllegatoDTO.isIncludiRawContenuto());
			Versamento versamento = allegato.getVersamento(configWrapper);
			Dominio dominio = versamento.getDominio(configWrapper);
			TipoVersamento tipoVersamento = versamento.getTipoVersamento(configWrapper);
			versamento.getTipoVersamentoDominio(configWrapper);
			
			response.setAllegato(allegato);
			response.setVersamento(versamento);
			response.setApplicazione(versamento.getApplicazione(configWrapper));
			response.setTipoVersamento(tipoVersamento);
			response.setDominio(dominio);
			response.setUnitaOperativa(versamento.getUo(configWrapper));

			return response;
		} catch (NotFoundException e) {
			throw new AllegatoNonTrovatoException(e.getMessage(), e);
		} finally {
			if(allegatoBD != null)
				allegatoBD.closeConnection();
		}
	}
	
	public StreamingOutput leggiBlobContenuto(Long idAllegato) throws ServiceException,AllegatoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{

		try {
			BlobJDBCAdapter jdbcAdapter = new BlobJDBCAdapter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			JDBC_SQLObjectFactory jdbcSqlObjectFactory = new JDBC_SQLObjectFactory();
			ISQLQueryObject sqlQueryObject = jdbcSqlObjectFactory.createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());

			AllegatoFieldConverter converter = new AllegatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			AllegatoModel model = it.govpay.orm.Allegato.model();

			String columnName = converter.toColumn(model.RAW_CONTENUTO, false);
			sqlQueryObject.addFromTable(converter.toTable(model.NOME));
			sqlQueryObject.addSelectField(converter.toTable(model.NOME), columnName);

			sqlQueryObject.addWhereCondition(true, converter.toTable(model.NOME, true) + ".id" + " = ? ");

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
						prepareStatement.setLong(1, idAllegato);

						resultSet = prepareStatement.executeQuery();
						if(resultSet.next()){
							InputStream isRead = jdbcAdapter.getBinaryStream(resultSet, columnName);
							if(isRead != null) {
								IOUtils.copy(isRead, output);
							} else {
								output.write("".getBytes());
							}
						} else {
							throw new AllegatoNonTrovatoException("Allegato ["+idAllegato+"] non trovato.");
						}
						
						bd.commit();
					} catch(Exception e) {
						bd.rollback();
						log.error("Errore durante la lettura dei bytes: " + e.getMessage(), e);
						throw new WebApplicationException("Errore durante la lettura del contenuto dell'allegato.");
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
