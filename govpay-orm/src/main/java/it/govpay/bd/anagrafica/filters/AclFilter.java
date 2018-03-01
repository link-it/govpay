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
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.ACL;

public class AclFilter extends AbstractFilter {

	private String ruolo = null;
	private String principal = null;
	private String servizio = null;
	private Boolean forceServizio = null;
	private Boolean forceRuolo = null;
	private Boolean forcePrincipal = null;
	
	public enum SortFields {
	}

	public AclFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor, false);
	}
	
	public AclFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		
		try{
			this.listaFieldSimpleSearch.add(ACL.model().RUOLO);
		} catch(Exception e){
			
		}
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression(); 
			boolean addAnd = false;
			
			if(this.ruolo != null){
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(ACL.model().RUOLO, this.ruolo);
			}
			
			if(this.forceRuolo != null && this.forceRuolo.booleanValue() == true){
				if(addAnd)
					newExpression.and();
				
				newExpression.isNotNull(ACL.model().RUOLO);
			}
			
			if(this.principal != null){
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(ACL.model().PRINCIPAL, this.principal);
			}
			
			if(this.forcePrincipal != null && this.forcePrincipal.booleanValue() == true){
				if(addAnd)
					newExpression.and();
				
				newExpression.isNotNull(ACL.model().PRINCIPAL);
			}
			
			if(this.servizio != null){
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(ACL.model().SERVIZIO, this.servizio);
			}

			if(this.forceServizio != null && this.forceServizio.booleanValue() == true){
				if(addAnd)
					newExpression.and();
				
				newExpression.isNotNull(ACL.model().SERVIZIO);
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

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public Boolean getForceRuolo() {
		return forceRuolo;
	}

	public void setForceRuolo(Boolean forceRuolo) {
		this.forceRuolo = forceRuolo;
	}

	public Boolean getForcePrincipal() {
		return forcePrincipal;
	}

	public void setForcePrincipal(Boolean forcePrincipal) {
		this.forcePrincipal = forcePrincipal;
	}

	public String getServizio() {
		return servizio;
	}

	public void setServizio(String servizio) {
		this.servizio = servizio;
	}
	
}
