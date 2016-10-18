/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.Uo;
import it.govpay.orm.dao.jdbc.converter.UoFieldConverter;

public class UnitaOperativaFilter extends AbstractFilter {

	public enum SortFields {

	}

	private String dbType;
	private Long idDominio;
	private List<Long> listaIdUo = null;
	private String codUo= null;
	private boolean excludeEC = false;

	public UnitaOperativaFilter(IExpressionConstructor expressionConstructor, String dbType) {
		super(expressionConstructor);
		this.dbType = dbType;
	}

	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			if(this.idDominio != null){
				UoFieldConverter fieldConverter = new UoFieldConverter(dbType);
				newExpression.equals(new CustomField("id_dominio", Long.class, "id_dominio", fieldConverter.toTable(it.govpay.orm.Uo.model())), idDominio);
				addAnd = true;
			}
			
			if(this.codUo != null){
				if(addAnd) newExpression.and();
				newExpression.ilike(Uo.model().COD_UO, this.codUo,LikeMode.ANYWHERE);
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
				UoFieldConverter fieldConverter = new UoFieldConverter(dbType);
				
				newExpression.in(new CustomField("id", Long.class, "id", fieldConverter.toTable(it.govpay.orm.Uo.model())), listaIdUo);				
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

	public void setDominioFilter(long idDominio) {
		this.idDominio = idDominio;
	}

	public List<Long> getListaIdUo() {
		return listaIdUo;
	}

	public void setListaIdUo(List<Long> listaIdUo) {
		this.listaIdUo = listaIdUo;
	}

	public String getCodUo() {
		return codUo;
	}

	public void setCodUo(String codUo) {
		this.codUo = codUo;
	}

	public boolean isExcludeEC() {
		return excludeEC;
	}

	public void setExcludeEC(boolean excludeEC) {
		this.excludeEC = excludeEC;
	}
}
