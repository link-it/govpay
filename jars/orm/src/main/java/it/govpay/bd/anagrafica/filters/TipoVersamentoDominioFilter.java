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
import org.openspcoop2.utils.sql.ISQLQueryObject;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.TipoVersamentoDominio;
import it.govpay.orm.dao.jdbc.converter.TipoVersamentoDominioFieldConverter;

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
	
	public enum SortFields { }
	
	public TipoVersamentoDominioFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public TipoVersamentoDominioFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		
		try{
			//TipoVersamentoDominioFieldConverter converter = new TipoVersamentoDominioFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
			//this.cf = new CustomField("id", Long.class, "id", converter.toTable(it.govpay.orm.TipoVersamentoDominio.model()));
			this.listaFieldSimpleSearch.add(TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO);
			this.listaFieldSimpleSearch.add(TipoVersamentoDominio.model().TIPO_VERSAMENTO.DESCRIZIONE);
			this.fieldAbilitato = it.govpay.orm.TipoVersamentoDominio.model().ABILITATO;
		} catch(Exception e){
			
		}
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.idDominio != null){
				TipoVersamentoDominioFieldConverter converter = new TipoVersamentoDominioFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				newExpression.equals(new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(it.govpay.orm.TipoVersamentoDominio.model())), this.idDominio);
				addAnd = true;
			}
			
			if(this.getCodDominio() != null && StringUtils.isNotEmpty(this.getCodDominio())){
				if(addAnd)
					newExpression.and();
				newExpression.equals(it.govpay.orm.TipoVersamentoDominio.model().ID_DOMINIO.COD_DOMINIO, this.getCodDominio());
				addAnd = true;
			}
			
			if(this.codTipoVersamento != null && StringUtils.isNotEmpty(this.codTipoVersamento)){
				if(addAnd)
					newExpression.and();
				if(!this.searchModeEquals)
					newExpression.ilike(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, this.codTipoVersamento,LikeMode.ANYWHERE);
				else 
					newExpression.equals(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.COD_TIPO_VERSAMENTO, this.codTipoVersamento);
				addAnd = true;
			}
			
			if(this.descrizione != null && StringUtils.isNotEmpty(this.descrizione)){
				if(addAnd)
					newExpression.and();
				newExpression.ilike(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.DESCRIZIONE, this.descrizione,LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.listaIdTipiVersamento != null && this.listaIdTipiVersamento.size() > 0){
				if(addAnd)
					newExpression.and();
				
				TipoVersamentoDominioFieldConverter converter = new TipoVersamentoDominioFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				newExpression.in(new CustomField("id_tipo_versamento", Long.class, "id_tipo_versamento", converter.toTable(it.govpay.orm.TipoVersamentoDominio.model())), this.listaIdTipiVersamento);
				
				addAnd = true;
			}
			
			if(this.listaIdDomini != null && this.listaIdDomini.size() > 0){
				if(addAnd)
					newExpression.and();
				
				TipoVersamentoDominioFieldConverter converter = new TipoVersamentoDominioFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				newExpression.in(new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(it.govpay.orm.TipoVersamentoDominio.model())), this.listaIdDomini);
				
				addAnd = true;
			}
			
			if(this.formBackoffice != null){
				if(addAnd)
					newExpression.and();
				
				if(this.formBackoffice) {
					IExpression formRidefinitoExpression = this.newExpression();
					formRidefinitoExpression.isNotNull(it.govpay.orm.TipoVersamentoDominio.model().BO_FORM_DEFINIZIONE).and().isNotNull(it.govpay.orm.TipoVersamentoDominio.model().BO_FORM_TIPO);
					
					IExpression formDefaultExpression = this.newExpression();
					formDefaultExpression.isNotNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_FORM_DEFINIZIONE)
						.and().isNotNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_FORM_TIPO)
						.and().isNull(it.govpay.orm.TipoVersamentoDominio.model().BO_FORM_DEFINIZIONE)
						.and().isNull(it.govpay.orm.TipoVersamentoDominio.model().BO_FORM_TIPO);
					
					newExpression.or(formRidefinitoExpression,formDefaultExpression);
				} else {
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_FORM_DEFINIZIONE);
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.BO_FORM_TIPO);
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().BO_FORM_DEFINIZIONE);
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().BO_FORM_TIPO);
				}
				
				addAnd = true;
			}
			
			if(this.formPortalePagamento != null){
				if(addAnd)
					newExpression.and();
				
				if(this.formPortalePagamento) {
					IExpression formRidefinitoExpression = this.newExpression();
					formRidefinitoExpression.isNotNull(it.govpay.orm.TipoVersamentoDominio.model().PAG_FORM_DEFINIZIONE).and().isNotNull(it.govpay.orm.TipoVersamentoDominio.model().PAG_FORM_TIPO);
					
					IExpression formDefaultExpression = this.newExpression();
					formDefaultExpression.isNotNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_DEFINIZIONE)
						.and().isNotNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_TIPO)
						.and().isNull(it.govpay.orm.TipoVersamentoDominio.model().PAG_FORM_DEFINIZIONE)
						.and().isNull(it.govpay.orm.TipoVersamentoDominio.model().PAG_FORM_TIPO);
					
					newExpression.or(formRidefinitoExpression,formDefaultExpression);
				} else {
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_DEFINIZIONE);
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.PAG_FORM_TIPO);
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().PAG_FORM_DEFINIZIONE);
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().PAG_FORM_TIPO);
				}
				
				addAnd = true;
			}
			
			if(this.trasformazione != null){
				if(addAnd)
					newExpression.and();
				
				if(this.trasformazione) {
					IExpression trasformazioneRidefinitoExpression = this.newExpression();
					trasformazioneRidefinitoExpression.isNotNull(it.govpay.orm.TipoVersamentoDominio.model().TRAC_CSV_HEADER_RISPOSTA)
					.and().isNotNull(it.govpay.orm.TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RICHIESTA)
					.and().isNotNull(it.govpay.orm.TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RISPOSTA)
					.and().isNotNull(it.govpay.orm.TipoVersamentoDominio.model().TRAC_CSV_TIPO);
					
					IExpression trasformazioneDefaultExpression = this.newExpression();
					trasformazioneDefaultExpression.isNotNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_HEADER_RISPOSTA)
						.and().isNotNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RICHIESTA)
						.and().isNotNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RISPOSTA)
						.and().isNotNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TIPO)
						.and().isNull(it.govpay.orm.TipoVersamentoDominio.model().TRAC_CSV_HEADER_RISPOSTA)
						.and().isNull(it.govpay.orm.TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RICHIESTA)
						.and().isNull(it.govpay.orm.TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RISPOSTA)
						.and().isNull(it.govpay.orm.TipoVersamentoDominio.model().TRAC_CSV_TIPO);
					
					newExpression.or(trasformazioneRidefinitoExpression,trasformazioneDefaultExpression);
				} else {
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_HEADER_RISPOSTA);
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RICHIESTA);
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TEMPLATE_RISPOSTA);
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.TRAC_CSV_TIPO);
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().TRAC_CSV_HEADER_RISPOSTA);
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RICHIESTA);
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RISPOSTA);
					newExpression.isNull(it.govpay.orm.TipoVersamentoDominio.model().TRAC_CSV_TIPO);
				}
				
				addAnd = true;
			}
			
			if(this.searchAbilitato != null) {
				if(addAnd)
					newExpression.and();
				
				IExpression abilitatoRidefinitoExpression = this.newExpression();
				abilitatoRidefinitoExpression.isNotNull(it.govpay.orm.TipoVersamentoDominio.model().ABILITATO).and().equals(it.govpay.orm.TipoVersamentoDominio.model().ABILITATO,this.searchAbilitato);
				
				IExpression abilitatoDefaultExpression = this.newExpression();
				abilitatoDefaultExpression.equals(it.govpay.orm.TipoVersamentoDominio.model().TIPO_VERSAMENTO.ABILITATO,this.searchAbilitato)
					.and().isNull(it.govpay.orm.TipoVersamentoDominio.model().ABILITATO);
				
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
		return null;
	}

	@Override
	public Object[] getParameters(ISQLQueryObject sqlQueryObject) throws ServiceException {
		return null;
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
