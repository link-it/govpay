package it.govpay.bd.viste;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.viste.filters.RendicontazioneFilter;
import it.govpay.bd.viste.model.Rendicontazione;
import it.govpay.bd.viste.model.converter.RendicontazioneConverter;
import it.govpay.orm.FR;
import it.govpay.orm.VistaRendicontazione;
import it.govpay.orm.dao.jdbc.JDBCVistaRendicontazioneServiceSearch;
import it.govpay.orm.dao.jdbc.converter.FRFieldConverter;
import it.govpay.orm.dao.jdbc.converter.VistaRendicontazioneFieldConverter;
import it.govpay.orm.model.FRModel;
import it.govpay.orm.model.VistaRendicontazioneModel;

public class RendicontazioniBD extends BasicBD {

	public RendicontazioniBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public RendicontazioniBD(String idTransaction) {
		super(idTransaction);
	}
	
	public RendicontazioniBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public RendicontazioniBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}
	
	public RendicontazioneFilter newFilter() throws ServiceException {
		return new RendicontazioneFilter(this.getVistaRendicontazioneServiceSearch());
	}
	
	public RendicontazioneFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new RendicontazioneFilter(this.getVistaRendicontazioneServiceSearch(),simpleSearch);
	}

	public List<Rendicontazione> findAll(RendicontazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVistaRendicontazioneServiceSearch());
			}
			
			List<it.govpay.orm.VistaRendicontazione> rendicontazioneVOLst = this.getVistaRendicontazioneServiceSearch().findAll(filter.toPaginatedExpression());
			return RendicontazioneConverter.toDTO(rendicontazioneVOLst);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public long count(RendicontazioneFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(RendicontazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVistaRendicontazioneServiceSearch());
			}
			
			return this.getVistaRendicontazioneServiceSearch().count(filter.toExpression()).longValue();
	
		} catch (NotImplementedException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private long _countConLimit(RendicontazioneFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
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
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.RND_DATA), "rnd_data");
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
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	/**
	 * Recupera la rendicontazione identificata dalla chiave fisica
	 */
	public Rendicontazione getRendicontazione(long id) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.VistaRendicontazione rendicontazione = ((JDBCVistaRendicontazioneServiceSearch)this.getVistaRendicontazioneServiceSearch()).get(id);
			return RendicontazioneConverter.toDTO(rendicontazione);
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
	
	public List<it.govpay.bd.viste.model.Rendicontazione> getFr(String codFlusso, Boolean obsoleto, Date dataOraFlusso) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			IExpression expr = this.getVistaRendicontazioneServiceSearch().newExpression();
			expr.equals(VistaRendicontazione.model().FR_COD_FLUSSO, codFlusso);
			
			if(obsoleto != null) {
				expr.equals(VistaRendicontazione.model().FR_OBSOLETO, obsoleto);
			}
			
			if(dataOraFlusso != null) {
				// controllo millisecondi
				Calendar cDataDa = Calendar.getInstance();
				cDataDa.setTime(dataOraFlusso);
				int currentMillis = cDataDa.get(Calendar.MILLISECOND);

				// in questo caso posso avere una data dove non sono stati impostati i millisecondi oppure millisecondi == 0, faccio una ricerca su un intervallo di un secondo
				if(currentMillis == 0) {
					Calendar cDataA = Calendar.getInstance();
					cDataA.setTime(dataOraFlusso);
					cDataA.set(Calendar.MILLISECOND, 999);
					Date dataA = cDataA.getTime();
					
					IExpression exprFR =  this.getFrService().newExpression();
					exprFR.equals(FR.model().COD_FLUSSO, codFlusso);
					
					if(obsoleto != null) {
						exprFR.equals(FR.model().OBSOLETO, obsoleto);
					}

					exprFR.greaterEquals(FR.model().DATA_ORA_FLUSSO, dataOraFlusso).and().lessEquals(FR.model().DATA_ORA_FLUSSO, dataA);
					IPaginatedExpression pagExpr = this.getFrService().toPaginatedExpression(exprFR);
					pagExpr.offset(0).limit(1);
					pagExpr.addOrder(FR.model().DATA_ORA_FLUSSO, SortOrder.DESC); // prendo il piu' recente
					
					FRFieldConverter converter = new FRFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
					FRModel model = it.govpay.orm.FR.model();
					CustomField cf = new CustomField("id", Long.class, "id", converter.toAliasTable(model));
					
					try {
						List<Object> select = this.getFrService().select(pagExpr, cf);
						
						Long idFR = 0L;
						for (Object obj : select) {
							idFR = (Long) obj;
						}
						expr.equals(VistaRendicontazione.model().FR_ID, idFR);
					} catch (NotFoundException e) {
						throw new NotFoundException("Nessuna entry corrisponde ai criteri indicati.");
					}
				} else {
					expr.equals(VistaRendicontazione.model().FR_DATA_ORA_FLUSSO, dataOraFlusso);
				}
			}
			
			IPaginatedExpression pagExpr = this.getVistaRendicontazioneServiceSearch().toPaginatedExpression(expr);
			pagExpr.offset(0);
			
			VistaRendicontazioneFieldConverter converter = new VistaRendicontazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			VistaRendicontazioneModel model = it.govpay.orm.VistaRendicontazione.model();
			CustomField cf = new CustomField("id", Long.class, "id", converter.toAliasTable(model));
			pagExpr.addOrder(cf, SortOrder.ASC);
			
			List<it.govpay.orm.VistaRendicontazione> rendicontazioneVOLst = this.getVistaRendicontazioneServiceSearch().findAll(pagExpr);
			return RendicontazioneConverter.toDTO(rendicontazioneVOLst);
			
		} catch (NotImplementedException | ExpressionNotImplementedException | ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
}
