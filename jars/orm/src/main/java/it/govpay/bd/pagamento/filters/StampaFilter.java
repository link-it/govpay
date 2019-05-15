package it.govpay.bd.pagamento.filters;

import java.util.Date;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.Stampa;
import it.govpay.orm.dao.jdbc.converter.StampaFieldConverter;

public class StampaFilter extends AbstractFilter {
	
	private Long idVersamento;
	private String tipo;
	private Date dataInizio;
	private Date dataFine;
	private Long id;

	public StampaFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public StampaFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(Stampa.model().TIPO);
		FilterSortWrapper filterSortWrapper = new FilterSortWrapper();
		filterSortWrapper.setField(Stampa.model().DATA_CREAZIONE);
		filterSortWrapper.setSortOrder(SortOrder.DESC);
		this.addFilterSort(filterSortWrapper );
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			StampaFieldConverter converter = new StampaFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			
			if(this.tipo!= null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Stampa.model().TIPO, this.tipo);
				
				addAnd = true;
			}

			if(this.idVersamento != null) {
				if(addAnd)
					newExpression.and();
				
				CustomField cf = new CustomField("id_versamento", Long.class, "id_versamento", converter.toTable(Stampa.model()));
				newExpression.equals(cf, this.idVersamento);
				
				addAnd = true;
			}
			
			if(this.id != null) {
				if(addAnd)
					newExpression.and();
				
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(Stampa.model()));
				newExpression.equals(cf, this.id);
				
				addAnd = true;
			}
			
			if(this.dataInizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(Stampa.model().DATA_CREAZIONE, this.dataInizio,this.dataFine);
				addAnd = true;
			} else {
				if(this.dataInizio != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.greaterEquals(Stampa.model().DATA_CREAZIONE, this.dataInizio);
					addAnd = true;
				} 
				
				if(this.dataFine != null) {
					if(addAnd)
						newExpression.and();
	
					newExpression.lessEquals(Stampa.model().DATA_CREAZIONE, this.dataFine);
					addAnd = true;
				}
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

	public Long getIdVersamento() {
		return idVersamento;
	}

	public void setIdVersamento(Long idVersamento) {
		this.idVersamento = idVersamento;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
}
