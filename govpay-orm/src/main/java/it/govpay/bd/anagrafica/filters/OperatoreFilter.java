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

import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.Operatore;

public class OperatoreFilter extends AbstractFilter {
	
	private String principal= null;
	private boolean searchModeEquals = false; 

	public enum SortFields {
	}
	
	public OperatoreFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public OperatoreFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(Operatore.model().ID_UTENZA.PRINCIPAL);
		this.listaFieldSimpleSearch.add(Operatore.model().NOME);
		this.fieldAbilitato = it.govpay.orm.Operatore.model().ID_UTENZA.ABILITATO;
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.principal != null){
				if(!this.searchModeEquals)
					newExpression.ilike(Operatore.model().ID_UTENZA.PRINCIPAL, this.principal,LikeMode.ANYWHERE);
				else 
					newExpression.equals(Operatore.model().ID_UTENZA.PRINCIPAL, this.principal);
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
