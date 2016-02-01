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
package it.govpay.bd.registro;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.Evento;

public class EventoFilter extends AbstractFilter {

	private String iuv;
	private String categoria;
	private String tipo;
	private String sottotipo;
	private String esito;
	private Date datainizio;
	private Date dataFine;
	private String codDominio;
	private List<String> codDominiAbilitati;
	private List<Long> idApplicazioni;
	
	
	public enum SortFields {
//TODO		COD_PSP, COD_FLUSSO
		}
	
	public EventoFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			
			if(this.iuv != null && StringUtils.isNotEmpty(this.iuv)) {
				newExpression.ilike(Evento.model().IUV, this.iuv, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			if(this.sottotipo != null && StringUtils.isNotEmpty(this.sottotipo)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Evento.model().SOTTOTIPO_EVENTO, this.sottotipo);
				addAnd = true;
			}
			
			if(this.categoria != null && StringUtils.isNotEmpty(this.categoria)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Evento.model().CATEGORIA_EVENTO, this.categoria);
				addAnd = true;
			}
			
			if(this.tipo != null && StringUtils.isNotEmpty(this.tipo)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Evento.model().TIPO_EVENTO, this.tipo);
				addAnd = true;
			}
			
			if(this.esito != null && StringUtils.isNotEmpty(this.esito)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Evento.model().ESITO, this.esito);
				addAnd = true;
			}
			
			if(this.codDominio != null && StringUtils.isNotEmpty(this.codDominio)) {
				if(addAnd)
					newExpression.and();
				
				newExpression.equals(Evento.model().COD_DOMINIO, this.codDominio);
				addAnd = true;
			}
			
			if(this.codDominiAbilitati != null && !this.codDominiAbilitati.isEmpty()) {
				if(addAnd)
					newExpression.and();
				
				newExpression.in(Evento.model().COD_DOMINIO, this.codDominiAbilitati);
				addAnd = true;
			}
			
			if(this.datainizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.between(Evento.model().DATA_ORA_EVENTO, this.datainizio,this.dataFine);
				addAnd = true;
			}
			
			if(this.idApplicazioni != null && !this.idApplicazioni.isEmpty()){
				if(addAnd)
					newExpression.and();
				
				newExpression.in(Evento.model().ID_APPLICAZIONE, this.idApplicazioni);
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
		
		//TODO
//		switch(field) {
//		case COD_FLUSSO: filterSortWrapper.setField(Psp.model().COD_FLUSSO);
//			break;
//		case COD_PSP: filterSortWrapper.setField(Psp.model().COD_PSP);
//			break;
//		default:
//			break;
//		}
		
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
	}

	public String getIuv() {
		return iuv;
	}

	public void setIuv(String iuv) {
		this.iuv = iuv;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getSottotipo() {
		return sottotipo;
	}

	public void setSottotipo(String sottotipo) {
		this.sottotipo = sottotipo;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
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

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public List<String> getCodDominiAbilitati() {
		return codDominiAbilitati;
	}

	public void setCodDominiAbilitati(List<String> codDominiAbilitati) {
		this.codDominiAbilitati = codDominiAbilitati;
	}

	public List<Long> getIdApplicazioni() {
		return idApplicazioni;
	}

	public void setIdApplicazioni(List<Long> idApplicazioni) {
		this.idApplicazioni = idApplicazioni;
	}
	

	 
}
