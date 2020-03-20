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
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.Uo;
import it.govpay.orm.dao.jdbc.converter.UoFieldConverter;

public class UnitaOperativaFilter extends AbstractFilter {

	public enum SortFields {

	}

	private String dbType;
	private Long idDominio;
	private String codDominio;
	private List<Long> listaIdUo = null;
	private String codUo= null;
	private String codIdentificativo= null;
	private String ragioneSociale= null;
	private boolean excludeEC = false;
	private Boolean abilitato; 
	private boolean searchModeEquals = false; 

	public UnitaOperativaFilter(IExpressionConstructor expressionConstructor, String dbType) {
		this(expressionConstructor,dbType,false);
	}
	
	public UnitaOperativaFilter(IExpressionConstructor expressionConstructor, String dbType, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.dbType = dbType;
		this.listaFieldSimpleSearch.add(Uo.model().COD_UO);
		this.fieldAbilitato = it.govpay.orm.Uo.model().ABILITATO;
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			if(this.idDominio != null){
				UoFieldConverter fieldConverter = new UoFieldConverter(this.dbType);
				newExpression.equals(new CustomField("id_dominio", Long.class, "id_dominio", fieldConverter.toTable(it.govpay.orm.Uo.model())), this.idDominio);
				addAnd = true;
			}
			
			if(this.codDominio != null){
				if(addAnd) newExpression.and();
				newExpression.equals(Uo.model().ID_DOMINIO.COD_DOMINIO, this.codDominio);
				addAnd = true;
			}
			
			if(this.codUo != null){
				if(addAnd) newExpression.and();
				if(!this.searchModeEquals)
					newExpression.ilike(Uo.model().COD_UO, this.codUo, LikeMode.ANYWHERE);
				else 
					newExpression.equals(Uo.model().COD_UO, this.codUo);
				addAnd = true;
			}
			
			if(this.codIdentificativo != null){
				if(addAnd) newExpression.and();
				newExpression.ilike(Uo.model().UO_CODICE_IDENTIFICATIVO, this.codIdentificativo, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.ragioneSociale != null){
				if(addAnd) newExpression.and();
				newExpression.ilike(Uo.model().UO_DENOMINAZIONE, this.ragioneSociale, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.abilitato != null){
				if(addAnd) newExpression.and();
				newExpression.equals(Uo.model().ABILITATO, this.abilitato);
				addAnd = true;
			}
			
			// esclude tutte le UO con codUo = EC
			if(this.excludeEC){
				if(addAnd) newExpression.and();
				newExpression.notEquals(Uo.model().COD_UO, it.govpay.model.Dominio.EC);
				addAnd = true;
			}
			
			if(this.listaIdUo != null && this.listaIdUo.size() > 0){
				if(addAnd) newExpression.and();
				UoFieldConverter fieldConverter = new UoFieldConverter(this.dbType);
				newExpression.in(new CustomField("id", Long.class, "id", fieldConverter.toTable(it.govpay.orm.Uo.model())), this.listaIdUo);				
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
	
	@Override
	public IExpression _toSimpleSearchExpression() throws ServiceException {
		try {
			IExpression newExpression = super._toSimpleSearchExpression();
			boolean addAnd = false;
			
			if(this.idDominio != null){
				UoFieldConverter fieldConverter = new UoFieldConverter(this.dbType);
				newExpression.equals(new CustomField("id_dominio", Long.class, "id_dominio", fieldConverter.toTable(it.govpay.orm.Uo.model())), this.idDominio);
				addAnd = true;
			}
			
			// esclude tutte le UO con codUo = EC
			if(this.excludeEC){
				if(addAnd) newExpression.and();
				newExpression.notEquals(Uo.model().COD_UO, it.govpay.model.Dominio.EC);
				addAnd = true;
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
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		return null;
	}

	@Override
	public Object[] getParameters(ISQLQueryObject sqlQueryObject) throws ServiceException {
		return null;
	}

	public void setDominioFilter(long idDominio) {
		this.idDominio = idDominio;
	}

	public List<Long> getListaIdUo() {
		return this.listaIdUo;
	}

	public void setListaIdUo(List<Long> listaIdUo) {
		this.listaIdUo = listaIdUo;
	}

	public String getCodUo() {
		return this.codUo;
	}

	public void setCodUo(String codUo) {
		this.codUo = codUo;
	}

	public boolean isExcludeEC() {
		return this.excludeEC;
	}

	public void setExcludeEC(boolean excludeEC) {
		this.excludeEC = excludeEC;
	}

	public void setCodIdentificativo(String codIdentificativo) {
		this.codIdentificativo = codIdentificativo;
	}
	
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	
	public boolean isSearchModeEquals() {
		return this.searchModeEquals;
	}

	public void setSearchModeEquals(boolean searchModeEquals) {
		this.searchModeEquals = searchModeEquals;
	}
}
