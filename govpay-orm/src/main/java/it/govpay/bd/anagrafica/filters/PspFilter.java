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

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.Psp;

public class PspFilter extends AbstractFilter {
	
	private String codPsp = null;
	private String ragioneSociale = null;

	public enum SortFields {
	}
	
	public PspFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}
	
	public PspFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.ragioneSociale != null){
				if(addAnd)
					newExpression.and();
				
				// 2. metto in and la stringa con la ragione sociale
				newExpression.ilike(Psp.model().RAGIONE_SOCIALE, this.ragioneSociale,LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.codPsp != null){
				if(addAnd)
					newExpression.and();
				
				// 2. metto in and la stringa con il codice psp
				newExpression.ilike(Psp.model().COD_PSP, this.codPsp,LikeMode.ANYWHERE);
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
	
	@Override
	public IExpression _toSimpleSearchExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			
			List<IExpression> orExpr = new ArrayList<IExpression>();
			if(this.simpleSearchString != null){
				IExpression codPspExpr = this.newExpression();
				codPspExpr.ilike(Psp.model().COD_PSP, this.simpleSearchString, LikeMode.ANYWHERE);
				orExpr.add(codPspExpr);
				IExpression ragioneSocialeExpr = this.newExpression();
				ragioneSocialeExpr.ilike(Psp.model().RAGIONE_SOCIALE, this.simpleSearchString, LikeMode.ANYWHERE);
				orExpr.add(ragioneSocialeExpr);
				newExpression.or(orExpr.toArray(new IExpression[orExpr.size()])); 
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

	public String getCodPsp() {
		return codPsp;
	}

	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	
	
}
