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

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.TipoTributo;
import it.govpay.orm.dao.jdbc.converter.TipoTributoFieldConverter;

public class TipoTributoFilter extends AbstractFilter {
	
	private String codTributo = null;
	private String codContabilita = null;
	private String codificaTipoContabilita = null;
	private String descrizione = null;
	private List<Long> listaIdTributi = null;
	private List<Long> listaIdTributiDaEscludere = null;
	private CustomField cf;
	private boolean searchModeEquals = false; 
	
	public enum SortFields { }
	
	public TipoTributoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public TipoTributoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		
		try{
			TipoTributoFieldConverter converter = new TipoTributoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			this.cf = new CustomField("id", Long.class, "id", converter.toTable(it.govpay.orm.TipoTributo.model()));
			this.listaFieldSimpleSearch.add(TipoTributo.model().COD_TRIBUTO);
			this.listaFieldSimpleSearch.add(TipoTributo.model().COD_CONTABILITA);
			this.listaFieldSimpleSearch.add(TipoTributo.model().DESCRIZIONE);
		} catch(Exception e){
			
		}
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
					newExpression.ilike(it.govpay.orm.TipoTributo.model().COD_TRIBUTO, this.codTributo,LikeMode.ANYWHERE);
				else 
					newExpression.equals(it.govpay.orm.TipoTributo.model().COD_TRIBUTO, this.codTributo);
				addAnd = true;
			}
			
			if(this.codContabilita != null && StringUtils.isNotEmpty(this.codContabilita)){
				if(addAnd)
					newExpression.and();
				newExpression.ilike(it.govpay.orm.TipoTributo.model().COD_CONTABILITA, this.codContabilita,LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.codificaTipoContabilita != null && StringUtils.isNotEmpty(this.codificaTipoContabilita)){
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.TipoTributo.model().TIPO_CONTABILITA, this.codificaTipoContabilita);
				addAnd = true;
			}
			
			if(this.descrizione != null && StringUtils.isNotEmpty(this.descrizione)){
				if(addAnd)
					newExpression.and();
				newExpression.ilike(it.govpay.orm.TipoTributo.model().DESCRIZIONE, this.descrizione,LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.listaIdTributi != null && this.listaIdTributi.size() > 0){
				if(addAnd)
					newExpression.and();
				newExpression.in(this.cf, this.listaIdTributi);
				
				addAnd = true;
			}
			
			if(this.listaIdTributiDaEscludere != null && this.listaIdTributiDaEscludere.size() > 0){
				if(addAnd)
					newExpression.and();
				
				IExpression notExpression = this.newExpression();
				notExpression.not().in(this.cf, this.listaIdTributiDaEscludere);
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
