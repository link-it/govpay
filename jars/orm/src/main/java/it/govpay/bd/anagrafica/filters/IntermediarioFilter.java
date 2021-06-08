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
import it.govpay.orm.Intermediario;
import it.govpay.orm.dao.jdbc.converter.IntermediarioFieldConverter;
import it.govpay.orm.model.IntermediarioModel;

public class IntermediarioFilter extends AbstractFilter {

	// Viene utilizzato in or sui campi IDIntermediario e NomeSpc
	private String idIntermediario;
	private String codIntermediario;
	private String denominazione;
	private CustomField cf;
	
	private static IntermediarioModel model = Intermediario.model();
	private IntermediarioFieldConverter converter = null;

	public enum SortFields {
		ID_INTERMEDIARIO, COD_INTERMEDIARIO
	}

	public IntermediarioFilter(IExpressionConstructor expressionConstructor) throws ServiceException {
		this(expressionConstructor,false);
	}
	
	public IntermediarioFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) throws ServiceException {
		super(expressionConstructor, simpleSearch);
		
		this.converter = new IntermediarioFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
		this.eseguiCountConLimit = false;
		try{
			this.cf = new CustomField("id", Long.class, "id", converter.toTable(model));
			this.listaFieldSimpleSearch.add(model.COD_INTERMEDIARIO);
			this.listaFieldSimpleSearch.add(model.DENOMINAZIONE);
			this.fieldAbilitato = model.ABILITATO;
		} catch(Exception e){
			
		}
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression exp = this.newExpression();
			boolean addAnd = false;
			
			if(this.idIntermediario != null){
//				//1 provo a convertirlo in un long
//				long l = -1l;
//				try{
//					l =Long.parseLong(this.idIntermediario);
//				}catch (NumberFormatException e){
//					l = -1l;
//				}
//
//				// se e' un numero valido maggiore di zero
//				if(l>0){
//					exp.equals(this.cf, l);
//					exp.or();
//				}
				
				// 2. metto in or l'eventuale stringa per il nome dell'intermediario
				exp.ilike(model.COD_INTERMEDIARIO, this.idIntermediario,LikeMode.ANYWHERE); 
				
				addAnd = true;
			}
			
			if(this.denominazione != null){
				if(addAnd)
					exp.and();
				
				exp.ilike(model.DENOMINAZIONE, this.denominazione,LikeMode.ANYWHERE);
			}
			
			if(this.codIntermediario != null){
				if(addAnd)
					exp.and();
				
				exp.equals(model.COD_INTERMEDIARIO, this.codIntermediario);
			}
			
			
			addAnd = this.setFiltroAbilitato(exp, addAnd);

			return exp;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
	
	public void addSortField(SortFields field, boolean asc){
		FilterSortWrapper filterSortWrapper = new FilterSortWrapper();

		switch(field) {
		case COD_INTERMEDIARIO: filterSortWrapper.setField(model.COD_INTERMEDIARIO);
		break;
		case ID_INTERMEDIARIO: 
			filterSortWrapper.setField(this.cf);
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
			if(this.idIntermediario != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.COD_INTERMEDIARIO, true), this.idIntermediario, true, true);
			}
			
			if(this.denominazione != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.DENOMINAZIONE, true), this.denominazione, true, true);
			}
			
			if(this.codIntermediario != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_INTERMEDIARIO, true) + " = ? ");
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
		
		if(this.idIntermediario != null){
			// do nothing
		}
		
		if(this.denominazione != null){
			// do nothing
		}
		
		if(this.codIntermediario != null){
			lst.add(this.codIntermediario);
		}
		
		// filtro abilitato
		try {
			lst = this.setValoreFiltroAbilitato(lst, converter);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public String getIdIntermediario() {
		return this.idIntermediario;
	}

	public void setIdIntermediario(String idIntermediario) {
		this.idIntermediario = idIntermediario;
	}

	public String getDenominazione() {
		return this.denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getCodIntermediario() {
		return this.codIntermediario;
	}

	public void setCodIntermediario(String codIntermediario) {
		this.codIntermediario = codIntermediario;
	}


}
