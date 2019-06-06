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

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.StatoConservazione;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.orm.Pagamento;
import it.govpay.orm.RPT;
import it.govpay.orm.dao.jdbc.converter.RPTFieldConverter;

public class RptFilter extends AbstractFilter {

	private List<Long> idVersamento;
	private String iuv;
	private String ccp;
	private String codDominio;
	private List<String> idDomini;
	private Boolean conservato;
	private List<String> stato;
	private List<Long> idRpt= null;
	private Long idPagamentoPortale = null;
	private String codPagamentoPortale = null;
	private Date dataInizio;
	private Date dataFine;
	private String codApplicazione = null;
	private String idPendenza = null;
	private EsitoPagamento esitoPagamento = null;
	
	private String cfCittadinoPagamentoPortale = null;
	private String codApplicazionePagamentoPortale = null;

	public RptFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}

	public RptFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(RPT.model().IUV);
		this.listaFieldSimpleSearch.add(RPT.model().CCP);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;

			if(this.idVersamento != null && !this.idVersamento.isEmpty()) {
				this.idVersamento.removeAll(Collections.singleton(null));				
				addAnd = true;
				RPTFieldConverter rptFieldConverter = new RPTFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField idRptCustomField = new CustomField("id_versamento",  Long.class, "id_versamento",  rptFieldConverter.toTable(RPT.model()));
				newExpression.in(idRptCustomField, this.idVersamento);
			}

			if(this.iuv != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(RPT.model().IUV, this.iuv, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.ccp != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(RPT.model().CCP, this.ccp, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.codDominio != null){
				if(addAnd)
					newExpression.and();
				newExpression.equals(RPT.model().COD_DOMINIO, this.codDominio);
				addAnd = true;
			}

			if(this.idDomini != null  && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				newExpression.in(RPT.model().COD_DOMINIO, this.idDomini);
				addAnd = true;
			}
			
			if(this.idRpt != null && !this.idRpt.isEmpty()){
				if(addAnd)
					newExpression.and();
				RPTFieldConverter converter = new RPTFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(RPT.model()));
				newExpression.in(cf, this.idRpt);
				addAnd = true;
			}
			
			if(this.conservato != null){
				if(addAnd)
					newExpression.and();
				
				IExpression newExpression2 = this.newExpression();
				if(this.conservato) {
					newExpression2.notEquals(RPT.model().STATO_CONSERVAZIONE, StatoConservazione.ERRORE.name()).and().isNotNull(RPT.model().STATO_CONSERVAZIONE);
				} else {
					newExpression2.equals(RPT.model().STATO_CONSERVAZIONE, StatoConservazione.ERRORE.name()).or().isNull(RPT.model().STATO_CONSERVAZIONE);					
				}
				
				newExpression.and(newExpression2);
				addAnd = true;
			}

			if(this.stato != null && !this.stato.isEmpty()){
				if(addAnd)
					newExpression.and();
				
				newExpression.in(RPT.model().STATO, this.stato);
				addAnd = true;
			}
			
			if(this.idPagamentoPortale != null) {
				if(addAnd)
					newExpression.and();
				
				
				RPTFieldConverter rptFieldConverter = new RPTFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField idRptCustomField = new CustomField("id_pagamento_portale",  Long.class, "id_pagamento_portale",  rptFieldConverter.toTable(RPT.model()));
				newExpression.equals(idRptCustomField, this.idPagamentoPortale);
				addAnd = true;
			}
			
			if(this.codPagamentoPortale != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(RPT.model().ID_PAGAMENTO_PORTALE.ID_SESSIONE, this.codPagamentoPortale);
				addAnd = true;
			}
			
			if(this.codApplicazionePagamentoPortale != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(RPT.model().ID_PAGAMENTO_PORTALE.ID_APPLICAZIONE.COD_APPLICAZIONE, this.codApplicazionePagamentoPortale);
				addAnd = true;
			}
			
			if(this.cfCittadinoPagamentoPortale != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(RPT.model().ID_PAGAMENTO_PORTALE.VERSANTE_IDENTIFICATIVO, this.cfCittadinoPagamentoPortale);
				addAnd = true;
			}
			
			if(this.codApplicazione != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(RPT.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE, this.codApplicazione);
				addAnd = true;
			}
			
			if(this.idPendenza != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(RPT.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE, this.idPendenza);
				addAnd = true;
			}
			
			if(this.dataInizio != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.greaterEquals(RPT.model().DATA_MSG_RICHIESTA, this.dataInizio);
				addAnd = true;
			}
			if(this.dataFine != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.lessEquals(RPT.model().DATA_MSG_RICHIESTA, this.dataFine);
				addAnd = true;
			}
			
			if(this.esitoPagamento != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(RPT.model().COD_ESITO_PAGAMENTO, this.esitoPagamento.getCodifica());
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
			IExpression newExpression = super._toSimpleSearchExpression();
			
			if(this.idVersamento != null){
				IExpression newExpressionVersamento = this.newExpression();
				RPTFieldConverter rptFieldConverter = new RPTFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField idRptCustomField = new CustomField("id_versamento",  Long.class, "id_versamento",  rptFieldConverter.toTable(RPT.model()));
				newExpressionVersamento.equals(idRptCustomField, this.idVersamento);
				newExpression.and(newExpressionVersamento);
			}

			if(this.idDomini != null){
				IExpression newExpressionDomini = this.newExpression();
				this.idDomini.removeAll(Collections.singleton(null));
				newExpressionDomini.in(Pagamento.model().COD_DOMINIO, this.idDomini);
				newExpression.and(newExpressionDomini);
			}

			return newExpression;
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public List<Long> getIdVersamenti() {
		return this.idVersamento;
	}

	public void setIdVersamento(Long idVersamento) {
		this.idVersamento = new ArrayList<>();
		this.idVersamento.add(idVersamento);
	}
	
	public void setIdVersamenti(List<Long> ids) {
		this.idVersamento = new ArrayList<>();
		if(ids != null && !ids.isEmpty())
			this.idVersamento.addAll(ids);
	}

	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public List<String> getIdDomini() {
		return this.idDomini;
	}

	public void setIdDomini(List<String> idDomini) {
		this.idDomini = idDomini;
	}

	public void setStato(StatoRpt stato) {
		this.stato = new ArrayList<>();
		if(stato!= null)
			this.stato.add(stato.name());
	}
	
	public void setStato(List<StatoRpt> stati) {
		this.stato = new ArrayList<String>();
		for(StatoRpt s : stati)
			this.stato.add(s.name());
	}

	public Boolean getConservato() {
		return this.conservato;
	}

	public void setConservato(Boolean conservato) {
		this.conservato = conservato;
	}

	public List<Long> getIdRpt() {
		return this.idRpt;
	}

	public void setIdRpt(List<Long> idRpt) {
		this.idRpt = idRpt;
	}

	public String getCcp() {
		return this.ccp;
	}

	public void setCcp(String ccp) {
		this.ccp = ccp;
	}

	public Long getIdPagamentoPortale() {
		return this.idPagamentoPortale;
	}

	public void setIdPagamentoPortale(Long idPagamentoPortale) {
		this.idPagamentoPortale = idPagamentoPortale;
	}
	public Date getDataInizio() {
		return this.dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return this.dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public String getCodPagamentoPortale() {
		return this.codPagamentoPortale;
	}

	public void setCodPagamentoPortale(String codPagamentoPortale) {
		this.codPagamentoPortale = codPagamentoPortale;
	}

	public String getCodApplicazione() {
		return this.codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public String getIdPendenza() {
		return this.idPendenza;
	}

	public void setIdPendenza(String idPendenza) {
		this.idPendenza = idPendenza;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getCfCittadinoPagamentoPortale() {
		return cfCittadinoPagamentoPortale;
	}

	public void setCfCittadinoPagamentoPortale(String cfCittadinoPagamentoPortale) {
		this.cfCittadinoPagamentoPortale = cfCittadinoPagamentoPortale;
	}

	public String getCodApplicazionePagamentoPortale() {
		return codApplicazionePagamentoPortale;
	}

	public void setCodApplicazionePagamentoPortale(String codApplicazionePagamentoPortale) {
		this.codApplicazionePagamentoPortale = codApplicazionePagamentoPortale;
	}

	public EsitoPagamento getEsitoPagamento() {
		return esitoPagamento;
	}

	public void setEsitoPagamento(EsitoPagamento esitoPagamento) {
		this.esitoPagamento = esitoPagamento;
	}

}
