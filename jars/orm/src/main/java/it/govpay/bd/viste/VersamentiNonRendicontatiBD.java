package it.govpay.bd.viste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.NonNegativeNumber;
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
import it.govpay.bd.viste.filters.VersamentoNonRendicontatoFilter;
import it.govpay.bd.viste.model.VersamentoNonRendicontato;
import it.govpay.bd.viste.model.converter.VersamentoNonRendicontatoConverter;
import it.govpay.orm.VistaVersamentoNonRendicontato;
import it.govpay.orm.dao.jdbc.JDBCVistaVersamentoNonRendicontatoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.VistaVersamentoNonRendicontatoFieldConverter;
import it.govpay.orm.model.VistaVersamentoNonRendicontatoModel;

public class VersamentiNonRendicontatiBD extends BasicBD {

	public VersamentiNonRendicontatiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public VersamentiNonRendicontatiBD(String idTransaction) {
		super(idTransaction);
	}
	
	public VersamentiNonRendicontatiBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public VersamentiNonRendicontatiBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}
	
	public VersamentoNonRendicontatoFilter newFilter() throws ServiceException {
		return new VersamentoNonRendicontatoFilter(this.getVistaVersamentoNonRendicontatoServiceSearch());
	}
	
	public VersamentoNonRendicontatoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new VersamentoNonRendicontatoFilter(this.getVistaVersamentoNonRendicontatoServiceSearch(),simpleSearch);
	}

	public List<VersamentoNonRendicontato> findAll(VersamentoNonRendicontatoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVistaVersamentoNonRendicontatoServiceSearch());
			}
			
			List<it.govpay.orm.VistaVersamentoNonRendicontato> rendicontazioneVOLst = this.getVistaVersamentoNonRendicontatoServiceSearch().findAll(filter.toPaginatedExpression());
			return VersamentoNonRendicontatoConverter.toDTO(rendicontazioneVOLst);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public long count(VersamentoNonRendicontatoFilter filter) throws ServiceException {
			return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(VersamentoNonRendicontatoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVistaVersamentoNonRendicontatoServiceSearch());
			}
			
			return this.getVistaVersamentoNonRendicontatoServiceSearch().count(filter.toExpression()).longValue();
	
		} catch (NotImplementedException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private long _countConLimit(VersamentoNonRendicontatoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			VistaVersamentoNonRendicontatoFieldConverter converter = new VistaVersamentoNonRendicontatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			VistaVersamentoNonRendicontatoModel model = it.govpay.orm.VistaVersamentoNonRendicontato.model();
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.PAG_IUV));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.PAG_IUV), "id");
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.PAG_DATA_PAGAMENTO), "pag_data_pagamento");
			
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.PAG_DATA_PAGAMENTO, true), false);
		
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getVistaVersamentoNonRendicontatoServiceSearch().nativeQuery(sql, returnTypes, parameters);
			
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
	 * Recupera la entry identificata dalla chiave fisica
	 */
	public VersamentoNonRendicontato getVersamentoNonRendicontato(long id) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.VistaVersamentoNonRendicontato rendicontazione = ((JDBCVistaVersamentoNonRendicontatoServiceSearch)this.getVistaVersamentoNonRendicontatoServiceSearch()).get(id);
			return VersamentoNonRendicontatoConverter.toDTO(rendicontazione);
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
	
	public List<it.govpay.bd.viste.model.VersamentoNonRendicontato> ricercaRiscossioniDominio(String codDominio, Date dataRtDa, Date dataRtA, List<String> listaTipiPendenza, Integer offset, Integer limit) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			VistaVersamentoNonRendicontatoModel model = it.govpay.orm.VistaVersamentoNonRendicontato.model();
			IExpression exp = this.getVistaVersamentoNonRendicontatoServiceSearch().newExpression();
			exp.equals(model.VRS_ID_DOMINIO.COD_DOMINIO, codDominio).and();
			if(dataRtDa != null) {
				exp.greaterEquals(model.PAG_DATA_PAGAMENTO, dataRtDa);
			}
			exp.lessEquals(model.PAG_DATA_PAGAMENTO, dataRtA);
//			exp.equals(model.STATO, Stato.INCASSATO.toString());
			if(listaTipiPendenza != null && !listaTipiPendenza.isEmpty()) {
				listaTipiPendenza.removeAll(Collections.singleton(null));
				exp.in(model.VRS_ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, listaTipiPendenza);
			}
			
			IPaginatedExpression pagExp = this.getVistaVersamentoNonRendicontatoServiceSearch().toPaginatedExpression(exp);
			pagExp.offset(offset).limit(limit);
			pagExp.addOrder(model.PAG_DATA_PAGAMENTO, SortOrder.ASC);
			
			List<VersamentoNonRendicontato> entratePrevisteLst = new ArrayList<>();
			List<VistaVersamentoNonRendicontato> riscossioniVOLst = this.getVistaVersamentoNonRendicontatoServiceSearch().findAll(pagExp);
			for(it.govpay.orm.VistaVersamentoNonRendicontato riscossioneVO: riscossioniVOLst) {
				entratePrevisteLst.add(VersamentoNonRendicontatoConverter.toDTO(riscossioneVO));
			}
			return entratePrevisteLst;
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public long countRiscossioniDominio(String codDominio, Date dataRtDa, Date dataRtA, List<String> listaTipiPendenza) throws ServiceException{
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			VistaVersamentoNonRendicontatoModel model = it.govpay.orm.VistaVersamentoNonRendicontato.model();
			IExpression exp = this.getVistaVersamentoNonRendicontatoServiceSearch().newExpression();
			exp.equals(model.VRS_ID_DOMINIO.COD_DOMINIO, codDominio).and();
			if(dataRtDa != null) {
				exp.greaterEquals(model.PAG_DATA_PAGAMENTO, dataRtDa);
			}
			exp.lessEquals(model.PAG_DATA_PAGAMENTO, dataRtA);
//			exp.equals(model.STATO, Stato.INCASSATO.toString());
			if(listaTipiPendenza != null && !listaTipiPendenza.isEmpty()) {
				listaTipiPendenza.removeAll(Collections.singleton(null));
				exp.in(model.VRS_ID_TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, listaTipiPendenza);
			}
			
			NonNegativeNumber count = this.getVistaVersamentoNonRendicontatoServiceSearch().count(exp);
			
			return count.longValue();
		} catch(NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
		
	}
}
