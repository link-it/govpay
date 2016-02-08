/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.bd.pagamento;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.Versamento;
import it.govpay.orm.dao.jdbc.converter.VersamentoFieldConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.generic_project.expression.SortOrder;

public class VersamentoFilter extends AbstractFilter {

	
	private String statoPagamento =  null;
	
	private String statoRendicontazione = null;
	
	private String iuv =null;
	
	private String codEnte = null;
	private String codUnivocoDebitore;
	private Boolean terminato;
	private List<Long> idEnti;
	private List<Long> idApplicazioni;

	private Date datainizio;
	private Date dataFine;
	
	public enum SortFields {
		STATO
//TODO		COD_PSP, COD_FLUSSO
		}
	
	public VersamentoFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			// Filtro sullo stato pagamenti
			if(this.statoPagamento != null && StringUtils.isNotEmpty(this.statoPagamento)){
				newExpression.equals(Versamento.model().STATO_VERSAMENTO, this.statoPagamento);
				addAnd = true;
			}
			
			if(this.statoRendicontazione != null && StringUtils.isNotEmpty(this.statoRendicontazione)){
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Versamento.model().STATO_RENDICONTAZIONE, this.statoRendicontazione);
				addAnd = true;
			}
			
			if(this.codEnte != null && StringUtils.isNotEmpty(this.codEnte)){
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Versamento.model().ID_ENTE.COD_ENTE, this.codEnte);
				addAnd = true;
			}
			
			if(this.iuv != null && StringUtils.isNotEmpty(this.iuv)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(Versamento.model().IUV, this.iuv, LikeMode.ANYWHERE);
			}
			
			if(this.datainizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.between(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO, this.datainizio,this.dataFine);
				addAnd = true;
			}
			
			if((this.idEnti != null && !this.idEnti.isEmpty()) || (this.idApplicazioni != null && !this.idApplicazioni.isEmpty())) {
				IExpression newExpressionEntiApplicazioni = this.newExpression();
				VersamentoFieldConverter versamentoFieldConverter = new VersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				
				CustomField idEnteCustomField = new CustomField("id_ente",  Long.class, "id_ente",  versamentoFieldConverter.toTable(Versamento.model()));
				CustomField idApplicazioneCustomField = new CustomField("id_applicazione",  Long.class, "id_applicazione",  versamentoFieldConverter.toTable(Versamento.model()));
				
				if(this.idEnti != null && !this.idEnti.isEmpty()) {
					for(Long id: this.idEnti) {
						newExpressionEntiApplicazioni.equals(idEnteCustomField, id).or();
					}
				}
				
				if(this.idApplicazioni != null && !this.idApplicazioni.isEmpty()) {
					for(Long id: this.idApplicazioni) {
						newExpressionEntiApplicazioni.equals(idApplicazioneCustomField, id).or();
					}
				}
				newExpression.and(newExpressionEntiApplicazioni);
				
			}
			
			if(this.codUnivocoDebitore != null) {
				newExpression.equals(Versamento.model().ID_ANAGRAFICA_DEBITORE.COD_UNIVOCO, this.codUnivocoDebitore);
			}
			
			if(this.terminato != null) {
				List<String> lst = new ArrayList<String>();
				
				if(this.terminato.booleanValue()) {
					lst.add(it.govpay.bd.model.Versamento.StatoVersamento.PAGAMENTO_ESEGUITO.toString());
					lst.add(it.govpay.bd.model.Versamento.StatoVersamento.PAGAMENTO_NON_ESEGUITO.toString());
					lst.add(it.govpay.bd.model.Versamento.StatoVersamento.PAGAMENTO_PARZIALMENTE_ESEGUITO.toString());
					lst.add(it.govpay.bd.model.Versamento.StatoVersamento.DECORRENZA_TERMINI.toString());
					lst.add(it.govpay.bd.model.Versamento.StatoVersamento.DECORRENZA_TERMINI_PARZIALE.toString());
					lst.add(it.govpay.bd.model.Versamento.StatoVersamento.ANNULLATO.toString());
					lst.add(it.govpay.bd.model.Versamento.StatoVersamento.SCADUTO.toString());
					lst.add(it.govpay.bd.model.Versamento.StatoVersamento.FALLITO.toString());
				} else {
					lst.add(it.govpay.bd.model.Versamento.StatoVersamento.IN_ATTESA.toString());
					lst.add(it.govpay.bd.model.Versamento.StatoVersamento.IN_CORSO.toString());
					lst.add(it.govpay.bd.model.Versamento.StatoVersamento.AUTORIZZATO_DIFFERITO.toString());
					lst.add(it.govpay.bd.model.Versamento.StatoVersamento.AUTORIZZATO_IMMEDIATO.toString());
				}
				
				newExpression.in(Versamento.model().STATO_VERSAMENTO, lst);
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
		if(field.equals(SortFields.STATO)) 
			filterSortWrapper.setField(Versamento.model().STATO_VERSAMENTO); 
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
	}

	public String getStatoPagamento() {
		return statoPagamento;
	}

	public void setStatoPagamento(String statoPagamento) {
		this.statoPagamento = statoPagamento;
	}

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public String getStatoRendicontazione() {
		return statoRendicontazione;
	}

	public void setStatoRendicontazione(String statoRendicontazione) {
		this.statoRendicontazione = statoRendicontazione;
	}

	public String getCodEnte() {
		return codEnte;
	}

	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}

	public List<Long> getIdEnti() {
		if(idEnti == null) idEnti = new ArrayList<Long>();
		return idEnti;
	}

	public void setIdEnti(List<Long> idEnti) {
		this.idEnti = idEnti;
	}

	public List<Long> getIdApplicazioni() {
		if(idApplicazioni == null) idApplicazioni = new ArrayList<Long>();
		return idApplicazioni;
	}

	public void setIdApplicazioni(List<Long> idApplicazioni) {
		this.idApplicazioni = idApplicazioni;
	}

	public String getCodUnivocoDebitore() {
		return codUnivocoDebitore;
	}

	public void setCodUnivocoDebitore(String codUnivocoDebitore) {
		this.codUnivocoDebitore = codUnivocoDebitore;
	}

	public Boolean getTerminato() {
		return terminato;
	}

	public void setTerminato(Boolean terminato) {
		this.terminato = terminato;
	}
	
	
	
}
