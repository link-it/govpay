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
package it.govpay.bd.operazioni.filters;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.constants.StatoTracciatoType;
import it.govpay.orm.dao.jdbc.converter.TracciatoFieldConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

public class TracciatoFilter extends AbstractFilter {

	private List<Long> idTracciati = null; 
	private CustomField cf;
	
	private List<StatoTracciatoType> statoTracciato;
	private Long idOperatore = null;
	private Date dataUltimoAggiornamentoMax;
	
	public enum SortFields {
	}
	
	public TracciatoFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
		
		try{
			TracciatoFieldConverter converter = new TracciatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			this.cf = new CustomField("id", Long.class, "id", converter.toTable(it.govpay.orm.Tracciato.model()));
			this.statoTracciato = new ArrayList<StatoTracciatoType>();
		} catch(Exception e){
			
		}
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.statoTracciato != null && !this.statoTracciato.isEmpty()) {
				newExpression.in(it.govpay.orm.Tracciato.model().STATO, this.statoTracciato);
				addAnd = true;
			}
			
			if(this.idTracciati != null && this.idTracciati.size() > 0 ){
				if(addAnd)
					newExpression.and();
				
				newExpression.in(cf, idTracciati);
				addAnd = true;
			}
			
			if(this.idOperatore != null){
				if(addAnd)
					newExpression.and();
				TracciatoFieldConverter converter = new TracciatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField idOperatoreCustomField = new CustomField("id_operatore",  Long.class, "id_operatore",converter.toTable(it.govpay.orm.Tracciato.model()));
				
				newExpression.equals(idOperatoreCustomField, this.idOperatore);
			}
			
			if(this.dataUltimoAggiornamentoMax != null) {
				newExpression.lessEquals(it.govpay.orm.Tracciato.model().DATA_ULTIMO_AGGIORNAMENTO, this.dataUltimoAggiornamentoMax);
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

	public List<Long> getIdTracciati() {
		return idTracciati;
	}

	public void setIdTracciati(List<Long> idTracciati) {
		this.idTracciati = idTracciati;
	}

	public List<StatoTracciatoType> getStatoTracciato() {
		return statoTracciato;
	}

	public void addStatoTracciato(StatoTracciatoType statoTracciato) {
		this.statoTracciato.add(statoTracciato);
	}

	public Long getIdOperatore() {
		return idOperatore;
	}

	public void setIdOperatore(Long idOperatore) {
		this.idOperatore = idOperatore;
	}

	public Date getDataUltimoAggiornamentoMax() {
		return dataUltimoAggiornamentoMax;
	}

	public void setDataUltimoAggiornamentoMax(Date dataUltimoAggiornamentoMax) {
		this.dataUltimoAggiornamentoMax = dataUltimoAggiornamentoMax;
	}

	
	
}
