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
import java.util.List;
import java.util.stream.Collectors;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.openspcoop2.utils.sql.SQLQueryObjectException;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.filters.DominioFilter.SortFields;
import it.govpay.orm.Documento;
import it.govpay.orm.dao.jdbc.converter.DocumentoFieldConverter;
import it.govpay.orm.dao.jdbc.converter.VersamentoFieldConverter;
import it.govpay.orm.model.DocumentoModel;

public class DocumentoFilter extends AbstractFilter {
	
	private String codDocumento = null;
	private String descrizione = null;
	private String codApplicazione = null;
	private List<Long> idDomini;
	private String codDominio = null;
	
	private static DocumentoModel model = Documento.model();
	private DocumentoFieldConverter converter = null;

	public DocumentoFilter(IExpressionConstructor expressionConstructor) throws ServiceException {
		this(expressionConstructor,false);
	}
	
	public DocumentoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) throws ServiceException {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(model.COD_DOCUMENTO);
		this.listaFieldSimpleSearch.add(model.DESCRIZIONE);
		this.converter = new DocumentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase());
	}

	@Override
	public IExpression _toSimpleSearchExpression() throws ServiceException {
		try {
			IExpression newExpressionOr = super._toSimpleSearchExpression();
			
			if(this.idDomini != null){
				IExpression newExpressionDomini = this.newExpression();

				this.idDomini.removeAll(Collections.singleton(null));
				VersamentoFieldConverter converter = new VersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField cf = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(model.COD_DOCUMENTO));
				newExpressionDomini.in(cf, this.idDomini);
				newExpressionOr.and(newExpressionDomini);
			}
			
			if(this.codDocumento != null) {
				newExpressionOr.equals(model.COD_DOCUMENTO, this.codDocumento);
			}
			
			return newExpressionOr;
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.codDocumento != null) {
				if(addAnd)
					newExpression.and();
				newExpression.equals(model.COD_DOCUMENTO, this.codDocumento);
				addAnd = true;
			}
			
			if(this.descrizione != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(model.DESCRIZIONE, this.descrizione);
				
				addAnd = true;
			}

			if(this.idDomini != null && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				CustomField cf = new CustomField("id_dominio", Long.class, "id_dominio", converter.toTable(model));
				newExpression.in(cf, this.idDomini);
				addAnd = true;
			}
			
			if(this.codApplicazione != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(model.ID_APPLICAZIONE.COD_APPLICAZIONE, this.codApplicazione);
				addAnd = true;
			}
			
			if(this.codDominio != null){
				if(addAnd)
					newExpression.and();

				newExpression.equals(model.ID_DOMINIO.COD_DOMINIO, this.codDominio);
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
	
	public void addSortField(SortFields field, boolean asc) {
		FilterSortWrapper filterSortWrapper = new FilterSortWrapper();
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
	}
	
	@Override
	public ISQLQueryObject toWhereCondition(ISQLQueryObject sqlQueryObject) throws ServiceException {
		try {
			boolean addTabellaDomini = false;
			boolean addTabellaApplicazioni = false;
			
			if(this.codDocumento != null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.COD_DOCUMENTO, true) + " = ? ");
			}
			
			if(this.descrizione!= null) {
				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.DESCRIZIONE, true) + " = ? ");
			}

			if(this.idDomini != null && !this.idDomini.isEmpty()){
				this.idDomini.removeAll(Collections.singleton(null));
				
				String [] idsDomini = this.idDomini.stream().map(e -> e.toString()).collect(Collectors.toList()).toArray(new String[this.idDomini.size()]);
				sqlQueryObject.addWhereINCondition(converter.toTable(model.COD_DOCUMENTO, true) + ".id_dominio", false, idsDomini );
			}

			if(this.codApplicazione != null){
				if(!addTabellaApplicazioni) {
					sqlQueryObject.addFromTable(converter.toTable(model.ID_APPLICAZIONE));
					sqlQueryObject.addWhereCondition(converter.toTable(model.COD_DOCUMENTO, true) + ".id_applicazione="
							+converter.toTable(model.ID_APPLICAZIONE, true)+".id");

					addTabellaApplicazioni = true;
				}

				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_APPLICAZIONE.COD_APPLICAZIONE, true) + " = ? ");
			}
			
			if(this.codDominio != null){
				if(!addTabellaDomini) {
					sqlQueryObject.addFromTable(converter.toTable(model.ID_DOMINIO));
					sqlQueryObject.addWhereCondition(converter.toTable(model.COD_DOCUMENTO, true) + ".id_dominio="
							+converter.toTable(model.ID_DOMINIO, true)+".id");

					addTabellaDomini = true;
				}

				sqlQueryObject.addWhereCondition(true,converter.toColumn(model.ID_DOMINIO.COD_DOMINIO, true) + " = ? ");
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
		
		if(this.codDocumento != null) {
			lst.add(this.codDocumento);
		}
		
		if(this.descrizione!= null) {
			lst.add(this.descrizione);
		}

		if(this.idDomini != null && !this.idDomini.isEmpty()){
			// donothing
		}
		
		if(this.codApplicazione != null){
			lst.add(this.codApplicazione);
		}
		
		if(this.codDominio != null){
			lst.add(this.codDominio);
		}
		
		return lst.toArray(new Object[lst.size()]);
	}

	public String getCodDocumento() {
		return codDocumento;
	}

	public void setCodDocumento(String codDocumento) {
		this.codDocumento = codDocumento;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodApplicazione() {
		return codApplicazione;
	}

	public void setCodApplicazione(String codApplicazione) {
		this.codApplicazione = codApplicazione;
	}

	public List<Long> getIdDomini() {
		return idDomini;
	}

	public void setIdDomini(List<Long> idDomini) {
		this.idDomini = idDomini;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

}
