package it.govpay.bd.operazioni.filters;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.constants.EsitoGovpayType;
import it.govpay.orm.dao.jdbc.converter.OperazioneFieldConverter;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

public class OperazioneFilter extends AbstractFilter {
	
	private Long idTracciato = null;
	private EsitoGovpayType esito = null;
	
	public OperazioneFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor, false);
	}
	
	public OperazioneFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor,simpleSearch);
	}

	public enum SortFields {
	}
	
	@Override
	public IExpression _toExpression() throws ServiceException {
		
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.esito != null){
				newExpression.equals(it.govpay.orm.Operazione.model().ESITO, this.esito);
				addAnd = true;
			}
			
			if(this.getIdTracciato() != null){
				if(addAnd)
					newExpression.and();
				OperazioneFieldConverter converter = new OperazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField idTracciatoCustomField = new CustomField("id_tracciato",  Long.class, "id_tracciato",converter.toTable(it.govpay.orm.Operazione.model()));
				
				newExpression.equals(idTracciatoCustomField, this.getIdTracciato());
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

	public void addSortField(SortFields field, boolean asc) {
		FilterSortWrapper filterSortWrapper = new FilterSortWrapper();
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
	}

	public Long getIdTracciato() {
		return idTracciato;
	}

	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}

	public EsitoGovpayType getEsito() {
		return esito;
	}

	public void setEsito(EsitoGovpayType esito) {
		this.esito = esito;
	}

}
