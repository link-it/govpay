package it.govpay.bd.reportistica.filters;

import java.util.List;

import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;

import it.govpay.bd.AbstractFilter;
import it.govpay.model.Operatore;


public class EstrattoContoFilter extends AbstractFilter {
	
	private List<Long> idDomini;
	private Operatore operatore;
	
	public EstrattoContoFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	public enum SortFields {
	}
	
	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			return super.newExpression();
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Long> getIdDomini() {
		return idDomini;
	}

	public void setIdDomini(List<Long> idDomini) {
		this.idDomini = idDomini;
	}

	public Operatore getOperatore() {
		return operatore;
	}

	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}

}
