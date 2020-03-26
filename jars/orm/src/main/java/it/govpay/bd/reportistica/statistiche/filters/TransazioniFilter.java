package it.govpay.bd.reportistica.statistiche.filters;

import org.apache.commons.lang.NotImplementedException;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.utils.sql.ISQLQueryObject;

public class TransazioniFilter extends StatisticaFilter {
	
	public TransazioniFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}
	
	private String codDominio;
	private String codPsp;
	
	public String getCodDominio() {
		return this.codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getCodPsp() {
		return this.codPsp;
	}
	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		throw new ServiceException(new NotImplementedException());
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		return null;
	}

	@Override
	public Object[] getParameters(ISQLQueryObject sqlQueryObject) throws ServiceException {
		return null;
	}
}
