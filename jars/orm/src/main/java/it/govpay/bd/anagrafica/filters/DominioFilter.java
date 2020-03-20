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

import java.util.Collection;

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
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.Dominio;
import it.govpay.orm.dao.jdbc.converter.DominioFieldConverter;

public class DominioFilter extends AbstractFilter {

	private String codStazione = null;
	private Collection<Long> idDomini = null; 
	private CustomField cf;
	
	private String codDominio = null;
	private String ragioneSociale = null;
	private Boolean abilitato = null;
	private boolean searchModeEquals = false; 
	
	public enum SortFields {
	}
	
	public DominioFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor, false);
	}
	
	public DominioFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		try{
			DominioFieldConverter converter = new DominioFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			this.cf = new CustomField("id", Long.class, "id", converter.toTable(it.govpay.orm.Dominio.model()));
			this.listaFieldSimpleSearch.add(it.govpay.orm.Dominio.model().ID_STAZIONE.COD_STAZIONE);
			this.listaFieldSimpleSearch.add(it.govpay.orm.Dominio.model().COD_DOMINIO);
			this.listaFieldSimpleSearch.add(it.govpay.orm.Dominio.model().RAGIONE_SOCIALE);
			this.fieldAbilitato = it.govpay.orm.Dominio.model().ABILITATO;
		} catch(Exception e){
			
		}
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.getCodStazione() != null){
				newExpression.equals(it.govpay.orm.Dominio.model().ID_STAZIONE.COD_STAZIONE, this.getCodStazione());
				addAnd = true;
			}
			
			if(this.idDomini != null && this.idDomini.size() > 0 ){
				if(addAnd)
					newExpression.and();
				
				newExpression.in(this.cf, this.idDomini);
				addAnd = true;
			}
			
			if(this.codDominio != null){
				IExpression exp = this.newExpression();
				//1 provo a convertirlo in un long
				long l = -1l;
				try{
					l = Long.parseLong(this.codDominio);
				}catch (NumberFormatException e){
					l = -1l;
				}

				// se e' un numero valido maggiore di zero
				if(l>0){
					exp.equals(this.cf, l);
					exp.or();
				}
				
				// 2. metto in or l'eventuale stringa per il nome dell'dominio
				if(!this.searchModeEquals)
					exp.ilike(Dominio.model().COD_DOMINIO, this.codDominio,LikeMode.ANYWHERE);
				else 
					exp.equals(Dominio.model().COD_DOMINIO, this.codDominio);
				
				newExpression.and(exp);
				addAnd = true;
			}
			
			if(this.ragioneSociale != null){
				if(addAnd)
					newExpression.and();
				
				// 2. metto in and la stringa con la ragione sociale
				newExpression.ilike(Dominio.model().RAGIONE_SOCIALE, this.ragioneSociale,LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.abilitato != null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(Dominio.model().ABILITATO, this.abilitato);
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
		return null;
	}

	@Override
	public Object[] getParameters(ISQLQueryObject sqlQueryObject) throws ServiceException {
		return null;
	}

	public String getCodStazione() {
		return this.codStazione;
	}

	public void setCodStazione(String codStazione) {
		this.codStazione = codStazione;
	}

	public Collection<Long> getIdDomini() {
		return this.idDomini;
	}

	public void setIdDomini(Collection<Long> idDomini) {
		this.idDomini = idDomini;
	}

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

	public Boolean getAbilitato() {
		return this.abilitato;
	}

	public void setAbilitato(Boolean abilitato) {
		this.abilitato = abilitato;
	}
	
	public boolean isSearchModeEquals() {
		return this.searchModeEquals;
	}

	public void setSearchModeEquals(boolean searchModeEquals) {
		this.searchModeEquals = searchModeEquals;
	}
}
