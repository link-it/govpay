package it.govpay.bd.pagamento.filters;

import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;

import it.govpay.bd.AbstractFilter;

public class IncassoFilter extends AbstractFilter{
	
	private List<Long> idDomini;
	private Date dataInizio;
	private Date dataFine;
	private String trn;
	private String dispositivo;
	private String causale;
	private List<Long> idIncasso= null;

	public IncassoFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}
	
	public IncassoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
	}
	
	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			return newExpression;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public IExpression _toSimpleSearchExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			return newExpression;
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

	public String getTrn() {
		return trn;
	}

	public void setTrn(String trn) {
		this.trn = trn;
	}

	public String getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}
	public List<Long> getIdIncasso() {
		return idIncasso;
	}

	public void setIdIncasso(List<Long> idIncasso) {
		this.idIncasso = idIncasso;
	}
}
