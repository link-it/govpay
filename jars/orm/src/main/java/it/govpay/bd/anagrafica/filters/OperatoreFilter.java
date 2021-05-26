/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.bd.anagrafica.filters;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.Operatore;
import it.govpay.orm.dao.jdbc.converter.OperatoreFieldConverter;
import it.govpay.orm.model.OperatoreModel;

public class OperatoreFilter extends AbstractFilter {
	
	private String principal= null;
	private boolean searchModeEquals = false; 
	
	private static OperatoreModel model = Operatore.model();
	private OperatoreFieldConverter converter = null;

	public enum SortFields {
	}
	
	public OperatoreFilter(IExpressionConstructor expressionConstructor) throws ServiceException {
		this(expressionConstructor,false);
	}
	
	public OperatoreFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) throws ServiceException {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(model.ID_UTENZA.PRINCIPAL);
		this.listaFieldSimpleSearch.add(model.NOME);
		this.fieldAbilitato = model.ID_UTENZA.ABILITATO;
		
		this.converter = new OperatoreFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
		this.eseguiCountConLimit = false;
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.principal != null){
				if(!this.searchModeEquals)
					newExpression.ilike(model.ID_UTENZA.PRINCIPAL, this.principal,LikeMode.ANYWHERE);
				else 
					newExpression.equals(model.ID_UTENZA.PRINCIPAL, this.principal);
				addAnd = true;
			}

			addAnd = this.setFiltroAbilitato(newExpression, addAnd);
			
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
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			
			boolean addTabellaUtenze = false;
			if(this.principal != null){
				sqlQueryObject.addFromTable(converter.toTable(model.ID_UTENZA));
				sqlQueryObject.addWhereCondition(converter.toTable(model.NOME, true) + ".id_utenza="
						+converter.toTable(model.ID_UTENZA, true)+".id");
				
				addTabellaUtenze = true;
				
				if(!this.searchModeEquals)
					sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.ID_UTENZA.PRINCIPAL, true), this.principal, true, true);
				else 
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_UTENZA.PRINCIPAL, true) + " = ? ");
				
			}
			
			// filtro abilitato
			if(this.searchAbilitato != null && this.fieldAbilitato != null) {
				if(!addTabellaUtenze) {
					sqlQueryObject.addFromTable(converter.toTable(model.ID_UTENZA));
					sqlQueryObject.addWhereCondition(converter.toTable(model.NOME, true) + ".id_utenza="
							+converter.toTable(model.ID_UTENZA, true)+".id");
					
					addTabellaUtenze = true;
				}
				
				sqlQueryObject.addWhereCondition(true,converter.toColumn(this.fieldAbilitato, true) + " = ? ");
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
		
		if(this.principal != null){
			if(this.searchModeEquals)
				lst.add(this.principal);
		}
		
		// filtro abilitato
		try {
			lst = this.setValoreFiltroAbilitato(lst, converter);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public String getPrincipal() {
		return this.principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	
	public boolean isSearchModeEquals() {
		return this.searchModeEquals;
	}

	public void setSearchModeEquals(boolean searchModeEquals) {
		this.searchModeEquals = searchModeEquals;
	}

}
