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

import java.util.Calendar;
import java.util.Collections;
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
import it.govpay.model.Pagamento.Stato;
import it.govpay.orm.Pagamento;
import it.govpay.orm.dao.jdbc.converter.PagamentoFieldConverter;

public class PagamentoFilter extends AbstractFilter {
	
	private Long idIncasso;
	private Long idRr;
	private Long idRpt;
	private List<String> idDomini;
	private Date dataInizio;
	private Date dataFine;
	private List<Long> idVersamenti;
	private List<Long> idPagamenti;
	private String stato;
	private Integer sogliaRitardo = null;
	public static final String STATO_RITARDO_INCASSO = "RITARDO_INCASSO";
	private String codSingoloVersamentoEnte = null;
	private String iur;
	
	public enum SortFields {
		DATA
	}

	public PagamentoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public PagamentoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(Pagamento.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE);
		this.listaFieldSimpleSearch.add(Pagamento.model().IUR);
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
			
			if(this.idDomini != null){
				idDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				newExpression.in(Pagamento.model().COD_DOMINIO, this.idDomini);
				addAnd = true;
			}
			
			if(stato != null){
				if(addAnd)
					newExpression.and();
				
				if(stato.equals(STATO_RITARDO_INCASSO)) {
					if(this.sogliaRitardo != null && this.sogliaRitardo.intValue() > 0){
						newExpression.notEquals(Pagamento.model().STATO,Stato.INCASSATO);
						Calendar tempo = Calendar.getInstance();
						tempo.setTime(new Date());
						tempo.add(Calendar.DAY_OF_YEAR, -this.sogliaRitardo);
						newExpression.lessThan(Pagamento.model().DATA_PAGAMENTO, tempo.getTime());
					}
				} else {
					newExpression.equals(Pagamento.model().STATO,Stato.valueOf(this.stato));
				}
				
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
			
			if(this.idPagamenti != null && this.idPagamenti.size() >0){ 
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField(ALIAS_ID, Long.class, ALIAS_ID, pagamentoFieldConverter.toTable(Pagamento.model()));
				newExpression.in(cf, this.idPagamenti);
				addAnd = true;
			}
			
			if(this.codSingoloVersamentoEnte != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(Pagamento.model().ID_SINGOLO_VERSAMENTO.COD_SINGOLO_VERSAMENTO_ENTE, this.codSingoloVersamentoEnte, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.iur != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(Pagamento.model().IUR, this.iur, LikeMode.ANYWHERE);
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
			PagamentoFieldConverter pagamentoFieldConverter = new PagamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			
			IExpression newExpression = super._toSimpleSearchExpression();
			
			boolean addAnd = false;

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
			
			if(this.idDomini != null){
				idDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				newExpression.in(Pagamento.model().COD_DOMINIO, this.idDomini);
				addAnd = true;
			}

			return newExpression;
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

	public List<String> getIdDomini() {
		return idDomini;
	}

	public void setIdDomini(List<String> idDomini) {
		this.idDomini = idDomini;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Integer getSogliaRitardo() {
		return sogliaRitardo;
	}

	public void setSogliaRitardo(Integer sogliaRitardo) {
		this.sogliaRitardo = sogliaRitardo;
	}

	public String getCodSingoloVersamentoEnte() {
		return codSingoloVersamentoEnte;
	}

	public void setCodSingoloVersamentoEnte(String codSingoloVersamentoEnte) {
		this.codSingoloVersamentoEnte = codSingoloVersamentoEnte;
	}

	public String getIur() {
		return iur;
	}

	public void setIur(String iur) {
		this.iur = iur;
	}

	public List<Long> getIdPagamenti() {
		return idPagamenti;
	}

	public void setIdPagamenti(List<Long> idPagamenti) {
		this.idPagamenti = idPagamenti;
	}
	
}
