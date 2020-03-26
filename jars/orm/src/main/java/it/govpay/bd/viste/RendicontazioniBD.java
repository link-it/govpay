package it.govpay.bd.viste;

import java.util.ArrayList;
import java.util.List;

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
import it.govpay.bd.viste.filters.RendicontazioneFilter;
import it.govpay.bd.viste.model.Rendicontazione;
import it.govpay.bd.viste.model.converter.RendicontazioneConverter;
import it.govpay.orm.IdRendicontazione;
import it.govpay.orm.dao.jdbc.converter.VistaRendicontazioneFieldConverter;
import it.govpay.orm.model.VistaRendicontazioneModel;

public class RendicontazioniBD extends BasicBD {

	public RendicontazioniBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public RendicontazioneFilter newFilter() throws ServiceException {
		return new RendicontazioneFilter(this.getVistaRendicontazioneServiceSearch());
	}
	
	public RendicontazioneFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new RendicontazioneFilter(this.getVistaRendicontazioneServiceSearch(),simpleSearch);
	}

	public List<Rendicontazione> findAll(RendicontazioneFilter filter) throws ServiceException {
		try {
			List<it.govpay.orm.VistaRendicontazione> rendicontazioneVOLst = this.getVistaRendicontazioneServiceSearch().findAll(filter.toPaginatedExpression());
			return RendicontazioneConverter.toDTO(rendicontazioneVOLst);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public long count(RendicontazioneFilter filter) throws ServiceException {
		try {
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			VistaRendicontazioneFieldConverter converter = new VistaRendicontazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			VistaRendicontazioneModel model = it.govpay.orm.VistaRendicontazione.model();
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.RND_IUV));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.RND_IUV), "id");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.RND_DATA, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getVistaRendicontazioneServiceSearch().nativeQuery(sql, returnTypes, parameters);
			
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
	
	/**
	 * Recupera la rendicontazione identificata dalla chiave fisica
	 */
	public Rendicontazione getRendicontazione(long id) throws ServiceException {
		try {
			IdRendicontazione idRendicontazione = new IdRendicontazione();
			idRendicontazione.setId(id);
			it.govpay.orm.VistaRendicontazione rendicontazione = this.getVistaRendicontazioneServiceSearch().get(idRendicontazione);
			return RendicontazioneConverter.toDTO(rendicontazione);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
}
