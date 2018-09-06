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

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.GovpayConfig;
import it.govpay.orm.FR;

public class FrFilter extends AbstractFilter {
	
	//select distinct id from fr join rendicontazioni on rendicontazioni.id_fr = fr.id join pagamenti on rendicontazioni.id_pagamento = pagamenti.id join singoli_versamenti on pagamenti.id_singolo_versamento = singoli_versamenti.id join versamenti on singoli_versamenti.id_versamento = versamenti.id and versamenti.id_applicazione = 5;
	private Long idApplicazione;
	private List<String> codDominio;
	private String codPsp;
	private String stato;
	private Date datainizio;
	private Date dataFine;
	private List<Long> idFr; // Lista di fr.id
	private String codFlusso; // stringa da cercare in like tra i fr.cod_flusso
	private String tnr;
	private boolean nascondiSeSoloDiAltriIntermediari;
	private String iuv;
	

	public FrFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public FrFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(FR.model().COD_FLUSSO);
		this.listaFieldSimpleSearch.add(FR.model().IUR);
		this.listaFieldSimpleSearch.add(FR.model().ID_PAGAMENTO.IUV);
	}

	public List<Object> getFields(boolean count) throws ServiceException {
		List<Object> obj = new ArrayList<>();

		if(this.idApplicazione != null){
			obj.add(this.idApplicazione);
		}
		
		if(this.iuv != null){
			obj.add("%" + this.iuv.toLowerCase() + "%");
		}

		if(this.codDominio != null && !this.codDominio.isEmpty()){
			obj.addAll(this.codDominio);
		}

		if(this.codPsp!= null){
			obj.add(this.codPsp);
		}

		if(this.stato!= null){
			obj.add(this.stato);
		}

		if(this.datainizio!= null){
			obj.add(this.datainizio);
		}

		if(this.dataFine!= null){
			obj.add(this.dataFine);
		}

		if(this.idFr != null && !this.idFr.isEmpty()){
			obj.addAll(this.idFr);
		}

		if(this.codFlusso!= null){
			obj.add("%" + this.codFlusso.toLowerCase() + "%");
		}

		if(this.tnr!= null){
			obj.add("%" + this.tnr.toLowerCase() + "%");
		}

		// campi in or per la simple search 
		if(this.simpleSearch && StringUtils.isNotEmpty(this.simpleSearchString)){
			for (int i = 0; i < this.listaFieldSimpleSearch.size(); i++) {
				obj.add("%" + this.simpleSearchString.toLowerCase() + "%");
			}
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
			
			String placeholderWhereIn = "";
			String placeholderWhereOut = "";
			String placeholderJoin = "";
			String placeholderOffsetLimit = "";
			
			if(this.idApplicazione != null){
				if(placeholderWhereIn.length() > 0) {
					placeholderWhereIn += " AND ";
				} else {
					placeholderWhereIn += " WHERE ";
				}
				placeholderWhereIn += "v.id_applicazione = ?";
				placeholderJoin = " join pagamenti p on p.id=r.id_pagamento join singoli_versamenti sv on p.id_singolo_versamento=sv.id join versamenti v on sv.id_versamento=v.id ";
			}
			
			if(this.iuv != null){
				if(placeholderWhereIn.length() > 0) {
					placeholderWhereIn += " AND ";
				} else {
					placeholderWhereIn += " WHERE ";
				}
				placeholderWhereIn += this.ilike("r.iuv") + " like ?";
				// join non necessaria, tabella r gia' inserita nella query principale
			}

			if(this.codDominio != null && !this.codDominio.isEmpty()){
				if(placeholderWhereIn.length() > 0) {
					placeholderWhereIn += " AND ";
				} else {
					placeholderWhereIn += " WHERE ";
				}
				placeholderWhereIn += "fr."+this.getColumn(FR.model().COD_DOMINIO)+" = ?";
			}

			if(this.codPsp!= null){
				if(placeholderWhereIn.length() > 0) {
					placeholderWhereIn += " AND ";
				} else {
					placeholderWhereIn += " WHERE ";
				}
				placeholderWhereIn += "fr."+this.getColumn(FR.model().COD_PSP)+" = ?";
			}

			if(this.stato!= null){
				if(placeholderWhereIn.length() > 0) {
					placeholderWhereIn += " AND ";
				} else {
					placeholderWhereIn += " WHERE ";
				}
				placeholderWhereIn += "fr."+this.getColumn(FR.model().STATO)+" = ?";
			}

			if(this.datainizio!= null){
				if(placeholderWhereIn.length() > 0) {
					placeholderWhereIn += " AND ";
				} else {
					placeholderWhereIn += " WHERE ";
				}
				placeholderWhereIn += "fr."+this.getColumn(FR.model().DATA_ORA_FLUSSO)+" > ?";
			}

			if(this.dataFine!= null){
				if(placeholderWhereIn.length() > 0) {
					placeholderWhereIn += " AND ";
				} else {
					placeholderWhereIn += " WHERE ";
				}
				placeholderWhereIn += "fr."+this.getColumn(FR.model().DATA_ORA_FLUSSO)+" < ?";
			}

			if(this.idFr != null && !this.idFr.isEmpty()){
				if(placeholderWhereIn.length() > 0) {
					placeholderWhereIn += " AND ";
				} else {
					placeholderWhereIn += " WHERE ";
				}

				String idFrMarks = "";
				for (int i = 0; i < this.idFr.size(); i++) {
					if(i > 0) {
						idFrMarks += ",";						
					}
					idFrMarks += "?";
				}
				placeholderWhereIn += "fr.id in("+idFrMarks+")";
			}

			if(this.codFlusso!= null){
				if(placeholderWhereIn.length() > 0) {
					placeholderWhereIn += " AND ";
				} else {
					placeholderWhereIn += " WHERE ";
				}
				String field = "fr."+this.getColumn(FR.model().COD_FLUSSO);

				String iLikefield = this.ilike(field);		
				
				placeholderWhereIn += iLikefield +" like ?";
			}

			if(this.tnr!= null){
				if(placeholderWhereIn.length() > 0) {
					placeholderWhereIn += " AND ";
				} else {
					placeholderWhereIn += " WHERE ";
				}
				String field = "fr."+this.getColumn(FR.model().IUR);

				String iLikefield = this.ilike(field);		
				
				placeholderWhereIn += iLikefield +" like ?";

			}
			
			if(this.nascondiSeSoloDiAltriIntermediari) {
				placeholderWhereOut += "WHERE (ok+anomale > 0)";
			}
			
			// campi in or per la simple search 
			if(this.simpleSearch && StringUtils.isNotEmpty(this.simpleSearchString)){
				if(placeholderWhereIn.length() > 0) {
					placeholderWhereIn += " AND ";
				} else {
					placeholderWhereIn += " WHERE ";
				}
				placeholderWhereIn += " ( ";
				for (int i = 0; i < this.listaFieldSimpleSearch.size(); i++) {
					if(i > 0)
						placeholderWhereIn += " OR ";
					
					IField iField = this.listaFieldSimpleSearch.get(i);
					String field = null;
					if(iField.getFieldName().equals(FR.model().ID_PAGAMENTO.IUV.getFieldName())) {
						field = "r.iuv";
					} else {
						field = this.getColumn(this.listaFieldSimpleSearch.get(i),true);
					}
					
					String iLikefield = this.ilike(field);		
					
					placeholderWhereIn += iLikefield +" like ?";
				}
				placeholderWhereIn += " ) ";
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
			nativeQuery = nativeQuery.replaceAll("\\$PLACEHOLDER_WHERE_IN\\$", placeholderWhereIn);
			nativeQuery = nativeQuery.replaceAll("\\$PLACEHOLDER_WHERE_OUT\\$", placeholderWhereOut);
			nativeQuery = nativeQuery.replaceAll("\\$PLACEHOLDER_JOIN\\$", placeholderJoin);
			nativeQuery = nativeQuery.replaceAll("\\$PLACEHOLDER_OFFSET_LIMIT\\$", placeholderOffsetLimit);

			return nativeQuery;
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	private String ilike(String field) throws ServiceException {
		if(GovpayConfig.getInstance().getDatabaseType().equals("postgresql")) {
			return "lower("+field+")";
		}
		if(GovpayConfig.getInstance().getDatabaseType().equals("mysql")) {
			return "LOWER("+field+")";
		}
		if(GovpayConfig.getInstance().getDatabaseType().equals("oracle")) {
			return "LOWER("+field+")";
		}
		
		throw new ServiceException("Database " + GovpayConfig.getInstance().getDatabaseType() + " non supportato");
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			
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
			
			if(this.codDominio != null && this.codDominio.size() > 0){
				newExpression.in(FR.model().COD_DOMINIO, this.codDominio);
				addAnd = true;
			}
			
			if(this.codPsp != null && StringUtils.isNotEmpty(this.codPsp)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(FR.model().COD_PSP, this.codPsp);
				addAnd = true;
			}
			
			if(this.datainizio != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.greaterEquals(FR.model().DATA_ORA_FLUSSO, this.datainizio);
				addAnd = true;
			}
			
			if(this.dataFine != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.lessEquals(FR.model().DATA_ORA_FLUSSO, this.dataFine);
				addAnd = true;
			}
			
			if(this.codFlusso != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.ilike(FR.model().COD_FLUSSO, this.codFlusso, LikeMode.EXACT);
				addAnd = true;
			}
			if(this.idFr != null && !this.idFr.isEmpty()) {
				if(addAnd)
					newExpression.and();
				CustomField baseField = new CustomField("id", Long.class, "id", this.getRootTable());

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
		return this.stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Date getDatainizio() {
		return this.datainizio;
	}

	public void setDatainizio(Date datainizio) {
		this.datainizio = datainizio;
	}

	public Date getDataFine() {
		return this.dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public String getCodPsp() {
		return this.codPsp;
	}

	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}

	public Long getIdApplicazione() {
		return this.idApplicazione;
	}

	public void setIdApplicazione(long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}

	public List<String> getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(List<String> codDominio) {
		this.codDominio = codDominio;
	}
	public List<Long> getIdFr() {
		return this.idFr;
	}

	public void setIdFr(List<Long> idFr) {
		this.idFr = idFr;
	}

	public String getCodFlusso() {
		return this.codFlusso;
	}

	public void setCodFlusso(String codFlusso) {
		this.codFlusso = codFlusso;
	}

	public String getTnr() {
		return this.tnr;
	}

	public void setTnr(String tnr) {
		this.tnr = tnr;
	}

	public boolean isNascondiSeSoloDiAltriIntermediari() {
		return this.nascondiSeSoloDiAltriIntermediari;
	}

	public void setNascondiSeSoloDiAltriIntermediari(
			boolean nascondiSeSoloDiAltriIntermediari) {
		this.nascondiSeSoloDiAltriIntermediari = nascondiSeSoloDiAltriIntermediari;
	}

	public String getIuv() {
		return this.iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	
}
