package it.govpay.bd.viste.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.orm.VistaRiscossioni;
import it.govpay.orm.dao.jdbc.converter.VistaRiscossioniFieldConverter;
import it.govpay.orm.model.VistaRiscossioniModel;

public class EntrataPrevistaFilter extends AbstractFilter{
	
	private Date dataInizio;
	private Date dataFine;
	private List<String> codDomini;
	private String codDominio;
	private String codApplicazione;
	
	
	public EntrataPrevistaFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}

	public EntrataPrevistaFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.dataInizio != null) {
				if(addAnd)
					newExpression.and();

				newExpression.greaterEquals(VistaRiscossioni.model().DATA_PAGAMENTO, this.dataInizio);
				addAnd = true;
			}
			
			if(this.dataFine != null) {
				if(addAnd)
					newExpression.and();

				newExpression.lessEquals(VistaRiscossioni.model().DATA_PAGAMENTO, this.dataFine);
				addAnd = true;
			}
			
			
			if(this.codDomini != null  && !this.codDomini.isEmpty()){
				this.codDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
			
				newExpression.in(VistaRiscossioni.model().COD_DOMINIO, this.codDomini);
				addAnd = true;
			}
			
			if(this.codDominio!= null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRiscossioni.model().COD_DOMINIO, this.codDominio);
				
				addAnd = true;
			}
			
			if(this.codApplicazione!= null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(VistaRiscossioni.model().COD_APPLICAZIONE, this.codApplicazione);
				
				addAnd = true;
			}
			return newExpression;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			VistaRiscossioniFieldConverter converter = new VistaRiscossioniFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			VistaRiscossioniModel model = it.govpay.orm.VistaRiscossioni.model(); 
			
			if(this.dataInizio != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_PAGAMENTO, true) + " >= ? ");
			}
			
			if(this.dataFine != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_PAGAMENTO, true) + " <= ? ");
			}
			
			
			if(this.codDomini != null  && !this.codDomini.isEmpty()){
				this.codDomini.removeAll(Collections.singleton(null));
				
				String [] codDomini = this.codDomini.toArray(new String[this.codDomini.size()]);
				sqlQueryObject.addWhereINCondition(converter.toColumn(model.COD_DOMINIO, true), true, codDomini );
			}
			
			if(this.codDominio!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_DOMINIO, true) + " = ? ");
			}
			
			if(this.codApplicazione!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_APPLICAZIONE, true) + " = ? ");
			}
			
			return sqlQueryObject;
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (SQLQueryObjectException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Object[] getParameters(ISQLQueryObject sqlQueryObject) throws ServiceException {
		List<Object> lst = new ArrayList<Object>();
		
		if(this.dataInizio != null) {
			lst.add(this.dataInizio);
		}
		
		if(this.dataFine != null) {
			lst.add(this.dataFine);
		}
		
		
		if(this.codDomini != null  && !this.codDomini.isEmpty()){
			// donothing 
		}
		
		if(this.codDominio!= null) {
			lst.add(this.codDominio);
		}
		
		if(this.codApplicazione!= null) {
			lst.add(this.codApplicazione);
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public List<String> getCodDomini() {
		return codDomini;
	}

	public void setCodDomini(List<String> codDomini) {
		this.codDomini = codDomini;
	}

	public String getCodApplicazione() {
		return codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

}
