/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import java.util.List;

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
import it.govpay.orm.dao.jdbc.converter.EnteFieldConverter;

public class EnteFilter extends AbstractFilter {
	
	

	public enum SortFields {
//TODO		COD_PSP, COD_FLUSSO
		}

	private String codDominio;
	private List<Long> listaIdEnti = null;
	private CustomField cf;
	
	public EnteFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
		
		try{
			EnteFieldConverter converter = new EnteFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			this.cf = new CustomField("id", Long.class, "id", converter.toTable(it.govpay.orm.Ente.model()));
		} catch(Exception e){
			
		}
	}

	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			
			boolean addAnd = false;
			if(this.getCodDominio() != null){
			newExpression.equals(it.govpay.orm.Ente.model().ID_DOMINIO.COD_DOMINIO, this.getCodDominio());
			addAnd =true;
			}
			
			if(this.listaIdEnti != null && this.listaIdEnti.size() > 0){
				if(addAnd)
					newExpression.and();
				
				
				newExpression.in(cf, listaIdEnti);				
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
		
		//TODO
//		switch(field) {
//		case COD_FLUSSO: filterSortWrapper.setField(Psp.model().COD_FLUSSO);
//			break;
//		case COD_PSP: filterSortWrapper.setField(Psp.model().COD_PSP);
//			break;
//		default:
//			break;
//		}
		
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public List<Long> getListaIdEnti() {
		return listaIdEnti;
	}

	public void setListaIdEnti(List<Long> listaIdEnti) {
		this.listaIdEnti = listaIdEnti;
	}
	
}
