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

import org.openspcoop2.generic_project.beans.AliasField;
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
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.reportistica.PagamentiBD;
import it.govpay.orm.Pagamento;
import it.govpay.orm.dao.jdbc.converter.PagamentoFieldConverter;

public class PagamentoFilter extends AbstractFilter {

	private List<String> codDomini;
	private Long idPagamento;

	private Date dataInizio;
	private Date dataFine;

	public AliasField dataPagamentoAliasField ;
	
	public String statoVersamento ;

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
			
			if(this.idPagamento != null){
				if(addAnd)
					newExpression.and();
				PagamentoFieldConverter pagamentoFieldConverter = new PagamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField idVersamentoField = new CustomField(PagamentiBD.ALIAS_ID, Long.class, PagamentiBD.ALIAS_ID,
						pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
				newExpression.equals(idVersamentoField, this.idPagamento);
				addAnd = true;
			}

			if(this.codDomini != null && !this.codDomini.isEmpty()){
				if(addAnd)
					newExpression.and();
				newExpression.in(Pagamento.model().ID_RPT.COD_DOMINIO, this.codDomini);
				addAnd = true;
			}

			if(this.dataInizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(Pagamento.model().DATA_PAGAMENTO, this.dataInizio,this.dataFine);
				addAnd = true;
			}
			
			if(this.statoVersamento != null){
				if(addAnd)
					newExpression.and();
				newExpression.equals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO, this.statoVersamento);
				addAnd = true;
			}
			
			// stato del versamento 
			if(addAnd)
				newExpression.and();
			newExpression.notEquals(Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO, StatoVersamento.NON_ESEGUITO.name());

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
			filterSortWrapper.setField(this.getDataPagamentoAliasField()); 
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

	public List<String> getCodDomini() {
		return codDomini;
	}

	public void setIdDomini(List<String> idDomini) {
		this.codDomini = idDomini;
	}

	public Long getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}

	public AliasField getDataPagamentoAliasField()  {
		if(this.dataPagamentoAliasField == null){
			try{
				this.dataPagamentoAliasField = new AliasField(Pagamento.model().DATA_PAGAMENTO, PagamentiBD.ALIAS_DATA_PAGAMENTO);
			}catch(Exception e){}
		}


		return dataPagamentoAliasField;
	}

	public void setDataPagamentoAliasField(AliasField dataPagamentoAliasField) {
		this.dataPagamentoAliasField = dataPagamentoAliasField;
	}

	public String getStatoVersamento() {
		return statoVersamento;
	}

	public void setStatoVersamento(String statoVersamento) {
		this.statoVersamento = statoVersamento;
	}


}
