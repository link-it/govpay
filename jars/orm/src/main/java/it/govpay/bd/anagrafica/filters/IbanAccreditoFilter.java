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

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.sql.ISQLQueryObject;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.IbanAccredito;
import it.govpay.orm.dao.jdbc.converter.IbanAccreditoFieldConverter;

public class IbanAccreditoFilter extends AbstractFilter {

	// Filtro che indica che voglio gli iban associati al dominio.
	private String codDominio;
	private Long idDominio;
	private String codIbanAccredito;
	private Boolean postale;
	private boolean searchModeEquals = false; 

	public enum SortFields {
		COD_IBAN
	}

	public IbanAccreditoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public IbanAccreditoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.fieldAbilitato = it.govpay.orm.IbanAccredito.model().ABILITATO;
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			boolean addAnd = false;
			IExpression expr = this.newExpression();
			if(StringUtils.isNotEmpty(this.getCodDominio())){
				expr.equals(IbanAccredito.model().ID_DOMINIO.COD_DOMINIO, this.getCodDominio());
				addAnd = true;
			} 
			if(this.getIdDominio() != null){
				if(addAnd) expr.and();
				IbanAccreditoFieldConverter fieldConverter = new IbanAccreditoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
				expr.equals(new CustomField("id_dominio", Long.class, "id_dominio", fieldConverter.toTable(it.govpay.orm.IbanAccredito.model())), this.getIdDominio());
				addAnd = true;
			}
			
			if(this.postale != null){
				if(addAnd) expr.and();
				expr.equals(IbanAccredito.model().POSTALE, this.postale);
				addAnd = true;
			}
			
			
			if(this.codIbanAccredito != null){
				if(addAnd) expr.and();
				if(!this.searchModeEquals)
					expr.ilike(IbanAccredito.model().COD_IBAN, this.codIbanAccredito,LikeMode.ANYWHERE);
				else 
					expr.equals(IbanAccredito.model().COD_IBAN, this.codIbanAccredito);
				addAnd = true;
			}
			
			addAnd = this.setFiltroAbilitato(expr, addAnd);
			
			return expr;
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

		switch(field) {
		case COD_IBAN: filterSortWrapper.setField(IbanAccredito.model().COD_IBAN);
		break;
		default:
			break;
		}

		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		return null;
	}

	@Override
	public Object[] getParameters(ISQLQueryObject sqlQueryObject) throws ServiceException {
		return null;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public Long getIdDominio() {
		return this.idDominio;
	}

	public void setIdDominio(Long idDominio) {
		this.idDominio = idDominio;
	}
	
	public void setCodIbanAccredito(String codIbanAccredito) {
		this.codIbanAccredito = codIbanAccredito;
	}

	public Boolean getPostale() {
		return this.postale;
	}

	public void setPostale(Boolean postale) {
		this.postale = postale;
	}

	public boolean isSearchModeEquals() {
		return this.searchModeEquals;
	}

	public void setSearchModeEquals(boolean searchModeEquals) {
		this.searchModeEquals = searchModeEquals;
	}
}
