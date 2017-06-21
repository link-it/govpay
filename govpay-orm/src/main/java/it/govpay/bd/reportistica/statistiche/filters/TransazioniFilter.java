package it.govpay.bd.reportistica.statistiche.filters;

import org.apache.commons.lang.NotImplementedException;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;

import it.govpay.bd.AbstractFilter;

public class TransazioniFilter extends AbstractFilter {
	
	public TransazioniFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}
	
	private String codDominio;
	private String codPsp;
	
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getCodPsp() {
		return codPsp;
	}
	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		throw new ServiceException(new NotImplementedException());
	}
}
