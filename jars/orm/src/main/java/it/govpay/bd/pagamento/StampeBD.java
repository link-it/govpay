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
import it.govpay.bd.model.converter.StampaConverter;
import it.govpay.bd.pagamento.filters.StampaFilter;
import it.govpay.model.Stampa;
import it.govpay.orm.IdDocumento;
import it.govpay.orm.IdStampa;
import it.govpay.orm.IdVersamento;
import it.govpay.orm.dao.IDBStampaServiceSearch;
import it.govpay.orm.dao.jdbc.JDBCStampaServiceSearch;
import it.govpay.orm.dao.jdbc.converter.StampaFieldConverter;
import it.govpay.orm.model.StampaModel;

public class StampeBD extends BasicBD{

	public StampeBD(BasicBD basicBD) {
		super(basicBD);
	}

	public StampaFilter newFilter() throws ServiceException {
		return new StampaFilter(this.getStampaService());
	}

	public StampaFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new StampaFilter(this.getStampaService(),simpleSearch);
	}
	
	public long count(StampaFilter filter) throws ServiceException {
		try {
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			StampaFieldConverter converter = new StampaFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			StampaModel model = it.govpay.orm.Stampa.model();
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.DATA_CREAZIONE));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.DATA_CREAZIONE), "id");
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
			
			List<List<Object>> nativeQuery = this.getStampaService().nativeQuery(sql, returnTypes, parameters);
			
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

	public List<Stampa> findAll(StampaFilter filter) throws ServiceException {
		try {
			List<Stampa> stampeLst = new ArrayList<>();

			List<it.govpay.orm.Stampa> stampeVOLst = this.getStampaService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.Stampa incassoVO: stampeVOLst) {
				stampeLst.add(StampaConverter.toDTO(incassoVO));
			}
			return stampeLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public Stampa getStampa(long id) throws ServiceException , NotFoundException{
		try {
			it.govpay.orm.Stampa stampaVO = ((JDBCStampaServiceSearch)this.getStampaService()).get(id);
			return StampaConverter.toDTO(stampaVO);
		} catch (NotImplementedException | MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	
	public Stampa getAvvisoVersamento(long idVersamento) throws ServiceException, NotFoundException {
		try {
			IdStampa idStampa = new IdStampa();
			idStampa.setTipo(Stampa.TIPO.AVVISO.toString());
			IdVersamento idVersamentoObj = new IdVersamento();
			idVersamentoObj.setId(idVersamento);
			idStampa.setIdVersamento(idVersamentoObj);
			it.govpay.orm.Stampa stampaVO = ((IDBStampaServiceSearch)this.getStampaService()).get(idStampa);
			return StampaConverter.toDTO(stampaVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	
	public Stampa getAvvisoDocumento(long idDocumento) throws ServiceException, NotFoundException {
		try {
			IdStampa idStampa = new IdStampa();
			idStampa.setTipo(Stampa.TIPO.AVVISO.toString());
			IdDocumento idDocumentoObj = new IdDocumento();
			idDocumentoObj.setId(idDocumento);
			idStampa.setIdDocumento(idDocumentoObj);
			it.govpay.orm.Stampa stampaVO = ((IDBStampaServiceSearch)this.getStampaService()).get(idStampa);
			return StampaConverter.toDTO(stampaVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	
	public void cancellaAvviso(long idVersamento) throws ServiceException, NotFoundException {
		try {
			IdStampa idStampa = new IdStampa();
			idStampa.setTipo(Stampa.TIPO.AVVISO.toString());
			IdVersamento idVersamentoObj = new IdVersamento();
			idVersamentoObj.setId(idVersamento);
			idStampa.setIdVersamento(idVersamentoObj);
			
			this.getStampaService().deleteById(idStampa);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public void insertStampa(Stampa stampa) throws ServiceException {
		try {
			it.govpay.orm.Stampa vo = StampaConverter.toVO(stampa);
			this.getStampaService().create(vo);
			stampa.setId(vo.getId());
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	public void deleteStampa(Stampa stampa) throws ServiceException {
		try {
			it.govpay.orm.Stampa vo = StampaConverter.toVO(stampa);
			this.getStampaService().delete(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public void updateStampa(Stampa stampa) throws NotFoundException,ServiceException {
		try {
			it.govpay.orm.Stampa vo = StampaConverter.toVO(stampa);
			IdStampa idStampa = this.getStampaService().convertToId(vo);
			this.getStampaService().update(idStampa,vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}  
	}
	
	public void updatePdfStampa(Stampa stampa) throws ServiceException {
		try {
			it.govpay.orm.Stampa vo = StampaConverter.toVO(stampa);
			IdStampa idStampa = this.getStampaService().convertToId(vo);
			List<UpdateField> lstUpdateFields = new ArrayList<>();
			lstUpdateFields.add(new UpdateField(it.govpay.orm.Stampa.model().DATA_CREAZIONE, stampa.getDataCreazione()));
			lstUpdateFields.add(new UpdateField(it.govpay.orm.Stampa.model().PDF, stampa.getPdf()));
			
			this.getStampaService().updateFields(idStampa, lstUpdateFields.toArray(new UpdateField[]{}));
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
}
