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

import it.govpay.bd.AbstractFilter;
import it.govpay.orm.FR;

public class FrFilter extends AbstractFilter {
	
	//select distinct id from fr join rendicontazioni on rendicontazioni.id_fr = fr.id join pagamenti on rendicontazioni.id_pagamento = pagamenti.id join singoli_versamenti on pagamenti.id_singolo_versamento = singoli_versamenti.id join versamenti on singoli_versamenti.id_versamento = versamenti.id and versamenti.id_applicazione = 5;
	private Long idApplicazione;
	private List<String> codDominio;
	private String codPsp;
	private String stato;
	private Date datainizio;
	private Date dataFine;
	private List<String> idFr; // Lista di fr.id
	private String codFlusso; // stringa da cercare in like tra i fr.cod_flusso
	

	public FrFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression newExpression = newExpression();
			
			boolean addAnd = false;
			// Filtro sullo stato pagamenti
			if(this.stato != null && StringUtils.isNotEmpty(this.stato)){
				newExpression.equals(FR.model().STATO, this.stato);
				addAnd = true;
			}
			
			if(this.idApplicazione != null){
				newExpression.isNotNull(FR.model().ID_PAGAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE); //sempre not null, serve solo per scatenare la join
				
				
				CustomField idApplicazioneField = new CustomField("id_applicazione", Long.class, "id_applicazione", this.getTable(FR.model().ID_PAGAMENTO.ID_VERSAMENTO));
				newExpression.equals(idApplicazioneField, this.idApplicazione); //per scatenare la join
				addAnd = true;
			}
			
			if(this.codDominio != null){
				newExpression.in(FR.model().COD_DOMINIO, this.codDominio);
				addAnd = true;
			}
			
			if(this.codPsp != null && StringUtils.isNotEmpty(this.codPsp)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(FR.model().COD_PSP, this.codPsp);
				addAnd = true;
			}
			
			if(this.datainizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.between(FR.model().DATA_ORA_FLUSSO, this.datainizio,this.dataFine);
				addAnd = true;
			}
			
			if(this.codFlusso != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.like(FR.model().COD_FLUSSO, this.codFlusso);
				addAnd = true;
			}
			if(this.idFr != null && !this.idFr.isEmpty()) {
				if(addAnd)
					newExpression.and();
				CustomField baseField = new CustomField("id", Long.class, "id", getRootTable());

				newExpression.in(baseField, this.idFr);
				addAnd = true;
			}
			
			return newExpression;
		}  catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}
 
	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Date getDatainizio() {
		return datainizio;
	}

	public void setDatainizio(Date datainizio) {
		this.datainizio = datainizio;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public String getCodPsp() {
		return codPsp;
	}

	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}

	public Long getIdApplicazione() {
		return idApplicazione;
	}

	public void setIdApplicazione(long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}

	public List<String> getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(List<String> codDominio) {
		this.codDominio = codDominio;
	}
	public List<String> getIdFr() {
		return idFr;
	}

	public void setIdFr(List<String> idFr) {
		this.idFr = idFr;
	}

	public String getCodFlusso() {
		return codFlusso;
	}

	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
	}
}
