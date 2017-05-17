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

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.LikeMode;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.orm.Incasso;
import it.govpay.orm.dao.jdbc.converter.VersamentoFieldConverter;

public class IncassoFilter extends AbstractFilter{

	private List<String> codDomini;
	private Date dataInizio;
	private Date dataFine;
	private String trn;
	private String dispositivo;
	private String causale;
	private List<Long> idIncasso= null;

	public IncassoFilter(IExpressionConstructor expressionConstructor) {
		this(expressionConstructor,false);
	}

	public IncassoFilter(IExpressionConstructor expressionConstructor, boolean simpleSearch) {
		super(expressionConstructor, simpleSearch);
		this.listaFieldSimpleSearch.add(Incasso.model().TRN);
		this.listaFieldSimpleSearch.add(Incasso.model().NOME_DISPOSITIVO);
		this.listaFieldSimpleSearch.add(Incasso.model().CAUSALE);
	}

	@Override
	public IExpression _toSimpleSearchExpression() throws ServiceException {
		try {
			IExpression newExpression = super._toSimpleSearchExpression();

			if(this.codDomini != null){
				codDomini.removeAll(Collections.singleton(null));
				newExpression.and();
				newExpression.in(Incasso.model().COD_DOMINIO, this.codDomini);
			}

			return newExpression;
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public IExpression _toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;

			if(this.dataInizio != null && this.dataFine != null) {
				newExpression.between(Incasso.model().DATA_ORA_INCASSO, this.dataInizio,this.dataFine);
				addAnd = true;
			}

			if(this.idIncasso != null && !this.idIncasso.isEmpty()){
				if(addAnd)
					newExpression.and();
				VersamentoFieldConverter converter = new VersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				CustomField cf = new CustomField("id", Long.class, "id", converter.toTable(Incasso.model()));
				newExpression.in(cf, this.idIncasso);
				addAnd = true;
			}

			if(this.codDomini != null){
				codDomini.removeAll(Collections.singleton(null));
				if(addAnd)
					newExpression.and();
				newExpression.in(Incasso.model().COD_DOMINIO, this.codDomini);
				addAnd = true;
			}

			if(this.trn != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(Incasso.model().TRN, this.trn, LikeMode.ANYWHERE);
				addAnd = true;
			}

			if(this.dispositivo != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(Incasso.model().NOME_DISPOSITIVO, this.dispositivo, LikeMode.ANYWHERE);
				addAnd = true;
			}

			if(this.causale != null){
				if(addAnd)
					newExpression.and();

				newExpression.ilike(Incasso.model().CAUSALE, this.causale, LikeMode.ANYWHERE);
				addAnd = true;
			}
			
			newExpression.addOrder(Incasso.model().DATA_ORA_INCASSO, SortOrder.DESC);

			return newExpression;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	public List<String> getCodDomini() {
		return codDomini;
	}

	public void setCodDomini(List<String> codDomini) {
		this.codDomini = codDomini;
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public String getTrn() {
		return trn;
	}

	public void setTrn(String trn) {
		this.trn = trn;
	}

	public String getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}
	public List<Long> getIdIncasso() {
		return idIncasso;
	}

	public void setIdIncasso(List<Long> idIncasso) {
		this.idIncasso = idIncasso;
	}
}
