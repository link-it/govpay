package it.govpay.bd.reportistica.filters;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;

import it.govpay.bd.AbstractFilter;
import it.govpay.model.Operatore;


public class EstrattoContoFilter extends AbstractFilter {
	
	private List<String> idDomini;
//	private List<Long> idPagamento;
	private List<Long> idVersamento;
	private List<Long> idSingoloVersamento;
	private Operatore operatore;
	
	private Date dataInizio;
	private Date dataFine;
	
	public String statoVersamento ;
	private boolean ignoraStatoVersamento;
	
	public EstrattoContoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor, true,false);
	}
	
	public EstrattoContoFilter(IExpressionConstructor expressionConstructor,boolean ignoraStatoVersamento) {
		this(expressionConstructor,ignoraStatoVersamento,false);
	}
	
	public EstrattoContoFilter(IExpressionConstructor expressionConstructor,boolean ignoraStatoVersamento , boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.ignoraStatoVersamento = ignoraStatoVersamento;
	}

	public enum SortFields {
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

	public List<String> getIdDomini() {
		return idDomini;
	}

	public void setIdDomini(List<String> idDomini) {
		this.idDomini = idDomini;
	}

	public Operatore getOperatore() {
		return operatore;
	}

	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}

//	public List<Long> getIdPagamento() {
//		return idPagamento;
//	}

//	public void setIdPagamento(List<Long> idPagamento) {
//		this.idPagamento = idPagamento;
//	}

	public List<Long> getIdVersamento() {
		return idVersamento;
	}

	public void setIdVersamento(List<Long> idVersamento) {
		this.idVersamento = idVersamento;
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		if(this.dataFine != null) {
			//imposto le ore 23:59:59 nella data fine
			Calendar c = Calendar.getInstance();
			c.setTime(this.dataFine);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MILLISECOND, 999);
			return c.getTime();
		}
		
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public String getStatoVersamento() {
		return statoVersamento;
	}

	public void setStatoVersamento(String statoVersamento) {
		this.statoVersamento = statoVersamento;
	}

	public boolean isIgnoraStatoVersamento() {
		return ignoraStatoVersamento;
	}

	public void setIgnoraStatoVersamento(boolean ignoraStatoVersamento) {
		this.ignoraStatoVersamento = ignoraStatoVersamento;
	}

	public List<Long> getIdSingoloVersamento() {
		return idSingoloVersamento;
	}

	public void setIdSingoloVersamento(List<Long> idSingoloVersamento) {
		this.idSingoloVersamento = idSingoloVersamento;
	}
	
	
}
