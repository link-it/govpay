package it.govpay.bd.pagamento;

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
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.converter.DocumentoConverter;
import it.govpay.bd.pagamento.filters.DocumentoFilter;
import it.govpay.orm.dao.jdbc.JDBCDocumentoServiceSearch;
import it.govpay.orm.dao.jdbc.converter.DocumentoFieldConverter;
import it.govpay.orm.model.DocumentoModel;

public class DocumentiBD extends BasicBD {

	public DocumentiBD(BasicBD basicBD) {
		super(basicBD);
	}
	
	public DocumentiBD(String idTransaction) {
		super(idTransaction);
	}
	
	public DocumentiBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}
	
	public DocumentiBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	public Documento getDocumento(long id) throws ServiceException, NotFoundException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			it.govpay.orm.Documento vo = ((JDBCDocumentoServiceSearch)this.getDocumentoService()).get(id);
			return DocumentoConverter.toDTO(vo);
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
	
	public Documento getDocumentoByDominioIdentificativo(Long idDominio, String codDocumento) throws NotFoundException, ServiceException {
		
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			IExpression exp = this.getDocumentoService().newExpression();
			
			DocumentoFieldConverter fieldConverter = new DocumentoFieldConverter(this.getJdbcProperties().getDatabaseType());
			exp.equals(new CustomField("id_dominio", Long.class, "id_dominio", fieldConverter.toTable(it.govpay.orm.Documento.model())), idDominio);
			exp.equals(it.govpay.orm.Documento.model().COD_DOCUMENTO, codDocumento);
			it.govpay.orm.Documento docuemnto = this.getDocumentoService().find(exp);
			return DocumentoConverter.toDTO(docuemnto);
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

	public Documento insertDocumento(Documento dto) throws ServiceException {
		it.govpay.orm.Documento vo = DocumentoConverter.toVO(dto);
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			this.getDocumentoService().create(vo);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
		dto.setId(vo.getId());
		return dto;
	}

	public DocumentoFilter newFilter() throws ServiceException {
		return new DocumentoFilter(this.getDocumentoService());
	}

	public DocumentoFilter newFilter(boolean simpleSearch) throws ServiceException {
		return new DocumentoFilter(this.getDocumentoService(),simpleSearch);
	}

	public long count(DocumentoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			int limitInterno = GovpayConfig.getInstance().getMaxRisultati();
			
			ISQLQueryObject sqlQueryObjectInterno = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			ISQLQueryObject sqlQueryObjectDistinctID = this.getJdbcSqlObjectFactory().createSQLQueryObject(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
			
			DocumentoModel model = it.govpay.orm.Documento.model();
			DocumentoFieldConverter converter = new DocumentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
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
			
			sqlQueryObjectInterno.addFromTable(converter.toTable(model.COD_DOCUMENTO));
			sqlQueryObjectInterno.addSelectField(converter.toTable(model.COD_DOCUMENTO), "id");
			sqlQueryObjectInterno.setANDLogicOperator(true);
			
			// creo condizioni
			sqlQueryObjectInterno = filter.toWhereCondition(sqlQueryObjectInterno);
			// preparo parametri
			Object[] parameters = filter.getParameters(sqlQueryObjectInterno);
			
			sqlQueryObjectInterno.addOrderBy(converter.toColumn(model.COD_DOCUMENTO, true), false);
			sqlQueryObjectInterno.setLimit(limitInterno);
			
			sqlQueryObjectDistinctID.addFromTable(sqlQueryObjectInterno);
			sqlQueryObjectDistinctID.addSelectCountField("id","id",true);
			
			String sql = sqlQueryObjectDistinctID.createSQLQuery();
			List<Class<?>> returnTypes = new ArrayList<>();
			returnTypes.add(Long.class); // Count
			
			List<List<Object>> nativeQuery = this.getDominioService().nativeQuery(sql, returnTypes, parameters);
			
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

	public List<Documento> findAll(DocumentoFilter filter) throws ServiceException {
		try {
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
				filter.setExpressionConstructor(this.getDocumentoService());
			}
			
			List<Documento> documentoLst = new ArrayList<>();

			List<it.govpay.orm.Documento> eventoVOLst = this.getDocumentoService().findAll(filter.toPaginatedExpression());

			for(it.govpay.orm.Documento documentoVO: eventoVOLst) {
				documentoLst.add(DocumentoConverter.toDTO(documentoVO));
			}
			return documentoLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} finally {
			if(this.isAtomica()) {
				this.closeConnection();
			}
		}
	}

}
