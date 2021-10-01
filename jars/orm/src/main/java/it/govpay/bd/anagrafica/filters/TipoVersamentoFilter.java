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
import it.govpay.orm.TipoVersamento;
import it.govpay.orm.dao.jdbc.converter.TipoVersamentoFieldConverter;
import it.govpay.orm.model.TipoVersamentoModel;

public class TipoVersamentoFilter extends AbstractFilter {
	
	private String codTipoVersamento = null;
	private boolean searchModeEquals = false; 
	private String descrizione = null;
	private List<Long> listaIdTipiVersamento = null;
	private List<Long> listaIdTipiVersamentoDaEscludere = null;
	private List<Long> listaIdTipiVersamentoDaIncludere = null;
	private Boolean formBackoffice;
	private Boolean formPortalePagamento;
	private Boolean trasformazione;
	
	private static TipoVersamentoModel model = TipoVersamento.model();
	private TipoVersamentoFieldConverter converter = null;
	
	public enum SortFields { }
	
	public TipoVersamentoFilter(IExpressionConstructor expressionConstructor) throws ServiceException {
		this(expressionConstructor,false);
	}
	
	public TipoVersamentoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) throws ServiceException {
		super(expressionConstructor, simpleSearch);
		
		this.converter = new TipoVersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
		this.eseguiCountConLimit = false;
		this.listaFieldSimpleSearch.add(TipoVersamento.model().COD_TIPO_VERSAMENTO);
		this.listaFieldSimpleSearch.add(TipoVersamento.model().DESCRIZIONE);
		this.fieldAbilitato = model.ABILITATO;
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			if(this.codTipoVersamento != null && StringUtils.isNotEmpty(this.codTipoVersamento)){
				if(addAnd)
					newExpression.and();
				if(!this.searchModeEquals)
					newExpression.ilike(model.COD_TIPO_VERSAMENTO, this.codTipoVersamento,LikeMode.ANYWHERE);
				else 
					newExpression.equals(model.COD_TIPO_VERSAMENTO, this.codTipoVersamento);
				addAnd = true;
			}
			
			if(this.descrizione != null && StringUtils.isNotEmpty(this.descrizione)){
				if(addAnd)
					newExpression.and();
				
				IExpression orExpr = this.newExpression();
				
				orExpr.ilike(model.DESCRIZIONE, this.descrizione,LikeMode.ANYWHERE);
				orExpr.or().ilike(model.COD_TIPO_VERSAMENTO, this.descrizione,LikeMode.ANYWHERE);
					
				newExpression.and(orExpr);	
				addAnd = true;
			}
			
			if(this.listaIdTipiVersamento != null && this.listaIdTipiVersamento.size() > 0){
				if(addAnd)
					newExpression.and();
				
				CustomField	cf = new CustomField("id", Long.class, "id", converter.toTable(model));
				newExpression.in(cf, this.listaIdTipiVersamento);
				
				addAnd = true;
			}
			
			if(this.listaIdTipiVersamentoDaEscludere != null && this.listaIdTipiVersamentoDaEscludere.size() > 0){
				if(addAnd)
					newExpression.and();
				
				IExpression notExpression = this.newExpression();
				CustomField	cf = new CustomField("id", Long.class, "id", converter.toTable(model));
				notExpression.not().in(cf, this.listaIdTipiVersamentoDaEscludere);
				newExpression.and(notExpression);
				addAnd = true;
			}
			
			if(this.listaIdTipiVersamentoDaIncludere != null && this.listaIdTipiVersamentoDaIncludere.size() > 0){
				if(addAnd)
					newExpression.and();
				
				IExpression notExpression = this.newExpression();
				CustomField	cf = new CustomField("id", Long.class, "id", converter.toTable(model));
				notExpression.in(cf, this.listaIdTipiVersamentoDaIncludere);
				newExpression.and(notExpression);
				addAnd = true;
			}
			
			if(this.formBackoffice != null){
				if(addAnd)
					newExpression.and();
				
				if(this.formBackoffice) {
					newExpression.isNotNull(model.BO_FORM_DEFINIZIONE);
					newExpression.isNotNull(model.BO_FORM_TIPO);
				} else {
					newExpression.isNull(model.BO_FORM_DEFINIZIONE);
					newExpression.isNull(model.BO_FORM_TIPO);
				}
				
				addAnd = true;
			}
			
			if(this.formPortalePagamento != null){
				if(addAnd)
					newExpression.and();
				
				if(this.formPortalePagamento) {
					newExpression.isNotNull(model.PAG_FORM_DEFINIZIONE);
					newExpression.isNotNull(model.PAG_FORM_TIPO);
				} else {
					newExpression.isNull(model.PAG_FORM_DEFINIZIONE);
					newExpression.isNull(model.PAG_FORM_TIPO);
				}
				
				addAnd = true;
			}
			
			if(this.trasformazione != null){
				if(addAnd)
					newExpression.and();
				
				if(this.trasformazione) {
					newExpression.isNotNull(model.TRAC_CSV_HEADER_RISPOSTA);
					newExpression.isNotNull(model.TRAC_CSV_TEMPLATE_RICHIESTA);
					newExpression.isNotNull(model.TRAC_CSV_TEMPLATE_RISPOSTA);
					newExpression.isNotNull(model.TRAC_CSV_TIPO);
				} else {
					newExpression.isNull(model.TRAC_CSV_HEADER_RISPOSTA);
					newExpression.isNull(model.TRAC_CSV_TEMPLATE_RICHIESTA);
					newExpression.isNull(model.TRAC_CSV_TEMPLATE_RISPOSTA);
					newExpression.isNull(model.TRAC_CSV_TIPO);
				}
				
				addAnd = true;
			}

			addAnd = this.setFiltroAbilitato(newExpression, addAnd);
			
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
			if(this.codTipoVersamento != null && StringUtils.isNotEmpty(this.codTipoVersamento)){
				if(!this.searchModeEquals)
					sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.COD_TIPO_VERSAMENTO, true), this.codTipoVersamento, true, true);
				else 
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_TIPO_VERSAMENTO, true) + " = ? ");
			}
			
			if(this.descrizione != null && StringUtils.isNotEmpty(this.descrizione)){
				sqlQueryObject.addWhereCondition(false, 
						sqlQueryObject.getWhereLikeCondition(converter.toColumn(model.DESCRIZIONE, true), this.descrizione, true, true),
						sqlQueryObject.getWhereLikeCondition(converter.toColumn(model.COD_TIPO_VERSAMENTO, true), this.descrizione, true, true));
			}
			
			if(this.listaIdTipiVersamento != null && this.listaIdTipiVersamento.size() > 0){
				this.listaIdTipiVersamento.removeAll(Collections.singleton(null));
				
				String [] ids = this.listaIdTipiVersamento.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.listaIdTipiVersamento.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.COD_TIPO_VERSAMENTO, true) + ".id", false, ids );
			}
			
			if(this.listaIdTipiVersamentoDaEscludere != null && this.listaIdTipiVersamentoDaEscludere.size() > 0){
				this.listaIdTipiVersamentoDaEscludere.removeAll(Collections.singleton(null));
				
				String [] ids = this.listaIdTipiVersamentoDaEscludere.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.listaIdTipiVersamentoDaEscludere.size()]);
				
				ISQLQueryObject sqlQueryObjectNot = sqlQueryObject.newSQLQueryObject();
				sqlQueryObjectNot.addFromTable(converter.toTable(model.COD_TIPO_VERSAMENTO));
				sqlQueryObjectNot.addSelectField(converter.toTable(model.COD_TIPO_VERSAMENTO), "id");
				
				sqlQueryObjectNot.addWhereINCondition(converter.toTable(model.COD_TIPO_VERSAMENTO, true) + ".id", false, ids );
				
				sqlQueryObject.addWhereINSelectSQLCondition(true, converter.toTable(model.COD_TIPO_VERSAMENTO, true) + ".id", sqlQueryObjectNot);	
			}
			
			if(this.listaIdTipiVersamentoDaIncludere != null && this.listaIdTipiVersamentoDaIncludere.size() > 0){
				this.listaIdTipiVersamentoDaIncludere.removeAll(Collections.singleton(null));
				
				String [] ids = this.listaIdTipiVersamentoDaIncludere.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.listaIdTipiVersamentoDaIncludere.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.COD_TIPO_VERSAMENTO, true) + ".id", false, ids );
			}
			
			if(this.formBackoffice != null){
				if(this.formBackoffice) {
					sqlQueryObject.addWhereIsNotNullCondition(converter.toColumn(model.BO_FORM_DEFINIZIONE, true));
					sqlQueryObject.addWhereIsNotNullCondition(converter.toColumn(model.BO_FORM_TIPO, true));
				} else {
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.BO_FORM_DEFINIZIONE, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.BO_FORM_TIPO, true));
				}
			}
			
			if(this.formPortalePagamento != null){
				if(this.formPortalePagamento) {
					sqlQueryObject.addWhereIsNotNullCondition(converter.toColumn(model.PAG_FORM_DEFINIZIONE, true));
					sqlQueryObject.addWhereIsNotNullCondition(converter.toColumn(model.PAG_FORM_TIPO, true));
				} else {
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.PAG_FORM_DEFINIZIONE, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.PAG_FORM_TIPO, true));
				}
			}
			
			if(this.trasformazione != null){
				if(this.trasformazione) {
					sqlQueryObject.addWhereIsNotNullCondition(converter.toColumn(model.TRAC_CSV_HEADER_RISPOSTA, true));
					sqlQueryObject.addWhereIsNotNullCondition(converter.toColumn(model.TRAC_CSV_TEMPLATE_RICHIESTA, true));
					sqlQueryObject.addWhereIsNotNullCondition(converter.toColumn(model.TRAC_CSV_TEMPLATE_RISPOSTA, true));
					sqlQueryObject.addWhereIsNotNullCondition(converter.toColumn(model.TRAC_CSV_TIPO, true));
				} else {
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TRAC_CSV_HEADER_RISPOSTA, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TRAC_CSV_TEMPLATE_RICHIESTA, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TRAC_CSV_TEMPLATE_RISPOSTA, true));
					sqlQueryObject.addWhereIsNullCondition(converter.toColumn(model.TRAC_CSV_TIPO, true));
				}
			}
			
			// filtro abilitato
			sqlQueryObject = this.setFiltroAbilitato(sqlQueryObject, converter);
			
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
		
		if(this.listaIdTipiVersamentoDaEscludere != null && this.listaIdTipiVersamentoDaEscludere.size() > 0){
			// do nothing
		}
		
		if(this.listaIdTipiVersamentoDaIncludere != null && this.listaIdTipiVersamentoDaIncludere.size() > 0){
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
		
		// filtro abilitato
		try {
			lst = this.setValoreFiltroAbilitato(lst, converter);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public List<Long> getListaIdTipiVersamento() {
		return listaIdTipiVersamento;
	}

	public void setListaIdTipiVersamento(List<Long> listaIdTipiVersamento) {
		this.listaIdTipiVersamento = listaIdTipiVersamento;
	}

	public List<Long> getListaIdTipiVersamentoDaEscludere() {
		return listaIdTipiVersamentoDaEscludere;
	}

	public void setListaIdTipiVersamentoDaEscludere(List<Long> listaIdTipiVersamentoDaEscludere) {
		this.listaIdTipiVersamentoDaEscludere = listaIdTipiVersamentoDaEscludere;
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

	public List<Long> getListaIdTipiVersamentoDaIncludere() {
		return listaIdTipiVersamentoDaIncludere;
	}

	public void setListaIdTipiVersamentoDaIncludere(List<Long> listaIdTipiVersamentoDaIncludere) {
		this.listaIdTipiVersamentoDaIncludere = listaIdTipiVersamentoDaIncludere;
	}
	
}
