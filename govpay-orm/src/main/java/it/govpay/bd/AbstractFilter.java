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
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.generic_project.expression.impl.sql.ISQLFieldConverter;

public abstract class AbstractFilter implements IFilter {
	
	public static final String ALIAS_ID = "id";
	
	public AbstractFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor, false);
	}
	
	public AbstractFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		this.expressionConstructor = expressionConstructor;
		this.filterSortList = new ArrayList<FilterSortWrapper>();
		this.simpleSearch = simpleSearch;
	}
	
	private IExpressionConstructor expressionConstructor;
	private Integer offset;
	private Integer limit;
	protected List<FilterSortWrapper> filterSortList;
	protected boolean simpleSearch = false;
	protected String simpleSearchString = null;

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public List<FilterSortWrapper> getFilterSortList() {
		return filterSortList;
	}

	public void setFilterSortList(List<FilterSortWrapper> filterSortList) {
		this.filterSortList = filterSortList;
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
				FilterSortWrapper filterSort = getDefaultFilterSortWrapper();
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
			return _toExpression();
		else 
			return _toSimpleSearchExpression();
	}
	
	public abstract IExpression _toExpression() throws ServiceException;
	
	public abstract IExpression _toSimpleSearchExpression() throws ServiceException;

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
		ISQLFieldConverter converter = ((IDBServiceUtilities<?>)this.expressionConstructor).getFieldConverter();
		return converter.toColumn(field,false);
	}
	
	protected FilterSortWrapper getDefaultFilterSortWrapper() throws ServiceException {
		try {
			CustomField baseField = new CustomField("id", Long.class, "id", getRootTable());
			FilterSortWrapper wrapper = new FilterSortWrapper();
			wrapper.setField(baseField);
			wrapper.setSortOrder(SortOrder.ASC);
			return wrapper;
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	public boolean isSimpleSearch() {
		return simpleSearch;
	}

	public void setSimpleSearch(boolean simpleSearch) {
		this.simpleSearch = simpleSearch;
	}

	public String getSimpleSearchString() {
		return simpleSearchString;
	}

	public void setSimpleSearchString(String simpleSearchString) {
		this.simpleSearchString = simpleSearchString;
	}
}
