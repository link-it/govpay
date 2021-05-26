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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
import it.govpay.orm.Applicazione;
import it.govpay.orm.dao.jdbc.converter.ApplicazioneFieldConverter;
import it.govpay.orm.model.ApplicazioneModel;

public class ApplicazioneFilter extends AbstractFilter {

	private List<Long> listaIdApplicazioni = null;
	private String codApplicazione = null;
	private String principalOriginale = null;
	private boolean searchModeEquals = false; 
	
	private static ApplicazioneModel model = Applicazione.model();
	private ApplicazioneFieldConverter converter = null;
	
	
	public enum SortFields {
	}

	public ApplicazioneFilter(IExpressionConstructor expressionConstructor) throws ServiceException {
		this(expressionConstructor, false);
	}
	
	public ApplicazioneFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) throws ServiceException {
		super(expressionConstructor, simpleSearch);
		
		this.converter = new ApplicazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
		this.eseguiCountConLimit = false;
		
		this.listaFieldSimpleSearch.add(model.COD_APPLICAZIONE);
		this.fieldAbilitato = model.ID_UTENZA.ABILITATO;
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression(); 
			boolean addAnd = false;
			if(this.listaIdApplicazioni != null && this.listaIdApplicazioni.size() > 0){
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(model));
				newExpression.in(cf, this.listaIdApplicazioni);
				addAnd = true;
			}
			
			if(this.codApplicazione != null){
				if(addAnd)
					newExpression.and();
				if(!this.searchModeEquals)
					newExpression.ilike(model.COD_APPLICAZIONE, this.codApplicazione,LikeMode.ANYWHERE);
				else 
					newExpression.equals(model.COD_APPLICAZIONE, this.codApplicazione);
			}
			
			if(this.principalOriginale != null){
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(model.ID_UTENZA.PRINCIPAL_ORIGINALE, this.principalOriginale,LikeMode.ANYWHERE);
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
			if(this.listaIdApplicazioni != null && this.listaIdApplicazioni.size() > 0){
				this.listaIdApplicazioni.removeAll(Collections.singleton(null));
				
				String [] ids = this.listaIdApplicazioni.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.listaIdApplicazioni.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.COD_APPLICAZIONE, true) + ".id", false, ids );
			}
			
			if(this.codApplicazione != null){
				if(!this.searchModeEquals)
					sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.COD_APPLICAZIONE, true), this.codApplicazione, true, true);
				else 
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_APPLICAZIONE, true) + " = ? ");
			}
			
			if(this.principalOriginale != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.ID_UTENZA.PRINCIPAL_ORIGINALE, true), this.principalOriginale, true, true);
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
		
		if(this.listaIdApplicazioni != null && this.listaIdApplicazioni.size() > 0){
			// do nothing
		}
		
		if(this.codApplicazione != null){
			if(this.searchModeEquals)
				lst.add(this.codApplicazione);
		}
		
		if(this.principalOriginale != null){
			// do nothing
		}
		
		// filtro abilitato
		try {
			lst = this.setValoreFiltroAbilitato(lst, converter);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public List<Long> getListaIdApplicazioni() {
		return this.listaIdApplicazioni;
	}

	public void setListaIdApplicazioni(List<Long> listaIdApplicazioni) {
		this.listaIdApplicazioni = listaIdApplicazioni;
	}

	public String getCodApplicazione() {
		return this.codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public String getPrincipalOriginale() {
		return principalOriginale;
	}

	public void setPrincipalOriginale(String principalOriginale) {
		this.principalOriginale = principalOriginale;
	}

	public boolean isSearchModeEquals() {
		return this.searchModeEquals;
	}

	public void setSearchModeEquals(boolean searchModeEquals) {
		this.searchModeEquals = searchModeEquals;
	}
}
