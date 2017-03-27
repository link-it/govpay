package it.govpay.bd.pagamento.filters;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.orm.IUV;
import it.govpay.orm.RPT;
import it.govpay.orm.dao.jdbc.converter.RPTFieldConverter;

public class IuvFilter extends AbstractFilter{

	private String iuv;
	
	public IuvFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}
	
	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.iuv != null){
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(IUV.model().IUV, this.iuv, LikeMode.ANYWHERE);
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
	
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
}
