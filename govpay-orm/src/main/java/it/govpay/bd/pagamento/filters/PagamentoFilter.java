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
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.Pagamento;
import it.govpay.orm.dao.jdbc.converter.PagamentoFieldConverter;

public class PagamentoFilter extends AbstractFilter {
	
	private Long idIncasso;
	private Long idRr;
	private Long idRpt;
	private String codDominio;
	private Date dataInizio;
	private Date dataFine;
	private List<Long> idVersamenti;
	
	public enum SortFields {
		DATA
	}

	public PagamentoFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}
	
	public PagamentoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;

			PagamentoFieldConverter pagamentoFieldConverter = new PagamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			
			if(this.getIdRr() != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(new CustomField("id_rr", Long.class, "id_rr", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), this.getIdRr());
				addAnd = true;
			}
			
			if(this.getIdRpt() != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(new CustomField("id_rpt", Long.class, "id_rpt", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), this.getIdRpt());
				addAnd = true;
			}
			
			if(this.getIdIncasso() != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(new CustomField("id_incasso", Long.class, "id_incasso", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), this.getIdIncasso());
				addAnd = true;
			}
			
			if(this.dataInizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();

				newExpression.between(Pagamento.model().DATA_ACQUISIZIONE, this.dataInizio,this.dataFine);
				addAnd = true;
			}
			
			if(this.codDominio != null) {
				if(addAnd)
					newExpression.and();

				newExpression.equals(Pagamento.model().ID_RPT.COD_DOMINIO,this.codDominio);
				addAnd = true;
			}
			
			if(this.idVersamenti != null && this.idVersamenti.size() >0){ 
				if(addAnd)
					newExpression.and();
				CustomField idVersamentoField = new CustomField(ALIAS_ID, Long.class, ALIAS_ID,
						pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
				newExpression.in(idVersamentoField, this.idVersamenti);
				// forzo la join con singoliversamenti
				newExpression.isNotNull(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE); 
				newExpression.isNotNull(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO);
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
	
	@Override
	public IExpression _toSimpleSearchExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			
			List<IExpression> orExpr = new ArrayList<IExpression>();
			if(this.simpleSearchString != null){
				IExpression codDominioExpr = this.newExpression();
				codDominioExpr.ilike(Pagamento.model().ID_RPT.COD_DOMINIO, this.simpleSearchString, LikeMode.ANYWHERE);
				orExpr.add(codDominioExpr);
			}
			newExpression.or(orExpr.toArray(new IExpression[orExpr.size()])); 
			
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
			filterSortWrapper.setField(Pagamento.model().DATA_ACQUISIZIONE); 
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
	}

	public Long getIdRr() {
		return idRr;
	}

	public void setIdRr(Long idRr) {
		this.idRr = idRr;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
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

	public List<Long> getIdVersamenti() {
		return idVersamenti;
	}

	public void setIdVersamenti(List<Long> idVersamenti) {
		this.idVersamenti = idVersamenti;
	}

	public Long getIdRpt() {
		return idRpt;
	}

	public void setIdRpt(Long idRpt) {
		this.idRpt = idRpt;
	}

	public Long getIdIncasso() {
		return idIncasso;
	}

	public void setIdIncasso(Long idIncasso) {
		this.idIncasso = idIncasso;
	}
	
}
