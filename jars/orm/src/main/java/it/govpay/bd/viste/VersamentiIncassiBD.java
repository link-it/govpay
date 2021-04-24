package it.govpay.bd.viste;

import java.util.ArrayList;
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
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.viste.filters.VersamentoIncassoFilter;
import it.govpay.bd.viste.model.converter.VersamentoIncassoConverter;
import it.govpay.orm.dao.jdbc.JDBCVersamentoIncassoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.VersamentoIncassoFieldConverter;
import it.govpay.orm.model.VersamentoIncassoModel;

public class VersamentiIncassiBD  extends BasicBD {

	public VersamentiIncassiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public VersamentiIncassiBD(String idTransaction) {
		super(idTransaction);
	}
	
	public VersamentiIncassiBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public VersamentiIncassiBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	/**
	 * Recupera il versamento identificato dalla chiave fisica
	 * @throws NotFoundException 
	 */
	public Versamento getVersamento(long id) throws ServiceException, NotFoundException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.VersamentoIncasso versamento = ((JDBCVersamentoIncassoServiceSearch)this.getVersamentoIncassoServiceSearch()).get(id);
			return VersamentoIncassoConverter.toDTO(versamento);
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
	
	/**
	 * Recupera il versamento identificato dalla chiave logica
	 */
	public Versamento getVersamento(String codDominio, String iuv) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getVersamentoIncassoServiceSearch().newExpression();
			exp.equals(it.govpay.orm.VersamentoIncasso.model().ID_UO.ID_DOMINIO.COD_DOMINIO, codDominio);
			exp.equals(it.govpay.orm.VersamentoIncasso.model().IUV_VERSAMENTO,iuv);
			it.govpay.orm.VersamentoIncasso versamento = this.getVersamentoIncassoServiceSearch().find(exp);
			return VersamentoIncassoConverter.toDTO(versamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
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
	
	/**
	 * Recupera il versamento identificato dalla coppia dominio/numeroavviso
	 */
	public Versamento getVersamentoFromDominioNumeroAvviso(String codDominio, String numeroAvviso) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getVersamentoIncassoServiceSearch().newExpression();
			exp.equals(it.govpay.orm.VersamentoIncasso.model().ID_UO.ID_DOMINIO.COD_DOMINIO, codDominio);
			exp.equals(it.govpay.orm.VersamentoIncasso.model().NUMERO_AVVISO,numeroAvviso);
			it.govpay.orm.VersamentoIncasso versamento = this.getVersamentoIncassoServiceSearch().find(exp);
			return VersamentoIncassoConverter.toDTO(versamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
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

	/**
	 * Recupera il versamento identificato dalla chiave logica
	 */
	public Versamento getVersamento(long idApplicazione, String codVersamentoEnte) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getVersamentoIncassoServiceSearch().newExpression();
			
			VersamentoIncassoFieldConverter fieldConverter = new VersamentoIncassoFieldConverter(this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_applicazione", Long.class, "id_applicazione", fieldConverter.toTable(it.govpay.orm.VersamentoIncasso.model())), idApplicazione);
			exp.and();
			exp.equals(it.govpay.orm.VersamentoIncasso.model().COD_VERSAMENTO_ENTE, codVersamentoEnte);
			
			it.govpay.orm.VersamentoIncasso versamento = this.getVersamentoIncassoServiceSearch().find(exp);
			
//			IdVersamento id = new IdVersamento();
//			IdApplicazione idApplicazioneOrm = new IdApplicazione();
//			idApplicazioneOrm.setId(idApplicazione);
//			id.setIdApplicazione(idApplicazioneOrm);
//			id.setCodVersamentoEnte(codVersamentoEnte);
//			it.govpay.orm.VersamentoIncasso versamento = this.getVersamentoIncassoServiceSearch().get(id);
			return VersamentoIncassoConverter.toDTO(versamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
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


	/**
	 * Recupera il versamento identificato dalla chiave logica
	 */
	public Versamento getVersamentoByBundlekey(long idApplicazione, String bundleKey, String codDominio, String codUnivocoDebitore) throws NotFoundException, ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getVersamentoIncassoServiceSearch().newExpression();
			exp.equals(it.govpay.orm.VersamentoIncasso.model().COD_BUNDLEKEY, bundleKey);

			if(codUnivocoDebitore != null)
				exp.equals(it.govpay.orm.VersamentoIncasso.model().DEBITORE_IDENTIFICATIVO, codUnivocoDebitore);

			if(codDominio != null)
				exp.equals(it.govpay.orm.VersamentoIncasso.model().ID_UO.ID_DOMINIO.COD_DOMINIO, codDominio);

			VersamentoIncassoFieldConverter fieldConverter = new VersamentoIncassoFieldConverter(this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_applicazione", Long.class, "id_applicazione", fieldConverter.toTable(it.govpay.orm.VersamentoIncasso.model())), idApplicazione);

			it.govpay.orm.VersamentoIncasso versamento =  this.getVersamentoIncassoServiceSearch().find(exp);
			return VersamentoIncassoConverter.toDTO(versamento);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
	
	public VersamentoIncassoFilter newFilter() throws ServiceException {
		return new VersamentoIncassoFilter(this.getVersamentoIncassoServiceSearch());
	}

	public VersamentoIncassoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new VersamentoIncassoFilter(this.getVersamentoIncassoServiceSearch(),simpleSearch);
	}
	
	public long count(VersamentoIncassoFilter filter) throws ServiceException {
		return filter.isEseguiCountConLimit() ? this._countConLimit(filter) : this._countSenzaLimit(filter);
	}
	
	private long _countSenzaLimit(VersamentoIncassoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVersamentoIncassoServiceSearch());
			}
			
			return this.getVersamentoIncassoServiceSearch().count(filter.toExpression()).longValue();
	
		} catch (NotImplementedException e) {
			return 0;
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

	private long _countConLimit(VersamentoIncassoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			VersamentoIncassoFieldConverter converter = new VersamentoIncassoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			VersamentoIncassoModel model = it.govpay.orm.VersamentoIncasso.model();
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.COD_VERSAMENTO_ENTE));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.COD_VERSAMENTO_ENTE), "id");
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
			
			List<List<Object>> nativeQuery = this.getVersamentoIncassoServiceSearch().nativeQuery(sql, returnTypes, parameters);
			
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

	public List<Versamento> findAll(VersamentoIncassoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getVersamentoIncassoServiceSearch());
			}
			
			List<Versamento> versamentoLst = new ArrayList<>();

			IPaginatedExpression paginatedExpression = filter.toPaginatedExpression();
			
			List<it.govpay.orm.VersamentoIncasso> versamentoVOLst = this.getVersamentoIncassoServiceSearch().findAll(paginatedExpression); 
			for(it.govpay.orm.VersamentoIncasso versamentoVO: versamentoVOLst) {
				versamentoLst.add(VersamentoIncassoConverter.toDTO(versamentoVO));
			}
			return versamentoLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}
}
