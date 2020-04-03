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

import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.model.converter.TracciatoConverter;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.orm.IdTracciato;
import it.govpay.orm.dao.jdbc.JDBCTracciatoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.TracciatoFieldConverter;
import it.govpay.orm.model.TracciatoModel;

public class TracciatiBD extends BasicBD {

	public TracciatiBD(BasicBD basicBD) {
		super(basicBD);
	}

	/**
	 * Recupera l'tracciato tramite l'id fisico
	 * 
	 * @param idEnte
	 * @return
	 * @throws NotFoundException se l'ente non esiste.
	 * @throws MultipleResultException in caso di duplicati.
	 * @throws ServiceException in caso di errore DB.
	 */
	public Tracciato getTracciato(Long idTracciato) throws NotFoundException, ServiceException {
		if(idTracciato == null) {
			throw new ServiceException("Parameter 'id' cannot be NULL");
		}

		long id = idTracciato.longValue();

		try {
			it.govpay.orm.Tracciato tracciatoVO = ((JDBCTracciatoServiceSearch)this.getTracciatoService()).get(id);
			return TracciatoConverter.toDTO(tracciatoVO);

		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Aggiorna l'intermediari con i dati forniti
	 * @param intermediari
	 * @throws NotPermittedException se si inserisce un intermediari gia' censito
	 * @throws ServiceException in caso di errore DB.
	 */
	public void insertTracciato(Tracciato tracciato) throws ServiceException{
		try {
			it.govpay.orm.Tracciato vo = TracciatoConverter.toVO(tracciato);

			this.getTracciatoService().create(vo);
			tracciato.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}

	}


	public TracciatoFilter newFilter() {
		return new TracciatoFilter(this.getTracciatoService());
	}
	
	public TracciatoFilter newFilter(boolean simpleSearch) {
		return new TracciatoFilter(this.getTracciatoService(),simpleSearch);
	}

	public long count(TracciatoFilter filter) throws ServiceException {
		try {
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
		}
	}

	public List<Tracciato> findAll(TracciatoFilter filter) throws ServiceException {
		try {
			List<Tracciato> lst = new ArrayList<>();
			List<it.govpay.orm.Tracciato> lstTracciatoVO = this.getTracciatoService().findAll(filter.toPaginatedExpression());
			for(it.govpay.orm.Tracciato tracciatoVO: lstTracciatoVO) {
				lst.add(TracciatoConverter.toDTO(tracciatoVO));
			}
			return lst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public void update(Tracciato tracciato) throws ServiceException {
		try {
			it.govpay.orm.Tracciato vo = TracciatoConverter.toVO(tracciato);
			this.getTracciatoService().update(this.getTracciatoService().convertToId(vo), vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}

	public void updateBeanDati(Tracciato tracciato, String beanDati) throws ServiceException {
		try {
			IdTracciato convertToId = this.getTracciatoService().convertToId(TracciatoConverter.toVO(tracciato));
			
//			log.info("aggiorno bean dati del tracciato: %s" , convertToId.getId());
			this.getTracciatoService().updateFields(convertToId, new UpdateField(it.govpay.orm.Tracciato.model().BEAN_DATI, beanDati));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
	
	public void updateFineElaborazione(Tracciato tracciato) throws ServiceException {
		try {
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
		}
	}

}

