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
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.model.Fr;
import it.govpay.orm.FR;
import it.govpay.orm.Rendicontazione;
import it.govpay.orm.dao.jdbc.converter.FRFieldConverter;
import it.govpay.orm.model.FRModel;

public class FrFilter extends AbstractFilter {
	
	//select distinct id from fr join rendicontazioni on rendicontazioni.id_fr = fr.id join pagamenti on rendicontazioni.id_pagamento = pagamenti.id join singoli_versamenti on pagamenti.id_singolo_versamento = singoli_versamenti.id join versamenti on singoli_versamenti.id_versamento = versamenti.id and versamenti.id_applicazione = 5;
	private Long idApplicazione;
	private List<String> codDominio;
	private String codDominioFiltro;
	private String codPsp;
	private Fr.StatoFr stato;
	private Date datainizio;
	private Date dataFine;
	private List<Long> idFr; // Lista di fr.id
	private String codFlusso; // stringa da cercare in like tra i fr.cod_flusso
	private String tnr;
	private boolean nascondiSeSoloDiAltriIntermediari;
	private String iuv;
	private Boolean incassato;
	private List<IdUnitaOperativa> dominiUOAutorizzati;
	private boolean searchModeEquals = false; 
	private Boolean obsoleto;
	private boolean ricercaIdFlussoCaseInsensitive = false;
	
	FRFieldConverter converter = null;

	public FrFilter(IExpressionConstructor expressionConstructor) throws ServiceException {
		this(expressionConstructor,false);
	}
	
	public FrFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) throws ServiceException {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(FR.model().COD_FLUSSO);
		this.listaFieldSimpleSearch.add(FR.model().IUR);
		this.listaFieldSimpleSearch.add(Rendicontazione.model().ID_PAGAMENTO.IUV);
		this.converter = new FRFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
	}

//	public List<Object> getFields(boolean count) throws ServiceException {
//		List<Object> obj = new ArrayList<>();
//
//		if(this.idApplicazione != null){
//			obj.add(this.idApplicazione);
//		}
//		
//		if(this.iuv != null){
//			obj.add("%" + this.iuv.toLowerCase() + "%");
//		}
//
//		if(this.codDominio != null && !this.codDominio.isEmpty()){
//			obj.addAll(this.codDominio);
//		}
//
//		if(this.codPsp!= null){
//			obj.add(this.codPsp);
//		}
//
//		if(this.stato!= null){
//			obj.add(this.stato);
//		}
//
//		if(this.datainizio!= null){
//			obj.add(this.datainizio);
//		}
//
//		if(this.dataFine!= null){
//			obj.add(this.dataFine);
//		}
//
//		if(this.idFr != null && !this.idFr.isEmpty()){
//			obj.addAll(this.idFr);
//		}
//
//		if(this.codFlusso!= null){
//			obj.add("%" + this.codFlusso.toLowerCase() + "%");
//		}
//
//		if(this.tnr!= null){
//			obj.add("%" + this.tnr.toLowerCase() + "%");
//		}
//
//		// campi in or per la simple search 
//		if(this.simpleSearch && StringUtils.isNotEmpty(this.simpleSearchString)){
//			for (int i = 0; i < this.listaFieldSimpleSearch.size(); i++) {
//				obj.add("%" + this.simpleSearchString.toLowerCase() + "%");
//			}
//		}
//
//		if(this.getOffset() != null && this.getLimit() != null && !count) {
//			obj.add(this.getOffset());
//			if(GovpayConfig.getInstance().getDatabaseType().equals("oracle")) {
//				obj.add(this.getOffset()+this.getLimit());
//			} else {
//				obj.add(this.getLimit());
//			}
//		}
//
//		return obj;
//	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			
			boolean addAnd = false;
			// Filtro sullo stato pagamenti
			if(this.stato != null){
				newExpression.equals(FR.model().STATO, this.stato.toString());
				addAnd = true;
			}
			
			if(this.idApplicazione != null){
				newExpression.isNotNull(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.COD_VERSAMENTO_ENTE); //sempre not null, serve solo per scatenare la join
				
				
				CustomField idApplicazioneField = new CustomField("id_applicazione", Long.class, "id_applicazione", this.getTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
				newExpression.equals(idApplicazioneField, this.idApplicazione); //per scatenare la join
				addAnd = true;
			}
			
			if(this.getIncassato() != null){
				if(addAnd)
					newExpression.and();
				
				CustomField idIncassoField = new CustomField("id_incasso", Long.class, "id_incasso", this.getTable(FR.model()));
				if(this.getIncassato()) {
					newExpression.isNotNull(idIncassoField);
				} else {
					newExpression.isNull(idIncassoField);
				}

				addAnd = true;
			}
			
			if(this.codDominio != null && this.codDominio.size() > 0){
				if(addAnd)
					newExpression.and();
				
				newExpression.in(FR.model().COD_DOMINIO, this.codDominio);
				addAnd = true;
			}
			
			if(this.codDominioFiltro != null){
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(FR.model().COD_DOMINIO, this.codDominioFiltro);
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
				
				newExpression.greaterEquals(FR.model().DATA_ACQUISIZIONE, this.datainizio);
				addAnd = true;
			}
			
			if(this.dataFine != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.lessEquals(FR.model().DATA_ACQUISIZIONE, this.dataFine);
				addAnd = true;
			}
			
			if(this.codFlusso != null && StringUtils.isNotEmpty(this.codFlusso)) {
				if(addAnd)
					newExpression.and();
				
				if(!this.searchModeEquals) {
					if(this.ricercaIdFlussoCaseInsensitive) {
						newExpression.ilike(FR.model().COD_FLUSSO, this.codFlusso, LikeMode.ANYWHERE);
					} else {
						newExpression.like(FR.model().COD_FLUSSO, this.codFlusso, LikeMode.ANYWHERE);
					}
				}else {
					if(this.ricercaIdFlussoCaseInsensitive) {
						IExpression newExpressionIngnoreCase = this.newExpression();
						
						newExpressionIngnoreCase.equals(FR.model().COD_FLUSSO, this.codFlusso).or()
						.equals(FR.model().COD_FLUSSO, this.codFlusso.toUpperCase()).or()
						.equals(FR.model().COD_FLUSSO, this.codFlusso.toLowerCase());
						
						newExpression.and(newExpressionIngnoreCase);
					} else {
						newExpression.equals(FR.model().COD_FLUSSO, this.codFlusso);
					}
				}
				addAnd = true;
			}
			if(this.idFr != null && !this.idFr.isEmpty()) {
				if(addAnd)
					newExpression.and();
				CustomField baseField = new CustomField("id", Long.class, "id", this.getRootTable());

				newExpression.in(baseField, this.idFr);
				addAnd = true;
			}
			
			if(this.dominiUOAutorizzati != null && this.dominiUOAutorizzati.size() > 0) {
				if(addAnd)
					newExpression.and();
				
				newExpression.isNotNull(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO.STATO_VERSAMENTO);
				IExpression newExpressionUO = this.newExpression();
				List<IExpression> listExpressionSingolaUO = new ArrayList<>();
				
				for (IdUnitaOperativa idUnita : this.dominiUOAutorizzati) {
					if(idUnita.getIdDominio() != null) {
						IExpression newExpressionSingolaUO = this.newExpression();
						
						CustomField idDominioCustomField = new CustomField("id_dominio", Long.class, "id_dominio", this.getTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
						newExpressionSingolaUO.equals(idDominioCustomField, idUnita.getIdDominio());
						
						if(idUnita.getIdUnita() != null ) {
							CustomField iduoCustomField = new CustomField("id_uo", Long.class, "id_uo", this.getTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
							newExpressionSingolaUO.and().equals(iduoCustomField, idUnita.getIdUnita());
						}
						
						listExpressionSingolaUO.add(newExpressionSingolaUO);
					}
				}
				
				newExpressionUO.or(listExpressionSingolaUO.toArray(new IExpression[listExpressionSingolaUO.size()]));
				newExpression.and(newExpressionUO);
				addAnd = true;
			}
			
			if(this.obsoleto != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(FR.model().OBSOLETO, this.obsoleto);
				addAnd = true;
			}
			
			if(this.iuv != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(FR.model().ID_RENDICONTAZIONE.IUV, this.iuv, LikeMode.ANYWHERE);
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
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			FRFieldConverter converter = new FRFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			FRModel model = it.govpay.orm.FR.model();
			
			boolean addTabellaVersamenti = false;
			boolean addTabellaRendicontazioni = false;
			
			String tableNameRendicontazioni = converter.toAliasTable(model.ID_RENDICONTAZIONE);
			String tableNameFr = converter.toAliasTable(model);
			
			// Filtro sullo stato pagamenti
			if(this.stato != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.STATO, true) + " = ? ");
			}
			
			if(this.idApplicazione != null){
				String tableNameVersamenti = converter.toAliasTable(model.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO);
				if(!addTabellaVersamenti) {
					String tableNameSingoliVersamenti = converter.toAliasTable(model.ID_SINGOLO_VERSAMENTO);
					
					// FR -> R
					sqlQueryObject.addFromTable(tableNameRendicontazioni);
					sqlQueryObject.addWhereCondition(tableNameFr+".id="+tableNameRendicontazioni+".id_fr");
					// R -> SV
					sqlQueryObject.addFromTable(tableNameSingoliVersamenti);
					sqlQueryObject.addWhereCondition(tableNameRendicontazioni+".id_singolo_versamento="+tableNameSingoliVersamenti+".id");
					// SV -> V
					sqlQueryObject.addFromTable(tableNameVersamenti);
					sqlQueryObject.addWhereCondition(tableNameSingoliVersamenti+".id_versamento="+tableNameVersamenti+".id");

					addTabellaVersamenti = true;
					addTabellaRendicontazioni = true;
				}

				sqlQueryObject.addWhereCondition(true, tableNameVersamenti + ".id_applicazione = ? ");
			}
			
			if(this.getIncassato() != null){
				if(this.getIncassato()) {
					sqlQueryObject.addWhereIsNotNullCondition(converter.toTable(model.COD_FLUSSO, true) + ".id_incasso");
				} else {
					sqlQueryObject.addWhereIsNullCondition(converter.toTable(model.COD_FLUSSO, true) + ".id_incasso");
				}
			}
			
			if(this.codDominio != null && this.codDominio.size() > 0){
				this.codDominio.removeAll(Collections.singleton(null));
				
				String [] codDomini = this.codDominio.toArray(new String[this.codDominio.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.COD_DOMINIO, true) + ".cod_dominio", true, codDomini );
			}
			
			if(this.codDominioFiltro != null){
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_DOMINIO, true) + " = ? ");
			}
			
			if(this.codPsp != null && StringUtils.isNotEmpty(this.codPsp)) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_PSP, true) + " = ? ");
			}
			
			if(this.datainizio != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_ACQUISIZIONE, true) + " >= ? ");
			}
			
			if(this.dataFine != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DATA_ACQUISIZIONE, true) + " <= ? ");
			}
			
			if(this.codFlusso != null && StringUtils.isNotEmpty(this.codFlusso)) {
				if(!this.searchModeEquals) {
					if(this.ricercaIdFlussoCaseInsensitive) {
						sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.COD_FLUSSO, true), this.codFlusso, true, true);
					} else {
						sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.COD_FLUSSO, true), this.codFlusso, true, false);
					}
				}else {
					if(this.ricercaIdFlussoCaseInsensitive) {
						sqlQueryObject.addWhereCondition(false,converter.toColumn(model.COD_FLUSSO, true) + " = ? ",converter.toColumn(model.COD_FLUSSO, true) + " = ? ",converter.toColumn(model.COD_FLUSSO, true) + " = ? ");
					} else {
						sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_FLUSSO, true) + " = ? ");
					}
				}
			}
			
			if(this.idFr != null && !this.idFr.isEmpty()) {
				this.idFr.removeAll(Collections.singleton(null));
				
				String [] idsFR = this.idFr.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idFr.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.COD_FLUSSO, true) + ".id", false, idsFR );
			}
			
			if(this.dominiUOAutorizzati != null && this.dominiUOAutorizzati.size() > 0) {
				String tableNameVersamenti = converter.toAliasTable(model.ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO);
				if(!addTabellaVersamenti) {
					String tableNameSingoliVersamenti = converter.toAliasTable(model.ID_SINGOLO_VERSAMENTO);
					
					// FR -> R
					sqlQueryObject.addFromTable(tableNameRendicontazioni);
					sqlQueryObject.addWhereCondition(tableNameFr+".id="+tableNameRendicontazioni+".id_fr");
					// R -> SV
					sqlQueryObject.addFromTable(tableNameSingoliVersamenti);
					sqlQueryObject.addWhereCondition(tableNameRendicontazioni+".id_singolo_versamento="+tableNameSingoliVersamenti+".id");
					// SV -> V
					sqlQueryObject.addFromTable(tableNameVersamenti);
					sqlQueryObject.addWhereCondition(tableNameSingoliVersamenti+".id_versamento="+tableNameVersamenti+".id");

					addTabellaVersamenti = true;
					addTabellaRendicontazioni = true;
				}
				
				List<String> uoConditions = new ArrayList<>();
				for (IdUnitaOperativa idUnita : this.dominiUOAutorizzati) {
					if(idUnita.getIdDominio() != null) {
						
						StringBuilder sb = new StringBuilder();
						sb.append(tableNameVersamenti + ".id_dominio = ? ");
						
//						IExpression newExpressionSingolaUO = this.newExpression();
//						
//						CustomField idDominioCustomField = new CustomField("id_dominio", Long.class, "id_dominio", this.getTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
//						newExpressionSingolaUO.equals(idDominioCustomField, idUnita.getIdDominio());
						
						if(idUnita.getIdUnita() != null ) {
							sb.append(" and ");
							sb.append(tableNameVersamenti + ".id_uo = ? ");
//							CustomField iduoCustomField = new CustomField("id_uo", Long.class, "id_uo", this.getTable(FR.model().ID_SINGOLO_VERSAMENTO.ID_VERSAMENTO));
//							newExpressionSingolaUO.and().equals(iduoCustomField, idUnita.getIdUnita());
						}
						
						uoConditions.add(sb.toString());
					}
				}
				if(!uoConditions.isEmpty()) {
					sqlQueryObject.addWhereCondition(false, uoConditions.toArray(new String[uoConditions.size()]));
				}
			}
			
			if(this.obsoleto != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.OBSOLETO, true) + " = ? ");
			}
			
			if(this.iuv != null) {
				if(!addTabellaRendicontazioni) {
					// FR -> R
					sqlQueryObject.addFromTable(tableNameRendicontazioni);
					sqlQueryObject.addWhereCondition(tableNameFr+".id="+tableNameRendicontazioni+".id_fr");
				}
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.ID_RENDICONTAZIONE.IUV, true), this.iuv, true, true);
			}
			
			return sqlQueryObject;
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (SQLQueryObjectException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Object[] getParameters(ISQLQueryObject sqlQueryObject) throws ServiceException {
		List<Object> lst = new ArrayList<Object>();
		
		if(this.stato != null){
			lst.add(this.stato.toString());
		}
		
		if(this.idApplicazione != null){
			lst.add(this.idApplicazione);
		}
		
		if(this.getIncassato() != null){
			// donothing
		}
		
		if(this.codDominio != null && this.codDominio.size() > 0){
			// donothing
		}
		
		if(this.codDominioFiltro != null){
			lst.add(this.codDominioFiltro);
		}
		
		if(this.codPsp != null && StringUtils.isNotEmpty(this.codPsp)) {
			lst.add(this.codPsp);
		}
		
		if(this.datainizio != null) {
			lst.add(this.datainizio);
		}
		
		if(this.dataFine != null) {
			lst.add(this.dataFine);
		}
		
		if(this.codFlusso != null && StringUtils.isNotEmpty(this.codFlusso)) {
			if(this.searchModeEquals) {
				if(this.ricercaIdFlussoCaseInsensitive) {
					lst.add(this.codFlusso);
					lst.add(this.codFlusso.toUpperCase());
					lst.add(this.codFlusso.toLowerCase());
				}else {
					lst.add(this.codFlusso);
				}
			}
		}
		
		if(this.idFr != null && !this.idFr.isEmpty()) {
			// donothing
		}
		
		if(this.dominiUOAutorizzati != null && this.dominiUOAutorizzati.size() > 0) {
			for (IdUnitaOperativa idUnita : this.dominiUOAutorizzati) {
				if(idUnita.getIdDominio() != null) {
					lst.add(idUnita.getIdDominio());
					if(idUnita.getIdUnita() != null ) {
						lst.add(idUnita.getIdUnita());
					}
				}
			}
		}
		
		if(this.obsoleto != null) {
			try {
				lst = this.setValoreFiltroBoolean(lst, converter, this.obsoleto);
			} catch (ExpressionException e) {
				throw new ServiceException(e);
			}
			
		}
		
		if(this.iuv != null) {
			// donothing
		}
		
		return lst.toArray(new Object[lst.size()]);
	}
	
	public Fr.StatoFr getStato() {
		return this.stato;
	}

	public void setStato(Fr.StatoFr stato) {
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

	public String getCodDominioFiltro() {
		return codDominioFiltro;
	}

	public void setCodDominioFiltro(String codDominioFiltro) {
		this.codDominioFiltro = codDominioFiltro;
	}

	public Boolean getIncassato() {
		return incassato;
	}

	public void setIncassato(Boolean incassato) {
		this.incassato = incassato;
	}
	
	public void setDominiUOAutorizzati(List<IdUnitaOperativa> dominiUOAutorizzati) {
		this.dominiUOAutorizzati = dominiUOAutorizzati;
	}

	public List<IdUnitaOperativa> getDominiUOAutorizzati() {
		return dominiUOAutorizzati;
	}
	
	public boolean isSearchModeEquals() {
		return this.searchModeEquals;
	}

	public void setSearchModeEquals(boolean searchModeEquals) {
		this.searchModeEquals = searchModeEquals;
	}

	public Boolean getObsoleto() {
		return obsoleto;
	}

	public void setObsoleto(Boolean obsoleto) {
		this.obsoleto = obsoleto;
	}

	public boolean isRicercaIdFlussoCaseInsensitive() {
		return ricercaIdFlussoCaseInsensitive;
	}

	public void setRicercaIdFlussoCaseInsensitive(boolean ricercaIdFlussoCaseInsensitive) {
		this.ricercaIdFlussoCaseInsensitive = ricercaIdFlussoCaseInsensitive;
	}
}
