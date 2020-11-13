/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2018 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBC_SQLObjectFactory;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.jdbc.BlobJDBCAdapter;
import org.openspcoop2.utils.jdbc.IJDBCAdapter;
import org.openspcoop2.utils.jdbc.JDBCAdapterException;
import org.openspcoop2.utils.jdbc.JDBCAdapterFactory;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.Operazione;
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.pagamento.OperazioniBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.filters.OperazioneFilter;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.core.beans.tracciati.TracciatoPendenza;
import it.govpay.core.business.Tracciati;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.ListaOperazioniTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.ListaOperazioniTracciatoDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaTracciatiDTO;
import it.govpay.core.dao.pagamenti.dto.ListaTracciatiDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PostTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.PostTracciatoDTOResponse;
import it.govpay.core.dao.pagamenti.exception.TracciatoNonTrovatoException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;
import it.govpay.orm.constants.StatoTracciatoType;
import it.govpay.orm.dao.jdbc.converter.TracciatoFieldConverter;
import it.govpay.orm.model.TracciatoModel;

public class TracciatiDAO extends BaseDAO{

	public TracciatiDAO() {
	}

	public Tracciato leggiTracciato(LeggiTracciatoDTO leggiTracciatoDTO) throws ServiceException,TracciatoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		TracciatiBD tracciatoBD = null;

		try {
			tracciatoBD = new TracciatiBD(configWrapper);
			Tracciato tracciato = tracciatoBD.getTracciato(leggiTracciatoDTO.getId(), leggiTracciatoDTO.isIncludiRawRichiesta(), leggiTracciatoDTO.isIncludiRawEsito(), leggiTracciatoDTO.isIncludiZipStampe());
			tracciato.getOperatore(configWrapper);
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

	public ListaTracciatiDTOResponse listaTracciati(ListaTracciatiDTO listaTracciatiDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException{
		TracciatiBD tracciatoBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			tracciatoBD = new TracciatiBD(configWrapper);
			TracciatoFilter filter = tracciatoBD.newFilter();

			filter.setCodDominio(listaTracciatiDTO.getIdDominio());
			filter.setDomini(listaTracciatiDTO.getCodDomini());
			filter.setTipo(listaTracciatiDTO.getTipoTracciato());
			filter.setOffset(listaTracciatiDTO.getOffset());
			filter.setLimit(listaTracciatiDTO.getLimit());
			filter.setOperatore(listaTracciatiDTO.getOperatore());
			filter.setStato(listaTracciatiDTO.getStatoTracciato()); 
			filter.setDettaglioStato(listaTracciatiDTO.getDettaglioStato()); 
			filter.setCodTipoVersamento(listaTracciatiDTO.getIdTipoPendenza());
			filter.setFormato(listaTracciatiDTO.getFormatoTracciato());

			List<FilterSortWrapper> filterSortList = new ArrayList<>();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setSortOrder(SortOrder.DESC);
			fsw.setField(it.govpay.orm.Tracciato.model().DATA_CARICAMENTO);
			filterSortList.add(fsw );
			filter.setFilterSortList(filterSortList );

			long count = tracciatoBD.count(filter);

			List<Tracciato> resList = new ArrayList<>();
			if(count > 0) {
				List<Tracciato> resListTmp = new ArrayList<>();

				resListTmp = tracciatoBD.findAll(filter);

				if(!resListTmp.isEmpty()) {
					for (Tracciato tracciato : resListTmp) {
						tracciato.getOperatore(configWrapper);
						resList.add(tracciato);
					}
				}
			} 

			return new ListaTracciatiDTOResponse(count, resList);
		} finally {
			if(tracciatoBD != null)
				tracciatoBD.closeConnection();
		}
	}

	public PostTracciatoDTOResponse create(PostTracciatoDTO postTracciatoDTO) throws NotAuthenticatedException, NotAuthorizedException, GovPayException {
		PostTracciatoDTOResponse postTracciatoDTOResponse = new PostTracciatoDTOResponse();
		TracciatiBD tracciatoBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		
		try {
			SerializationConfig config = new SerializationConfig();
			config.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
			config.setIgnoreNullValues(true);
			ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, config);

			//			if(!AuthorizationManager.isDominioAuthorized(postTracciatoDTO.getUser(), postTracciatoDTO.getIdDominio())) {
			//				throw AuthorizationManager.toNotAuthorizedException(postTracciatoDTO.getUser(), postTracciatoDTO.getIdDominio(), null);
			//			}

			tracciatoBD = new TracciatiBD(configWrapper);

			it.govpay.core.beans.tracciati.TracciatoPendenza beanDati = new TracciatoPendenza();
			beanDati.setStepElaborazione(StatoTracciatoType.NUOVO.getValue());
			beanDati.setStampaAvvisi(postTracciatoDTO.isStampaAvvisi());

			Tracciato tracciato = new Tracciato();
			tracciato.setCodDominio(postTracciatoDTO.getIdDominio());
			tracciato.setBeanDati(serializer.getObject(beanDati));
			tracciato.setDataCaricamento(new Date());
			tracciato.setFileNameRichiesta(postTracciatoDTO.getNomeFile());
			tracciato.setRawRichiesta(postTracciatoDTO.getContenuto());

			tracciato.setIdOperatore(postTracciatoDTO.getOperatore().getId());
			tracciato.setTipo(TIPO_TRACCIATO.PENDENZA);
			tracciato.setStato(STATO_ELABORAZIONE.ELABORAZIONE);
			tracciato.setFormato(postTracciatoDTO.getFormato());
			tracciato.setCodTipoVersamento(postTracciatoDTO.getIdTipoPendenza());

			tracciatoBD.insertTracciato(tracciato);

			// avvio elaborazione tracciato
			it.govpay.core.business.Operazioni.setEseguiElaborazioneTracciati();

			postTracciatoDTOResponse.setCreated(true);

			tracciato.getOperatore(configWrapper);
			postTracciatoDTOResponse.setTracciato(tracciato);
			return postTracciatoDTOResponse;
		} catch (ServiceException | IOException e) {
			throw new GovPayException(e);
		} finally {
			if(tracciatoBD != null)
				tracciatoBD.closeConnection();
		}

	}

	public ListaOperazioniTracciatoDTOResponse listaOperazioniTracciatoPendenza(ListaOperazioniTracciatoDTO listaOperazioniTracciatoDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException{
		OperazioniBD operazioniBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			operazioniBD = new OperazioniBD(configWrapper);
			OperazioneFilter filter = operazioniBD.newFilter();

			filter.setIdTracciato(listaOperazioniTracciatoDTO.getIdTracciato());
			filter.setOffset(listaOperazioniTracciatoDTO.getOffset());
			filter.setLimit(listaOperazioniTracciatoDTO.getLimit());
			filter.setStato(listaOperazioniTracciatoDTO.getStato());
			filter.setTipo(listaOperazioniTracciatoDTO.getTipo());

			long count = operazioniBD.count(filter);

			List<Operazione> resList = new ArrayList<>();
			if(count > 0) {
				List<Operazione> resListTmp = operazioniBD.findAll(filter);

				Tracciati tracciatiBD = new Tracciati();
				for (Operazione operazione : resListTmp) {
					resList.add(tracciatiBD.fillOperazione(operazione).getOperazione());
				}
			} 

			return new ListaOperazioniTracciatoDTOResponse(count, resList);
		} finally {
			if(operazioniBD != null)
				operazioniBD.closeConnection();
		}
	}

	public StreamingOutput leggiBlobTracciato(Long idTracciato, IField field) throws ServiceException,TracciatoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{

		try {
			IJDBCAdapter jdbcAdapter = JDBCAdapterFactory.createJDBCAdapter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			JDBC_SQLObjectFactory jdbcSqlObjectFactory = new JDBC_SQLObjectFactory();
			ISQLQueryObject sqlQueryObject = jdbcSqlObjectFactory.createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());

			TracciatoFieldConverter converter = new TracciatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			TracciatoModel model = it.govpay.orm.Tracciato.model();

			sqlQueryObject.addFromTable(converter.toTable(model.STATO));
			sqlQueryObject.addSelectField(converter.toTable(model.STATO), converter.toColumn(field, false));

			sqlQueryObject.addWhereCondition(true, converter.toTable(model.STATO, true) + ".id" + " = ? ");

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
						
						prepareStatement = bd.getConnection().prepareStatement(sql);
						prepareStatement.setLong(1, idTracciato);

						resultSet = prepareStatement.executeQuery();
						if(resultSet.next()){
							InputStream isRead = jdbcAdapter.getBinaryStream(resultSet, converter.toColumn(field, false));
							if(isRead != null) {
								IOUtils.copy(isRead, output);
							} else {
								output.write("".getBytes());
							}
						} else {
							throw new TracciatoNonTrovatoException("Tracciato ["+idTracciato+"] non trovato.");
						}
					} catch(Exception e) {
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
						
						if(bd != null)
							bd.closeConnection();
					}
				}
			};
			return zipStream;

		} catch (SQLQueryObjectException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (JDBCAdapterException e) {
			throw new ServiceException(e);
		} finally {
		}
	}
	
	
	public StreamingOutput leggiBlobStampeTracciato(Long idTracciato, IField field) throws ServiceException,TracciatoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{

		try {
			BlobJDBCAdapter jdbcAdapter = new BlobJDBCAdapter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			JDBC_SQLObjectFactory jdbcSqlObjectFactory = new JDBC_SQLObjectFactory();
			ISQLQueryObject sqlQueryObject = jdbcSqlObjectFactory.createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());

			TracciatoFieldConverter converter = new TracciatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			TracciatoModel model = it.govpay.orm.Tracciato.model();

			String columnName = converter.toColumn(field, false);
			sqlQueryObject.addFromTable(converter.toTable(model.STATO));
			sqlQueryObject.addSelectField(converter.toTable(model.STATO), columnName);

			sqlQueryObject.addWhereCondition(true, converter.toTable(model.STATO, true) + ".id" + " = ? ");

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
						prepareStatement.setLong(1, idTracciato);

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
