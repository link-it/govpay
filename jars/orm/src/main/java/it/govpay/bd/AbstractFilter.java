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
package it.govpay.bd;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.IModel;
import org.openspcoop2.generic_project.dao.IDBServiceUtilities;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.generic_project.expression.impl.sql.AbstractSQLFieldConverter;
import org.openspcoop2.generic_project.expression.impl.sql.ISQLFieldConverter;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

public abstract class AbstractFilter implements IFilter {
	
	public static final String ALIAS_ID = "id";
	
	public AbstractFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor, false);
	}
	
	public AbstractFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		this.expressionConstructor = expressionConstructor;
		this.filterSortList = new ArrayList<>();
		this.simpleSearch = simpleSearch;
		this.listaFieldSimpleSearch = new ArrayList<>();
	}
	
	private IExpressionConstructor expressionConstructor;
	private Integer offset;
	private Integer limit;
	protected List<FilterSortWrapper> filterSortList;
	protected boolean simpleSearch = false;
	protected String simpleSearchString = null;
	protected List<IField> listaFieldSimpleSearch = null;
	
	protected IField fieldAbilitato = null;
	protected Boolean searchAbilitato = null;
	
	protected boolean eseguiCountConLimit = true;

	public Integer getOffset() {
		return this.offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return this.limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public List<FilterSortWrapper> getFilterSortList() {
		return this.filterSortList;
	}

	public void setFilterSortList(List<FilterSortWrapper> filterSortList) {
		this.filterSortList = filterSortList;
	}
	
	public void addFilterSort(FilterSortWrapper filterSort) {
		this.filterSortList.add(filterSort);
	}

	protected IExpression newExpression() throws ServiceException, NotImplementedException {
		return this.expressionConstructor.newExpression();
	}
	
	@Override
	public IPaginatedExpression toPaginatedExpression() throws ServiceException {
		try {
			IPaginatedExpression exp = this.expressionConstructor.toPaginatedExpression(this.toExpression());
			
			if(this.filterSortList != null && !this.filterSortList.isEmpty()){
			
				for(FilterSortWrapper filterSort: this.filterSortList) {
					exp.addOrder(filterSort.getField(), filterSort.getSortOrder());
				}
			} else {
				FilterSortWrapper filterSort = this.getDefaultFilterSortWrapper();
				exp.addOrder(filterSort.getField(), filterSort.getSortOrder());
			}
			
			
			if(this.offset != null) {
				exp.offset(this.offset);
			}
	
			if(this.limit != null) {
				exp.limit(this.limit);
			}
			
			return exp;
			
		}catch(ExpressionException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
		
	}
	
	@Override
	public IExpression toExpression() throws ServiceException {
		if(!this.simpleSearch)
			return this._toExpression();
		else 
			return this._toSimpleSearchExpression();
	}
	
	public abstract IExpression _toExpression() throws ServiceException;
	
	public abstract ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException;
	
	public abstract Object[] getParameters(ISQLQueryObject sqlQueryObject) throws ServiceException;
	
	/**
	 * Implementazione base della ricerca semplice, mette in or la ricerca per like anywere di tutti i campi indicati nella lista listaFieldSimpleSearch
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public IExpression _toSimpleSearchExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression(); 
			
			if(this.simpleSearchString != null && this.listaFieldSimpleSearch.size() > 0){
				List<IExpression> orExpr = this.getSimpleSearchExpression();
				newExpression.or(orExpr.toArray(new IExpression[orExpr.size()])); 
			}
			
			this.setFiltroAbilitato(newExpression, true);
			
			return newExpression;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	protected String getRootTable() throws ExpressionException {
		ISQLFieldConverter converter = ((IDBServiceUtilities<?>)this.expressionConstructor).getFieldConverter();
		IModel<?> model = converter.getRootModel();
		return converter.toTable(model);
	}

	protected String getTable(IModel<?> model) throws ExpressionException {
		ISQLFieldConverter converter = ((IDBServiceUtilities<?>)this.expressionConstructor).getFieldConverter();
		return converter.toTable(model);
	}
	
	protected String getColumn(IField field) throws ExpressionException {
		return this.getColumn(field, false);
	}
	
	protected String getColumn(IField field, boolean includeTableName) throws ExpressionException {
		ISQLFieldConverter converter = ((IDBServiceUtilities<?>)this.expressionConstructor).getFieldConverter();
		return converter.toColumn(field,includeTableName);
	}
	
	protected FilterSortWrapper getDefaultFilterSortWrapper() throws ServiceException {
		try {
			CustomField baseField = new CustomField("id", Long.class, "id", this.getRootTable());
			FilterSortWrapper wrapper = new FilterSortWrapper(baseField,SortOrder.ASC);
			return wrapper;
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	public FilterSortWrapper getDefaultFilterSortWrapperDesc() throws ServiceException {
		try {
			CustomField baseField = new CustomField("id", Long.class, "id", this.getRootTable());
			FilterSortWrapper fsw = new FilterSortWrapper(baseField, SortOrder.DESC);
			return fsw;
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	protected boolean setFiltroAbilitato(IExpression newExpression, boolean addAnd) throws ExpressionNotImplementedException, ExpressionException {
		if(this.searchAbilitato != null && this.fieldAbilitato != null) {
			if(addAnd)
				newExpression.and();
			
			newExpression.equals(this.fieldAbilitato, this.searchAbilitato);
			addAnd = true;
		}
		
		return addAnd;
	}
	
	protected ISQLQueryObject setFiltroAbilitato(ISQLQueryObject sqlQueryObject, AbstractSQLFieldConverter converter) throws SQLQueryObjectException, ExpressionException {
		if(this.searchAbilitato != null && this.fieldAbilitato != null) {
			sqlQueryObject.addWhereCondition(true,converter.toColumn(this.fieldAbilitato, true) + " = ? ");
		}
		
		return sqlQueryObject;
	}
	
	protected List<Object> setValoreFiltroAbilitato(List<Object> lst, AbstractSQLFieldConverter converter) throws ExpressionException {
		if(this.searchAbilitato != null && this.fieldAbilitato != null) {
			return this.setValoreFiltroBoolean(lst, converter, searchAbilitato);
		}
		
		return lst;
	}
	
	protected List<Object> setValoreFiltroBoolean(List<Object> lst, AbstractSQLFieldConverter converter, Boolean valoreFiltro) throws ExpressionException {
		if(valoreFiltro != null) {
			
			org.openspcoop2.utils.TipiDatabase tipoDatabase = converter.getDatabaseType();
			
			switch (tipoDatabase) {
			case ORACLE:
			case DB2:
			case SQLSERVER:
				lst.add(valoreFiltro ? 1 : 0);
				break;
			case DERBY:
			case HSQL:
			case MYSQL:
			case POSTGRESQL:
			default:
				lst.add(valoreFiltro);
				break;
			}
		}
		
		return lst;
	}

	public Boolean getSearchAbilitato() {
		return this.searchAbilitato;
	}

	public void setSearchAbilitato(Boolean searchAbilitato) {
		this.searchAbilitato = searchAbilitato;
	}

	public boolean isSimpleSearch() {
		return this.simpleSearch;
	}

	public void setSimpleSearch(boolean simpleSearch) {
		this.simpleSearch = simpleSearch;
	}

	public String getSimpleSearchString() {
		return this.simpleSearchString;
	}

	public void setSimpleSearchString(String simpleSearchString) {
		this.simpleSearchString = simpleSearchString;
	}

	public List<IField> getListaFieldSimpleSearch() {
		return this.listaFieldSimpleSearch;
	}

	public void setListaFieldSimpleSearch(List<IField> listaFieldSimpleSearch) {
		this.listaFieldSimpleSearch = listaFieldSimpleSearch;
	}
	
	protected List<IExpression> getSimpleSearchExpression() throws ServiceException, ExpressionNotImplementedException, ExpressionException, NotImplementedException{
		List<IExpression> expressions = new ArrayList<>();
		for (IField field : this.listaFieldSimpleSearch) {
			IExpression expr = this.newExpression();
			expr.ilike(field, this.simpleSearchString,LikeMode.ANYWHERE);
			expressions.add(expr);
		}
		return expressions;
	}

	public void setExpressionConstructor(IExpressionConstructor expressionConstructor) {
		this.expressionConstructor = expressionConstructor;
	}

	public boolean isEseguiCountConLimit() {
		return eseguiCountConLimit;
	}

	public void setEseguiCountConLimit(boolean eseguiCountConLimit) {
		this.eseguiCountConLimit = eseguiCountConLimit;
	}
}
