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
package it.govpay.bd.reportistica.filters;

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
import it.govpay.orm.Pagamento;
import it.govpay.orm.dao.jdbc.converter.PagamentoFieldConverter;

public class PagamentoFilter extends AbstractFilter {
	
	private List<Long> idDomini;
	private Long idPagamento;

	private Date dataInizio;
	private Date dataFine;
	
	public enum SortFields {
		DATA
	}

	public PagamentoFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;

//			PagamentoFieldConverter pagamentoFieldConverter = new PagamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			
			if(this.idDomini != null){
				if(addAnd)
					newExpression.and();
				
//				IExpression orExpr = this.newExpression();
//				IExpression revocaExpr = this.newExpression();
//				
//				orExpr.in(new CustomField("id_fr_applicazione", Long.class, "id_fr_applicazione", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), idFrApplicazioneOrIdFrApplicazioneRevoca);
//				orExpr.or();
//				
//				// id fr applicazione revoca non null
//				revocaExpr.in(new CustomField("id_fr_applicazione_revoca", Long.class, "id_fr_applicazione_revoca", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), idFrApplicazioneOrIdFrApplicazioneRevoca)
//				.and().isNotNull(new CustomField("id_fr_applicazione_revoca", Long.class, "id_fr_applicazione_revoca", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())));
//				
//				orExpr.or(revocaExpr);
//				
//				newExpression.and(orExpr);
				
				addAnd = true;
				
			}
			
			if(this.dataInizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(Pagamento.model().DATA_ACQUISIZIONE, this.dataInizio,this.dataFine);
				addAnd = true;
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
		if(field.equals(SortFields.DATA)) 
			filterSortWrapper.setField(Pagamento.model().DATA_PAGAMENTO); 
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public List<Long> getIdDomini() {
		return idDomini;
	}

	public void setIdDomini(List<Long> idDomini) {
		this.idDomini = idDomini;
	}

	public Long getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}
	
}
