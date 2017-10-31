package it.govpay.bd.pagamento.filters;

import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;

import it.govpay.bd.AbstractFilter;
import it.govpay.model.avvisi.AvvisoPagamento.StatoAvviso;
import it.govpay.orm.Avviso;

public class AvvisoPagamentoFilter extends AbstractFilter {
	
	private StatoAvviso stato;

	public AvvisoPagamentoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public AvvisoPagamentoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(Avviso.model().COD_DOMINIO);
		this.listaFieldSimpleSearch.add(Avviso.model().IUV);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			
			if(this.stato != null) {
				newExpression.equals(Avviso.model().STATO, this.stato.toString());
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

	public StatoAvviso getStato() {
		return stato;
	}

	public void setStato(StatoAvviso stato) {
		this.stato = stato;
	}

}
