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
import it.govpay.bd.reportistica.PagamentiBD;
import it.govpay.orm.Pagamento;
import it.govpay.orm.dao.jdbc.converter.PagamentoFieldConverter;

public class PagamentoFilter extends AbstractFilter {

	private Long idRr;
	private Long idFrApplicazione;
	private Long idFrApplicazioneRevoca;
	private List<Long> idFrApplicazioneOrIdFrApplicazioneRevoca;
	
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

	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;

			PagamentoFieldConverter pagamentoFieldConverter = new PagamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			
			if(this.getIdRr() != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(new CustomField("id_rr", Long.class, "id_rr", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), getIdRr());
				addAnd = true;
			}
			
			if(this.getIdFrApplicazione() != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(new CustomField("id_fr_applicazione", Long.class, "id_fr_applicazione", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), idFrApplicazione);
				addAnd = true;
			}
			

			if(this.getIdFrApplicazioneRevoca() != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(new CustomField("id_fr_applicazione_revoca", Long.class, "id_fr_applicazione_revoca", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), idFrApplicazioneRevoca);
				addAnd = true;
			}
			
			if(this.idFrApplicazioneOrIdFrApplicazioneRevoca != null){
				if(addAnd)
					newExpression.and();
				
				IExpression orExpr = this.newExpression();
				IExpression revocaExpr = this.newExpression();
				
				orExpr.in(new CustomField("id_fr_applicazione", Long.class, "id_fr_applicazione", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), idFrApplicazioneOrIdFrApplicazioneRevoca);
				orExpr.or();
				
				// id fr applicazione revoca non null
				revocaExpr.in(new CustomField("id_fr_applicazione_revoca", Long.class, "id_fr_applicazione_revoca", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())), idFrApplicazioneOrIdFrApplicazioneRevoca)
				.and().isNotNull(new CustomField("id_fr_applicazione_revoca", Long.class, "id_fr_applicazione_revoca", pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model())));
				
				orExpr.or(revocaExpr);
				
				newExpression.and(orExpr);
				
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
				CustomField idVersamentoField = new CustomField(PagamentiBD.ALIAS_ID, Long.class, PagamentiBD.ALIAS_ID,
						pagamentoFieldConverter.toTable(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
				newExpression.in(idVersamentoField, this.idVersamenti);
				// forzo la join con singoliversamenti
				newExpression.isNotNull(it.govpay.orm.Pagamento.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE); 
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

	public Long getIdFrApplicazione() {
		return idFrApplicazione;
	}

	public void setIdFrApplicazione(Long idFrApplicazione) {
		this.idFrApplicazione = idFrApplicazione;
	}

	public Long getIdFrApplicazioneRevoca() {
		return idFrApplicazioneRevoca;
	}

	public void setIdFrApplicazioneRevoca(Long idFrApplicazioneRevoca) {
		this.idFrApplicazioneRevoca = idFrApplicazioneRevoca;
	}

	public List<Long> getIdFrApplicazioneOrIdFrApplicazioneRevoca() {
		return idFrApplicazioneOrIdFrApplicazioneRevoca;
	}

	public void setIdFrApplicazioneOrIdFrApplicazioneRevoca(List<Long> idFrApplicazioneOrIdFrApplicazioneRevoca) {
		this.idFrApplicazioneOrIdFrApplicazioneRevoca = idFrApplicazioneOrIdFrApplicazioneRevoca;
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

 
	
}
