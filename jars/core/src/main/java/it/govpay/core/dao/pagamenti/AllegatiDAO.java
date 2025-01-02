/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.core.dao.pagamenti;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBC_SQLObjectFactory;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;
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
import it.govpay.model.TipoVersamento;
import it.govpay.orm.dao.jdbc.converter.AllegatoFieldConverter;
import it.govpay.orm.model.AllegatoModel;

public class AllegatiDAO  extends BaseDAO {


	public LeggiAllegatoDTOResponse leggiAllegato(LeggiAllegatoDTO leggiAllegatoDTO) throws ServiceException,AllegatoNonTrovatoException {
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
			allegatoBD.closeConnection();
		}
	}

	public StreamingOutput leggiBlobContenuto(Long idAllegato) throws ServiceException{

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

			return new StreamingOutput() {
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
						scriviOutputStream(idAllegato, jdbcAdapter, columnName, output, resultSet);

						bd.commit();
					} catch(Exception e) {
						if(bd != null) bd.rollback();
						log.error("Errore durante la lettura dei bytes: " + e.getMessage(), e);
						throw new WebApplicationException("Errore durante la lettura del contenuto dell'allegato.");
					} finally {
						chiusuraRisorse(prepareStatement, resultSet, bd);
					}
				}

				private void chiusuraRisorse(PreparedStatement prepareStatement, ResultSet resultSet, BasicBD bd) {
					try {
						if(resultSet != null)
							resultSet.close(); 
					} catch (SQLException e) {
						//donothing
					}
					try {
						if(prepareStatement != null)
							prepareStatement.close();
					} catch (SQLException e) { 
						//donothing
					}

					if(bd != null) {
						try {
							bd.setAutoCommit(true);
						} catch (ServiceException e) {
							//donothing
						}
						bd.closeConnection();
					}
				}

				private void scriviOutputStream(Long idAllegato, BlobJDBCAdapter jdbcAdapter, String columnName,
						OutputStream output, ResultSet resultSet)
						throws SQLException, UtilsException, IOException, AllegatoNonTrovatoException {
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
				}
			};
		} catch (SQLQueryObjectException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			//donothing
		}
	}

	public ByteArrayOutputStream copiaBlobContenuto(Long idAllegato) throws ServiceException, AllegatoNonTrovatoException{

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

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			ResultSet resultSet = null;
			BasicBD bd = null;
			try {
				bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

				bd.setupConnection(ContextThreadLocal.get().getTransactionId());

				bd.setAutoCommit(false);

				try (PreparedStatement prepareStatement =bd.getConnection().prepareStatement(sql);){
					prepareStatement.setLong(1, idAllegato);
	
					resultSet = prepareStatement.executeQuery();
					if(resultSet.next()){
						InputStream isRead = jdbcAdapter.getBinaryStream(resultSet, columnName);
						if(isRead != null) {
							IOUtils.copy(isRead, baos);
						} else {
							baos.write("".getBytes());
						}
					} else {
						throw new AllegatoNonTrovatoException("Allegato ["+idAllegato+"] non trovato.");
					}
				}
				bd.commit();
			} catch(ServiceException | SQLException | IOException | UtilsException e) {
				if(bd != null && !bd.isAutoCommit()) {
					bd.rollback();
				}
				log.error("Errore durante la lettura dei bytes: " + e.getMessage(), e);
				throw new WebApplicationException("Errore durante la lettura del contenuto dell'allegato.");
			} finally {
				try {
					if(resultSet != null)
						resultSet.close(); 
				} catch (SQLException e) { 
					//donothing
				}

				if(bd != null) {
					try {
						bd.setAutoCommit(true);
					} catch (ServiceException e) {
						//donothing
					}
					bd.closeConnection();
				}
			}
			return baos;

		} catch (SQLQueryObjectException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			//donothing
		}
	}
}
