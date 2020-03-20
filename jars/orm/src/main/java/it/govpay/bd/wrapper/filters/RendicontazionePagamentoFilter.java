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
import it.govpay.bd.GovpayConfig;
import it.govpay.orm.RendicontazionePagamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.sql.ISQLQueryObject;

public class RendicontazionePagamentoFilter extends AbstractFilter {
	
	private Long codApplicazione;
	private Date dataPagamentoMin;
	private Date dataPagamentoMax;
	private String codFlusso;

	private List<String> codDomini;
	public void setCodDomini(List<String> codDomini) {
		this.codDomini = codDomini;
	}

	public void setIdVersamento(List<Long> idVersamento) {
		this.idVersamento = idVersamento;
	}

	public void setStatoVersamento(String statoVersamento) {
		this.statoVersamento = statoVersamento;
	}

	public void setIgnoraStatoVersamento(boolean ignoraStatoVersamento) {
		this.ignoraStatoVersamento = ignoraStatoVersamento;
	}

	private List<Long> idVersamento;
	private List<Long> idPagamento;
	
	private String statoVersamento;
	private boolean ignoraStatoVersamento;
	

	public enum SortFields {
	}
	
	public RendicontazionePagamentoFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}
	
	public RendicontazionePagamentoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
	}

	public List<Object> getFields(boolean count) throws ServiceException {
		List<Object> obj = new ArrayList<>();

		if(this.dataPagamentoMin != null){
			obj.add(this.dataPagamentoMin);
		}
		if(this.dataPagamentoMax != null){
			obj.add(this.dataPagamentoMax);
		}
		if(this.idPagamento != null && !this.idPagamento.isEmpty()){
			obj.addAll(this.idPagamento);
		}
		if(this.codDomini != null && !this.codDomini.isEmpty()){
			obj.addAll(this.codDomini);
		}
		
		//ripeto per doppia condizione, nella query inner
		if(this.dataPagamentoMin != null){
			obj.add(this.dataPagamentoMin);
		}
		if(this.dataPagamentoMax != null){
			obj.add(this.dataPagamentoMax);
		}
		if(this.idPagamento != null && !this.idPagamento.isEmpty()){
			obj.addAll(this.idPagamento);
		}
		if(this.codDomini != null && !this.codDomini.isEmpty()){
			obj.addAll(this.codDomini);
		}

		//query outter
		
		if(this.idVersamento != null && !this.idVersamento.isEmpty()){
			obj.addAll(this.idVersamento);
		}
		if(!this.ignoraStatoVersamento && this.statoVersamento != null){
			obj.add(this.statoVersamento);
		}
		if(this.codApplicazione != null){
			obj.add(this.codApplicazione);
		}
		if(this.codFlusso != null) {
			obj.add(this.codFlusso);
		}
		
		if(this.getOffset() != null && this.getLimit() != null && !count) {
			obj.add(this.getOffset());
			if(GovpayConfig.getInstance().getDatabaseType().equals("oracle")) {
				obj.add(this.getOffset()+this.getLimit());
			} else {
				obj.add(this.getLimit());
			}
		}

		return obj;
	}

	public String getSQLFilterString(String nativeQuery) throws ServiceException {
		try {
			
			String placeholderIn = "";
			String placeholderOut = "";
			String placeholderOffsetLimit = "";
			if(this.dataPagamentoMin != null){
				if(placeholderIn.length() > 0) {
					placeholderIn += " AND ";
				} else {
					placeholderIn += " WHERE ";
				}
				placeholderIn += "p." + this.getColumn(RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO) + " > ?";
			}
			if(this.dataPagamentoMax != null){
				if(placeholderIn.length() > 0) {
					placeholderIn += " AND ";
				} else {
					placeholderIn += " WHERE ";
				}
				placeholderIn += "p." + this.getColumn(RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO) + " <  ?";
			}
			if(this.idPagamento != null && !this.idPagamento.isEmpty()){
				if(placeholderIn.length() > 0) {
					placeholderIn += " AND ";
				} else {
					placeholderIn += " WHERE ";
				}
				String idSingoliVersamenti = "";
				for(@SuppressWarnings("unused") Long idPagamento: this.idPagamento) {
					if(idSingoliVersamenti.length() > 0) {
						idSingoliVersamenti += ",";
					}
					idSingoliVersamenti += "?";
				}
				placeholderIn +=  "p.id in ("+idSingoliVersamenti+")";				
			}
			if(this.codDomini != null && !this.codDomini.isEmpty()){
				if(placeholderIn.length() > 0) {
					placeholderIn += " AND ";
				} else {
					placeholderIn += " WHERE ";
				}
				String idDomini = "";
				for(@SuppressWarnings("unused") String idDominio: this.codDomini) {
					if(idDomini.length() > 0) {
						idDomini += ",";
					}
					idDomini += "?";
				}
				placeholderIn +=  "p." + this.getColumn(RendicontazionePagamento.model().PAGAMENTO.COD_DOMINIO) + " in ("+idDomini+")";				
			}
			if(this.idVersamento != null && !this.idVersamento.isEmpty()){
				if(placeholderOut.length() > 0) {
					placeholderOut += " AND ";
				} else {
					placeholderOut += " WHERE ";
				}
				String idVersamenti = "";
				for(@SuppressWarnings("unused") Long idPagamento: this.idVersamento) {
					if(idVersamenti.length() > 0) {
						idVersamenti += ",";
					}
					idVersamenti += "?";
				}
				placeholderOut +=  "versamenti.id in ("+idVersamenti+")";				
			}
			if(this.ignoraStatoVersamento){
				if(placeholderOut.length() > 0) {
					placeholderOut += " AND ";
				} else {
					placeholderOut += " WHERE ";
				}
				placeholderOut +=  "versamenti." + this.getColumn(RendicontazionePagamento.model().VERSAMENTO.STATO_VERSAMENTO) + " != 'NON_ESEGUITO' and versamenti." + this.getColumn(RendicontazionePagamento.model().VERSAMENTO.STATO_VERSAMENTO) + " != 'ANNULLATO'";
			} else {
				if(this.statoVersamento != null){
					if(placeholderOut.length() > 0) {
						placeholderOut += " AND ";
					} else {
						placeholderOut += " WHERE ";
					}
					placeholderOut += "versamenti." + this.getColumn(RendicontazionePagamento.model().VERSAMENTO.STATO_VERSAMENTO) + " = ?";
				}
			}
			if(this.codApplicazione != null){
				if(placeholderOut.length() > 0) {
					placeholderOut += " AND ";
				} else {
					placeholderOut += " WHERE ";
				}
				placeholderOut += "versamenti.id_applicazione =  ?";
			}
			if(this.codFlusso != null) {
				if(placeholderOut.length() > 0) {
					placeholderOut += " AND ";
				} else {
					placeholderOut += " WHERE ";
				}
				placeholderOut += "fr." + this.getColumn(RendicontazionePagamento.model().FR.COD_FLUSSO) + " = ?";
			}
			
			if(this.getOffset() != null && this.getLimit() != null) {
				if(GovpayConfig.getInstance().getDatabaseType().equals("postgresql")) {
					placeholderOffsetLimit = "OFFSET ? LIMIT ?";
				}
				if(GovpayConfig.getInstance().getDatabaseType().equals("mysql")) {
					placeholderOffsetLimit = "LIMIT ?,?";
				}
				if(GovpayConfig.getInstance().getDatabaseType().equals("oracle")) {
					placeholderOffsetLimit = "WHERE (  rowNumber > ? AND  rowNumber <= ? )";
				}
			}
			nativeQuery = nativeQuery.replaceAll("\\$PLACEHOLDER_IN\\$", placeholderIn);
			nativeQuery = nativeQuery.replaceAll("\\$PLACEHOLDER_OUT\\$", placeholderOut);
			nativeQuery = nativeQuery.replaceAll("\\$PLACEHOLDER_OFFSET_LIMIT\\$", placeholderOffsetLimit);

			return nativeQuery;
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
//			if(this.codDominio != null){
//				newExpression.equals(RendicontazionePagamento.model().FR.COD_DOMINIO, this.codDominio);
//			}
//			if(this.codApplicazione != null){
//				newExpression.equals(RendicontazionePagamento.model().VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE, this.codApplicazione);
//			}
//			if(this.dataPagamentoMin != null){
//				newExpression.greaterEquals(RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO, this.dataPagamentoMin);
//			}
//			if(this.dataPagamentoMax != null){
//				newExpression.lessEquals(RendicontazionePagamento.model().PAGAMENTO.DATA_PAGAMENTO, this.dataPagamentoMax);
//			}
//			if(this.codFlusso != null) {
//				newExpression.equals(RendicontazionePagamento.model().FR.COD_FLUSSO, this.codFlusso);
//			}
			
			return newExpression;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
//		} catch (ExpressionNotImplementedException e) {
//			throw new ServiceException(e);
//		} catch (ExpressionException e) {
//			throw new ServiceException(e);
		}
	}
	
	public void addSortField(SortFields field, boolean asc) {
		FilterSortWrapper filterSortWrapper = new FilterSortWrapper();
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		return null;
	}

	@Override
	public Object[] getParameters(ISQLQueryObject sqlQueryObject) throws ServiceException {
		return null;
	}

	public Long getCodApplicazione() {
		return this.codApplicazione;
	}

	public void setCodApplicazione(Long codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public Date getDataPagamentoMin() {
		return this.dataPagamentoMin;
	}

	public void setDataPagamentoMin(Date dataPagamentoMin) {
		this.dataPagamentoMin = dataPagamentoMin;
	}

	public Date getDataPagamentoMax() {
		return this.dataPagamentoMax;
	}

	public void setDataPagamentoMax(Date dataPagamentoMax) {
		this.dataPagamentoMax = dataPagamentoMax;
	}

	public String getCodFlusso() {
		return this.codFlusso;
	}

	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
	}

	public void setIdPagamento(List<Long> idPagamento) {
		this.idPagamento = idPagamento;
	}

	
}
