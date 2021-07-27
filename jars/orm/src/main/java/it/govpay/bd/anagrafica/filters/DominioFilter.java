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
import it.govpay.orm.Dominio;
import it.govpay.orm.dao.jdbc.converter.DominioFieldConverter;
import it.govpay.orm.model.DominioModel;

public class DominioFilter extends AbstractFilter {

	private String codStazione = null;
	private List<Long> idDomini = null; 
	private String codDominio = null;
	private String ragioneSociale = null;
	private boolean searchModeEquals = false; 
	
	private static DominioModel model = Dominio.model();
	private DominioFieldConverter converter = null;
	
	private Boolean intermediato = null; 
	
	public enum SortFields {
	}
	
	public DominioFilter(IExpressionConstructor expressionConstructor) throws ServiceException {
		this(expressionConstructor, false);
	}
	
	public DominioFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) throws ServiceException {
		super(expressionConstructor, simpleSearch);
		this.idDomini = new ArrayList<>();
		
		this.converter = new DominioFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
		this.eseguiCountConLimit = false;
		
		this.listaFieldSimpleSearch.add(model.ID_STAZIONE.COD_STAZIONE);
		this.listaFieldSimpleSearch.add(model.COD_DOMINIO);
		this.listaFieldSimpleSearch.add(model.RAGIONE_SOCIALE);
		this.fieldAbilitato = model.ABILITATO;
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.getCodStazione() != null){
				newExpression.equals(model.ID_STAZIONE.COD_STAZIONE, this.getCodStazione());
				addAnd = true;
			}
			
			if(this.idDomini != null && this.idDomini.size() > 0 ){
				if(addAnd)
					newExpression.and();
				
				this.idDomini.removeAll(Collections.singleton(null));
				
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(model));
				newExpression.in(cf, this.idDomini);
				addAnd = true;
			}
			
			if(this.codDominio != null){
				if(!this.searchModeEquals)
					newExpression.ilike(model.COD_DOMINIO, this.codDominio,LikeMode.ANYWHERE);
				else 
					newExpression.equals(model.COD_DOMINIO, this.codDominio);
				
				addAnd = true;
			}
			
			if(this.ragioneSociale != null){
				if(addAnd)
					newExpression.and();
				
				// 2. metto in and la stringa con la ragione sociale
				newExpression.ilike(model.RAGIONE_SOCIALE, this.ragioneSociale,LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.intermediato != null){
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(model.INTERMEDIATO, this.intermediato);
				addAnd = true;
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
			if(this.codStazione != null){
				sqlQueryObject.addFromTable(converter.toTable(model.ID_STAZIONE));
				sqlQueryObject.addWhereCondition(converter.toTable(model.COD_DOMINIO, true) + ".id_stazione="
						+converter.toTable(model.ID_STAZIONE, true)+".id");
				
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_STAZIONE.COD_STAZIONE, true) + " = ? ");
			}
			
			if(this.idDomini != null && this.idDomini.size() > 0 ){
				this.idDomini.removeAll(Collections.singleton(null));
				
				String [] ids = this.idDomini.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idDomini.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.COD_DOMINIO, true) + ".id", false, ids );
			}
			
			if(this.codDominio != null){
				if(!this.searchModeEquals)
					sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.COD_DOMINIO, true), this.codDominio, true, true);
				else 
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_DOMINIO, true) + " = ? ");
				
			}
			
			if(this.ragioneSociale != null){
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.RAGIONE_SOCIALE, true), this.ragioneSociale, true, true);
			}
			
			if(this.intermediato != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.INTERMEDIATO, true) + " = ? ");
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
		
		if(this.codStazione != null){
			lst.add(this.codStazione);
		}
		
		if(this.idDomini != null && this.idDomini.size() > 0 ){
			// do nothing
		}
		
		if(this.codDominio != null){
			if(this.searchModeEquals)
				lst.add(this.codDominio);
		}
		
		if(this.ragioneSociale != null){
			// do nothing
		}
		
		if(this.intermediato != null) {
			try {
				lst = this.setValoreFiltroBoolean(lst, this.converter, this.intermediato);
			} catch (ExpressionException e) {
				throw new ServiceException(e);
			}
			
		}
		
		// filtro abilitato
		try {
			lst = this.setValoreFiltroAbilitato(lst, converter);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public String getCodStazione() {
		return this.codStazione;
	}

	public void setCodStazione(String codStazione) {
		this.codStazione = codStazione;
	}

	public List<Long> getIdDomini() {
		return this.idDomini;
	}

//	public void setIdDomini(Collection<Long> idDomini) {
//		this.idDomini = idDomini;
//	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getRagioneSociale() {
		return this.ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public boolean isSearchModeEquals() {
		return this.searchModeEquals;
	}

	public void setSearchModeEquals(boolean searchModeEquals) {
		this.searchModeEquals = searchModeEquals;
	}

	public Boolean getIntermediato() {
		return intermediato;
	}

	public void setIntermediato(Boolean intermediato) {
		this.intermediato = intermediato;
	}
}
