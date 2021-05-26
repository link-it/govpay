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
package it.govpay.bd.anagrafica.filters;

import java.util.ArrayList;
import java.util.Collections;
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
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.TipoVersamentoDominio;
import it.govpay.orm.dao.jdbc.converter.TipoVersamentoDominioFieldConverter;
import it.govpay.orm.model.TipoVersamentoDominioModel;

public class TipoVersamentoDominioFilter extends AbstractFilter {
	
	private String codDominio = null;
	private Long idDominio;
	private String codTipoVersamento = null;
	private String descrizione = null;
	private List<Long> listaIdTipiVersamento = null;
	private List<Long> listaIdDomini = null;
	private Boolean formBackoffice;
	private Boolean formPortalePagamento;
	private Boolean trasformazione;
	private boolean searchModeEquals = false; 
	
	private static TipoVersamentoDominioModel model = TipoVersamentoDominio.model();
	private TipoVersamentoDominioFieldConverter converter = null;
	
	public enum SortFields { }
	
	public TipoVersamentoDominioFilter(IExpressionConstructor expressionConstructor) throws ServiceException {
		this(expressionConstructor,false);
	}
	
	public TipoVersamentoDominioFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) throws ServiceException {
		super(expressionConstructor, simpleSearch);
		
		this.converter = new TipoVersamentoDominioFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
		this.eseguiCountConLimit = false;
		this.listaFieldSimpleSearch.add(model.TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO);
		this.listaFieldSimpleSearch.add(model.TIPO_VERSAMENTO.DESCRIZIONE);
		this.fieldAbilitato = model.ABILITATO;
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.idDominio != null){
				newExpression.equals(new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(model)), this.idDominio);
				addAnd = true;
			}
			
			if(this.getCodDominio() != null && StringUtils.isNotEmpty(this.getCodDominio())){
				if(addAnd)
					newExpression.and();
				newExpression.equals(model.ID_DOMINIO.COD_DOMINIO, this.getCodDominio());
				addAnd = true;
			}
			
			if(this.codTipoVersamento != null && StringUtils.isNotEmpty(this.codTipoVersamento)){
				if(addAnd)
					newExpression.and();
				if(!this.searchModeEquals)
					newExpression.ilike(model.TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, this.codTipoVersamento,LikeMode.ANYWHERE);
				else 
					newExpression.equals(model.TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, this.codTipoVersamento);
				addAnd = true;
			}
			
			if(this.descrizione != null && StringUtils.isNotEmpty(this.descrizione)){
				if(addAnd)
					newExpression.and();
				newExpression.ilike(model.TIPO_VERSAMENTO.DESCRIZIONE, this.descrizione,LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.listaIdTipiVersamento != null && this.listaIdTipiVersamento.size() > 0){
				if(addAnd)
					newExpression.and();
				
				newExpression.in(new CustomField("id_tipo_versamento", Long.class, "id_tipo_versamento", converter.toTable(model)), this.listaIdTipiVersamento);
				
				addAnd = true;
			}
			
			if(this.listaIdDomini != null && this.listaIdDomini.size() > 0){
				if(addAnd)
					newExpression.and();
				
				newExpression.in(new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(model)), this.listaIdDomini);
				
				addAnd = true;
			}
			
			if(this.formBackoffice != null){
				if(addAnd)
					newExpression.and();
				
				if(this.formBackoffice) {
					IExpression formRidefinitoExpression = this.newExpression();
					formRidefinitoExpression.isNotNull(model.BO_FORM_DEFINIZIONE).and().isNotNull(model.BO_FORM_TIPO);
					
					IExpression formDefaultExpression = this.newExpression();
					formDefaultExpression.isNotNull(model.TIPO_VERSAMENTO.BO_FORM_DEFINIZIONE)
						.and().isNotNull(model.TIPO_VERSAMENTO.BO_FORM_TIPO)
						.and().isNull(model.BO_FORM_DEFINIZIONE)
						.and().isNull(model.BO_FORM_TIPO);
					
					newExpression.or(formRidefinitoExpression,formDefaultExpression);
				} else {
					newExpression.isNull(model.TIPO_VERSAMENTO.BO_FORM_DEFINIZIONE);
					newExpression.isNull(model.TIPO_VERSAMENTO.BO_FORM_TIPO);
					newExpression.isNull(model.BO_FORM_DEFINIZIONE);
					newExpression.isNull(model.BO_FORM_TIPO);
				}
				
				addAnd = true;
			}
			
			if(this.formPortalePagamento != null){
				if(addAnd)
					newExpression.and();
				
				if(this.formPortalePagamento) {
					IExpression formRidefinitoExpression = this.newExpression();
					formRidefinitoExpression.isNotNull(model.PAG_FORM_DEFINIZIONE).and().isNotNull(model.PAG_FORM_TIPO);
					
					IExpression formDefaultExpression = this.newExpression();
					formDefaultExpression.isNotNull(model.TIPO_VERSAMENTO.PAG_FORM_DEFINIZIONE)
						.and().isNotNull(model.TIPO_VERSAMENTO.PAG_FORM_TIPO)
						.and().isNull(model.PAG_FORM_DEFINIZIONE)
						.and().isNull(model.PAG_FORM_TIPO);
					
					newExpression.or(formRidefinitoExpression,formDefaultExpression);
				} else {
					newExpression.isNull(model.TIPO_VERSAMENTO.PAG_FORM_DEFINIZIONE);
					newExpression.isNull(model.TIPO_VERSAMENTO.PAG_FORM_TIPO);
					newExpression.isNull(model.PAG_FORM_DEFINIZIONE);
					newExpression.isNull(model.PAG_FORM_TIPO);
				}
				
				addAnd = true;
			}
			
			if(this.trasformazione != null){
				if(addAnd)
					newExpression.and();
				
				if(this.trasformazione) {
					IExpression trasformazioneRidefinitoExpression = this.newExpression();
					trasformazioneRidefinitoExpression.isNotNull(model.TRAC_CSV_HEADER_RISPOSTA)
					.and().isNotNull(model.TRAC_CSV_TEMPLATE_RICHIESTA)
					.and().isNotNull(model.TRAC_CSV_TEMPLATE_RISPOSTA)
					.and().isNotNull(model.TRAC_CSV_TIPO);
					
					IExpression trasformazioneDefaultExpression = this.newExpression();
					trasformazioneDefaultExpression.isNotNull(model.TIPO_VERSAMENTO.TRAC_CSV_HEADER_RISPOSTA)
						.and().isNotNull(model.TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RICHIESTA)
						.and().isNotNull(model.TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RISPOSTA)
						.and().isNotNull(model.TIPO_VERSAMENTO.TRAC_CSV_TIPO)
						.and().isNull(model.TRAC_CSV_HEADER_RISPOSTA)
						.and().isNull(model.TRAC_CSV_TEMPLATE_RICHIESTA)
						.and().isNull(model.TRAC_CSV_TEMPLATE_RISPOSTA)
						.and().isNull(model.TRAC_CSV_TIPO);
					
					newExpression.or(trasformazioneRidefinitoExpression,trasformazioneDefaultExpression);
				} else {
					newExpression.isNull(model.TIPO_VERSAMENTO.TRAC_CSV_HEADER_RISPOSTA);
					newExpression.isNull(model.TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RICHIESTA);
					newExpression.isNull(model.TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RISPOSTA);
					newExpression.isNull(model.TIPO_VERSAMENTO.TRAC_CSV_TIPO);
					newExpression.isNull(model.TRAC_CSV_HEADER_RISPOSTA);
					newExpression.isNull(model.TRAC_CSV_TEMPLATE_RICHIESTA);
					newExpression.isNull(model.TRAC_CSV_TEMPLATE_RISPOSTA);
					newExpression.isNull(model.TRAC_CSV_TIPO);
				}
				
				addAnd = true;
			}
			
			if(this.searchAbilitato != null) {
				if(addAnd)
					newExpression.and();
				
				IExpression abilitatoRidefinitoExpression = this.newExpression();
				abilitatoRidefinitoExpression.isNotNull(model.ABILITATO).and().equals(model.ABILITATO,this.searchAbilitato);
				
				IExpression abilitatoDefaultExpression = this.newExpression();
				abilitatoDefaultExpression.equals(model.TIPO_VERSAMENTO.ABILITATO,this.searchAbilitato)
					.and().isNull(model.ABILITATO);
				
				newExpression.or(abilitatoRidefinitoExpression,abilitatoDefaultExpression);
				
				addAnd = true;
			}
			
//			addAnd = this.setFiltroAbilitato(newExpression, addAnd);
			
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
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			
			boolean addTabellaDomini = false;
			boolean addTabellaTipiVersamento = false;
			
			if(this.idDominio != null){
				sqlQueryObject.addWhereCondition(true,converter.toTable(model.ABILITATO, true) + ".id_dominio" + " = ? ");
			}
			
			if(this.getCodDominio() != null && StringUtils.isNotEmpty(this.getCodDominio())){
				if(!addTabellaDomini) {
					sqlQueryObject.addFromTable(converter.toTable(model.ID_DOMINIO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.ABILITATO, true) + ".id_dominio="
							+converter.toTable(model.ID_DOMINIO, true)+".id");

					addTabellaDomini = true;
				}
				
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_DOMINIO.COD_DOMINIO, true) + " = ? ");
			}
			
			if(this.codTipoVersamento != null && StringUtils.isNotEmpty(this.codTipoVersamento)){
				if(!addTabellaTipiVersamento) {
					sqlQueryObject.addFromTable(converter.toTable(model.TIPO_VERSAMENTO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.ABILITATO, true) + ".id_tipo_versamento="
							+converter.toTable(model.TIPO_VERSAMENTO, true)+".id");

					addTabellaTipiVersamento = true;
				}
				
				if(!this.searchModeEquals)
					sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, true), this.codTipoVersamento, true, true);
				else 
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, true) + " = ? ");
			}
			
			if(this.descrizione != null && StringUtils.isNotEmpty(this.descrizione)){
				if(!addTabellaTipiVersamento) {
					sqlQueryObject.addFromTable(converter.toTable(model.TIPO_VERSAMENTO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.ABILITATO, true) + ".id_tipo_versamento="
							+converter.toTable(model.TIPO_VERSAMENTO, true)+".id");

					addTabellaTipiVersamento = true;
				}
				
				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.TIPO_VERSAMENTO.DESCRIZIONE, true), this.descrizione, true, true);
			}
			
			if(this.listaIdTipiVersamento != null && this.listaIdTipiVersamento.size() > 0){
				this.listaIdTipiVersamento.removeAll(Collections.singleton(null));
				
				String [] ids = this.listaIdTipiVersamento.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.listaIdTipiVersamento.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.ABILITATO, true) + ".id_tipo_versamento", false, ids );
			}
			
			if(this.listaIdDomini != null && this.listaIdDomini.size() > 0){
				this.listaIdDomini.removeAll(Collections.singleton(null));
				
				String [] ids = this.listaIdDomini.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.listaIdDomini.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.ABILITATO, true) + ".id_dominio", false, ids );
			}
			
			if(this.formBackoffice != null){
				if(!addTabellaTipiVersamento) {
					sqlQueryObject.addFromTable(converter.toTable(model.TIPO_VERSAMENTO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.ABILITATO, true) + ".id_tipo_versamento="
							+converter.toTable(model.TIPO_VERSAMENTO, true)+".id");

					addTabellaTipiVersamento = true;
				}
				
				if(this.formBackoffice) {
					
					StringBuilder sbFormRidefinito = new StringBuilder();
					sbFormRidefinito.append(" ( ");
					sbFormRidefinito.append(converter.toColumn(model.BO_FORM_DEFINIZIONE, true)).append(" is not null "); 
					sbFormRidefinito.append(" and ");
					sbFormRidefinito.append(converter.toColumn(model.BO_FORM_TIPO, true)).append(" is not null ");
					sbFormRidefinito.append(" ) ");
					
					StringBuilder sbFormDefault = new StringBuilder();
					sbFormDefault.append(" ( ");
					sbFormDefault.append(converter.toColumn(model.TIPO_VERSAMENTO.BO_FORM_DEFINIZIONE, true)).append(" is not null "); 
					sbFormDefault.append(" and ");
					sbFormDefault.append(converter.toColumn(model.TIPO_VERSAMENTO.BO_FORM_TIPO, true)).append(" is not null ");
					sbFormDefault.append(" and ");
					sbFormDefault.append(converter.toColumn(model.BO_FORM_DEFINIZIONE, true)).append(" is null "); 
					sbFormDefault.append(" and ");
					sbFormDefault.append(converter.toColumn(model.BO_FORM_TIPO, true)).append(" is null ");
					sbFormDefault.append(" ) ");
					
					sqlQueryObject.addWhereCondition(false, sbFormRidefinito.toString(), sbFormDefault.toString());
				} else {
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TIPO_VERSAMENTO.BO_FORM_DEFINIZIONE, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TIPO_VERSAMENTO.BO_FORM_TIPO, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.BO_FORM_DEFINIZIONE, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.BO_FORM_TIPO, true));
				}
			}
			
			if(this.formPortalePagamento != null){
				if(!addTabellaTipiVersamento) {
					sqlQueryObject.addFromTable(converter.toTable(model.TIPO_VERSAMENTO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.ABILITATO, true) + ".id_tipo_versamento="
							+converter.toTable(model.TIPO_VERSAMENTO, true)+".id");

					addTabellaTipiVersamento = true;
				}
				
				if(this.formPortalePagamento) {
					StringBuilder sbFormRidefinito = new StringBuilder();
					sbFormRidefinito.append(" ( ");
					sbFormRidefinito.append(converter.toColumn(model.PAG_FORM_DEFINIZIONE, true)).append(" is not null "); 
					sbFormRidefinito.append(" and ");
					sbFormRidefinito.append(converter.toColumn(model.PAG_FORM_TIPO, true)).append(" is not null ");
					sbFormRidefinito.append(" ) ");
					
					StringBuilder sbFormDefault = new StringBuilder();
					sbFormDefault.append(" ( ");
					sbFormDefault.append(converter.toColumn(model.TIPO_VERSAMENTO.PAG_FORM_DEFINIZIONE, true)).append(" is not null "); 
					sbFormDefault.append(" and ");
					sbFormDefault.append(converter.toColumn(model.TIPO_VERSAMENTO.PAG_FORM_TIPO, true)).append(" is not null ");
					sbFormDefault.append(" and ");
					sbFormDefault.append(converter.toColumn(model.PAG_FORM_DEFINIZIONE, true)).append(" is null "); 
					sbFormDefault.append(" and ");
					sbFormDefault.append(converter.toColumn(model.PAG_FORM_TIPO, true)).append(" is null ");
					sbFormDefault.append(" ) ");
					
					sqlQueryObject.addWhereCondition(false, sbFormRidefinito.toString(), sbFormDefault.toString());
				} else {
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TIPO_VERSAMENTO.PAG_FORM_DEFINIZIONE, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TIPO_VERSAMENTO.PAG_FORM_TIPO, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.PAG_FORM_DEFINIZIONE, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.PAG_FORM_TIPO, true));
				}
			}
			
			if(this.trasformazione != null){
				if(!addTabellaTipiVersamento) {
					sqlQueryObject.addFromTable(converter.toTable(model.TIPO_VERSAMENTO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.ABILITATO, true) + ".id_tipo_versamento="
							+converter.toTable(model.TIPO_VERSAMENTO, true)+".id");

					addTabellaTipiVersamento = true;
				}
				
				if(this.trasformazione) {
					StringBuilder sbTrasformazioneRidefinito = new StringBuilder();
					sbTrasformazioneRidefinito.append(" ( ");
					sbTrasformazioneRidefinito.append(converter.toColumn(model.TRAC_CSV_HEADER_RISPOSTA, true)).append(" is not null "); 
					sbTrasformazioneRidefinito.append(" and ");
					sbTrasformazioneRidefinito.append(converter.toColumn(model.TRAC_CSV_TEMPLATE_RICHIESTA, true)).append(" is not null ");
					sbTrasformazioneRidefinito.append(" and ");
					sbTrasformazioneRidefinito.append(converter.toColumn(model.TRAC_CSV_TEMPLATE_RISPOSTA, true)).append(" is not null ");
					sbTrasformazioneRidefinito.append(" and ");
					sbTrasformazioneRidefinito.append(converter.toColumn(model.TRAC_CSV_TIPO, true)).append(" is not null ");
					sbTrasformazioneRidefinito.append(" ) ");
					
					StringBuilder sbTrasformazioneDefault = new StringBuilder();
					sbTrasformazioneDefault.append(" ( ");
					sbTrasformazioneDefault.append(converter.toColumn(model.TIPO_VERSAMENTO.TRAC_CSV_HEADER_RISPOSTA, true)).append(" is not null "); 
					sbTrasformazioneDefault.append(" and ");
					sbTrasformazioneDefault.append(converter.toColumn(model.TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RICHIESTA, true)).append(" is not null ");
					sbTrasformazioneDefault.append(" and ");
					sbTrasformazioneDefault.append(converter.toColumn(model.TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RISPOSTA, true)).append(" is not null ");
					sbTrasformazioneDefault.append(" and ");
					sbTrasformazioneDefault.append(converter.toColumn(model.TIPO_VERSAMENTO.TRAC_CSV_TIPO, true)).append(" is not null ");
					
					sbTrasformazioneDefault.append(" and ");
					sbTrasformazioneDefault.append(converter.toColumn(model.TRAC_CSV_HEADER_RISPOSTA, true)).append(" is null "); 
					sbTrasformazioneDefault.append(" and ");
					sbTrasformazioneDefault.append(converter.toColumn(model.TRAC_CSV_TEMPLATE_RICHIESTA, true)).append(" is null ");
					sbTrasformazioneDefault.append(" and ");
					sbTrasformazioneDefault.append(converter.toColumn(model.TRAC_CSV_TEMPLATE_RISPOSTA, true)).append(" is null ");
					sbTrasformazioneDefault.append(" and ");
					sbTrasformazioneDefault.append(converter.toColumn(model.TRAC_CSV_TIPO, true)).append(" is null ");
					sbTrasformazioneDefault.append(" ) ");
					
					sqlQueryObject.addWhereCondition(false, sbTrasformazioneRidefinito.toString(), sbTrasformazioneDefault.toString());
				} else {
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TIPO_VERSAMENTO.TRAC_CSV_HEADER_RISPOSTA, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RICHIESTA, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RISPOSTA, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TIPO_VERSAMENTO.TRAC_CSV_TIPO, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TRAC_CSV_HEADER_RISPOSTA, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TRAC_CSV_TEMPLATE_RICHIESTA, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TRAC_CSV_TEMPLATE_RISPOSTA, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TRAC_CSV_TIPO, true));
				}
			}
			
			if(this.searchAbilitato != null) {
				if(!addTabellaTipiVersamento) {
					sqlQueryObject.addFromTable(converter.toTable(model.TIPO_VERSAMENTO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.ABILITATO, true) + ".id_tipo_versamento="
							+converter.toTable(model.TIPO_VERSAMENTO, true)+".id");

					addTabellaTipiVersamento = true;
				}
				
				StringBuilder sbAbilitatoRidefinito = new StringBuilder();
				sbAbilitatoRidefinito.append(" ( ");
				sbAbilitatoRidefinito.append(converter.toColumn(model.ABILITATO, true)).append(" is not null "); 
				sbAbilitatoRidefinito.append(" and ");
				sbAbilitatoRidefinito.append(converter.toColumn(model.ABILITATO, true)).append(" =? ");
				sbAbilitatoRidefinito.append(" ) ");
				
				StringBuilder sbAbilitatoDefault = new StringBuilder();
				sbAbilitatoDefault.append(" ( ");
				sbAbilitatoDefault.append(converter.toColumn(model.TIPO_VERSAMENTO.ABILITATO, true)).append(" =? "); 
				sbAbilitatoDefault.append(" and ");
				sbAbilitatoDefault.append(converter.toColumn(model.ABILITATO, true)).append(" is null ");
				sbAbilitatoDefault.append(" ) ");
				
				sqlQueryObject.addWhereCondition(false, sbAbilitatoRidefinito.toString(), sbAbilitatoDefault.toString());
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
		
		if(this.idDominio != null){
			lst.add(this.idDominio);
		}
		
		if(this.getCodDominio() != null && StringUtils.isNotEmpty(this.getCodDominio())){
			lst.add(this.getCodDominio());
		}
		
		if(this.codTipoVersamento != null && StringUtils.isNotEmpty(this.codTipoVersamento)){
			if(this.searchModeEquals)
				lst.add(this.codTipoVersamento);
		}
		
		if(this.descrizione != null && StringUtils.isNotEmpty(this.descrizione)){
			// do nothing
		}
		
		if(this.listaIdTipiVersamento != null && this.listaIdTipiVersamento.size() > 0){
			// do nothing
		}
		
		if(this.listaIdDomini != null && this.listaIdDomini.size() > 0){
			// do nothing
		}
		
		if(this.formBackoffice != null){
			// do nothing
		}
		
		if(this.formPortalePagamento != null){
			// do nothing
		}
		
		if(this.trasformazione != null){
			// do nothing
		}
		
		if(this.searchAbilitato != null) {
			lst.add(this.searchAbilitato);
			lst.add(this.searchAbilitato);
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public List<Long> getListaIdTipiVersamento() {
		return listaIdTipiVersamento;
	}

	public void setListaIdTipiVersamento(List<Long> listaIdTipiVersamento) {
		this.listaIdTipiVersamento = listaIdTipiVersamento;
	}

	public String getCodTipoVersamento() {
		return codTipoVersamento;
	}

	public void setCodTipoVersamento(String codTipoVersamento) {
		this.codTipoVersamento = codTipoVersamento;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public Long getIdDominio() {
		return this.idDominio;
	}

	public void setIdDominio(Long idDominio) {
		this.idDominio = idDominio;
	}

	public Boolean getFormBackoffice() {
		return formBackoffice;
	}

	public void setFormBackoffice(Boolean formBackoffice) {
		this.formBackoffice = formBackoffice;
	}

	public Boolean getFormPortalePagamento() {
		return formPortalePagamento;
	}

	public void setFormPortalePagamento(Boolean formPortalePagamento) {
		this.formPortalePagamento = formPortalePagamento;
	}

	public Boolean getTrasformazione() {
		return trasformazione;
	}

	public void setTrasformazione(Boolean trasformazione) {
		this.trasformazione = trasformazione;
	}
	
	public boolean isSearchModeEquals() {
		return this.searchModeEquals;
	}

	public void setSearchModeEquals(boolean searchModeEquals) {
		this.searchModeEquals = searchModeEquals;
	}

	public List<Long> getListaIdDomini() {
		return listaIdDomini;
	}

	public void setListaIdDomini(List<Long> listaIdDomini) {
		this.listaIdDomini = listaIdDomini;
	}
	
	
}
