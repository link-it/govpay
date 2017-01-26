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
package it.govpay.bd.wrapper.filters;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.RendicontazionePagamento;

import java.util.Date;

import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

public class RendicontazionePagamentoFilter extends AbstractFilter {
	
	private String codDominio;
	private String codApplicazione;
	private Date dataPagamentoMin;
	private Date dataPagamentoMax;
	private String codFlusso;

	public enum SortFields {
	}
	
	public RendicontazionePagamentoFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			if(this.codDominio != null){
				newExpression.equals(RendicontazionePagamento.model().FR.COD_DOMINIO, this.codDominio);
			}
			//TODO Nardi
//			if(this.codApplicazione != null){
//				newExpression.equals(RendicontazionePagamento.model().FR_APPLICAZIONE.ID_APPLICAZIONE.COD_APPLICAZIONE, this.codApplicazione);
//			}
			if(this.dataPagamentoMin != null){
				newExpression.greaterEquals(RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO, this.dataPagamentoMin);
			}
			if(this.dataPagamentoMax != null){
				newExpression.lessEquals(RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO, this.dataPagamentoMax);
			}
			if(this.codFlusso != null) {
				newExpression.equals(RendicontazionePagamento.model().FR.COD_FLUSSO, this.codFlusso);
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

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getCodApplicazione() {
		return codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public Date getDataPagamentoMin() {
		return dataPagamentoMin;
	}

	public void setDataPagamentoMin(Date dataPagamentoMin) {
		this.dataPagamentoMin = dataPagamentoMin;
	}

	public Date getDataPagamentoMax() {
		return dataPagamentoMax;
	}

	public void setDataPagamentoMax(Date dataPagamentoMax) {
		this.dataPagamentoMax = dataPagamentoMax;
	}

	public String getCodFlusso() {
		return codFlusso;
	}

	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
	}

	
}
