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
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.orm.dao.jdbc.converter.PagamentoPortaleFieldConverter;

public class PagamentoPortaleFilter extends AbstractFilter {

	private Date dataInizio;
	private Date dataFine;
	private STATO stato;
	private String versante;
	private String idSessionePortale;
	private String idSessionePsp;
	private String idSessione;
	private List<String> codDomini;
	private Boolean ack;
	private String cfCittadino;
	private List<Long> idPagamentiPortale;
	private String tipoUtenza;
	private String codApplicazione;
	
	public enum SortFields {
		DATA
	}

	public PagamentoPortaleFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}

	public PagamentoPortaleFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.dataInizio != null) {
				newExpression.greaterEquals(it.govpay.orm.PagamentoPortale.model().DATA_RICHIESTA, this.dataInizio);
				addAnd = true;
			}
			if(this.dataFine != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.lessEquals(it.govpay.orm.PagamentoPortale.model().DATA_RICHIESTA, this.dataFine);
				addAnd = true;
			}
			if(this.stato != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(it.govpay.orm.PagamentoPortale.model().STATO, this.stato.toString());
				addAnd = true;
			}
			if(this.versante!= null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.PagamentoPortale.model().VERSANTE_IDENTIFICATIVO, this.versante);
				addAnd = true;
			}
			if(this.idSessionePortale!= null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.PagamentoPortale.model().ID_SESSIONE_PORTALE, this.idSessionePortale);
				addAnd = true;
			}
			if(this.idSessionePsp!= null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.PagamentoPortale.model().ID_SESSIONE_PSP, this.idSessionePsp);
				addAnd = true;
			}
			
			if(this.codDomini != null && this.codDomini.size() > 0) {
				if(addAnd)
					newExpression.and();
				this.codDomini.removeAll(Collections.singleton(null));
				
				newExpression.in(it.govpay.orm.PagamentoPortale.model().MULTI_BENEFICIARIO, this.codDomini);
				newExpression.isNotNull(it.govpay.orm.PagamentoPortale.model().MULTI_BENEFICIARIO);
			
				addAnd = true;
			}
			
			if(this.ack!=null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.PagamentoPortale.model().ACK, this.ack);
				
				addAnd = true;
			}
			
			if(this.cfCittadino!= null) {
				if(addAnd)
					newExpression.and();
				
				IExpression cfExpr = this.newExpression();
				
				cfExpr.equals(it.govpay.orm.PagamentoPortale.model().VERSANTE_IDENTIFICATIVO, this.cfCittadino);
				
				newExpression.and(cfExpr);
				
				addAnd = true;
			}
			
			if(this.idPagamentiPortale != null && !this.idPagamentiPortale.isEmpty()) {
				PagamentoPortaleFieldConverter converter = new PagamentoPortaleFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(it.govpay.orm.PagamentoPortale.model()));
				newExpression.in(cf, this.idPagamentiPortale);
				addAnd = true;
			}
			
			if(this.tipoUtenza!=null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.PagamentoPortale.model().TIPO_UTENZA, this.tipoUtenza);
				
				addAnd = true;
			}
			
			if(this.codApplicazione != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(it.govpay.orm.PagamentoPortale.model().ID_APPLICAZIONE.COD_APPLICAZIONE, this.codApplicazione);
				addAnd = true;
			}
			
			if(this.idSessione!= null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.PagamentoPortale.model().ID_SESSIONE, this.idSessione);
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
//		try {
			IExpression newExpression = super._toSimpleSearchExpression();

			return newExpression;
//		} catch (ExpressionNotImplementedException e) {
//			throw new ServiceException(e);
//		} catch (ExpressionException e) {
//			throw new ServiceException(e);
//		} catch (NotImplementedException e) {
//			throw new ServiceException(e);
//		}
	}

	public void addSortField(SortFields field, boolean asc) {
		FilterSortWrapper filterSortWrapper = new FilterSortWrapper();
		if(field.equals(SortFields.DATA)) 
			filterSortWrapper.setField(it.govpay.orm.PagamentoPortale.model().DATA_RICHIESTA); 
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
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

	public String getVersante() {
		return this.versante;
	}

	public void setVersante(String versante) {
		this.versante = versante;
	}

	public STATO getStato() {
		return this.stato;
	}

	public void setStato(STATO stato) {
		this.stato = stato;
	}

	public List<String> getCodDomini() {
		return this.codDomini;
	}

	public void setCodDomini(List<String> codDomini) {
		this.codDomini = codDomini;
	}

	public Boolean getAck() {
		return this.ack;
	}

	public void setAck(Boolean ack) {
		this.ack = ack;
	}

	public String getIdSessionePortale() {
		return this.idSessionePortale;
	}

	public void setIdSessionePortale(String idSessionePortale) {
		this.idSessionePortale = idSessionePortale;
	}

	public String getIdSessionePsp() {
		return idSessionePsp;
	}

	public void setIdSessionePsp(String idSessionePsp) {
		this.idSessionePsp = idSessionePsp;
	}

	public String getCfCittadino() {
		return cfCittadino;
	}

	public void setCfCittadino(String cfCittadino) {
		this.cfCittadino = cfCittadino;
	}

	public List<Long> getIdPagamentiPortale() {
		return idPagamentiPortale;
	}

	public void setIdPagamentiPortale(List<Long> idPagamentiPortale) {
		this.idPagamentiPortale = idPagamentiPortale;
	}

	public String getTipoUtenza() {
		return tipoUtenza;
	}

	public void setTipoUtenza(String tipoUtenza) {
		this.tipoUtenza = tipoUtenza;
	}

	public String getCodApplicazione() {
		return codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public String getIdSessione() {
		return idSessione;
	}

	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
	}

}
