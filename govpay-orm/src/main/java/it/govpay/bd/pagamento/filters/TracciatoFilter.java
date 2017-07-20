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
package it.govpay.bd.pagamento.filters;

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

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.model.Tracciato.StatoTracciatoType;
import it.govpay.orm.Tracciato;
import it.govpay.orm.dao.jdbc.converter.TracciatoFieldConverter;

public class TracciatoFilter extends AbstractFilter {

	private List<Long> idTracciati = null; 
	private CustomField cf;
	
	private List<StatoTracciatoType> statoTracciato;
	private Long idOperatore = null;
	private Long idApplicazione = null;
	private Date dataUltimoAggiornamentoMax;
	private Date dataUltimoAggiornamentoMin;
	private Date dataCaricamentoMax;
	private Date dataCaricamentoMin;
	
	public enum SortFields {
		DATA_CARICAMENTO
	}
	
	public TracciatoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor, false);
	}
	
	public TracciatoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		
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
				List<it.govpay.orm.constants.StatoTracciatoType> statoTracciatoOrm  = toStatoTracciatoOrm(this.statoTracciato);
				newExpression.in(it.govpay.orm.Tracciato.model().STATO, statoTracciatoOrm);
				addAnd = true;
			}
			
			if(this.idTracciati != null && this.idTracciati.size() > 0 ){
				if(addAnd)
					newExpression.and();
				
				newExpression.in(cf, idTracciati);
				addAnd = true;
			}

			TracciatoFieldConverter converter = new TracciatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 

			if(this.idOperatore != null){
				if(addAnd)
					newExpression.and();
				CustomField idOperatoreCustomField = new CustomField("id_operatore",  Long.class, "id_operatore",converter.toTable(it.govpay.orm.Tracciato.model()));
				newExpression.equals(idOperatoreCustomField, this.idOperatore);
			}
			
			if(this.idApplicazione != null){
				if(addAnd)
					newExpression.and();
				CustomField idOperatoreCustomField = new CustomField("id_applicazione",  Long.class, "id_applicazione",converter.toTable(it.govpay.orm.Tracciato.model()));
				newExpression.equals(idOperatoreCustomField, this.idApplicazione);
			}
			
			if(this.dataUltimoAggiornamentoMax != null) {
				newExpression.lessEquals(it.govpay.orm.Tracciato.model().DATA_ULTIMO_AGGIORNAMENTO, this.dataUltimoAggiornamentoMax);
			}
			
			if(this.dataUltimoAggiornamentoMin != null) {
				newExpression.greaterEquals(it.govpay.orm.Tracciato.model().DATA_ULTIMO_AGGIORNAMENTO, this.dataUltimoAggiornamentoMin);
			}
			
			if(this.dataCaricamentoMax != null) {
				newExpression.lessEquals(it.govpay.orm.Tracciato.model().DATA_CARICAMENTO, this.dataCaricamentoMax);
			}
			
			if(this.dataCaricamentoMin != null) {
				newExpression.greaterEquals(it.govpay.orm.Tracciato.model().DATA_CARICAMENTO, this.dataCaricamentoMin);
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
			IExpression newExpressionOr = super._toSimpleSearchExpression();
			
			if(this.idOperatore != null){
				IExpression newExpressionOperatore = this.newExpression();
				TracciatoFieldConverter converter = new TracciatoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField idOperatoreCustomField = new CustomField("id_operatore",  Long.class, "id_operatore",converter.toTable(it.govpay.orm.Tracciato.model()));
				newExpressionOperatore.equals(idOperatoreCustomField, this.idOperatore);
				
				newExpressionOr.and(newExpressionOperatore);
			}
			return newExpressionOr;
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * @param statoTracciato2
	 * @return
	 */
	private List<it.govpay.orm.constants.StatoTracciatoType> toStatoTracciatoOrm(
			List<StatoTracciatoType> statoTracciato) {
		if(statoTracciato == null) return null;
		List<it.govpay.orm.constants.StatoTracciatoType> lst = new ArrayList<it.govpay.orm.constants.StatoTracciatoType>();
		for(StatoTracciatoType stato: statoTracciato) {
			lst.add(it.govpay.orm.constants.StatoTracciatoType.valueOf(stato.name()));
		}
		return lst;
	}

	public void addSortField(SortFields field, boolean asc) {
		FilterSortWrapper filterSortWrapper = new FilterSortWrapper();
		
		switch (field) {
		case DATA_CARICAMENTO:
			filterSortWrapper.setField(Tracciato.model().DATA_CARICAMENTO);
			break;
		}
		
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

	public Long getIdApplicazione() {
		return idApplicazione;
	}

	public void setIdApplicazione(Long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}

	public Date getDataUltimoAggiornamentoMin() {
		return dataUltimoAggiornamentoMin;
	}

	public void setDataUltimoAggiornamentoMin(Date dataUltimoAggiornamentoMin) {
		this.dataUltimoAggiornamentoMin = dataUltimoAggiornamentoMin;
	}

	public Date getDataCaricamentoMin() {
		return dataCaricamentoMin;
	}

	public void setDataCaricamentoMin(Date dataCaricamentoMin) {
		this.dataCaricamentoMin = dataCaricamentoMin;
	}

	public Date getDataCaricamentoMax() {
		return dataCaricamentoMax;
	}

	public void setDataCaricamentoMax(Date dataCaricamentoMax) {
		this.dataCaricamentoMax = dataCaricamentoMax;
	}

	
	
}
