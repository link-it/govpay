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
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.Stazione;

public class StazioneFilter extends AbstractFilter {

	// Filtro che indica che voglio solo le stazioni dell'intermediario passato.
	private String codIntermediario;

	public enum SortFields {
		COD_STAZIONE
	}

	public StazioneFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}
	
	public StazioneFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
	}

	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression expr = this.newExpression();
			if(StringUtils.isNotEmpty(this.getCodIntermediario())){
				//long id = Long.parseLong(this.getCodIntermediario());
				expr.equals(Stazione.model().ID_INTERMEDIARIO.COD_INTERMEDIARIO, this.getCodIntermediario());
			} 
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
		case COD_STAZIONE: filterSortWrapper.setField(Stazione.model().COD_STAZIONE);
		break;
		default:
			break;
		}

		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
	}

	public String getCodIntermediario() {
		return codIntermediario;
	}

	public void setCodIntermediario(String codIntermediario) {
		this.codIntermediario = codIntermediario;
	}

 
}
