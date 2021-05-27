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
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.IbanAccredito;
import it.govpay.orm.dao.jdbc.converter.IbanAccreditoFieldConverter;
import it.govpay.orm.model.IbanAccreditoModel;

public class IbanAccreditoFilter extends AbstractFilter {

	// Filtro che indica che voglio gli iban associati al dominio.
	private String codDominio;
	private Long idDominio;
	private String codIbanAccredito;
	private Boolean postale;
	private boolean searchModeEquals = false; 
	private String descrizione;
	
	private static IbanAccreditoModel model = IbanAccredito.model();
	private IbanAccreditoFieldConverter converter = null;

	public enum SortFields {
		COD_IBAN, DESCRIZIONE
	}

	public IbanAccreditoFilter(IExpressionConstructor expressionConstructor) throws ServiceException {
		this(expressionConstructor,false);
	}
	
	public IbanAccreditoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) throws ServiceException {
		super(expressionConstructor, simpleSearch);
		this.fieldAbilitato = model.ABILITATO;
		this.converter = new IbanAccreditoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
		this.eseguiCountConLimit = false;
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			boolean addAnd = false;
			IExpression expr = this.newExpression();
			if(StringUtils.isNotEmpty(this.getCodDominio())){
				expr.equals(model.ID_DOMINIO.COD_DOMINIO, this.getCodDominio());
				addAnd = true;
			} 
			if(this.getIdDominio() != null){
				if(addAnd) expr.and();
				expr.equals(new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(model)), this.getIdDominio());
				addAnd = true;
			}
			
			if(this.postale != null){
				if(addAnd) expr.and();
				expr.equals(model.POSTALE, this.postale);
				addAnd = true;
			}
			
			
			if(this.codIbanAccredito != null){
				if(addAnd) expr.and();
				if(!this.searchModeEquals)
					expr.ilike(model.COD_IBAN, this.codIbanAccredito,LikeMode.ANYWHERE);
				else 
					expr.equals(model.COD_IBAN, this.codIbanAccredito);
				addAnd = true;
			}
			
			if(this.descrizione != null){
				if(addAnd) expr.and();
				if(!this.searchModeEquals)
					expr.ilike(model.DESCRIZIONE, this.descrizione,LikeMode.ANYWHERE);
				else 
					expr.equals(model.DESCRIZIONE, this.descrizione);
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
		case COD_IBAN: filterSortWrapper.setField(model.COD_IBAN);
		case DESCRIZIONE: filterSortWrapper.setField(model.DESCRIZIONE);
		break;
		default:
			break;
		}

		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			boolean addTabellaDomini = false;
			
			if(StringUtils.isNotEmpty(this.getCodDominio())){
				if(!addTabellaDomini) {
					sqlQueryObject.addFromTable(converter.toTable(model.ID_DOMINIO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.COD_IBAN, true) + ".id_dominio="
							+converter.toTable(model.ID_DOMINIO, true)+".id");

					addTabellaDomini = true;
				}

				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_DOMINIO.COD_DOMINIO, true) + " = ? ");
			} 
			
			if(this.getIdDominio() != null){
				sqlQueryObject.addWhereCondition(true,converter.toTable(model.COD_IBAN, true) + ".id_dominio" + " = ? ");
			}
			
			if(this.postale != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.POSTALE, true) + " = ? ");
			}
			
			
			if(this.codIbanAccredito != null){
				if(!this.searchModeEquals)
					sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.COD_IBAN, true), this.codIbanAccredito, true, true);
				else 
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_IBAN, true) + " = ? ");
			}
			
			if(this.descrizione != null){
				if(!this.searchModeEquals)
					sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.DESCRIZIONE, true), this.descrizione, true, true);
				else 
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DESCRIZIONE, true) + " = ? ");
			}
			
			// filtro abilitato
			sqlQueryObject = this.setFiltroAbilitato(sqlQueryObject, converter);
			
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
		
		if(StringUtils.isNotEmpty(this.getCodDominio())){
			lst.add(this.getCodDominio());
		} 
		
		if(this.getIdDominio() != null){
			lst.add(this.getIdDominio());
		}
		
		if(this.postale != null){
			try {
				lst = this.setValoreFiltroBoolean(lst, converter, this.postale);
			} catch (ExpressionException e) {
				throw new ServiceException(e);
			}
		}
		
		if(this.codIbanAccredito != null){
			if(this.searchModeEquals)
				lst.add(this.codIbanAccredito);
		}
		
		if(this.descrizione != null){
			if(this.searchModeEquals)
				lst.add(this.descrizione);
		}

		// filtro abilitato
		try {
			lst = this.setValoreFiltroAbilitato(lst, converter);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
		
		return lst.toArray(new Object[lst.size()]);
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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
