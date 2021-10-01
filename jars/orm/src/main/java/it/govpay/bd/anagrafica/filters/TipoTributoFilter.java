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
import it.govpay.orm.TipoTributo;
import it.govpay.orm.dao.jdbc.converter.TipoTributoFieldConverter;
import it.govpay.orm.model.TipoTributoModel;

public class TipoTributoFilter extends AbstractFilter {
	
	private String codTributo = null;
	private String codContabilita = null;
	private String codificaTipoContabilita = null;
	private String descrizione = null;
	private List<Long> listaIdTributi = null;
	private List<Long> listaIdTributiDaEscludere = null;
	private boolean searchModeEquals = false; 
	
	private static TipoTributoModel model = TipoTributo.model();
	private TipoTributoFieldConverter converter = null;
	
	public enum SortFields { }
	
	public TipoTributoFilter(IExpressionConstructor expressionConstructor) throws ServiceException {
		this(expressionConstructor,false);
	}
	
	public TipoTributoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) throws ServiceException {
		super(expressionConstructor, simpleSearch);
		
		this.converter = new TipoTributoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
		this.eseguiCountConLimit = false;
		
		this.listaFieldSimpleSearch.add(TipoTributo.model().COD_TRIBUTO);
		this.listaFieldSimpleSearch.add(TipoTributo.model().COD_CONTABILITA);
		this.listaFieldSimpleSearch.add(TipoTributo.model().DESCRIZIONE);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			if(this.codTributo != null && StringUtils.isNotEmpty(this.codTributo)){
				if(addAnd)
					newExpression.and();
				if(!this.searchModeEquals)
					newExpression.ilike(model.COD_TRIBUTO, this.codTributo,LikeMode.ANYWHERE);
				else 
					newExpression.equals(model.COD_TRIBUTO, this.codTributo);
				addAnd = true;
			}
			
			if(this.codContabilita != null && StringUtils.isNotEmpty(this.codContabilita)){
				if(addAnd)
					newExpression.and();
				newExpression.ilike(model.COD_CONTABILITA, this.codContabilita,LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.codificaTipoContabilita != null && StringUtils.isNotEmpty(this.codificaTipoContabilita)){
				if(addAnd)
					newExpression.and();
				newExpression.equals(model.TIPO_CONTABILITA, this.codificaTipoContabilita);
				addAnd = true;
			}
			
			if(this.descrizione != null && StringUtils.isNotEmpty(this.descrizione)){
				if(addAnd)
					newExpression.and();
				newExpression.ilike(model.DESCRIZIONE, this.descrizione,LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.listaIdTributi != null && this.listaIdTributi.size() > 0){
				if(addAnd)
					newExpression.and();
				
				CustomField	cf = new CustomField("id", Long.class, "id", converter.toTable(model));
				newExpression.in(cf, this.listaIdTributi);
				
				addAnd = true;
			}
			
			if(this.listaIdTributiDaEscludere != null && this.listaIdTributiDaEscludere.size() > 0){
				if(addAnd)
					newExpression.and();
				
				IExpression notExpression = this.newExpression();
				
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(model));
				notExpression.not().in(cf, this.listaIdTributiDaEscludere);
				newExpression.and(notExpression);
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
	
	public void addSortField(SortFields field, boolean asc) {
		FilterSortWrapper filterSortWrapper = new FilterSortWrapper();
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			if(this.codTributo != null && StringUtils.isNotEmpty(this.codTributo)){
				if(!this.searchModeEquals)
					sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.COD_TRIBUTO, true), this.codTributo, true, true);
				else 
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_TRIBUTO, true) + " = ? ");
			}
			
			if(this.codContabilita != null && StringUtils.isNotEmpty(this.codContabilita)){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.COD_CONTABILITA, true), this.codContabilita, true, true);
			}
			
			if(this.codificaTipoContabilita != null && StringUtils.isNotEmpty(this.codificaTipoContabilita)){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.TIPO_CONTABILITA, true) + " = ? ");
			}
			
			if(this.descrizione != null && StringUtils.isNotEmpty(this.descrizione)){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.DESCRIZIONE, true), this.descrizione, true, true);
			}
			
			if(this.listaIdTributi != null && this.listaIdTributi.size() > 0){
				this.listaIdTributi.removeAll(Collections.singleton(null));
				
				String [] ids = this.listaIdTributi.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.listaIdTributi.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.COD_TRIBUTO, true) + ".id", false, ids );
			}
			
			if(this.listaIdTributiDaEscludere != null && this.listaIdTributiDaEscludere.size() > 0){
				this.listaIdTributiDaEscludere.removeAll(Collections.singleton(null));
				
				String [] ids = this.listaIdTributiDaEscludere.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.listaIdTributiDaEscludere.size()]);
				
				ISQLQueryObject sqlQueryObjectNot = sqlQueryObject.newSQLQueryObject();
				sqlQueryObjectNot.addFromTable(converter.toTable(model.COD_TRIBUTO));
				sqlQueryObjectNot.addSelectField(converter.toTable(model.COD_TRIBUTO), "id");
				
				sqlQueryObjectNot.addWhereINCondition(converter.toTable(model.COD_TRIBUTO, true) + ".id", false, ids );
				
				sqlQueryObject.addWhereINSelectSQLCondition(true, converter.toTable(model.COD_TRIBUTO, true) + ".id", sqlQueryObjectNot);
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
		
		if(this.codTributo != null && StringUtils.isNotEmpty(this.codTributo)){
			if(this.searchModeEquals)
				lst.add(this.codTributo);
		}
		
		if(this.codContabilita != null && StringUtils.isNotEmpty(this.codContabilita)){
			// do nothing
		}
		
		if(this.codificaTipoContabilita != null && StringUtils.isNotEmpty(this.codificaTipoContabilita)){
			lst.add(this.codificaTipoContabilita);
		}
		
		if(this.descrizione != null && StringUtils.isNotEmpty(this.descrizione)){
			// do nothing
		}
		
		if(this.listaIdTributi != null && this.listaIdTributi.size() > 0){
			// do nothing
		}
		
		if(this.listaIdTributiDaEscludere != null && this.listaIdTributiDaEscludere.size() > 0){
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

	public List<Long> getListaIdTributi() {
		return this.listaIdTributi;
	}

	public void setListaIdTributi(List<Long> listaIdTributi) {
		this.listaIdTributi = listaIdTributi;
	}

	public String getCodTributo() {
		return this.codTributo;
	}

	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}

	public String getCodContabilita() {
		return this.codContabilita;
	}

	public void setCodContabilita(String codContabilita) {
		this.codContabilita = codContabilita;
	}

	public String getCodificaTipoContabilita() {
		return this.codificaTipoContabilita;
	}

	public void setCodificaTipoContabilita(String codificaTipoContabilita) {
		this.codificaTipoContabilita = codificaTipoContabilita;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<Long> getListaIdTributiDaEscludere() {
		return this.listaIdTributiDaEscludere;
	}

	public void setListaIdTributiDaEscludere(List<Long> listaIdTributiDaEscludere) {
		this.listaIdTributiDaEscludere = listaIdTributiDaEscludere;
	}
	
	public boolean isSearchModeEquals() {
		return this.searchModeEquals;
	}

	public void setSearchModeEquals(boolean searchModeEquals) {
		this.searchModeEquals = searchModeEquals;
	}
}
