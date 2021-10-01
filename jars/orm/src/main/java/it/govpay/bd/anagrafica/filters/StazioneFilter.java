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
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.Stazione;
import it.govpay.orm.dao.jdbc.converter.StazioneFieldConverter;
import it.govpay.orm.model.StazioneModel;

public class StazioneFilter extends AbstractFilter {

	// Filtro che indica che voglio solo le stazioni dell'intermediario passato.
	private String codIntermediario;
	private String codStazione;
	
	private static StazioneModel model = Stazione.model();
	private StazioneFieldConverter converter = null;

	public enum SortFields {
		COD_STAZIONE
	}

	public StazioneFilter(IExpressionConstructor expressionConstructor) throws ServiceException {
		this(expressionConstructor,false);
	}
	
	public StazioneFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) throws ServiceException {
		super(expressionConstructor, simpleSearch);
		this.fieldAbilitato = model.ABILITATO;
		
		this.converter = new StazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
		this.eseguiCountConLimit = false;
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression expr = this.newExpression();
			boolean addAnd = false; 
			if(StringUtils.isNotEmpty(this.getCodIntermediario())){
				//long id = Long.parseLong(this.getCodIntermediario());
				expr.equals(model.ID_INTERMEDIARIO.COD_INTERMEDIARIO, this.getCodIntermediario());
				addAnd = true;
			} 
			
			if(StringUtils.isNotEmpty(this.getCodStazione())){
				//long id = Long.parseLong(this.getCodIntermediario());
				expr.equals(model.COD_STAZIONE, this.getCodStazione());
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
		case COD_STAZIONE: filterSortWrapper.setField(model.COD_STAZIONE);
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
			boolean addTabellaIntermediari = false;
			
			if(StringUtils.isNotEmpty(this.getCodIntermediario())){
				if(!addTabellaIntermediari) {
					sqlQueryObject.addFromTable(converter.toTable(model.ID_INTERMEDIARIO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.COD_STAZIONE, true) + ".id_intermediario="
							+converter.toTable(model.ID_INTERMEDIARIO, true)+".id");

					addTabellaIntermediari = true;
				}

				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_INTERMEDIARIO.COD_INTERMEDIARIO, true) + " = ? ");
			} 
			
			if(StringUtils.isNotEmpty(this.getCodStazione())){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_STAZIONE, true) + " = ? ");
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
		
		if(StringUtils.isNotEmpty(this.getCodIntermediario())){
			lst.add(this.getCodIntermediario());
		} 
		
		if(StringUtils.isNotEmpty(this.getCodStazione())){
			lst.add(this.getCodStazione());
		} 
		
		// filtro abilitato
		try {
			lst = this.setValoreFiltroAbilitato(lst, converter);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public String getCodIntermediario() {
		return this.codIntermediario;
	}

	public void setCodIntermediario(String codIntermediario) {
		this.codIntermediario = codIntermediario;
	}

	public String getCodStazione() {
		return this.codStazione;
	}

	public void setCodStazione(String codStazione) {
		this.codStazione = codStazione;
	}

 
}
