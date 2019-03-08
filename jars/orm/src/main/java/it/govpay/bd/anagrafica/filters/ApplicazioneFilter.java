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

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.Applicazione;
import it.govpay.orm.dao.jdbc.converter.ApplicazioneFieldConverter;

public class ApplicazioneFilter extends AbstractFilter {

	private List<Long> listaIdApplicazioni = null;
	private CustomField cf;
	private String codApplicazione = null;
	private String principalOriginale = null;
	
	
	public enum SortFields {
	}

	public ApplicazioneFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor, false);
	}
	
	public ApplicazioneFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		
		try{
			ApplicazioneFieldConverter converter = new ApplicazioneFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			this.cf = new CustomField("id", Long.class, "id", converter.toTable(it.govpay.orm.Applicazione.model()));
			this.listaFieldSimpleSearch.add(Applicazione.model().COD_APPLICAZIONE);
			this.fieldAbilitato = Applicazione.model().ID_UTENZA.ABILITATO;
		} catch(Exception e){
			
		}
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression(); 
			boolean addAnd = false;
			if(this.listaIdApplicazioni != null && this.listaIdApplicazioni.size() > 0){
				newExpression.in(this.cf, this.listaIdApplicazioni);
				addAnd = true;
			}
			
			if(this.codApplicazione != null){
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(Applicazione.model().COD_APPLICAZIONE, this.codApplicazione,LikeMode.ANYWHERE);
			}
			
			if(this.principalOriginale != null){
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(Applicazione.model().ID_UTENZA.PRINCIPAL_ORIGINALE, this.principalOriginale,LikeMode.ANYWHERE);
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

	public List<Long> getListaIdApplicazioni() {
		return this.listaIdApplicazioni;
	}

	public void setListaIdApplicazioni(List<Long> listaIdApplicazioni) {
		this.listaIdApplicazioni = listaIdApplicazioni;
	}

	public String getCodApplicazione() {
		return this.codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public String getPrincipalOriginale() {
		return principalOriginale;
	}

	public void setPrincipalOriginale(String principalOriginale) {
		this.principalOriginale = principalOriginale;
	}

	
}
