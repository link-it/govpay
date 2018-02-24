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

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.orm.Canale;

public class CanaleFilter extends AbstractFilter {
	
	private Long idPsp = null;
	private String codPsp = null;
	private Boolean abilitato = null;
	private String modello = null;
	private TipoVersamento tipoVersamento = null;

	public enum SortFields {
	}
	
	public CanaleFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}
	
	public CanaleFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(Canale.model().MODELLO_PAGAMENTO);
		this.listaFieldSimpleSearch.add(Canale.model().TIPO_VERSAMENTO);
		this.fieldAbilitato = Canale.model().ABILITATO;
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.modello != null){
				if(addAnd)
					newExpression.and();
				
				// 2. metto in and la stringa con la ragione sociale
				newExpression.equals(Canale.model().MODELLO_PAGAMENTO, this.modello);
				addAnd = true;
			}
			
			if(this.idPsp != null){
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(new CustomField("id_psp", Long.class, "id_psp", this.getTable(Canale.model())), this.idPsp);
				addAnd = true;
			}
			
			addAnd = this.setFiltroAbilitato(newExpression, addAnd);
			
			if(this.abilitato != null && this.abilitato != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Canale.model().ABILITATO, this.abilitato);
				addAnd = true;
			}
			
			if(this.tipoVersamento != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Canale.model().TIPO_VERSAMENTO, this.tipoVersamento.toString());
				addAnd = true;
			}
			
			if(this.codPsp != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Canale.model().ID_PSP.COD_PSP, this.codPsp);
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

	public Long getIdPsp() {
		return idPsp;
	}

	public void setIdPsp(Long idPsp) {
		this.idPsp = idPsp;
	}

	public Boolean getAbilitato() {
		return abilitato;
	}

	public void setAbilitato(Boolean abilitato) {
		this.abilitato = abilitato;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public TipoVersamento getTipoVersamento() {
		return tipoVersamento;
	}

	public void setTipoVersamento(TipoVersamento tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}

	public String getCodPsp() {
		return codPsp;
	}

	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}

	
	
}
