package it.govpay.bd.pagamento.filters;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.orm.Operazione;
import it.govpay.orm.dao.jdbc.converter.OperazioneFieldConverter;
import it.govpay.orm.model.OperazioneModel;

public class OperazioneFilter extends AbstractFilter {
	
	private Long idTracciato = null;
	private StatoOperazioneType stato = null;
	private TipoOperazioneType tipo = null;
	
	public OperazioneFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor, false);
	}
	
	public OperazioneFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor,simpleSearch);
	}

	public enum SortFields {
		LINEA
	}
	
	@Override
	public IExpression _toExpression() throws ServiceException {
		
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.stato != null){
				newExpression.equals(it.govpay.orm.Operazione.model().STATO, this.stato.toString());
				addAnd = true;
			}
			
			if(this.tipo != null){
				newExpression.equals(it.govpay.orm.Operazione.model().TIPO_OPERAZIONE, this.tipo.toString());
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
		IField ifield = null;
		switch(field){
		case LINEA: ifield = Operazione.model().LINEA_ELABORAZIONE;  
			break;
		default:
			break;
		
		}
		
		filterSortWrapper.setField(ifield);
		this.filterSortList.add(filterSortWrapper);
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			OperazioneFieldConverter converter = new OperazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			OperazioneModel model = it.govpay.orm.Operazione.model();
			
			if(this.stato != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.STATO, true) + " = ? ");
			}
			
			if(this.tipo != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.TIPO_OPERAZIONE, true) + " = ? ");
			}
			
			if(this.getIdTracciato() != null){
				sqlQueryObject.addWhereCondition(true, converter.toTable(model.TIPO_OPERAZIONE, true) + ".id_tracciato" + " = ? ");
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
		
		if(this.stato != null){
			lst.add(this.stato.toString());
		}
		
		if(this.tipo != null){
			lst.add(this.tipo.toString());
		}
		
		if(this.getIdTracciato() != null){
			lst.add(this.getIdTracciato());
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public Long getIdTracciato() {
		return this.idTracciato;
	}

	public void setIdTracciato(Long idTracciato) {
		this.idTracciato = idTracciato;
	}

	public StatoOperazioneType getStato() {
		return this.stato;
	}

	public void setStato(StatoOperazioneType stato) {
		this.stato = stato;
	}

	public TipoOperazioneType getTipo() {
		return tipo;
	}

	public void setTipo(TipoOperazioneType tipo) {
		this.tipo = tipo;
	}


}
