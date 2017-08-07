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
import it.govpay.orm.dao.jdbc.converter.TributoFieldConverter;

public class TributoFilter extends AbstractFilter {
	
	private String codDominio = null;
	private List<Long> listaIdTributi = null;
	private CustomField cf;
	private Long idDominio;
	private String codTributo = null;
	private String descrizione = null; //TODO
	
	public enum SortFields { }
	
	public TributoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public TributoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		try{
			TributoFieldConverter converter = new TributoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			this.cf = new CustomField("id", Long.class, "id", converter.toTable(it.govpay.orm.Tributo.model()));
		} catch(Exception e){
			
		}
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			if(this.idDominio != null){
				TributoFieldConverter fieldConverter = new TributoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
				newExpression.equals(new CustomField("id_dominio", Long.class, "id_dominio", fieldConverter.toTable(it.govpay.orm.Tributo.model())), idDominio);
				addAnd = true;
			}
			
			if(this.getCodDominio() != null && StringUtils.isNotEmpty(this.getCodDominio())){
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.Tributo.model().ID_DOMINIO.COD_DOMINIO, this.getCodDominio());
				addAnd = true;
			}
			
			if(this.listaIdTributi != null && this.listaIdTributi.size() > 0){
				if(addAnd)
					newExpression.and();
				newExpression.in(cf, listaIdTributi);
				
				addAnd = true;
			}
			
			if(this.codTributo != null && StringUtils.isNotEmpty(this.codTributo)){
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(it.govpay.orm.Tributo.model().TIPO_TRIBUTO.COD_TRIBUTO, this.codTributo,LikeMode.ANYWHERE);
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
			IExpression newExpression = super._toSimpleSearchExpression();
		//	boolean addAnd = false;
			
			if(this.idDominio != null){
				TributoFieldConverter fieldConverter = new TributoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
				newExpression.equals(new CustomField("id_dominio", Long.class, "id_dominio", fieldConverter.toTable(it.govpay.orm.Tributo.model())), idDominio);
			//	addAnd = true;
			}

			return newExpression;
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

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public List<Long> getListaIdTributi() {
		return listaIdTributi;
	}

	public void setListaIdTributi(List<Long> listaIdTributi) {
		this.listaIdTributi = listaIdTributi;
	}

	public Long getIdDominio() {
		return idDominio;
	}

	public void setIdDominio(Long idDominio) {
		this.idDominio = idDominio;
	}

	public String getCodTributo() {
		return codTributo;
	}

	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	
}
