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
import it.govpay.orm.Tributo;
import it.govpay.orm.dao.jdbc.converter.TributoFieldConverter;
import it.govpay.orm.model.TributoModel;

public class TributoFilter extends AbstractFilter {

	private String codDominio = null;
	private List<Long> listaIdTributi = null;
	//	private CustomField cf;
	private Long idDominio;
	private String codTributo = null;
	private String descrizione = null; //TODO
	private Long idIbanAccredito = null;
	private Long idIbanAppoggio = null;
	private boolean searchModeEquals = false; 

	private static TributoModel model = Tributo.model();
	private TributoFieldConverter converter = null;

	public enum SortFields { }

	public TributoFilter(IExpressionConstructor expressionConstructor) throws ServiceException {
		this(expressionConstructor,false);
	}

	public TributoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) throws ServiceException {
		super(expressionConstructor, simpleSearch);

		this.converter = new TributoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
		this.eseguiCountConLimit = false;
		this.fieldAbilitato = model.ABILITATO;
		this.listaFieldSimpleSearch.add(model.TIPO_TRIBUTO.COD_TRIBUTO);
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

			if(this.listaIdTributi != null && this.listaIdTributi.size() > 0){
				if(addAnd)
					newExpression.and();

				CustomField	cf = new CustomField("id", Long.class, "id", converter.toTable(model));
				newExpression.in(cf, this.listaIdTributi);

				addAnd = true;
			}

			if(this.codTributo != null && StringUtils.isNotEmpty(this.codTributo)){
				if(addAnd)
					newExpression.and();

				if(!this.searchModeEquals)
					newExpression.ilike(model.TIPO_TRIBUTO.COD_TRIBUTO, this.codTributo,LikeMode.ANYWHERE);
				else 
					newExpression.equals(model.TIPO_TRIBUTO.COD_TRIBUTO, this.codTributo);
				addAnd = true;
			}

			if(this.idIbanAccredito != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(new CustomField("id_iban_accredito", Long.class, "id_iban_accredito", converter.toTable(model)), this.idIbanAccredito);
				addAnd = true;
			}

			if(this.idIbanAppoggio!= null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(new CustomField("id_iban_appoggio", Long.class, "id_iban_appoggio", converter.toTable(model)), this.idIbanAppoggio);
				addAnd = true;
			}

			if(this.descrizione != null && StringUtils.isNotEmpty(this.descrizione)){
				if(addAnd)
					newExpression.and();
				newExpression.ilike(model.TIPO_TRIBUTO.DESCRIZIONE, this.descrizione,LikeMode.ANYWHERE);
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

	@Override
	public IExpression _toSimpleSearchExpression() throws ServiceException {
		try {
			IExpression newExpression = super._toSimpleSearchExpression();
			//	boolean addAnd = false;

			if(this.idDominio != null){
				TributoFieldConverter fieldConverter = new TributoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
				newExpression.equals(new CustomField("id_dominio", Long.class, "id_dominio", fieldConverter.toTable(it.govpay.orm.Tributo.model())), this.idDominio);
				//	addAnd = true;
			}

			return newExpression;
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
			boolean addTabellaTipiTributo = false;

			if(this.idDominio != null){
				sqlQueryObject.addWhereCondition(true,converter.toTable(model.ABILITATO, true) + ".id_dominio" + " = ? ");
			}

			if(StringUtils.isNotEmpty(this.getCodDominio())){
				if(!addTabellaDomini) {
					sqlQueryObject.addFromTable(converter.toTable(model.ID_DOMINIO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.ABILITATO, true) + ".id_dominio="
							+converter.toTable(model.ID_DOMINIO, true)+".id");

					addTabellaDomini = true;
				}

				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_DOMINIO.COD_DOMINIO, true) + " = ? ");
			} 

			if(this.listaIdTributi != null && this.listaIdTributi.size() > 0){
				this.listaIdTributi.removeAll(Collections.singleton(null));

				String [] ids = this.listaIdTributi.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.listaIdTributi.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.ABILITATO, true) + ".id", false, ids );
			}

			if(this.codTributo != null && StringUtils.isNotEmpty(this.codTributo)){
				if(!addTabellaTipiTributo) {
					sqlQueryObject.addFromTable(converter.toTable(model.TIPO_TRIBUTO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.ABILITATO, true) + ".id_tipo_tributo="
							+converter.toTable(model.TIPO_TRIBUTO, true)+".id");

					addTabellaTipiTributo = true;
				}

				if(!this.searchModeEquals)
					sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.TIPO_TRIBUTO.COD_TRIBUTO, true), this.codTributo, true, true);
				else 
					sqlQueryObject.addWhereCondition(true,converter.toColumn(model.TIPO_TRIBUTO.COD_TRIBUTO, true) + " = ? ");
			}

			if(this.idIbanAccredito != null){
				sqlQueryObject.addWhereCondition(true,converter.toTable(model.ABILITATO, true) + ".id_iban_accredito" + " = ? ");
			}

			if(this.idIbanAppoggio!= null){
				sqlQueryObject.addWhereCondition(true,converter.toTable(model.ABILITATO, true) + ".id_iban_appoggio" + " = ? ");
			}

			if(this.descrizione != null && StringUtils.isNotEmpty(this.descrizione)){
				if(!addTabellaTipiTributo) {
					sqlQueryObject.addFromTable(converter.toTable(model.TIPO_TRIBUTO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.ABILITATO, true) + ".id_tipo_tributo="
							+converter.toTable(model.TIPO_TRIBUTO, true)+".id");

					addTabellaTipiTributo = true;
				}

				sqlQueryObject.addWhereLikeCondition(converter.toColumn(model.TIPO_TRIBUTO.DESCRIZIONE, true), this.descrizione, true, true);
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

		if(this.idDominio != null){
			lst.add(this.idDominio);
		}

		if(StringUtils.isNotEmpty(this.getCodDominio())){
			lst.add(this.getCodDominio());
		} 

		if(this.listaIdTributi != null && this.listaIdTributi.size() > 0){
			// do nothing
		}

		if(this.codTributo != null && StringUtils.isNotEmpty(this.codTributo)){
			if(this.searchModeEquals)
				lst.add(this.codTributo);
		}

		if(this.idIbanAccredito != null){
			lst.add(this.idIbanAccredito);
		}

		if(this.idIbanAppoggio!= null){
			lst.add(this.idIbanAppoggio);
		}

		if(this.descrizione != null && StringUtils.isNotEmpty(this.descrizione)){
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

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public List<Long> getListaIdTributi() {
		return this.listaIdTributi;
	}

	public void setListaIdTributi(List<Long> listaIdTributi) {
		this.listaIdTributi = listaIdTributi;
	}

	public Long getIdDominio() {
		return this.idDominio;
	}

	public void setIdDominio(Long idDominio) {
		this.idDominio = idDominio;
	}

	public String getCodTributo() {
		return this.codTributo;
	}

	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


	public Long getIdIbanAccredito() {
		return this.idIbanAccredito;
	}

	public void setIdIbanAccredito(Long idIbanAccredito) {
		this.idIbanAccredito = idIbanAccredito;
	}

	public Long getIdIbanAppoggio() {
		return this.idIbanAppoggio;
	}

	public void setIdIbanAppoggio(Long idIbanAppoggio) {
		this.idIbanAppoggio = idIbanAppoggio;
	}

	public boolean isSearchModeEquals() {
		return this.searchModeEquals;
	}

	public void setSearchModeEquals(boolean searchModeEquals) {
		this.searchModeEquals = searchModeEquals;
	}
}
